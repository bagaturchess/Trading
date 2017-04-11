package business.z_old1.impl.players.position;


import java.util.ArrayList;
import java.util.List;

import technical.impl.utils.SequenceUtils;
import business.api.IAccount;
import business.z_old1.api.IBroker;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_OpenClose_ManyPos extends Player_BuySellImpl<OpenCloseMetainf> {
	
	
	private double[] sequence;
	
	private int avgLength;
	private double[] avgs;
	
	private List<Position> lose_positions;
	private List<Position> win_positions;
	
	private List<Position> opened;
	
	private double riskMargin = 0.0001;
	private double spreadMargin = 0.0002;
	
	public Player_OpenClose_ManyPos(String _name, int toCurrencyType, IBroker _broker, int _avgLength) {
		super(_name + _avgLength, toCurrencyType, _broker, -1);
		
		if (_avgLength < 1) {
			throw new IllegalStateException("avgLength=" + avgLength);
		}
		
		avgLength = _avgLength;
	}
	
	
	public void addNewRate(IAccount account, int order, double rate) { 
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (order < avgLength) {
			return;
		}
		
		avgs = SequenceUtils.buildAVGSequence(sequence, avgLength);

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
		
		int lastPos = avgs.length;

		//Decision
		if (avgs[lastPos - 1] < avgs[lastPos - 2] && avgs[lastPos - 2] > avgs[lastPos - 3]) {
			//Buy
			Position open = new Position(1, getBroker().getRate(account.getCurrencyType(), getToCurrencyType()));
			opened.add(open);
		} else if (avgs[lastPos - 1] > avgs[lastPos - 2] && avgs[lastPos - 2] < avgs[lastPos - 3]) {
			//Sell
		} else {
			//up or down or side shift
		}
	}
	
	@Override
	public OpenCloseMetainf getMetaInf() {
		return new OpenCloseMetainf(lose_positions, win_positions);
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		sequence = new double[0];
		avgs = null;
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
