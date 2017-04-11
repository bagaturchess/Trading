package business.z_old1.impl.players.position;


import java.util.ArrayList;
import java.util.List;

import technical.api.IPredictor;
import technical.impl.prediction.deltarate.PredictorDeltaRates;
import technical.impl.prediction.random.Predictor50;
import technical.impl.utils.SequenceUtils;
import business.api.IAccount;
import business.z_old1.api.IBroker;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_OpenClose_Prediction extends Player_BuySellImpl<OpenCloseMetainf> {
	
	
	private double[] sequence;
	
	private List<Position> lose_positions;
	private List<Position> win_positions;
	
	private List<Position> opened;
	
	private double riskMargin = 0.0001;
	private double spreadMargin = 0.0002;
	
	
	//private IPredictor predictor = new PredictorContinuationAdaptiveHist(5, 0.49, 0);
	//private IPredictor predictor = new PredictorContinuationAdaptiveHist(2, 0, 0);
	private IPredictor predictor = new PredictorDeltaRates();
	//private IPredictor predictor = new PredictorTrending(26, 1);
	//private IPredictor predictor = new PredictorTrending(11, 7);
	//private IPredictor predictor = new Predictor50();
	
	
	public Player_OpenClose_Prediction(String _name, int toCurrencyType, IBroker _broker) {
		super(_name, toCurrencyType, _broker, -1);
	}
	
	
	public void addNewRate(IAccount account, int order, double rate) { 
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (sequence.length < 27) {
			return;
		}
		
		Double go_up_percent = predictor.next(sequence);
		
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
				} /*else if (cur_win == spreadMargin) {
					//wait
				}*/ else if (cur_win < 0 && Math.abs(cur_win) > riskMargin) {
					cur_open = opened.remove(i);
					i--;
					lose_positions.add(cur_open);
				} else {
					/*if (go_up_percent != null) {
						if (go_up_percent > 0.5) {
							cur_open = opened.remove(i);
							i--;
							lose_positions.add(cur_open);
						}
					}*/
				}
			}
		}
		
		//Decision
		if (go_up_percent != null) {
			//go_up_percent = Math.random();
			//if (go_up_percent < 0.5) {
				//Buy
				Position open = new Position((1 - go_up_percent) * 10, getBroker().getRate(account.getCurrencyType(), getToCurrencyType()));
				opened.add(open);
			//}
		}
	}
	
	@Override
	public OpenCloseMetainf getMetaInf() {
		return new OpenCloseMetainf(lose_positions, win_positions);
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		sequence = new double[0];
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
