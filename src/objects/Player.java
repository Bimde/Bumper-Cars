package objects;

import java.awt.Color;
import java.util.ArrayList;

public class Player extends Entity {

	private boolean[] keysPressed;
	public final double SPEED_INCREMENT = 0.1, ANGLE_INCREMENT = 0.4;

	public Player(Color color, int x, int y, int size, boolean[] keysPressed, ArrayList<Entity> entityList) {
		super(color, new Point(x, y), new double[] { size, size, size, size }, entityList);
		this.keysPressed = keysPressed;
	}

	@Override
	public void move() {
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
			}
		} else if (keysPressed[Board.RIGHT]) {
			this.relativeDirectionChange(ANGLE_INCREMENT);
		}
		super.move();
	}
}
