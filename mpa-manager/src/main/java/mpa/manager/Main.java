package mpa.manager;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mpa.manager.view.MpaAtual;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    
		try {
			MpaAtual frame = new MpaAtual();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
