package models;

import java.util.Date;

import javax.swing.text.DateFormatter;

public class ActiveBillModel {

	String bid, uid;
	Date sessionFrom, sessionTo;
	int amountPending, amountPaid;

	public ActiveBillModel(String bid, String uid, Date sessionFrom, Date sessionTo, int amountPending,
			int amountPaid) {
		super();
		this.bid = bid;
		this.uid = uid;
		this.sessionFrom = sessionFrom;
		this.sessionTo = sessionTo;
		this.amountPending = amountPending;
		this.amountPaid = amountPaid;
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

	public Date getSessionFrom() {
		return sessionFrom;
	}

	public void setSessionFrom(Date sessionFrom) {
		this.sessionFrom = sessionFrom;
	}

	public Date getSessionTo() {
		return sessionTo;
	}

	public void setSessionTo(Date sessionTo) {
		this.sessionTo = sessionTo;
	}

	public int getAmountPending() {
		return amountPending;
	}

	public void setAmountPending(int amountPending) {
		this.amountPending = amountPending;
	}

	public int getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Override
	public String toString() {
		return "ActiveBillModel [bid=" + bid + ", uid=" + uid + ", sessionFrom=" + sessionFrom + ", sessionTo="
				+ sessionTo + ", amountPending=" + amountPending + ", amountPaid=" + amountPaid + "]";
	}

}
