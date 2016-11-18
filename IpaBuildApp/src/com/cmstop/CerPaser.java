package com.cmstop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CerPaser {
   
	public static void main(String[] args) {

		String sid="55PV479U94.hz.aheading.mobile.inewsread.dongguanrb";
		String tid="hz.aheading.mobile.inewsread.dongguanrb";
		if(sid.endsWith(tid)){
			System.out.println("yes");
		}else{
			System.out.println("no");
		}
		
	}
	//cer 证书安装
	public String installCer(String cerPath){
		 //loginKeyChain
		
		
		
		
		try{
			
			Process process = null;
			Runtime rt = Runtime.getRuntime();
			Properties prop2 = new Properties();
			String xp = XSSCheckFilter.class.getClassLoader()
					.getResource("").toURI().getPath();
			FileInputStream fis2 = new FileInputStream(new File(xp
					+ "ip.properties"));
			prop2.load(fis2);
			String keyChainPath=prop2.getProperty("loginKeyChain");
			String cmd="security import "+cerPath+" -k "+keyChainPath;
			System.out.println("cmd:"+cmd);
			String cmds []={"/bin/sh","-c",cmd};
			process = rt.exec(cmds);
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(),"utf-8"));
			
			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(process.getErrorStream(),"utf-8"));
			String line;
			StringBuffer buffer=new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			while ((line = errorReader.readLine()) != null) {
				buffer.append(line);
			}
			process.waitFor();
			
			bufferedReader.close();
			
			String result=buffer.toString();
			
			
			//System.out.println(result);
			if(result.indexOf("item already exists")>=0){
				
				//return 
			}
			return result;
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
	public String mobileProvisionIdentifier(String provisionpath){
		String tmpPlist=provisionpath.replace(".mobileprovision", "tmpMobileProvision.plist");
		String cmd="security cms -D -i "+provisionpath+" > "+tmpPlist+" && /usr/libexec/PlistBuddy -c 'Print :Entitlements:application-identifier' "+tmpPlist+"";
		
		Process process = null;
		Runtime rt = Runtime.getRuntime();
		try{
			process = rt.exec(cmd);
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(),"utf-8"));
			String line;
			StringBuffer buffer=new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			process.waitFor();
			
			bufferedReader.close();
			
			String plistId=buffer.toString();
			if(plistId!=null){
				 
				Properties prop2 = new Properties();
				String xp = XSSCheckFilter.class.getClassLoader()
						.getResource("").toURI().getPath();
				FileInputStream fis2 = new FileInputStream(new File(xp
						+ "ip.properties"));
				prop2.load(fis2);
				String keyChainPath=prop2.getProperty("keyChainpath");
				cmd="cp \""+provisionpath +"\"  \""+keyChainPath+"/"+plistId+".mobileprovision\"";
				String cmds2 []={"/bin/sh","-c",cmd};
				
				process = rt.exec(cmds2);
				process.waitFor();
				
				return plistId;
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return  null;
		
	}
	// mobileprovision 证书安装  解析provision 证书id
	public String  installMobileProvision(String provisionpath){
		String tmpPlist=provisionpath.replace(".mobileprovision", "tmpMobileProvision.plist");
		String cmd="security cms -D -i "+provisionpath+" > "+tmpPlist+" && /usr/libexec/PlistBuddy -c 'Print :UUID' "+tmpPlist+"";
		
		Process process = null;
		Runtime rt = Runtime.getRuntime();
		try{
			process = rt.exec(cmd);
			
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(),"utf-8"));
			String line;
			StringBuffer buffer=new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			process.waitFor();
			
			bufferedReader.close();
			
			String plistId=buffer.toString();
			if(plistId!=null){
				 
				Properties prop2 = new Properties();
				String xp = XSSCheckFilter.class.getClassLoader()
						.getResource("").toURI().getPath();
				FileInputStream fis2 = new FileInputStream(new File(xp
						+ "ip.properties"));
				prop2.load(fis2);
				String keyChainPath=prop2.getProperty("keyChainpath");
				cmd="mv \""+provisionpath +"\"  \""+keyChainPath+"/"+plistId+".mobileprovision\"";
				
				process = rt.exec(cmd);
				process.waitFor();
				
				return plistId;
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return  null;
	}
	//处理 cer证书cn字段
	public List<String> pareseCN(String path){
		Process process = null;
		Runtime rt = Runtime.getRuntime();
		
		String pkgPath=path.replace(".cer", "cnParse.pem");
		String cmd="openssl x509 -in "+path+" -inform DER -out "+pkgPath+" -outform PKG";
		//
		try{
			process = rt.exec(cmd);
			int iretCode = process.waitFor();
			cmd="openssl x509 -noout -subject -in "+pkgPath+" ";
			// 读取标准输出流
			process = rt.exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(),"utf-8"));
			String line;
			StringBuffer buffer=new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			System.out.println(buffer.toString());
			process.waitFor();
			bufferedReader.close();
			
			String tmp =buffer.toString();
			if(tmp!=null){
				String aaa[] =tmp.split("/");
				String UId="";
				for(int i=0;i<aaa.length;i++){
					
					if(aaa[i].startsWith("UID=")){
						String bbb[]=aaa[i].split("=");
						UId=bbb[1];
						//System.out.println("uuid "+UId);
					}
					if(aaa[i].startsWith("CN=")){
						String bbb[]=aaa[i].split("=");
						if(bbb.length==2){
							String cerName=bbb[1];
							//System.out.println(cerName);
							
							
							List<String> a=new ArrayList<String>();
							a.add(cerName);
							if(cerName.indexOf("(")>0){
								String teamId=cerName.substring(cerName.indexOf("("));
								teamId=teamId.replace("(","").replace(")", "");
								a.add(teamId);
							}else{
								a.add(UId);
							
							}
							
							
							return  a;
						}
						
					}
				}
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
}
