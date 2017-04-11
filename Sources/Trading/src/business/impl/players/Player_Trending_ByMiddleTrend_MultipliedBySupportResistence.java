package business.impl.players;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.Trend;
import technical.impl.utils.SequenceUtils;

import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_Trending_ByMiddleTrend_MultipliedBySupportResistence extends Player_BaseImpl {
	
	
	private int trendLength = 8;
	private double[] sequence;
	private List<Trend> trends;
	
	
	public Player_Trending_ByMiddleTrend_MultipliedBySupportResistence(Object[] args) {
		super(new Object[] {Player_Trending_ByMiddleTrend_MultipliedBySupportResistence.class.getName(), args[0], args[1], args[2]});
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
		double support = cur_trend.getTrendSupport().calculate(index);
		double middle = cur_trend.getTrendMiddle().calculate(index);
		double resistence = cur_trend.getTrendResistence().calculate(index);
		
		//if (support < middle && middle < resistence) {
			//openPosition(1, 0.0005);
			if (rate > middle) {
				double multiplier = 0;
				double delta1 = rate - middle;
				if (rate >= resistence) {
					multiplier = 1;
				} else {
					double delta2 = resistence - rate;
					multiplier = (delta1 / (delta1 + delta2));
				}
				
				openPosition(getAmount(multiplier), 0.0005);
			}
		//}
		
		if (rate < middle) {
			List<Position> openedPositions = getOpenedPositions();
			for (Position cur: openedPositions) {
				
				double multiplier = 0;
				double delta1 = middle - rate;
				if (rate <= support) {
					multiplier = 1;
				} else {
					double delta2 = rate - support;
					multiplier = (delta1 / (delta1 + delta2));
				}
				
				if (getRate() / cur.getRateOpen() >= getAmount(multiplier)) {
					addPositionForClosing(cur);	
				}
			}
		}
		
		closePositions();
	}
	
	private double getAmount(double multiplier) {
		return 1 * multiplier;
	}
}
