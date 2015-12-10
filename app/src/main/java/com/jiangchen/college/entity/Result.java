package com.jiangchen.college.entity;


public class Result<T> {

	public static final int STATE_SUC = 100;
	public static final int STATE_FAIE = 200;

	public int state;

	public String descrpit;

	public T data;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDescrpit() {
		return descrpit;
	}

	public void setDescrpit(String descrpit) {
		this.descrpit = descrpit;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
