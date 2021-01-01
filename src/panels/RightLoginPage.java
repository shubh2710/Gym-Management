package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Statement;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.AncestorListener;

import com.gymManagement.AdminPanel;
import com.gymManagement.ShowPanel;

import database.myDatabaseHandler;
import db_keys.Keys;
import models.admin;
import preferences.getPrefsSingletan;

public class RightLoginPage extends JPanel {
	private Image img;
	private JButton login,unistall;
	private JLabel forget, invalid;
	private JTextField username;
	private JPasswordField password;
	private ShowPanel parent;
	private boolean needTOsave=false;
	Preferences prefs = new getPrefsSingletan().getPrefs();

	public RightLoginPage(ShowPanel parent) {
		this.parent = parent;
		
		URL path = getClass().getResource("/images/rightPic.jpg");
		System.out.println(path.getPath());
		
		try {
			this.img = new ImageIcon(ImageIO.read(path)).getImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.img=new ImageIcon("rightPic.jpg").getImage();
		// setBorder(BorderFactory.createTitledBorder("Login Page"));
		setPreferredSize(new Dimension((int)getPersent(parent.getWidth()), parent.getHeight()));
		addComponent();
	}


	private void unistal() {
		
		try {
			prefs.clear();
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			db.createDb(statement);
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	private double getPersent(int width) {
		// TODO Auto-generated method stub
		System.out.println(width);
		double f=30.00/100.00;
		
		return f*(double)width;
	}
	private JPanel getPanel(JTextField username,JTextField email) {
		JPanel Layout=new JPanel();
		unistall=new JButton("UNISTALL");
		unistall.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int dialogButton=JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Want to unistal the software ? It will delete all admin data, user data and software key", "Warning" ,dialogButton);
						if(dialogResult==JOptionPane.YES_OPTION){
							unistal();	
						}else if(dialogResult==JOptionPane.NO_OPTION){
								
						}
			}
			
		});
		Layout.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		Layout.add(new JLabel("User name"),constraints);
		constraints.gridy = 1;
		Layout.add(username,constraints);
		constraints.gridy = 2;
		Layout.add(new JLabel("Email"),constraints);
		constraints.gridy = 3;
		Layout.add(email,constraints);
		constraints.gridy = 4;
		Layout.add(unistall,constraints);
		
		return Layout;
	}
	public String checkForgetlogin(String user ,String email){
		String result="";
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		if(db.checkTable(Keys.TABLE_ADMIN_DATA, db.getConnection())){
			admin a = db.getAdminForgetAuth(email,user, statement);
			if (a != null) {
				result=a.getPassword();
				}
			}
		return result;	
		}
	private void GetForgetLogin() {
		// TODO Auto-generated method stub
		JTextField user=new JTextField();
		JTextField key=new JTextField();
		int dialogResult = JOptionPane.showConfirmDialog(null, getPanel(user,key),
				"Enter username and email : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			String pass=checkForgetlogin(key.getText().toString(), user.getText().toString());
			if(pass.length()>0){
				JOptionPane.showMessageDialog(null, new JLabel("Password is :"+pass));
			}else{
				JOptionPane.showMessageDialog(null, new JLabel("Invalid Key"));
			}	
		}
	}
	
	private void addComponent() {
		username = new JTextField(40);
		
		password = new JPasswordField(40);
		
		login = new JButton("Login");
		invalid = new JLabel("Invalid password");
		invalid.setForeground(Color.RED);
		invalid.setVisible(false);
		forget = new JLabel("Forget Password");
		forget.setForeground(Color.WHITE);
		boolean	saveLogin=prefs.getBoolean(Keys.PREF_FRONT_SAVE_PASSWORD, false);
		if(saveLogin){
			String user_name=prefs.get(Keys.FRONT_PREFS_USERNAME, null);
			String pass_word=prefs.get(Keys.FRONT_PREFS_PASSWORd, null);
			if(user_name!=null   && pass_word!=null){
				if(user_name.length()>0 &&  pass_word.length()>0){
					username.setText(user_name);
					password.setText(pass_word);
				}
			}else
				needTOsave=true;
		}else{
			username.setText("");
			password.setText("");
		}
		
		forget.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try{
				 GetForgetLogin();

				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Login(username.getText().toString(), password.getText().toString());
				// parent.changePanel(new AdminPanel(a));
			}
		});
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 30, 5, 30);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		add(new JLabel("User Name"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(username, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(new JLabel("Password"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		add(password, constraints);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.fill = GridBagConstraints.NONE;
		add(login, constraints);
		constraints.insets = new Insets(0, 170, 0, 5);
		constraints.gridx = 1;
		constraints.gridy = 6;
		add(invalid, constraints);
		constraints.gridx = 1;
		constraints.gridy = 7;
		add(forget, constraints);

	}

	protected void Login(String username, String password) {
		// TODO Auto-generated method stub
			new Thread (new Runnable(){

				@Override
				public void run() {
					myDatabaseHandler db = new myDatabaseHandler();
					Statement statement = db.getStatement();
					//db.createAdminTable(statement);
					//db.addAdminData("451142141", "shubham@gmail", "shubham", "admin", "1234567890", "/images/profile-pictures.png",statement);
						
					if(db.checkTable(Keys.TABLE_ADMIN_DATA, db.getConnection())){
						admin a = db.getAdminAuth(username, password, statement);
						if (a != null) {
							if(needTOsave){
								prefs.put(Keys.FRONT_PREFS_USERNAME, username);
								prefs.put(Keys.FRONT_PREFS_PASSWORd, password);
							}
							try{
								prefs.put(Keys.PREFS_LOGIN_ADMIN, a.getAid());
								parent.changePanel(new AdminPanel(a));
							}catch(Exception e){
								//JOptionPane.showMessageDialog(null, new JLabel(a.getName()));
								System.out.print("RuntimeException: ");
							    System.out.println(e.getMessage());
							}
						} else {
							System.out.println("Invalid auth");
							invalid.setVisible(true);
						}
					}else{
						prefs.putBoolean(Keys.PREF_IS_SETUP, false);
						System.exit(0);
					}
				}
				}).start();;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

}
