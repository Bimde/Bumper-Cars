package betterCollisions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener, KeyListener {

	private ArrayList<Entity> entityList;
	private Player player;
	Timer timer;
	private static final Color START_COLOR = Color.BLACK;
	private Color color;
	public static final int MAX_SQUARE_SIZE = 40, BOARD_SIZE = 800,
			DELAY = 1000 / 60, MIN_SQAURE_SIZE = 20;

	public Board() {
		super();
		this.color = START_COLOR;
		this.setFocusable(true);
		this.addKeyListener(this);
		this.timer = new Timer(DELAY, this);
		this.timer.start();
		this.entityList = new ArrayList<Entity>();
	}

	private void addAI() {
		this.entityList
				.add(new AI(
						color,
						(int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
						(int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
						(int) (Math.random()
								* (MAX_SQUARE_SIZE - MIN_SQAURE_SIZE) + MIN_SQAURE_SIZE)));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < this.entityList.size(); i++) {
			this.entityList.get(i).paint(g);
		}
		this.player.paint(g);
	}

	public static void main(String[] args) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//this.player.update();
		this.repaint();
	}

}
