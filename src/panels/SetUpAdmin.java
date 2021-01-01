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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gymManagement.AdminPanel;
import com.gymManagement.ShowPanel;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.CircleImage;
import extra_classes.ImageResizer;
import extra_classes.genrateUniqueID;
import models.admin;
import preferences.getPrefsSingletan;

public class SetUpAdmin extends JPanel {

	private JButton chooser, save;
	private JLabel pic, profile;
	private JTextField name, email,username;
	private JPasswordField password, repassword;
	private JPanel top, right, left;
	private JFileChooser filePicker;
	private File selectedFile;
	private ShowPanel parent;

	public SetUpAdmin(ShowPanel parent) {
		setPreferredSize(new Dimension(300, 480));
		try {
			String path = "C:gym_manngment";
			String path2 = "C:gym_manngment/images";
			Files.createDirectories(Paths.get(path));
			Files.createDirectories(Paths.get(path2));
			filePicker = new JFileChooser(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// setLayout(new GridBagLayout());
		this.parent = parent;
		addComponent();
	}
	private void addComponent() {
		top = new JPanel();
		right = new JPanel();
		left = new JPanel();
		top.setBackground(new Color(46, 209, 200));
		right.setBackground(new Color(46, 240, 200));
		left.setBackground(new Color(46, 250, 200));
		name = new JTextField(40);
		chooser = new JButton("select pic");

		chooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int returnValue = filePicker.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = filePicker.getSelectedFile();
					if (selectedFile != null)
						setProfile(selectedFile.getAbsolutePath(),true);
					System.out.println("we selected: " + selectedFile.getAbsolutePath());
				}
			}
		});
		save = new JButton("Save And Next");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAdminData();
			}
		});
		email = new JTextField(40);
		password = new JPasswordField(40);
		repassword = new JPasswordField(40);
		username = new JTextField(40);
		setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);
		add(left, BorderLayout.WEST);
		right.setLayout(new GridBagLayout());
		left.setLayout(new GridBagLayout());
		add(right, BorderLayout.CENTER);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(0, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;

		// top
		
		pic = new JLabel();
		setLogo("/images/frontPic2.jpg",false);
		top.add(pic, constraints);

		// left
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		profile = new JLabel();
		setProfile("/images/profile-pictures.png",false);
		left.add(profile, constraints);
		constraints.insets = new Insets(5, 40, 5, 5);
		constraints.gridy = 2;
		left.add(chooser, constraints);
		// right
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 50, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		right.add(new JLabel("Name"), constraints);
		constraints.weightx = 1;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		right.add(name, constraints);
		constraints.weightx = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.NONE;
		right.add(new JLabel("Email"), constraints);
		constraints.weightx = 1;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		right.add(email, constraints);
		constraints.weightx = 0;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.NONE;
		right.add(new JLabel("User name"), constraints);
		constraints.weightx = 1;
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		right.add(username, constraints);
		constraints.weightx = 0;
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.fill = GridBagConstraints.NONE;
		right.add(new JLabel("Password"), constraints);
		constraints.weightx = 1;
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		right.add(password, constraints);
		constraints.weightx = 0;
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.fill = GridBagConstraints.NONE;
		right.add(new JLabel("Reenter password"), constraints);
		constraints.weightx = 1;
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		right.add(repassword, constraints);
		constraints.weightx = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 2;
		constraints.gridy = 6;
		right.add(save, constraints);

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

	private void setProfile(String path,boolean isProfile) {
		BufferedImage Bufimage = new CircleImage(path,isProfile).getCircleImage();
		Image newimg = Bufimage.getScaledInstance(160, 160, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		profile.setIcon(imageIcon);
	}
	private void setLogo(String path,boolean isProfile) {
		BufferedImage Bufimage = new CircleImage(path,isProfile).getCircleImage();
		Image newimg = Bufimage.getScaledInstance(160, 160, java.awt.Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(newimg);
		pic.setIcon(imageIcon);
	}

	public void saveAdminData(){
		String name = this.name.getText().toString();
		String email = this.email.getText().toString();
		String password = this.password.getText().toString();
		String repass = this.repassword.getText().toString();
		String username = this.username.getText().toString();
		String profile;
		if (selectedFile != null)
			profile = selectedFile.getAbsolutePath();
		else
			profile = "/images/profile-pictures.png";
		if (password.equals(repass)) {
			myDatabaseHandler db = new myDatabaseHandler();
			Statement statement = db.getStatement();
			String uniqueID = new genrateUniqueID("admin").getUID();
			System.out.println(uniqueID);
			if (name.length() > 0 && email.length() > 0 && password.length() > 0 && username.length() > 0) {
				
				if(db.checkTable(Keys.TABLE_ADMIN_DATA,db.getConnection())){
					if (db.addAdminData(uniqueID, email, name, username, password, profile,0, statement)) {
						System.out.println("admin added");
						Preferences prefs = prefs = new getPrefsSingletan().getPrefs();
						prefs.putBoolean(Keys.PREF_IS_SETUP, true);
						prefs.put(Keys.PREFS_LOGIN_ADMIN,uniqueID );
						try{
						parent.changePanel(new AdminPanel(new admin(uniqueID, email, name, username, password, profile,0)));}
						catch(Exception e){
						}
					  }
				}else{
					db.createDb(statement);
					 db = new myDatabaseHandler();
					statement = db.getStatement();
					if (db.addAdminData(uniqueID, email, name, username, password, profile,0, statement)) {
						System.out.println("admin added");
						Preferences prefs = new getPrefsSingletan().getPrefs();
						prefs.putBoolean(Keys.PREF_IS_SETUP, true);
						try{
						parent.changePanel(new AdminPanel(new admin(uniqueID, email, name, username, password, profile,0)));}
						catch(Exception e){
						}
					  }
				}
					
				} else
				JOptionPane.showMessageDialog(null, new JLabel("Fill all fileds"));
		} else {
			JOptionPane.showMessageDialog(null, new JLabel("Password not matching"));
		}
	}
}
