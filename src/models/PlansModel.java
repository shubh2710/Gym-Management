package models;

public class PlansModel {
	int amt = 0;
	String name;
	String pid;
	int noOfMonth = 0;
	String detail;

	public PlansModel(String pid, String name, int amt, int noOfMonth,String detail) {
		super();
		this.amt = amt;
		this.name = name;
		this.pid = pid;
		this.noOfMonth = noOfMonth;
		this.detail=detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getAmt() {
		return amt;
	}

	public void setAmt(int amt) {
		this.amt = amt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getNoOfMonth() {
		return noOfMonth;
	}

	public void setNoOfMonth(int noOfMonth) {
		this.noOfMonth = noOfMonth;
	}

	@Override
	public String toString() {
		return "PlansModel [amt=" + amt + ", name=" + name + ", pid=" + pid + ", noOfMonth=" + noOfMonth + "]";
	}

}
