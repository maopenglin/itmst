package com.cmstop;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class IOS extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 501382508144397787L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setContentType("text/json;charset=utf-8");
		// response.setCharacterEncoding("utf-8");
		// PrintWriter out = response.getWriter();
		// out.print("中文");
		// out.flush();
		request.getRequestDispatcher("/WEB-INF/error.json").forward(request,
				response);

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public String getPath() {

		String a = getServletContext().getRealPath("/");

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = 0; i < ar.length - 3; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString() + "iossrcdir/";
	}

	public String getRootPath() {

		String a = getServletContext().getRealPath("/");

		String[] ar = a.split("/");
		StringBuffer buf = new StringBuffer();
		// buf.append("/");
		for (int i = ar.length - 3; i < ar.length - 2; i++) {
			buf.append(ar[i] + "/");

		}
		return buf.toString();
	}

	public String testPath(String apiUrl, String identity) {

		if (apiUrl.length() > 1) {

			return "/"
					+ apiUrl.replace("://", "").replace("/", "")
							.replace(".", "") + identity;
		} else {

			return "";
		}

	}

	public String testPath2(String apiUrl, String identity) {

		if (apiUrl.length() > 1) {

			return apiUrl.replace("://", "").replace("/", "").replace(".", "")
					+ identity;
		} else {

			return "";
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String encoding = "UTF-8";
		request.setCharacterEncoding(encoding);
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		Logger log = Logger.getLogger(this.getClass());
		String userId = "";// site id 站点id
		String appPrefix = "";
		String qr = "";
		String k56key = "";
		String k56sec = "";
		String kAppName = "";
		String kAppZhCnName = "";
		String xgAppId = "";
		String xgAppKey = "";
		String appversion = "";
		String apiUrl = "";
		String YOUMENG_APP_KEY = "";
		String CHANNEL_ID = "";
		String displayName = "";
		String dentifier = "";
		String urlSchemes = "";
		String projectId = "";
		String pushKey = "";
		String webSite = "";

		String cysdk = "";
		String cysec = "";
		String notiUrl = "";
		String appKey = "";
		String app_sign = "";

		String app_bundle = "";
		String identity = "";
		String cycallbackurl = "";
		String cytooken = "";
		int use_default = 0;
		long stackid = 1L;
		RandomAccessFile randFile = null;
		String sign = "";
		try {

			DiskFileItemFactory factory = new DiskFileItemFactory();

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding(encoding);
			// upload.setFileSizeMax(1024*1024*3);
			// 设置整个表单的最大字节数为10M
			List items = new ArrayList();

			items = upload.parseRequest(request);
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FileItem fit = (FileItem) it.next();
				String name = fit.getName();

				if (!fit.isFormField()) {

				} else {
					String value = fit.getString();
					String fn = fit.getFieldName();

					if ("siteid".equals(fn)) {
						userId = value;
					}
					if ("sign".equals(fn)) {
						sign = value;
					}
					if ("clientid".equals(fn)) {
						projectId = value;
					}
					if ("secret".equals(fn)) {
						appKey = value;
					}
					if ("api_url".equals(fn)) {
						apiUrl = value;
					}

					if ("notice_url".equals(fn)) {
						notiUrl = value;
					}
					if ("identifier".equals(fn)) {
						identity = value;
					}
					if ("use_default".equals(fn)) {
						use_default = Integer.parseInt(value);

					}
				}
			}

			SHA1 sha = new SHA1();
			log.info(Integer.parseInt(identity) + " "
					+ Integer.parseInt(userId) + " "
					+ Integer.parseInt(projectId) + " " + appKey);
			if (sha.validateRequest(Integer.parseInt(identity),
					Integer.parseInt(userId), Integer.parseInt(projectId),
					appKey, sign) == false) {
				throw new CustomerException(" sign error " + sign, 118);
			}

			boolean exist = SingletonIpa.getInstance().exist(projectId, userId,
					identity, apiUrl);
			if (exist) {
				throw new CustomerException("已经在打包中,请不要重复刷新", 100);

			}

			// 获得磁盘文件条目工厂

			// 获取文件需要上传到的路径
			String strDirPath = this.getPath();

			String path = strDirPath + userId + "/";
			path = path + projectId + "/";
			File myFilePath = new File(path);
			if (!myFilePath.exists()) {
				boolean b = myFilePath.mkdirs();
			}

			log.info("dir:" + path + testPath(apiUrl, identity));
			File myPath = new File(path + testPath(apiUrl, identity));
			if (!myPath.exists()) {
				boolean b = myPath.mkdirs();
			}

			upload.setSizeMax(-1);
			Iterator it2 = items.iterator();
			int sw = 57;
			int sh = 57;
			int fileCount = 0;
			File deleteFile = new File(path + testPath(apiUrl, identity) + "/"
					+ "big.png");
			if (deleteFile.exists()) {
				deleteFile.delete();
			}

			log.info("=======================+++++++++++++++++++++++++=======================================");
			while (it2.hasNext()) {
				FileItem fit = (FileItem) it2.next();
				String name = fit.getName();

				if (!fit.isFormField()) {
					String fn = fit.getFieldName();

					String type = fit.getContentType();

					log.info("type " + type + " name " + fn);
					sw = sh = -1;// 清除数据
					if (fn.equals("icon")) {
						name = "Icon.png";
						sw = 57;
						sh = 57;
					}
					if (fn.equals("icon114")) {

						name = "Icon@2x.png";
						sw = 114;
						sh = 114;
					}

					if (fn.equals("icon29")) {

						name = "icon_29.png";
						sw = 29;
						sh = 29;
					}
					if (fn.equals("icon58")) {

						name = "icon_58.png";
						sw = 58;
						sh = 58;
					}
					if (fn.equals("icon80")) {

						name = "icon_80.png";
						sw = 80;
						sh = 80;
					}
					if (fn.equals("icon120")) {

						name = "icon_120.png";
						sw = 120;
						sh = 120;
					}
					if (fn.equals("icon180")) {

						name = "icon_180.png";
						sw = 180;
						sh = 180;
					}

					if (fn.equals("Default@2x")) {

						name = "Default@2x.png";
						sw = 640;
						sh = 960;

					}
					if (fn.equals("Default-568h@2x")) {

						name = "Default-568h@2x.png";
						sw = 640;
						sh = 1136;
					}
					if (fn.equals("Default-750")) {

						name = "i6@2x.png";
						sw = 750;
						sh = 1334;
					}
					if (fn.equals("Default-1242")) {

						name = "plus@2x.png";
						sw = 1242;
						sh = 2208;
					}

					if (fn.equals("awdcer")) {

						name = "awdcer.cer";
						File file2 = new File(path + testPath(apiUrl, identity)
								+ "/" + name);

						fit.write(file2);
						log.info("ios distributioncer"
								+ file2.getAbsolutePath());
						continue;
					}
					if (fn.equals("distributioncer")) {

						name = "distributioncer.cer";
						File file2 = new File(path + testPath(apiUrl, identity)
								+ "/" + name);

						fit.write(file2);
						log.info("ios distributioncer"
								+ file2.getAbsolutePath());
						continue;
					}
					if (fn.equals("mobileprovision")) {

						name = "mobileprovision.mobileprovision";
						File file2 = new File(path + testPath(apiUrl, identity)
								+ "/" + name);

						fit.write(file2);
						log.info("ios mobileprovision"
								+ file2.getAbsolutePath());
						System.out.println(file2);
						continue;
					}

					if (!type.toLowerCase().trim().equals("image/png")) {

						throw new CustomerException(name + "" + type, 117);
					}

					if (fn.equals("startimg_ios")) {
						name = "big.png";
						File file2 = new File(path + testPath(apiUrl, identity)
								+ "/" + name);
						fit.write(file2);

						BufferedImage bi = ImageIO.read(file2);
						if (bi == null) {
							// file2.delete();
							throw new CustomerException(
									"startimg_ios image content error", 116);

						}
						continue;
					}

					File file2 = new File(path + testPath(apiUrl, identity)
							+ "/" + name);

					fit.write(file2);

					BufferedImage bi = ImageIO.read(file2);
					if (bi == null) {

						throw new CustomerException(name
								+ " image content error", 115);

					}
					File file = new File(path + testPath(apiUrl, identity)
							+ "/" + name);
					FileInputStream fis = new FileInputStream(file);
					BufferedImage bufferedImg = ImageIO.read(fis);
					int imgWidth = bufferedImg.getWidth();
					int imgHeight = bufferedImg.getHeight();
					if (sw != -1 && sh != -1) {
						if (imgWidth != sw || imgHeight != sh) {
							throw new CustomerException(name + "width :"
									+ imgWidth + "   height:" + imgHeight
									+ "图片尺寸错误 " + sw + "*" + sh, 114);
						}
					}
				} else {

					// System.out.println(fit);
					String value = fit.getString(encoding);
					String fn = fit.getFieldName();
					this.seekFile(randFile, fn + ":\t" + value);
					if ("identifier".equals(fn)) {
						identity = value;

					}
					if ("app_sh_callback".equals(fn)) {
						cycallbackurl = value;

					}
					if ("app_bundle".equals(fn)) {
						app_bundle = value;

					}
					if ("app_sh_token".equals(fn)) {
						cytooken = value;

					}
					if ("siteid".equals(fn)) {
						userId = value;
					}
					if ("notice_url".equals(fn)) {
						notiUrl = value;

					}
					if ("secret".equals(fn)) {

						app_sign = value;
					}
					if ("app_sh_key".equals(fn)) {
						cysec = value;
					}
					if ("app_sh_id".equals(fn)) {
						cysdk = value;
						// break;
					}
					if ("clientid".equals(fn)) {
						projectId = value;
					}
					if ("api_url".equals(fn)) {
						apiUrl = value;
					}
					if ("app_profile".equals(fn)) {
						appPrefix = value;
					}

					if ("app_qrurl".equals(fn)) {
						qr = value;
					}
					if ("webSite".equals(fn)) {
						webSite = value;
					}
					if ("app_name".equals(fn)) {
						kAppZhCnName = value;
					}

					if ("app_xgid".equals(fn)) {
						xgAppId = value;
					}
					if ("app_xgkey".equals(fn)) {
						xgAppKey = value;
					}
					if ("app_version".equals(fn)) {
						appversion = value;
					}
					if ("mta_key".equals(fn)) {
						YOUMENG_APP_KEY = value;
					}

					if ("app_identifier".equals(fn)) {
						dentifier = value;
					}
					if ("app_urlschemes".equals(fn)) {
						urlSchemes = value;
					}

				}
			}

			if (Integer.parseInt(identity) <= 0
					|| Integer.parseInt(userId) <= 0
					|| Integer.parseInt(projectId) <= 0) {
				this.seekFile(randFile, "identity | siteid | clientid 必填"
						+ identity + "," + userId + "," + projectId);
				throw new CustomerException("identity | siteid | clientid 必填"
						+ identity + "," + userId + "," + projectId, 113);
			}

			log.info("=======================+++++++++++++++++++++++++=======================================");

			if (kAppName.equals("") || kAppName == null) {

				kAppName = kAppZhCnName;
			}
			if (displayName.equals("") || displayName == null) {

				displayName = kAppZhCnName;
			}
			if (dentifier.equals("") || dentifier == null) {
				dentifier = appPrefix;
			}

			Been been = new Been();
			been.downloadPath = SHA1.sha1(Math.abs(new Random().nextInt())
					+ identity
					+ DBHelper.genId(notiUrl + userId + identity, "iphone"));
			Properties prop2 = new Properties();

			String xp = XSSCheckFilter.class.getClassLoader().getResource("")
					.toURI().getPath();
			FileInputStream fis2 = new FileInputStream(new File(xp
					+ "ip.properties"));
			prop2.load(fis2);

			String outputPath = prop2.getProperty("outputPath");
			Process process = null;

			Runtime rt = Runtime.getRuntime();
			// 初始化下载文件目录
			process = rt.exec("mkdir " + outputPath + "/" + userId);
			log.info("mkdir " + outputPath + "/" + userId);
			int iretCode = process.waitFor();
			process = rt.exec("mkdir " + outputPath + "/" + userId + "/"
					+ been.downloadPath);
			log.info("mkdir " + outputPath + "/" + userId + "/"
					+ been.downloadPath);
			iretCode = process.waitFor();

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
			String date = df.format(new Date());

			String rdownload = userId + "/" + been.downloadPath + "/" + date
					+ ".ipa";
			been.realPath = rdownload;

			File logf = new File(outputPath + "/"
					+ been.realPath.replaceAll(".ipa", ".log"));
			if (!logf.exists()) {
				logf.createNewFile();

			}
			randFile = new RandomAccessFile(outputPath + "/"
					+ been.realPath.replaceAll(".ipa", ".log"), "rw");
			this.seekFile(randFile, "接收参数值");
			this.seekFile(randFile, "\r\tidentity:" + identity + "\r\tcientid:"
					+ projectId + " " + "\r\tsiteid:" + userId
					+ "\r\tappPrefix:" + appPrefix + "\r\tqr:" + qr
					+ "\r\tkAppName:" + kAppName + "\tkAppZhCnName:"
					+ kAppZhCnName + "\r\txgAppId:" + xgAppId + "\r\txgAppKey:"
					+ xgAppKey + "" + "\r\tappversion:" + appversion
					+ "\r\tapiUrl:" + apiUrl + CHANNEL_ID + ""
					+ "\tdisplayName:" + displayName + "\r\tapp_identifier:"
					+ dentifier + "\r\turlSchemes:" + urlSchemes + "\r\tshid:"
					+ cysdk + "\r\tsec:" + cysec + "\r\tcycallbackurl :"
					+ cycallbackurl + "\r\tcytoken :" + cytooken
					+ "\r\tapp bundle :" + app_bundle + "\r\tnotiUrl:"
					+ notiUrl);

			been.userId = userId;
			been.appPrefix = appPrefix;
			been.qr = qr;
			been.app_bundle = app_bundle;
			been.notiUrl = notiUrl;
			been.identity = identity;
			been.cycallbackurl = cycallbackurl;
			been.cytoken = cytooken;
			been.serverId = stackid;
			been.k56key = k56key;
			been.app_sign = app_sign;
			been.k56sec = k56sec;
			been.kAppName = kAppName;
			been.kAppZhCnName = kAppZhCnName;
			been.xgAppId = xgAppId;
			been.xgAppKey = xgAppKey;
			been.appversion = appversion;
			been.apiUrl = apiUrl;
			been.YOUMENG_APP_KEY = YOUMENG_APP_KEY;
			been.CHANNEL_ID = CHANNEL_ID;
			been.displayName = displayName;
			been.dentifier = dentifier;
			been.urlSchemes = urlSchemes;
			been.projectId = projectId;
			been.pushKey = pushKey;
			been.webSite = webSite;
			been.cykey = cysdk;
			been.cysec = cysec;

			if (use_default == 1) {
				VendorXinge xg = DBHelper.getBuildInfo(
						Integer.parseInt(identity), Integer.parseInt(userId),
						Integer.parseInt(projectId));
				if (xg == null) {
					throw new CustomerException("数据库证书查询错误,未查询到可用记录", 112);
				}
				

				identity = xg.identifier + "";

				xgAppId = xg.xgIos_access_id;
				xgAppKey = xg.xgIos_access_key;
				been.xgIos_secret_key = xg.xgIos_secret_key;

				been.app_bundle = xg.app_bundle;

				been.identity = identity;
				been.xgAppId = xgAppId;
				been.xgAppKey = xgAppKey;
				been.dentifier = xg.app_identifier;

				this.seekFile(randFile,
						"==++++++++++++++++++++自动分配的证书信息   信鸽+++++++++++++++++++++++++++++++++++==");
				this.seekFile(randFile, "identifier " + xg.identifier);
				this.seekFile(randFile, "xg.ios_access_id "
						+ xg.xgIos_access_id);
				this.seekFile(randFile, "xg.ios_access_key "
						+ xg.xgIos_access_key);
				this.seekFile(randFile, "xg.app_bundle " + xg.app_bundle);
				this.seekFile(randFile, "xg.app_identifier "
						+ xg.app_identifier);
				this.seekFile(randFile,
						"==++++++++++++++++++++证书源目录+++++++++++++++++++++++++++++++++++==");
				String a = getServletContext().getRealPath("/");

				String[] ar = a.split("/");
				StringBuffer buf = new StringBuffer();
				// buf.append("/");
				for (int i = 0; i < ar.length - 3; i++) {
					buf.append(ar[i] + "/");

				}

				String rootPath = buf.toString() + "MediaCloud/Applecert/";
				String wwcder = rootPath + "AppleWWDRCA.cer";
				this.seekFile(randFile, "wdcer:" + wwcder);
				String dis = rootPath + xg.cer_path;
				this.seekFile(randFile, "dis :" + dis);
				String mobilepro = rootPath + xg.provision_path;
				this.seekFile(randFile, "mobileprovision : " + mobilepro);
				File mp = new File(mobilepro);
				this.seekFile(randFile, "证书根路径:" + rootPath);
				if (!mp.exists()) {
					throw new CustomerException("mobileprovision 文件不存在"
							+ xg.provision_path, 110);
				}

				File mcer = new File(dis);
				if (!mcer.exists()) {
					throw new CustomerException("distributioncer.cer 文件不存在"
							+ xg.cer_path, 109);
				}

				String twwcder = path + testPath2(apiUrl, identity) + "/"
						+ "awdcer.cer";
				String tdis = path + testPath2(apiUrl, identity) + "/"
						+ "distributioncer.cer";
				String tmobilepro = path + testPath2(apiUrl, identity) + "/"
						+ "mobileprovision.mobileprovision";

				String c1 = "cp " + wwcder + "  " + twwcder;
				log.info(c1);
				process = rt.exec(c1);
				int diretCode = process.waitFor();

				c1 = "cp " + dis + "  " + tdis;
				log.info(c1);
				process = rt.exec(c1);
				diretCode = process.waitFor();

				c1 = "cp " + mobilepro + "  " + tmobilepro;
				log.info(c1);
				process = rt.exec(c1);
				diretCode = process.waitFor();
			}

			if (been.xgAppId == null || been.xgAppId.length() < 2) {
				throw new CustomerException("xg app id 不对" + been.xgAppId, 108);
			}
			if (!isNumeric(been.xgAppId)) {

				throw new CustomerException("xg app id 格式错误", 107);
			}
			if (been.xgAppKey == null || been.xgAppKey.length() < 2) {
				throw new CustomerException("xg app key 不对" + been.xgAppKey,
						106);
			}
			if (been.cykey == null || been.cykey.length() < 2) {
				throw new CustomerException("app sh id 必填" + been.cykey, 104);
			}
			if (been.cysec == null || been.cysec.length() < 2) {
				throw new CustomerException("app sh key 必填" + been.cysec, 105);
			}
			if (been.cycallbackurl == null || been.cycallbackurl.length() < 2) {
				throw new CustomerException("app sh callback 畅言回调地址必填"
						+ been.cycallbackurl, 103);
			}
			if (been.cytoken == null || been.cytoken.length() < 2) {
				throw new CustomerException("app sh token 畅言token必填"
						+ been.cytoken, 102);
			}

			String tmobilepro = path + testPath2(apiUrl, identity) + "/"
					+ "mobileprovision.mobileprovision";
			CerPaser paser = new CerPaser();
			String mobProvisionID = paser.mobileProvisionIdentifier(tmobilepro);
			this.seekFile(randFile, "证书appID" + mobProvisionID);
			String buildAppid = been.getCustomerBundle() + been.dentifier;
			this.seekFile(randFile, "xg appid:" + buildAppid);
			if (!mobProvisionID.endsWith(buildAppid)) {
				this.seekFile(randFile, mobProvisionID + "     != "
						+ buildAppid);
				throw new CustomerException(
						"证书bundle identifier 与编译 identifier不一致，拒绝编译", 101);
			}
			String p = XSSCheckFilter.class.getClassLoader().getResource("")
					.toURI().getPath();
			FileInputStream fis = new FileInputStream(new File(p
					+ "ip.properties"));
			Properties prop = new Properties();
			prop.load(fis);
			stackid = DBHelper.genId(kAppName + projectId + "iphone" + apiUrl
					+ been.downloadPath + "_" + userId + "_" + projectId
					+ ".ipa" + pushKey, "iphone");
			been.serverId = stackid;

			boolean existagain = SingletonIpa.getInstance().exist(projectId,
					userId, identity, apiUrl);
			if (existagain) {
				throw new CustomerException("已经在打包中,请不要重复刷新", 100);

			}
			DBHelper.buildApp(stackid, kAppName, projectId, userId, "iphone",
					apiUrl, appPrefix, appversion, rdownload, identity);

			SingletonIpa.getInstance().addList(been);

			try {
				String bin = "/ios.bin";

				FileOutputStream fos = new FileOutputStream(path
						+ testPath(apiUrl, identity) + bin);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(been);
				oos.close();
			} catch (Exception ex) {

			}

			SingletonIpa
					.getInstance()
					.getThread()
					.execute(
							new IpaRunable(been, getServletContext()
									.getRealPath("/")));
			// String ctx=request.getRequestURL().toString();

			log.info("ios 已经加入队列 并正常返回数据");

			out.print("{\"status\":\"0\",\"id\":\"" + stackid
					+ "\",\"message\":\"ok\"}");
			this.seekFile(randFile, "{\"status\":\"0\",\"id\":\"" + stackid
					+ "\",\"message\":\"ok\"}");
		} catch (CustomerException e) {
			this.seekFile(randFile, e.getMessage());
			log.error("ios error  " + userId + " userId msg:" + e.getMessage());
			out.print("{\"status\":\"" + e.getCode() + "\",\"message\":\""
					+ e.getMessage() + "\"}");
		} catch (Exception e) {
			this.seekFile(randFile, e.getMessage());
			log.error("ios error  " + userId + " userId msg:" + e.getMessage());
			out.print("{\"status\":\"1\",\"message\":\"" + e.getMessage()
					+ "\"}");
		} finally {
			out.flush();
			out.close();
			randFile.close();
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */

	public void init() throws ServletException {

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

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public void write(String filePath, String content) {
		BufferedWriter bw = null;

		try {
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
}
