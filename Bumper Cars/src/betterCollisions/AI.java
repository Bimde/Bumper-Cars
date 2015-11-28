package betterCollisions;

import java.awt.Color;

public class AI extends Entity {

	private int dCoX, dCoY;
	private static final int AI_VELOCITY = 5;
	private static final double DIRECTION_CHANGE_CHANCE = 0.1;

	public AI(Color color, int x, int y, int size) {
		super(color, x, y, size, new Direction(Math.random() * 360, AI_VELOCITY));
		this.dCoX = Math.random() < 0.5 ? 1 : -1;
		this.dCoY = Math.random() < 0.5 ? 1 : -1;
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
