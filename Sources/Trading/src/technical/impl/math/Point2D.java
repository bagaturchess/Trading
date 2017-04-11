package technical.impl.math;

public class Point2D {

	private double x;
	private double y;
	
	public Point2D(double _x, double _y) {
		x = _x;
		y = _y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "[" + x + ", " + y + "]";
		return result;
	}
}
