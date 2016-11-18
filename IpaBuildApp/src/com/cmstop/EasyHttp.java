package com.cmstop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class EasyHttp {

	private static int TIMEOUT = 1000000;  
	  public static void main(String[] args) {
		  HashMap<String, String> map=new HashMap<String, String>();
		  map.put("status", "0");
		String result=EasyHttp.post("http://www.baidu.com", map);
		System.out.println("ios:"+result);
	}
    // post请求  
    public static String post(String url, Map<String, String> paramMap) {  
        HttpURLConnection conn = null;  
        String resultStr = null; 
        Logger log = Logger.getLogger(EasyHttp.class);
       
        try {  
            URL urlA = new URL(url);  
            conn = (HttpURLConnection) urlA.openConnection();  
            conn.setConnectTimeout(TIMEOUT);// 连接超时 单位毫秒  
            conn.setReadTimeout(TIMEOUT);// 读取超时 单位毫秒  
            // 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true  
            conn.setDoOutput(true);  
            // Read from the connection. Default is true.  
            conn.setDoInput(true);  
            // Set the post method. Default is GET  
            conn.setRequestMethod("POST");  
            // Post 请求不能使用缓存  
            conn.setUseCaches(false);  
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的  
//            conn.setRequestProperty("Content-Type",  
//                    "application/x-www-form-urlencoded");  
  
            String paramStr = mapToString(paramMap);
            log.info("ios    "+url+"?"+paramStr);
            System.out.println("paramStr:"+paramStr);
            byte[] bypes = paramStr.getBytes("UTF-8");  
            conn.connect();  
            OutputStream out = conn.getOutputStream();  
            out.write(bypes);// 输入参数  
            out.flush();  
            out.close();  
            resultStr = returnFromUrl(conn);  
        } catch (Exception e) { 
        	log.error(e.getMessage());
            e.printStackTrace();  
        } finally {  
            conn.disconnect();  
        }  
        return resultStr;  
    }  
  
      
    // get请求  
    public static String get(String url, Map<String, String> paramMap) {  
        HttpURLConnection conn = null;  
        String resultStr = null;  
        try {  
            if (paramMap!=null) {  
                String paramStr = mapToString(paramMap);  
                // 如果不包含?  
                if (!url.contains("?")) {  
                    url = url + "?";  
                }  
                // 如果结尾不是&?  
                char lastChar = url.charAt(url.length() - 1);  
                if ('&' != lastChar && '?' != lastChar) {  
                    paramStr = "&" + paramStr;  
                }  
                url = url + paramStr;  
            }  
            URL urlA = new URL(url);  
            conn = (HttpURLConnection) urlA.openConnection();  
            conn.setConnectTimeout(TIMEOUT);// 连接超时 单位毫秒  
            conn.setReadTimeout(TIMEOUT);// 读取超时 单位毫秒  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setRequestMethod("GET");  
            conn.setUseCaches(false);  
            //conn.setRequestProperty("User-Agent", "");  
            resultStr = returnFromUrl(conn);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            conn.disconnect();  
        }  
        return resultStr;  
    }  
  
    // 从url连接中取得返回值  
    public static String returnFromUrl(HttpURLConnection conn) {  
        String result = null;  
        BufferedReader reader = null;  
        try {  
            // 取得输入流，并使用Reader读取  
            reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            StringBuffer buff = new StringBuffer();  
            String oneLine = null;  
            while ((oneLine = reader.readLine()) != null) {  
                buff.append(oneLine);  
            }  
            result = buff.toString();  
        } catch (Exception e) {  
            //e.printStackTrace();  
        } finally {  
            try {  
                reader.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
  
    // 将参数转为字符串  
    public static String mapToString(Map<String, String> map) {  
        if (map==null) {  
            return "";  
        }  
        Set<String> set = map.keySet();  
        StringBuffer buf = new StringBuffer();  
        for (String key : set) {  
            String value = map.get(key);  
            if (null != value) {  
                String valueEn = null;  
                try {  
                    valueEn = URLEncoder.encode(value, "UTF-8");  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                buf.append(key).append("=").append(valueEn).append("&");  
            }  
        }  
        buf.deleteCharAt(buf.length() - 1);  
        return buf.toString();  
    }  
}
