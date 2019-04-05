package sudoku;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class Ventana {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ventana() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		try {
			String[] option = { "Facilito", "Medio..", "Gede", "Dio" };
			int opcion = JOptionPane.showOptionDialog(null, "Elegi un modo wacho", "Que miras el titulo?",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, option[1]);

			Sudoku sudoku = new Sudoku(this.frame, opcion);
			GridBagConstraints gbc_sudoku = new GridBagConstraints();
			gbc_sudoku.fill = GridBagConstraints.BOTH;
			gbc_sudoku.gridx = 0;
			gbc_sudoku.gridy = 0;
			frame.getContentPane().add(sudoku, gbc_sudoku);
		} catch (Exception e) {
			frame.dispose();
		}

	}

}
