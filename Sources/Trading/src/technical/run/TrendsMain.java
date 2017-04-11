package technical.run;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.Point2D;
import technical.impl.math.Trend;
import technical.impl.visual.VisualSequence;


public class TrendsMain {
	
	
	public static void main(String[] args) {
		
		
		double[] input_org = HistoricalCurrenciesPairs.eur2usd;
		int start = 2250;//2500
		int size = 500;//input_org.length;
		double[] input = new double[size];
		System.arraycopy(input_org, start, input, 0, input.length);
		
		int trendLength = 17;
		
		double[] support_coeffA_arr = new double[size];
		double[] middle_coeffA_arr = new double[size];
		double[] resistence_coeffA_arr = new double[size];
		double[] sr_sum = new double[size];
		double[] all_sum = new double[size];
		
		for (int i=0; i<size - trendLength; i++) {
			double[] trend_arr = new double[trendLength];
			System.arraycopy(input, i, trend_arr, 0, trend_arr.length);
			Trend trend = new Trend(trend_arr);
			
			int j = i + trendLength;
			support_coeffA_arr[j] = trend.getTrendSupport().getCoeffA();
			middle_coeffA_arr[j] = trend.getTrendMiddle().getCoeffA();
			resistence_coeffA_arr[j] = trend.getTrendResistence().getCoeffA();
			sr_sum[j] = resistence_coeffA_arr[j] + support_coeffA_arr[j];
			all_sum[j] = resistence_coeffA_arr[j] + support_coeffA_arr[j] + middle_coeffA_arr[j];
			/*if (i > 0) {
				support_coeffA_arr[j] -= support_coeffA_arr[j - 1];
				middle_coeffA_arr[j] -= middle_coeffA_arr[j - 1];
				resistence_coeffA_arr[j] -= resistence_coeffA_arr[j - 1];
				sr_diffs[j] -= sr_diffs[j - 1];
			}*/
		}
		
		
		double[] input_norm = new double[input.length];
		for (int i=0; i<input_norm.length; i++) {
			input_norm[i] = input[i] / 30;
		}
		
		List<Point2D> probs = new ArrayList<Point2D>();
		for (int i=trendLength; i<all_sum.length; i++) {
			double up_prob = getUpProbOfSinusoidalSignal(all_sum, i);
			if (up_prob < 0.02) {
				probs.add(new Point2D(i, all_sum[i]));
				continue;
			}
		}
		
		
		//Visualize
		VisualSequence visual_trends = new VisualSequence(1);
		//visual_trends.addLine(support_coeffA_arr, new Color(255, 0, 0), true);
		//visual_trends.addLine(middle_coeffA_arr, new Color(0, 255, 0), true);
		//visual_trends.addLine(resistence_coeffA_arr, new Color(0, 0, 255), true);
		//visual_trends.addLine(sr_sum, new Color(0, 255, 255), true);
		visual_trends.addLine(all_sum, new Color(255, 0, 255), true);
		visual_trends.addLine(input_norm, new Color(0, 0, 0), true);
		visual_trends.addLine(probs, new Color(0, 0, 255), true);
		
		visual_trends.visualize();
	}
	
	public static double getUpProbOfSinusoidalSignal(double[] deltas, int toIndex) {
		
		int lastIdx = toIndex;//deltas.length - 1;
		
		if ( deltas[lastIdx] == 0) {
			throw new UnsupportedOperationException("TODO: Implement");
		}
		
		double prob;
		if (deltas[lastIdx] > 0) {
			double peak = Double.MIN_VALUE;
			int counter = lastIdx;
			while (deltas[counter] > 0) {
				if (deltas[counter] > peak) {
					peak = deltas[counter];
				}
				counter--;
			}
			prob = deltas[lastIdx] / peak;
		} else if (deltas[lastIdx] < 0) {
			double peak = Double.MAX_VALUE;
			int counter = lastIdx;
			while (deltas[counter] < 0) {
				if (deltas[counter] < peak) {
					peak = deltas[counter];
				}
				counter--;
			}
			prob = 1 - (deltas[lastIdx] / peak);
		} else {
			throw new UnsupportedOperationException("TODO: Implement");
		}
		
		return prob;
	}

}
