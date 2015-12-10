package com.jiangchen.college.entity;

public class Comment {

	private int cid;
	private int nid;
	private User commenter;
	private String content;
	private User replyTo;
	private String cdate;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(User replyTo) {
		this.replyTo = replyTo;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

}
