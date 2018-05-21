package com.mininet.main;

import javax.swing.JFrame;

import com.mininet.view.FrontView;

/**
 * 
 * @author 
 * Main class of our application
 */
public class MiniNet {
	/**
	 * Mainmethod where our application strts
	 * @param args
	 */
	public static void main(String args[])
	{
		/**
		 * Setting Nimbus look
		 */
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrontView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrontView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrontView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrontView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				//making frame Class object
				FrontView frame=new FrontView();
				//setting the frame to full screen
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				//terminate application if frame is closed
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//set title of the app
				frame.setTitle("My MININET");
				//set the app to visible
				frame.setVisible(true);
			}
		});
	}
}
