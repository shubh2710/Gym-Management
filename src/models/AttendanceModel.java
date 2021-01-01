package models;

import java.util.Date;

public class AttendanceModel {
String aid,uid;
long date;
boolean isPresent;
int day,month,year;
public AttendanceModel(String aid, String uid, long date, boolean isPresent, int day, int month, int year) {
	super();
	this.aid = aid;
	this.uid = uid;
	this.date = date;
	this.isPresent = isPresent;
	this.day = day;
	this.month = month;
	this.year = year;
}
public int getDay() {
	return day;
}
public void setDay(int day) {
	this.day = day;
}
public int getMonth() {
	return month;
}
public void setMonth(int month) {
	this.month = month;
}
public int getYear() {
	return year;
}
public void setYear(int year) {
	this.year = year;
}
public boolean isPresent() {
	return isPresent;
}
public void setPresent(boolean isPresent) {
	this.isPresent = isPresent;
}
public String getAid() {
	return aid;
}
public void setAid(String aid) {
	this.aid = aid;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public long getDate() {
	return date;
}
public void setDate(long date) {
	this.date = date;
}
@Override
public String toString() {
	return "AttendanceModel [aid=" + aid + ", uid=" + uid + ", date=" + new Date(date) + ", isPresent=" + isPresent + ", day="
			+ day + ", month=" + month + ", year=" + year + "]";
}

}
