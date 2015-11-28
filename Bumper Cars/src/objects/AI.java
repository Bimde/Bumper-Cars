package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class AI extends Entity {

	private int dCoX, dCoY;
	private static final int AI_VELOCITY = 5;
	private static final double DIRECTION_CHANGE_CHANCE = 0.1;

	public AI(Color color, int x, int y, int size) {
		super(color, x, y, size, new Direction(Math.random() * 360, AI_VELOCITY));
		this.dCoX = Math.random() < 0.5 ? 1 : -1;
		this.dCoY = Math.random() < 0.5 ? 1 : -1;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 10, 30);
		g2d.setColor(this.color);
		g2d.rotate(Math.toRadians(this.vector.angle), this.x + this.size / 2, this.y + this.size / 2);
		g2d.fillRect((int) this.x, (int) this.y, this.size, this.size);
		g2d.dispose();
	}

	synchronized void changeX(int dif) {
		if (Math.random() <= DIRECTION_CHANGE_CHANCE) {
			this.dCoX *= -1;
		}
		this.x += dif * this.dCoX;
		if (this.x < 0) {
			this.x = 0;
			this.dCoX *= -1;
		} else if (this.x > Board.BOARD_SIZE - this.size()) {
			this.x = Board.BOARD_SIZE - this.size();
			this.dCoX *= -1;
		}
	}

	synchronized void changeY(int dif) {
		if (Math.random() <= DIRECTION_CHANGE_CHANCE) {
			this.dCoY *= -1;
		}
		this.y += dif * this.dCoY;
		if (this.y < 0) {
			this.y = 0;
			this.dCoY *= -1;
		} else if (this.y > Board.BOARD_SIZE - this.size()) {
			this.y = Board.BOARD_SIZE - this.size();
			this.dCoY *= -1;
		}
	}

	@Override
	public void move() {
		this.changeX(AI_VELOCITY);
		this.changeY(AI_VELOCITY);
	}
}
