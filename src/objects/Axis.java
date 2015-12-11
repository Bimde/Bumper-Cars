package objects;

public class Axis {
	public double slope, yInt;
	public boolean vertical;

	public Axis(double slope, double yInt) {
		this.slope = slope;
		this.yInt = yInt;
		this.vertical = false;
	}

	public Axis(double slope, double yInt, boolean vertical) {
		this.slope = slope;
		this.yInt = yInt;
		this.vertical = vertical;
	}
}
