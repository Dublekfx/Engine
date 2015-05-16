package data_structures;

public class Vector2D {
	public double x;
	public double y;
	
	public void Vector2D () {
		x = 0;
		y = 0;
	}
	public void Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void Vector2d(Vector2D otherVector) {
		this.x = otherVector.x;
		this.y = otherVector.y;
	}
	
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	public void add(Vector2D otherVector) {
		this.x += otherVector.x;
		this.y += otherVector.y;
	}
	
}
