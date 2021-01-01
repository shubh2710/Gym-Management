package extra_classes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.qoppa.pdfWriter.PDFDocument;
import com.qoppa.pdfWriter.PDFGraphics;
import com.qoppa.pdfWriter.PDFPage;

import database.myDatabaseHandler;
import models.ActiveBillModel;
import models.CompanyDetailModel;
import models.User;

public class BillPrinter 
{	private User user;
	private ActiveBillModel bill;
	private String companyName = "GYM MANGEMT";
	private Date today;
	private Graphics2D g2 = null;
	PDFDocument pdfDoc = new PDFDocument();
	PDFPage newPage = pdfDoc.createPage(new PageFormat());
	private CompanyDetailModel company=null;
	private DateFormat f = new SimpleDateFormat("dd-MM-yyyy");

	public BillPrinter(User user, ActiveBillModel bill) {
		getCompanyDetails();
		if (user != null && bill != null ) {
			this.user = user;
			this.bill = bill;
			if(company!=null)
			this.companyName = company.getName();
			else
				this.companyName = "NAME";
			this.today = new Date();
		}
		makeBill2();
	}

	private void getCompanyDetails() {
		// TODO Auto-generated method stub
		myDatabaseHandler db = new myDatabaseHandler();
		Statement statement = db.getStatement();
		// db.createUserTable(statement);
		company = db.getCompanyDetail(statement);
	}
	private void makeBill2() {
		BufferedImage master = null;	
		URL url=getClass().getResource("/images/invoice.bmp");
		try {
			master = ImageIO.read(url);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		}
		Graphics2D g2d = newPage.createGraphics();
		g2d.drawImage(master, 60, 70,500,650, null);
		g2d.setFont(PDFGraphics.HELVETICA.deriveFont(24f));
		//g2d.drawString(this.companyName, 200, 100);
		g2d.setFont(PDFGraphics.HELVETICA.deriveFont(15f));
		
		
		pdfDoc.addPage(newPage);
		String path = "C:gym_manngment/bills";
		try {
			Files.createDirectories(Paths.get(path));
			pdfDoc.saveDocument("C:gym_manngment/bills/" + user.getUid() + ".pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
