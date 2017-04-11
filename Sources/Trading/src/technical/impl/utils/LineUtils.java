package technical.impl.utils;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.Point2D;


public class LineUtils {
	
	public static List<Point2D> line_2pointsFromCoeffs(double[] coeffs, int fromX, int toX) {
		List<Point2D> two_points = new ArrayList<Point2D>();
		two_points.add(new Point2D(fromX, coeffs[0] + fromX * coeffs[1]));
		two_points.add(new Point2D(toX, coeffs[0] + toX * coeffs[1]));
		return two_points;
	}
	
	public static List<Point2D> lines_FromSelectedIndexesOfSequence(double[] input, int[] indexes) {
		List<Point2D> list = new ArrayList<Point2D>();
		for (int i=0; i<indexes.length; i++) {
			int cur_idx = indexes[i];
			list.add(new Point2D(cur_idx, input[cur_idx]));
		}
		return list;
	}
	
	public static List<Point2D> lines_FromSequence(double[] input) {
		List<Point2D> list = new ArrayList<Point2D>();
		for (int i=0; i<input.length; i++) {
			double cur = input[i];
			list.add(new Point2D(i, cur));
		}
		return list;
	}
	
	public static List<Point2D> lines_FromSequence(List<Double> input) {
		List<Point2D> list = new ArrayList<Point2D>();
		for (int i=0; i<input.size(); i++) {
			double cur = input.get(i);
			list.add(new Point2D(i, cur));
		}
		return list;
	}
}
