import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;


public class OperationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * The button labeled "LU pivot"
	 */
	protected JButton luButton;
	
	/**
	 * The button labeled "Inverse"
	 */
	protected JButton inverseButton;

	/**
	 * The button labeled "Clear"
	 */
	protected JButton clearButton;
	
	/**
	 * The button labeled "Load"
	 */
	protected JButton loadButton;
	
	/**
	 * The button labeled "Save"
	 */
	protected JButton saveButton;

	/**
	 * The center area for matrix and vector input
	 */
	protected JPanel centerPanel;

	/**
	 * The button panel for holding lu_pivot, inverse and clear buttons. 
	 */
	protected JPanel bottomPanel;

	/**
	 * The textarea for matrix insertion
	 */
	protected JTextArea matrixTextArea;

	/**
	 * The textarea for vector insertion
	 */
	protected JTextArea vectorTextArea;

	/**
	 * The textarea for displaying result
	 */
	protected JTextArea resultTextArea;
	
	public OperationPanel() {

		this.setLayout(new BorderLayout());

		//bottom area
		luButton = new JButton("LU Pivot");
		luButton.setToolTipText("Get LU pivot result");
		inverseButton = new JButton("Inverse");
		inverseButton.setToolTipText("Get matrix inversion result");
		clearButton = new JButton("Clear");
		clearButton.setToolTipText("Clears all the contents");
		ToolTipManager.sharedInstance().setInitialDelay(5);

		// Action Listeners
		luButton.addActionListener(new LUActionListener());
		inverseButton.addActionListener(new InverseActionListener());
		clearButton.addActionListener(new ClearActionListener());
		
		bottomPanel = new JPanel();
		bottomPanel.add(luButton);
		bottomPanel.add(inverseButton);
		bottomPanel.add(clearButton);

		this.add(bottomPanel, BorderLayout.CENTER);

		//center area
		centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout());

		JLabel matrixLabel = new JLabel("A = ");
		JLabel vectorLabel = new JLabel("  b = ");
		matrixTextArea = new JTextArea(12,30);
		vectorTextArea = new JTextArea(12,16);
		JScrollPane matrixPane = new JScrollPane(matrixTextArea);
		TitledBorder matrixBorder = BorderFactory.createTitledBorder("Matrix:");
		matrixBorder.setTitleColor(Color.BLACK);
		matrixPane.setBorder(matrixBorder);
		JScrollPane vectorPane = new JScrollPane(vectorTextArea);
		TitledBorder vectorBorder = BorderFactory.createTitledBorder("Vector:");
		vectorBorder.setTitleColor(Color.BLACK);
		vectorPane.setBorder(vectorBorder);

		centerPanel.add(matrixLabel);
		centerPanel.add(matrixPane);
		centerPanel.add(vectorLabel);
		centerPanel.add(vectorPane);

		this.add(centerPanel, BorderLayout.NORTH);
		this.setPreferredSize(this.getPreferredSize());
		
		resultTextArea = new JTextArea(20,15);
		resultTextArea.setEditable(false);
		JScrollPane resultPane = new JScrollPane(resultTextArea);

		TitledBorder resultBorder = BorderFactory.createTitledBorder("Result:");
		resultBorder.setTitleColor(Color.BLACK);
		resultPane.setBorder(resultBorder);
		this.add(resultPane, BorderLayout.SOUTH);

	}

	public class LUActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String res = "LU Decomposition with scaled partial pivoting\nOriginal matrix (Doolittle factorisation)\n";
			Matrix matrix = getMatrix();
			res += matrix.toString();
			Vector vector = getVector();
			res += "\nOriginal vector\n" + vector.toString();

			//error message for size incompatibility
			if(matrix.ncols != vector.size())
			{
				JOptionPane.showMessageDialog(new JFrame(), "ERROR\nmatrix and vector size not match for operations");	
			}

			if(matrix.CalcDeterminant() == 0)
				res += "\nSingular matrix";

			else
			{
				Vector x = new Vector(vector.size());
				Matrix p = new Matrix(matrix.nrows, matrix.ncols);
				matrix.reorder(matrix, matrix.nrows, p);

				Matrix pa = new Matrix(matrix.nrows, matrix.ncols);
				pa = new Matrix(p.mult(matrix));

				Matrix lower = new Matrix(matrix.nrows, matrix.ncols);
				Matrix upper = new Matrix(matrix.nrows, matrix.ncols);
				matrix.lu_fact(pa, lower, upper, matrix.nrows);

				res += "\nLower matrix\n" + lower.toString();
				res += "\nUpper matrix\n" + upper.toString();

				x = matrix.lu_solve(lower, upper, p.mult(vector));

				res += "\nSolution\n" + x.toString();
				res += "\nDeterminant = " + matrix.CalcDeterminant() + "\n";
			}

			resultTextArea.setText(res);
		}
	}

	public class InverseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String res = "Matrix Inversion\n";

			Matrix matrix = getMatrix();
			res += "\nOriginal matrix\n" + matrix.toString();

			if(matrix.nrows != matrix.ncols)
				JOptionPane.showMessageDialog(new JFrame(), "ERROR\nmatrix needs to be square for operations");

			//Vector vector = getVector();
			//res += "\nOriginal vector\n" + vector.toString();

			if(matrix.CalcDeterminant() == 0)
				res += "\nSingular matrix";

			else
			{
				//Vector x = new Vector(vector.size());
				Matrix p = new Matrix(matrix.nrows, matrix.ncols);
				matrix.reorder(matrix, matrix.nrows, p);

				Matrix pa = new Matrix(matrix.nrows, matrix.ncols);
				pa = new Matrix(p.mult(matrix));

				Matrix lower = new Matrix(matrix.nrows, matrix.ncols);
				Matrix upper = new Matrix(matrix.nrows, matrix.ncols);
				matrix.lu_fact(pa, lower, upper, matrix.nrows);
				res += "\nLower matrix\n" + lower.toString();
				res += "\nUpper matrix\n" + upper.toString();

				Matrix inverse = new Matrix(matrix.inverse(lower, upper));
				res += "\nInverse matrix\n" + inverse.toString();

				res += "\nDeterminant = " + matrix.CalcDeterminant() + "\n";
				res += "\nPivot array\n"; //TODO: + pivot array
			}
			
			resultTextArea.setText(res);
		}
	}

	public class ClearActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			matrixTextArea.setText("");
			vectorTextArea.setText("");
			resultTextArea.setText("");
		}

	}

	public Vector getVector() {
		String vector_text = vectorTextArea.getText();
		String[] vector_aux = vector_text.trim().split("\n");
		if(vector_aux.length > 1)
			throw new IllegalArgumentException("vector not in right format");

		int n = vector_aux[0].trim().split("\\s+").length;
		Vector vector = new Vector(n);
		String[] vector_number = vector_aux[0].trim().split("\\s+");
		for(int i=0; i<vector_number.length; i++)
		{
			vector.set(i, Double.parseDouble(vector_number[i]));
		}
		return vector;
	}

	public Matrix getMatrix() {
		String matrix_text = matrixTextArea.getText();	
		String[] matrix_aux = matrix_text.trim().split("\n");
		int ncols = matrix_aux[0].trim().split("\\s+").length;
		if(matrix_aux.length > 1)
		{
			for(int i=1; i<matrix_aux.length; i++)
				if(ncols != matrix_aux[i].trim().split("\\s+").length)
					throw new IllegalArgumentException("matrix not in right format");
		}

		Matrix matrix = new Matrix(matrix_aux.length, ncols);
		for(int i=0; i<matrix_aux.length; i++)
		{
			String[] matrix_number = matrix_aux[i].trim().split("\\s+");
			for(int j=0; j<matrix_number.length; j++)
			{
				matrix.set(i, j, Double.parseDouble(matrix_number[j]));
			}
		}
		return matrix;
	}
}
