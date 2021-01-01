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
import models.CompanyDetailModel;
import models.admin;
import preferences.getPrefsSingletan;
import snippet.Snippet;

public class CompanyDetailPanel extends JPanel{

	
	private JButton update, edit,remove;
	private JTextField name, email,mob, address, gstNo, gstPersent;
	private CompanyDetailModel company = null;

public 	CompanyDetailPanel(){
		
		setup();
		getCompanyDetail();
		setAdmin();
	}
protected void addCompanyDetails(String name, String mob, String email, String address, String gstNo, String gstPercent) {
		try {
			company= new CompanyDetailModel(name,mob,  email,  address,  gstNo,  Integer.parseInt(gstPercent));
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			db.createCompanyTable(statement);
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Update Company details ?", "Warning",
					dialogButton);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (db.addCompany(company, statement)) {
					JOptionPane.showMessageDialog(null, new JLabel("Details Updated"));
					setEnableOrDisable(false);
					getCompanyDetail();
					System.out.println(company.getName());
					statement.close();
				} else {
					JOptionPane.showMessageDialog(null, new JLabel("Can't Update Details"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
		}
}
protected void setAdmin() {
	// TODO Auto-generated method stub
	if(company!=null){
	System.out.println(company.getName());
	name.setText(company.getName());
	email.setText(company.getEmail());
	mob.setText(company.getMob() + "");
	address.setText(company.getAddress());
	gstNo.setText(company.getGstNo());
	gstPersent.setText(company.getGstPercent()+"");
	}
	
}
protected void getCompanyDetail() {
	// TODO Auto-generated method stub
	myDatabaseHandler db = new myDatabaseHandler();
	Statement statement = db.getStatement();
	// db.createUserTable(statement);
	company = db.getCompanyDetail(statement);
}

private void resetFields() {
	// TODO Auto-generated method stub
	name.setText("");
	email.setText("");
	mob.setText("");
	gstNo.setText("");
	gstPersent.setText("");
	address.setText("");
}

public void setEnableOrDisable(boolean value) {
	if (value) {
		update.setEnabled(true);
		name.setEnabled(true);
		mob.setEnabled(true);
		gstNo.setEnabled(true);
		gstPersent.setEnabled(true);
		address.setEnabled(true);
		email.setEnabled(true);
		name.setBackground(Color.white);
		mob.setBackground(Color.white);
		gstNo.setBackground(Color.white);
		gstPersent.setBackground(Color.white);
		address.setBackground(Color.white);
		email.setBackground(Color.white);

	} else {
		Color c = new Color(50, 70, 66);
		update.setEnabled(false);
		name.setEnabled(false);
		mob.setEnabled(false);
		gstNo.setEnabled(false);
		gstPersent.setEnabled(false);
		address.setEnabled(false);
		email.setEnabled(false);
		name.setBackground(c);
		mob.setBackground(c);
		gstNo.setBackground(c);
		gstPersent.setBackground(c);
		address.setBackground(c);
		email.setBackground(c);

	}

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
	remove=new JButton("remove");
	remove.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			db.createCompanyTable(statement);
			getCompanyDetail();
		}
		
	});
	update.setEnabled(false);
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
				addCompanyDetails(name.getText().toString(), mob.getText().toString(), email.getText().toString(),
					address.getText().toString(), gstNo.getText().toString(),gstPersent.getText().toString());
		}
	});
	Color c = new Color(50, 70, 66);
	name = new JTextField(40);
	name.setBackground(c);
	name.setEnabled(false);
	email = new JTextField(40);
	email.setBackground(c);
	email.setEnabled(false);
	mob = new JTextField(40);
	mob.setBackground(c);
	mob.setEnabled(false);
	address = new JTextField(40);
	address.setBackground(c);
	address.setEnabled(false);
	gstNo = new JTextField(40);
	gstNo.setEnabled(false);
	gstNo.setBackground(c);
	gstPersent = new JTextField(40);
	gstPersent.setBackground(c);
	gstPersent.setEnabled(false);
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
	add(remove,constraints);
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
	add(new JLabel("Address:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 4;
	add(address, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 5;
	add(new JLabel("Mobile:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 5;
	add(mob, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 6;
	add(new JLabel("GST No:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 6;
	add(gstNo, constraints);

	constraints.weightx = 0;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 0;
	constraints.gridy = 7;
	add(new JLabel("GST %:"), constraints);

	constraints.weightx = 2;
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.gridx = 1;
	constraints.gridy = 7;
	add(gstPersent, constraints);
}
}
