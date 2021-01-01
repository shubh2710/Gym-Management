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

public class SMSApiSrtupPanel extends JPanel{

	
	private JButton update, remove, edit;
	private JTextField TF_name, TF_SID, TF_TOKEN, TF_SERVICE;
	String sid,name,token,service;
	Preferences prefs = new getPrefsSingletan().getPrefs();
	

public 	SMSApiSrtupPanel(){
	
		getSMSApi();
		setup();
	
		setSMSApi();
	}
private void setSMSApi() {
	// TODO Auto-generated method stub
	TF_name.setText(name);
	TF_SID.setText(sid);
	TF_TOKEN.setText(token);
	TF_SERVICE.setText(service);
}
private void addSMS(){
	prefs.put(Keys.PREFS_SMS_NAME, TF_name.getText().toString());
	prefs.put(Keys.PREFS_SMS_SID, TF_SID.getText().toString());
	prefs.put(Keys.PREFS_SMS_TOKEN, TF_TOKEN.getText().toString());
	prefs.put(Keys.PREFS_SMS_SERVICE, TF_SERVICE.getText().toString());
}
protected void getSMSApi() {
	// TODO Auto-generated method stub
	// db.createUserTable(statement);
	name =prefs.get(Keys.PREFS_SMS_NAME, null);
	sid=prefs.get(Keys.PREFS_SMS_SID, null);
	token=prefs.get(Keys.PREFS_SMS_TOKEN, null);
	service=prefs.get(Keys.PREFS_SMS_SERVICE, null);
}
public void setEnableOrDisable(boolean value) {
	if (value) {
		update.setEnabled(true);
		TF_name.setEditable(true);
		TF_SID.setEditable(true);
		TF_TOKEN.setEditable(true);
		TF_SERVICE.setEditable(true);
	} else {
		Color c = new Color(50, 70, 66);
		update.setEnabled(false);

		update.setEnabled(false);
		TF_name.setEditable(false);
		TF_SID.setEditable(false);
		TF_TOKEN.setEditable(false);
		TF_SERVICE.setEditable(false);
	}

}
protected void removeSMS() {
	// TODO Auto-generated method stub
	int dialogButton = JOptionPane.YES_NO_OPTION;
	int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like remove API ?", "Warning", dialogButton);
	if (dialogResult == JOptionPane.YES_OPTION) {

		prefs.put(Keys.PREFS_SMS_NAME,null);
		prefs.put(Keys.PREFS_SMS_SID, null);
		prefs.put(Keys.PREFS_SMS_TOKEN, null);
		prefs.put(Keys.PREFS_SMS_SERVICE, null);
			JOptionPane.showMessageDialog(null, new JLabel("API Removed"));
			
		} else
			JOptionPane.showMessageDialog(null, new JLabel("Can't Remove"));
}

private void setup() {
	// TODO Auto-generated method stub	try{
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
	
	update.setEnabled(false);
	remove = new JButton("Remove");
	
	remove.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
				removeSMS();
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
			addSMS();
		}
	});

	TF_name=new JTextField(40);
	TF_SID=new JTextField(40);
	TF_TOKEN=new JTextField(40);
	TF_SERVICE=new JTextField(40);
	setLayout(new GridBagLayout());
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.anchor = GridBagConstraints.WEST;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 1;
	add(update, constraints);
	constraints.insets = new Insets(5, 100, 5, 5);

	add(edit, constraints);
	constraints.insets = new Insets(5, 200, 5, 5);

	add(remove, constraints);

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
	add(TF_name, constraints);
	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 3;
	add(new JLabel("SID:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 3;
	add(TF_SID, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 4;
	add(new JLabel("TOKEN:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 4;
	add(TF_TOKEN, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 5;
	add(new JLabel("SERVICE :"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 5;
	add(TF_SERVICE, constraints);
}
}