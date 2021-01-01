package extra_classes;

import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import database.myDatabaseHandler;
import models.AttendanceModel;
import models.User;

public class LineToAttendance {

	private String line;
	AttendanceModel attendance=null;
	private ArrayList<User> userList;
	private User user=null;
	private String[] words;
	public LineToAttendance(String line){
		this.line=line;
		getUserList();
	}
	public AttendanceModel  getAttendance() {
		words = this.line.split("\\s+");
		for (int i = 0; i < words.length; i++) {
		    // You may want to check for a non-word character before blindly
		    // performing a replacement
		    // It may also be necessary to adjust the character class
			//System.out.println(words[i]);
		   // words[i] = words[i].replaceAll("[^\\w]", "");
		}
		if(words.length==8){
			String aid=new genrateUniqueID("attendance").getUID();
			try{
				long ts=converDateTime(words[6],words[7]);
				GetDates date=new GetDates(ts);
				if(getUid(words[2])!=null)
				attendance =new AttendanceModel(aid
								,getUid(words[2])
								,ts
								,true
								,date.getDay()
								,date.getMonth()
								,date.getYear());
			}catch(Exception e){
				
			}
		}
		return attendance;
	}
	private long converDateTime(String stringDate, String stringTime) {
		// TODO Auto-generated method stub
			SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date lFromDate1;
			Timestamp fromTS1=null;
			try {
				lFromDate1 = datetimeFormatter1.parse(stringDate+" "+stringTime);
				System.out.println("gpsdate :" + lFromDate1);
				fromTS1 = new Timestamp(lFromDate1.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fromTS1.getTime();
	}
	private String getUid(String reg){
		String uid=null;
		for (User u : userList) {
			if(u.getReg_no()==Integer.parseInt(reg)){
				uid=u.getUid();
				user=u;
				}
		}
		return 	uid;
	}
	public User getUser(){
		return this.user;
	}
	protected void getUserList() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		userList = new ArrayList();
		userList = db.getUserList(statement);
		for (User u : userList) {
		
		}
	}
	public String getID(){
		try{
			return	this.words[2];		
		}catch(Exception e){
			return null;
		}
	
	}
	public String getName(){try{
		return	this.words[3];		
	}catch(Exception e){
		return null;
	}
}
	
}
