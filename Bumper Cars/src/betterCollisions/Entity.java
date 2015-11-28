package betterCollisions;

import java.awt.Color;
import java.awt.Graphics;

public class Entity {

	protected int x, y;
	private Color color;
	private int size;
	private Vector vector;
	public static final double MAX_VELOCITY = 10;

	public Entity(Color color, int x, int y, int size) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public void paint(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x, this.y, this.size, this.size);
	}

	public boolean contains(int x, int y, int width, int height) {
		int tempX = this.modifyAndGetX(0);
		int tempY = this.modifyAndGetY(0);
		return (tempX + this.size > x && x + width > tempX
				&& tempY + this.size > y && y + height > tempY);
	}

	public boolean contains(Entity other) {
		int tempX = this.modifyAndGetX(0), tempY = this.modifyAndGetY(0), otherX = other
				.modifyAndGetX(0), otherY = other.modifyAndGetY(0);
		return (tempX + this.size > otherX && otherX + other.getWidth() > tempX
				&& tempY + this.size > otherY && otherY + other.getHeight() > tempY);
	}

	public synchronized void speed(int change) {
		this.vector.incrementAndGetSpeed(change);
	}

	public synchronized void direction(int angleChange) {
		this.vector.incrementAndGetDirection(angleChange);
	}

	synchronized int modifyAndGetX(int amount) {
		return this.x += amount;
	}

	synchronized int modifyAndGetY(int amount) {
		return this.y += amount;
	}

	public int getSize() {
		return this.size;
	}

	public int getWidth() {
		return this.size;
	}

	public int getHeight() {
		return this.size;
	}
}
