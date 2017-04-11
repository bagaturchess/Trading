package business.impl.players;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.Trend;
import technical.impl.utils.SequenceUtils;

import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_Trending extends Player_BaseImpl {
	
	
	private int trendLength = 7;
	private double[] sequence;
	private List<Trend> trends;
	
	
	public Player_Trending(Object[] args) {
		super(new Object[] {Player_Trending.class.getName(), args[0], args[1], args[2]});
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
		
		if (trends.size() == 0) {
			trends.add(cur_trend);
			return;
		}
		
		Trend last_trend = trends.get(trends.size() - 1);
		trends.add(cur_trend);
		
		if (last_trend.getTrendResistence().getCoeffA() > 0 && cur_trend.getTrendResistence().getCoeffA() < 0
				//&& last_trend.getTrendSupport().getCoeffA() > 0 && cur_trend.getTrendSupport().getCoeffA() < 0
				) {
			//System.out.println("open");
			openPosition(1, 0.0005);
		}
		
		if (//last_trend.getTrendResistence().getCoeffA() < 0 && cur_trend.getTrendResistence().getCoeffA() > 0 &&
				last_trend.getTrendSupport().getCoeffA() < 0 && cur_trend.getTrendSupport().getCoeffA() > 0
				) {
			List<Position> openedPositions = getOpenedPositions();
			for (Position cur: openedPositions) {
				double rate_open = cur.getRateOpen();
				double spread = cur.getSpread();
				double current_rate = getRate();
				
				if (current_rate - rate_open > spread) {
					addPositionForClosing(cur);
				}
			}
		}
		
		closePositions();
	}
}
