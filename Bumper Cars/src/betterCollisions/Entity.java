package betterCollisions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Entity {

	double x, y;
	protected int size;
	protected Color color;
	protected Direction vector;
	public static final double MAX_VELOCITY = 10, MIN_VELOCITY = -MAX_VELOCITY / 2, FRICTION = 0.01;

	public Entity(Color color, int x, int y, int size, Direction vector) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.size = size;
		this.vector = vector;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 10, 10);
		g2d.setColor(this.color);
		g2d.rotate(Math.toRadians(this.vector.angle), this.x + this.size / 2, this.y + this.size / 2);
		g2d.fillRect((int) this.x, (int) this.y, this.size, this.size);
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
		this.x += this.vector.velocity * Math.sin(radians);
		this.y -= this.vector.velocity * Math.cos(radians);
		this.friction();
	}

	protected void incrementX(int change) {
		// this.x += change;
		// if(this.x > Board.BOARD_SIZE - this.size) {
		// this.x = Board.BOARD_SIZE - this.size;
		// this.direction(angleChange);
		// }
		//
	}

	protected void incrementY(int change) {
		this.y += change;
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

	public synchronized void direction(double angleChange) {
		this.vector.angle = (360 + this.vector.angle
				+ (this.vector.velocity > 0 ? angleChange : this.vector.velocity < 0 ? -angleChange : 0)
						* Math.abs(this.vector.velocity))
				% 360;
	}

	public Direction movement() {
		return this.vector;
	}

	public int x() {
		return (int) this.x;
	}

	public int y() {
		return (int) this.y;
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
