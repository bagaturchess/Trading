package business.impl.players;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.Trend;
import technical.impl.utils.SequenceUtils;

import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_Trending_ByMiddleTrend_test1 extends Player_BaseImpl {
	
	
	private int shortTrendLength = 8;
	private int longTrendLength = 14;
	
	private double[] sequence;
	private List<Trend> trendsShort;
	private List<Trend> trendsLong;
	
	
	public Player_Trending_ByMiddleTrend_test1(Object[] args) {
		super(new Object[] {Player_Trending_ByMiddleTrend_test1.class.getName(), args[0], args[1], args[2]});
		trendsShort = new ArrayList<Trend>();
		trendsLong = new ArrayList<Trend>();
	}
	
	
	@Override
	public void addNewRate(int order, double rate) {
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (longTrendLength <= shortTrendLength) {
			throw new IllegalStateException();
		}
		
		if (order < longTrendLength) {
			return;
		}
		
		//Short trend
		double[] trend_short_arr = new double[shortTrendLength];
		System.arraycopy(sequence, sequence.length - trend_short_arr.length, trend_short_arr, 0, trend_short_arr.length);
		Trend cur_trend_short = new Trend(trend_short_arr);


		if (trendsShort.size() == 0) {
			trendsShort.add(cur_trend_short);
			return;
		}
		
		Trend last_trend_short = trendsShort.get(trendsShort.size() - 1);
		trendsShort.add(cur_trend_short);
		
		//long trend
		double[] trend_long_arr = new double[longTrendLength];
		System.arraycopy(sequence, sequence.length - trend_long_arr.length, trend_long_arr, 0, trend_long_arr.length);
		Trend cur_trend_long = new Trend(trend_long_arr);
		
		if (trendsLong.size() == 0) {
			trendsLong.add(cur_trend_long);
			return;
		}
		
		Trend last_trend_long = trendsLong.get(trendsLong.size() - 1);
		trendsLong.add(cur_trend_long);
		
		
		double deltaMidTrend_long = cur_trend_long.getTrendMiddle().getCoeffA() - last_trend_long.getTrendMiddle().getCoeffA();
		double deltaMidTrend_short = cur_trend_short.getTrendMiddle().getCoeffA() - last_trend_short.getTrendMiddle().getCoeffA();
		
		int index = shortTrendLength - 1;
		double support = cur_trend_short.getTrendSupport().calculate(index);
		double middle = cur_trend_short.getTrendMiddle().calculate(index);
		double resistence = cur_trend_short.getTrendResistence().calculate(index);
		
		if (deltaMidTrend_short > deltaMidTrend_long) {
			if (rate > middle) {
				openPosition(1, 0.0005);
			}
		}
		
		//if (deltaMidTrend_short < deltaMidTrend_long) {
			if (rate < middle) {
				List<Position> openedPositions = getOpenedPositions();
				for (Position cur: openedPositions) {
					addPositionForClosing(cur);
				}
			}
		//}
		
		closePositions();
	}
}
