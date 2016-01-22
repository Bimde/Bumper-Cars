package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	public static final int MAX_SQUARE_SIZE = 40, BOARD_SIZE = 600, DELAY = 1000 / 60, MIN_SQUARE_SIZE = 20, UP = 0,
			DOWN = 1, LEFT = 2, RIGHT = 3;

	public Board() {
		super();
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
					(int) (Math.random() * (MAX_SQUARE_SIZE - MIN_SQUARE_SIZE) + MIN_SQUARE_SIZE), this.entityList));
		} else {
			this.player = new Player(START_COLOR, (int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)),
					(int) (Math.random() * (BOARD_SIZE - MAX_SQUARE_SIZE)), (MAX_SQUARE_SIZE + MIN_SQUARE_SIZE) / 2,
					this.keysPressed, this.entityList);
			this.entityList.add(this.player);
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 1; i < this.entityList.size(); i++) {
			this.entityList.get(i).paint(g);
		}
		this.entityList.get(0).paint(g);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Bumper Cars");
		Board board = new Board();
		frame.add(board);
		frame.setSize(BOARD_SIZE + 16, BOARD_SIZE + 39);
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
		else if (key == KeyEvent.VK_F) {
			System.out.println("Number of AI: " + this.entityList.size() + "\nVelocity: " + this.player.movement.speed
					+ "\nAngle: " + this.player.movement.angle);
			for (int i = 0; i < 3; i++)
				System.out.println("------------------------------------------------");
		}
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
		else if (key == KeyEvent.VK_R)
			this.player.roundAngle();

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
		for (Entity i : this.entityList) {
			i.move();
		}
	}
}
