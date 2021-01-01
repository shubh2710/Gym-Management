package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.CircleImage;
import extra_classes.genrateUniqueID;
import interfaces.WebCamReply;
import models.admin;
import preferences.getPrefsSingletan;
import snippet.Snippet;

public class AdminSettions_Right extends JPanel implements WebCamReply {

	
	private JButton update, remove, edit, selectProfile,addnew;
	private JTextField name, email, pass, userName, rePass, dob;
	private JLabel profile;
	private JFileChooser filePicker;
	private File selectedFile;
	private admin user = null;

public 	AdminSettions_Right(){
		
	try {
		String path = "C:gym_manngment";
		Files.createDirectories(Paths.get(path));
		filePicker = new JFileChooser(path);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		getAdmin();
		setup();
	
		setAdmin();
	}
protected void addAdmin(String name, String email, String username, String password, String profile,int type) {
	if (user != null)
		try {
			admin a = new admin(user.getAid(), name, email, password, username, profile,type);
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Update admin ?", "Warning",
					dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (db.updateAdmin(a, statement)) {
					JOptionPane.showMessageDialog(null, new JLabel("Admin Updated"));
					setEnableOrDisable(false);
					getAdmin();
					System.out.println(user.getProfile());
					statement.close();
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Can't Update Admin"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
		}
}
protected void addNewAdmin(String name, String email, String username, String password, String profile,int type) {
	if (user != null)
		try {
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Add New Admin ?", "Warning",
					dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (db.addAdminData(new genrateUniqueID("admin").getUID(), email, name, username, password, profile,1, statement)) {
					JOptionPane.showMessageDialog(null, new JLabel("New Admin added"));
					statement.close();
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Can't add Admin"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
		}
}
protected void setAdmin() {
	// TODO Auto-generated method stub
	System.out.println(user.getName() + " aid" + user.getAid());
	name.setText(user.getName());
	email.setText(user.getEmail());
	userName.setText(user.getUsername() + "");
	rePass.setText(user.getPassword());
	pass.setText(user.getPassword());
	setProfile(user.getProfile(),true);
	System.out.println(user.getProfile());
}
protected void getAdmin() {
	// TODO Auto-generated method stub
	myDatabaseHandler db = new myDatabaseHandler();
	Statement statement = db.getStatement();
	// db.createUserTable(statement);
	Preferences prefs = new getPrefsSingletan().getPrefs();
	user = db.getAdminData(prefs.get(Keys.PREFS_LOGIN_ADMIN, null),statement);
}

private void setProfile(String path,boolean isProfile) {
	CircleImage c = new CircleImage(path,isProfile);
	if (user != null)
		try {
			user.setProfile(path);
			BufferedImage Bufimage = c.getCircleImage();
			Image newimg = Bufimage.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
			ImageIcon imageIcon = new ImageIcon(newimg);
			profile.setIcon(imageIcon);
		} catch (Exception e) {
			e.printStackTrace();
			c = new CircleImage("/images/profile-pictures.png",false);
			BufferedImage Bufimage = c.getCircleImage();
			Image newimg = Bufimage.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
			ImageIcon imageIcon = new ImageIcon(newimg);
			profile.setIcon(imageIcon);
		}
}
private void resetFields() {
	// TODO Auto-generated method stub
	name.setText("");
	email.setText("");
	userName.setText("");
	rePass.setText("");
	pass.setText("");
}

public void setEnableOrDisable(boolean value) {
	if (value && user != null) {
		update.setEnabled(true);
		name.setEnabled(true);
		selectProfile.setEnabled(true);
		pass.setEnabled(true);
		dob.setEnabled(true);
		rePass.setEnabled(true);
		userName.setEnabled(true);
		email.setEnabled(true);
		name.setBackground(Color.white);
		pass.setBackground(Color.white);
		dob.setBackground(Color.white);
		rePass.setBackground(Color.white);
		userName.setBackground(Color.white);
		email.setBackground(Color.white);

	} else {
		Color c = new Color(50, 70, 66);
		update.setEnabled(false);
		name.setEnabled(false);
		pass.setEnabled(false);
		dob.setEnabled(false);
		rePass.setEnabled(false);
		userName.setEnabled(false);
		email.setEnabled(false);
		name.setBackground(c);
		selectProfile.setEnabled(false);
		pass.setBackground(c);
		dob.setBackground(c);
		rePass.setBackground(c);
		userName.setBackground(c);
		email.setBackground(c);
	}

}
protected void removeUser(String uid) {
	// TODO Auto-generated method stub
	myDatabaseHandler db = new myDatabaseHandler();
	Statement statement = db.getStatement();
	int dialogButton = JOptionPane.YES_NO_OPTION;
	int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like remove user ?", "Warning", dialogButton);
	if (dialogResult == JOptionPane.YES_OPTION) {
		if (db.createAdminTable(statement)) {
			Preferences prefs = prefs = new getPrefsSingletan().getPrefs();
			prefs.putBoolean(Keys.PREF_IS_SETUP, false);
			JOptionPane.showMessageDialog(null, new JLabel("User Removed"));
			System.exit(0);
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
	if(user.getEmail().length()>0)
	cam.open("C:/gym_manngment",user.getEmail(),this);
}
private void setup() {
	// TODO Auto-generated method stub
	try{
		Preferences prefs;
		prefs = new getPrefsSingletan().getPrefs();
		setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		
	}catch(Exception e){
		setBackground(Color.decode("#ffffff"));
		JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
	}
	setPreferredSize(new Dimension(100, 400));
	update = new JButton("Update");
	addnew=new JButton("Add New User");
	update.setEnabled(false);
	remove = new JButton("Remove");
	addnew.addActionListener(new ActionListener() {

		private JTextField newName,newEmail,newPassword,newUsername,newRepass;
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int dialogResult = JOptionPane.showConfirmDialog(null, getPanel(),
					"Create New Plan : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (newPassword.getText().toString().equals(newRepass.getText().toString()))
					if(newEmail.getText().toString().length()>0 && newUsername.getText().toString().length()>0  ){
						addNewAdmin(newName.getText().toString(), newEmail.getText().toString(), newUsername.getText().toString(),
									newPassword.getText().toString(), user.getProfile(),1);
					
					}
			}
			setProfile(user.getProfile(),true);
			}
			private JPanel getPanel() {
				JPanel layout = new JPanel();
				layout.setLayout(new GridBagLayout());
				newName=new JTextField(40);
				newEmail =new JTextField(40);
				newPassword=new JTextField(40);
				newRepass=new JTextField(40);
				newUsername=new JTextField(40);
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.anchor = GridBagConstraints.NORTH;
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.weightx=1;
				constraints.gridx = 0;
				constraints.gridy = 0;
				layout.add(new JLabel("Add New User"), constraints);
				constraints.gridx = 0;
				constraints.gridy = 1;
				layout.add(new JLabel("Name:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 1;
				layout.add(newName, constraints);
				constraints.gridx = 0;
				constraints.gridy = 2;
				layout.add(new JLabel("Email:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 2;
				layout.add(newEmail, constraints);
				constraints.gridx = 0;
				constraints.gridy = 3;
				layout.add(new JLabel("Username:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 3;
				layout.add(newUsername, constraints);
				constraints.gridx = 0;
				constraints.gridy = 4;
				layout.add(new JLabel("Password:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 4;
				layout.add(newPassword, constraints);
				constraints.gridx = 0;
				constraints.gridy = 5;
				layout.add(new JLabel("Re-Password:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 5;
				layout.add(newRepass, constraints);
				constraints.gridx = 0;
				constraints.gridy = 5;
				layout.add(new JLabel("Re-Password:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 7;
				layout.add(selectProfile, constraints);
				constraints.gridx = 0;
				constraints.gridy = 6;
				layout.add(profile, constraints);
				
				return layout;
		}
	});

	remove.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (user != null)
				removeUser(user.getAid());
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
			if (pass.getText().toString().equals(rePass.getText().toString()))
				addAdmin(name.getText().toString(), email.getText().toString(), userName.getText().toString(),
						rePass.getText().toString(), user.getProfile(),0);
		}
	});
	Color c = new Color(50, 70, 66);
	name = new JTextField(40);
	name.setBackground(c);
	name.setEnabled(false);
	email = new JTextField(40);
	email.setBackground(c);
	email.setEnabled(false);
	userName = new JTextField(40);
	userName.setBackground(c);
	userName.setEnabled(false);
	rePass = new JTextField(40);
	rePass.setBackground(c);
	rePass.setEnabled(false);
	dob = new JTextField(40);
	dob.setEnabled(false);
	pass = new JTextField(40);
	pass.setBackground(c);
	pass.setEnabled(false);

	selectProfile = new JButton("Take Pic");

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
	setLayout(new GridBagLayout());
	profile = new JLabel();
	setProfile("/images/profile-pictures.png",false);
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.anchor = GridBagConstraints.SOUTH;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 0;
	add(profile, constraints);
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 1;
	add(selectProfile, constraints);

	constraints.anchor = GridBagConstraints.WEST;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 1;
	add(update, constraints);
	constraints.insets = new Insets(5, 100, 5, 5);

	add(edit, constraints);
	constraints.insets = new Insets(5, 200, 5, 5);

	add(remove, constraints);
	constraints.insets = new Insets(5, 300, 5, 5);

	add(addnew, constraints);

	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.weightx = 0;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 2;
	add(new JLabel("Name:"), constraints);
	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 2;
	add(name, constraints);
	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 3;
	add(new JLabel("Email:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 3;
	add(email, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 4;
	add(new JLabel("User Name:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 4;
	add(userName, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 5;
	add(new JLabel("Password :"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 5;
	add(pass, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 6;
	add(new JLabel("Re-Password:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 6;
	add(rePass, constraints);
}
@Override
public void camReply(String path) {
	setProfile(path,true);
}
}
