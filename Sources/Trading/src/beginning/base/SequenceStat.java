package beginning.base;


import java.util.ArrayList;
import java.util.List;


//y = y1 + [(y2 - y1) / (x2 - x1)]·(x - x1)

public class SequenceStat {
	
	private static final double PROP_TREND_SKIPPED_POINTS_PERCENT = 0.05;
	
	private Sequence sequence;
	
	private Rate[] peaks;
	private Rate[] bottoms;
	
	private double bottomsTrend;
	private double peaksTrend;
	
	private Rate upkeepLine_p1;
	private Rate upkeepLine_p2;
	private Rate downkeepLine_p1;
	private Rate downkeepLine_p2;
	
	
	public SequenceStat(Sequence _sequence) {
		
		sequence = _sequence;
		
		buildPeaksAndBottoms();
		buildPeaksTrend();
		buildBottomsTrend();
		buildDownKeeperLine();
		buildUpKeeperLine();
	}
	
	
	public Rate[] getPeaks() {
		return peaks;
	}
	
	
	public Rate[] getBottoms() {
		return bottoms;
	}
	
	
	public double getBottomsTrend() {
		return bottomsTrend;
	}
	
	
	public double getPeaksTrend() {
		return peaksTrend;
	}
	
	
	public double getBottomsTrend_NORM() {
		return bottomsTrend * sequence.getValues().length;//TODO: keep max rate and delete this with it
	}
	
	
	public double getPeaksTrend_NORM() {
		return peaksTrend * sequence.getValues().length;//TODO: keep max rate and delete this with it
	}
	
	/*public double getDistanceToUpkeepLine() {
		
		double id = sequence.getValues()[sequence.getValues().length - 1].getID();
		double rate = sequence.getValues()[sequence.getValues().length - 1].getValue();
		
		double x1 = upkeepLine_p1.getID();
		double y1 = upkeepLine_p1.getValue();
		double x2 = upkeepLine_p2.getID();
		double y2 = upkeepLine_p2.getValue();
		
		double y = y1 + ((y2 - y1) / (x2 - x1)) * (id - x1);
		
		return rate - y;
	}
	
	
	public double getDistanceToDownkeepLine() {
		
		double id = sequence.getValues()[sequence.getValues().length - 1].getID();
		double rate = sequence.getValues()[sequence.getValues().length - 1].getValue();
		
		double x1 = downkeepLine_p1.getID();
		double y1 = downkeepLine_p1.getValue();
		double x2 = downkeepLine_p2.getID();
		double y2 = downkeepLine_p2.getValue();
		
		double y = y1 + ((y2 - y1) / (x2 - x1)) * (id - x1);
		
		return y - rate;
	}*/
	
	
	public double getDistanceToKeeprs() {
		
		double id = sequence.getValues()[sequence.getValues().length - 1].getID();
		double rate = sequence.getValues()[sequence.getValues().length - 1].getValue();
		
		double x1_up = upkeepLine_p1.getID();
		double y1_up = upkeepLine_p1.getValue();
		double x2_up = upkeepLine_p2.getID();
		double y2_up = upkeepLine_p2.getValue();
		
		double y_up = y1_up + ((y2_up - y1_up) / (x2_up - x1_up)) * (id - x1_up);
		
		double x1_down = downkeepLine_p1.getID();
		double y1_down = downkeepLine_p1.getValue();
		double x2_down = downkeepLine_p2.getID();
		double y2_down = downkeepLine_p2.getValue();
		
		double y_down = y1_down + ((y2_down - y1_down) / (x2_down - x1_down)) * (id - x1_down);
		
		double deltaY = y_down - y_up;
		
		if (deltaY <= 0) {
			return Double.NaN;
		}
		
		double zeroPoint = y_up + deltaY / (double) 2;
		
		return (rate - zeroPoint) / (deltaY / (double) 2);
	}
	
	
	public double getDistanceBetweenKeepLines() {
		
		double id = sequence.getValues()[sequence.getValues().length - 1].getID();
		
		double x1_up = upkeepLine_p1.getID();
		double y1_up = upkeepLine_p1.getValue();
		double x2_up = upkeepLine_p2.getID();
		double y2_up = upkeepLine_p2.getValue();
		
		double y_up = y1_up + ((y2_up - y1_up) / (x2_up - x1_up)) * (id - x1_up);
		
		double x1_down = downkeepLine_p1.getID();
		double y1_down = downkeepLine_p1.getValue();
		double x2_down = downkeepLine_p2.getID();
		double y2_down = downkeepLine_p2.getValue();
		
		double y_down = y1_down + ((y2_down - y1_down) / (x2_down - x1_down)) * (id - x1_down);
		
		return y_down - y_up;
	}
	
	
	public void dumpUpkeepLine() {
		
		
		double x1 = upkeepLine_p1.getID();
		double y1 = upkeepLine_p1.getValue();
		double x2 = upkeepLine_p2.getID();
		double y2 = upkeepLine_p2.getValue();
		
		for (int i=0; i<sequence.getValues().length; i++) {
			double y = y1 + ((y2 - y1) / (x2 - x1)) * (i - x1);
			System.out.println(y);
		}
	}
	
	
	public void dumpDownkeepLine() {
		
		
		double x1 = downkeepLine_p1.getID();
		double y1 = downkeepLine_p1.getValue();
		double x2 = downkeepLine_p2.getID();
		double y2 = downkeepLine_p2.getValue();
		
		for (int i=0; i<sequence.getValues().length; i++) {
			double y = y1 + ((y2 - y1) / (x2 - x1)) * (i - x1);
			System.out.println(y);
		}
	}
	
	
	@Override
	public String toString() {
		String result = "";
		
		result += "\r\n";
		result += "Peaks: ";
		for (int i=0; i<peaks.length; i++) {
			if (i != 0) result += ", ";
			result += peaks[i];
		}
		
		result += "\r\n";
		result += "Bottoms: ";
		for (int i=0; i<bottoms.length; i++) {
			if (i != 0) result += ", ";
			result += bottoms[i];
		}
		
		result += "\r\n";
		result += "Bottoms trend: " + bottomsTrend;
		
		result += "\r\n";
		result += "Peaks trend: " + peaksTrend;
		
		return result;
	}
	
	
	private void buildPeaksAndBottoms() {
		
		Rate[] values = sequence.getValues();
		
		if (values.length < 2) {
			throw new IllegalStateException("Less than 2 values in a sequence");
		}
		
		List<Rate> peaksList = new ArrayList<Rate>();
		List<Rate> bottomsList = new ArrayList<Rate>();
		
		//TODO: Check the last and first values. Whether they are up of the first bottom or down of the last peak.
		//Skips the first and the last peak (peak or bottom)
		int trend = 0;
		double old = values[0].getValue();
		for (int i=1; i<values.length; i++) {
			
			double cur = values[i].getValue();
			
			//At least second iteration
			if (cur > old) {
				if (trend != 1) {
					trend = 1;
					bottomsList.add(new Rate(i - 1, old));
				}
			} else if (cur < old) {
				if (trend != -1) {
					trend = -1;
					peaksList.add(new Rate(i - 1, old));
				}				
			} else {
				//Do nothing
			}
			
			if (i == values.length - 1) { //Last iteration
				if (trend == 1) {
					bottomsList.add(new Rate(i, cur));
				} else if (trend == -1) {
					peaksList.add(new Rate(i, cur));
				}
			}
			
			old = cur;
		}

		Rate[] peaksArray = peaksList.toArray(new Rate[peaksList.size()]);
		peaks = new Rate[peaksArray.length];
		for (int i=0; i<peaks.length; i++) {
			peaks[i] = peaksArray[i];
		}

		Rate[] bottomsArray = bottomsList.toArray(new Rate[bottomsList.size()]);
		bottoms = new Rate[bottomsArray.length];
		for (int i=0; i<bottoms.length; i++) {
			bottoms[i] = bottomsArray[i];
		}
	}
	
	
	private void buildPeaksTrend() {
		Rate[] points = buildTrend(peaks, 0, true);
		peaksTrend = (points[1].getValue() - points[0].getValue()) / (points[1].getID() - points[0].getID());
	}
	
	
	private void buildBottomsTrend() {
		Rate[] points = buildTrend(bottoms, 0, false);		
		bottomsTrend = (points[1].getValue() - points[0].getValue()) / (points[1].getID() - points[0].getID());
	}
	
	
	private void buildDownKeeperLine() {
		Rate[] points = buildTrend(peaks, PROP_TREND_SKIPPED_POINTS_PERCENT, true);		
		downkeepLine_p1 = points[0];
		downkeepLine_p2 = points[1];
	}
	
	
	private void buildUpKeeperLine() {
		Rate[] points = buildTrend(bottoms, PROP_TREND_SKIPPED_POINTS_PERCENT, false);
		upkeepLine_p1 = points[0];
		upkeepLine_p2 = points[1];
	}
	
	
	private Rate[] buildTrend(Rate[] rates, double skipedPointsPercent, boolean startWithMaxPoint) {

		if (skipedPointsPercent < 0) {
			throw new IllegalStateException("skipedPointsPercent=" + skipedPointsPercent);
		}

		if (skipedPointsPercent >= 1) {
			throw new IllegalStateException("skipedPointsPercent=" + skipedPointsPercent);
		}
		
		if (rates.length < 2) {
			throw new IllegalStateException("Less than 2 rates in a sequence");
		}
		
		boolean[] skiped = new boolean[rates.length];
		int skippedPointsCount = 0;
		
		int firstPointIdx = -1;
		//int secondPointIdx = -1;
		Rate firstPoint = null;
		Rate secondPoint = null;
		
		do {
			//Find first point (the one with the max rate)
			firstPoint = null;
			for (int i=0; i<rates.length; i++) {
				
				if (skiped[i]) continue;
				
				if (firstPoint == null) {
					firstPoint = rates[i];
					firstPointIdx = i;
				} else {
					double curValue = rates[i].getValue();
					if (startWithMaxPoint) {
						if (curValue > firstPoint.getValue()) {
							firstPoint = rates[i];
							firstPointIdx = i;
						}
					} else {
						if (curValue < firstPoint.getValue()) {
							firstPoint = rates[i];
							firstPointIdx = i;
						}
					}
				}
			}
			
			skiped[firstPointIdx] = true;
			
			skippedPointsCount += 1;
		} while(((double)skippedPointsCount / ((double) rates.length)) <= skipedPointsPercent);
		

		//Find second point (the min 'math.tan')
		secondPoint = null;
		double minTan = Double.MAX_VALUE;
		for (int i=0; i<rates.length; i++) {
			
			if (skiped[i]) continue;
			
			if (i == firstPointIdx) {
				continue;
			}
			
			double x2 = rates[i].getID();
			double y2 = rates[i].getValue();
			double curDeltaX = x2 - firstPoint.getID();
			double curDeltaY = y2 - firstPoint.getValue();
			double curTanAlpha = curDeltaY / curDeltaX;
			
			if (secondPoint == null) {
				secondPoint = rates[i];
				minTan = curTanAlpha;
				//secondPointIdx = i;
			} else {
				if (curTanAlpha < minTan) {
					secondPoint = rates[i];
					minTan = curTanAlpha;
					//secondPointIdx = i;
				}
			}
		}
		
		//Switch points if necessary
		if (firstPoint.getID() > secondPoint.getID()) {
			Rate tmp = firstPoint;
			firstPoint = secondPoint;
			secondPoint = tmp;
			
			//int tmp1 = firstPointIdx;
			//firstPointIdx = secondPointIdx;
			//secondPointIdx = tmp1;
			
			minTan = -minTan;
		}
		
		if (firstPoint.getID() >= secondPoint.getID()) {
			throw new IllegalStateException();
		}
		
		return new Rate[] {firstPoint, secondPoint};
	}
}
