import java.awt.BorderLayout;
import javax.swing.JPanel;


public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		
		OperationPanel operationPanel = new OperationPanel();
		this.add(operationPanel, BorderLayout.CENTER);

	}
}
