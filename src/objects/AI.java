package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class AI extends Entity {

	private double angleChange;
	private static final double AI_VELOCITY = 5;
	private static final double DIRECTION_CHANGE_CHANCE = 0.1, ANGLE_CHANGE = 2;

	public AI(Color color, int x, int y, int size, ArrayList<Entity> entityList) {
		super(color, x, y, size, new Vector(Math.random() * 360, AI_VELOCITY), entityList);
		this.angleChange = (int) (Math.random() * 3) - 1;
		this.vector.velocity = AI_VELOCITY;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 10, 30);
		g2d.setColor(this.color);
		// g2d.rotate(Math.toRadians(this.vector.angle), this.x + this.size / 2,
		// this.y + this.size / 2);
		// g2d.fillRect(this.x.intValue(), this.y.intValue(), this.size,
		// this.size);

		g2d.fillPolygon(
				new int[] { (int) this.points[X][0], (int) this.points[X][1], (int) this.points[X][2],
						(int) this.points[X][3] },
				new int[] { (int) this.points[Y][0], (int) this.points[Y][1], (int) this.points[Y][2],
						(int) this.points[Y][3] },
				this.points[X].length);
		g2d.dispose();
	}

	synchronized void update() {
		if (Math.random() <= DIRECTION_CHANGE_CHANCE) {
			this.angleChange = (int) (Math.random() * 3) - 1;
			this.isTurning = this.angleChange == 0 ? false : true;
		}
		this.absoluteDirectionChange(this.angleChange * ANGLE_CHANGE);
	}

	/**
	 * Overrides Entity's move to prevent friction from applying
	 */
	@Override
	public synchronized void move() {
		double radians = Math.toRadians(this.vector.angle);
		this.incrementX(this.vector.velocity * Math.sin(radians));
		this.incrementY(-this.vector.velocity * Math.cos(radians));
		this.update();
	}
}
