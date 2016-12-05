package com.scsb.vaadin.ui.rc.temp;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.scsb.util.AsianFontProvider;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.OutputStream; 
 
/**
 * <p/> 
 * PDFRenderer supports headless rendering of XHTML documents, outputting 
 * to PDF format. There are two static utility methods, one for rendering 
 * a {@link java.net.URL}, {@link #renderToPDF(String, String)} and one 
 * for rendering a {@link File}, {@link #renderToPDF(File, String)}</p> 
 * <p>You can use this utility from the command line by passing in 
 * the URL or file location as first parameter, and PDF path as second 
 * parameter: 
 * <pre> 
 * java -cp %classpath% org.xhtmlrenderer.simple.PDFRenderer <url> <pdf> 
 * </pre> 
 * 
 * @author Pete Brant 
 * @author Patrick Wright 
 */ 
public class PDFRenderer { 

    public static void renderToPDF(String url, String pdf)   throws IOException{ 
   
    	url="http://sas.scsb.com.tw/cgi-bin/broker.exe?_service=default&_program=ibspgm.rpt_mail.sas&_debug=0&xpgn=noCheck&xtype=var8&xquery=0034711&xsystem=SAS01_20160506";
    	
    	HttpClient httpClient = new HttpClient();
    	GetMethod get = new GetMethod(url);
    	httpClient.executeMethod(get);
        byte[] responseBody = get.getResponseBody();    	
        String result =new String(responseBody,"big5");
	   
    	org.jsoup.nodes.Document doc = Jsoup.parse(result);
    	// Clean the document.
    	doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
    	doc.outputSettings().prettyPrint(true);

    	doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
    	// Get back the string of the body.
    	 String xhtml = doc.html();
    	 ByteArrayInputStream htmlbis =new ByteArrayInputStream(xhtml.getBytes());
    	 
    	 OutputStream os =new FileOutputStream(new File("d://test.pdf"));
		try {
	     	// step 1
	     	Document document = new Document(PageSize.A3);
	     	// step 2
	     	PdfWriter writer = PdfWriter.getInstance(document, os);
	     	writer.setViewerPreferences(PdfWriter.HideToolbar);
	     	// step 3
	     	document.open();
	     	// step 4
	    	// CSS
	     	AsianFontProvider fontProvider =new AsianFontProvider();
	     	fontProvider.addFontSubstitute("lowagie", "garamond");
	     	fontProvider.setUseUnicode(true);
	     	
	     	CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
	     	HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
	     	htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	     	
	     	CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
	        PdfWriterPipeline xpdf = new PdfWriterPipeline(document, writer);
	        HtmlPipeline html = new HtmlPipeline(htmlContext, xpdf);
	        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
	        XMLWorker worker = new XMLWorker(css, true);
	        XMLParser p = new XMLParser(worker);
	        p.parse(htmlbis);
	     	//step 5
	     	document.close();    
	     	os.close();
	     	//加浮水印
	        PdfReader pdfReader = new PdfReader("d://test.pdf");
	        // Get the PdfStamper object
	        PdfStamper pdfStamper = new PdfStamper(pdfReader  , new FileOutputStream("d://test2.pdf"));
	        addWatermark(pdfStamper, "上海商業儲蓄銀行");
	        pdfStamper.close(); 	     	
		} catch (com.itextpdf.text.DocumentException e1) {
			e1.printStackTrace();
		}
    } 
    
    private static void addWatermark(PdfStamper pdfStamper    , String waterMarkName) {
              PdfContentByte content = null;
              BaseFont base = null;
              Rectangle pageRect = null;
              PdfGState gs = new PdfGState();
              try {
                  // 設定字體
            	  String fontPath =  "Font\\scsbeudc.ttf";
            	  //base = BaseFont.createFont("MSung-Light","UniCNS-UCS2-H",  BaseFont.NOT_EMBEDDED);
            	  base     =BaseFont.createFont(fontPath,BaseFont.IDENTITY_H,true);
              } catch (IOException e) {
                  e.printStackTrace();
              } catch (com.itextpdf.text.DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              try {
                  if (base == null || pdfStamper == null) {
                      return;
                  }
                  //設定透明度0.4
                  gs.setFillOpacity(0.4f);
                  gs.setStrokeOpacity(0.4f);
                  int toPage = pdfStamper.getReader().getNumberOfPages();
                  for (int i = 1; i <= toPage; i++) {
                      pageRect = pdfStamper.getReader().
                         getPageSizeWithRotation(i);
                      //計算浮水印xy坐標
                      float x = pageRect.getWidth() / 2;
                      float y = pageRect.getHeight() / 2;
                      //取得pdf最上層
                      content = pdfStamper.getOverContent(i);
                      content.saveState();
                      // set Transparency
                      content.setGState(gs);
                      content.beginText();
                      content.setColorFill(BaseColor.GRAY);
                      content.setFontAndSize(base, 60);
                      //設定浮水印成45度角
                      content.showTextAligned(Element.ALIGN_CENTER  , waterMarkName, x,  y, 45);
                      content.endText(); 
                  }
              } catch (Exception ex) {
                  ex.printStackTrace();
              } finally {
                  content = null;
                  base = null;
                  pageRect = null;
              }
          }    
    
    public static void main(String[] args) throws IOException { 
    	renderToPDF("", "");
    } 
 
}