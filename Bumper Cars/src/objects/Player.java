package objects;

import java.awt.Color;
import java.util.ArrayList;

public class Player extends Entity {

	private boolean[] keysPressed;
	private final double SPEED_INCREMENT = 0.1, ANGLE_INCREMENT = 0.4;

	public Player(Color color, int x, int y, int size, boolean[] keysPressed, ArrayList<Entity> entityList) {
		super(color, x, y, size, new Direction(0, 0), entityList);
		this.keysPressed = keysPressed;
	}

	@Override
	public void move() {
		if (keysPressed[Board.UP])
			this.speed(SPEED_INCREMENT);
		if (keysPressed[Board.DOWN])
			this.speed(-SPEED_INCREMENT);
		if (keysPressed[Board.LEFT])
			this.relativeDirectionChange(-ANGLE_INCREMENT);
		if (keysPressed[Board.RIGHT])
			this.relativeDirectionChange(ANGLE_INCREMENT);
		super.move();
	}
}
