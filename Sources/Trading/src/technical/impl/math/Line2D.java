package technical.impl.math;


import java.util.List;

import technical.impl.utils.LineUtils;


public class Line2D {
	
	
	private List<Point2D> points;
	//private Point2D p1;
	//private Point2D p2;
	private double[] coeffs;
	
	
	public Line2D(double[] _coeffs, int fromX, int toX) {
		coeffs = _coeffs;
		points = LineUtils.line_2pointsFromCoeffs(coeffs, fromX, toX);
		//p1 = points.get(0);
		//p2 = points.get(1);
	}
	
	public double calculate(double X) {
		return coeffs[0] + X * coeffs[1];
	}
	
	public List<Point2D> get2Points() {
		return points;
	}
	
	public double getCoeffA() {
		return coeffs[1];
	}
}
