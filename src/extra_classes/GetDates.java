package extra_classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDates {
private int year,month,day;
private int minute,hour,sec;
private Date today;
private long todayTs;
public GetDates(Date today) {
	
	this.today = today;
	DateFormat f = new SimpleDateFormat("dd");
	this.day=Integer.parseInt(f.format(today));
	
	f = new SimpleDateFormat("yyyy");
	this.year=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("MM");
	this.month=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("mm");
	this.minute=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("ss");
	this.sec=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("HH");
	this.hour=Integer.parseInt(f.format(today));
}
public GetDates(long today) {
	DateFormat f = new SimpleDateFormat("dd");
	this.day=Integer.parseInt(f.format(today));
	
	f = new SimpleDateFormat("yyyy");
	this.year=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("MM");
	this.month=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("mm");
	this.minute=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("ss");
	this.sec=Integer.parseInt(f.format(today));

	f = new SimpleDateFormat("HH");
	this.hour=Integer.parseInt(f.format(today));
}
public int getYear() {
	return year;
}
public void setYear(int year) {
	this.year = year;
}
public int getMonth() {
	return month;
}
public void setMonth(int month) {
	this.month = month;
}
public int getDay() {
	return day;
}
public void setDay(int day) {
	this.day = day;
}
public int getMinut() {
	return minute;
}
public void setMinut(int minute) {
	this.minute = minute;
}
public int getHour() {
	return hour;
}
public void setHour(int hour) {
	this.hour = hour;
}
public int getSec() {
	return sec;
}
public void setSec(int sec) {
	this.sec = sec;
}
	

	
}
