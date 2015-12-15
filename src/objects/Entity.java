package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Entity {

	protected double[][] points;
	protected int size;
	protected Color color;
	protected Vector vector;
	protected ArrayList<Entity> entityList;
	public static final double MAX_VELOCITY = 20, MIN_VELOCITY = -MAX_VELOCITY / 2, FRICTION = 0.01,
			TURN_FRICTION = 0.6, VELOCTY_EFFECT_ON_TURN_FRICTION = 0.5;
	private static final int X = 0, Y = 1;
	protected boolean isTurning;

	public Entity(Color color, double x, double y, int size, Vector vector, ArrayList<Entity> entityList) {
		this.color = color;
		int temp = size / 2;
		this.points = new double[][] { { x - temp, x + temp, x - temp, x + temp },
				{ y - temp, y + temp, y - temp, y + temp } };
		this.size = size;
		this.vector = vector;
		this.entityList = entityList;
		this.isTurning = false;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 10, 10);
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

	// TODO
	public boolean contains(int x, int y, int width, int height) {
		int tempX = this.x();
		int tempY = this.y();
		return (tempX + this.size > x && x + width > tempX && tempY + this.size > y && y + height > tempY);
	}

	public boolean contains(Entity other) {
		int tempX = this.x(), tempY = this.y(), otherX = other.x(), otherY = other.y();
		return (tempX + this.size > otherX && otherX + other.getWidth() > tempX && tempY + this.size > otherY
				&& otherY + other.getHeight() > tempY);
	}

	public synchronized void move() {
		for (Entity i : this.entityList)
			intersects(i);
		double radians = Math.toRadians(this.vector.angle);
		this.incrementX(this.vector.velocity * Math.sin(radians));
		this.incrementY(-this.vector.velocity * Math.cos(radians));
		this.friction();
	}

	protected void incrementX(double change) {
		synchronized (this.points[X]) {
			this.x += change;
			if (this.x > Board.BOARD_SIZE - this.size
					&& (this.forward() ? this.vector.angle < 180 : this.vector.angle > 180)) {
				this.x = (Board.BOARD_SIZE - this.size) * 1.0;
				this.vector.angle = 360 - this.vector.angle;
				Toolkit.getDefaultToolkit().beep();
			} else if (this.x < 0 && (this.forward() ? this.vector.angle > 180 : this.vector.angle < 180)) {
				this.x = 0 * 1.0;
				this.vector.angle = 360 - this.vector.angle;
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	protected void incrementY(double change) {
		synchronized (this.y) {
			this.y += change;
			if (this.y > Board.BOARD_SIZE - this.size
					&& (this.forward() ? this.vector.angle > 90 && this.vector.angle < 270
							: this.vector.angle < 90 || this.vector.angle > 270)) {
				this.y = (Board.BOARD_SIZE - this.size) * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			} else if (this.y < 0 && (!this.forward() ? this.vector.angle > 90 && this.vector.angle < 270
					: this.vector.angle < 90 || this.vector.angle > 270)) {
				this.y = 0 * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	public void friction() {
		if (this.vector.velocity > 0) {
			// Reduce the velocity of the vector proportionally based on the
			// current velocity and whether or not it is turning
			this.vector.velocity = Math.max(0,
					this.vector.velocity
							- FRICTION
									* (this.isTurning
											? Math.max(
													TURN_FRICTION * this.vector.velocity * this.vector.velocity
															/ Math.pow(MAX_VELOCITY, VELOCTY_EFFECT_ON_TURN_FRICTION),
													1)
											: 1));
		} else {
			this.vector.velocity = Math.min(0,
					this.vector.velocity
							+ FRICTION
									* (this.isTurning ? Math.max(
											TURN_FRICTION * this.vector.velocity * this.vector.velocity
													/ Math.pow(Math.abs(MIN_VELOCITY), VELOCTY_EFFECT_ON_TURN_FRICTION),
											1) : 1));
		}
	}

	// TODO Finish this + project method below
	protected void intersects(Entity i) {
		if (i == this)
			return;
	}

	protected double[][] getCords() {
		return new double
	}

	protected Axis[] getAxes() {
		Axis[] axes = new Axis[2];
		double slope = 0;
		if (this.vector.angle != 90 && this.vector.angle != 270) {
			slope = Math.tan(Math.toRadians(this.vector.angle));
			axes[0] = new Axis(slope, this.y - slope * this.x);
			// Lazy way for rectangles!
			if (slope == 0) {
				slope = 1 / slope;
				axes[1] = new Axis(slope, this.points[Y][0] - slope * this.points[X][0]);
			} else {
				axes[1] = new Axis(0, this.points[Y][0] - slope * this.points[X][0], true);
			}
		} else {
			axes[0] = new Axis(0, this.points[Y][0] - slope * this.points[X][0], true);

			axes[1] = new Axis(0, this.points[Y][0] - slope * this.points[X][0]);
		}
		return axes;
	}

	protected double[][] project(Axis axis) {
		double[][] cords = this.getCords();
		double[] min = new double[] { Double.MIN_VALUE, Double.MIN_VALUE };
		double[] max = new double[] { Double.MAX_VALUE, Double.MAX_VALUE };

		if (axis.vertical) {
			// TODO
		} else {
			for (int i = 0; i < cords.length; i++) {
				double x = (cords[i][1] * axis.slope + cords[i][0] - axis.slope * axis.yInt)
						/ (axis.slope * axis.slope + 1);
				double y = axis.slope * x + axis.yInt;
				if (y < min[1]) {
					min[0] = x;
					min[1] = y;
				}
				if (y > max[1]) {
					max[0] = x;
					max[1] = y;
				}
			}
		}
		return new double[][] { min, max };
	}

	public synchronized void speed(double change) {
		this.vector.velocity = Math.max(MIN_VELOCITY, Math.min(this.vector.velocity + change, MAX_VELOCITY));
	}

	public synchronized void relativeDirectionChange(double angleChange) {
		this.vector.angle = (360 + this.vector.angle + angleChange * this.vector.velocity) % 360;
	}

	public synchronized void absoluteDirectionChange(double change) {
		this.vector.angle = (this.vector.angle + change + 360) % 360;
	}

	public void roundAngle() {
		this.vector.angle = Math.round(this.vector.angle);
	}

	public Vector vector() {
		return this.vector;
	}

	public int size() {
		return this.size;
	}

	public int getWidth() {
		return this.size;
	}

	public int getHeight() {
		return this.size;
	}

	public boolean forward() {
		return this.vector.velocity > 0;
	}
}
