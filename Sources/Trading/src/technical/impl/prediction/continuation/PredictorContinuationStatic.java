package technical.impl.prediction.continuation;

import technical.api.IPredictor;
import technical.impl.input.HistoricalCurrenciesPairs;

public class PredictorContinuationStatic implements IPredictor {
	
	int count = 7;
	
	PatternsStatic patterns = new PatternsStatic(HistoricalCurrenciesPairs.eur2cad, count);
	
	public PredictorContinuationStatic() {
		
	}
	
	public Double next(double[] sequence) {
		
		double[] arg = new double[count];
		System.arraycopy(sequence, sequence.length - count, arg, 0, count);
		
		double upProb = patterns.getNextUpProb(arg);
		
		return upProb;
	}

	@Override
	public String getName() {
		return "PredictorContinuationStatic" + "" + count;
	}
}
