import java.awt.Container;

import javax.swing.JApplet;

/**
 *  This class is the main driver for the app. <p>
 *
 *  It creates the Main Panel which contains the operation and result panels.
 *  It then displays the GUI panel to the user.
 *
 *  @author Miao Sun
 */
public class Applet extends JApplet {

	private static final long serialVersionUID = 1L;

	/**
	 *  Main method to create the applet and display the GUI panel.
	 */
	public void init() {
		MainPanel mainPanel = new MainPanel();
		
		Container contentPane = this.getContentPane();
		contentPane.add(mainPanel);
	}
}