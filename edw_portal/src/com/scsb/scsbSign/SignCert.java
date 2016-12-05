package com.scsb.scsbSign;
	
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.UnsupportedEncodingException;
	import java.security.KeyStore;
	import java.security.PrivateKey;
	import java.security.cert.CertificateFactory;
	import java.util.Calendar;
	import java.util.Date;
	
	import sun.security.x509.AlgorithmId;
	import sun.security.x509.CertificateAlgorithmId;
	import sun.security.x509.CertificateIssuerName;
	import sun.security.x509.CertificateSerialNumber;
	import sun.security.x509.CertificateValidity;
	import sun.security.x509.X509CertImpl;
	import sun.security.x509.X509CertInfo;
	
	public class SignCert {
	
	private String mKeystore = "C:/tmp/keys/mykey.keystore"; // 密鎖庫路徑
	private char[] mKeystorePass = "123456".toCharArray();// 密鎖庫密碼
	private char[] mSignPrivateKeyPass = "123456".toCharArray();// 取得簽發者私鎖所需的密碼
	private String mSignCertAlias = "keytest";// 簽發者別名
	private String mSignedCert = "C:/tmp/keys/client.cer"; // 被簽證書
	private String mNewCert = "C:/tmp/keys/clientSignKey.cer"; // 簽發後的新證書全名
	private int mValidityDay = 3000; // 簽發後的新證書有效期（天）
	
	private PrivateKey mSignPrivateKey = null;// 簽發者的私鎖
	private X509CertInfo mSignCertInfo = null;// 簽發證書信息
	private X509CertInfo mSignedCertInfo = null;// 被簽證書信息
	
	public void Sign() throws Exception {
	try {
	/**
	* 證書簽名
	*/
	getSignCertInfo(); // 獲取簽名證書信息
	signCertificate(); // 用簽名證書信息簽發待簽名證書
	createNewCertificate(); // 創建並保存簽名後的新證書
	} catch (Exception e) {
	System.out.println("Error:" + e.getMessage());
	}
	}
	
	/**
	* 取得簽名證書信息
	*
	* @throws Exception
	*/
	private void getSignCertInfo() throws Exception {
	FileInputStream vFin = null;
	KeyStore vKeyStore = null;
	java.security.cert.Certificate vCert = null;
	X509CertImpl vCertImpl = null;
	byte[] vCertData = null;
	
	// 獲取簽名證書密鎖庫
	vFin = new FileInputStream(mKeystore);
	vKeyStore = KeyStore.getInstance("JKS");
	vKeyStore.load(vFin, mKeystorePass);
	// 獲取簽名證書
	vCert = vKeyStore.getCertificate(mSignCertAlias);
	vCertData = vCert.getEncoded();
	vCertImpl = new X509CertImpl(vCertData);
	// 獲取簽名證書信息
	mSignCertInfo = (X509CertInfo) vCertImpl.get(X509CertImpl.NAME + "."
	+ X509CertImpl.INFO);
	mSignPrivateKey = (PrivateKey) vKeyStore.getKey(mSignCertAlias,
	mSignPrivateKeyPass);
	vFin.close();
	}
	
	/**
	* 取得待簽證書信息，並簽名待簽證書
	*
	* @throws Exception
	*/
	private void signCertificate() throws Exception {
	FileInputStream vFin = null;
	java.security.cert.Certificate vCert = null;
	CertificateFactory vCertFactory = null;
	byte[] vCertData = null;
	X509CertImpl vCertImpl = null;
	
	// 獲取待簽名證書
	vFin = new FileInputStream(mSignedCert);
	vCertFactory = CertificateFactory.getInstance("X.509");
	vCert = vCertFactory.generateCertificate(vFin);
	vFin.close();
	vCertData = vCert.getEncoded();
	// 設置簽名證書信息：有效日期、序列號、簽名者、數字簽名算發
	vCertImpl = new X509CertImpl(vCertData);
	mSignedCertInfo = (X509CertInfo) vCertImpl.get(X509CertImpl.NAME + "."
	+ X509CertImpl.INFO);
	mSignedCertInfo.set(X509CertInfo.VALIDITY, getCertValidity());
	mSignedCertInfo.set(X509CertInfo.SERIAL_NUMBER, getCertSerualNumber());
	mSignedCertInfo.set(
	X509CertInfo.ISSUER + "." + CertificateIssuerName.DN_NAME,
	mSignCertInfo.get(X509CertInfo.SUBJECT + "."
	+ CertificateIssuerName.DN_NAME));
	mSignedCertInfo.set(CertificateAlgorithmId.NAME + "."
	+ CertificateAlgorithmId.ALGORITHM, getAlgorithm());
	
	}
	
	/**
	* 待簽簽證書被簽名後，保存新證書
	*
	* @throws Exception
	*/
	private void createNewCertificate() throws Exception {
	FileOutputStream vOut = null;
	X509CertImpl vCertImpl = null;
	// 用新證書信息封成為新X.509證書
	vCertImpl = new X509CertImpl(mSignedCertInfo);
	// 生成新正書驗證碼
	vCertImpl.sign(mSignPrivateKey, "MD5WithRSA");
	vOut = new FileOutputStream(mNewCert);
	// 保存為der編碼二進制X.509格式證書
	vCertImpl.derEncode(vOut);
	vOut.close();
	
	}
	
	// 輔助方法===========================================================================
	
	/**
	* 得到新證書有效日期
	*
	* @throws Exception
	* @return CertificateValidity
	*/
	private CertificateValidity getCertValidity() throws Exception {
	long vValidity = (60 * 60 * 24 * 1000L) * mValidityDay;
	Calendar vCal = null;
	Date vBeginDate = null, vEndDate = null;
	vCal = Calendar.getInstance();
	vBeginDate = vCal.getTime();
	vEndDate = vCal.getTime();
	vEndDate.setTime(vBeginDate.getTime() + vValidity);
	return new CertificateValidity(vBeginDate, vEndDate);
	}
	
	/**
	* 得到新證書的序列號
	*
	* @return CertificateSerialNumber
	*/
	private CertificateSerialNumber getCertSerualNumber() {
	Calendar vCal = null;
	vCal = Calendar.getInstance();
	int vSerialNum = 0;
	vSerialNum = (int) (vCal.getTimeInMillis() / 1000);
	return new CertificateSerialNumber(vSerialNum);
	}
	
	/**
	* 得到新證書的簽名算法
	*
	* @return AlgorithmId
	*/
	private AlgorithmId getAlgorithm() {
	AlgorithmId vAlgorithm = new AlgorithmId(
	AlgorithmId.md5WithRSAEncryption_oid);
	return vAlgorithm;
	}
	
	public static void main(String args[]) throws UnsupportedEncodingException {
	SignCert s = new SignCert();
	try {
	s.Sign();
	} catch (Exception e) {
	e.printStackTrace();
	}
	}
	
	}