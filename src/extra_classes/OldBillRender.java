package extra_classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import models.OldBillModel;

public class OldBillRender extends JPanel implements  ListCellRenderer<OldBillModel> {

	private JLabel uid;
	private ArrayList<OldBillModel> billList;
	public OldBillRender(ArrayList<OldBillModel> billList){
		setLayout(new GridBagLayout());
		this.billList=billList;
		System.out.println("Render create");
		setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.gray));
		
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends OldBillModel> arg0, OldBillModel model, int arg2,
			boolean arg3, boolean arg4) {
		removeAll();
		
		System.out.println("Render called");
		uid=new JLabel("Bill Id:"+model.getBid());
		System.out.println("rendeer "+arg2);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx=1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		add(new JLabel("Date :"+f.format(model.getBill_date())),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weightx=1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		int no=arg2+1;
		add(new JLabel("Serial NO."+no),constraints);
		
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
		add(new JLabel("session from:"+model.getSession_from()),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("session To:"+model.getSession_to()),constraints);
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Amount Paid:"+model.getAmount_paid()),constraints);
		return this;
	}
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(350, 150);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

}
