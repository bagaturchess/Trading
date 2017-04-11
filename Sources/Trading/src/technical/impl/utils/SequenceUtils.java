package technical.impl.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.Point2D;
import technical.impl.math.VarStatistic;
import business.api.CurrencyPairMetadataFactory;
import business.api.ICurrencyPairMetadata;


public class SequenceUtils {
	
	
	public static void main(String[] args) {
		//1.4232 1.4234 
		//dumpLengths(GlobalConstants.eur2usd, 0.0003);
		
		//double[] arr = new double[] {1,2,3,2,1,2,3,4,3,2,1};
		double[] arr = HistoricalCurrenciesPairs.eur2usd;
		
		//double[] avg = buildAVGSequence(arr, 1);
		//SequenceInfo stat = getPeriodStat(avg);
		//System.out.println(stat);
		
		/*for (int i=0; i<arr.length; i++) {
			System.out.println("GlobalConstants.eur2usd[" + i + "]=" + arr[i]);
		}*/
		
		/*double[] avg = buildAVGSequence(arr, 200);
		for (int i=0; i<avg.length; i++) {
			//System.out.println("AVG[" + i + "]=" + avg[i]);
			System.out.println(avg[i]);
		}*/
		
		/*double[] arr = new double[] {1,2,3,4};
		double[] extended = addRateToSequence(arr, 5);
		for (int i=0; i<extended.length; i++) {
			//System.out.println("AVG[" + i + "]=" + avg[i]);
			System.out.println(extended[i]);
		}*/
		
		for (int i=1; i<30; i++) {
			double[] avg = buildAVGSequence(arr, i);
			SequenceInfo stat = getPeriodStat(avg);
			System.out.println(i + " / " + stat);
		}
	}
		
	
	public static List<Point2D> genRandomPoints() {
		int size = 180;
		Random r = new Random();
		List<Point2D> array = new ArrayList<Point2D>(size);
		for (int i = 0; i < size; i++) {
			int x = r.nextInt(350) + 15;
			int y = r.nextInt(350) + 15;
			array.add(new Point2D(x, y));
		}
		return array;
	}
	
	public static List<Point2D> buildPointsList(double[] sequence) {
		List<Point2D> result = new ArrayList<Point2D>();
		
		for (int i=0 ;i<sequence.length; i++) {
			result.add(new Point2D(i, sequence[i]));
		} 
		
		return result;
	}
	
	public static void buildOrderList(double[] numbers, int[] order) {

		//Fill order array with zeros
		for (int i=0; i<order.length; i++) {
			order[i] = -1;
		}
		
		
		//Ordering
		int counter = 1;
		for (int p=0; p<numbers.length; p++) {
			
			//Find min rate
			double min_val = Double.MAX_VALUE;
			for (int i=0; i<numbers.length; i++) {
				if (order[i] == -1) {
					double cur = numbers[i];
					if (cur < min_val) {
						min_val = cur;
					}
				}
			}
			
			//Mark all with this rate
			for (int i=0; i<numbers.length; i++) {
				if (numbers[i] == min_val) {
					order[i] = counter;
				}
			}
			
			//Increase order counter
			counter++;
		}
	}
	
	public static int[] buildOrderList(double[] numbers) {
		int[] order = new int[numbers.length];
		buildOrderList(numbers, order);
		return order;
	}
	
	public static final String arrayToString(int[] array) {
		String result = "";
		for (int i=0; i<array.length; i++) {
			result += array[i] + ", ";
		}
		return result;
	}
	
	public static SequenceInfo getPeriodStat(double[] sequence) {
		VarStatistic periodLengthStat = new VarStatistic(false);
		VarStatistic periodRateDiffStat = new VarStatistic(false);
		
		int counter = 1;
		while (sequence[counter] == sequence[counter - 1]) {
			counter++;
		}
		int trend = (sequence[counter] - sequence[counter - 1] > 0) ? 1 : -1;
		
		int periodCounter = 0;
		double old = sequence[0];
		double oldPeak = old;
		for (int i=1; i<sequence.length; i++) {
			
			periodCounter++;
			
			double cur = sequence[i];
			
			//At least second iteration
			if (cur > old) {
				if (trend != 1) {
					//Bottom
					trend = 1;
					periodRateDiffStat.addValue(Math.abs(oldPeak - old), Math.abs(oldPeak - old));
					periodLengthStat.addValue(periodCounter - 1, periodCounter - 1);
					periodCounter = 1;
					oldPeak = old;
				}
			} else if (cur < old) {
				if (trend != -1) {
					//peak
					trend = -1;
					periodRateDiffStat.addValue(Math.abs(oldPeak - old), Math.abs(oldPeak - old));
					periodLengthStat.addValue(periodCounter - 1, periodCounter - 1);
					periodCounter = 1;
					oldPeak = old;
				}				
			} else {
				//Do nothing
			}
			
			if (i == sequence.length - 1) { //Last iteration
				if (trend == 1) {
					periodRateDiffStat.addValue(Math.abs(oldPeak - cur), Math.abs(oldPeak - cur));
					periodLengthStat.addValue(periodCounter, periodCounter);
				} else if (trend == -1) {
					periodRateDiffStat.addValue(Math.abs(oldPeak - cur), Math.abs(oldPeak - cur));
					periodLengthStat.addValue(periodCounter, periodCounter);
				}
			}
			
			old = cur;
		}
		
		return new SequenceInfo(periodLengthStat, periodRateDiffStat);
	}
	
	public static double dumpLengths(double[] sequence, double deltaRate) {
		
		ICurrencyPairMetadata meta = CurrencyPairMetadataFactory.create(0, 1, deltaRate, sequence);
		
		System.out.println(meta);
		
		return 0;
	}
	
	public static double[] addRateToSequence(double[] sequence, double rate) {
		if (sequence == null) sequence = new double[0];
		double[] result = new double[sequence.length + 1]; 
		System.arraycopy(sequence, 0, result, 0, sequence.length);
		result[result.length - 1] = rate;
		return result;
	}
	
	public static double[] buildDeltas(double[] sequence) {
		double[] result = new double[sequence.length];
		
		for (int i=1; i<sequence.length; i++) {
			result[i - 1] = sequence[i] - sequence[i - 1];
		}
		
		return result;
	}
	
	public static double[] buildAVGSequence(double[] sequence, int countForAVG) {
		
		if (countForAVG > sequence.length) {
			throw new IllegalStateException("countForAVG is bigger than the sequence length countForAVG=" + countForAVG);
		}
		
		double[] result = new double[sequence.length];
		double[] buff_avg = new double[countForAVG];
		
		for (int i=0; i<buff_avg.length; i++) {
			buff_avg[i] = sequence[i];
		}
		
		for (int i=buff_avg.length - 1; i<sequence.length - 1; i++) {
			
			//Calc avg
			VarStatistic stat = new VarStatistic(false);
			for (int j=0; j<buff_avg.length; j++) {
				stat.addValue(buff_avg[j], buff_avg[j]);
			}
			double curAvg = stat.getEntropy();
			result[i] = curAvg;
			
			//Shift and put new val
			for (int j=0; j<buff_avg.length-1; j++) {
				buff_avg[j] = buff_avg[j+1];
			}
			
			//Add new val
			double rate = sequence[i + 1];
			buff_avg[buff_avg.length-1] = rate;
		}
		
		//Calc last avg
		VarStatistic stat = new VarStatistic(false);
		for (int j=0; j<buff_avg.length; j++) {
			stat.addValue(buff_avg[j], buff_avg[j]);
		}
		double curAvg = stat.getEntropy();
		result[result.length - 1] = curAvg;
		
		return result;
	}
}
