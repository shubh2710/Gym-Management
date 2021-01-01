package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.myDatabaseHandler;
import db_keys.Keys;
import extra_classes.OldBillRender;
import models.OldBillModel;
import models.User;
import preferences.getPrefsSingletan;

public class OldBillsPanel extends JPanel {
	private JPanel right, left;
	private  JList BillList;
	private ArrayList<OldBillModel> billList;
	private OldBillModel oldBill;

	private ArrayList<User> userList;
	private ArrayList<User> searchedUserList;
	private JTextField search;
	private User user;
	private final DefaultListModel users = new DefaultListModel();

	private final DefaultListModel Bills = new DefaultListModel();
	
	public OldBillsPanel(){
		setLayout(new BorderLayout());
		addComponentRight();
		addComponentLeft();
		
	}
	private void addComponentRight() {
		// TODO Auto-generated method stub
		right = new JPanel();
		left = new JPanel();

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
		right.setPreferredSize(new Dimension(500, 800));
		add(right, BorderLayout.CENTER);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.weighty = 1;
		constraints.ipadx = 210;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		
		BillList = new JList(Bills);
		JScrollPane ListScrollPane = new JScrollPane(BillList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		ListScrollPane.setPreferredSize(new Dimension(500,600));
		right.add(ListScrollPane, constraints);
	}
	private void addComponentLeft() {

		
		
		left.setPreferredSize(new Dimension(250, 400));
		add(left, BorderLayout.WEST);
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

		users.addElement("None");
		getUserList();
		final JList userList = new JList(users);
		userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					try {
						JList source = (JList) lse.getSource();
						String selected = source.getSelectedValue().toString();
						int index = source.getSelectedIndex();
						setValuse(index);
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}
			}

			private void setValuse(int index) {
				// TODO Auto-generated method stub
				user = searchedUserList.get(index);
				getOldBillDetails(user.getUid()); 
				Bills.removeAllElements();
				for (OldBillModel b : billList) {
					Bills.addElement(b);
					System.out.println(b.getBid());
				}
				BillList.setCellRenderer(new OldBillRender(billList));
			}

		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane ListScrollPane = new JScrollPane(userList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		left.add(ListScrollPane, constraints);
	}
	protected void getUserList() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		userList = new ArrayList();
		searchedUserList = new ArrayList();
		searchedUserList = db.getUserList(statement);
		userList = db.getUserList(statement);
		users.removeAllElements();
		for (User u : userList) {
			users.addElement(u.getName());
		}
	}

	private void searchUser(String search) {
		users.removeAllElements();
		searchedUserList.clear();
		
		for (User u : userList) {
			String reg=u.getReg_no()+"";
			if (u.getName().contains(search) || reg.contains(search)) {
				users.addElement(u.getName());
				searchedUserList.add(u);
			}
		}
	}
	private void getOldBillDetails(String uid) {
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		if(user!=null)
		billList=db.getOldBillList(uid,statement);
	}
}
