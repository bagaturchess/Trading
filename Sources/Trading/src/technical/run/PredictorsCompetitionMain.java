package technical.run;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import technical.api.IPredictor;
import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.VarStatistic;
import technical.impl.prediction.PredictionInfo;
import technical.impl.prediction.PredictorsGen;


public class PredictorsCompetitionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//IPredictor[] preds = PredictorsGen.getPredictors_Adaptive();
		//IPredictor[] preds = PredictorsGen.getPredictors_AdaptiveWithHistory();
		//IPredictor[] preds = PredictorsGen.getPredictors_ContinuationAdaptiveHist_5_X_0();
		IPredictor[] preds = PredictorsGen.getPredictors_LKG();
		//IPredictor[] preds = PredictorsGen.getPredictors_Trending();
		
		double[][] sequences = new double[][] {HistoricalCurrenciesPairs.eur2usd, HistoricalCurrenciesPairs.eur2cad};
		
		List<PredictionInfo> results = new ArrayList<PredictionInfo>();
		
		for (int pred=0; pred<preds.length; pred++) {
						
			IPredictor predictor = preds[pred];
			System.out.println(predictor.getName());
			
			PredictionInfo info = new PredictionInfo();
			info.name = predictor.getName();
			
			for (int seq=0; seq<sequences.length; seq++) {
				
				//preds = PredictorsGen.getPredictors_Adaptive();
				//preds = PredictorsGen.getPredictors_AdaptiveWithHistory();
				//preds = PredictorsGen.getPredictors_ContinuationAdaptiveHist_5_X_0();
				//preds = PredictorsGen.getPredictors_LKG();
				//preds = PredictorsGen.getPredictors_Trending();
				
				predictor = preds[pred];
				
				info.sequences_count++;
				testPredictor(predictor, info, sequences[seq]);
			}
			
			results.add(info);
			
			preds[pred] = null;
		}
		
		Collections.sort(results);
		
		System.out.println();
		System.out.println("**************************************************************************************************************");
		System.out.println();
		for (int i=0; i<results.size(); i++) {
			if (results.get(i).sum_prediction_count > 0) {
				System.out.println(results.get(i));
			}
		}
	}

	public static void testPredictor(IPredictor pred, PredictionInfo info, double[] sequence) {
		
		VarStatistic stat = new VarStatistic(false);
		double mistake = 0;
		
		int count_predictions = 0;
		int startIdx = 50;
		
		for (int i=startIdx; i<sequence.length; i++) {
			
			double cur = sequence[i];
			
			double actual = -1;
			if (cur > sequence[i-1]) {
				actual = 1;
			} else if (cur == sequence[i-1]) {
				actual = 0.5;
			} else {
				actual = 0;
			}
			
			double[] cur_sequence = new double[i];
			System.arraycopy(sequence, 0, cur_sequence, 0, cur_sequence.length);
			
			Double expected = pred.next(cur_sequence);
			if (expected != null) {
				count_predictions++;
				double cur_mistake = Math.abs(actual - expected);
				mistake += cur_mistake;
				stat.addValue(cur_mistake, cur_mistake);
			}
		}
		
		info.sum_total_count += (sequence.length - startIdx);
		info.deviation += stat.getSTDEV();
		
		if (count_predictions > 0) {
			info.sum_mistake_percent += (mistake / count_predictions);
		}
		info.sum_prediction_count += count_predictions;
	}
	
}
