package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Statement;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import db_keys.Keys;
import models.ActiveBillModel;
import models.AttendanceModel;
import models.OldBillModel;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;

public class ImportBackup {
	myDatabaseHandler dbbackup;
	Statement statement = null;
	String path = "C://gym_manngment//database";
	
	public ImportBackup(String path) {
		super();
		this.path = path;
		this.dbbackup = new myDatabaseHandler();
		this.statement = dbbackup.getStatement(path);
		startImporting();
		importImages(path);
		JOptionPane.showMessageDialog(null, new JLabel("Importing done!"));
	}
	private void importImages(String path) {
		// TODO Auto-generated method stub
		File f=new File(path);
		File parent=f.getParentFile();
		
		if(parent.isDirectory()){
			copyImages(parent); 
		}
	}
	private void copyImages(File folder) {
		// TODO Auto-generated method stub
		File[] listOfFiles = folder.listFiles();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("copy File: " + listOfFiles[i].getName());
		        try {
					copyFileUsingStream(new File(listOfFiles[i].getAbsolutePath()),new File("C://gym_manngment//images//"+listOfFiles[i].getName()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	private void startImporting(){
		myDatabaseHandler db = new myDatabaseHandler();
		Statement mainStatement = db.getStatement();
		db.createAtendanceTable(mainStatement);
		db.createOldBills(mainStatement);
		db.createPlansTable(mainStatement);
		db.createUserActiveBills(mainStatement);
		db.createUserTable(mainStatement);
		// importing users
		int reg=0;
		for(User u:this.dbbackup.getUserList(this.statement)){
			if(u.getReg_no()>reg)
				reg=u.getReg_no();
			db.addUser(u, mainStatement);
		}
		// importing bills
		for(ActiveBillModel bill:this.dbbackup.getActiveBillList(this.statement)){
			db.addBill(bill, mainStatement);
		}
		// importing plans
		for(PlansModel plan:this.dbbackup.getPlansList(this.statement)){
			db.addPlan(plan, mainStatement);
		}
		// importing oldbils
		for(OldBillModel oldbill:this.dbbackup.getAllOldBillList(this.statement)){
			db.addOldBillFromOldBill(oldbill , mainStatement);
		}
		// importing attendance
		for(AttendanceModel att:this.dbbackup.getAllUserAttendanceDates(this.statement)){
			db.addAttendance(att, mainStatement);
		}
		Preferences prefs;
		reg++;
		prefs = new getPrefsSingletan().getPrefs();
		prefs.put(Keys.PREF_REG_NO,reg+"");
	}
	
}
