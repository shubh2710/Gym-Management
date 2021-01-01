package extra_classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import database.myDatabaseHandler;
import models.ActiveBillModel;
import models.AttendanceModel;
import models.OldBillModel;
import models.PlansModel;
import models.User;

public class CreateBackup implements Runnable {

	StringBuffer text=new StringBuffer();
	myDatabaseHandler db = new myDatabaseHandler();
	Statement statement = db.getStatement();
	private ArrayList<PlansModel> plansList;
	private ArrayList<User> userList;
	private ArrayList<ActiveBillModel> billList;
	private ArrayList<getAttendance> AttList;
	private ArrayList<GetOldBill> oldbill;
	
	@Override
	public void run() {
		Statement statement = null;
			String path = "C://gym_manngment//Backup";
			try {
				Files.createDirectories(Paths.get(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		getUsers();
		
		getActiveBills();
		
		getplans();
		getoldbills();
		getAttendance();
		makeTxt();
		makeAttendanceTxt();
		JOptionPane.showMessageDialog(null, new JLabel("Backup Complete save at C:/gym_manngment/Backup"));
	}
	private void makeAttendanceTxt() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("C://gym_manngment//Backup//Attendance.txt", "UTF-8");
			writer.println("_______________________ATTENDANCE LIST_______________________ \n");
			for(getAttendance ga:AttList){
				writer.println("USER ID: "+ga.getUid() +"\n");
					for(AttendanceModel am:ga.getAt()){
						writer.println(am +"\n");
						writer.println("__________________________________________________________ \n");
					}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void makeTxt() {
		// TODO Auto-generated method stub
		PrintWriter writer;
		try {
			writer = new PrintWriter("C://gym_manngment//Backup//backupdata.txt", "UTF-8");
			writer.println("___________________________USER LIST_____________________________ \n");
			for(User u:userList){
				writer.println(u);
				writer.println("_______________________________________________________________ \n");
			}
			writer.println("___________________________ACTIVE BILLS LIST______________________ \n");
			for(ActiveBillModel abm:billList){
				writer.println(abm);
				writer.println("_______________________________________________________________ \n");
			}
			writer.println("______________________________OLD BILL LIST__________________________ \n");
			for(GetOldBill gob:oldbill){
				writer.println("USER : "+gob.getUid());
				for(OldBillModel obm:gob.getOldBillList()){
					writer.println(obm);
					//writer.println("_____________________________________________________________ \n");
				}
				writer.println("___________________________________________________________________ \n");
			}
			writer.println("_______________________________PLANS LIST_______________________________ \n");
			for(PlansModel pl:plansList){
				writer.println(pl);
				writer.println("____________________________________________________________________ \n");
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getplans() {
		// TODO Auto-generated method stub
		plansList = new ArrayList();
						plansList = db.getPlansList(statement);
			}
	private void getUsers() {
		userList = new ArrayList();
		userList = db.getUserList(statement);
		
	}
	private void getActiveBills() {
		// TODO Auto-generated method stub
		billList=new ArrayList();
		billList = db.getActiveBillList(statement);
	}

	private void getoldbills() {
		// TODO Auto-generated method stub
		oldbill=new ArrayList();
		ArrayList<OldBillModel> oldBillList;
		for(User u:userList){
			oldBillList=db.getOldBillList(u.getUid(),statement);
			oldbill.add(new GetOldBill(oldBillList,u.getReg_no()+""));
		}
	}
	private void getAttendance() {
		// TODO Auto-generated method stub
		AttList=new ArrayList();
				ArrayList<AttendanceModel> at=new ArrayList();
				for(User u: userList){
					at=db.getUserAttendanceDates(u.getUid(),statement);
					AttList.add(new getAttendance(at,u.getReg_no()+""));
				}
	}
	class getAttendance {
		ArrayList<AttendanceModel> at=new ArrayList();
		String uid=null;
		public getAttendance(ArrayList<AttendanceModel> at, String uid) {
			this.at = at;
			this.uid = uid;
		}
		public ArrayList<AttendanceModel> getAt() {
			return at;
		}
		public String getUid() {
			return uid;
		}
	}
	class GetOldBill{
		ArrayList<OldBillModel> oldBillList;
		String uid=null;
		public GetOldBill(ArrayList<OldBillModel> oldBillList, String uid) {
			super();
			this.oldBillList = oldBillList;
			this.uid = uid;
		}
		public ArrayList<OldBillModel> getOldBillList() {
			return oldBillList;
		}
		public String getUid() {
			return uid;
		}
		
	}
}
