package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.GetDates;
import models.ActiveBillModel;
import models.OldBillModel;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;

public class MonthlyIncomePanel extends JPanel{
	private Date today;
	private int month,year;
	private JButton prv,next;
	private JLabel noOfAdmission,noOfOldBill,noOfActiveBill,total;
	private ArrayList<User> users;
	private ArrayList<PlansModel> plans;
	private ArrayList<OldBillModel> oldBills;
	private ArrayList<ActiveBillModel> activeBill;
	private int noUser,noOldBill,noActiveBill,totalAmount;
	private JLabel totalAmt,date;
	
	public MonthlyIncomePanel(){
	today=new Date();
	Calendar cal = Calendar.getInstance();
	month=cal.get(cal.MONTH);
	year=cal.get(cal.YEAR);
	getUsers();
	getOldBills();
	getPlans();
	addComponents();
	setDate();
	try{
		Preferences prefs;
		prefs = new getPrefsSingletan().getPrefs();
		setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
		setBackground(Color.decode("#ffffff"));
		JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
	}
	}
	
	private void addComponents() {
		noOfAdmission=new JLabel();
		noOfOldBill=new JLabel();
		noOfActiveBill=new JLabel();
		totalAmt=new JLabel();
		date=new JLabel("/");
		removeAll();
		prv=new JButton("PRV");
		next=new JButton("NEXT");
		prv.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(month>1)
				month-=1;
				else{
					month=12;
					year-=1;
				}
				setDate();
			}
			
		});
		next.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(month<12)
				month+=1;
				else{
					month=12;
					year+=1;
				}
				setDate();
			}
			
		});
		total=new JLabel();
		// TODO Auto-generated method stub
		setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(400,400));
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.weightx=1;
		constraints.gridx =0;
		constraints.gridy =0;
		add(prv,constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(new JLabel("MONTHLY INCOME RECORD:"));
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(next,constraints);
		
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(date,constraints);
		

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(new JLabel("TOTAL NO OF ADMISSION:"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(new JLabel("TOTAL NO OF ACTIVE BILL:"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 4;
		add(new JLabel("TOTAL NO OF UPDATE PLANS:"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 5;
		add(new JLabel("NET AMOUNT COLLECTED:"),constraints);
		
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(noOfAdmission,constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		add(noOfActiveBill,constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		add(noOfOldBill,constraints);
		constraints.gridx = 1;
		constraints.gridy = 5;
		add(totalAmt,constraints);
		
	}
	void setDate(){
		noOfAdmission.setText(getThisMonthAdmission()+"");
		noOfOldBill.setText(getNoOfOldBill()+"");
		noOfActiveBill.setText(activeBill.size()+"");
		totalAmt.setText(getTotalAmountPaid()+" Rs");
		date.setText((month+" / "+year));
	}
	private int getThisMonthAdmission(){
		noUser=0;
		for(User u:users){
			Calendar cal = Calendar.getInstance();
			cal.setTime(u.getJoiningDate());

			if(cal.get(cal.MONTH)==month && cal.get(cal.YEAR)==year){
				noUser++;
			}
		}
		return noUser;
	}
	private void getPlans() {
		// TODO Auto-generated method stub
		plans = new ArrayList();
				// TODO Auto-generated method stub
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				plans = db.getPlansList(statement);
				activeBill=db.getActiveBillList(statement);
	}
	private PlansModel getSelectedPlan(String string) {
		PlansModel p=null;
		for (int i = 0; i < plans.size(); i++) {
			if (plans.get(i).getPid().equals(string)) {
				p=plans.get(i);
			}
		}
		return p;
	}

	private void getUsers() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		
		users = new ArrayList();
		users = db.getUserList(statement);
	}

	private int getNoOfActiveBill(){
		noActiveBill=0;
		for(ActiveBillModel bill:activeBill){
			if (bill.getSessionTo().after(new Date())) {
				noActiveBill++;
			} else {
				
			}
		}
		
	return noActiveBill;	
	}
	private int getNoOfOldBill(){
		noOldBill=0;
		totalAmount=0;
		for(OldBillModel bill:oldBills){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(bill.getBill_date()));

			if (cal.get(cal.MONTH)==month && cal.get(cal.YEAR)==year ) {
				noOldBill++;
				totalAmount+=bill.getAmount_paid();
			} else {
			}
		}
		
	return noOldBill;	
	}
	private int getTotalAmountPaid(){
		return totalAmount;
	}
	private void getOldBills() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		oldBills=new ArrayList();
		oldBills=db.getAllOldBillList(statement);
	}
}
