package betterCollisions;

import java.awt.Color;

public class Player extends Entity {

	private boolean[] keysPressed;
	private final double SPEED_INCREMENT = 0.1, ANGLE_INCREMENT = 0.2;

	public Player(Color color, int x, int y, int size, boolean[] keysPressed) {
		super(color, x, y, size, new Direction(0, 0));
		this.keysPressed = keysPressed;
	}

	@Override
	public void move() {
		if (keysPressed[Board.UP])
			this.speed(SPEED_INCREMENT);
		if (keysPressed[Board.DOWN])
			this.speed(-SPEED_INCREMENT);
		if (keysPressed[Board.LEFT])
			this.direction(-ANGLE_INCREMENT);
		if (keysPressed[Board.RIGHT])
			this.direction(ANGLE_INCREMENT);
		super.move();
	}
}
