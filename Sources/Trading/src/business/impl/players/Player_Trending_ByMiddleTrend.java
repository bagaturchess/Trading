package business.impl.players;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.Trend;
import technical.impl.utils.SequenceUtils;

import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_Trending_ByMiddleTrend extends Player_BaseImpl {
	
	
	private int trendLength = 7;
	private double[] sequence;
	private List<Trend> trends;
	
	
	public Player_Trending_ByMiddleTrend(Object[] args) {
		super(new Object[] {Player_Trending_ByMiddleTrend.class.getName(), args[0], args[1], args[2]});
		trends = new ArrayList<Trend>();
	}
	
	
	@Override
	public void addNewRate(int order, double rate) {
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (order < trendLength) {
			return;
		}
		
		double[] trend_arr = new double[trendLength];
		System.arraycopy(sequence, sequence.length - trend_arr.length, trend_arr, 0, trend_arr.length);
		Trend cur_trend = new Trend(trend_arr);
		trends.add(cur_trend);
		
		int index = trendLength - 1;
		double middle = cur_trend.getTrendMiddle().calculate(index);
		
		if (rate > middle) {
			openPosition(1, 0.0005);
		}
		
		if (rate < middle) {
			List<Position> openedPositions = getOpenedPositions();
			for (Position cur: openedPositions) {
				addPositionForClosing(cur);
			}
		}
		
		closePositions();
	}
}
