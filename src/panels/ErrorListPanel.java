package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ErrorListPanel extends JPanel{
	private ArrayList<String> list;
	private  JList showList;
	private final DefaultListModel errors = new DefaultListModel();
	public ErrorListPanel(ArrayList<String> list){
		this.list=list;
		setLayout(new BorderLayout());
		showList=new JList(errors);
		showList.setBorder(new EmptyBorder(5,5,5,5));
		JScrollPane ListScrollPane = new JScrollPane(showList);
		ListScrollPane.setBackground(new Color(40, 150, 100));
		ListScrollPane.setPreferredSize(new Dimension(500,600));
		add(ListScrollPane,BorderLayout.CENTER);
		for(String err:list){
			errors.addElement(err);
		}
	}
}
