package models;

import java.util.Date;

public class OldBillModel {
	public String bid ;
	public String uid ;
	public  long session_from ;
	public  long session_to ;
	public int amount_paid;
	public long bill_date;
	public OldBillModel(String bid, String uid, long session_from, long session_to, int amount_paid, long bill_date) {
		super();
		this.bid = bid;
		this.uid = uid;
		this.session_from = session_from;
		this.session_to = session_to;
		this.amount_paid = amount_paid;
		this.bill_date = bill_date;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public long getSession_from() {
		return session_from;
	}
	public void setSession_from(long session_from) {
		this.session_from = session_from;
	}
	public long getSession_to() {
		return session_to;
	}
	public void setSession_to(long session_to) {
		this.session_to = session_to;
	}
	public int getAmount_paid() {
		return amount_paid;
	}
	public void setAmount_paid(int amount_paid) {
		this.amount_paid = amount_paid;
	}
	public long getBill_date() {
		return bill_date;
	}
	public void setBill_date(long bill_date) {
		this.bill_date = bill_date;
	}
	@Override
	public String toString() {
		return "OldBillModel [bid=" + bid + ", uid=" + uid + ", session_from=" + new Date(session_from) + ", session_to="
				+ new Date(session_to) + ", amount_paid=" + amount_paid + ", bill_date=" + bill_date + "]";
	}
	
}
