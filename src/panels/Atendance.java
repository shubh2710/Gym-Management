
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
import extra_classes.Cal;
import extra_classes.CircleImage;
import extra_classes.DateLabelFormatter;
import extra_classes.Dates;
import extra_classes.GetDates;
import extra_classes.genrateUniqueID;
import interfaces.CalClick;
import models.ActiveBillModel;
import models.AttendanceModel;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;

public class Atendance extends JPanel implements CalClick {
	private JPanel right, left,top,j;
	private JButton addAten;
	private JTextField search;
	private JLabel name ,uid,noOfTime,dateOfJoin,timing,selectedDay;
	private final DefaultListModel users = new DefaultListModel();
	private User user = null;
	private int UserListIndex;
	private ArrayList<User> userList;
	private ArrayList<User> searchedUserList;
	private ArrayList<Dates> dates;
	private ArrayList<AttendanceModel> AttList;
	private Cal cal;
	private JLabel profile;

	public Atendance() {
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}
	private void setupUI() {
		left = new JPanel();
		right = new JPanel();
		top=new JPanel();
		j=new JPanel();
		
		setLayout(new BorderLayout());
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.CENTER);
		

		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			cal=new Cal(this,prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff"));
			setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			j.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			top.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			left.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			right.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
			setBackground(Color.decode("#ffffff"));
			cal=new Cal(this, "#ffffff");
			top.setBackground(Color.decode("#ffffff"));
			j.setBackground(Color.decode("#ffffff"));
			left.setBackground(Color.decode("#ffffff"));
			right.setBackground(Color.decode("#ffffff"));
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		
		left.setPreferredSize(new Dimension(250, 400));
		right.setPreferredSize(new Dimension(100, 400));
		
		addComponentLeft();
		right.setLayout(new BorderLayout());
		right.add(addTopComponent(),BorderLayout.NORTH);
		addRightCalendar();
	}
	private JPanel addTopComponent() {
		// TODO Auto-generated methodstub
		
		top.setLayout(new GridBagLayout());
		name=new JLabel("-");
		name.setForeground(Color.white);
		uid=new JLabel("-");
		uid.setForeground(Color.white);
		noOfTime=new JLabel("-");
		noOfTime.setForeground(Color.white);
		dateOfJoin=new JLabel("-");
		dateOfJoin.setForeground(Color.white);
		noOfTime=new JLabel("-");
		timing=new JLabel("-");
		timing.setForeground(Color.white);
		selectedDay=new JLabel("-");
		selectedDay.setForeground(Color.white);
		profile=new JLabel();
		setProfile("/images/profile-pictures.png",false);
		noOfTime.setForeground(Color.white);
		
		addAten=new JButton("Add Today's Attendance");
		addAten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
					int dialogResult = JOptionPane.showConfirmDialog(null, new AddAttendancePanel(user.getReg_no()),
							"Add Attendance : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						addAttendance(user.getUid());
						}
				
				//if(user!=null){	
				//addAttendance(user.getUid());
				//}
				}
			}
		);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		//constraints.weightx=1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		top.add(new JLabel("--:User Datils:--"),constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		top.add(profile,constraints);
		constraints.insets = new Insets(5, 5, 5, 200);
		constraints.gridx = 2;
		constraints.gridy = 0;
		top.add(addAten,constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		top.add(new JLabel("Name:"),constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		top.add(name,constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		top.add(new JLabel("Reg ID:"),constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		top.add(uid,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		top.add(new JLabel("--:Attendance Details:--"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 4;
		top.add(new JLabel("Date:"),constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		top.add(selectedDay,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 5;
		top.add(new JLabel("Timing:"),constraints);
		constraints.weightx=1;
		constraints.gridx = 1;
		constraints.gridy = 5;
		top.add(timing,constraints);

		
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 5, 5, 210);
		constraints.weightx=0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		//top.add(addAten,constraints);
		return top;
	}
	private ActiveBillModel getUserBill(String uid) {
		ActiveBillModel bill=null;
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		if(user!=null)
		bill=db.getActiveBillListUser(statement,uid);
		return bill;
	}
	protected void addAttendance(String uid) {
		// TODO Auto-generated method stub
		myDatabaseHandler db=new myDatabaseHandler();
		Statement statment =db.getStatement();
		//db.createAtendanceTable(statment);
		String aid=new genrateUniqueID("attendance").getUID();
		GetDates date=new GetDates(new Date());
		ActiveBillModel bill = getUserBill(user.getUid());
		AttendanceModel attendance=new AttendanceModel(aid,user.getUid(),new Date().getTime(),true,date.getDay(),date.getMonth(),date.getYear());
		if(checkSession(bill)){
				if(db.addAttendance(attendance,statment)){
					JOptionPane.showMessageDialog(null, new JLabel("Attendance Added"));
				}
				else{
					JOptionPane.showMessageDialog(null, new JLabel("Already added !"));
				}
		}else{
			JOptionPane.showMessageDialog(null, new JLabel(""));
			int dialogButton=JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Can't Added Attendance session expired ! Want to add Attendance ?", "Warning" ,dialogButton);
					if(dialogResult==JOptionPane.YES_OPTION){
						if(db.addAttendance(attendance,statment)){
							JOptionPane.showMessageDialog(null, new JLabel("Attendance Added"));
						}
						else{
							JOptionPane.showMessageDialog(null, new JLabel("Already added !"));
						}
						
					}else if(dialogResult==JOptionPane.NO_OPTION){
							
					}
			
			
		}
	}
	private boolean checkSession(ActiveBillModel bill) {
		boolean isActive=false;
		if (bill.getSessionTo().after(new Date())) {
			isActive=true;	
		}
		return isActive;
	}
	private void addRightCalendar() {
		
		
		cal.makeCal(dates);
		
		j.setPreferredSize(new Dimension(260,350));
		right.add(j,BorderLayout.WEST);
		right.add(cal,BorderLayout.CENTER);
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
		name.setText(user.getName());
		uid.setText(user.getReg_no()+"");
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		dateOfJoin.setText(f.format(user.getJoiningDate()));
		noOfTime.setText("-");
		timing.setText("-");
		selectedDay.setText("-");
		makeDatesList();
		setProfile(user.getProfile(),true);
			
	}
	private void setProfile(String path,boolean isProfile) {
		CircleImage c = new CircleImage(path,isProfile);
		if (user != null){
			user.setProfile(path);
		System.out.println(user.getProfile());
		}
		BufferedImage Bufimage = c.getCircleImage();
		Image newimg = Bufimage.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		profile.setIcon(imageIcon);
	}


	private void makeDatesList() {
		// TODO Auto-generated method stub
		dates=new ArrayList();
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		AttList=db.getUserAttendanceDates(user.getUid(),statement);
		for(AttendanceModel att:AttList){
			dates.add(new Dates(att.getDay(),att.getMonth(),att.getYear()));
		}
		setCalender();
	}
	private void setCalender() {
		cal.makeCal(dates);
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
		for (User u : userList) {
			String reg=u.getReg_no()+"";
			if (u.getName().contains(search) || reg.contains(search)) {
				users.addElement(u.getName());
				searchedUserList.add(u);
			}
		}
	}
	@Override
	public void getdate(int day, int month, int year) {
		// TODO Auto-generated method stub
		timing.setText("Not Come");
		String times="";
		selectedDay.setText(day+"/"+month+"/"+year);
		for(AttendanceModel att:AttList){
			if(att.getDay()==day && att.getMonth()==month && att.getYear()==year){
				DateFormat t = new SimpleDateFormat("hh:mm a");
				times=times+t.format(att.getDate())+", ";
			}
		}
		if(times.length()>0)
		timing.setText(times);
	}

}
