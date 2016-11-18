package com.cmstop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DBTest {

	public static void main(String[] args) throws IOException {

		try {
			// 获取计算机名
			String name = InetAddress.getLocalHost().getHostName();
			// 获取IP地址
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("计算机名：" + name);
			System.out.println("IP地址：" + ip);
		} catch (UnknownHostException e) {
			System.out.println("异常：" + e);
			e.printStackTrace();
		}

		// VendorXinge entity= DBHelper.getBuildInfo(999, 999, 999);
		// System.out.println(entity.appid);
		// System.out.println(entity.xgIos_access_id);
		// System.out.println(entity.xgIos_access_key);
		// System.out.println(entity.xgIos_secret_key);
		// System.out.println(entity.cer_path);
		// System.out.println(entity.provision_path);
		// long id=DBHelper.genId("a;", "0");
		// System.out.println(id +"\t" +(id+"").length());;

		ImageHelper.resizeImage("/Users/maopenglin/Desktop/gaode/s.png",
				"/Users/maopenglin/Desktop/gaode/T24.png", 48, 48);

		// String
		// d1136="/Users/maopenglin/Desktop/baby/12/ios/CASS.CSSN/CASS.CSSN/Images.xcassets/LaunchImage.launchimage/136.png";
		// String
		// sourcePath="/Users/maopenglin/Desktop/baby/12/ios/CASS.CSSN/CASS.CSSN/Images.xcassets/LaunchImage.launchimage/640_1136.png";
		// ImageHelper.resizeImage(sourcePath, d1136, 640, 1136);
		//
		// File f=new File("/Users/cmstop/temp/aaa/bbb/");
		// f.mkdirs();

		// Plist p=new Plist();
		// String a=p.genPlist("http://www.cmstop.com/download/test.ipa",
		// "思拓合众", "", "http://www.cmstop.com/114.png",
		// "com.cmstop.test","1.0.0");
		// System.out.println(a);
		//
		// DBHelper.buildApp("1000", "测试名字", "10001", "10221", "iphone",
		// "http://donwload.com", "com.cmsotp.test", "1.0.0",
		// "http://filepath.com");
		// DBHelper.updateApp("1000", 0);
		// Random rand = new Random();
		// HashMap<String, String> map=new HashMap<String, String>();
		//
		//
		// map.put("status", "0");
		// map.put("message", "ok");
		// map.put("user_id", "1");
		// map.put("project_id", "2");
		// map.put("id", 123455+"");
		// map.put("project_filepath","aaa/aaa.ipa");
		// String a=EasyHttp.post("http://data.cmstop.cc/index/package/getinfo",
		// map);
		// System.out.println(a);
		// for(int i=0;i<100;i++){
		// // System.out.println((int)(Math.random()*89)+10);
		// System.out.println(DBHelper.genId("100010001000011测试项目我的 测试cio0099909991.0.0这是一个特别长的描述你小不晓得啊啊的扩大了圣诞节法律的减肥；阿里看电视剧法律；啊可接受的；富利卡就单身；付了款",
		// "android"));
		// }
		/*
		 * ConnectionPool pool = ConnectionPool.getInstance(); Connection con =
		 * null; PreparedStatement stmt = null; ResultSet rs = null; try { con =
		 * pool.getConnection(); stmt=con.prepareStatement(
		 * "select * from  buildapp where  DATE_FORMAT(created,'%Y-%m-%d')>=? and DATE_FORMAT(created,'%Y-%m-%d')<=?  limit ?,?"
		 * ); stmt.setString(1, "2014-08-10"); stmt.setString(2, "2014-08-10");
		 * stmt.setInt(3,0); stmt.setInt(4,10); rs = stmt.executeQuery(); while
		 * (rs.next()) { System.out.println(rs.getString("id"));; } }catch
		 * (Exception e) { // TODO: handle exception }
		 */
	}

}
