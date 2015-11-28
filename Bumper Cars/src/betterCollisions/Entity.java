package betterCollisions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Entity {

	int x, y, size;
	private Color color;
	private Direction vector;
	public static final double MAX_VELOCITY = 5;

	public Entity(Color color, int x, int y, int size, Direction vector) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.size = size;
		this.vector = vector;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawString("Angle: " + this.vector.angle + " | Velocity: " + this.vector.velocity, 120, 120);
		g2d.setColor(this.color);
		g2d.rotate(Math.toRadians(this.vector.angle), this.x + this.size / 2, this.y + this.size / 2);
		g2d.fillRect(this.x, this.y, this.size, this.size);
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
		this.y -= this.vector.velocity * Math.cos(this.vector.angle);
		this.x += this.vector.velocity * Math.sin(this.vector.angle);
	}

	public synchronized void speed(int change) {
		this.vector.velocity = Math.max(-MAX_VELOCITY, Math.min(this.vector.velocity + change, MAX_VELOCITY));
	}

	public synchronized void direction(int angleChange) {
		this.vector.angle = (360 + this.vector.angle + angleChange) % 360;
	}

	public Direction movement() {
		return this.vector;
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
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
