package com.jiangchen.college.entity;

import java.io.Serializable;

public class AppVersion  implements Serializable{
	private int verId;
	private int verCode;
	private String verName;
	private String verUrl;


	public int getVerId() {
		return verId;
	}
	public void setVerId(int verId) {
		this.verId = verId;
	}
	public int getVerCode() {
		return verCode;
	}
	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}
	public String getVerName() {
		return verName;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public String getVerUrl() {
		return verUrl;
	}
	public void setVerUrl(String verUrl) {
		this.verUrl = verUrl;
	}

	
}
