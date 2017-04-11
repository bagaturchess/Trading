package technical.run;


import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.Trend;
import technical.impl.math.VarStatistic;


public class AnalysisMain {
	
	
	public static void main(String[] args) {
		
		
		double[] input_org = HistoricalCurrenciesPairs.eur2usd;
		
		/*
		VarStatistic stat = new VarStatistic(false);
		int min_size = 3;
		int toindex = 300;//input_org.length;
		
		int ok = 0;
		int all = 0;
		
		for (int to = min_size - 1; to < toindex; to++) {
			double optimal = 10000000;
			//System.out.println("to=" + to);
			for (int from = min_size - 1; from <= to; from++) {
				double[] input = new double[from];
				//System.out.println("input=" + input.length);
				//System.out.println("to=" + to + ", from=" + from + ", L=" + input.length);
				System.arraycopy(input_org, to - from, input, 0, input.length);
				Trend trend = new Trend(input);
				
				int lastindex = input.length - 1;
				double up = trend.getTrendResistence().calculate(lastindex);
				double mid = trend.getTrendMiddle().calculate(lastindex);
				double down = trend.getTrendSupport().calculate(lastindex);
				
				//System.out.println("up" + up + ", mid=" + mid + ", down=" + down);
				
				if (up > mid && mid > down) {
					double countermeasure = (up - mid) / (mid - down); //Balance function - when close to 1 is balanced
					if (Math.abs(1 - countermeasure) < optimal) {
						optimal = Math.abs(1 - countermeasure);
					}
					ok++;	
				}
				all++;
			}
			
			if (optimal != 10000000) {
				stat.addValue(optimal, optimal);
			}
		}
		
		System.out.println("ok=" + ok + ", all=" + all);
		System.out.println(stat);*/
		
		test(input_org);
	}
	
	private static void test(double[] input_org) {
		
		//int MARK = 1000000000;
		
		int OK_RATE = 10;
		
		int min_size = 4;
		int max_size = 100;
		
		for (int size = min_size; size <= max_size; size++) {
			
			//double optimal = MARK;
			int ok = 0;
			int all = 0;
			VarStatistic stat = new VarStatistic(false);
			
			for (int from = 0; from < input_org.length - size; from++) {
				double[] input = new double[size];
				System.arraycopy(input_org, from, input, 0, input.length);
				
				Trend trend = new Trend(input);
				
				int lastindex = input.length - 1;
				double up = trend.getTrendResistence().calculate(lastindex);
				double mid = trend.getTrendMiddle().calculate(lastindex);
				double down = trend.getTrendSupport().calculate(lastindex);
				
				if (up > mid && mid > down) {
					double countermeasure = (up - mid) / (mid - down); //Balance function - when close to 1 is balanced
					
					if (countermeasure > 1/OK_RATE && countermeasure < OK_RATE) {
						stat.addValue(countermeasure, countermeasure);
						ok++;
					}
					
					/*if (Math.abs(1 - countermeasure) < optimal) {
						optimal = Math.abs(1 - countermeasure);
					}*/
				}
				all++;
			}
			
			/*if (optimal != MARK) {
				stat.addValue(optimal, optimal);
			}*/
			
			System.out.println(size + "	" + all + "	" + ok + "	" + stat);
		}
	}
}
