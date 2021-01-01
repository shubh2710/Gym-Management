package com.gymManagement;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.Cal;
import extra_classes.CircleImage;
import extra_classes.ReadExcel;
import models.CompanyDetailModel;
import models.admin;
import panels.AdminDetailPanel;
import panels.AdminSettingPanel;
import panels.Atendance;
import panels.BillingPanel;
import panels.ContentTestPanel;
import panels.EditPlansPanel;
import panels.LeftPage;
import panels.LoginCheckPanel;
import panels.RegisterUser;
import panels.RightLoginPage;
import panels.SetUpAdmin;
import panels.SettingsPanel;
import panels.UserDetails;
import preferences.getPrefsSingletan;

public class AdminPanel extends JPanel implements ActionListener {
	public JPanel top, rightContent, leftMenu;
	private String name, Email, profile;
	private admin a;
	private CompanyDetailModel company=null;
	Preferences prefs;
	public AdminPanel(admin a) {
		setLayout(new BorderLayout());
		this.a = a;
		prefs = new getPrefsSingletan().getPrefs();
		getCompanyDetails();
		initUI();
	
		//new Thread(new ReadExcel()).start();
	}
	private void getCompanyDetails() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		company = db.getCompanyDetail(statement);
	}
	private void initUI() {
		top = new JPanel();
		rightContent = new ContentTestPanel();
		leftMenu = new JPanel();
		try{
			top.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_TOP, "#ffffff")));
			rightContent.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			leftMenu.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_OTHER, "#ffffff")));

		}catch(Exception e){
			top.setBackground(Color.decode("#ffffff"));
			rightContent.setBackground(Color.decode("#ffffff"));
			leftMenu.setBackground(Color.decode("#ffffff"));

			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}

		top.setPreferredSize(new Dimension(800, 100));
		leftMenu.setPreferredSize(new Dimension(200, 800));
		// leftContent.setPreferredSize(new Dimension(800,80));
		top.setLayout(new GridBagLayout());
		rightContent.setLayout(new GridBagLayout());
		leftMenu.setLayout(new GridBagLayout());

		add(top, BorderLayout.NORTH);
		add(leftMenu, BorderLayout.WEST);
		add(rightContent, BorderLayout.CENTER);
		addTopContent();
		addLeftMenu();
		addRightContent();
	}

	private void addTopContent() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel logo = new JLabel();
		
		
		String image=prefs.get(Keys.LOGO, null);
		BufferedImage Bufimage=null;
		if(image!=null && image.length()>0){
			Bufimage =new CircleImage(image,true).getCircleImage(); 
		}else{
			Bufimage =new CircleImage("/images/frontPic2.jpg",false).getCircleImage();
		}
		Image newimg = Bufimage.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg); // transform it back
		logo.setIcon(imageIcon);
		top.add(logo, constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 0;
		JLabel titel;
		if(company!=null)
		titel = new JLabel(company.getName().toUpperCase());
		else
			titel = new JLabel("NAME");
		titel.setFont(new Font("Serif", Font.PLAIN, 30));
		top.add(titel, constraints);
		constraints.insets = new Insets(30, 800, 5, 5);
		constraints.gridx = 2;
		constraints.gridy = 0;
		JLabel profile = new JLabel();
		BufferedImage Buffimage = new CircleImage(a.getProfile(),true).getCircleImage(); 
		Image img = Buffimage.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon2 = new ImageIcon(img); // transform it back
		profile.setIcon(imageIcon2);
		top.add(profile, constraints);

		constraints.insets = new Insets(0, 0, 0, 70);
		constraints.gridx = 3;
		constraints.gridy = 0;
		JLabel name = new JLabel(a.getName());
		name.setFont(new Font("Serif", Font.PLAIN, 20));
		top.add(name, constraints);

		constraints.insets = new Insets(40, 1, 1, 70);
		constraints.gridx = 3;
		constraints.gridy = 0;
		JLabel email = new JLabel(a.getEmail());
		email.setFont(new Font("Serif", Font.PLAIN, 20));
		top.add(email, constraints);

	}

	private void addRightContent() {
		// TODO Auto-generated method stub

	}
	private double getPersent(double h,double scale) {
		// TODO Auto-generated method stub
		System.out.println(h);
		double f=scale/h;
		return f*100;
	}
	
	private void addLeftMenu() {
		// TODO Auto-generated method stub
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.ipadx = (int)getPersent(getPreferredSize().getHeight(),300);
		constraints.ipady = (int)getPersent(getPreferredSize().getHeight(),300);
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		int y = 0;
		JButton button;
		ArrayList list;
		list = new ArrayList<String>();
		leftMenu.setBorder(BorderFactory.createTitledBorder("MENU"));
		list.add("ADD NEW USER");
		list.add("USER DETAILS");
		list.add("ATTENDANCE");
		list.add("BILLING");
		list.add("PLANS");
		list.add("ADMIN");
		for (int i = 0; i < list.size(); i++) {
			constraints.gridy = y;
			y++;
			button = new JButton(list.get(i).toString());
			button.addActionListener(this);
			button.setPreferredSize(new Dimension(140, 90));
			leftMenu.add(button, constraints);
		}
		// leftMenu.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// setVisible(true);

	}

	private BufferedImage createCircleImage(String path) {
		// TODO Auto-generated method stub
		BufferedImage master = null;
		try {

			URL url=getClass().getResource("/images/frontPic2.jpg");
			master = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			JOptionPane.showMessageDialog(null, new JLabel("image reading 2"));
			e.printStackTrace();
		}
		int diameter = Math.min(master.getWidth(), master.getHeight());
		BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = mask.createGraphics();
		applyQualityRenderingHints(g2d);
		g2d.fillOval(0, 0, diameter - 1, diameter - 1);
		g2d.dispose();
		BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		g2d = masked.createGraphics();
		applyQualityRenderingHints(g2d);
		int x = (diameter - master.getWidth()) / 2;
		int y = (diameter - master.getHeight()) / 2;
		g2d.drawImage(master, x, y, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
		g2d.drawImage(mask, 0, 0, null);
		g2d.dispose();
		// JOptionPane.showMessageDialog(null, new JLabel(new
		// ImageIcon(masked)));
		return masked;
	}

	public static void applyQualityRenderingHints(Graphics2D g2d) {

		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		JButton b = (JButton) ae.getSource();
		switch (b.getText()) {
		case "ADD NEW USER":

			remove(rightContent);
			rightContent = new RegisterUser();
			changePanel(rightContent);
			break;
		case "USER DETAILS":
			remove(rightContent);
			rightContent = new UserDetails();
			changePanel(rightContent);
			break;
		case "ATTENDANCE":
			remove(rightContent);
			rightContent = new Atendance();
			//rightContent.
			changePanel(rightContent);
			break;
		case "BILLING":
			remove(rightContent);
			//rightContent = new BillingPanel();
			rightContent =new LoginCheckPanel(this,new BillingPanel());
			changePanel(rightContent);

			break;
		case "ADMIN":
			remove(rightContent);
			rightContent = new LoginCheckPanel(this,new AdminSettingPanel());;
			changePanel(rightContent);
			break;
		case "PLANS":
			remove(rightContent);
			rightContent = new LoginCheckPanel(this,new EditPlansPanel());
			changePanel(rightContent);
			break;
		}
	}

	public void Remove(){
		remove(rightContent);
	}
	public void changePanel(JPanel jpanel) {
		add(jpanel, BorderLayout.CENTER);
		repaint();
		revalidate();
	}
}
