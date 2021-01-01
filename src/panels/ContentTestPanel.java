package panels;

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
import javax.swing.JPanel;

import db_keys.Keys;
import preferences.getPrefsSingletan;

public class ContentTestPanel extends JPanel {

	private Image image = null;
	private int iWidth2;
	private int iHeight2;

	private Image img;
	private Preferences prefs;
	 URL path;
	
	public ContentTestPanel() {
		
		setPreferredSize(new Dimension(500, 480));
		prefs = new getPrefsSingletan().getPrefs();
		String image=prefs.get(Keys.BACKIMAGE, null);
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
					this.img=ImageIO.read(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
	}


	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

}
