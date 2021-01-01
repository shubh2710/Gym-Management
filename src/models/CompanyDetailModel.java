package models;

public class CompanyDetailModel {

	String name;
	String mob;
	String email;
	String address;
	String gstNo;
	int gstPercent;
	public CompanyDetailModel(String name, String mob, String email, String address, String gstNo, int gstPercent) {
		super();
		this.name = name;
		this.mob = mob;
		this.email = email;
		this.address = address;
		this.gstNo = gstNo;
		this.gstPercent = gstPercent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMob() {
		return mob;
	}
	public void setMob(String mob) {
		this.mob = mob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public int getGstPercent() {
		return gstPercent;
	}
	public void setGstPercent(int gstPercent) {
		this.gstPercent = gstPercent;
	}
	
}
