package snippet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import interfaces.WebCamReply;
import panels.RegisterUser;

public class Snippet {
	String location;
	String name;
	WebCamReply reply;
	public void open(String location,String name,WebCamReply reply){
		this.reply=reply;
		this.location=location;
		this.name=name;
		Webcam webcam = Webcam.getDefault();
		
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		JPanel window = new JPanel();
		window.setLayout(new BorderLayout());
		window.add(panel,BorderLayout.NORTH);
		JButton capture =new JButton("take");
		capture.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//window.add(capture,BorderLayout.SOUTH);
		window.setVisible(true);
		int dialogResult = JOptionPane.showConfirmDialog(null, window,
				"Take Picture : ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			webcam.open();
			BufferedImage image = webcam.getImage();
			try {
				ImageIO.write( image,"PNG", new File(location+"/"+name+".png"));
				reply.camReply(location+"/"+name+".png");
				webcam.close();
				//window.setVisible(false); //you can't see me!
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(dialogResult == JOptionPane.CANCEL_OPTION){
			webcam.close();
		}
		}
}