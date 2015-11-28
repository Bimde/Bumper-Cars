package betterCollisions;

public class Vector {

	private double direction, velocity;

	public Vector(double direction, double speed) {
		this.direction = direction;
		this.velocity = speed;
	}

	public synchronized double incrementAndGetDirection(double angle) {
		return this.direction = (this.direction + angle) % 360;
	}

	public synchronized double incrementAndGetSpeed(double amount) {
		this.velocity = this.velocity + amount;
		if (this.velocity > Entity.MAX_VELOCITY)
			this.velocity = Entity.MAX_VELOCITY;
		return this.velocity;
	}

	synchronized void resetDirection(double degrees) {
		this.direction = Math.abs((this.direction = degrees) % 360);
	}

	synchronized void resetVelocity(double velocity) {
		this.velocity = velocity < 0 ? Math.max(-1 * Entity.MAX_VELOCITY,
				velocity) : Math.min(Entity.MAX_VELOCITY, velocity);
	}
}
