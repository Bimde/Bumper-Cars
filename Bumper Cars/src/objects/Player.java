package objects;

import java.awt.Color;
import java.util.ArrayList;

public class Player extends Entity {

	private boolean[] keysPressed;
	private final double SPEED_INCREMENT = 0.1, ANGLE_INCREMENT = 0.6;

	public Player(Color color, int x, int y, int size, boolean[] keysPressed, ArrayList<Entity> entityList) {
		super(color, x, y, size, new Direction(0, 0), entityList);
		this.keysPressed = keysPressed;
	}

	@Override
	public void move() {
		boolean turning = false;
		if (keysPressed[Board.UP]) {
			if (!keysPressed[Board.DOWN]) {
				this.speed(SPEED_INCREMENT);
			}
		} else if (keysPressed[Board.DOWN]) {
			this.speed(-SPEED_INCREMENT);
		}
		if (keysPressed[Board.LEFT]) {
			if (!keysPressed[Board.RIGHT]) {
				this.relativeDirectionChange(-ANGLE_INCREMENT);
				turning = true;
			}
		} else if (keysPressed[Board.RIGHT]) {
			this.relativeDirectionChange(ANGLE_INCREMENT);
			turning = true;
		}
		this.isTurning = turning;
		super.move();
	}
}
