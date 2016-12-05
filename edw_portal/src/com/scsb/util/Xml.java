package com.scsb.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Xml {
	private static Logger logger = Logger.getLogger(Xml.class.getName());
	
	public Xml(){		
	}
	
	/**
	 * 複製Doc
	 * @param doc
	 * @return
	 */
	public org.w3c.dom.Document cloneDoc(Document doc){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
	        Document copiedDocument = db.newDocument();
	        Node copiedRoot = copiedDocument.importNode(doc.getDocumentElement(), true);
	        copiedDocument.appendChild(copiedRoot);
	        return copiedDocument;
		} catch (ParserConfigurationException e) {
			logger.error(StrUtil.convException(e));
		}
		return null;
	}
	
	/**
	 * 依照指定檔案路徑名稱.取回w3c格式文件
	 * @param filepath
	 * @param encoding
	 * @return
	 */
	public org.w3c.dom.Document getDocW3C(String filepath, String encoding){
		if (encoding==null || encoding.length()==0) {
			try {
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = docBuilder.parse(new FileInputStream(filepath));
				return doc;
			} catch (Exception e) {
				logger.error(StrUtil.convException(e));
			}
		} else {
			String xmlcontent = IO.read(filepath, encoding);
			try {
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        Document doc = docBuilder.parse(new InputSource(new StringReader(xmlcontent)));
				//Document doc = docBuilder.parse(new ByteArrayInputStream(xmlcontent.getBytes(encoding)));
		        return doc;
			} catch (ParserConfigurationException e) {
				logger.error(StrUtil.convException(e));
			} catch (SAXException e) {
				logger.error(StrUtil.convException(e));
			} catch (IOException e) {
				logger.error(StrUtil.convException(e));
			}
		}
		return null;
	}
	
	
	/**
	 * 取得XML檔案.依照傳入資料填入XML中,回傳Document
	 * @param filepath
	 * @param hmData
	 * @return 
	 */
	public Document setData(Document doc, HashMap<String, String> hmData){		
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();

			Iterator<Entry<String, String>> it = hmData.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = (Entry<String, String>)it.next();
				String tagpathname = entry.getKey();
				String value = entry.getValue();
				String tagname = trimForTagName(tagpathname);
				NodeList nodeList = (NodeList)xPath.evaluate(tagpathname, doc.getDocumentElement(), XPathConstants.NODESET);
				
				if (nodeList.getLength()==0) {
					Node parentNode = (Node)xPath.evaluate(trimForPathName(tagpathname), doc, XPathConstants.NODE);
					Node createNode = doc.createElement(tagname);
					createNode.setTextContent(value);
					parentNode.appendChild(createNode);
				} else {
					for (int i = 0; i < nodeList.getLength(); i++) {				 
						Node node = nodeList.item(i);
						if (tagname.equals(node.getNodeName())) {
							node.setTextContent(value);
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.error(StrUtil.convException(e));		
		}
		return doc;
	}
	
	/**
	 * 將完整TAG路徑.僅擷取該Tag名稱
	 * @param fullTagPath
	 * @return
	 */
	private String trimForTagName(String fullTagPath){
		String[] tmpTag = fullTagPath.split("/");
		String lastTag = tmpTag[tmpTag.length-1];
		return lastTag;
	}

	/**
	 * 將完整TAG路徑.僅擷取該父路徑.(不包含Tag名稱)
	 * @param fullTagPath
	 * @return
	 */
	private String trimForPathName(String fullTagPath){
		String[] tmpTag = fullTagPath.split("/");
		String lastTag = tmpTag[tmpTag.length-1];
		String elementPath = fullTagPath.replaceAll("/"+lastTag, "");
		return elementPath;
	}	
	
	/**
	 * 取得 指定XML檔案中.符合指定路徑的資料值
	 * @param filepath
	 * @param tagpath
	 * @return
	 */
	public String getTagValue(String filepath, String tagpath){
		String rtnValue = "";
		File fSendTemplate = new File(filepath);
		Document doc = getDocW3C(fSendTemplate.toString(),"utf-8"); 
		
		try {			
			String elementPath = trimForPathName(tagpath);
			String tagname = trimForTagName(tagpath);
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList)xPath.evaluate(elementPath, doc.getDocumentElement(), XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Element innerNode = (Element)nodeList.item(i);
				if(innerNode.getNodeType()== Node.ELEMENT_NODE){
                    Element innerElement = (Element) innerNode;    
                    rtnValue = getTagValue(tagname,innerElement);
				}
			}		
		} catch (XPathExpressionException e) {
			logger.error(StrUtil.convException(e));
		}
		return rtnValue;
		
	}
	
	/**
	 * 取得 指定XML中.符合指定路徑的資料值
	 * @param doc
	 * @param tagpath
	 * @return
	 */
	public String getTagValue(Document doc, String tagpath){
		String rtnValue = "";
		
		try {			
			String elementPath = trimForPathName(tagpath);
			String tagname = trimForTagName(tagpath);
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList)xPath.evaluate(elementPath, doc.getDocumentElement(), XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Element innerNode = (Element)nodeList.item(i);
				if(innerNode.getNodeType()== Node.ELEMENT_NODE){
                    Element innerElement = (Element) innerNode;    
                    rtnValue = getTagValue(tagname,innerElement);
				}
			}		
		} catch (XPathExpressionException e) {
			logger.error(StrUtil.convException(e));
		}
		return rtnValue;
		
	}	
	
	/**
	 * 取得該Element下.符合指定名稱的第一個資料值
	 * @param sTag 
	 * @param eElement
	 * @return
	 */
	private String getTagValue(String sTag, Element eElement) {
		if (eElement.getElementsByTagName(sTag)!=null && eElement.getElementsByTagName(sTag).item(0)!=null) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		    Node nValue = (Node) nlList.item(0);
		    if (nValue==null)
		    	return "";
		    else 
		    	return nValue.getNodeValue();
		} else {
			return "";
		}
	}
	
	/**
	 * 將Document寫成檔案
	 * @param doc org.w3c.dom.Document 物件
	 * @param filepath 產生檔案全路徑
	 * @return
	 */
	public String writeToFile(Document doc, String filepath, String encode){
		String rtnValue = "";
		String enconding = encode;
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, enconding);
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);			
		} catch (TransformerConfigurationException e) {
			logger.error(StrUtil.convException(e));
			rtnValue = e.getMessage();
		} catch (TransformerException e) {
			logger.error(StrUtil.convException(e));
			rtnValue = e.getMessage();
		}
		return rtnValue;
	}
	
    /**
     * 取得XML文件的定義資料
     * @param doc
     * @return
     */
	private String getXMLHeader(Document doc) { 
        StringBuffer header = new StringBuffer();
        String encoding = doc.getXmlEncoding(); 
        String version = doc.getXmlVersion();
        boolean standalone = doc.getXmlStandalone();
        if (version == null || encoding == null) return ""; 
        header.append("<?xml");
        header.append(" version=\"" + version + "\"");
        header.append(" encoding=\"" + encoding + "\"");
        header.append(standalone == false ? "" : " standalone=\"true\"");
        header.append("?>\n");
        return header.toString();
    }
    
    /**
     * 取得DocumentType訊息。先忽略對Entity等的解析
     * @param doc
     * @return
     */
    private String getDocumentType(Document doc) { 
        StringBuffer sb = new StringBuffer();
        DocumentType type = doc.getDoctype();
        if (type!=null) {
        	String name = type.getName();
        	String publicId = type.getPublicId();
        	String systemId = type.getSystemId();
        	if (name == null) return "";
        
        	sb.append("<!DOCTYPE " + name);
        	sb.append(publicId == null ? "" : " PUBLIC \"" + publicId + "\"");
        	sb.append(systemId == null ? "" : " \"" + systemId + "\"");
        	sb.append(">\n");
        }
        return sb.toString();
    }    
	
    /**
     * 取得Element的所有屬性
     * @param node
     * @return
     */
    private String getElementAttrs(Node node) { 
        StringBuffer attrs = new StringBuffer();
        if (node.hasChildNodes()) {
            NamedNodeMap map = node.getAttributes();
            for (int i = 0; i < map.getLength(); i++) {
                Node attr = map.item(i);
                attrs.append(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");    
            }   
        }  
        return attrs.toString(); 
    }
    
    /**
     * 解譯NodeList中節點.併入字串中
     * @param list
     * @param stringbuffer
     * @return
     */
    private StringBuffer parseChild(NodeList list, StringBuffer sb) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.hasChildNodes()) { //如果該節點有子節點.表示為Element
                String nodeName = node.getNodeName();
                sb.append("<" + nodeName + this.getElementAttrs(node) + ">"); //取得該Element所有屬性
                sb = this.parseChild(node.getChildNodes(), sb); //
                sb.append("</" + nodeName + ">");
            } else if (node.getNodeType() == Node.COMMENT_NODE) { //檢查非Element是否為註釋
                sb.append("<!--" + node.getNodeValue() + "-->");
            } else {
            	String nodeValue=node.getNodeValue();
            	if (nodeValue != null){
            		nodeValue=nodeValue.replaceAll("&","&amp;");
            		nodeValue=nodeValue.replaceAll("'","&apos;");
            		nodeValue=nodeValue.replaceAll("<","&lt;");
            		nodeValue=nodeValue.replaceAll(">","&gt;");
            		nodeValue=nodeValue.replaceAll("\"","&quot;");
            	}
                sb.append(nodeValue);
            } 
        }   
        return sb;
    } 
    
    /**
     * 讀取XML檔案.轉為String
     * @param xmlFile 檔案全路徑
     * @return
     */
    public String converXmlFile2Str(String xmlFile) { 
        StringBuffer sb = new StringBuffer("");
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(xmlFile); 
            sb.append(convertDoc2Str(doc));
            /*
            sb.append(this.getXMLHeader(doc)); //取得XML定義
            sb.append(this.getDocumentType(doc)); //取得DocumentType訊息
            
            org.w3c.dom.Element rootElement = doc.getDocumentElement(); //取得根節點
            String rootTagName = rootElement.getTagName();
            sb.append("<" + rootTagName + this.getElementAttrs(rootElement) + ">");
            if (rootElement.hasChildNodes()) { 
                sb = this.parseChild(rootElement.getChildNodes(), sb);   
            }
            sb.append("</" + rootTagName + ">");
            */
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            logger.error(StrUtil.convException(e));
        } catch (org.xml.sax.SAXException e) {
        	logger.error(StrUtil.convException(e));
        } catch (java.io.IOException e) {
        	logger.error(StrUtil.convException(e));
        }
        return sb.toString();   
    }
    
    /**
     * 讀取XML檔案部份節點.轉為String
     * @param xmlFile 檔案全路徑
     * @return
     */
    public String converXmlNode2Str(NodeList nList) { 
        StringBuffer sb = new StringBuffer("");
        sb=parseChild(nList ,sb);
        return sb.toString();   
    }    
    
    /**
     * 顯示XML內容
     * @param doc
     * @return
     */
    public String convertDoc2Str(Document doc) {    	
    	String xmlstr = "";
    	TransformerFactory transFactory = TransformerFactory.newInstance();    	
    	try {
        	Transformer transformer = transFactory.newTransformer();
        	StringWriter buffer = new StringWriter();
        	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        	transformer.transform(new DOMSource(doc), new StreamResult(buffer));
        	xmlstr = buffer.toString();
        	//logger.info("將XMLDOC轉為字串顯示:"+System.getProperty("line.separator")+xmlstr);
    	} catch (TransformerConfigurationException e) {
    		logger.error(StrUtil.convException(e));
		} catch (TransformerException e) {
			logger.error(StrUtil.convException(e));
		}
    	return xmlstr;
    }
    
	public String printDOMTree(Node node) {
		return printDOMTree(node, null);
	}
	
    /**
     * 印出整個結構樹
     * @param node
     * @param strTree 
     * @return
     */
	private String printDOMTree(Node node, String strTree) {
		int type = node.getNodeType();
		String tagstr = strTree;
		boolean printFlag = false;
		if (tagstr == null) {
			printFlag = true;
		} 
		
		switch (type) {
			// print the document element
			case Node.DOCUMENT_NODE: {
				tagstr = "<?xml version=\"1.0\" ?>";
				tagstr = tagstr + printDOMTree(((Document) node).getDocumentElement(), "");
				break;
			}
	
			// print element with attributes
			case Node.ELEMENT_NODE: {
				tagstr = tagstr + "<" + node.getNodeName();
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					tagstr = tagstr +" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"";
				}
				tagstr = tagstr + ">";
	
				NodeList children = node.getChildNodes();
				if (children != null) {
					int len = children.getLength();
					for (int i = 0; i < len; i++)
						tagstr = tagstr + printDOMTree(children.item(i), "");
				}
	
				break;
			}
	
			// handle entity reference nodes
			case Node.ENTITY_REFERENCE_NODE: {
				tagstr = tagstr +"&"+node.getNodeName()+";";
				break;
			}
	
			// print cdata sections
			case Node.CDATA_SECTION_NODE: {
				tagstr = tagstr + "<![CDATA["+node.getNodeValue()+"]]>" + System.getProperty("line.separator");
				break;
			}
	
			// print text
			case Node.TEXT_NODE: {
				tagstr = tagstr + node.getNodeValue();
				break;
			}
	
			// print processing instruction
			case Node.PROCESSING_INSTRUCTION_NODE: {
				tagstr = tagstr + "<?"+node.getNodeName()+ node.getNodeValue()+"?>" ;
				break;
			}
		}
		
		if (type == Node.ELEMENT_NODE) {
			tagstr = tagstr + "</"+node.getNodeName()+">"+ System.getProperty("line.separator");
		}
		
		if (printFlag) // && tagstr.startsWith("<")
			logger.info(tagstr);
		return tagstr;
	}
	
	
	/**
	 * 將XML字串轉為Document
	 * 此種方式.當碰上XML tag只有單邊的(沒有value).會有錯誤
	 * ex:<IS_LOST desc="身分證一年內掛失紀錄"/>
	 * @param xml
	 * @return
	 */
	public Document loadXMLFromString(String xml)  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(xml.getBytes()));			
		} catch (ParserConfigurationException e) {
			logger.error(StrUtil.convException(e));
		} catch (SAXException e) {
			logger.error(StrUtil.convException(e));
		} catch (IOException e) {
			logger.error(StrUtil.convException(e));
		}
		return null;
	}
	
	/**
	 * 驗證XML與XSD是否符合定義
	 * @param xsdpath 傳入XSD Schema格式
	 * @param xmlpath 要被檢核的XML檔案
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean validate(String xsdpath, String xmlpath) {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			FileInputStream fileinputstream = new FileInputStream(xsdpath);
			Source schemaSource = new StreamSource(fileinputstream);
			schema = factory.newSchema(schemaSource);

			String input = IO.read(xmlpath);
			ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes("UTF-8"));

			Validator validator = schema.newValidator();
			Source source = new StreamSource(bais);

			try {
				validator.validate(source);
				logger.info("XML檢核通過");
				return true;
			} catch (Exception e) {
				logger.error(StrUtil.convException(e));
				return false;
			}
		} catch (FileNotFoundException e) {
			logger.error(StrUtil.convException(e));
		} catch (SAXException e) {
			logger.error(StrUtil.convException(e));
		} catch (UnsupportedEncodingException e) {
			logger.error(StrUtil.convException(e));
		}
		return false;
	}	
	
	public Document setAttribute(Document doc, String tagPath, String attName, String attValue){
		Document rtnDoc = cloneDoc(doc);
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			Node node = (Node)xPath.evaluate(trimForPathName(tagPath), rtnDoc, XPathConstants.NODE);
			NamedNodeMap attributes = node.getAttributes();
		    Node attNode = node.getOwnerDocument().createAttribute(attName);
		    attNode.setNodeValue(attValue);
		    attributes.setNamedItem(attNode);
		} catch (XPathExpressionException e) {
			logger.error(StrUtil.convException(e));		
		}
		return rtnDoc;
	}
	
	public static void main(String[] args){
		Xml xml = new Xml();
		//xml.testJcic();
		//org.jdom.Document strDoc = xml.getDocJDOM("C:/TEMP/sc/send/xml/201304120009.xml");
		//Document doc = xml.getDocW3C(strDoc);
		//String xx = xml.getTagValue(doc, "/client-response/response-result/status-code");
		//doc = xml.setAttribute(doc,"client-query", "MsgName", "34071");
		//xml.printDOMTree(doc);
	}
}
