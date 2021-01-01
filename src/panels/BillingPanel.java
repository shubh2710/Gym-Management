package panels;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.BillPrinter;
import extra_classes.CircleImage;
import extra_classes.DateLabelFormatter;
import extra_classes.ReceiptPrinter;
import extra_classes.genrateUniqueID;
import models.ActiveBillModel;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;

public class BillingPanel extends JPanel {
	private JPanel right, left,topRight;
	private JButton createNew;
	private JTextField amtPaid, amtPending, sessionFrom, sessionTo, status, Plan;
	private JLabel profile, name, email, uid, bid, joiningDate;
	private JTextField search;
	private final DefaultListModel users = new DefaultListModel();;
	private User user = null;
	private int UserListIndex;
	private ActiveBillModel Bill = null;
	private ArrayList<PlansModel> plansList;
	private ArrayList<ActiveBillModel> billList;
	private JComboBox<String> plans;
	private DefaultComboBoxModel planModel;
	private ArrayList<User> userList;
	private ArrayList<User> searchedUserList;

	public BillingPanel() {
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}

	private void setupUI() {
		getAllData();
		left = new JPanel();
		right = new JPanel();
		topRight = new JPanel();

		setLayout(new BorderLayout());
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.CENTER);
		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			topRight.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			left.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			right.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
			topRight.setBackground(Color.decode("#ffffff"));
			left.setBackground(Color.decode("#ffffff"));
			right.setBackground(Color.decode("#ffffff"));
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		left.setPreferredSize(new Dimension(250, 400));
		right.setPreferredSize(new Dimension(100, 400));

		addComponentLeft();
		addComponentRight();
	}

	private void getAllData() {
		// TODO Auto-generated method stub
		plansList = new ArrayList();
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		plansList = db.getPlansList(statement);
		billList = db.getActiveBillList(statement);
	}

	private void resetFields() {
		// TODO Auto-generated method stub
		amtPaid.setText("");
		amtPending.setText("");
		Plan.setText("");
		sessionFrom.setText("");
		sessionTo.setText("");
		status.setText("");
	}

	public void setEnableOrDisable(boolean value) {
		if (value && user != null) {

			amtPaid.setEnabled(true);

			amtPending.setEnabled(true);
			amtPaid.setEnabled(true);
			status.setEnabled(true);
			sessionFrom.setEnabled(true);
			sessionTo.setEnabled(true);
			Plan.setEnabled(true);
			amtPaid.setBackground(Color.white);

			amtPending.setBackground(Color.white);
			// status.setBackground(Color.white);
			sessionFrom.setBackground(Color.white);
			sessionTo.setBackground(Color.white);
			Plan.setBackground(Color.white);

		} else {
			Color c = new Color(50, 70, 66);

			amtPaid.setEnabled(false);

			amtPending.setEnabled(false);
			status.setEnabled(false);
			sessionFrom.setEnabled(false);
			sessionTo.setEnabled(false);
			Plan.setEnabled(false);

			amtPaid.setBackground(c);

			amtPending.setBackground(c);
			amtPaid.setBackground(c);
			// status.setBackground(c);
			sessionFrom.setBackground(c);
			sessionTo.setBackground(c);

		}

	}

	protected void removeUser(String uid) {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like remove user ?", "Warning", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			if (db.removeUser(uid, statement)) {
				JOptionPane.showMessageDialog(null, new JLabel("User Removed"));
				getUserList();
			} else
				JOptionPane.showMessageDialog(null, new JLabel("Can't Remove"));
		}
	}

	private void addComponentRight() {

		Color c = new Color(50, 70, 66);
		amtPaid = new JTextField(40);
		amtPaid.setBackground(c);
		amtPaid.setEnabled(false);
		amtPending = new JTextField(40);
		amtPending.setBackground(c);
		amtPending.setEnabled(false);
		sessionFrom = new JTextField(40);
		sessionFrom.setBackground(c);
		sessionFrom.setEnabled(false);

		status = new JTextField(40);
		status.setBackground(c);
		status.setEnabled(false);
		sessionTo = new JTextField(40);
		sessionTo.setBackground(c);
		sessionTo.setEnabled(false);
		status = new JTextField(40);
		status.setEnabled(false);
		status.setBackground(c);
		Plan = new JTextField(40);
		Plan.setBackground(c);
		Plan.setEnabled(false);

		createNew = new JButton("Create New");
		createNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				UpdateBill(Bill);
			}

			private void UpdateBill(ActiveBillModel bill) {
				// TODO Auto-generated method stub
				boolean ex=true;
				if(Bill!=null)
				ex=Bill.getSessionTo().after(new Date());
				
				int dialogResult = JOptionPane.showConfirmDialog(null, getPanel(ex),
						"Create New Plan : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					PlansModel plan = plansList.get(plans.getSelectedIndex());
					if (Bill != null) {
						if (updateBill(plan, user.getUid(), Bill, Bill.getSessionTo().after(new Date()))) {
							getUserList();
							getAllData();
							setValuse(UserListIndex);
						}
					} else {
						if (newBill(plan, user.getUid())) {
							getUserList();
							getAllData();
							setValuse(UserListIndex);
						}
					}
				}
			}

			private JPanel getPanel(boolean isActive) {
				JPanel layout = new JPanel();
				layout.setLayout(new GridBagLayout());
				Vector comboBoxItems = new Vector();
				for (PlansModel p : plansList) {
					comboBoxItems.add(p.getName());
				}
				planModel = new DefaultComboBoxModel(comboBoxItems);
				plans = new JComboBox(planModel);
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.anchor = GridBagConstraints.NORTH;
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = 0;
				if (!isActive)
					layout.add(new JLabel("Current Plan Will be removed!"), constraints);
				else
					layout.add(new JLabel("Plan is Active! New Plan Start from Today"), constraints);
				constraints.gridy = 1;
				layout.add(new JLabel("Name :" + user.getName()), constraints);
				constraints.gridy = 2;
				layout.add(new JLabel("Reg ID :" + user.getReg_no()), constraints);
				constraints.gridy = 3;
				if (Bill != null)
					layout.add(new JLabel("Bill Id:" + Bill.getBid()), constraints);
				else
					layout.add(new JLabel("Bill Id:Create New"), constraints);
				constraints.gridy = 4;
				layout.add(plans, constraints);
				return layout;
			}
		});

		right.setLayout(new GridBagLayout());
		profile = new JLabel();
		JPanel TopRight = getTopRight();
		setProfile("/images/profile-pictures.png",false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		right.add(profile, constraints);
		constraints.insets = new Insets(5, 5, 30, 5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		right.add(TopRight, constraints);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		right.add(new JLabel("CURRENT PLAN"), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		right.add(new JLabel("Selected Plan:"), constraints);
		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 2;

		right.add(Plan, constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		right.add(new JLabel("Amount Paid:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 3;
		right.add(amtPaid, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		right.add(new JLabel("Amount Pending:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 4;
		right.add(amtPending, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 5;
		right.add(new JLabel("Session From:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 5;
		right.add(sessionFrom, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 6;
		right.add(new JLabel("Session To:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 6;
		right.add(sessionTo, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 7;
		right.add(new JLabel("Current Status:"), constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 7;
		right.add(status, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 8;
		right.add(new JLabel("Create New Plan:"), constraints);
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		right.add(createNew, constraints);

		constraints.insets = new Insets(5, 110, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		JButton print = new JButton("Print Bill");
		right.add(print, constraints);
		constraints.insets = new Insets(5, 210, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		JButton sms = new JButton("SEND SMS");
		right.add(sms, constraints);
		sms.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//SMSapi api=new SMSapi();
				if(user!=null){
					/*if(api.send(user.getMob(), "mesage")){
						System.out.println("sms send");
						JOptionPane.showMessageDialog(null,new JLabel("SMS SEND TO " +user.getMob()));
					}else{
						JOptionPane.showMessageDialog(null,new JLabel("SMS NOT SEND CHECK YOUR INTERNET CONNECTION OR MOBBILE NO. OR API SETUP"));
						System.out.println("sms not send");
						}*/
				}
			}
			
		});
		print.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				  PrinterJob pj = PrinterJob.getPrinterJob();      
				  
				ReceiptPrinter	print =new ReceiptPrinter(user, Bill);
				 pj.setPrintable(print,getPageFormat(pj));
				  try {
			             pj.print();
			             JOptionPane.showMessageDialog(null, new JLabel("Bill Created"));
			          
			        }
			         catch (PrinterException ex) {
			                 ex.printStackTrace();
			                 JOptionPane.showMessageDialog(null, new JLabel("Can't Created Bill"));
			        }
				
			}

		});
	}
    public PageFormat getPageFormat(PrinterJob pj)
{
    
    PageFormat pf = pj.defaultPage();
    Paper paper = pf.getPaper();    

    double middleHeight =8.0;  
    double headerHeight = 2.0;                  
    double footerHeight = 2.0;                  
    double width = convert_CM_To_PPI(8);      //printer know only point per inch.default value is 72ppi
    double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
    paper.setSize(width, height);
    paper.setImageableArea(                    
        0,
        10,
        width,            
        height - convert_CM_To_PPI(1)
    );   //define boarder size    after that print area width is about 180 points
            
    pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
    pf.setPaper(paper);    

    return pf;
}
    
    protected static double convert_CM_To_PPI(double cm) {            
	        return toPPI(cm * 0.393600787);            
}
 
protected static double toPPI(double inch) {            
	        return inch * 72d;            
}

	protected boolean newBill(PlansModel plan2, String uid2) {
		// TODO Auto-generated method stub
		boolean isadd = false;
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		String bid = new genrateUniqueID("ActiveBill").getUID();
		Date sessionFrom, sessionTo;
		Date today = new Date();
		sessionFrom = today;
		sessionTo = addMonth(plan2.getNoOfMonth());
		int amountPending = 0;
		int amountPaid = plan2.getAmt();
		ActiveBillModel activeBill = new ActiveBillModel(bid, uid2, sessionFrom, sessionTo, amountPending, amountPaid);
		if (db.addBill(activeBill, statement))
			isadd = db.updateUserPlan(uid2, plan2.getPid(), statement);
		return isadd;
	}

	protected boolean updateBill(PlansModel plan2, String uid2, ActiveBillModel bill, boolean isActive) {
		// TODO Auto-generated method stub
		boolean isadd = false;
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		
		
		Date sessionFrom, sessionTo;
		Date today = new Date();
		if (isActive)
			sessionFrom = today;
		else
			sessionFrom = bill.getSessionTo();
		sessionTo = updateMonth(plan2.getNoOfMonth(), sessionFrom);
		int amountPending = 0;
		int amountPaid = plan2.getAmt();
		ActiveBillModel activeBill = new ActiveBillModel(bill.getBid(), bill.getUid(), sessionFrom, sessionTo,
				amountPending, amountPaid);
		if (db.updateBill(activeBill, statement)){
			isadd = db.updateUserPlan(uid2, plan2.getPid(), statement);
			String oid = new genrateUniqueID("ActiveBill").getUID();
			if (db.addOldBill(oid, activeBill, statement))
				System.out.println("old bill created");
			}
		return isadd;
	}

	public Date addMonth(int no) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, no);
		return cal.getTime();
	}

	public Date updateMonth(int no, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, no);
		return cal.getTime();
	}

	private JPanel getTopRight() {
				topRight.setLayout(new GridBagLayout());

		joiningDate = new JLabel("Joining Date :");
		joiningDate.setForeground(Color.white);
		name = new JLabel("Name :");
		name.setForeground(Color.white);
		email = new JLabel("Email :");
		email.setForeground(Color.white);
		uid = new JLabel("Reg ID :");
		uid.setForeground(Color.white);
		bid = new JLabel("Bill Id :");
		bid.setForeground(Color.white);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		topRight.add(name, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		topRight.add(email, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		topRight.add(uid, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		topRight.add(joiningDate, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		topRight.add(bid, constraints);
		return topRight;
	}

	private void addComponentLeft() {

		search = new JTextField();
		search.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				// System.out.println("chain");
			}

			@Override
			public void insertUpdate(DocumentEvent de) {
				// TODO Auto-generated method stub
				System.out.println("insertchain");
				searchUser(search.getText().toString());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				searchUser(search.getText().toString());
			}

		});
		left.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		left.add(search, constraints);
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		JButton searchButton = new JButton("Search");
		left.add(searchButton, constraints);

		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.weighty = 1;
		constraints.ipadx = 210;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.gridx = 0;
		constraints.gridy = 2;

		users.addElement("None");
		getUserList();
		final JList userList = new JList(users);
		userList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					try {
						JList source = (JList) lse.getSource();
						String selected = source.getSelectedValue().toString();
						UserListIndex = source.getSelectedIndex();
						setValuse(UserListIndex);
						setEnableOrDisable(false);
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}
			}

		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane ListScrollPane = new JScrollPane(userList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		left.add(ListScrollPane, constraints);
	}

	protected void setValuse(int index) {
		user = searchedUserList.get(index);
		Bill = getUserBill(user.getUid());
		System.out.println(user.getName());
		name.setText("Name :" + user.getName());
		email.setText("Email :" + user.getEmail());
		if (Bill != null)
			bid.setText("Bill Id :" + Bill.getBid() + "");
		uid.setText("Reg ID :" + user.getReg_no() + "");
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		joiningDate.setText("Joining Date :" + f.format(user.getJoiningDate()));
		setProfile(user.getProfile(),true);
		System.out.println(user.getProfile());
		if (Bill != null) {
			Plan.setText(plansList.get(getSelectedPlan(user.getPlan())).getName());
			amtPaid.setText(Bill.getAmountPaid() + "");
			amtPending.setText(Bill.getAmountPending() + "");

			sessionFrom.setText(f.format(Bill.getSessionFrom()) + "");
			sessionTo.setText(f.format(Bill.getSessionTo()) + "");
			if (Bill.getSessionTo().after(new Date())) {
				status.setText("Active");
				status.setBackground(new Color(45, 180, 100));
			} else {
				status.setText("Expire");
				status.setBackground(new Color(150, 45, 60));
			}
		}
	}

	private ActiveBillModel getUserBill(String uid) {
		// TODO Auto-generated method stub
		ActiveBillModel bill = null;
		for (ActiveBillModel b : billList) {
			System.out.println(b.getUid());
			System.out.println(uid);
			if (b.getUid().equals(uid)) {
				bill = b;
			}
		}
		return bill;
	}

	private int getSelectedPlan(String pid) {
		int j = 0;
		for (int i = 0; i < plansList.size(); i++) {
			if (plansList.get(i).getPid().equals(pid)) {
				j = i;
			}
		}
		return j;
	}

	protected void getUserList() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		userList = new ArrayList();
		searchedUserList = new ArrayList();
		searchedUserList = db.getUserList(statement);
		userList = db.getUserList(statement);
		users.removeAllElements();
		for (User u : userList) {
			users.addElement(u.getName());
		}
	}

	private void searchUser(String search) {
		users.removeAllElements();
		searchedUserList.clear();
		;
		for (User u : userList) {
			String reg=u.getReg_no()+"";
			if (u.getName().contains(search) || reg.contains(search)) {
				users.addElement(u.getName());
				searchedUserList.add(u);
			}
		}
	}

	private void setProfile(String path,boolean isProfile) {
		CircleImage c = new CircleImage(path,isProfile);
		if (user != null)
			user.setProfile(path);
		BufferedImage Bufimage = c.getCircleImage();
		Image newimg = Bufimage.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		profile.setIcon(imageIcon);
	}

}
