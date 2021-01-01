package panels;

import java.awt.BorderLayout;
import java.awt.Checkbox;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Statement;
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

public class LoginCheckPanel extends JPanel {
	private Image img;
	private JButton login;
	private JLabel forget, invalid;
	private JTextField username;
	private JPasswordField password;
	private AdminPanel parent;
	private JPanel Display;
	private String user_name,pass_word;
	private Preferences prefs;
	Checkbox checkbox1 = new Checkbox("Save Password");
	private boolean saveLogin;
	public LoginCheckPanel(AdminPanel parent,JPanel Display) {
		
		prefs = new getPrefsSingletan().getPrefs();
		saveLogin=prefs.getBoolean(Keys.PREF_SAVE_PASSWORD, false);
		if(saveLogin){
			checkbox1.setState(true);
			user_name=prefs.get(Keys.PREFS_USERNAME, "");
			pass_word=prefs.get(Keys.PREFS_PASSWORd, "");
		}else{
			checkbox1.setState(false);
			user_name="";
			pass_word="";
		}
		this.parent = parent;
		this.Display=Display;
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
		setPreferredSize(new Dimension(parent.getWidth()-960, parent.getHeight()));
		addComponent();
	}

	private void addComponent() {
		username = new JTextField(40);
		username.setText(user_name);
		password = new JPasswordField(40);
		password.setText(pass_word);
		login = new JButton("Login");
		invalid = new JLabel("Invalid password");
		invalid.setForeground(Color.RED);
		invalid.setVisible(false);
		forget = new JLabel("Forget Password");
		forget.setForeground(Color.WHITE);
		forget.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			
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
		constraints.insets = new Insets(5,30, 5, 30);
		constraints.gridx = 1;
		constraints.gridy = 6;
		add(invalid, constraints);
		constraints.gridx = 1;
		constraints.gridy = 8;
		 checkbox1.setBackground(Color.green);
		add(checkbox1, constraints);  
		checkbox1.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				
				if(checkbox1.getState()){
					prefs.putBoolean(Keys.PREF_SAVE_PASSWORD, true);
					saveLogin=true;
				}else{
					prefs.putBoolean(Keys.PREF_SAVE_PASSWORD, false);
					saveLogin=false;
				}
			}
			
		});

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
						admin a = db.getAdminAuth(username, password, statement);
						if (a != null && a.getType()==0) {
							System.out.println("islogin"+saveLogin);
							if(saveLogin){
								System.out.println("usenane"+username);
								prefs.put(Keys.PREFS_USERNAME, username);
								prefs.put(Keys.PREFS_PASSWORd, password);
							}else{
								prefs.put(Keys.PREFS_USERNAME, "");
								prefs.put(Keys.PREFS_PASSWORd, "");
							}
							try{
								parent.Remove();
								parent.rightContent=Display;
								parent.changePanel(parent.rightContent);	
							}catch(Exception e){
								//JOptionPane.showMessageDialog(null, new JLabel(a.getName()));
								System.out.print("RuntimeException: ");
							    System.out.println(e.getMessage());
							}
						} else {
							System.out.println("Invalid auth");
							invalid.setVisible(true);
						}
					
				}
				}).start();;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

}
