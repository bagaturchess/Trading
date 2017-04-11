package technical.impl.prediction;

import java.util.ArrayList;
import java.util.List;

import technical.api.IPredictor;
import technical.impl.prediction.continuation.PredictorContinuationAdaptive;
import technical.impl.prediction.continuation.PredictorContinuationAdaptiveHist;
import technical.impl.prediction.deltarate.PredictorDeltaRates;
import technical.impl.prediction.random.Predictor50;
import technical.impl.prediction.random.PredictorRandom;
import technical.impl.prediction.trending.PredictorTrending;

public class PredictorsGen {
	
	public static final IPredictor[] getPredictors_Adaptive() {
		List<IPredictor> list = new ArrayList<IPredictor>();
		for (int i=2; i<=8; i++) {
			for (double deltaConfidence = 0.0; deltaConfidence <= 0.45; deltaConfidence += 0.05) {
				IPredictor cur = new PredictorContinuationAdaptive(deltaConfidence, i);
				list.add(cur);
			}
		}
		return list.toArray(new IPredictor[0]);
	}
	
	public static final IPredictor[] getPredictors_AdaptiveWithHistory() {
		List<IPredictor> list = new ArrayList<IPredictor>();
		for (int i=2; i<=8; i++) {
			for (double historyPercent = 0.0; historyPercent <= 0.95; historyPercent += 0.05) {
				for (double deltaConfidence = 0.0; deltaConfidence <= 0.45; deltaConfidence += 0.05) {
					IPredictor cur = new PredictorContinuationAdaptiveHist(i, historyPercent, deltaConfidence);
					list.add(cur);
				}
			}
		}
		return list.toArray(new IPredictor[0]);
	}
	
	public static final IPredictor[] getPredictors_ContinuationAdaptiveHist_5_X_0() {
		List<IPredictor> list = new ArrayList<IPredictor>();
		for (int i=5; i<=5; i++) {
			for (double historyPercent = 0.0; historyPercent <= 0.99; historyPercent += 0.01) {
				for (double deltaConfidence = 0.0; deltaConfidence <= 0.0; deltaConfidence += 0.05) {
					IPredictor cur = new PredictorContinuationAdaptiveHist(i, historyPercent, deltaConfidence);
					list.add(cur);
				}
			}
		}
		return list.toArray(new IPredictor[0]);
	}
	
	public static final IPredictor[] getPredictors_LKG() {
		List<IPredictor> list = new ArrayList<IPredictor>();
		
		list.add(new Predictor50());
		list.add(new PredictorRandom());
		list.add(new PredictorDeltaRates());
		//list.add(new PredictorContinuationAdaptiveHist(5, 0.49, 0));
		//list.add(new PredictorTrending(11, 7));
		list.add(new PredictorContinuationAdaptiveHist(2, 0, 0));
		list.add(new PredictorTrending(26, 1));
		
		return list.toArray(new IPredictor[0]);
	}
	
	public static final IPredictor[] getPredictors_Trending() {
		List<IPredictor> list = new ArrayList<IPredictor>();
		for (int i=3; i<30; i+=1) {
			for (int j=1; j<10; j+=1) {
				IPredictor cur = new PredictorTrending(i, j);
				list.add(cur);
			}
		}
		return list.toArray(new IPredictor[0]);
	}
	
	
	IPredictor[] preds = new IPredictor[] {
			new Predictor50(),
			//new PredictorRandom(),
			//new PredictorAVG(1),
			//new PredictorAVG(2),
			//new PredictorAVG(4),
			//new PredictorAVG(8),
			//new PredictorAVG(10),
			new PredictorDeltaRates(),
			//new PredictorContinuation2(),
			//new PredictorContinuation3(),
			//new PredictorContinuation4(),
			//new PredictorContinuation5(),
			//new PredictorContinuationBase(),
			new PredictorContinuationAdaptive(0.27, 7),
			new PredictorContinuationAdaptiveHist(7, 0.30, 0.27),
		};
}
