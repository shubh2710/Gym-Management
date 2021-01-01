package extra_classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import database.myDatabaseHandler;
import models.ActiveBillModel;
import models.OldBillModel;
import models.PlansModel;
import models.User;

public class AllUserRenders extends JPanel implements  ListCellRenderer<User> {

	private JLabel uid;
	private ArrayList<PlansModel> plans;
	private ActiveBillModel bill;
	public AllUserRenders(){
		getAllPlans();
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.gray));
		
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends User> arg0, User model, int arg2,
			boolean arg3, boolean arg4) {
		removeAll();
		getBill(model.getUid());
		System.out.println("Render called");
		uid=new JLabel("Register Id:"+model.getReg_no());
		System.out.println("rendeer "+arg2);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx=1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		add(new JLabel("Joining Date :"+f.format(model.getJoiningDate())),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx=1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		int no=arg2+1;
		add(new JLabel("Serial NO. "+no),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx=1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		add(uid,constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Selected Plan:"+getSelectedPlan(model.getPlan()).getName()),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Last Pay:"+f.format(bill.getSessionFrom())),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Expired On:"+f.format(bill.getSessionTo())),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Name:"+model.getName()),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		JLabel status=new JLabel();
		if (bill.getSessionTo().after(new Date())) {
			status.setText("Active");
			status.setForeground(new Color(45, 180, 100));
		} else {
			status.setText("Expired");
			status.setForeground(new Color(150, 45, 60));
		}
		add(status,constraints);
		return this;
	}
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(350, 180);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

	private PlansModel getSelectedPlan(String string) {
		PlansModel p=null;
		for (int i = 0; i < plans.size(); i++) {
			if (plans.get(i).getPid().equals(string)) {
				p=plans.get(i);
			}
		}
		return p;
	}
	private void getBill(String uid){

		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		bill=db.getActiveBillListUser(statement,uid);
	}
	private void getAllPlans() {
		// TODO Auto-generated method stub
		plans = new ArrayList();
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myDatabaseHandler db = new myDatabaseHandler();
				Statement statement = db.getStatement();
				plans = db.getPlansList(statement);
			}
			
		}).start();
	}

}
