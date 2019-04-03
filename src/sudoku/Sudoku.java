package sudoku;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sudoku extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7204984644200054930L;

	private int[][] panel;

	private Point posicionSeleccionada;

	private int tamCelda = 30;

	private int numToResaltar;

	/**
	 * Create the panel.
	 * 
	 * @param frame
	 */
	public Sudoku(JFrame frame) {
		this.panel = new int[9][9];
		int cantNumeros = (int) (Math.random() * 5) + 80;
		while (cantNumeros > 0) {

			int y = (int) (Math.random() * 9);
			int x = (int) (Math.random() * 9);
			int n = (int) (Math.random() * 9) + 1;

			if (n > 0 && this.panel[x][y] == 0 && add(y, x, n))
				cantNumeros--;
		}
		paintComponent(frame.getGraphics());
		this.addMouseListener(getMouseEvent());
		this.addKeyListener(getKeyEvent());
		frame.setSize((tamCelda) * 9+7, (tamCelda) * 10 );
		frame.setResizable(false);
	}

	private boolean add(int indexY, int indexX, int numero) {
		if (!validar(indexY, indexX, numero))
			return false;

		this.panel[indexY][indexX] = numero;
		return true;
	}

	private boolean validar(int indexX, int indexY, int numero) {
		if (!examinarLineaV(indexX, numero))
			return false;

		if (!examinarLineaH(indexY, numero))
			return false;

		return examinarCuadrado(indexX, indexY, numero);
	}

	private boolean examinarLineaV(int indexX, int numero) {
		int movimiento = 0;
		while (movimiento < 9)
			if (this.panel[indexX][movimiento++] == numero)
				return false;
		return true;
	}

	private boolean examinarLineaH(int indexY, int numero) {
		int movimiento;
		movimiento = 0;
		while (movimiento < 9)
			if (this.panel[movimiento++][indexY] == numero)
				return false;
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
				if (this.panel[x][y] == numero)
					return false;
				movimientoX++;
			}
			movimientoY++;
		}
		return true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		dibujar(g);
	}

	private void dibujar(Graphics g) {
		if (g == null)
			return;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 500, 500);
		g.setFont(new Font("Arial", Font.BOLD, 15));

		drawSeparaciones(g, tamCelda);

		drawNumeros(g, tamCelda);

		if (numToResaltar != 0)
			resaltar(numToResaltar);

		this.removeAll();
		this.repaint();
	}

	private void drawNumeros(Graphics g, int tamCelda) {
		int indexX;
		int indexY = 0;
		String numero;
		g.setColor(Color.BLACK);
		while (indexY < 9) {
			indexX = 0;
			while (indexX < 9) {
				int num = this.panel[indexX][indexY];
				if (num != 0) {
					numero = num + "";
					g.drawString(numero, 7 + indexX * tamCelda, 16 + indexY * tamCelda);
				}
				indexX++;
			}
			indexY++;
		}
	}

	private void drawSeparaciones(Graphics g, int tamCelda) {
		int linea = 0;
		while (linea < 10) {
			int i = tamCelda * linea++;
			int j = tamCelda * 9;

			if ((linea - 1) % 3 == 0)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.GRAY);

			g.drawLine(0, i, j, i);// horizontales
			g.drawLine(i, 0, i, j);// verticales
		}

	}

	public KeyAdapter getKeyEvent() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int num = e.getKeyCode() - '0';
				boolean ctrl = e.isControlDown();

				boolean isBorrar = e.getKeyCode() == KeyEvent.VK_BACK_SPACE;
				boolean isResaltar = posicionSeleccionada == null && !ctrl;
				boolean isNumero = num > 0 && num < 10;

				if (!isNumero && !isResaltar && !isBorrar)
					return;

				if (ctrl) {
					numToResaltar = num;
				} else if (isNumero) {
					int x = posicionSeleccionada.x;
					int y = posicionSeleccionada.y;
					add(x, y, num);
				} else if (isBorrar) {
					int x = posicionSeleccionada.x;
					int y = posicionSeleccionada.y;
					panel[x][y] = 0;
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL)
					numToResaltar = 0;
			}
		};
	}

	private void resaltar(int num) {
		int x = -1, y = -1;

		while (++x < 9) {
			y = -1;
			while (++y < 9) {
				if (this.panel[x][y] == num) {
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

	public MouseAdapter getMouseEvent() {
		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int x = e.getPoint().x / tamCelda;
				int y = e.getPoint().y / tamCelda;

				posicionSeleccionada = new Point(x, y);
			}

		};
	}

}
