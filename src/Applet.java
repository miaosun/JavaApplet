import java.awt.Container;

import javax.swing.JApplet;


public class Applet extends JApplet {

	private static final long serialVersionUID = 1L;

	public void init() {
		MainPanel mainPanel = new MainPanel();
		
		Container contentPane = this.getContentPane();
		contentPane.add(mainPanel);
	}
}
