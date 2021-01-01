package extra_classes;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import interfaces.CalClick;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Cal extends JPanel{
     JLabel lblMonth, lblYear;
     JButton btnPrev, btnNext;
     JTable tblCalendar;
     JComboBox cmbYear;
   // static JFrame frmMain;
    //static Container pane;
     DefaultTableModel mtblCalendar; //Table model
     JScrollPane stblCalendar; //The scrollpane
    private static ArrayList<Dates> dates;
    private CalClick click;
     int realYear, realMonth, realDay, currentYear, currentMonth,colorRow,colorCol;
    
    public Cal(CalClick click,String color){
    	this.click=click;
    	setLayout(null);
    	
        setBackground(Color.decode(color));
        //Create controls
        lblMonth = new JLabel ("January");
        lblYear = new JLabel ("Change year:");
        cmbYear = new JComboBox();
        btnPrev = new JButton ("&lt;&lt;");
        btnNext = new JButton ("&gt;&gt;");
        mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblCalendar = new JTable(mtblCalendar);
        stblCalendar = new JScrollPane(tblCalendar);
       // pnlCalendar = new JPanel(null);
        
        //Set border
        //setBorder(BorderFactory.createTitledBorder("Calendar"));
        
        //Register action listeners
        btnPrev.addActionListener(new btnPrev_Action());
        btnNext.addActionListener(new btnNext_Action());
        cmbYear.addActionListener(new cmbYear_Action());
        
        //Add controls to pane
        
        add(lblMonth);
        add(lblYear);
        add(cmbYear);
        add(btnPrev);
        add(btnNext);
        add(stblCalendar);
        
        //Set bounds
        setBounds(0, 0, 350, 335);
        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 100, 25);
        lblYear.setBounds(10, 305, 80, 20);
        cmbYear.setBounds(230, 305, 80, 20);
        btnPrev.setBounds(10, 25, 50, 25);
        btnNext.setBounds(260, 25, 50, 25);
        stblCalendar.setBounds(10, 50, 300, 250);

    	tblCalendar.setCellSelectionEnabled(true);
        tblCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override
        	 public void mouseClicked(java.awt.event.MouseEvent evt) {
        	    int row = tblCalendar.rowAtPoint(evt.getPoint());
        	    int col = tblCalendar.columnAtPoint(evt.getPoint());
        	    if (row >= 0 && col >= 0) {
        	    	System.out.println();	
        	    	int day=Integer.parseInt(tblCalendar.getValueAt(row, col).toString());
        	    	int m=currentMonth;
        	    	int y=currentYear;
        	    	m+=1;
        	    	click.getdate(day, m, y);
              	   }
        	 }
        	});
    }
	public void makeCal(ArrayList<Dates> dates){
    	this.dates=dates;
        
        //Make frame visible
        //Get real month/year
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;
        
        //Add headers
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i=0; i<7; i++){
            mtblCalendar.addColumn(headers[i]);
        }
        
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
        
        //No resize/reorder
        tblCalendar.getTableHeader().setResizingAllowed(false);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
        
        //Single cell selection
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Set row/column count
        tblCalendar.setRowHeight(38);
        mtblCalendar.setColumnCount(7);
        mtblCalendar.setRowCount(6);
        
        //Populate table
        for (int i=realYear-100; i<=realYear+100; i++){
            cmbYear.addItem(String.valueOf(i));
        }
        
        //Refresh calendar
        refreshCalendar (realMonth, realYear); //Refresh calendar
  
    }
    public void setDates(ArrayList<Dates> dates){
    	this.dates=dates;
    }
    
    public void refreshCalendar(int month, int year){
        //Variables
        String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som; //Number Of Days, Start Of Month
        
        //Allow/disallow buttons
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
        if (month == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
        if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
        lblMonth.setText("");
        lblMonth.setText(months[month]); //Refresh the month label (at the top)
        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
        cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
        
        //Clear table
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                mtblCalendar.setValueAt(null, i, j);
            }
        }
        
        //Get first day of month and number of days
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
        
        //Draw calendar
        for (int i=1; i<=nod; i++){
        	
            int row = new Integer((i+som-2)/7);
            int column  =  (i+som-2)%7;
            if(i==17){
        		colorRow=row;
        		colorCol=column;
        	}
            mtblCalendar.setValueAt(i, row, column);
        }
        
        //Apply renderers
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
                
    }
    
    class tblCalendarRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setForeground(Color.black);
            if(value!=null)
            if (checkPresent(Integer.parseInt(value.toString()),currentMonth,currentYear)){ //Week-end
                setBackground(new Color(200, 255, 200));
            }
            else{ //Week
                setBackground(new Color(255, 255, 255));
            }
            if (value != null){
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear){ //Today
                    //setBackground(new Color(220, 220, 255));
                	setForeground(Color.red);
                }
            }
            setBorder(null);
            
            return this;
        }
    }
    
    class btnPrev_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (currentMonth == 0){ //Back one year
                currentMonth = 11;
                currentYear -= 1;
            }
            else{ //Back one month
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }
    class btnNext_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (currentMonth == 11){ //Foward one year
                currentMonth = 0;
                currentYear += 1;
            }
            else{ //Foward one month
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }
    class cmbYear_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (cmbYear.getSelectedItem() != null){
                String b = cmbYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }
	public boolean checkPresent(int parseInt, int currentMonth2, int currentYear2) {
		boolean isFound=false;
		currentMonth2+=1;
		if(dates!=null)
		for(Dates d:dates){
			if(d.getD()==parseInt && currentMonth2==d.getM() && d.getY()== currentYear2){
				isFound=true;
			}
		}
		
		return isFound;
	}
}
