package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Entity {

	Double x, y;
	protected int size;
	protected Color color;
	protected Direction vector;
	protected ArrayList<Entity> entityList;
	public static final double MAX_VELOCITY = 50, MIN_VELOCITY = -MAX_VELOCITY / 2, FRICTION = 0.01;

	public Entity(Color color, int x, int y, int size, Direction vector, ArrayList<Entity> entityList) {
		this.color = color;
		this.x = x * 1.0;
		this.y = y * 1.0;
		this.size = size;
		this.vector = vector;
		this.entityList = entityList;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 10, 10);
		g2d.setColor(this.color);
		g2d.rotate(Math.toRadians(this.vector.angle), this.x + this.size / 2, this.y + this.size / 2);
		g2d.fillRect(this.x.intValue(), this.y.intValue(), this.size, this.size);
		g2d.dispose();
	}

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
		double radians = Math.toRadians(this.vector.angle);
		this.incrementX(this.vector.velocity * Math.sin(radians));
		this.incrementY(-this.vector.velocity * Math.cos(radians));
		this.friction();
	}

	protected void incrementX(double change) {
		synchronized (this.x) {
			this.x += change;
			if (this.x > Board.BOARD_SIZE - this.size && this.vector.angle < 180) {
				this.x = (Board.BOARD_SIZE - this.size) * 1.0;
				this.vector.angle = 360 - this.vector.angle;
			} else if (this.x < 0 && this.vector.angle > 180) {
				this.x = 0 * 1.0;
				this.vector.angle = 360 - this.vector.angle;
			}
		}
	}

	protected void incrementY(double change) {
		synchronized (this.y) {
			this.y += change;
			if (this.y > Board.BOARD_SIZE - this.size && this.vector.angle > 90 && this.vector.angle < 270) {
				this.y = (Board.BOARD_SIZE - this.size) * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
			} else if (this.y < 0 && (this.vector.angle < 90 || this.vector.angle > 270)) {
				this.y = 0 * 1.0;
				this.vector.angle = (360 + 180 - this.vector.angle) % 360;
			}
		}
	}

	public void friction() {
		if (this.vector.velocity > 0) {
			this.vector.velocity = Math.max(0, this.vector.velocity - FRICTION);
		} else {
			this.vector.velocity = Math.min(0, this.vector.velocity + FRICTION);
		}
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

	public Direction movement() {
		return this.vector;
	}

	public int x() {
		return this.x.intValue();
	}

	public int y() {
		return this.y.intValue();
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
}
