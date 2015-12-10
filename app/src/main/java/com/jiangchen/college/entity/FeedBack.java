package com.jiangchen.college.entity;

public class FeedBack {
	private int fid;
	private String content;
	private int uid;
	
	public FeedBack() {
		super();
	}

	public FeedBack(String content, int uid) {
		super();
		this.content = content;
		this.uid = uid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}
