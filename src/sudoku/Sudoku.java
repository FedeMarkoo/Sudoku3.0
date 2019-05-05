package sudoku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sudoku extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7204984644200054930L;

	private int tamCelda = 30;

	private JTextField[][] numeros;

	private Color gris = Color.LIGHT_GRAY; // Color.getHSBColor(0,0, 84);

	private JFrame ventana;

	private int dificultad;

	/**
	 * Create the panel.
	 * 
	 * @param frame
	 */
	public Sudoku(JFrame frame, int dificultad) {
		this.numeros = new JTextField[9][9];
		this.ventana = frame;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		double[] ds = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		gridBagLayout.columnWeights = ds;
		gridBagLayout.rowWeights = ds;
		this.setLayout(gridBagLayout);
		setVista();
		this.dificultad=dificultad;
		int cantNumeros = (int) (Math.random() * (dificultad * 15)) + 20;

		while (!generadorInicial())
			;
		borrarCuadros(cantNumeros);

		frame.setSize((tamCelda) * 9 + 7, (tamCelda) * 10);
		frame.setResizable(false);
	}

	private void borrarCuadros(int cantNumeros) {
		while (--cantNumeros != 0) {
			int x = (int) ((Math.random() * 9));
			int y = (int) ((Math.random() * 9));
			JTextField jTextField = this.numeros[x][y];

			jTextField.setText("");
			jTextField.setEnabled(true);
		}
	}

	private boolean generadorInicial() {
		limpiar();
		int x = -1, y;
		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				int num = (int) ((Math.random() * 9));
				int numero = ((++num) % 9) + 1;
				int intentos = 0;
				while (!add(x, y, numero)) {
					numero = ((++num) % 9) + 1;
					if (++intentos > 10)
						return false;
				}
			}
		}
		return true;

	}

	private void limpiar() {
		int x = -1, y;
		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				this.numeros[x][y].setText("");
			}
		}

	}

	private void setVista() {
		int x = -1, y;
		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				JTextField temp = new JTextField();
				temp.setSize(tamCelda, tamCelda);
				temp.addKeyListener(new KeyAdapter() {

					@Override
					public void keyPressed(KeyEvent e) {
						temp.setText("");

						Point l = temp.getLocation();
						int indX = l.x / tamCelda;
						int indY = l.y / tamCelda;

						if (((indX / 3 + indY / 3)) % 2 == 1)
							temp.setBackground(gris);
						else
							temp.setBackground(Color.WHITE);

					}

					@Override
					public void keyReleased(KeyEvent e) {
						temp.setText("");

						int num = e.getKeyCode() - '0';
						Point l = temp.getLocation();

						int indX = l.x / tamCelda;
						int indY = l.y / tamCelda;

						if (((indX / 3 + indY / 3)) % 2 == 1)
							temp.setBackground(gris);
						else
							temp.setBackground(Color.WHITE);

						if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
							if (add(indX, indY, num))
								temp.setText(e.getKeyChar() + "");
							else
								temp.setBackground(Color.RED);
						}
						if (gano())
							ganar();
					}
				});

				GridBagConstraints gbc_sudoku = new GridBagConstraints();
				gbc_sudoku.fill = GridBagConstraints.BOTH;
				gbc_sudoku.gridx = x;
				gbc_sudoku.gridy = y;

				if (((x / 3 + y / 3)) % 2 == 1) {
					temp.setBackground(gris);
				} else
					temp.setBackground(Color.WHITE);

				temp.setEnabled(false);
				temp.setDisabledTextColor(Color.BLACK);
				temp.setHorizontalAlignment(JTextField.CENTER);
				this.add(temp, gbc_sudoku);

				this.numeros[x][y] = temp;
			}
		}
	}

	protected void ganar() {
		String[] option = { "Volver a empezar", "Salgo por cagon" };
		int eligio = JOptionPane.showOptionDialog(null, "GANASTE!! bien ahi wachin", "ganaste!!",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, option[1]);

		if (eligio == 0)
			reinicio();
		else
			ventana.dispose();
	}

	private void reinicio() {
		int cantNumeros = (int) (Math.random() * (dificultad * 15)) + 20;

		while (!generadorInicial())
			;
		borrarCuadros(cantNumeros);

		
	}

	private boolean gano() {
		int x = -1, y;
		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				if (this.numeros[x][y].getText().isEmpty())
					return false;
			}
		}
		return true;
	}

	private boolean add(int indexY, int indexX, int numero) {
		if (!validar(indexY, indexX, numero))
			return false;

		this.numeros[indexY][indexX].setText(numero + "");

		return true;
	}

	private boolean validar(int indexX, int indexY, int numero) {
		if (numero < 1 || numero > 9)
			return false;

		if (!examinarLineaV(indexX, numero))
			return false;

		if (!examinarLineaH(indexY, numero))
			return false;

		return examinarCuadrado(indexX, indexY, numero);
	}

	private boolean examinarLineaH(int indexY, int numero) {
		int movimiento = 0;
		while (movimiento < 9) {
			String textNum = this.numeros[movimiento++][indexY].getText();
			try {
				if (Integer.parseInt(textNum) == numero)
					return false;
			} catch (Exception e) {
			}
		}
		return true;
	}

	private boolean examinarLineaV(int indexX, int numero) {
		int movimiento;
		movimiento = 0;
		while (movimiento < 9) {
			String textNum = this.numeros[indexX][movimiento++].getText();
			try {
				if (Integer.parseInt(textNum) == numero)
					return false;
			} catch (Exception e) {
			}
		}
		return true;
	}

	private boolean examinarCuadrado(int indexX, int indexY, int numero) {
		int cuadradoX = indexX;
		int cuadradoY = indexY;
		cuadradoY -= cuadradoY % 3;
		cuadradoX -= cuadradoX % 3;

		int movimientoY = 0;
		while (movimientoY < 3) {
			int movimientoX = 0;
			while (movimientoX < 3) {
				int y = cuadradoY + movimientoY;
				int x = cuadradoX + movimientoX;
				String textNum = this.numeros[x][y].getText();
				try {
					if (Integer.parseInt(textNum) == numero)
						return false;
				} catch (Exception e) {
				}
				movimientoX++;
			}
			movimientoY++;
		}
		return true;
	}

	protected void resaltar(int num) {
		int x = -1, y = -1;

		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				if (this.numeros[x][y].getText() == num + "") {
					int x2 = x * tamCelda;
					int y2 = y * tamCelda;
					Graphics g = this.getGraphics();
					g.setColor(Color.YELLOW);
					g.fillRect(x2, y2, tamCelda, tamCelda);
					g.setColor(Color.BLACK);
					g.drawString(num + "", 7 + x2, 16 + y2);
				}
			}
		}
	}

}
