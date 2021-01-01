package com.gymManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import db_keys.Keys;
import panels.LeftPage;
import panels.RightLoginPage;
import panels.SetUpAdmin;
import preferences.getPrefsSingletan;

public class ShowPanel extends JFrame {
	JPanel leftPage, RightLoginPage, admin;
	private Preferences prefs;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();

	public ShowPanel() {
		// read pref
		prefs = new getPrefsSingletan().getPrefs();
		boolean isSetup = prefs.getBoolean(Keys.PREF_IS_SETUP, false);
		boolean isValid= prefs.getBoolean(Keys.PREF_IS_VALID, false);
		System.out.println(isSetup + "");
		setLayout(new BorderLayout());
		setResizable(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setMinimumSize(new Dimension((int)width, (int)height));
		setSize((int)width, (int)height);
		if (isSetup && isValid) {
			leftPage = new LeftPage(this);
			RightLoginPage = new RightLoginPage(this);
			initUI();
		} else if(!isSetup && isValid) {
			admin = new SetUpAdmin(this);
			add(admin, BorderLayout.CENTER);
			setSize((int)width, (int)height);
			setTitle("Admin Setup");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}else{
			GetValitidy();
		}
	}

	private void GetValitidy() {
		// TODO Auto-generated method stub
		JTextField user=new JTextField();
		JTextField key=new JTextField();
		int dialogResult = JOptionPane.showConfirmDialog(null, getPanel(user,key),
				"Enter Key : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			if(key.getText().toString().equals("SKTEFM4MG7RNC4MSO5E") && user.getText().toString().equals("appwingway")){
				prefs.putBoolean(Keys.PREF_IS_VALID, true);
				System.exit(0);
			}else{
				JOptionPane.showMessageDialog(null, new JLabel("Invalid Key"));
				System.exit(0);
			}
		}
	}

	private JPanel getPanel(JTextField user,JTextField key) {
		JPanel keyLayout=new JPanel();
		keyLayout.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		keyLayout.add(new JLabel("User name"),constraints);
		constraints.gridy = 1;
		keyLayout.add(user,constraints);
		constraints.gridy = 2;
		keyLayout.add(new JLabel("Key"),constraints);
		constraints.gridy = 3;
		keyLayout.add(key,constraints);		
		
		return keyLayout;
	}

	private void initUI() {
		add(leftPage, BorderLayout.WEST);
		add(RightLoginPage, BorderLayout.EAST);
	
		setTitle("Gym Managment");
		// setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void changePanel(JPanel panel) throws Exception{
		getContentPane().removeAll();
		getContentPane().add(panel, BorderLayout.CENTER);
		repaint();
		revalidate();
	}
}