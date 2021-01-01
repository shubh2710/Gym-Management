package com.gymManagement;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ShowPanel sp = new ShowPanel();
				sp.setVisible(true);
			}
		});
	}
}
