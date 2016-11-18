package com.cmstop;

import java.io.Serializable;

public class Been implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long serverId;
	String userId;
	String appPrefix;
	String qr;

	String k56key;
	String k56sec;
	String kAppName;
	String kAppZhCnName;
	String xgAppId;
	String xgAppKey;
	String appversion;
	String apiUrl;
	String YOUMENG_APP_KEY;
	String CHANNEL_ID;
	String displayName;
	String dentifier;
	String urlSchemes;
	String projectId;
	String pushKey;
	String webSite;
	String downloadPath;

	String app_sign;

	String identity;
	String cycallbackurl;
	String cytoken;

	String app_bundle;

	String xgIos_secret_key;

	public String getXgIos_secret_key() {
		return xgIos_secret_key;
	}

	public void setXgIos_secret_key(String xgIos_secret_key) {
		this.xgIos_secret_key = xgIos_secret_key;
	}

	public String getCustomerBundle() {

		if (app_bundle == null || app_bundle.length() <= 5) {

			return "com.cmstop.cloud.";
		} else {

			return app_bundle + ".";
		}

	}

	public String getApp_bundle() {
		return app_bundle;
	}

	public void setApp_bundle(String app_bundle) {
		this.app_bundle = app_bundle;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getCycallbackurl() {
		return cycallbackurl;
	}

	public void setCycallbackurl(String cycallbackurl) {
		this.cycallbackurl = cycallbackurl;
	}

	public String getCytoken() {
		return cytoken;
	}

	public void setCytoken(String cytoken) {
		this.cytoken = cytoken;
	}

	public String getApp_sign() {
		return app_sign;
	}

	public void setApp_sign(String app_sign) {
		this.app_sign = app_sign;
	}

	String cykey;

	String cysec;

	String realPath;

	String notiUrl;

	String profileId;

	public String getNotiUrl() {
		return notiUrl;
	}

	public void setNotiUrl(String notiUrl) {
		this.notiUrl = notiUrl;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getCysec() {
		return cysec;
	}

	public void setCysec(String cysec) {
		this.cysec = cysec;
	}

	public String getCykey() {
		return cykey;
	}

	public void setCykey(String cykey) {
		this.cykey = cykey;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAppPrefix() {
		return appPrefix;
	}

	public void setAppPrefix(String appPrefix) {
		this.appPrefix = appPrefix;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public String getK56key() {
		return k56key;
	}

	public void setK56key(String k56key) {
		this.k56key = k56key;
	}

	public String getK56sec() {
		return k56sec;
	}

	public void setK56sec(String k56sec) {
		this.k56sec = k56sec;
	}

	public String getkAppName() {
		return kAppName;
	}

	public void setkAppName(String kAppName) {
		this.kAppName = kAppName;
	}

	public String getkAppZhCnName() {
		return kAppZhCnName;
	}

	public void setkAppZhCnName(String kAppZhCnName) {
		this.kAppZhCnName = kAppZhCnName;
	}

	public String getXgAppId() {
		return xgAppId;
	}

	public void setXgAppId(String xgAppId) {
		this.xgAppId = xgAppId;
	}

	public String getXgAppKey() {
		return xgAppKey;
	}

	public void setXgAppKey(String xgAppKey) {
		this.xgAppKey = xgAppKey;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getYOUMENG_APP_KEY() {
		return YOUMENG_APP_KEY;
	}

	public void setYOUMENG_APP_KEY(String yOUMENG_APP_KEY) {
		YOUMENG_APP_KEY = yOUMENG_APP_KEY;
	}

	public String getCHANNEL_ID() {
		return CHANNEL_ID;
	}

	public void setCHANNEL_ID(String cHANNEL_ID) {
		CHANNEL_ID = cHANNEL_ID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDentifier() {
		return dentifier;
	}

	public void setDentifier(String dentifier) {
		this.dentifier = dentifier;
	}

	public String getUrlSchemes() {
		return urlSchemes;
	}

	public void setUrlSchemes(String urlSchemes) {
		this.urlSchemes = urlSchemes;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPushKey() {
		return pushKey;
	}

	public void setPushKey(String pushKey) {
		this.pushKey = pushKey;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

}
