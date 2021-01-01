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
import javax.swing.BorderFactory;
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
import extra_classes.ReadTxt;
import extra_classes.genrateUniqueID;
import models.PlansModel;
import models.User;
import models.admin;
import preferences.getPrefsSingletan;

public class AdminSettingPanel extends JPanel implements ActionListener {
	private JPanel right, left;
	private JButton update, remove, edit, selectProfile;
	private JTextField name, email, pass, userName, rePass, dob;
	private JLabel profile;
	private JFileChooser filePicker;
	private File selectedFile;
	private admin user = null;


	public AdminSettingPanel() {
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}
	private double getPersent(double width) {
		// TODO Auto-generated method stub
		System.out.println(width);
		double f=30.00/100.00;
		return f*width;
	}
	
	private void setupUI() {
		left = new JPanel();
		right = new AdminSettions_Right();
		setLayout(new BorderLayout());
		add(left,BorderLayout.WEST);

		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			left.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			
		}catch(Exception e){
			left.setBackground(Color.decode("#ffffff"));
			
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		left.setPreferredSize(new Dimension(300, 400));
		
		addComponentLeft();
		addComponentRight();

	}

	private void addComponentLeft() {
		// TODO Auto-generated method stub
		left.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.ipadx = 10;
		constraints.ipady = 10;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		int y = 0;
		JButton button;
		ArrayList list;
		list = new ArrayList<String>();
		//left.setBorder(BorderFactory.createTitledBorder("MENU"));
		list.add("ADMIN DETAIL");
		list.add("OLD BILLS");
		list.add("ALL USERS");
		list.add("ADD ATTENDANCE");
		list.add("MONTHLY SALE");
		list.add("COMPANY DETAILS");
		list.add("SMS API SETUP");
		list.add("SETTINGS");
		for (int i = 0; i < list.size(); i++) {
			constraints.gridy = y;
			y++;
			button = new JButton(list.get(i).toString());
			button.addActionListener(this);
			left.add(button, constraints);
		}
		// leftMenu.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// setVisible(true);

	}

	private void addComponentRight() {
		add(right, BorderLayout.CENTER);
			}

	

	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton b = (JButton) ae.getSource();
		switch (b.getText()) {
		case "ADMIN DETAIL":

			remove(right);
			right = new AdminSettions_Right();
			changePanel(right);
			break;
		case "OLD BILLS":
			remove(right);
			right = new OldBillsPanel();
			changePanel(right);
			break;
		case "NEW USERS":
			remove(right);
			right = new Atendance();
			//rightContent.
			changePanel(right);
			break;
		case "ALL USERS":
			remove(right);
			right = new AllUsersPanel();
			changePanel(right);

			break;
		case "ADD ATTENDANCE":
			remove(right);
			right = new AdminAtendance();
			changePanel(right);
			openFileChooser();
			break;
		case "MONTHLY SALE":
			remove(right);
			right = new MonthlyIncomePanel();
			changePanel(right);
			break;
		case "COMPANY DETAILS":
			remove(right);
			right = new CompanyDetailPanel();
			changePanel(right);			
			break;
		case "SMS API SETUP":
			remove(right);
			right = new SMSApiSrtupPanel();
			changePanel(right);		
			break;
		case "SETTINGS":
			remove(right);
			right = new SettingsPanel();
			changePanel(right);
			break;
		}
	}
	private void openFileChooser() {
		// TODO Auto-generated method stub
		filePicker = new JFileChooser();
		int returnValue = filePicker.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = filePicker.getSelectedFile();
			if (selectedFile != null && (selectedFile.getAbsolutePath().endsWith(".txt"))){
				ReadTxt r=new ReadTxt(selectedFile.getAbsolutePath());
				new Thread (r).start();
			}else
				JOptionPane.showMessageDialog(null, new JLabel("Wrong file !"));
		}
	}

	public void changePanel(JPanel jpanel) {
		add(jpanel, BorderLayout.CENTER);
		repaint();
		revalidate();
	}

}
