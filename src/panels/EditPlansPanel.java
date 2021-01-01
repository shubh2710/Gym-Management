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
import extra_classes.genrateUniqueID;
import models.PlansModel;
import models.User;
import preferences.getPrefsSingletan;

public class EditPlansPanel extends JPanel {
	private JPanel right, left;
	private JButton update, remove, edit, addPlan;
	private JTextField planId,plan_name,plan_amt,plan_months,plan_detail;
	private JTextField  new_name, new_months,new_amt,new_plan_detail;
	private JTextField search;
	private final DefaultListModel plansModel = new DefaultListModel();;
	
	private ArrayList<PlansModel> plansList;
	private ArrayList<PlansModel> searchedPlanList;
	private PlansModel plan=null;
	public EditPlansPanel() {
		setPreferredSize(new Dimension(500, 480));
		setupUI();
	}

	private void setupUI() {
		getAllPlans();
		left = new JPanel();
		right = new JPanel();
		setLayout(new BorderLayout());
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.CENTER);
		try{
			Preferences prefs;
			prefs = new getPrefsSingletan().getPrefs();
			left.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
			right.setBackground(Color.decode(prefs.get(Keys.PREFS_COLOR_CENTER, "#ffffff")));
		}catch(Exception e){
			left.setBackground(Color.decode("#ffffff"));
			right.setBackground(Color.decode("#ffffff"));
			JOptionPane.showMessageDialog(null, new JLabel("Wrong color code"));
		}
		left.setPreferredSize(new Dimension(250, 400));
		right.setPreferredSize(new Dimension(100, 400));
		
		addComponentLeft();
		addComponentRight();
	}

	private void getAllPlans() {
		// TODO Auto-generated method stub
		plansList = new ArrayList();
		Vector comboBoxItems = new Vector();
		for (PlansModel p : plansList) {
			plansModel.addElement(p);
		}
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				plansList = db.getPlansList(statement);
				Vector comboBoxItems = new Vector();
				for (PlansModel p : plansList) {
					plansModel.addElement(p);
				}
			}
			
		}).start();
	}

	protected void addPlan(String name, String amt, String months,String detail) {
		int a = 0, m = 0;
			try {
				a = Integer.parseInt(amt);
				m= Integer.parseInt(months);
				System.out.println("add PLan"+name+" "+ amt+" "+months);
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				String pid=new genrateUniqueID("plan").getUID();
				PlansModel p=new PlansModel(pid,name,a,m,detail);
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Add plan ?", "Warning",
						dialogButton);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (db.addPlan(p, statement)) {
						JOptionPane.showMessageDialog(null, new JLabel("Plan Added"));
						setEnableOrDisable(false);
						getPlanList();
					} else {
						JOptionPane.showMessageDialog(null, new JLabel("Can't Add"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
			}
	}
	protected void updatePlan(String pid,String name, String amt, String months,String detail) {
		int a = 0, m = 0;
		if (plan != null)
			try {
				a = Integer.parseInt(amt);
				m= Integer.parseInt(months);
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				String uid=new genrateUniqueID("plan").getUID();
				PlansModel p=new PlansModel(pid,name,a,m,detail);
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like To Update plan ?", "Warning",
						dialogButton);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (db.updatePlan(p, statement)) {
						JOptionPane.showMessageDialog(null, new JLabel("Plan Updated"));
						setEnableOrDisable(false);
						getPlanList();
					} else {
						JOptionPane.showMessageDialog(null, new JLabel("Can't Update Plan"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, new JLabel("Invalid input"));
			}
	}

	private void resetFields() {
		// TODO Auto-generated method stub
		planId.setText("");
		plan_name.setText("");
		plan_amt.setText("");
		plan_months.setText("");
		plan_detail.setText("");
	}

	public void setEnableOrDisable(boolean value) {
		if (value && plan != null) {
			update.setEnabled(true);
			plan_detail.setEnabled(false);
			planId.setEnabled(false);
			plan_name.setEnabled(true);
			addPlan.setEnabled(true);
			plan_amt.setEnabled(true);
			plan_months.setEnabled(true);
			plan_detail.setBackground(Color.white);
			planId.setBackground(Color.white);
			addPlan.setEnabled(true);
			plan_name.setBackground(Color.white);
			plan_amt.setBackground(Color.white);
			plan_months.setBackground(Color.white);

		} else {
			Color c = new Color(50, 70, 66);
			update.setEnabled(false);
			planId.setEnabled(false);
			plan_name.setEnabled(false);
			plan_name.setBackground(c);
			plan_detail.setEnabled(false);
			plan_detail.setBackground(c);
			plan_amt.setEnabled(false);
			plan_months.setEnabled(false);
			planId.setBackground(c);
			plan_name.setBackground(c);
			plan_amt.setBackground(c);
			plan_months.setBackground(c);
		}
	}

	protected void removeUser(String pid) {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like remove plan ?", "Warning", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			if (db.removePlan(pid, statement)) {
				JOptionPane.showMessageDialog(null, new JLabel("plan Removed"));
				getPlanList();
			} else
				JOptionPane.showMessageDialog(null, new JLabel("Can't Remove"));
		}
	}

	private void addComponentRight() {
		update = new JButton("Update");
		update.setEnabled(false);
		plan_detail=new JTextField(40);
		remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (plan != null)
					removeUser(plan.getPid());
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
				updatePlan(planId.getText().toString(), plan_name.getText().toString(),
						plan_amt.getText().toString(),plan_months.getText().toString(),plan_detail.getText().toString());
			
			}
		});
		Color c = new Color(50, 70, 66);
		planId=new JTextField(40);
		new_plan_detail=new JTextField(40);
		planId.setBackground(c);
		planId.setEnabled(false);
		plan_name = new JTextField(40);
		plan_name.setBackground(c);
		plan_name.setEnabled(false);
		plan_amt = new JTextField(40);
		plan_amt.setBackground(c);
		plan_amt.setEnabled(false);
		plan_detail.setEnabled(false);
		plan_detail.setBackground(c);
		plan_months = new JTextField(40);
		plan_months.setBackground(c);
		plan_months.setEnabled(false);
		
		addPlan = new JButton("Add New");
		addPlan.setEnabled(true);

		addPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int dialogResult = JOptionPane.showConfirmDialog(null, getPanel(),
						"Create New Plan : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					addPlan(new_name.getText().toString(),
							new_amt.getText().toString(),new_months.getText().toString(),new_plan_detail.getText().toString());
				}
				}
			private JPanel getPanel() {
				JPanel layout = new JPanel();
				layout.setLayout(new GridBagLayout());
				new_name=new JTextField();
				new_months =new JTextField();
				new_amt=new JTextField();
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.anchor = GridBagConstraints.NORTH;
				constraints.insets = new Insets(5, 5, 5, 5);
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.weightx=1;
				constraints.gridx = 0;
				constraints.gridy = 0;
				layout.add(new JLabel("Add New Plan"), constraints);
				constraints.gridx = 0;
				constraints.gridy = 1;
				layout.add(new JLabel("Name:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 1;
				layout.add(new_name, constraints);
				constraints.gridx = 0;
				constraints.gridy = 2;
				layout.add(new JLabel("Months:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 2;
				layout.add(new_months, constraints);
				constraints.gridx = 0;
				constraints.gridy = 3;
				layout.add(new JLabel("Amount:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 3;
				layout.add(new_amt, constraints);
				constraints.gridx = 0;
				constraints.gridy = 4;
				layout.add(new JLabel("Detail:"), constraints);
				constraints.gridx = 1;
				constraints.gridy = 4;
				layout.add(new_plan_detail, constraints);
				return layout;
		}
		});
		right.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.SOUTH;
		
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 1;
		right.add(addPlan, constraints);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		right.add(update, constraints);
		constraints.insets = new Insets(5, 100, 5, 5);

		right.add(edit, constraints);
		constraints.insets = new Insets(5, 200, 5, 5);

		right.add(remove, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		right.add(new JLabel("Plan ID:"), constraints);
		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 2;
		right.add(planId, constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		right.add(new JLabel("Plan Name:"), constraints);
		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 3;

		right.add(plan_name, constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		right.add(new JLabel("Plan Amount:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 4;
		right.add(plan_amt, constraints);
		

		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 5;
		right.add(new JLabel("plan Months:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 5;
		right.add(plan_months, constraints);
		constraints.weightx = 0;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 6;
		right.add(new JLabel("Details:"), constraints);

		constraints.weightx = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 6;
		right.add(plan_detail, constraints);
	}

	private void addComponentLeft() {

		search = new JTextField();
		search.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				// System.out.println("chain");
			}

			@Override
			public void insertUpdate(DocumentEvent de) {
				// TODO Auto-generated method stub
				System.out.println("insertchain");
				searchUser(search.getText().toString());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				searchUser(search.getText().toString());
			}

		});
		left.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		left.add(search, constraints);
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		JButton searchButton = new JButton("Search");
		left.add(searchButton, constraints);

		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.weighty = 1;
		constraints.ipadx = 210;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.gridx = 0;
		constraints.gridy = 2;

		plansModel.addElement("None");
		getPlanList();
		final JList userList = new JList(plansModel);
		userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					try {
						JList source = (JList) lse.getSource();
						String selected = source.getSelectedValue().toString();
						int index = source.getSelectedIndex();
						System.out.println(index);
						setValuse(index);
						setEnableOrDisable(false);
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}
			}

		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane ListScrollPane = new JScrollPane(userList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		left.add(ListScrollPane, constraints);
	}
	protected void setValuse(int index) {
		// TODO Auto-generated method stub
		plan = plansList.get(index);
		planId.setText(plan.getPid());
		plan_amt.setText(plan.getAmt()+"");
		plan_name.setText(plan.getName());
		plan_months.setText(plan.getNoOfMonth() + "");
		plan_detail.setText(plan.getDetail());
	}
	protected void getPlanList() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		plansList = new ArrayList();
		searchedPlanList = new ArrayList();
		searchedPlanList = db.getPlansList(statement);
		plansList = db.getPlansList(statement);
		plansModel.removeAllElements();
		for (PlansModel p : plansList) {
			plansModel.addElement(p.getName());
		}
	}

	private void searchUser(String search) {
		plansModel.removeAllElements();
		searchedPlanList.clear();
		;
		for (PlansModel p : plansList) {
			if (p.getName().contains(search)) {

				plansModel.addElement(p.getName());
				searchedPlanList.add(p);
			}
		}
	}
}
