package panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class AdminDetailPanel extends JPanel {
	Image img;

	public AdminDetailPanel(Image img) {
		super();
		this.img = img;
		setPreferredSize(new Dimension(500, 480));
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

}
