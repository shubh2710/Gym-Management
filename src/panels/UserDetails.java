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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import extra_classes.CircleImage;
import extra_classes.DateLabelFormatter;
import extra_classes.genrateUniqueID;
import interfaces.WebCamReply;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;
import snippet.Snippet;

public class UserDetails extends JPanel implements WebCamReply{
	private JPanel right, left;
	private JButton update, remove, edit, selectProfile;
	private JTextField plansText,reg_no,name, email, mob, weight, selectedPlan, age, dob,address;
	private JLabel profile;
	private JTextField search;
	private JDatePickerImpl datePicker;
	private final DefaultListModel users = new DefaultListModel();;
	private JFileChooser filePicker;
	private File selectedFile;
	private User user = null;
	private DefaultComboBoxModel planModel;
	private ArrayList<PlansModel> plansList;
	private ArrayList<User> userList;
	private ArrayList<User> searchedUserList;

	public UserDetails() {
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
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				plansList = db.getPlansList(statement);
			}
		}).start();
	}

	protected void addUser(String name, String email, String weight, String age, String mob, String dob,String reg,String address) {
		int r=0,w = 0, a = 0;
		if (user != null)
			try {
				w = Integer.parseInt(weight);
				a = Integer.parseInt(age);
				r=Integer.parseInt(reg);
				String uid = user.getUid();
				myDatabaseHandler db = new myDatabaseHandler();
				String planUid = user.getPlan();
				Statement statement = db.getStatement();
				//User u = new User(name, uid, email, mob, user.getProfile(), planUid, w, a, dob,r, user.getJoiningDate());
				User u = new User(dob, name, uid, email,mob, user.getProfile(),a, w, planUid,r,user.getJoiningDate(),address);
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Update user ?", "Warning",
						dialogButton);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (db.updateUser(u, statement)) {
						JOptionPane.showMessageDialog(null, new JLabel("User Updated"));
						setEnableOrDisable(false);
						getUserList();
					} else {
						JOptionPane.showMessageDialog(null, new JLabel("Can't Update User"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
			}
	}

	private void resetFields() {
		// TODO Auto-generated method stub
		name.setText("");
		reg_no.setText("");
		email.setText("");
		weight.setText("");
		age.setText("");
		mob.setText("");
		plansText.setText("");
		address.setText("");
	}

	public void setEnableOrDisable(boolean value) {
		if (value && user != null) {
			address.setEditable(true);
			update.setEnabled(true);
			name.setEnabled(true);
			reg_no.setEnabled(false);
			selectProfile.setEnabled(true);
			mob.setEnabled(true);
			dob.setEnabled(true);
			age.setEnabled(true);
			weight.setEnabled(true);
			email.setEnabled(true);
			plansText.setEnabled(false);
			plansText.setBackground(Color.white);
			reg_no.setBackground(Color.white);
			name.setBackground(Color.white);
			selectProfile.setEnabled(true);
			mob.setBackground(Color.white);
			dob.setBackground(Color.white);
			age.setBackground(Color.white);
			weight.setBackground(Color.white);
			email.setBackground(Color.white);
			address.setBackground(Color.WHITE);

		} else {
			Color c = new Color(50, 70, 66);
			address.setEditable(false);
			update.setEnabled(false);
			name.setEnabled(false);
			reg_no.setEnabled(false);
			reg_no.setBackground(c);
			selectProfile.setEnabled(false);
			mob.setEnabled(false);
			dob.setEnabled(false);
			age.setEnabled(false);
			weight.setEnabled(false);
			email.setEnabled(false);
			plansText.setEnabled(false);
			plansText.setBackground(c);
			name.setBackground(c);
			selectProfile.setEnabled(false);
			mob.setBackground(c);
			dob.setBackground(c);
			age.setBackground(c);
			weight.setBackground(c);
			email.setBackground(c);
			address.setBackground(c);

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
	private void addComponentRight() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setEnabled(false);
		Date date = new Date();
		update = new JButton("Update");
		update.setEnabled(false);
		plansText=new JTextField();
		address=new JTextField();
		plansText.setEnabled(false);
		
		remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (user != null)
					removeUser(user.getUid());
			}
		});

		edit = new JButton("Edit");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setEnableOrDisable(true);
			}
		});
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				addUser(name.getText().toString(), email.getText().toString(), weight.getText().toString(),
						age.getText().toString(), mob.getText().toString(),
						datePicker.getJFormattedTextField().getText().toString(),reg_no.getText().toString(),address.getText().toString());
			}
		});
		Color c = new Color(50, 70, 66);
		reg_no=new JTextField(40);
		reg_no.setBackground(c);
		reg_no.setEnabled(false);
		address.setEnabled(false);
		address.setBackground(c);
		name = new JTextField(40);
		name.setBackground(c);
		name.setEnabled(false);
		email = new JTextField(40);
		email.setBackground(c);
		email.setEnabled(false);
		weight = new JTextField(40);
		weight.setBackground(c);
		weight.setEnabled(false);
		plansText.setBackground(c);
		selectedPlan = new JTextField(40);
		age = new JTextField(40);
		age.setBackground(c);
		age.setEnabled(false);
		dob = new JTextField(40);
		dob.setEnabled(false);

		mob = new JTextField(40);
		mob.setBackground(c);
		mob.setEnabled(false);

		selectProfile = new JButton("Change");
		selectProfile.setEnabled(false);

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
		right.setLayout(new GridBagLayout());
		profile = new JLabel();
		setProfile("/images/profile-pictures.png",false);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.SOUTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		right.add(profile, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		right.add(selectProfile, constraints);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		right.add(update, constraints);
		constraints.insets = new Insets(5, 100, 5, 5);

		right.add(edit, constraints);
		constraints.insets = new Insets(5, 200, 5, 5);

		right.add(remove, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		right.add(new JLabel("Reg No:"), constraints);
		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 2;
		right.add(reg_no, constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		right.add(new JLabel("Name:"), constraints);
		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 3;

		right.add(name, constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		right.add(new JLabel("Email:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 4;
		right.add(email, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 5;
		right.add(new JLabel("Weight:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 5;
		right.add(weight, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 6;
		right.add(new JLabel("Age:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 6;
		right.add(age, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 7;
		right.add(new JLabel("Mobile No:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 7;
		right.add(mob, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 8;
		right.add(new JLabel("Address:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 8;
		right.add(address, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 9;
		right.add(new JLabel("Seleted Plan:"), constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 9;
		right.add(plansText, constraints);

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 10;
		right.add(new JLabel("Date Of Birth:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 10;
		constraints.anchor = GridBagConstraints.WEST;
		right.add(datePicker, constraints);
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
						int index = source.getSelectedIndex();
						System.out.println(index);
						setValuse(index);
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
		// TODO Auto-generated method stub
		user = searchedUserList.get(index);
		name.setText(user.getName());
		reg_no.setText(user.getReg_no()+"");
		email.setText(user.getEmail());
		weight.setText(user.getWeight() + "");
		age.setText(user.getAge() + "");
		mob.setText(user.getMob());
		datePicker.getJFormattedTextField().setText(user.getDob());
		
		if(user.getProfile().equals("no"))
			setProfile("/images/profile-pictures.png",false);
		else
			setProfile(user.getProfile(),true);
		System.out.println(user.getPlan());
		plansText.setText(getSelectedPlan(user.getPlan()).getName());
		address.setText(user.getAddress());
		
	}

	private PlansModel getSelectedPlan(String string) {
		PlansModel p=null;
		for (int i = 0; i < plansList.size(); i++) {
			if (plansList.get(i).getPid().equals(string)) {
				p=plansList.get(i);
			}
		}
		return p;
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
	private void setProfile(String path,boolean isProfile) {
		CircleImage c = new CircleImage(path,isProfile);
		if (user != null){
			user.setProfile(path);
		System.out.println(user.getProfile()+" yaha");
		}
		BufferedImage Bufimage = c.getCircleImage();
		Image newimg = Bufimage.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		profile.setIcon(imageIcon);
	}

	@Override
	public void camReply(String path) {
		// TODO Auto-generated method stub
		setProfile(path,true);
	}

}
