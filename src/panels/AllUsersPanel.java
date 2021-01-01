package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import extra_classes.AllUserRenders;
import extra_classes.OldBillRender;
import models.ActiveBillModel;
import models.OldBillModel;
import models.User;
import preferences.getPrefsSingletan;

public class AllUsersPanel extends JPanel {
	private JPanel right, left;
	private  JList BillList;
	private ArrayList<User> UserArrayList;
	private OldBillModel oldBill;
	private ArrayList<String> FilterList;
	private JTextField search;
	private User user;
	private final DefaultListModel Filters = new DefaultListModel();

	private final DefaultListModel Users = new DefaultListModel();
	
	public AllUsersPanel(){
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
		
		BillList = new JList(Users);
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
				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
			
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

		Filters.addElement("None");
		FilterList();
		final JList userList = new JList(Filters);
		userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					try {
						JList source = (JList) lse.getSource();
						String selected = source.getSelectedValue().toString();
						int index = source.getSelectedIndex();
						ApplyFilters(index);
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}
			}

			private void ApplyFilters(int index) {
				// TODO Auto-generated method stub
				String name = FilterList.get(index);
				UserArrayList=new ArrayList();
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				UserArrayList= db.getUserList(statement);
				Users.removeAllElements();
				Date today=new Date();
				switch(index){
				case 0:
					for (User u : UserArrayList) {
						Users.addElement(u);
						System.out.println(u.getUid());
					}
					break;
				case 1:
					for (User u : UserArrayList) {
						if(u.getJoiningDate().getMonth()<today.getMonth()){
							
						}else
						Users.addElement(u);
						System.out.println(u.getUid());
					}
					break;
				case 2:
					for (User u : UserArrayList) {
						if (getBill(u.getUid()).getSessionTo().after(new Date())) {
							Users.addElement(u);							
						} else {
						}
						}
					break;
				case 3:
					for (User u : UserArrayList) {
						if (getBill(u.getUid()).getSessionTo().after(new Date())) {
							
						} else {
							Users.addElement(u);
						}
						}
					break;
				}
				
				BillList.setCellRenderer(new AllUserRenders());
			}

		});
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane ListScrollPane = new JScrollPane(userList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		left.add(ListScrollPane, constraints);
	}
	private ActiveBillModel getBill(String uid){
		ActiveBillModel bill=null;
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		return	bill=db.getActiveBillListUser(statement,uid);
	}
	protected void FilterList() {
		FilterList = new ArrayList();
		FilterList.add("ALL");
		FilterList.add("NEW USER");
		FilterList.add("ACTIVE");
		FilterList.add("EXPIRED");
		Filters.removeAllElements();
		for (String u : FilterList) {
			Filters.addElement(u);
		}
	}
}
