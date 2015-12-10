package com.jiangchen.college.entity;

public class User {

	private int uid;
	private String phone;
	private String pwd;
	private String email;
	private String nick;
	private int roleId  = -1;
	private String school;
	private String area;
	private String department;
	private String gradeClass;
	private String year;
	private String photoUrl;
	private String name;
	private int gender = -1;
	private String token;
	
	public User() {
		super();
	}

	public User(int uid, String phone, String pwd, String email, String nick,
			int roleId, String school, String area, String department,
			String gradeClass, String year, String photoUrl, String name,
			int gender, String token) {
		super();
		this.uid = uid;
		this.phone = phone;
		this.pwd = pwd;
		this.email = email;
		this.nick = nick;
		this.roleId = roleId;
		this.school = school;
		this.area = area;
		this.department = department;
		this.gradeClass = gradeClass;
		this.year = year;
		this.photoUrl = photoUrl;
		this.name = name;
		this.gender = gender;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int role_id) {
		this.roleId = role_id;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGradeClass() {
		return gradeClass;
	}

	public void setGradeClass(String gradeClass) {
		this.gradeClass = gradeClass;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}


	@Override
	public String toString() {
		return "User{" +
				"uid=" + uid +
				", phone='" + phone + '\'' +
				", pwd='" + pwd + '\'' +
				", email='" + email + '\'' +
				", nick='" + nick + '\'' +
				", roleId=" + roleId +
				", school='" + school + '\'' +
				", area='" + area + '\'' +
				", department='" + department + '\'' +
				", gradeClass='" + gradeClass + '\'' +
				", year='" + year + '\'' +
				", photoUrl='" + photoUrl + '\'' +
				", name='" + name + '\'' +
				", gender=" + gender +
				", token='" + token + '\'' +
				'}';
	}
}
