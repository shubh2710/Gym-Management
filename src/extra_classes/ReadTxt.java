package extra_classes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.text.DateFormatter;

import database.myDatabaseHandler;
import models.ActiveBillModel;
import models.AttendanceModel;
import models.User;
import panels.ErrorListPanel;
 
public class ReadTxt implements Runnable{
	private ArrayList<AttendanceModel> attList;

	private ArrayList<User> users;
	private ArrayList<String> showList;
	private Statement statment;
	private myDatabaseHandler db;
	private int lineCount=0;
	private String path;
	public ReadTxt(String path){
		this.path=path;
		attList=new ArrayList();
		showList=new ArrayList();
		users=new ArrayList();
		db=new myDatabaseHandler();
		statment =db.getStatement();
	    
	}
	    public void read(){
	    	
	    }
	    private ActiveBillModel getUserBill(String uid) {
			ActiveBillModel bill=null;
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			if(uid.length()>0)
			bill=db.getActiveBillListUser(statement,uid);
			return bill;
		}
	    private boolean checkSession(ActiveBillModel bill,AttendanceModel a) {
			boolean isActive=false;
			if (bill.getSessionTo().after(new Date(a.getDate()))) {
				isActive=true;	
			}
			return isActive;
		}
		private void MakeEntries(ArrayList<AttendanceModel> attList,ArrayList<User> users) {
			
			for(int i=0;i<attList.size();i++){
				AttendanceModel a=attList.get(i);
				User u=users.get(i);
				lineCount++;
				DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
			if(!db.checkAttendance(a, statment)){
				ActiveBillModel bill = getUserBill(a.getUid());
					if(checkSession(bill,a)){
						if(db.addAttendance(a,statment)){
							showList.add(lineCount +" ) Attendance Added-> Name: "+u.getName() +" ID: "+u.getReg_no() +" Date: "+f.format(a.getDate()) +"\n");	
						}else
							showList.add(lineCount +" ) Database Unknown Error-> Name: "+u.getName() +" ID: "+u.getReg_no()+" Date: "+f.format(a.getDate()) +"\n");
					}else{
						if(db.addAttendance(a,statment)){
							showList.add(lineCount +" )Error! Attendance Added but Session expired -> Name:  "+u.getName() +" ID: "+u.getReg_no()+" Date: "+f.format(a.getDate())+"\n" );
						}else
							showList.add(lineCount +" ) Database Unknown Error Session expired-> Name: "+u.getName()+" ID: "+u.getReg_no() +" Date: "+f.format(a.getDate()) +"\n");
					}
				}
				else{	
					showList.add(lineCount +" )Error! Alread Exsit-> Name:  "+u.getName()+" ID: "+u.getReg_no() +" Date: "+f.format(a.getDate()) +"\r\n");
				}
			}
			int dialogResult = JOptionPane.showConfirmDialog(null, new ErrorListPanel(showList),
					"Report List : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		}
		@Override
		public void run() {
			
			 try {
		 			File file = new File(path);
		 			FileReader fileReader = new FileReader(file);
		 			BufferedReader bufferedReader = new BufferedReader(fileReader);
		 			//StringBuffer stringBuffer = new StringBuffer();
		 			String line;
		 			System.out.println("Contents of file:");
		 			while ((line = bufferedReader.readLine()) != null) {
		 				LineToAttendance read=new LineToAttendance(line);
		 				AttendanceModel att=read.getAttendance();
		 				if(att!=null){
		 					users.add(read.getUser());
		 					attList.add(att);
		 					}else{
		 						lineCount++;
		 						showList.add(lineCount +" )Error!  ID not found-> Name: "+read.getName() +" ID: "+ read.getID()+"\n");
		 					}
		 				/*stringBuffer.append(line);
		 				stringBuffer.append("\n");*/
		 			}
		 			fileReader.close();
		 		} catch (IOException e) {
		 			e.printStackTrace();
		 		}
		    	 MakeEntries(attList,users);
		}
	   }
