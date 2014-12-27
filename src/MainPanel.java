import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		
		OperationPanel operationPanel = new OperationPanel();
		this.add(operationPanel, BorderLayout.CENTER);

	}
}
