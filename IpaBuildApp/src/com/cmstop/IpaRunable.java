package com.cmstop;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.RandomAccess;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

public class IpaRunable implements Runnable {

	Been been;
	String basePath;

	public IpaRunable(Been ben, String path) {
		been = ben;
		basePath = path;
		Singleton.getInstance().setRecord(been.getkAppName(), "已经进入队列,排队中");
	}

	public String getPath() {

		String a = basePath;

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = 0; i < ar.length - 3; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString() + "iossrcdir/";
	}

	public String getRootPath() {

		String a = basePath;

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = ar.length - 3; i < ar.length - 2; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString();
	}

	public void start() {
		this.run();
	}

	public String testPath(String apiUrl, String identity) {

		if (apiUrl.length() > 1) {

			return apiUrl.replace("://", "").replace("/", "").replace(".", "")
					+ identity;
		} else {

			return "";
		}

	}

	public void seekFile(RandomAccessFile randFile, String cmd) {

		try {
			long fileLengths;
			fileLengths = randFile.length();
			randFile.seek(fileLengths);
			randFile.write(("\r\n" + cmd).getBytes());
		} catch (Exception e) {

		}

	}

	public void run() {
		Singleton.getInstance().setRecord(been.getkAppName(), "已经开始打包");
		Logger log = Logger.getLogger(this.getClass());
		Process process = null;
		String strDirPath = this.getPath();
		Been bee = been;
		String userId = bee.userId;
		String appPrefix = bee.appPrefix;
		String qr = bee.qr;
		String k56key = bee.k56key;
		String k56sec = bee.k56sec;
		String kAppName = bee.kAppName;
		String kAppZhCnName = bee.kAppZhCnName;
		String xgAppId = bee.xgAppId;
		String xgAppKey = bee.xgAppKey;
		String appversion = bee.appversion;
		String apiUrl = bee.apiUrl;
		String YOUMENG_APP_KEY = bee.YOUMENG_APP_KEY;
		String CHANNEL_ID = bee.CHANNEL_ID;
		String displayName = bee.displayName;
		String dentifier = bee.dentifier;
		String urlSchemes = bee.urlSchemes;
		String projectId = bee.projectId;
		String pushKey = bee.pushKey;
		String webSite = bee.webSite;
		Runtime rt = Runtime.getRuntime();
		String makePath = this.getPath();
		String rpath = testPath(been.apiUrl, been.identity);
		String mpath = strDirPath + userId + "/";

		Properties prop2 = new Properties();
		DBHelper.updateApp(been.getServerId(), 1);
		String outputPath = "";

		RandomAccessFile randFile = null;
		try {
			String xp = XSSCheckFilter.class.getClassLoader().getResource("")
					.toURI().getPath();
			FileInputStream fis2 = new FileInputStream(new File(xp
					+ "ip.properties"));
			prop2.load(fis2);

			String zipPath = "ios.zip";

			String zip2 = been.apiUrl.replace("http://", "")
					.replace("https://", "").replace("/", "")
					+ ".zip";
			String rp = makePath.replaceAll("iossrcdir/", "") + zip2;
			log.info("notification zip Path:" + rp);
			File exFile = new File(rp);
			if (exFile.exists()) {
				zipPath = zip2;
			}

			// 删除 source code cache
			String dsourceIpa = mpath + projectId + "/" + rpath
					+ "/release.ipa";
			File dFileipa = new File(dsourceIpa);
			if (dFileipa.exists()) {
				dFileipa.delete();
			}
			String dsourceZip = mpath + projectId + "/" + rpath + "/ios.zip";
			File dsourceZipFile = new File(dsourceZip);
			if (dsourceZipFile.exists()) {
				dsourceZipFile.delete();
			}
			String dsource = mpath + projectId + "/" + rpath + "/IOS";
			String dcmd = "rm -rf  " + dsource;
			log.info(dcmd);
			process = rt.exec(dcmd);
			int diretCode = process.waitFor();

			dsource = mpath + projectId + "/" + rpath + "/__MACOSX";
			dcmd = "rm -rf  " + dsource;
			log.info(dcmd);
			process = rt.exec(dcmd);
			diretCode = process.waitFor();

			// end delete

			String cmd = "cp -r -f " + makePath.replaceAll("iossrcdir/", "")
					+ zipPath + " " + mpath + projectId + "/" + rpath
					+ "/ios.zip";
			log.info(cmd);
			process = rt.exec(cmd);
			int iretCode = process.waitFor();

			if (iretCode == 0) {
				log.info("文件复制成功 important ----------- " + cmd
						+ "   result success iretCode=0");

				CertificateTester test = new CertificateTester();
				Singleton.getInstance().setRecord(been.getkAppName(), "源码复制完成");

				String download2 = prop2.getProperty("download");
				outputPath = prop2.getProperty("outputPath");

				randFile = new RandomAccessFile(outputPath + "/"
						+ been.realPath.replaceAll(".ipa", ".log"), "rw");

				this.seekFile(randFile, "文件复制成功,开始解压,进入打包流程");
				cmd = "unzip -o -q  " + mpath + projectId + "/" + rpath
						+ "/ios.zip -d " + mpath + projectId + "/" + rpath
						+ "/";
				log.info("开始解压");
				process = Runtime.getRuntime().exec(cmd);
				iretCode = process.waitFor();
				Singleton.getInstance().setRecord(been.getkAppName(), "源码解压完成");
				this.seekFile(randFile, "文件复制成功,解压完成");

				if (download2 == null) {
					download2 = "ROOT";
				}

				cmd = "rm -rf " + getPath() + getRootPath() + "webapps/"
						+ download2 + "/" + rpath + "/" + userId + userId + "_"
						+ projectId + ".json";
				;
				// 再次校验参数值

				userId = bee.userId;
				appPrefix = bee.appPrefix;
				qr = bee.qr;
				k56key = bee.k56key;
				k56sec = bee.k56sec;
				kAppName = bee.kAppName;
				kAppZhCnName = bee.kAppZhCnName;
				xgAppId = bee.xgAppId;
				xgAppKey = bee.xgAppKey;
				appversion = bee.appversion;
				apiUrl = bee.apiUrl;
				YOUMENG_APP_KEY = bee.YOUMENG_APP_KEY;
				CHANNEL_ID = bee.CHANNEL_ID;
				displayName = bee.displayName;
				dentifier = bee.dentifier;
				urlSchemes = bee.urlSchemes;
				projectId = bee.projectId;
				pushKey = bee.pushKey;
				webSite = bee.webSite;
				// 校验完成
				log.info(cmd);
				process = rt.exec(cmd);
				process.waitFor();

				// 初始化下载文件目录
				process = rt.exec("mkdir " + outputPath + "/" + userId);
				iretCode = process.waitFor();
				process = rt.exec("mkdir " + outputPath + "/" + userId + "/"
						+ been.downloadPath);
				iretCode = process.waitFor();
				this.seekFile(randFile, "下载目录初始化完成");
				// end
				// 初始化编译日志文件信息
				File logf = new File(outputPath + "/"
						+ been.realPath.replaceAll(".ipa", ".log"));
				if (!logf.exists()) {
					logf.createNewFile();

				}

				this.seekFile(randFile, "+++++++++" + kAppName
						+ " 编译信息       +++++++++\r\n");

				String sourcePath = mpath + "" + projectId + "/"
						+ testPath(been.apiUrl, been.identity) + "/big.png";
				File deleteFile = new File(sourcePath);
				if (deleteFile.exists()) {

					String default640960 = mpath + "" + projectId + "/"
							+ testPath(been.apiUrl, been.identity)
							+ "/Default@2x.png";
					String i6 = mpath + "" + projectId + "/"
							+ testPath(been.apiUrl, been.identity)
							+ "/i6@2x.png";
					String plus = mpath + "" + projectId + "/"
							+ testPath(been.apiUrl, been.identity)
							+ "/plus@2x.png";
					String d1136 = mpath + "" + projectId + "/"
							+ testPath(been.apiUrl, been.identity)
							+ "/Default-568h@2x.png";

					ImageHelper.resizeImage(sourcePath, d1136, 640, 1136);
					ImageHelper.resizeImage(sourcePath, i6, 750, 1334);
					ImageHelper.resizeImage(sourcePath, plus, 1242, 2208);
					ImageHelper
							.resizeImage(sourcePath, default640960, 640, 960);
					this.seekFile(randFile, "启动图裁剪完成");
				}

				cmd = "rm -rf " + getPath() + getRootPath() + "webapps/"
						+ download2 + "/" + rpath + userId + "_" + projectId
						+ ".ipa";
				;

				process = rt.exec(cmd);

				iretCode = process.waitFor();

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Icon.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Icon@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/icon_29.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/logo-29.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/icon_80.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/logo-80.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/icon_58.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/logo-58.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/icon_180.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/logo-180.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/icon_120.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/AppIcon.appiconset/logo-120.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Default@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/640960-1.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Default@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/640960-2.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);
				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Default@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/640960.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/i6@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/7501334.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);
				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/plus@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/12422208.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Default-568h@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/6401136-12.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				cmd = "cp -r -f "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/Default-568h@2x.png    "
						+ mpath
						+ ""
						+ projectId
						+ "/"
						+ rpath
						+ "/IOS/CmsTopMediaCloud/Images.xcassets/LaunchImage.launchimage/6401136-1.png";
				this.seekFile(randFile, cmd + "\r\n");
				process = rt.exec(cmd);

				iretCode = process.waitFor();
				Singleton.getInstance().setRecord(been.getkAppName(), "图片修改完成");

				// update define.h file
				if (true) {
					String define = strDirPath + userId + "/" + projectId + "/"
							+ rpath
							+ "/IOS/CmsTopMediaCloud/AppConfig/AppConfig.h";
					log.info("defeine path " + define);
					File filename = new File(define); // 要读取以上路径的input。txt文件
					InputStreamReader reader = new InputStreamReader(
							new FileInputStream(filename)); // 建立一个输入流对象reader
					BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
					String line2 = "";

					line2 = br.readLine();

					StringBuffer buf = new StringBuffer();
					while (line2 != null) {

						if (line2.startsWith("#define AppPreFix")) {
							buf.append("#define AppPreFix @\"" + appPrefix
									+ "://\"");
						} else if (line2.startsWith("#define CMSAppName")) {
							buf.append("#define CMSAppName @\"" + kAppName
									+ "\"");
						} else if (line2.startsWith("#define CMS_XGAppId")) {
							buf.append("#define CMS_XGAppId " + xgAppId);
						} else if (line2.startsWith("#define CMS_XGAppKey")) {
							buf.append("#define CMS_XGAppKey @\"" + xgAppKey
									+ "\"");
						} else if (line2.startsWith("#define CMSAppVersion")) {
							buf.append("#define CMSAppVersion @\"" + appversion
									+ "\"");
						} else if (line2.startsWith("#define CMSRequestUrl")) {
							buf.append("#define CMSRequestUrl @\"" + apiUrl
									+ "\"");
						} else if (line2.startsWith("#define ChangyanAppKey")) {// 畅言sec
							buf.append("#define ChangyanAppKey @\""
									+ been.cysec + "\"");
						} else if (line2
								.startsWith("#define ChangyanRegisterApp")) {// 畅言app
																				// id
							buf.append("#define ChangyanRegisterApp @\""
									+ been.cykey + "\"");
						} else if (line2
								.startsWith("#define ChangyanRedirectUrl")) {// 畅言回调地址
							buf.append("#define ChangyanRedirectUrl @\""
									+ been.cycallbackurl + "\"");
						} else if (line2.startsWith("#define ChangyanaToken")) {// 畅言token
							buf.append("#define ChangyanaToken @\""
									+ been.cytoken + "\"");
						}

						else if (line2.startsWith("#define mtakey")) {
							buf.append("#define mtakey  @\"" + YOUMENG_APP_KEY
									+ "\"");
						} else if (line2.startsWith("#define CMSAPPClientid")) {
							buf.append("#define CMSAPPClientid  @\""
									+ been.projectId + "\"");
						} else if (line2.startsWith("#define CMSAPPSiteid")) {
							buf.append("#define CMSAPPSiteid  @\""
									+ been.userId + "\"");
						} else if (line2.startsWith("#define CMSAPPSecret")) {
							buf.append("#define CMSAPPSecret  @\""
									+ been.app_sign + "\"");
						} else if (line2.startsWith("#define CMSAPPIdentifier")) {
							buf.append("#define CMSAPPIdentifier  @\""
									+ been.identity + "\"");
						} else {
							buf.append(line2);
						}

						line2 = br.readLine(); // 一次读入一行数据

						buf.append(System.getProperty("line.separator"));
					}
					this.seekFile(randFile, buf.toString() + "\r\n");
					this.write(define, buf.toString());

					log.info(define);
					reader.close();
				}
				// update pod config file

				if (true) {
					String define = strDirPath + userId + "/" + projectId + "/"
							+ rpath + "/IOS/Pods/MWPhotoBrowser/CTAppConfig.h";
					log.info("pods file image config path " + define);

					File filename = new File(define); // 要读取以上路径的input。txt文件
					String code = this.codeString(define);
					InputStreamReader reader = new InputStreamReader(
							new FileInputStream(filename)); // 建立一个输入流对象reader
					BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
					String line2 = "";

					line2 = br.readLine();

					StringBuffer buf = new StringBuffer();
					while (line2 != null) {
						if (line2.startsWith("#define AppName")) {
							buf.append("#define AppName @\"" + displayName
									+ "\"");
						} else {
							buf.append(line2);
						}

						line2 = br.readLine(); // 一次读入一行数据

						buf.append(System.getProperty("line.separator"));
					}
					this.seekFile(randFile, buf.toString() + "\r\n");
					//
					this.write(define, buf.toString());
					log.info(define);
					reader.close();

				}

				// update CmsTop-Info.plist file
				if (true) {
					String define = strDirPath + userId + "/" + projectId + "/"
							+ rpath + "/IOS/CmsTopMediaCloud/Info.plist";
					File file = new File(define);
					NSDictionary rootDict = (NSDictionary) PropertyListParser
							.parse(file);
					String[] key = rootDict.allKeys();
					rootDict.put("CFBundleDisplayName", displayName);
					rootDict.put("CFBundleName", displayName);
					rootDict.put("CFBundleShortVersionString", appversion);
					rootDict.put("CFBundleVersion", appversion);
					rootDict.put("CFBundleIdentifier", been.getCustomerBundle()
							+ dentifier);

					NSArray arr = (NSArray) rootDict
							.objectForKey("CFBundleURLTypes");
					if (arr.count() == 1) {
						NSDictionary ad = (NSDictionary) arr.objectAtIndex(0);
						String a[] = urlSchemes.split(",");
						NSArray wa = new NSArray(a.length + 2);
						wa.setValue(0, appPrefix);
						for (int i = 0; i < a.length; i++) {
							wa.setValue(i, a[i]);
						}
						wa.setValue(a.length, appPrefix);
						wa.setValue(a.length + 1, been.dentifier);
						ad.put("CFBundleURLName", been.getCustomerBundle()
								+ dentifier);
						ad.put("CFBundleURLSchemes", wa);
					}
					this.seekFile(randFile, rootDict.toXMLPropertyList()
							+ "\r\n");
					PropertyListParser.saveAsXML(rootDict, new File(define));
				}
				Singleton.getInstance().setRecord(been.getkAppName(),
						"配置文件修改完成,->开始解析证书");
				String cerPath = mpath + "" + projectId + "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/distributioncer.cer";
				CerPaser parser = new CerPaser();
				parser.installCer(cerPath);
				List<String> cns = parser.pareseCN(cerPath);
				cerPath = mpath + "" + projectId + "/"
						+ testPath(been.apiUrl, been.identity) + "/awdcer.cer";
				parser.installCer(cerPath);
				cerPath = mpath + "" + projectId + "/"
						+ testPath(been.apiUrl, been.identity)
						+ "/mobileprovision.mobileprovision";
				String appId = parser.installMobileProvision(cerPath);

				// CmsTopCloud.xcodeproj/project.pbxproj

				String pbProjectPath = mpath + projectId + "/" + rpath
						+ "/IOS/CmsTopMediaCloud.xcodeproj/project.pbxproj";

				if (cns.size() == 2) {
					this.updatePbProject(pbProjectPath, appId, cns.get(0),
							cns.get(0), cns.get(1),been.getCustomerBundle()
							+ dentifier);
					System.out.println("工程文件修改完成," + pbProjectPath + " appId:"
							+ appId + " cn:" + cns.get(0) + " team:"
							+ cns.get(1));
					this.seekFile(randFile,
							" appId:" + appId + " cn:" + cns.get(0) + " team:"
									+ cns.get(1) + "\r\n");
					// log.error("工程文件修改完成,"+pbProjectPath+" appId:"+appId+" cn:"+cns.get(0)+" team:"+cns.get(1));
					Singleton.getInstance().setRecord(
							been.getkAppName(),
							"<br/>cn:" + cns.get(0) + " <br/>team:"
									+ cns.get(1));
				} else {

					throw new CustomerException("cer 解析失败", 121);

				}

				Singleton.getInstance().setRecord(been.getkAppName(),
						"证书安装完成,正在编译");
				cmd = "" + mpath + projectId + "/" + rpath
						+ "/IOS/ipa-build.sh " + mpath + projectId + "/"
						+ rpath + "/IOS/   -w   -s CmsTopMediaCloud -o "
						+ mpath + projectId + "/" + rpath + "/ -n";
				this.seekFile(randFile, cmd);
				this.seekFile(randFile, "开始编译。。。。。");
				// log.info("userId" + userId + cmd);

				String cmds[] = { "/bin/sh", "-c", cmd };
				process = Runtime.getRuntime().exec(cmds);

				// 读取标准输出流
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				boolean buildOk = false;

				while ((line = bufferedReader.readLine()) != null) {

					log.info(line + "\n");
					// System.out.println(line);
					System.out.println(line);
					this.seekFile(randFile, line);
					if (line.startsWith("Results at")
							|| line.indexOf("Results at") >= 0) {
						Singleton.getInstance().setRecord(been.getkAppName(),
								"编译成功");

						this.seekFile(randFile, "编译成功");
						buildOk = true;
					}
					if (line.startsWith("** BUILD FAILED **")
							|| line.indexOf("BUILD FAILED") >= 0) {

						// throw new CustomerException("编译失败,请检查证书",122);
					}
				}
				// 读取标准错误流
				BufferedReader brError = new BufferedReader(
						new InputStreamReader(process.getErrorStream(),
								"gb2312"));
				String errline = null;
				while ((errline = brError.readLine()) != null) {
					log.error(errline);
					System.out.println(line);
					this.seekFile(randFile, errline);
				}

				int c = process.waitFor();
				brError.close();
				bufferedReader.close();

				String ap = mpath + projectId + "/" + rpath
						+ "/IOS/build/Release-iphoneos/CmsTopMediaCloud.app";
				String ipa = mpath + projectId + "/" + rpath + "/release.ipa";
				process = Runtime.getRuntime().exec("rm  -rf " + ipa);
				process.waitFor();
				cmd = "xcrun -sdk iphoneos PackageApplication -v " + ap
						+ " -o " + ipa + "";
				String cmds2[] = { "/bin/sh", "-c", cmd };
				System.out.println(cmds2);
				process = Runtime.getRuntime().exec(cmds2);
				BufferedReader bufferedReader2 = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line2;

				while ((line2 = bufferedReader2.readLine()) != null) {
					System.out.println(line2);
					this.seekFile(randFile, line2);
				}
				BufferedReader brError2 = new BufferedReader(
						new InputStreamReader(process.getErrorStream(),
								"gb2312"));
				String errline2 = null;
				while ((errline2 = brError2.readLine()) != null) {

					System.out.println(errline2);
					this.seekFile(randFile, errline2);
				}

				process.waitFor();

				if (c != 0) {

				}
				String path = XSSCheckFilter.class.getClassLoader()
						.getResource("").toURI().getPath();
				FileInputStream fis = new FileInputStream(new File(path
						+ "ip.properties"));
				Properties prop = new Properties();
				prop.load(fis);

				cmd = "rm -rf " + outputPath + "/" + been.realPath;
				String[] cmd23 = { "/bin/sh", "-c", cmd };
				process = rt.exec(cmd);
				iretCode = process.waitFor();

				this.seekFile(randFile, "ipa编译完成  开始拷贝文件到目标目录");
				cmd = "cp -r -f " + getPath() + userId + "/" + projectId + "/"
						+ rpath + "/release.ipa" + " " + outputPath + "/"
						+ been.realPath;
				this.seekFile(randFile, cmd);

				Singleton.getInstance().setRecord(been.getkAppName(),
						"拷贝完成,生成安装文件");

				process = rt.exec(cmd);
				this.seekFile(randFile, "ipa文件开始拷贝");
				iretCode = process.waitFor();

				File targetValidate = new File(outputPath + "/" + been.realPath);
				this.seekFile(randFile, "文件拷贝：" + outputPath + "/"
						+ been.realPath);
				System.out.println("文件拷贝:" + outputPath + "/" + been.realPath);
				if (!targetValidate.exists()) {
					this.seekFile(randFile, "拷贝ipa文件失败");
					throw new CustomerException("打包失败,请重新打包", 120);
				} else {
					buildOk = true;
					this.seekFile(randFile, "拷贝ipa文件成功 目标文件存在");
				}

				String domain = prop.getProperty("domain");
				File pFile = new File(outputPath + "/"
						+ been.realPath.replaceAll(".ipa", ".plist"));
				log.info(outputPath + "/"
						+ been.realPath.replaceAll(".ipa", ".plist"));
				this.seekFile(
						randFile,
						outputPath + "/"
								+ been.realPath.replaceAll(".ipa", ".plist"));
				if (pFile.createNewFile()) {
				} else {
					this.seekFile(randFile, been.realPath + "   文件创建失败");
					buildOk = false;
				}

				if (pFile.exists()) {

					OutputStreamWriter outputStream = new OutputStreamWriter(
							new FileOutputStream(pFile), "UTF-8");

					cmd = "cp -r -f " + mpath + "" + projectId + "/"
							+ testPath(been.apiUrl, been.identity)
							+ "/Icon@2x.png    " + outputPath + "/"
							+ been.realPath.replace(".ipa", "@2x.png");
					log.info(cmd);
					this.seekFile(randFile, "\r\n" + cmd);
					process = rt.exec(cmd);
					iretCode = process.waitFor();
					Plist plist = new Plist();
					String a = plist.genPlist(domain + been.realPath,
							bee.kAppZhCnName, "",
							domain + been.realPath.replace(".ipa", "@2x.png"),
							been.getCustomerBundle() + been.dentifier,
							bee.appversion);

					outputStream.write(a);
					this.seekFile(randFile, "安装 文件制作\r\n" + a);
					outputStream.flush();
					outputStream.close();

					File htmlFile = new File(outputPath + "/"
							+ been.realPath.replaceAll(".ipa", ".html"));
					OutputStreamWriter outputStream2 = new OutputStreamWriter(
							new FileOutputStream(htmlFile), "UTF-8");
					String b = plist.genHtmlFile(been.kAppZhCnName, domain
							+ been.realPath.replace(".ipa", ".plist"));
					outputStream2.write(b);
					outputStream2.flush();
					outputStream2.close();

				}

				String ips = prop.getProperty("deleteCode").trim();
				if (ips != null && (ips.equals("1") || ips.equals("true"))) {

					cmd = "rm -rf " + getPath() + userId + "/" + projectId
							+ "/" + rpath;
					int ti = Integer.valueOf(prop.getProperty("timeToDelete"));
					Timer t = new Timer();
					t.schedule(new ClearSource(t, cmd), 1000 * ti);
				}

				this.seekFile(randFile, "\r\n" + cmd);
				rt.exec(cmd);
				process.waitFor();
				bufferedReader.close();
				brError.close();
				Singleton.getInstance().setRecord(been.getkAppName(),
						"打包完成,通知打包服务器");

				log.info("ios 开始发送 数据:" + been.notiUrl);

				HashMap<String, String> map = new HashMap<String, String>();
				if (buildOk) {
					map.put("status", "0");
					map.put("message", "ok");
					map.put("siteid", userId);
					map.put("clientid", projectId);
					map.put("id", been.getServerId() + "");
					map.put("filepath", been.realPath);
					map.put("app_identifier", dentifier);
					map.put("app_bundle", been.getCustomerBundle());
					map.put("xgIos_access_id", been.xgAppId);
					map.put("xgIos_access_key", been.xgAppKey);
					map.put("xgIos_secret_key", been.xgIos_secret_key);

					DBHelper.updateApp(been.getServerId(), 0);
				} else {
					map.put("status", "123");
					map.put("message", "编译错误" + errline);
					map.put("siteid", userId);
					map.put("clientid", projectId);
					map.put("id", been.getServerId() + "");
					map.put("filepath", been.realPath);
					DBHelper.updateApp(been.getServerId(), 2);
				}
				this.seekFile(randFile, "\r\n通知地址:" + been.notiUrl + "?"
						+ EasyHttp.mapToString(map));

				String nUrl = been.notiUrl;
				String result = EasyHttp.post(nUrl, map);

				try {
					this.seekFile(randFile, result);
				} catch (Exception e1) {

				}
				log.info("ios noti:" + result);

			} else {
				DBHelper.updateApp(been.getServerId(), 2);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("status", "124");
				map.put("message", "文件复制失败");
				map.put("siteid", userId);
				map.put("clientid", projectId);
				map.put("id", been.getServerId() + "");
				map.put("filepath", been.realPath);
				String nUrl = prop2.getProperty("finishNoticeUrl").trim();
				String result = EasyHttp.post(nUrl, map);

				log.info("ios noti:" + result);
				try {
					this.seekFile(randFile, result);
				} catch (Exception e1) {

				}
				log.error("ios projectId:" + projectId + " " + "userId:"
						+ userId + "   源代码文件 copy失败");

			}
		} catch (CustomerException e) {
			DBHelper.updateApp(been.getServerId(), 2);
			e.printStackTrace();
			log.error("ios projectId:" + projectId + " " + "userId:" + userId
					+ "\tappPrefix:" + appPrefix + "\tqr:" + qr + "\tk56key:"
					+ k56key + "\tk56sec:" + k56sec + "" + "\tkAppName:"
					+ kAppName + "\tkAppZhCnName:" + kAppZhCnName
					+ "\txgAppId:" + xgAppId + "\txgAppKey:" + xgAppKey + ""
					+ "\tappversion:" + appversion + "\tapiUrl:" + apiUrl
					+ "\tYOUMENG_APP_KEY:" + YOUMENG_APP_KEY + "\tCHANNEL_ID:"
					+ CHANNEL_ID + "" + "\tdisplayName:" + displayName
					+ "\tdentifier:" + dentifier + "\turlSchemes:" + urlSchemes);
			log.error("ios " + userId + "\t" + e.getMessage() + "\n");

			DBHelper.updateApp(been.getServerId(), 2);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("status", e.getCode() == 0 ? "1" : e.getCode() + "");
			map.put("message", e.getMessage());
			map.put("siteid", userId);
			map.put("clientid", projectId);
			map.put("id", been.getServerId() + "");
			map.put("filepath", been.realPath);
			String nUrl = prop2.getProperty("finishNoticeUrl").trim();
			this.seekFile(
					randFile,
					"\r\n通知地址:" + been.notiUrl + "?"
							+ EasyHttp.mapToString(map));

			String result = EasyHttp.post(nUrl, map);

			this.seekFile(randFile, result);
			this.seekFile(randFile, e.getMessage());

			log.info("ios noti:" + result);

		} catch (Exception e) {
			DBHelper.updateApp(been.getServerId(), 2);
			e.printStackTrace();
			log.error("ios projectId:" + projectId + " " + "userId:" + userId
					+ "\tappPrefix:" + appPrefix + "\tqr:" + qr + "\tk56key:"
					+ k56key + "\tk56sec:" + k56sec + "" + "\tkAppName:"
					+ kAppName + "\tkAppZhCnName:" + kAppZhCnName
					+ "\txgAppId:" + xgAppId + "\txgAppKey:" + xgAppKey + ""
					+ "\tappversion:" + appversion + "\tapiUrl:" + apiUrl
					+ "\tYOUMENG_APP_KEY:" + YOUMENG_APP_KEY + "\tCHANNEL_ID:"
					+ CHANNEL_ID + "" + "\tdisplayName:" + displayName
					+ "\tdentifier:" + dentifier + "\turlSchemes:" + urlSchemes);
			log.error("ios " + userId + "\t" + e.getMessage() + "\n");

			DBHelper.updateApp(been.getServerId(), 2);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("status", "1");
			map.put("message", e.getMessage());
			map.put("siteid", userId);
			map.put("clientid", projectId);
			map.put("id", been.getServerId() + "");
			map.put("filepath", been.realPath);
			String nUrl = prop2.getProperty("finishNoticeUrl").trim();
			String result = EasyHttp.post(nUrl, map);
			this.seekFile(
					randFile,
					"\r\n通知地址:" + been.notiUrl + "?"
							+ EasyHttp.mapToString(map));

			this.seekFile(randFile, result);
			this.seekFile(randFile, e.getMessage());

			log.info("ios noti:" + result);

		} finally {

			SingletonIpa.getInstance().remove(bee);
			Singleton.getInstance().clear(been.getkAppName());
			if (process != null) {
				try {
					process.getErrorStream().close();
					process.getInputStream().close();
					process.getOutputStream().close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			try {
				randFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void updatePbProject(String fileName, String profileName,
			String caName, String appIdProfile, String team,String bundleId) {
		Logger log = Logger.getLogger(this.getClass());
		// TODO Auto-generated method stub
		// String profileName = "e931330c-454e-4493-ab89-4062b91524e6";
		// String caName = "iPhone Developer: shenghui zhong (4H6BK8A398)";
		// String appIdProfile =
		// "iPhone Developer: shenghui zhong (4H6BK8A398)";
		// String team = "4H6BK8A398";
		try {
			// String
			// fileName="/Users/cmstop/Desktop/cmstop-cloud/autocerTest/CmsTopCloud/CmsTopCloud.xcodeproj/project.pbxproj";
			File filename = new File(fileName); // 要读取以上路径的input。txt文件
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(filename)); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			String line2 = "";

			line2 = br.readLine();
			StringBuffer buf = new StringBuffer();
			while (line2 != null) {
				line2 = new String(line2.getBytes(), "UTF-8");
				if (line2.indexOf("PROVISIONING_PROFILE =") >= 0) {

					line2 = "PROVISIONING_PROFILE = \"" + "" + profileName
							+ "\";";
					// log.error(line2);
					// System.out.println(line2+"\n");
				}
				if (line2.indexOf("CODE_SIGN_IDENTITY =") >= 0) {

					line2 = "CODE_SIGN_IDENTITY = \"" + "" + caName + "\";";
					// log.error(line2);
				}
				
				if (line2.indexOf("DevelopmentTeam =") >= 0) {

					line2 = "DevelopmentTeam = \"" + "" + team + "\";";
					log.error(line2);
				}
				if(line2.indexOf("PRODUCT_BUNDLE_IDENTIFIER")>=0){
					
					line2="PRODUCT_BUNDLE_IDENTIFIER = "+bundleId+";";
				}
				buf.append(line2 + "\r\n");
				line2 = br.readLine();
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(buf.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	public void write(String filePath, String content) {
		BufferedWriter bw = null;

		try {

			// bw= new BufferedWriter(new OutputStreamWriter(new
			// FileOutputStream(new File(filePath)),"UTF-8"));

			// 根据文件路径创建缓冲输出流
			bw = new BufferedWriter(new FileWriter(filePath));
			// 将内容写入文件中
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}

	public String codeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
				fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}

		return code;
	}
}
