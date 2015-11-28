package betterCollisions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener, KeyListener {

	private ArrayList<Entity> entityList;
	private Player player;
	private boolean[] keysPressed = { false, false, false, false };
	Timer timer;
	private static final Color START_COLOR = Color.BLACK;
	private Color color;
	public static final int MAX_SQUARE_SIZE = 40, BOARD_SIZE = 800, DELAY = 1000 / 60, MIN_SQAURE_SIZE = 20, UP = 0,
			DOWN = 1, LEFT = 2, RIGHT = 3;

	public Board() {
		super();
		this.color = START_COLOR;
		this.setFocusable(true);
		this.addKeyListener(this);
		this.timer = new Timer(DELAY, this);
		this.timer.start();
		this.entityList = new ArrayList<Entity>();
		this.addEntity(false);
	}

	private void addEntity(boolean isAI) {
		if (isAI) {
			this.entityList.add(new AI(Color.ORANGE, (int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
					(int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
					(int) (Math.random() * (MAX_SQUARE_SIZE - MIN_SQAURE_SIZE) + MIN_SQAURE_SIZE)));
		} else {
			this.player = new Player(START_COLOR, (int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
					(int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)), (MAX_SQUARE_SIZE + MIN_SQAURE_SIZE) / 2,
					this.keysPressed);
			this.entityList.add(this.player);
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < this.entityList.size(); i++) {
			this.entityList.get(i).paint(g);
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Bumper Cars");
		frame.add(new Board());
		frame.setSize(BOARD_SIZE, BOARD_SIZE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
			this.keysPressed[UP] = true;
		else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
			this.keysPressed[DOWN] = true;
		else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
			this.keysPressed[LEFT] = true;
		else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
			this.keysPressed[RIGHT] = true;
		else if (key == KeyEvent.VK_V)
			this.addEntity(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
			this.keysPressed[UP] = false;
		else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
			this.keysPressed[DOWN] = false;
		else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
			this.keysPressed[LEFT] = false;
		else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
			this.keysPressed[RIGHT] = false;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_V)
			this.addEntity(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
		for (Entity i : this.entityList) {
			i.move();
		}
	}
}
