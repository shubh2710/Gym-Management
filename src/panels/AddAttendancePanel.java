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
import extra_classes.BillPrinter;
import extra_classes.CircleImage;
import extra_classes.DateLabelFormatter;
import extra_classes.ReceiptPrinter;
import extra_classes.genrateUniqueID;
import models.ActiveBillModel;
import models.PlansModel;
import models.User;

public class AddAttendancePanel extends JPanel {
	private JPanel right;
	private JTextField amtPaid, amtPending, sessionFrom, sessionTo, status, Plan;
	private JLabel profile, name, email, uid, bid, joiningDate;
	private User user = null;
	private ActiveBillModel Bill = null;
	private ArrayList<PlansModel> plansList;
	private ArrayList<ActiveBillModel> billList;
	private JComboBox<String> plans;
	private DefaultComboBoxModel planModel;
	private ArrayList<User> userList;
private int reg_no=0;
	public AddAttendancePanel(int reg) {
		this.reg_no=reg;
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}

	private void setupUI() {
		getAllData();
		right = new JPanel();
		setLayout(new BorderLayout());
		add(right, BorderLayout.CENTER);
		right.setBackground(new Color(90, 150, 100));
		right.setPreferredSize(new Dimension(100, 400));
		addComponentRight();
		getUserList();
		resetFields();
		setValuse(getUser(this.reg_no));
	}

	private User getUser(int reg_no) {
		User user=null;
		for(User u:userList){
			if(u.getReg_no()==reg_no){
				user=u;
			}
		}
		return user;
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
		JPanel topRight = new JPanel();
		topRight.setBackground(new Color(90, 150, 100));
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
	protected void setValuse(User u) {
		// add users
		user =u;
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
		userList = db.getUserList(statement);
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
