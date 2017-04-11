package technical.impl.math;


import java.util.ArrayList;
import java.util.List;

//SOURCE: http://www.ahristov.com/tutorial/geometry-games/convex-hull.html

//GOOGLE: incremental convex hull algorithm implemented in java
//http://www.experts-exchange.com/Other/Math_Science/Q_22065073.html
//http://ww3.algorithmdesign.net/handouts/IncrementalHull.pdf
//http://www.dreamincode.net/code/snippet4178.htm

public class QuickHull {

	// Returns the indexes inside the input sequence of the convex hull in the
	// correct order
	public static int[][] cHull(double[] sequence) {

		if (sequence.length < 2) {
			throw new IllegalStateException();
		}
		
		List<Point2D> points = new ArrayList<Point2D>();

		for (int i = 0; i < sequence.length; i++) {
			points.add(new Point2D(i, sequence[i]));
		}

		List<Point2D> result_list = quickHull(points);
		List<Point2D> result_list_up = new ArrayList<Point2D>();
		List<Point2D> result_list_down = new ArrayList<Point2D>();

		int startindex = -1;
		for (int i=0; i<result_list.size(); i++) {
			if (result_list.get(i).getX() == 0) {
				startindex = i;
				break;
			}
		}
		
		int endindex = -1;
		for (int i=0; i<result_list.size(); i++) {
			if (result_list.get(i).getX() == sequence.length - 1) {
				endindex = i;
				break;
			}
		}
		
		if (startindex == -1) {
			throw new IllegalStateException("startindex=" + startindex + ", endindex=" + endindex + "");
		}
		
		if (result_list.get(result_list.size() - 1).getX() != sequence.length - 1) {
			throw new IllegalStateException("" + startindex + ", " + result_list.size());
		}
		
		for (int i = startindex; i<=endindex; i++) {
			result_list_down.add(result_list.get(i));
		}
		
		for (int i = startindex; i>=0; i--) {
			result_list_up.add(result_list.get(i));
		}
		result_list_up.add(result_list.get(result_list.size() - 1));
		
		if (result_list_up.size() < 2) {
			throw new IllegalStateException("result_list_up.size()=" + result_list_up.size());
		}

		if (result_list_down.size() < 2) {
			throw new IllegalStateException("result_list_down.size()=" + result_list_down.size());
		}
		

		//Return result
		int[] result_array_down = new int[result_list_up.size()];

		for (int i = 0; i < result_array_down.length; i++) {
			result_array_down[i] = (int) result_list_up.get(i).getX();
		}

		int[] result_array_up = new int[result_list_down.size()];

		for (int i = 0; i < result_array_up.length; i++) {
			result_array_up[i] = (int) result_list_down.get(i).getX();
		}

		return new int[][] { result_array_down, result_array_up };
	}

	private static List<Point2D> quickHull(List<Point2D> points) {

		List<Point2D> convexHull = new ArrayList<Point2D>();
		if (points.size() < 3) {
			for (Point2D cur: points) {
				convexHull.add(cur);
			}
			return convexHull;
			//throw new IllegalStateException("TODO: return the points list");
		}

		// find extremals
		int minPoint = -1, maxPoint = -1;
		double minX = Integer.MAX_VALUE;
		double maxX = Integer.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() < minX) {
				minX = points.get(i).getX();
				minPoint = i;
			}
			if (points.get(i).getX() > maxX) {
				maxX = points.get(i).getX();
				maxPoint = i;
			}
		}
		Point2D A = points.get(minPoint);
		Point2D B = points.get(maxPoint);
		convexHull.add(A);
		convexHull.add(B);
		points.remove(A);
		points.remove(B);

		List<Point2D> leftSet = new ArrayList<Point2D>();
		List<Point2D> rightSet = new ArrayList<Point2D>();

		for (int i = 0; i < points.size(); i++) {
			Point2D p = points.get(i);
			if (pointLocation(A, B, p) == -1)
				leftSet.add(p);
			else
				rightSet.add(p);
		}
		hullSet(A, B, rightSet, convexHull);
		hullSet(B, A, leftSet, convexHull);

		return convexHull;
	}

	private static void hullSet(Point2D A, Point2D B, List<Point2D> set,
			List<Point2D> hull) {
		int insertPosition = hull.indexOf(B);
		if (set.size() == 0)
			return;
		if (set.size() == 1) {
			Point2D p = set.get(0);
			set.remove(p);
			hull.add(insertPosition, p);
			return;
		}
		double dist = Integer.MIN_VALUE;
		int furthestPoint = -1;
		for (int i = 0; i < set.size(); i++) {
			Point2D p = set.get(i);
			double distance = distance(A, B, p);
			if (distance > dist) {
				dist = distance;
				furthestPoint = i;
			}
		}
		Point2D P = set.get(furthestPoint);
		set.remove(furthestPoint);
		hull.add(insertPosition, P);

		// Determine who's to the left of AP
		List<Point2D> leftSetAP = new ArrayList<Point2D>();
		for (int i = 0; i < set.size(); i++) {
			Point2D M = set.get(i);
			if (pointLocation(A, P, M) == 1) {
				leftSetAP.add(M);
			}
		}

		// Determine who's to the left of PB
		List<Point2D> leftSetPB = new ArrayList<Point2D>();
		for (int i = 0; i < set.size(); i++) {
			Point2D M = set.get(i);
			if (pointLocation(P, B, M) == 1) {
				leftSetPB.add(M);
			}
		}
		hullSet(A, P, leftSetAP, hull);
		hullSet(P, B, leftSetPB, hull);

	}

	private static double pointLocation(Point2D A, Point2D B, Point2D P) {
		double cp1 = (B.getX() - A.getX()) * (P.getY() - A.getY())
				- (B.getY() - A.getY()) * (P.getX() - A.getX());
		return (cp1 > 0) ? 1 : -1;
	}

	/*
	 * Computes the square of the distance of point C to the segment defined by
	 * points AB
	 */
	private static double distance(Point2D A, Point2D B, Point2D C) {
		double ABx = B.getX() - A.getX();
		double ABy = B.getY() - A.getY();
		double num = ABx * (A.getY() - C.getY()) - ABy * (A.getX() - C.getX());
		if (num < 0)
			num = -num;
		return num;
	}
}
