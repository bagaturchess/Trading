package business.z_old1.impl.players.position;


import java.util.ArrayList;
import java.util.List;

import technical.impl.utils.SequenceUtils;
import business.api.IAccount;
import business.z_old1.api.IBroker;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_OpenClose_ManyPos_2AVG extends Player_BuySellImpl<OpenCloseMetainf> {
	
	
	private double[] sequence;
	
	private int avgLength1;
	private double[] avgs1;

	private int avgLength2;
	private double[] avgs2;

	private List<Position> lose_positions;
	private List<Position> win_positions;
	
	private List<Position> opened;
	
	private double riskMargin = 0.0001;
	private double spreadMargin = 0.0002;
	
	
	public Player_OpenClose_ManyPos_2AVG(String _name, int toCurrencyType, IBroker _broker, int _avgLength1, int _avgLength2) {
		super(_name + "[" + _avgLength1 + ", " + _avgLength2 + "]", toCurrencyType, _broker, -1);
		
		if (_avgLength1 < 1) {
			throw new IllegalStateException("_avgLength1=" + _avgLength1);
		}
		
		if (_avgLength2 < 1) {
			throw new IllegalStateException("_avgLength2=" + _avgLength2);
		}
		
		if (_avgLength2 <= _avgLength1) {
			throw new IllegalStateException("_avgLength1=" + _avgLength1 + ", _avgLength2=" + _avgLength2);
		}
		
		avgLength1 = _avgLength1;
		avgLength2 = _avgLength2;
	}
	
	
	public void addNewRate(IAccount account, int order, double rate) { 
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (order < avgLength2) {
			return;
		}
		
		avgs1 = SequenceUtils.buildAVGSequence(sequence, avgLength1);
		avgs2 = SequenceUtils.buildAVGSequence(sequence, avgLength2);

		if (opened.size() > 0) { //has open positions
			double close_rate = getBroker().getRate(account.getCurrencyType(), getToCurrencyType());
			
			for (int i=0; i<opened.size(); i++) {
				Position cur_open = opened.get(i);
				double cur_win = cur_open.getOpenRate() - close_rate;
				if (cur_win > spreadMargin) {
					cur_open = opened.remove(i);
					i--;
					cur_open.close(close_rate);
					win_positions.add(cur_open);
				} else if (cur_win == spreadMargin) {
					//wait
				} else if (cur_win < 0 && Math.abs(cur_win) > riskMargin) {
					cur_open = opened.remove(i);
					i--;
					lose_positions.add(cur_open);
				}
			}
		}
		
		int lastPos = avgs1.length;
		
		//Decision
		if (avgs2[lastPos - 1] <= avgs2[lastPos - 2]) {
			if (avgs1[lastPos - 1] < avgs1[lastPos - 2] && avgs1[lastPos - 2] > avgs1[lastPos - 3]) {
				//Buy
				Position open = new Position(1, getBroker().getRate(account.getCurrencyType(), getToCurrencyType()));
				opened.add(open);
			}
		}
	}
	
	@Override
	public OpenCloseMetainf getMetaInf() {
		return new OpenCloseMetainf(lose_positions, win_positions);
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		sequence = new double[0];
		avgs1 = null;
		avgs2 = null;
		lose_positions = new ArrayList<Position>();
		win_positions = new ArrayList<Position>();
		opened = new ArrayList<Position>();
	}

	@Override
	public void endRateSequence(IAccount account) {
		
	}
	
	protected double getRiskPercent() {
		throw new UnsupportedOperationException();
	}
}
