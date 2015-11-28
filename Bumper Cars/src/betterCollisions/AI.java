package betterCollisions;

import java.awt.Color;

public class AI extends Entity {

	private int dCoX;
	private int dCoY;
	private static final int AI_INCREMENT = 5;
	private static final double DIRECTION_CHANGE_CHANCE = 0.1;

	public AI(Color color, int x, int y, int size) {
		super(color, x, y, size);
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
		} else if (this.x > Board.BOARD_SIZE - this.getSize()) {
			this.x = Board.BOARD_SIZE - this.getSize();
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
		} else if (this.y > Board.BOARD_SIZE - this.getSize()) {
			this.y = Board.BOARD_SIZE - this.getSize();
			this.dCoY *= -1;
		}
	}

	void updatePos() {
		this.changeX(AI_INCREMENT);
		this.changeY(AI_INCREMENT);
	}

}
