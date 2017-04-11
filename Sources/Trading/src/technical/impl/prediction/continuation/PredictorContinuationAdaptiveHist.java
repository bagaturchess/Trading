package technical.impl.prediction.continuation;


import technical.api.IPredictor;
import technical.impl.utils.StringUtils;


public class PredictorContinuationAdaptiveHist implements IPredictor {
	
	
	private int patternLength;
	private double historyPercent;
	private double deltaConfidence;
	private PatternsAdaptiveHist patterns;
	
	
	public PredictorContinuationAdaptiveHist(int _patternLength, double _historyPercent, double _deltaConfidence) {
		
		patternLength = _patternLength;
		historyPercent = _historyPercent;
		deltaConfidence = _deltaConfidence;
		
		patterns = new PatternsAdaptiveHist(patternLength, historyPercent);
		
		/*double[] sequence = GlobalConstants.eur2cad;
		for (int i=0; i<sequence.length - count - 1; i++) {
			double[] rates_old = new double[count];
			System.arraycopy(sequence, i, rates_old, 0, count);
			double[] rates_new = new double[count];
			System.arraycopy(sequence, i + 1, rates_new, 0, count);
			
			patterns.add(rates_old, rates_new);
		}*/
	}
	
	@Override
	public String getName() {
		return "ContinuationAdaptiveHist" + patternLength + "/" + StringUtils.align(historyPercent) + "/" + StringUtils.align(deltaConfidence);
	}
	
	public Double next(double[] sequence) {
		
		double[] rates_new = new double[patternLength];
		System.arraycopy(sequence, sequence.length - patternLength, rates_new, 0, patternLength);
		//double[] rates_old = new double[patternLength];
		//System.arraycopy(sequence, sequence.length - patternLength - 1, rates_old, 0, patternLength);
		
		patterns.add(rates_new);
		
		Double upProb = patterns.getNextUpProb(rates_new);
		
		if (upProb == null) {
			return null;
		}
		
		//System.out.println(upProb);
		if (upProb > 0.5 + deltaConfidence) {
			return upProb;
		} else if (upProb < 0.5 - deltaConfidence) {
			return upProb;
		}
		
		return null;
	}
}
