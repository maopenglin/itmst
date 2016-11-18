package com.cmstop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class SHA1 {
	private static String prviateKey="123";
	
	public boolean validateRequest(int identity, int siteid, int clientid,
			String appKey, String comparSign) throws CustomerException,Exception {
		System.out.println(identity+"" + siteid+"" + clientid+"" + appKey);
		String r1 = SHA1.MD5(identity+"" + siteid+"" + clientid+"" + appKey);
       System.out.println(r1);
		String xp = SHA1.class.getClassLoader().getResource("").toURI()
				.getPath();
		String path = xp + identity + ".txt";
		//System.out.println(path);
		File f = new File(path);
		if (!f.exists()) {
			throw new CustomerException("尚未配置校验证书",119);
		}
		
		String txt=this.txt2String(f);
		//System.out.println(r1 + txt);
		String r2 = SHA1.MD5(r1 +txt);
		//System.out.println(r2);
		if (r2.equals(comparSign)) {

			return true;
		}
		return false;
	}
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	public static void main(String[] args) {
		
		String str1 = "abcdefghijklmnabc"; 
	    // 从头开始查找是否存在指定的字符 
	    System.out.println(str1.indexOf("T")); 
		
     System.out.println(isNumeric("02123"));
		int identity=1;
		int siteid=10001;
		int clientid=1;
	   String secret="1220f9c9ea73d926838a3a7263c383cd";
	      
	  SHA1 sha=new SHA1();
	  try{
		boolean a=  sha.validateRequest(identity, siteid, clientid, secret, "70c7295716b876df568bc558a085ef92");
		System.out.println(a);
	  }catch (Exception e) {
		System.out.println(e.getMessage());
	}
	   
	}
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	public static String sha1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("sha1");
           
            byte[] digest = md.digest(inStr.getBytes());
           
            outStr = bytetoString(digest);
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
    }//
	
	
	public static String txt2String(File file) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result = result + s;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
		     
	public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        
        for (int i = 1; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            }
            else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
	
	 public static String[] getCryptoImpls(String serviceType) {
	        Set result = new HashSet();
	    
	        // All all providers
	        Provider[] providers = Security.getProviders();
	        for (int i=0; i<providers.length; i++) {
	            // Get services provided by each provider
	            Set keys = providers[i].keySet();
	            for (Iterator it=keys.iterator(); it.hasNext(); ) {
	                String key = (String)it.next();
	                key = key.split(" ")[0];
	    
	                if (key.startsWith(serviceType+".")) {
	                    result.add(key.substring(serviceType.length()+1));
	                } else if (key.startsWith("Alg.Alias."+serviceType+".")) {
	                    // This is an alias
	                    result.add(key.substring(serviceType.length()+11));
	                }
	            }
	        }
	        return (String[])result.toArray(new String[result.size()]);
	    }
}
