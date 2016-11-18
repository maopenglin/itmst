package com.cmstop;

import java.util.List;

public class ReportEntity {
 
	public int status;
	public List<BuildEntity> datas;
	public int count;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<BuildEntity> getDatas() {
		return datas;
	}
	public void setDatas(List<BuildEntity> datas) {
		this.datas = datas;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
