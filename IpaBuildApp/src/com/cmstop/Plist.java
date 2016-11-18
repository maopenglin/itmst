package com.cmstop;

public class Plist {

	public String genHtmlFile(String title,String servicePath){
		String html="<html>" +
				"<head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html\"; charset=\"UTF-8\"><title>"+title+"在线安装</title>" +
				"<meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\" name=\"viewport\">" +
				"" +
				"<script>" +
				"window.location.href='itms-services://?action=download-manifest&url="+servicePath+"'" +
				"</script>" +
				"</head>" +
				"<body> " +
				"</body>" +
				"</html> ";
	  return html;
		
	}
	/**
	 * 
	 * @param ipaWebPath ipa donwload path
	 * @param title  app name
	 * @param icon1024Path  Icon 1024 image
	 * @param icon114   Icon@2x.png
	 * @param identifier  com.cmstop.xxx
	 * @return
	 */
	 public String genPlist(String ipaWebPath,String title,String icon1024Path,String icon114,String identifier,String version){
		  String  plist="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		  		"<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">" +
		  		"<plist version=\"1.0\">" +
		  		"<dict>" +
		  		"<key>items</key>" +
		  		"<array>" +
		  		"<dict>" +
		  		"<key>assets</key>" +
		  		"<array>" +
		  		"<dict>" +
		  		"<key>kind</key>" +
		  		"<string>software-package</string>" +
		  		"<key>url</key>" +
		  		"<string>"+ipaWebPath+"</string>" +
		  		"</dict>" +
		  		"<dict>" +
		  		"<key>kind</key>" +
		  		"<string>full-size-image</string>" +
		  		"<key>needs-shine</key>" +
		  		"<false/>" +
		  		"<key>url</key>" +
		  		"<string>"+icon1024Path+"</string>" +
		  		"</dict>" +
		  		"<dict>" +
		  		"<key>kind</key>" +
		  		"<string>display-image</string>" +
		  		"<key>needs-shine</key>" +
		  		"<false/>" +
		  		"<key>url</key>" +
		  		"<string>"+icon114+"</string>" +
		  		"</dict>" +
		  		"</array>" +
		  		"<key>metadata</key>" +
		  		"<dict>" +
		  		"<key>bundle-identifier</key>" +
		  		"<string>"+identifier+"</string>" +
		  		"<key>bundle-version</key>" +
		  		"<string>"+version+"</string>" +
		  		"<key>kind</key>" +
		  		"<string>software</string>" +
		  		"<key>subtitle</key>" +
		  		"<string>"+title+"</string>" +
		  		"<key>title</key>" +
		  		"<string>"+title+"</string>" +
		  		"</dict>" +
		  		"</dict>" +
		  		"</array>" +
		  		"</dict>" +
		  		"</plist>";
		 
		 return plist;
		 
	 }
}
