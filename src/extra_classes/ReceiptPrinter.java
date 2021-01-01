/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extra_classes;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.myDatabaseHandler;
import models.ActiveBillModel;
import models.CompanyDetailModel;
import models.User;

public class ReceiptPrinter implements Printable {
    
	private User user;
	private ActiveBillModel bill;
	private CompanyDetailModel company=null;
	private String companyName = "NAME",address="",mob="",gstNo="";
	private int gstPersentage=0;
	private Date today;
	private DateFormat f = new SimpleDateFormat("dd-MM-yyyy h:mm a");

  public ReceiptPrinter(User user, ActiveBillModel bill) {
	  getCompanyDetails();
	  if (user != null && bill != null) {
			this.user = user;
			this.bill = bill;
			if(company!=null){
			this.companyName = company.getName().toUpperCase();
			this.address=company.getAddress();
			this.mob=company.getMob();
			this.gstNo=company.getGstNo();
			this.gstPersentage=company.getGstPercent();
			}
			this.today = new Date();
		}
	}

	private void getCompanyDetails() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		company = db.getCompanyDetail(statement);
	}

public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
  throws PrinterException 
  {    
      
                
        
      int result = NO_SUCH_PAGE;    
        if (pageIndex == 0) {                    
        
            Graphics2D g2d = (Graphics2D) graphics;                    

            double width = pageFormat.getImageableWidth();                    
           
            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 

            ////////// code by alqama//////////////

            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
        //    int idLength=metrics.stringWidth("000000");
            //int idLength=metrics.stringWidth("00");
            int idLength=metrics.stringWidth("000");
            int amtLength=metrics.stringWidth("000000");
            int qtyLength=metrics.stringWidth("00000");
            int priceLength=metrics.stringWidth("000000");
            int prodLength=(int)width - idLength - amtLength - qtyLength - priceLength-17;

        //    int idPosition=0;
        //    int productPosition=idPosition + idLength + 2;
        //    int pricePosition=productPosition + prodLength +10;
        //    int qtyPosition=pricePosition + priceLength + 2;
        //    int amtPosition=qtyPosition + qtyLength + 2;
            
            int productPosition = 0;
            int discountPosition= prodLength+5;
            int pricePosition = discountPosition +idLength+10;
            int qtyPosition=pricePosition + priceLength + 4;
            int amtPosition=qtyPosition + qtyLength;
        try{
            /*Draw Header*/
            int y=20;
            int yShift = 10;
            int headerRectHeight=15;
            
            ///////////////// Product price Get ///////////
            DateFormat f2 = new SimpleDateFormat("dd-MM-yyyy");
             g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
             g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;
            g2d.drawString("      	    "+companyName+"          ",12,y);y+=yShift;
            g2d.drawString("  "+address+"                        ",12,y);y+=yShift;
            g2d.drawString("      DATE "+f.format(today)+"	     ",12,y);y+=yShift;
            g2d.drawString("      	    GST No :"+gstNo+"	     ",12,y);y+=yShift;
            g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;
            g2d.drawString("                Bill Id:             ",10,y);y+=yShift;
            g2d.drawString("            "+bill.getBid()+"           ",10,y);y+=yShift;
            g2d.drawString("-------------------------------------",10,y);y+=yShift;
            g2d.drawString(" Reg No:              "+user.getReg_no()+"",10,y);y+=yShift;
            g2d.drawString(" Name:                "+user.getName()+"",10,y);y+=yShift;
            g2d.drawString(" Joining:             "+f2.format(user.getJoiningDate()),10,y);y+=yShift;
            g2d.drawString(" Session From:        "+f2.format(bill.getSessionFrom()),10,y);y+=yShift;
            g2d.drawString(" Session To:          "+f2.format(bill.getSessionTo()),10,y);y+=yShift;
            g2d.drawString("--------------------------------------",10,y);y+=headerRectHeight;
            g2d.drawString(" Amount:     "+bill.getAmountPaid()+"",10,y);y+=yShift;
            g2d.drawString(" Discount:   0%                        ",10,y);y+=yShift;
            g2d.drawString(" GST :       "+gstPersentage+"%        ",10,y);y+=yShift;
            g2d.drawString(" Net amount: 378 Rs                  ",10,y);y+=yShift;
            g2d.drawString("-------------------------------------",10,y);y+=yShift;
            g2d.drawString("              Mobile No              ",10,y);y+=yShift;
            g2d.drawString("               "+mob+"               ",10,y);y+=yShift;
            g2d.drawString("*************************************",10,y);y+=yShift;
            g2d.drawString("           THANKS TO VISIT           ",10,y);y+=yShift;
            g2d.drawString("*************************************",10,y);y+=yShift;
                   
           
             
           
            
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
          

    }
    catch(Exception r){
    r.printStackTrace();
    }

           result = PAGE_EXISTS;    
     }    
         return result;    
 
  }
 }
             
  