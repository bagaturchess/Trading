package technical.impl.math;

import java.util.List;



//http://mste.illinois.edu/users/carvell/PlotPoints/PlotPoints.txt

public class LinearRegressionMeans {
	
	public static double[] regressionLine(List<Point2D> points) {
		
		double[] X = new double[points.size()];
		double[] Y = new double[points.size()];
		
		for (int i=0; i<points.size(); i++) {
			Point2D point = points.get(i);
			X[i] = point.getX();
			Y[i] = point.getY();
			
			if (Y[i] == 0) {
				throw new IllegalStateException();
			}
		}
		
		return regressionLine(X, Y);
	}
	
	public static double[] regressionLine(double[] xVals, double[] yVals){
		
		int pointCount = xVals.length;
		
		double sumXs = 0, sumYs = 0;
		double meanXs = 0, meanYs = 0;

		for (int i = 0; i < pointCount; i++) {
			sumXs += xVals[i];
			sumYs += yVals[i];
		}

		meanXs = sumXs/pointCount;
		meanYs = sumYs/pointCount;

		double sumDiffXsTimesdiffYs = 0;
		double sumDiffXsSquared = 0;
		for (int i = 0; i < pointCount; i++)
		{
			sumDiffXsTimesdiffYs += (xVals[i] - meanXs)*(yVals[i] - meanYs);
			sumDiffXsSquared += (xVals[i] - meanXs)*(xVals[i] - meanXs);
		}		

		if (sumDiffXsSquared == 0) {
			throw new IllegalStateException("sumDiffXsSquared=" + sumDiffXsSquared);
		}
		
		double slope = sumDiffXsTimesdiffYs / sumDiffXsSquared;
		double yIntercept = meanYs - slope * meanXs;
		
		/*
		 double xIntercept = -1*yIntercept/slope;	
		
		//double x1 = meanXs;//0;
		//double y1 = meanYs;//yIntercept;
		//double x2 = meanXs + 1;//xIntercept;
		//double y2 = meanYs + slope;//0;
		
		//y = y1 + [(y2 - y1) / (x2 - x1)]·(x - x1)
		//y = [(y2 - y1) / (x2 - x1)]·* x + y1 - x1 * [(y2 - y1) / (x2 - x1)]
		
		double a = (y2 - y1) / (x2 - x1);
		double b = y1 - x1 * ((y2 - y1) / (x2 - x1));
		*/
		
		return new double[] {yIntercept, slope};
	}	

}
