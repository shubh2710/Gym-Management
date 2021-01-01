package panels;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.CircleImage;
import extra_classes.DateLabelFormatter;
import extra_classes.genrateUniqueID;
import interfaces.WebCamReply;
import models.ActiveBillModel;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;
import snippet.Snippet;

public class RegisterUser extends JPanel implements WebCamReply{
	private JPanel left, right;
	private JButton add, selectProfile;
	private JLabel joiningDate;
	private long jonning;
	private JTextField reg_no,name, email, mob, weight, selectedPlan, age, dob,address;
	private JLabel profile;
	private JFileChooser filePicker;
	private File selectedFile=null;
	private JComboBox<String> plans;
	private DefaultComboBoxModel planModel;
	private ArrayList<PlansModel> plansList;
	private Calendar cal = Calendar.getInstance();

	public RegisterUser() {
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}
	private void setupUI() {
		getAllPlans();
		left = new JPanel();
		right = new JPanel();
		setLayout(new BorderLayout());
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.CENTER);
		
		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			left.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			right.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
			left.setBackground(Color.decode("#ffffff"));
			right.setBackground(Color.decode("#ffffff"));
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		left.setPreferredSize(new Dimension(250, 400));
		right.setPreferredSize(new Dimension(100, 400));
		try {
			String path = "C:gym_manngment";
			Files.createDirectories(Paths.get(path));
			filePicker = new JFileChooser(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addComponentLeft();
		addComponentRight();
	}

	private void getAllPlans() {
		// TODO Auto-generated method stub
		plansList = new ArrayList();
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		plansList = db.getPlansList(statement);
		Vector comboBoxItems = new Vector();
		for (PlansModel p : plansList) {
			comboBoxItems.add(p.getName());
		}
		planModel = new DefaultComboBoxModel(comboBoxItems);
		plans = new JComboBox(planModel);
	}

	protected void addUser(String name, String email, String weight, String age, String mob, String dob, long date,String reg,String address) {
		int r=0,w = 0, a = 0;
		User u=null;
		try {
			w = Integer.parseInt(weight);
			a = Integer.parseInt(age);
			r=Integer.parseInt(reg);
			String uid = new genrateUniqueID("user").getUID();
			myDatabaseHandler db = new myDatabaseHandler();
			String planUid = plansList.get(plans.getSelectedIndex()).getPid();
			Statement statement = db.getStatement();
			//db.createUserActiveBills(statement);
			//db.createUserTable(statement);
			String Profilepath="";
			
			if(dob!=null && name !=null && uid !=null && email!=null &&  planUid!=null){
				if(selectedFile!=null)
					// find sloutuon for image path
				u = new User(dob, name, uid, email,mob, selectedFile.getAbsolutePath(),a, w, planUid,r,new Date(date),address);
				else
				u = new User(dob, name, uid, email,mob,"/images/profile-pictures.png",a, w, planUid,r,new Date(date),address);
				
				System.out.println(u.getProfile()+"profile user");
			}
			if(u!=null)
			  if (db.addUser(u, statement)) {
				if (createBill(uid, plansList.get(plans.getSelectedIndex()))) {
					JOptionPane.showMessageDialog(null, new JLabel("User Added"));
					 Preferences prefs = new getPrefsSingletan().getPrefs();
					 prefs.putInt(Keys.PREF_REG_NO,r+1);
					resetFields();
				} else
					JOptionPane.showMessageDialog(null, new JLabel("Can't Add Bill"));
			} else {
				JOptionPane.showMessageDialog(null, new JLabel("Can't Add User try to change email or reg_no"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
		}

	}

	private boolean createBill(String uid, PlansModel plan) {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		String bid = new genrateUniqueID("ActiveBill").getUID();
		Date sessionFrom, sessionTo;
		// joinning date
		Date today = new Date(jonning);
		sessionFrom = today;
		sessionTo = addMonth(plan.getNoOfMonth(),today);
		int amountPending = 0;
		int amountPaid = plan.getAmt();
		ActiveBillModel activeBill = new ActiveBillModel(bid, uid, sessionFrom, sessionTo, amountPending, amountPaid);
		String oid = new genrateUniqueID("ActiveBill").getUID();
		if (db.addOldBill(oid, activeBill, statement)){
			System.out.println("old bill created");
		}
		return db.addBill(activeBill, statement);
	}

	public Date addMonth(int no,Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, no);
		return cal.getTime();
	}

	private void resetFields() {
		// TODO Auto-generated method stub
		reg_no.setText("");
		name.setText("");
		email.setText("");
		weight.setText("");
		age.setText("");
		mob.setText("");
		address.setText("");
	}

	private void addComponentRight() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		add = new JButton("Save");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addUser(name.getText().toString(), email.getText().toString(), weight.getText().toString(),
						age.getText().toString(), mob.getText().toString(),
						datePicker.getJFormattedTextField().getText(), jonning,reg_no.getText().toString(),address.getText().toString());
			}
		});
		reg_no=new JTextField(40);
		 Preferences prefs = new getPrefsSingletan().getPrefs();
		 int no= prefs.getInt(Keys.PREF_REG_NO,1000);
		 reg_no.setText(no+"");
		 address=new JTextField(40);
		 name = new JTextField(40);
		email = new JTextField(40);
		weight = new JTextField(40);
		selectedPlan = new JTextField(40);
		age = new JTextField(40);
		dob = new JTextField(40);
		mob = new JTextField(40);
		right.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		right.add(new JLabel("Reg Id:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		right.add(reg_no, constraints);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		right.add(new JLabel("Name:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		right.add(name, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		right.add(new JLabel("Email:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 2;
		right.add(email, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		right.add(new JLabel("Weight:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 3;
		right.add(weight, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		right.add(new JLabel("Age:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 4;
		right.add(age, constraints);

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 5;
		right.add(new JLabel("Mobile No:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 5;
		right.add(mob, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 6;
		right.add(new JLabel("Seleted Plan:"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 6;
		right.add(plans, constraints);
		constraints.gridx = 0;
		constraints.gridy = 7;
		right.add(new JLabel("Address:"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 7;
		right.add(address, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 8;
		right.add(new JLabel("Date Of Birth:"), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.anchor = GridBagConstraints.WEST;
		right.add(datePicker, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.anchor = GridBagConstraints.WEST;
		right.add(add, constraints);
	}

public void	takePicFromFile(){
	int returnValue = filePicker.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
		selectedFile = filePicker.getSelectedFile();
		if (selectedFile != null)
			setProfile(selectedFile.getAbsolutePath(),true);
		System.out.println("we selected: " + selectedFile.getAbsolutePath());
	}
}
public void takePicFromCam(){
	Snippet cam=new Snippet();
	if(reg_no.getText().toString().length()>0)
	cam.open("C:/gym_manngment/images",reg_no.getText().toString(),this);
}
	private void addComponentLeft() {
		selectProfile = new JButton("Select");
		jonning=new Date().getTime();
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		joiningDate=new JLabel("Joinging Date:" + f.format(jonning));
		selectProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int dialogButton=JOptionPane.YES_NO_CANCEL_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Open WebCam ?", "Warning",dialogButton);
				if(dialogResult==JOptionPane.YES_OPTION){
					takePicFromCam();
				}else if(dialogResult==JOptionPane.NO_OPTION){
					takePicFromFile();	
				}
			}
		});
		left.setLayout(new GridBagLayout());
		profile = new JLabel();
		setProfile("/images/profile-pictures.png",false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		left.add(profile, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		left.add(selectProfile, constraints);
		JButton add=new JButton("+");
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		left.add(add,constraints);
		
		constraints.insets = new Insets(40, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		
		
		left.add(joiningDate, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		JButton minus=new JButton("-");
		left.add(minus,constraints);
		
		minus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				addDay(-1);
			}

		
			
		});
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addDay(1);
			}
			
		});
	}
	private void addDay(int day) {
		// TODO Auto-generated method stub
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DAY_OF_MONTH, day);
			jonning=cal.getTime().getTime();
			
			System.out.println(jonning);
			joiningDate.setText("Joinging Date:" + f.format(jonning));
	}
	public void setProfile(String path,boolean isProfile) {
		CircleImage c = new CircleImage(path,isProfile);
		BufferedImage Bufimage = c.getCircleImage();
		Image newimg = Bufimage.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		profile.setIcon(imageIcon);
	}
	@Override
	public void camReply(String path) {
		selectedFile=new File(path);
		setProfile(path,true);
	}

}
