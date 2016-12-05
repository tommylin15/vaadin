package com.scsb.scsbSign;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.utils.ElementProxy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import com.scsb.scsbEncrypt.EncrypRSA;
import com.scsb.util.IO;
import com.scsb.util.StrUtil;
import com.scsb.util.Xml;
 
public class XmlSignVerify_Enveloping {
 
	public XmlSignVerify_Enveloping(){
	}
	
	/**
	 * 產生XML數位簽章
	 * @param signData
	 * @param key
	 * @param 
	 * @return
	 */
	public void XMLSign(String xmlFile ,PublicKey publickey ,PrivateKey privateKey ,String signXmlFile )throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);		
		//1.，創建DOM的XMLSignatureFactory將用於生成XMLSignature	
        String providerName = System.getProperty("jsr105Provider", "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",(Provider) Class.forName(providerName).newInstance());
        //XMLSignatureFactory fac =XMLSignatureFactory.getInstance("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
        
        
        //2.，創建一個參考同一個文檔URI是一個對象元素，並指定演算法,在SHA-1，SHA-256或SHA-512的情况下，我们可以使用null
        javax.xml.crypto.dsig.Reference ref = fac.newReference("#invoice",fac.newDigestMethod(DigestMethod.SHA256 ,null));
        
        //3.,創建要簽章的對象
        Document srcDoc = dbf.newDocumentBuilder().parse(new File(xmlFile));
        Node rootNode = srcDoc.getDocumentElement();
        XMLStructure content = new DOMStructure(rootNode);
        XMLObject obj = fac.newXMLObject(Collections.singletonList(content), "invoice", null, null);
        
        //4., Create the SignedInfo(產生SignedInfo區段)
        SignedInfo si = fac.newSignedInfo(
            fac.newCanonicalizationMethod
                (CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,(C14NMethodParameterSpec) null), 
            fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null),
            Collections.singletonList(ref));

        //5., Create a KeyValue containing the RSA PublicKey that was generated
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        //直接使用public key
        KeyValue kv = kif.newKeyValue(publickey);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
		//使用憑證檔
		//CertificateFactory cf = CertificateFactory.getInstance("X.509");
		//FileInputStream fis = new FileInputStream("d:\\encrypt\\fatca\\fatca_versign_cert.cer");
		//java.security.cert.Certificate cert = cf.generateCertificate(fis);
		//fis.close();
		//X509Data x509d = kif.newX509Data(Collections.singletonList(cert));
		//KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));		

        // Create the XMLSignature (but don't sign it yet)
        XMLSignature signature = fac.newXMLSignature(si, ki,Collections.singletonList(obj), null, null); 

        // Create a DOMSignContext and specify the RSA PrivateKey for signing
        // and the document location of the XMLSignature
        DOMSignContext dsc = new DOMSignContext(privateKey, srcDoc);

        // Lastly, generate the enveloping signature using the PrivateKey
        signature.sign(dsc);

        //轉換成xml文檔
        OutputStream os =new FileOutputStream(signXmlFile);
        //OutputStream os =System.out;
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(srcDoc), new StreamResult(os));        
	}
	
	/**
	 * 驗章
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws FileNotFoundException 
	 */	
	
	public boolean Validate(String signXmlFile) throws Exception{
		boolean retFlag=false;
		//第一步
        String providerName = System.getProperty("jsr105Provider", "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",(Provider) Class.forName(providerName).newInstance());
        //第二步
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(signXmlFile));        
        //第三步
        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS , "Signature");
        if (nl.getLength() == 0) {
        	retFlag=false;
        	throw new Exception("Cannot find Signature element!");
        }
        //第四步，分為情形4.1，4.2或4.3
        //4.1
        DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(),nl.item(0));
     	//4.2使用 keystore
        // KeyStore ks = KeyStore.getInstance("JKS");
        // FileInputStream fis = new FileInputStream("./etc/bizkeystore");
        // ks.load(fis，"sp1234".toCharArray());
        // fis.close();
        // X509KeySelector x509ks = new X509KeySelector(ks);
        // DOMValidateContext valContext = new DOMValidateContext(x509ks, nl.item(0));
        //4.3
        // PublicKey pKey = KeyStoreInfo.getPublicKey("./etc/bizkeystore","sp1234"， "biz");
		//第五步
		XMLSignature signature = fac.unmarshalXMLSignature(valContext);
		//XMLSignature signature = fac.unmarshalXMLSignature(new DOMStructure(nl.item(0)));
		//第六步
		boolean coreValidity = signature.validate(valContext);
		//檢查核心校驗狀態
		if (coreValidity == false) {
			System.err.println("Signature failed core validation!");
			boolean sv = signature.getSignatureValue().validate(valContext);
			System.out.println("Signature validation status: " + sv);
			//每一個Reference的檢查校驗狀態
			Iterator i = signature.getSignedInfo().getReferences().iterator();
			for (int j = 0; i.hasNext(); j++) {
					boolean refValid = ((Reference) i.next()).validate(valContext);
					System.out.println("Reference (" + j + ") validation status: "+ refValid);
			}
			retFlag=false;
		} else {
			System.out.println("Signature passed core validation!");
			retFlag=true;
		}
        return retFlag;
	}	
	
	/**
	 * 拆解簽章的xml
	 * @param filepath
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public org.w3c.dom.Document getData(String filepath, String encoding ) throws Exception{
		Xml xml=new Xml();
		org.w3c.dom.Document doc=xml.getDocW3C(filepath, encoding);
		
		NodeList objList = doc.getElementsByTagName("Object");
		if (objList.hashCode() > 0){
            Node objNode = objList.item(0);
            NodeList dataList =objNode.getChildNodes();            
            if (dataList.hashCode() > 0){
            	String sb = xml.converXmlNode2Str(dataList);
            	//System.out.println(sb);
            	doc =xml.loadXMLFromString(sb);
            }
		}
		return doc;
	}
	
	
	public void XMLSignX509(String xmlFile ,String SCSB_CA_CER ,PrivateKey privateKey ,String signXmlFile )throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);		
		//1.，創建DOM的XMLSignatureFactory將用於生成XMLSignature	
        String providerName = System.getProperty("jsr105Provider", "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",(Provider) Class.forName(providerName).newInstance());
        
        //2.，創建一個參考同一個文檔URI是一個對象元素，並指定演算法,在SHA-1，SHA-256或SHA-512的情况下，我们可以使用null
        javax.xml.crypto.dsig.Reference ref = fac.newReference("#invoice",fac.newDigestMethod(DigestMethod.SHA256 ,null));
        
        //3.,創建要簽章的對象
        Document srcDoc = dbf.newDocumentBuilder().parse(new File(xmlFile));
        Node rootNode = srcDoc.getDocumentElement();
        XMLStructure content = new DOMStructure(rootNode);
        XMLObject obj = fac.newXMLObject(Collections.singletonList(content), "invoice", null, null);

        //4., Create the SignedInfo(產生SignedInfo區段)
        String signatureMethod ="http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        SignedInfo si = fac.newSignedInfo(
            fac.newCanonicalizationMethod
                (CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,(C14NMethodParameterSpec) null), 
            fac.newSignatureMethod(signatureMethod, null),
            Collections.singletonList(ref));

        //5., Create a KeyValue containing the RSA PublicKey that was generated
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        //直接使用public key
        //KeyValue kv = kif.newKeyValue(publickey);
        //KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
		//使用憑證檔
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream fis = new FileInputStream(SCSB_CA_CER);
		java.security.cert.Certificate cert = cf.generateCertificate(fis);
		fis.close();
		
        X509Certificate x509cert = (X509Certificate) cert;
        x509cert.checkValidity();
        //get subjectDn
        Principal principal = x509cert.getSubjectDN();
        String subjectDn = principal.getName();
        
		List x509List = new ArrayList();
		//x509List.add(subjectDn);
		x509List.add(cert);
		
		X509Data x509Data = kif.newX509Data(x509List);
		
		KeyInfo ki = kif.newKeyInfo(Arrays.asList(x509Data));

        // Create the XMLSignature (but don't sign it yet)
        XMLSignature signature = fac.newXMLSignature(si, ki,Collections.singletonList(obj), null, null); 

        // Create a DOMSignContext and specify the RSA PrivateKey for signing
        // and the document location of the XMLSignature
        DOMSignContext dsc = new DOMSignContext(privateKey, srcDoc);

        // Lastly, generate the enveloping signature using the PrivateKey
        signature.sign(dsc);

        //轉換成xml文檔
        OutputStream os =new FileOutputStream(signXmlFile);
        //OutputStream os =System.out;
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(srcDoc), new StreamResult(os));        
	}	
	
	/**
	 * 驗章(X509DATA<X509SubjectName><X509Certificate> )
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws FileNotFoundException 
	 */    
    public boolean ValidateX509(String signedFile) throws Exception {
    	boolean retFlag=false;
        String providerName = System.getProperty("jsr105Provider", "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",(Provider) Class.forName(providerName).newInstance());
    	
    	  // Parse the signed XML document to unmarshal <Signature> object.
    	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	  dbf.setNamespaceAware(true);
    	  Document doc = dbf.newDocumentBuilder().parse(
    	    new FileInputStream(signedFile));
    	  // Search the Signature element
    	  NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
    	  if (nl.getLength() == 0) {
    	   throw new Exception("Cannot find Signature element");
    	  }
    	  Node signatureNode = nl.item(0);
    	  //XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
    	  XMLSignature signature = fac.unmarshalXMLSignature(new DOMStructure(
    	    signatureNode));
    	  javax.xml.crypto.dsig.keyinfo.X509Data data=(javax.xml.crypto.dsig.keyinfo.X509Data)signature.getKeyInfo().getContent().get(0);
    	  // Get the public key for signature validation
    	//  KeyValue keyValue = (KeyValue) signature.getKeyInfo().getContent().get(
//    	    0);
    	  List ls=data.getContent();
    	  X509CertImpl cert=(X509CertImpl)ls.get(0);
    	  PublicKey pubKey=cert.getPublicKey() ;//= data.getPublicKey();
    	 
    	  // Create ValidateContext
    	  DOMValidateContext valCtx = new DOMValidateContext(pubKey,
    	    signatureNode);
    	  // Validate the XMLSignature
    	  boolean coreValidity = signature.validate(valCtx);
    	  // Check core validation status
    	  if (coreValidity == false) {
    	   System.err.println("Core validation failed");
    	   // Check the signature validation status
    	   boolean sv = signature.getSignatureValue().validate(valCtx);
    	   System.out.println("Signature validation status: " + sv);
    	   // check the validation status of each Reference
    	   List refs = signature.getSignedInfo().getReferences();
    	   for (int i = 0; i < refs.size(); i++) {
    	    Reference ref = (Reference) refs.get(i);
    	    boolean refValid = ref.validate(valCtx);
    	    System.out.println("Reference[" + i + "] validity status: "
    	      + refValid);
    	    retFlag=false;
    	   }
    	  } else {
    		  System.out.println("Signature passed core validation");
    		  retFlag=true;
    	  }
    	  return retFlag;
    }	
	
    public static void main(String[] args) throws Exception {
    	/*
   		EncrypRSA rsa = new EncrypRSA();
   		rsa.generateKey(2048 ,"SCSB");
   		//PublicKey publicKey =rsa.getRSAPublicKey();
   		//PrivateKey privateKey =rsa.getRSAPrivateKey();
   		XmlSignVerify_Enveloping xmlSign =new XmlSignVerify_Enveloping();
   		//String signData="SCSB_SIGN_TEST";
   		// Generate a signature
    	xmlSign.XMLSign("d:/encrypt/fatca/fatca.xml",rsa.getRSAPublicKey(),rsa.getRSAPrivateKey() ,"D:\\encrypt\\fatca\\signOut.xml");
    	*/
    	
    	//XmlSignVerify_Enveloping xmlSign =new XmlSignVerify_Enveloping();
    	//xmlSign.getData("D:/encrypt/fatca/irs/temp/J6NT1S.00000.LE.158_Parload.xml", "utf-8");
    }
    
    /**
     * KeySelector which retrieves the public key out of the
     * KeyValue element and returns it.
     * NOTE: If the key algorithm doesn't match signature algorithm,
     * then the public key will be ignored.
     * KeySelectors以檢索公鑰出來元素的KeyValues並返回。
     * 注意：如果密鑰算法不匹配的簽名算法公共密鑰將被忽略。   
     */
    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                                        KeySelector.Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
            throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (int i = 0; i < list.size(); i++) {
                XMLStructure xmlStructure = (XMLStructure) list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue)xmlStructure).getPublicKey();                        
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        //@@@FIXME: this should also work for key types other than DSA/RSA
        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("EC") &&
                algURI.equalsIgnoreCase("http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256")) {
                return true;
            } else {
                return false;
            }
        }
    }    
    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;
        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        public Key getKey() { return pk; }
    }  
    
     
}

