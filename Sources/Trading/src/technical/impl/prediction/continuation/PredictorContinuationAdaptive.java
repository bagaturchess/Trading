package technical.impl.prediction.continuation;


import technical.api.IPredictor;
import technical.impl.utils.StringUtils;


public class PredictorContinuationAdaptive implements IPredictor {
	
	
	private double deltaConfidence;
	private int patternLength;
	private PatternsAdaptive patterns;
	
	
	public PredictorContinuationAdaptive(double _deltaConfidence, int _patternLength) {
		
		deltaConfidence = _deltaConfidence;
		patternLength = _patternLength;
		patterns = new PatternsAdaptive(patternLength);
	}
	
	@Override
	public String getName() {
		return "ContinuationAdaptive" + patternLength + "/" + StringUtils.align(deltaConfidence);
	}
	
	public Double next(double[] sequence) {
		
		double[] rates_new = new double[patternLength];
		System.arraycopy(sequence, sequence.length - patternLength, rates_new, 0, patternLength);
		
		patterns.add(rates_new);
		
		Double upProb = patterns.getNextUpProb(rates_new);
		
		if (upProb == null) {
			return null;
		}
		
		if (upProb > 0.5 + deltaConfidence) {
			return upProb;
		} else if (upProb < 0.5 - deltaConfidence) {
			return upProb;
		}
		
		return null;
	}
}
