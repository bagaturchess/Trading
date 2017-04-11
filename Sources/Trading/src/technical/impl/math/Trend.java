package technical.impl.math;

import java.util.List;

import technical.impl.utils.LineUtils;
import technical.impl.utils.SequenceUtils;

public class Trend {
	
	
	private double[] sequence;
	
	private Line2D trend_resistence;
	private Line2D trend_middle;
	private Line2D trend_support;
	private List<Point2D> chull_up;
	private List<Point2D> chull_down;
	

	public Trend(double[] _sequence) {
		sequence = _sequence;
		init();
	}
	
	private void init() {
		
		//convert to points
		List<Point2D> all = SequenceUtils.buildPointsList(sequence);
		
		//int[][] chulls_up_down =  ConvexHuff.cHull(sequence);
		int[][] chulls_down_up =  QuickHull.cHull(sequence);
		
		chull_down = LineUtils.lines_FromSelectedIndexesOfSequence(sequence, chulls_down_up[0]);
		chull_up = LineUtils.lines_FromSelectedIndexesOfSequence(sequence, chulls_down_up[1]);
		
		trend_resistence = new Line2D(LinearRegressionMeans.regressionLine(chull_up), 0, sequence.length);
		trend_middle = new Line2D(LinearRegressionMeans.regressionLine(all), 0, sequence.length);
		trend_support = new Line2D(LinearRegressionMeans.regressionLine(chull_down), 0, sequence.length); 
	}

	public Line2D getTrendResistence() {
		return trend_resistence;
	}

	public Line2D getTrendMiddle() {
		return trend_middle;
	}

	public Line2D getTrendSupport() {
		return trend_support;
	}
	
	public List<Point2D> getChullUp() {
		return chull_up;
	}

	public List<Point2D> getCHullDown() {
		return chull_down;
	}
}
