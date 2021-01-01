package panels;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.ImportBackup;
import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.CreateBackup;
import extra_classes.CreateBackupDatabase;
import preferences.getPrefsSingletan;

public class SettingsPanel extends JPanel implements ActionListener {

	private JLabel backgroundPath,title;
	private JButton bChooser,rest,unistal;
	private	JPanel  top,center,bottom;
	private JTextField C_top,c_center,c_other;
	private JButton choseLogo,choseBack,reset,save,Bunistal,backup,importBackup;
	private JFileChooser filePicker;
	private File selectedLogo;
	private File selectedImage;
	private File selectedBackup;
	private Preferences prefs;
 private Checkbox checkbox = new Checkbox();
	
	public SettingsPanel(){
		bottom=new JPanel();
		
		top=new JPanel();
		center=new JPanel();
		setLayout(new BorderLayout());
		bottom.setBackground(Color.black);
		center.setLayout(new GridBagLayout());
		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			top.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			center.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
			top.setBackground(Color.decode("#ffffff"));
			center.setBackground(Color.decode("#ffffff"));
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		add(bottom,BorderLayout.SOUTH);
		add(top,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		addComponents();
	}

	private void addComponents() {
		prefs = new getPrefsSingletan().getPrefs();
		title=new JLabel("Settings");
		title.setFont(new Font("Serif", Font.PLAIN, 30));
		top.add(title);
		C_top=new JTextField(40);
		c_center=new JTextField(40);
		c_other=new JTextField(40);
		backup=new JButton("CREATE BACKUP");
		importBackup=new JButton("IMPORT BACKUP");
		choseLogo=new JButton("Logo");
		choseBack=new JButton("Image");
		reset=new JButton("RESET");
		save=new JButton("SAVE");
		Bunistal=new JButton("UNISTALL");
		backup.addActionListener(this);
		choseLogo.addActionListener(this);
		choseBack.addActionListener(this);
		reset.addActionListener(this);
		save.addActionListener(this);
		Bunistal.addActionListener(this);
		importBackup.addActionListener(this);
		boolean state= prefs.getBoolean(Keys.PREF_FRONT_SAVE_PASSWORD, false);
		checkbox.setState(state);
		checkbox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
					prefs.putBoolean(Keys.PREF_FRONT_SAVE_PASSWORD, checkbox.getState());
			}
			
		});
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 190, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx=1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		center.add(new JLabel("Background image"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		center.add(new JLabel("Logo"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		center.add(new JLabel("Save Login Password"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		center.add(new JLabel("Create Backup"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 4;
		center.add(new JLabel("Import Backup"),constraints);
		constraints.gridx = 0;
		constraints.gridy = 5;
		center.add(new JLabel("Set Hex Colors"),constraints);
		
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx=1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		center.add(choseBack,constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		center.add(choseLogo,constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		center.add(checkbox,constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		center.add(backup,constraints);		
		constraints.gridx = 1;
		constraints.gridy = 4;
		center.add(importBackup,constraints);
		constraints.gridx = 1;
		constraints.gridy = 5;
		center.add(C_top,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 6;
		center.add(c_center,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 7;
		center.add(c_other,constraints);
		
		bottom.add(save);
		bottom.add(reset);
		bottom.add(Bunistal);
		setColors();
	}

	@Override
	public void actionPerformed(ActionEvent ae){
		// TODO Auto-generated method stub
		int dialogButton;
		int dialogResult;
		JButton b = (JButton) ae.getSource();
		switch (b.getText()) {
			case "Logo":
				selectLogo();
				break;
			case "Image":
				selectImage();
				break;
			case "RESET":
				 dialogButton=JOptionPane.YES_NO_OPTION;
				 dialogResult = JOptionPane.showConfirmDialog(null, "Want to reset software and delete your all data ? Admin user id password will be same", "Warning" ,dialogButton);
						if(dialogResult==JOptionPane.YES_OPTION){
							reset();	
						}else if(dialogResult==JOptionPane.NO_OPTION){
								
						}
				break;
			case "UNISTALL":
				 dialogButton=JOptionPane.YES_NO_OPTION;
				 dialogResult = JOptionPane.showConfirmDialog(null, "Want to unistal the software ? It will delete all admin data, user data and software key Make sure u have backup data", "Warning" ,dialogButton);
						if(dialogResult==JOptionPane.YES_OPTION){
							unistal();
						}else if(dialogResult==JOptionPane.NO_OPTION){
								
						}
				break;
			case "SAVE":
				save();
				break;
			case "CREATE BACKUP":
				//new Thread(new CreateBackup()).start();
				new Thread(new CreateBackupDatabase()).start();
				break;
				
			case "IMPORT BACKUP":
				importdb();
				break;
		}
	}
	private void importdb() {
		// TODO Auto-generated method stub
		
		
			int dialogButton=JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "It will remove your current data! except admin", "Warning" ,dialogButton);
					if(dialogResult==JOptionPane.YES_OPTION){
						takeBakup();
						if(this.selectedBackup!=null)
							new	ImportBackup(selectedBackup.getAbsolutePath());
					}else if(dialogResult==JOptionPane.NO_OPTION){
							
					}
	}

	private void save() {
		if(this.selectedImage!=null)
			prefs.put(Keys.BACKIMAGE,selectedImage.getAbsolutePath());
		if(this.selectedLogo!=null)
			prefs.put(Keys.LOGO,selectedLogo.getAbsolutePath());
		saveColor();
		JOptionPane.showMessageDialog(null, new JLabel("Setting saved"));
	}


	private void setColors(){
		C_top.setText(prefs.get(Keys.PREFS_COLOR_TOP, "#ffffff"));
		c_center.setText(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff"));
		c_other.setText(prefs.get(Keys.PREFS_COLOR_OTHER, "#ffffff"));
		
	}
	private void saveColor() {
		// TODO Auto-generated method stub
		String top,center,other;
		top=C_top.getText().toString();
		center=c_center.getText().toString();
		other=c_other.getText().toString();
		if(top!=null && center!=null && other!=null)
		prefs.put(Keys.PREFS_COLOR_TOP, top);
		prefs.put(Keys.PREFS_COLOR_CENTER, center);
		prefs.put(Keys.PREFS_COLOR_OTHER, other);
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

	private void reset() {
		
		prefs.putBoolean(Keys.PREF_FRONT_SAVE_PASSWORD, false);
		prefs.putBoolean(Keys.PREF_SAVE_PASSWORD, false);
		prefs.put(Keys.PREFS_PASSWORd, "");
		prefs.put(Keys.PREFS_USERNAME, "");
		prefs.put(Keys.BACKIMAGE, "");
		prefs.put(Keys.LOGO, "");
		prefs.put(Keys.FRONT_PREFS_PASSWORd, "");
		prefs.put(Keys.FRONT_PREFS_USERNAME, "");
		System.exit(0);
	}

	private void selectImage() {
		// TODO Auto-generated method stub
		takePicFromFile();
		
	}

	private void selectLogo() {
		// TODO Auto-generated method stub
		takePicFromFileLogo();
	}

public void	takePicFromFile(){
	File selectedFile;
	filePicker = new JFileChooser();
	int returnValue = filePicker.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
		selectedFile = filePicker.getSelectedFile();
		if (selectedFile != null && (selectedFile.getAbsolutePath().endsWith(".jpg") ||selectedFile.getAbsolutePath().endsWith(".png"))){
			this.selectedImage=selectedFile;
		}else{
			JOptionPane.showMessageDialog(null, new JLabel("Wrong File"));
		}
	}
}
public void	takePicFromFileLogo(){
	File selectedFile;
	filePicker = new JFileChooser();
	int returnValue = filePicker.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
		selectedFile = filePicker.getSelectedFile();
		if (selectedFile != null && (selectedFile.getAbsolutePath().endsWith(".jpg") ||selectedFile.getAbsolutePath().endsWith(".png"))){
			this.selectedLogo=selectedFile;
		}else{
			JOptionPane.showMessageDialog(null, new JLabel("Wrong File"));
		}
	}
}
public void takeBakup(){
	File selectedFile;
	filePicker = new JFileChooser();
	int returnValue = filePicker.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
		selectedFile = filePicker.getSelectedFile();
		if (selectedFile != null && (selectedFile.getAbsolutePath().endsWith(".db") )){
			this.selectedBackup=selectedFile;
		}else{
			JOptionPane.showMessageDialog(null, new JLabel("Wrong database File"));
		}
	}	
}
}
