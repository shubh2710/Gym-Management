/*
package extra_classes;

import java.util.prefs.Preferences;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import db_keys.Keys;
import preferences.getPrefsSingletan;

public class SMSapi {
  // Find your Account Sid and Token at twilio.com/user/account
	Preferences prefs = new getPrefsSingletan().getPrefs();
  public String ACCOUNT_SID = "";
  public  String AUTH_TOKEN = "";
  private String SERVICE="";
  
  public SMSapi(){
	  this.ACCOUNT_SID=prefs.get(Keys.PREFS_SMS_SID, null);
	  this.AUTH_TOKEN=prefs.get(Keys.PREFS_SMS_TOKEN, null);
	  this.SERVICE=prefs.get(Keys.PREFS_SMS_SERVICE, null);
  }
  public Boolean send(String no,String text) {
	  boolean isSend=false;
	  try{
		  Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		    Message message = Message.creator(new PhoneNumber("+91"+no),SERVICE,
		        "Tomorrow's forecast in Financial District, San Francisco is Clear")
		      //.setMediaUrl("https://climacons.herokuapp.com/clear.png")
		      .create();
		    System.out.println(message.getSid());  
		    isSend=true;
	  }catch(Exception ex){
		  ex.printStackTrace();
		isSend=false;
	  }
	  return isSend;
  }
}
*/
