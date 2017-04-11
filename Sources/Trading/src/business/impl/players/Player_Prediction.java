package business.impl.players;


import technical.api.IPredictor;
import technical.impl.prediction.continuation.PredictorContinuationAdaptiveHist;
import technical.impl.prediction.deltarate.PredictorDeltaRates;
import technical.impl.prediction.trending.PredictorTrending;
import technical.impl.utils.SequenceUtils;
import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_Prediction extends Player_BaseImpl {
	
	
	private double[] sequence;
	
	private double riskMargin = 0.0001;
	private double spreadMargin = 0.0002;
	
	
	//private IPredictor predictor = new PredictorContinuationAdaptiveHist(5, 0.49, 0);
	//private IPredictor predictor = new PredictorContinuationAdaptiveHist(2, 0, 0);
	//private IPredictor predictor = new PredictorDeltaRates();
	//private IPredictor predictor = new PredictorTrending(26, 1);
	private IPredictor predictor = new PredictorTrending(11, 7);
	//private IPredictor predictor = new Predictor50();
	
	
	public Player_Prediction(Object[] args) {
		super(new Object[] {Player_RandomBuy.class.getName(), args[0], args[1], args[2]});
	}
	
	@Override
	public void addNewRate(int order, double rate) { 
		
		sequence = SequenceUtils.addRateToSequence(sequence, rate);
		
		if (sequence.length < 27) {
			return;
		}
		
		Double go_up_percent = predictor.next(sequence);
		
		//Decision
		if (go_up_percent != null) {
			//go_up_percent = Math.random();
			//if (go_up_percent < 0.5) {
				//Buy
				openPosition(1 * (1 - go_up_percent), 0.0005);
			//}
		}
		
		if (getOpenedPositions().size() > 0) { //has open positions
			double close_rate = getRate();
			
			for (int i=0; i<getOpenedPositions().size(); i++) {
				Position cur_open = getOpenedPositions().get(i);
				double cur_win = cur_open.getRateOpen() - close_rate;
				if (cur_win > spreadMargin) {
					addPositionForClosing(cur_open);
				} /*else if (cur_win == spreadMargin) {
					//wait
				}*/ else if (cur_win < 0 && Math.abs(cur_win) > riskMargin) {
					addPositionForClosing(cur_open);
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
		
		closePositions();
	}
}
