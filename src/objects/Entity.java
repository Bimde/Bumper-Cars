package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class Entity {

	protected ArrayList<Vector> sides;
	protected Color color;
	protected Vector position, movement;
	protected ArrayList<Entity> entityList;
	public static final int ANGLE = 0, SPEED = 1, SIZE = 1;
	public static final double MAX_VELOCITY = 20,
			MIN_VELOCITY = -MAX_VELOCITY / 2, FRICTION = 0.01,
			TURN_FRICTION = 0.6, VELOCTY_EFFECT_ON_TURN_FRICTION = 0.5;

	public Entity(Color color, Vector position, double[] sideLengths,
			ArrayList<Entity> entityList) {
		this.color = color;
		double totalAngle = 180 * (sideLengths.length - 2);
		double increment = totalAngle / sideLengths.length;
		double currentAngle = 0;
		this.sides = new ArrayList<>();
		for (int i = 0; i < sideLengths.length; i++) {
			this.sides.add(new Vector(currentAngle, sideLengths[i]));
			currentAngle += increment;
		}
		this.position = position;
		this.movement = new Vector(0, 0);
		this.entityList = entityList;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString(
				"Angle: " + this.movement.x + " | Velocity: " + this.movement.y,
				10, 10);
		g2d.setColor(this.color);

		Path2D.Double shape = new Path2D.Double();
		double x = this.position.x, y = this.position.y;
		shape.moveTo(x, y);
		int size = this.sides.size();
		for (int i = 0; i < size - 1; i++) {
			Vector side = this.sides.get(i);
			double radians = Math.toRadians(side.x);
			x += side.y * Math.sin(radians);
			y += side.y * Math.cos(radians);
			shape.moveTo(x, y);
		}
		shape.closePath();
		g2d.fill(shape);
		g2d.dispose();
	}

	public synchronized void move() {
		for (Entity i : this.entityList)
			intersects(i);
		double radians = Math.toRadians(this.movement.x);
		this.incrementX(this.movement.y * Math.sin(radians));
		this.incrementY(-this.movement.y * Math.cos(radians));
		this.friction();
	}

	protected void incrementX(double change) {
		// for (int i = 0; i < points[X].length; i++) {
		// this.points[X][i] += change;
		// }
		// boolean collided = false;
		// for (int i = 0; !collided && i < this.points[X].length; i++) {
		// if (this.points[X][i] < 0) {
		// change = -points[X][i];
		// collided = true;
		// // this.vector.angle = 360 - this.vector.angle;
		// if (this.vector.y != 0)
		// this.relativeDirectionChange(
		// (360 - 2 * this.vector.x) / this.vector.y);
		// } else if (this.points[X][i] > Board.BOARD_SIZE) {
		// change = -(points[X][i] - Board.BOARD_SIZE);
		// collided = true;
		// // this.vector.angle = 360 - this.vector.angle;
		// if (this.vector.y != 0)
		// this.relativeDirectionChange(
		// (360 - 2 * this.vector.x) / this.vector.y);
		// }
		// }

		if (collided) {
			for (int i = 0; i < points[X].length; i++) {
				this.points[X][i] += change;
			}
		}

		this.position.x += change;
		if (this.position.x > Board.BOARD_SIZE - this.size && (this.forward()
				? this.movement.angle < 180 : this.movement.angle > 180)) {
			this.position.x = (Board.BOARD_SIZE - this.size) * 1.0;
			this.movement.x = 360 - this.movement.angle;
			Toolkit.getDefaultToolkit().beep();
		} else if (this.x < 0 && (this.forward() ? this.movement.angle > 180
				: this.movement.angle < 180)) {
			this.position.x = 0 * 1.0;
			this.movement.angle = 360 - this.movement.angle;
			Toolkit.getDefaultToolkit().beep();
		}
	}

	protected void incrementY(double change) {
		synchronized (this.points[Y]) {
			// for (int i = 0; i < points[Y].length; i++) {
			// this.points[Y][i] += change;
			// }
			// boolean collided = false;
			// for (int i = 0; !collided && i < this.points[Y].length; i++) {
			// if (this.points[Y][i] < 0) {
			// change = -points[Y][i];
			// collided = true;
			// this.vector.x = (360 + 180 - this.vector.x) % 360;
			// } else if (this.points[Y][i] > Board.BOARD_SIZE) {
			// change = -(points[Y][i] - Board.BOARD_SIZE);
			// collided = true;
			// this.vector.x = (360 + 180 - this.vector.x) % 360;
			// }
			// }
			//
			// if (collided) {
			// for (int i = 0; i < points[Y].length; i++) {
			// this.points[Y][i] += change;
			// }
			// }

			this.y += change;
			if (this.y > Board.BOARD_SIZE - this.size && (this.forward()
					? this.vector.angle > 90 && this.vector.angle < 270
					: this.vector.angle < 90 || this.vector.angle > 270)) {
				this.y = (Board.BOARD_SIZE - this.size) * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			} else if (this.y < 0 && (!this.forward()
					? this.vector.angle > 90 && this.vector.angle < 270
					: this.vector.angle < 90 || this.vector.angle > 270)) {
				this.y = 0 * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	public void friction() {
		if (this.vector.y > 0) {
			// Reduce the velocity of the vector proportionally based on the
			// current velocity and whether or not it is turning
			this.vector.y = Math.max(0,
					this.vector.y
							- FRICTION * (this.isTurning ? Math.max(
									TURN_FRICTION * this.vector.y
											* this.vector.y
											/ Math.pow(MAX_VELOCITY,
													VELOCTY_EFFECT_ON_TURN_FRICTION),
							1) : 1));
		} else {
			this.vector.y = Math.min(0,
					this.vector.y
							+ FRICTION
									* (this.isTurning ? Math.max(
											TURN_FRICTION * this.vector.y
													* this.vector.y
													/ Math.pow(
															Math.abs(
																	MIN_VELOCITY),
															VELOCTY_EFFECT_ON_TURN_FRICTION),
									1) : 1));
		}
	}

	// TODO Finish this + project method below
	protected void intersects(Entity i) {
		if (i == this)
			return;
	}

	protected double[][] getCords() {
		return this.points;
	}

	protected Axis[] getAxes() {
		Axis[] axes = new Axis[2];
		double slope = 0;
		if (this.vector.x != 90 && this.vector.x != 270) {
			slope = Math.tan(Math.toRadians(this.vector.x));
			axes[0] = new Axis(slope,
					this.points[Y][0] - slope * this.points[X][0]);
			// Lazy way for rectangles!
			if (slope == 0) {
				slope = 1 / slope;
				axes[1] = new Axis(slope,
						this.points[Y][0] - slope * this.points[X][0]);
			} else {
				axes[1] = new Axis(0,
						this.points[Y][0] - slope * this.points[X][0], true);
			}
		} else {
			axes[0] = new Axis(0, this.points[Y][0] - slope * this.points[X][0],
					true);

			axes[1] = new Axis(0,
					this.points[Y][0] - slope * this.points[X][0]);
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
				double x = (cords[i][1] * axis.slope + cords[i][0]
						- axis.slope * axis.yInt)
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
		this.vector.y = Math.max(MIN_VELOCITY,
				Math.min(this.vector.y + change, MAX_VELOCITY));
	}

	public synchronized void relativeDirectionChange(double angleChange) {
		this.vector.x = (360 + this.vector.x
				+ (angleChange = angleChange * this.vector.y)) % 360;
		double radians = Math.toRadians(angleChange);
		for (int i = 0; i < this.points[X].length; i++) {
			double cx = 0, cy = 0;
			for (int j = 0; j < this.points[X].length; j++) {
				cx += this.points[X][j];
				cy += this.points[Y][j];
			}
			cx = cx / 4;
			cy = cy / 4;

			double tempX = this.points[X][i] - cx;
			double tempY = this.points[Y][i] - cy;
			double theta = Math.toRadians(angleChange);
			double rotatedX = tempX * Math.cos(theta) - tempY * Math.sin(theta);
			double rotatedY = tempX * Math.sin(theta) + tempY * Math.cos(theta);

			this.points[X][i] = rotatedX + cx;
			this.points[Y][i] = rotatedY + cy;
		}
	}

	public synchronized void absoluteDirectionChange(double change) {
		this.vector.x = (this.vector.x + change + 360) % 360;
	}

	public void roundAngle() {
		this.movement.x = Math.round(this.movement.x);
	}

	public Vector movement() {
		return this.movement;
	}

	public Vector position() {
		return this.position;
	}

	public ArrayList<Vector> getSides() {
		return this.sides;
	}
}
