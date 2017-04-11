package business.z_old1.impl.players.position;


import java.util.ArrayList;
import java.util.List;

import technical.impl.utils.SequenceUtils;
import business.api.IAccount;
import business.z_old1.api.IBroker;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_OpenClose_SinglePos extends Player_BuySellImpl<OpenCloseMetainf> {
	
	
	private double[] sequence;
	
	private int avgLength;
	private double[] avgs;
	
	private List<Position> all_positions;
	private List<Position> win_positions;
	
	
	public Player_OpenClose_SinglePos(String _name, int toCurrencyType, IBroker _broker, int _avgLength) {
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

		int lastPos = avgs.length;

		//Decision
		if (avgs[lastPos - 1] < avgs[lastPos - 2] && avgs[lastPos - 2] > avgs[lastPos - 3]) {
			//Buy
			all_positions.add(new Position(1, getBroker().getRate(account.getCurrencyType(), getToCurrencyType())));
		} else if (avgs[lastPos - 1] > avgs[lastPos - 2] && avgs[lastPos - 2] < avgs[lastPos - 3]) {
			//Sell
			if (all_positions.size() > 0) { //has open position
				double close_rate = getBroker().getRate(account.getCurrencyType(), getToCurrencyType());
				Position curPos = all_positions.get(all_positions.size() - 1);
				curPos.close(close_rate);
				
				if (close_rate < curPos.getOpenRate()) {
					win_positions.add(curPos);
				}
			}
		} else {
			//up or down or side shift
		}
	}
	
	@Override
	public OpenCloseMetainf getMetaInf() {
		return new OpenCloseMetainf(all_positions, win_positions);
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		sequence = new double[0];
		avgs = null;
		all_positions = new ArrayList<Position>();
		win_positions = new ArrayList<Position>();
	}

	@Override
	public void endRateSequence(IAccount account) {
		
	}
	
	protected double getRiskPercent() {
		throw new UnsupportedOperationException();
	}
}
