package com.scsb.scsbSign;
 
import java.security.*;
import com.scsb.scsbEncrypt.EncrypRSA;
 
public class GenSignVerify {
 
	public GenSignVerify(){
	}
	
	/**
	 * 產生數位簽章
	 * @param signData
	 * @param key
	 * @param signAlg[SHA256withRSA / SHA1withDSA / MD2withRSA /MD5withRSA /SHA1withRSA]
	 * @return
	 */
	public byte[] Sign(byte[] signData ,PrivateKey key ,String signAlg){
        Signature signature=null;
		try {
			//SignAlg : SHA256withRSA / SHA1withDSA / MD2withRSA /MD5withRSA /SHA1withRSA
			signature = Signature.getInstance(signAlg);
			signature.initSign(key);
            /* Update and sign the data */
            signature.update(signData);
            /* generate a signature */
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (SignatureException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 數位簽章進行驗章
	 * @param signData
	 * @param sigToVerify[Signature]
	 * @param key
	 * @param signAlg[SHA256withRSA / SHA1withDSA / MD2withRSA /MD5withRSA /SHA1withRSA]
	 * @return
	 */
	public boolean Verify(byte[] signData ,byte[] sigToVerify ,PublicKey key ,String signAlg){
		
        Signature signature=null;
		try {
			//SignAlg : SHA256withRSA / SHA1withDSA / MD2withRSA /MD5withRSA /SHA1withRSA
			signature = Signature.getInstance(signAlg);
			signature.initVerify(key);
            /* Update and sign the data */
            signature.update(signData);
            /* generate a signature */
			return signature.verify(sigToVerify);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return false;
		} catch (SignatureException e) {
			e.printStackTrace();
			return false;
		}
	}	
	
    public static void main(String[] args) {
       		EncrypRSA rsa = new EncrypRSA();
       		rsa.generateKey(2048 ,"SCSB");
       		GenSignVerify genSig =new GenSignVerify();
       		String signData="SCSB_SIGN_TEST";
       		// Generate a signature
        	byte[] realSig = genSig.Sign(signData.getBytes(),rsa.getRSAPrivateKey(),"SHA256withRSA");
        	
        	//des signature
        	boolean isSign = genSig.Verify(signData.getBytes() ,realSig ,rsa.getRSAPublicKey(),"SHA256withRSA");
        	
            System.out.println("簽章明文是:" + signData);
            System.out.println("簽章加密後是:" + new String(realSig));
            System.out.println("簽章驗證結果:" + isSign);
    };
 
}