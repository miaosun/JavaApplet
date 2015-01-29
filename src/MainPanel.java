import java.awt.BorderLayout;
import javax.swing.JPanel;

/*
 * The Main Panel of the Application
 * Consists of 2 parts: 
 * 1. middle side is the Operation Panel   @see OperationPanel
 * 2. lower part is the result panel, for showing operational results 
 */
public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	
	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		
		/*
		 *  MainPanel constructor
		 */
		OperationPanel operationPanel = new OperationPanel();
		this.add(operationPanel, BorderLayout.CENTER);

	}
}
