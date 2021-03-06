package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Entity {

	protected final ArrayList<Vector> sides;
	protected final Color color;
	protected final Point position;
	protected final Vector movement, firstPoint;
	protected final ArrayList<Entity> entityList;
	protected final Polygon shape;
	protected final int noSides;
	public static final int ANGLE = 0, SPEED = 1;
	public static final double MAX_VELOCITY = 20, MIN_VELOCITY = -MAX_VELOCITY / 2, FRICTION = 0.01,
			TURN_FRICTION = 0.6, VELOCTY_EFFECT_ON_TURN_FRICTION = 0.5;

	public Entity(Color color, Point position, double[] sideLengths, ArrayList<Entity> entityList) {
		this.color = color;
		double totalAngle = 180 * (sideLengths.length - 2);
		double increment = totalAngle / sideLengths.length;
		double currentAngle = 0;
		this.sides = new ArrayList<>();
		this.noSides = sideLengths.length;
		for (int i = 0; i < sideLengths.length; i++) {
			this.sides.add(new Vector(currentAngle, sideLengths[i]));
			currentAngle += increment;
		}
		this.shape = new Polygon(new int[sideLengths.length], new int[sideLengths.length], sideLengths.length);
		this.position = position;
		this.movement = new Vector(0, 0);
		this.entityList = entityList;
		this.firstPoint = this.loadFirstPoint();
	}

	public void paint(Graphics g) {
		final Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.movement.angle + " | Velocity: " + this.movement.speed, 10, 10);
		g2d.drawString("X: " + this.position.x + " | Y: " + this.position.y, 10, 25);

		for (int i = 0; i < this.shape.ypoints.length; i++) {
			g2d.drawString("Side " + (i + 1) + " | X: " + this.shape.xpoints[i] + " | Y: " + this.shape.ypoints[i], 10,
					40 + 15 * i);
		}
		g2d.setColor(this.color);

		// Path2D.Double shape = new Path2D.Double();
		// double x = this.position.x, y = this.position.y;
		// shape.moveTo(x, y);
		// int size = this.sides.size();
		// for (int i = 0; i < size - 1; i++) {
		// Vector side = this.sides.get(i);
		// double radians = Math.toRadians(side.x);
		// x += side.y * Math.sin(radians);
		// y += side.y * Math.cos(radians);
		// shape.moveTo(x, y);
		// }
		// shape.closePath();
		// g2d.fill(shape);

		g2d.fill(this.shape);
		g2d.dispose();
	}

	public synchronized void move() {
		for (Entity i : this.entityList)
			intersects(i);
		double radians = Math.toRadians(this.movement.angle);
		this.incrementX(this.movement.speed * Math.sin(radians));
		this.incrementY(-this.movement.speed * Math.cos(radians));
		// this.friction();
		this.updatePoints();
	}

	// TODO Update for support for non-square entities
	private Vector loadFirstPoint() {
		double length = this.sides.get(0).speed;
		double x = (-length) / 2, y = (-length) / 2;
		System.out.println(x + " : " + y);

		// Calculate vector (scalable to multiple shapes, depending on
		// implementation)
		Vector temp = new Vector((360 + Math.toDegrees(Math.atan(x / y))) % 360 + this.position.x,
				Math.sqrt(x * x + y * y) + this.position.y);
		System.out.println(temp.angle + " : " + temp.speed);
		return temp;
	}

	public Point getFirstPoint() {
		double radians = Math.toRadians((this.firstPoint.angle + this.movement.angle) % 360);
		Point temp = new Point(this.position.x + this.firstPoint.speed * Math.sin(radians),
				this.position.y + this.firstPoint.speed * Math.cos(radians));
		System.out.println(temp.x + " : " + temp.y);
		return temp;
	}

	private void updatePoints() {
		Point temp = this.getFirstPoint();
		this.shape.xpoints[0] = (int) temp.x;
		this.shape.ypoints[0] = (int) temp.y;
		for (int i = 1; i < this.noSides; i++) {
			Vector side = this.sides.get(i);
			double radians = Math.toRadians((side.angle + this.movement.angle) % 360);
			this.shape.xpoints[i] = (int) (this.shape.xpoints[i - 1] + side.speed * Math.sin(radians));
			this.shape.ypoints[i] = (int) (this.shape.ypoints[i - 1] + side.speed * Math.cos(radians));
		}
	}

	protected void incrementX(double change) {
		boolean collided = false;
		for (int i = 0; !collided && i < this.noSides; i++) {
			int point = (int) (this.shape.xpoints[i] + change);
			if (point < 0) {
				change -= point;
				collided = true;
				this.relativeDirectionChange((360 - 2 * this.movement.angle) / this.movement.speed);
				Toolkit.getDefaultToolkit().beep();
			} else if (point > Board.BOARD_SIZE) {
				change -= (point - Board.BOARD_SIZE);
				collided = true;
				this.relativeDirectionChange((360 - 2 * this.movement.angle) / this.movement.speed);
				Toolkit.getDefaultToolkit().beep();
			}
		}
		this.position.x += change;
	}

	protected void incrementY(double change) {
		boolean collided = false;
		for (int i = 0; !collided && i < this.noSides; i++) {
			int point = (int) (this.shape.ypoints[i] + change);
			if (point < 0) {
				change -= point;
				collided = true;
				this.movement.angle = (360 + 180 - this.movement.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			} else if (point > Board.BOARD_SIZE) {
				change -= (point - Board.BOARD_SIZE);
				collided = true;
				this.movement.angle = (360 + 180 - this.movement.angle) % 360;
				Toolkit.getDefaultToolkit().beep();
			}
		}
		this.position.y += change;
	}

	// public void friction() {
	// if (this.vector.y > 0) {
	// // Reduce the velocity of the vector proportionally based on the
	// // current velocity and whether or not it is turning
	// this.vector.y = Math.max(0,
	// this.vector.y
	// - FRICTION
	// * (this.isTurning
	// ? Math.max(
	// TURN_FRICTION * this.vector.y * this.vector.y
	// / Math.pow(MAX_VELOCITY, VELOCTY_EFFECT_ON_TURN_FRICTION),
	// 1)
	// : 1));
	// } else {
	// this.vector.y = Math.min(0,
	// this.vector.y
	// + FRICTION
	// * (this.isTurning ? Math.max(
	// TURN_FRICTION * this.vector.y * this.vector.y
	// / Math.pow(Math.abs(MIN_VELOCITY), VELOCTY_EFFECT_ON_TURN_FRICTION),
	// 1) : 1));
	// }
	// }

	// TODO Finish this + project method below
	protected void intersects(Entity i) {
		if (i == this)
			return;
	}

	protected Polygon getCords() {
		return this.shape;
	}

	// protected Axis[] getAxes() {
	// Axis[] axes = new Axis[2];
	// double slope = 0;
	// if (this.vector.x != 90 && this.vector.x != 270) {
	// slope = Math.tan(Math.toRadians(this.vector.x));
	// axes[0] = new Axis(slope, this.shape[Y][0] - slope * this.shape[X][0]);
	// // Lazy way for rectangles!
	// if (slope == 0) {
	// slope = 1 / slope;
	// axes[1] = new Axis(slope, this.shape[Y][0] - slope * this.shape[X][0]);
	// } else {
	// axes[1] = new Axis(0, this.shape[Y][0] - slope * this.shape[X][0], true);
	// }
	// } else {
	// axes[0] = new Axis(0, this.shape[Y][0] - slope * this.shape[X][0], true);
	//
	// axes[1] = new Axis(0, this.shape[Y][0] - slope * this.shape[X][0]);
	// }
	// return axes;
	// }

	// protected double[][] project(Axis axis) {
	// double[][] cords = this.getCords();
	// double[] min = new double[] { Double.MIN_VALUE, Double.MIN_VALUE };
	// double[] max = new double[] { Double.MAX_VALUE, Double.MAX_VALUE };
	//
	// if (axis.vertical) {
	// // TODO
	// } else {
	// for (int i = 0; i < cords.length; i++) {
	// double x = (cords[i][1] * axis.slope + cords[i][0] - axis.slope *
	// axis.yInt)
	// / (axis.slope * axis.slope + 1);
	// double y = axis.slope * x + axis.yInt;
	// if (y < min[1]) {
	// min[0] = x;
	// min[1] = y;
	// }
	// if (y > max[1]) {
	// max[0] = x;
	// max[1] = y;
	// }
	// }
	// }
	// return new double[][] { min, max };
	// }

	public synchronized void speed(double change) {
		this.movement.speed = Math.max(MIN_VELOCITY, Math.min(this.movement.speed + change, MAX_VELOCITY));
	}

	public synchronized void relativeDirectionChange(double angleChange) {
		this.movement.angle = (360 + this.movement.angle + (angleChange = angleChange * this.movement.speed)) % 360;
		// double radians = Math.toRadians(angleChange);
		// for (int i = 0; i < this.shape[X].length; i++) {
		// double cx = 0, cy = 0;
		// for (int j = 0; j < this.shape[X].length; j++) {
		// cx += this.shape[X][j];
		// cy += this.shape[Y][j];
		// }
		// cx = cx / 4;
		// cy = cy / 4;
		//
		// double tempX = this.shape[X][i] - cx;
		// double tempY = this.shape[Y][i] - cy;
		// double theta = Math.toRadians(angleChange);
		// double rotatedX = tempX * Math.cos(theta) - tempY * Math.sin(theta);
		// double rotatedY = tempX * Math.sin(theta) + tempY * Math.cos(theta);
		//
		// this.shape[X][i] = rotatedX + cx;
		// this.shape[Y][i] = rotatedY + cy;
		// }
	}

	public synchronized void absoluteDirectionChange(double change) {
		this.movement.angle = (this.movement.angle + change + 360) % 360;
	}

	public void roundAngle() {
		this.movement.angle = Math.round(this.movement.angle);
	}

	public Vector movement() {
		return this.movement;
	}

	public Point position() {
		return this.position;
	}

	public ArrayList<Vector> getSides() {
		return this.sides;
	}
}
