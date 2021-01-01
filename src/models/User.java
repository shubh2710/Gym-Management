package models;

import java.util.Date;

public class User {

	String dob, name, uid, email, mob, profile, plan,address;
	int weight, age,reg_no;
	Date joiningDate;
	public int getReg_no() {
		return reg_no;
	}

	public void setReg_no(int reg_no) {
		this.reg_no = reg_no;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public User(String dob, String name, String uid, String email, String mob, String profile, int age, int weight,
			String plan, int reg_no, Date joiningDate,String address) {
		super();
		this.dob = dob;
		this.name = name;
		this.uid = uid;
		this.email = email;
		this.mob = mob;
		this.profile = profile;
		this.plan = plan;
		this.weight = weight;
		this.age = age;
		this.reg_no = reg_no;
		this.joiningDate = joiningDate;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	@Override
	public String toString() {
		return "User "+reg_no+"\r\n [dob=" + dob + ",\r\n name=" + name + ",\r\n uid=" + uid + ",\r\n email=" + email + ",\r\n mob=" + mob
				+ ",\r\n profile=" + profile + ",\r\n plan=" + plan + ",\r\n weight=" + weight + ",\r\n age=" + age + ",\r\n reg_no="
				+ reg_no + ",\r\n joiningDate=" + joiningDate + "]";
	}

}
