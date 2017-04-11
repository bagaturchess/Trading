package technical.run;


import java.awt.Color;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.Trend;
import technical.impl.visual.VisualSequence;


public class UIMain {
	
	
	public static void main(String[] args) {
		
		int start_pos = 0;
		int size = 3164;
		
		double[] input_org = HistoricalCurrenciesPairs.eur2usd;
		
		double[] input = new double[size];
		System.arraycopy(input_org, start_pos, input, 0, input.length);
		
		Trend trend = new Trend(input);
		
		//Visualize
		VisualSequence visual = new VisualSequence(1);
		
		visual.addLine(input, new Color(10, 10, 10), true);
		visual.addLine(trend.getChullUp(), new Color(10, 10, 10), true);
		visual.addLine(trend.getCHullDown(), new Color(10, 10, 10), true);
		
		visual.addLine(trend.getTrendResistence().get2Points(), new Color(255, 0, 0), true);
		visual.addLine(trend.getTrendMiddle().get2Points(), new Color(0, 255, 0), true);
		visual.addLine(trend.getTrendSupport().get2Points(), new Color(0, 0, 255), true);
		
		visual.visualize();
	}
}
