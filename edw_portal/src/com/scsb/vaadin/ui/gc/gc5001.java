package com.scsb.vaadin.ui.gc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;

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
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.scsb.db.bean.MsgUrl;
import com.scsb.db.service.MsgUrlService;
import com.scsb.util.AsianFontProvider;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 預覽 msg_url的資訊
 * @author 34711
 *
 */
@SuppressWarnings("serial")
public class gc5001  extends VerticalLayout{

    @SuppressWarnings("deprecation")
	public gc5001(String msg_code)  {
        setSizeFull();
    	
        MsgUrlService msgurlSrv =new MsgUrlService();
        MsgUrl bean =new MsgUrl();
        bean.setMsgUrlx64(msg_code);
        bean =msgurlSrv.getMsgUrl_UrlX64(bean);
        
    	final String sUrl=bean.getMsgUrl();
		Panel panel =new Panel();
		panel.setSizeFull();

        StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
        	@Override
        	public InputStream getStream() {
	        	try {
   	        		HttpClient httpClient = new HttpClient();
    	        	//PostMethod post = new PostMethod(sUrl);
    	        	GetMethod get = new GetMethod(sUrl);
    	        	httpClient.executeMethod(get);
    	    		//InputStream inStream=get.getResponseBodyAsStream();
    		        byte[] responseBody = get.getResponseBody();
    		        String result =new String(responseBody,"big5"); 	        	
    		        //本段處理html tag 合法性驗證
    		    	org.jsoup.nodes.Document doc = Jsoup.parse(result);
    		    	// Clean the document.
    		    	doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
    		    	doc.outputSettings().prettyPrint(true);	        
    		    	doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
    		    	String xhtml = doc.html();	 
    		    	
    		    	ByteArrayOutputStream pdfbos =new ByteArrayOutputStream();

		 		    //Initial HTML轉換物件
		 		    XMLWorkerHelper xmlWorker =XMLWorkerHelper.getInstance();
		 	     	// step 1
		 		   
		 	     	Document document = new Document(PageSize.A4.rotate());
		 	     	
		 	     	//Rectangle pageSize = new Rectangle(1600, 728);
		 	     	//document.setPageSize(pageSize);
		 	     	
		 	     	// step 2
		 	     	com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, pdfbos);
		 	     	writer.setViewerPreferences(PdfWriter.HideToolbar);
		 	     	// step 3
		 	     	document.open();
		 	     	// step 4
		 			//Parse HTML字串，要把自訂的FontProvider傳入
		 			xmlWorker.parseXHtml(writer, document, new ByteArrayInputStream(xhtml.getBytes("UTF-8")), null, Charset.forName("UTF-8"),  new AsianFontProvider());		   
		 	     	//step 5
		 	     	document.close();    
		 	     	pdfbos.close();
		 	     	
		 	     	//加浮水印
	                ByteArrayOutputStream waterbos =new ByteArrayOutputStream();	                
		 	     	ByteArrayInputStream pdfios =new ByteArrayInputStream(pdfbos.toByteArray());
		 	        PdfReader pdfReader = new PdfReader(pdfios);
		 	        // Get the PdfStamper object
		 	        PdfStamper pdfStamper = new PdfStamper(pdfReader  , waterbos);
		 	        addWatermark(pdfStamper, "上海商業儲蓄銀行");
		 	        pdfStamper.close();
	                return new ByteArrayInputStream(waterbos.toByteArray());
	    		} catch (HttpException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();		                  
		 		} catch (com.itextpdf.text.DocumentException e1) {
		 			e1.printStackTrace();
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
				return null;
        	}
        };
        UUID uuid = UUID.randomUUID();
        StreamResource streamResource = new StreamResource(streamSource, uuid+".pdf");
        Embedded object = new Embedded();
        object.setSizeFull();
        object.setMimeType("application/pdf");
        object.setType(Embedded.TYPE_BROWSER);
        object.setSource(streamResource);
		panel.setSizeFull();
		panel.setContent(object);
		
		this.addComponent(panel);    	
    	
		/*
		 * 1.BrowserFrame 會唯讀 
		 * 2.BrowserWindowOpener 才能進行本文選取
		 * 3.目前尚未增加Ldap驗證機制
		 **/
		/*
		BrowserFrame browser = new BrowserFrame("Browser", new ExternalResource(sUrl));
		browser.setSizeFull();

		Panel panel =new Panel();
		panel.setSizeFull();
		panel.setContent(browser);
		
		this.addComponent(panel);
		*/
		/*
	    Button downloadButton = new Button("人員驗證完畢,請按我開啟訊息");		
		final BrowserWindowOpener browserWindowOpener = new BrowserWindowOpener(new ExternalResource(sUrl) );
	    browserWindowOpener.setFeatures("location=0");
	    browserWindowOpener.extend(downloadButton);
		this.setContent(downloadButton);
	    */
    }
   
    /**
     * 浮水印
     * @param pdfStamper
     * @param waterMarkName
     */
    private static void addWatermark(PdfStamper pdfStamper    , String waterMarkName) {
        PdfContentByte content = null;
        BaseFont base = null;
        Rectangle pageRect = null;
        PdfGState gs = new PdfGState();
        try {
            // 設定字體
      	  String fontPath =  "Font\\scsbeudc.ttf";
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
                //取得pdf最上層
                content = pdfStamper.getOverContent(i);
                content.saveState();
                // set Transparency
                content.setGState(gs);
                content.beginText();
                content.setColorFill(BaseColor.GRAY);
                content.setFontAndSize(base, 60);
                
	                //計算浮水印xy坐標
	                float x = pageRect.getWidth() / 2;
	                float y = pageRect.getHeight() / 2;
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
}



