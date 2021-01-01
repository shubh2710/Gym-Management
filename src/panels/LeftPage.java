package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gymManagement.ShowPanel;

import db_keys.Keys;
import preferences.getPrefsSingletan;

public class LeftPage extends JPanel {

	private Image image = null;
	private int iWidth2;
	private int iHeight2;

	JLabel pic=new JLabel();
	private BufferedImage img;
	private Preferences prefs;
	 URL path;
	public LeftPage(ShowPanel parent) {
		prefs = new getPrefsSingletan().getPrefs();
		String image=prefs.get(Keys.BACKIMAGE, "");
		if(image!=null && image.length()>0){
			try {
				this.img=ImageIO.read(new File(image));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else{
			 path=getClass().getResource("/images/frontPic2.jpg");
				try {
					System.out.println(path);
					this.img=ImageIO.read(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
		
			setPreferredSize(new Dimension((int)getPersent(parent.getWidth()), parent.getHeight()));
			System.out.println((int)getPersent(parent.getWidth()));
		 pic.setPreferredSize(getPreferredSize());
		pic.setBackground(Color.black);
		
		Image newimg=this.img.getScaledInstance((int)pic.getPreferredSize().getWidth(),(int)pic.getPreferredSize().getHeight(),Image.SCALE_SMOOTH);
		pic.setIcon(new ImageIcon(newimg));
		add(pic);
	}
	private double getPersent(int width) {
		// TODO Auto-generated method stub
		System.out.println(width);
		double f=70.00/100.00;
		
		return f*(double)width;
	}
	
	/*public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, (int)getPreferredSize().getWidth(), (int)getPreferredSize().getHeight(), null);
	}*/
}
