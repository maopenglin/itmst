package com.cmstop;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CertificateTester {
	private static final String KEY_STORE_NAME = "cmstop.keystore";
    private static final String CERTIFICATE_NAME = "cmsotp.cer";
    private static final String password = "cmsTop";
    private static final String alias = "www.cmstop.com";
    private static String certificatePath;
    private static String keyStorePath;
    
    static {
        String currentDir = CertificateTester.class.getResource("").getPath();
        if (currentDir.startsWith("/"))
            currentDir = currentDir.substring(1);
        if (!currentDir.endsWith("/")) 
            currentDir += "/";
        keyStorePath = "/"+currentDir + KEY_STORE_NAME;
        certificatePath = "/"+currentDir + CERTIFICATE_NAME;
    }
    
    public static void main(String[] args) throws Exception {
    	//Windows_Insider_Desktop_D-1920x1080-HD.png
    	String oldImage="/Users/cmstop/Desktop/jdk7/news_big_4@2x.png";
    	String outPutPath="/Users/cmstop/Desktop/jdk7/a.png";
//    	String pa750="/Users/cmstop/Desktop/jdk7/750.png";
//    	String pa1224="/Users/cmstop/Desktop/jdk7/1242.png";
//    	String pa960="/Users/cmstop/Desktop/jdk7/960.png";
    	long jpgStart = System.currentTimeMillis(); 
    	ImageHelper.resizeImage(oldImage, outPutPath, 640, 460);
//    	ImageHelper.resizeImage(oldImage, pa750, 750, 1334);
//    	ImageHelper.resizeImage(oldImage, pa1224, 1242, 2208);
//    	ImageHelper.resizeImage(oldImage, pa960, 640, 960);
    	long jpgEnd = System.currentTimeMillis();  
    	System.out.println(jpgEnd-jpgStart);
//    	String cmd ="pwd";
//		
//    	Process process = Runtime.getRuntime().exec(cmd);
//
//		// 读取标准输出流
//		BufferedReader bufferedReader = new BufferedReader(
//				new InputStreamReader(process.getInputStream()));
//		String line;
//		while ((line = bufferedReader.readLine()) != null) {
//			System.out.println(line);
//		}
//		// 读取标准错误流
//		BufferedReader brError = new BufferedReader(
//				new InputStreamReader(process.getErrorStream(),
//						"gb2312"));
//		String errline = null;
//		while ((errline = brError.readLine()) != null) {
//			System.err.println(errline);;
//		}
    	
        //simple();
        //simpleSign();
      
       // String base64="jNtylaqieE0bxZu9MxY2ZBXozVQFLdvZTXwkYBFzptL3LUZIeoE6SfvFQOZV9iO/ywFTWieO7SWuwIM/hHHbgxPiJOEDIJvrQo2Ge3ASMDaElzXGNA7DEdkXHnijSdCsq7RkD8PUP3YkmDHPBRnouQgkguZc45ua1nB/LmYvhKJGt2Y+9oYaHCY+ERYXeNvVPrOpog/tbi1k4UJlNmdMEhGI/6EP28oxRSxUnwQ9uSPYXk2laF105Szo6AFqsxy/JEr0l0ptr969GwgHTwbkDUhzOk6SR15sTBfM5QSuA1p1qGEyCMAmB7EwiM0hi4JHjSrD8Rih5oHvCiB3+auaiQ==";
       // byte[] decodedData = Cer.decryptByPublicKey(Base64Utils.decode(base64), certificatePath);

       // String target = new String(decodedData);
       // System.out.println(target);
    }

    static void simple() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        byte[] data = source.getBytes();

       
        byte[] encrypt = Cer.encryptByPublicKey(data, certificatePath);
        
        byte[] decrypt = Cer.decryptByPrivateKey(encrypt, keyStorePath, alias, password);
        String outputStr = new String(decrypt);

        System.out.println("加密前: \r\n" + source + "\r\n" + "解密后: \r\n" + outputStr);

        // 验证数据一致
       // assertArrayEquals(data, decrypt);

        // 验证证书有效
        //assertTrue(CertificateUtils.verifyCertificate(certificatePath));
    }
  public String zipPass(String s){
	  
	  byte[] decodedData=null;
	try {
		decodedData = Cer.decryptByPublicKey(Base64Utils.decode(s), certificatePath);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return new String(decodedData);
	 
  }
  public void encSourcePass(String src,String desc){
	  
	  try {
		Cer.encryptFileByPrivateKey(src, desc, keyStorePath, alias, password);
		
	System.out.println(src+"\t"+desc);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  //CER.encryptFileByPrivateKey(srcFilePath, destFilePath, keyStorePath, alias, password)
  }
  public void descZip(String src,String desc){
	  try {
		Cer.decryptFileByPublicKey(src, desc, certificatePath);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
    static void simpleSign() throws Exception {
        String source = "2121213RSDYH*JKE#%@QWA12Zdsdsds";
        byte[] data = source.getBytes();

        byte[] encodedData = Cer.encryptByPrivateKey(data, keyStorePath, alias, password);

        String s = new String(Base64Utils.encode(encodedData));
        System.err.println("s:"+s);
        //encodedData
        byte[] decodedData = Cer.decryptByPublicKey(Base64Utils.decode(s), certificatePath);

        String target = new String(decodedData);
        System.out.println("加密前: \r\n" + source + "\r\n" + "解密后: \r\n" + target);
      //  assertEquals(source, target);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = Cer.signToBase64(encodedData, keyStorePath, alias, password);
        System.out.println("签名:\r\n" + sign);
 
        // 验证签名
        boolean status = Cer.verifySign(encodedData, sign, certificatePath);
        System.err.println("状态:\r\n" + status);
       // assertTrue(status);
    }
    
  
}
