package com.cmstop;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DBHelper {

	public static int countstr(String sql) {

		int result = 0;

		for (int i = 0; i < sql.length(); i++) {

			if ('?' == sql.charAt(i)) {

				result++;
			}

		}

		return result;

	}

	public static ReportEntity select(long buildId, int projectId, int siteId,
			int pageNO, int pageSize, String createDate, String endDate,
			String status, String appType, String identifier) {
		ReportEntity entity = new ReportEntity();

		List<BuildEntity> list = new ArrayList<BuildEntity>();
		entity.datas = list;
		entity.status = 1;
		entity.count = 0;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = pool.getConnection();
			StringBuffer buffer = new StringBuffer(
					"select * from  buildapp where ");

			if (buildId == 0 && projectId == 0 && siteId == 0
					&& null == createDate && endDate == null && null == status
					&& null == appType) {

				return entity;
			}
			if (buildId != 0) {
				buffer.append(" buildid= ?");
			}
			if (projectId != 0) {
				if (buildId == 0) {

					buffer.append("   projectid= ?");
				} else {
					buffer.append(" and  projectid= ?");
				}
			}

			if (siteId != 0) {
				if (buildId != 0 || projectId != 0) {
					buffer.append(" and  siteid= ?");
				} else {
					buffer.append("  siteid= ?");

				}
			}
			int dateStart = DBHelper.countstr(buffer.toString());
			if (createDate != null) {
				if (dateStart == 0) {
					buffer.append(" DATE_FORMAT(created,'%Y-%m-%d')>=?");
				} else {

					buffer.append(" and DATE_FORMAT(created,'%Y-%m-%d')>=?");
				}

				buffer.append(" and DATE_FORMAT(created,'%Y-%m-%d')<=?");
			}
			int statusCount = DBHelper.countstr(buffer.toString());
			if (null != status) {
				if (statusCount == 0) {
					buffer.append(" status=?");
				} else {
					buffer.append(" and status=?");
				}
			}
			int typeCount = DBHelper.countstr(buffer.toString());
			if (null != appType) {

				if (typeCount == 0) {

					buffer.append(" apptype=?");
				} else {

					buffer.append(" and apptype=?");
				}
			}

			int identityCount = DBHelper.countstr(buffer.toString());
			if (null != identifier) {

				if (identityCount == 0) {

					buffer.append(" identifier=?");
				} else {

					buffer.append(" and identifier=?");
				}
			}
			identityCount = DBHelper.countstr(buffer.toString());
			buffer.append("  limit ?,?");

			stmt = con.prepareStatement(buffer.toString());

			if (buildId != 0) {
				stmt.setLong(1, buildId);
			}
			if (projectId != 0) {
				if (buildId == 0) {

					stmt.setInt(1, projectId);
				} else {
					stmt.setInt(2, projectId);
				}
			}

			if (siteId != 0) {
				if (buildId != 0 && projectId != 0) {
					stmt.setInt(3, siteId);
				} else if ((buildId != 0 && projectId == 0)
						|| (buildId == 0 && projectId != 0)) {
					stmt.setInt(2, siteId);

				} else {
					stmt.setInt(1, siteId);
				}
			}
			if (null != createDate) {

				dateStart = dateStart + 1;
				stmt.setString(dateStart, createDate);
				stmt.setString(dateStart + 1, endDate);

			}
			if (null != status) {
				statusCount = statusCount + 1;
				stmt.setString(statusCount, status);
			}

			if (null != appType) {
				typeCount = typeCount + 1;
				stmt.setString(typeCount, appType);
				System.out.println("typeCount " + typeCount);
			}

			// int identityCount=DBHelper.countstr(buffer.toString());
			if (null != identifier) {

				typeCount = typeCount + 1;
				stmt.setString(typeCount, identifier);
				System.out.println("identifier " + typeCount);

			}

			int countStr = DBHelper.countstr(buffer.toString());
			// \\ System.out.println(buffer.toString()+" type "+appType);
			stmt.setInt(countStr - 1, pageNO);

			stmt.setInt(countStr, pageSize);

			rs = stmt.executeQuery();
			while (rs.next()) {
				BuildEntity b = new BuildEntity();

				b.buildid = rs.getString("buildid");
				b.platform = rs.getString("identifier");
				b.siteid = rs.getString("siteid");
				b.clientid = rs.getString("projectid");
				b.apptype = rs.getString("apptype");
				b.appname = rs.getString("appname");
				b.appurl = rs.getString("appurl");
				b.appidentifier = rs.getString("appidentifier");
				b.appversion = rs.getString("appversion");
				b.filepath = rs.getString("filepath");
				b.created = rs.getString("created");
				b.updated = rs.getString("updated");
				b.status = rs.getString("status");
				if (b.updated == null) {
					b.updated = "";
				}
				entity.datas.add(b);

			}
			// count sql
			String selectSql = buffer.toString();
			selectSql = selectSql.replace("*", "count(id) as count");
			selectSql = selectSql.replace("limit ?,?", "");
			stmt = con.prepareStatement(selectSql);
			// set value
			if (buildId != 0) {
				stmt.setLong(1, buildId);
			}
			if (projectId != 0) {
				if (buildId == 0) {

					stmt.setInt(1, projectId);
				} else {
					stmt.setInt(2, projectId);
				}
			}

			if (siteId != 0) {
				if (buildId != 0 && projectId != 0) {
					stmt.setInt(3, siteId);
				} else if ((buildId != 0 && projectId == 0)
						|| (buildId == 0 && projectId != 0)) {
					stmt.setInt(2, siteId);

				} else {
					stmt.setInt(1, siteId);
				}
			}
			if (null != createDate) {

				stmt.setString(dateStart, createDate);
				stmt.setString(dateStart + 1, endDate);
			}
			if (null != status) {

				stmt.setString(statusCount, status);
			}
			if (null != appType) {
				stmt.setString(typeCount, appType);

			}
			if (null != identifier) {
				stmt.setString(identityCount, identifier);

			}
			rs = stmt.executeQuery();
			int total = 0;
			if (rs.next()) {

				total = rs.getInt("count");

			}
			entity.count = total;

		} catch (Exception e) {
			// System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}
		if (entity.datas.size() > 0) {
			entity.status = 0;

		}
		return entity;

	}

	public static int updateApp(long buildId, int sucess) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = pool.getConnection();
			stmt = con
					.prepareStatement("update buildapp set updated=? , status=? where buildid=? ");
			// stmt.setTimestamp(parameterIndex, x)
			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			if (sucess == 0) {

				stmt.setString(2, "success");
			} else if (sucess == 1) {

				stmt.setString(2, "running");
			} else {
				stmt.setString(2, "error");

			}
			stmt.setLong(3, buildId);

			int result = stmt.executeUpdate();
			return result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {

				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 1;
	}

	public static VendorXinge getBuildInfo(int identifier, int siteId,
			int clientid) {
		VendorXinge entity = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = pool.getConnection();
			stmt = con.prepareStatement("{CALL selectBuildInfo (?,?,?)}");

			stmt.setInt(1, identifier);
			stmt.setInt(2, siteId);
			stmt.setInt(3, clientid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				entity = new VendorXinge();
				entity.setId(rs.getInt("id"));
				entity.setIdentifier(rs.getInt("identifier"));
				entity.setSiteid(rs.getInt("siteid"));
				entity.setClientid(rs.getInt("clientid"));
				entity.setAppid(rs.getString("appid"));
				entity.setXgIos_access_id(rs.getString("xgIos_access_id"));
				entity.setXgIos_access_key(rs.getString("xgIos_access_key"));
				entity.setXgIos_secret_key(rs.getString("xgIos_secret_key"));
				entity.setAppleid(rs.getString("appleid"));
				entity.setCer_path(rs.getString("cert_path"));
				entity.setProvision_path(rs.getString("provision_path"));
				entity.setXgAndroid_access_id(rs
						.getString("xgAndroid_access_id"));
				entity.setXgAndroid_access_key(rs
						.getString("xgAndroid_access_key"));
				entity.setXgAndroid_secret_key(rs
						.getString("xgAndroid_secret_key"));
				entity.setHascert(rs.getBoolean("hascert"));
				entity.setApp_bundle(rs.getString("app_bundle"));
				entity.setApp_identifier(rs.getString("app_identifier"));
			}
		} catch (Exception e) {

		} finally {
			try {

				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return entity;

	}

	public static int buildApp(long buildId, String appName, String projectId,
			String siteId_userId, String appType, String appurl,
			String appidentifier, String appversion, String filepath,
			String identity) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = pool.getConnection();
			stmt = con
					.prepareStatement("INSERT INTO buildapp (buildid,identifier,siteid,projectid,apptype,appname,appurl,appidentifier,appversion,filepath,status) VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			stmt.setLong(1, buildId);
			stmt.setString(2, identity);
			stmt.setInt(3, Integer.valueOf(siteId_userId));
			stmt.setInt(4, Integer.valueOf(projectId));
			stmt.setString(5, appType);
			stmt.setString(6, appName);
			stmt.setString(7, appurl);
			stmt.setString(8, appidentifier);
			stmt.setString(9, appversion);
			stmt.setString(10, filepath);
			stmt.setString(11, "default");

			int result = stmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;

	}

	public static long genId(String str, String type) {
		str = Math.random() + str + System.currentTimeMillis() + Math.random()
				* 10000 + str;
		SimpleDateFormat sFormat = new SimpleDateFormat("yyMMdd");
		String date = sFormat.format(new Date());
		String t = "0";
		int h = 0;
		int off = 0;

		int len = str.length();
		for (int i = 0; i < len; i++) {
			h = 31 * h + str.charAt(off++);
		}

		if (type.equals("iphone")) {
			t = "1";
		}
		if (h < 0) {
			h = 0 - h;
		}
		String hr = h + "";
		for (int i = hr.length(); i < 10; i++) {
			hr = hr + "0";

		}

		if (hr.length() > 10) {
			hr = hr.substring(0, 10);
		}
		String result = date + t + hr + ((int) (Math.random() * 89) + 10);
		return Long.valueOf(result).longValue();
	}
}
