package technical.impl.prediction.random;

import technical.api.IPredictor;

public class PredictorRandom implements IPredictor {
	
	public Double next(double[] sequence) { 
		return Math.random();
	}

	@Override
	public String getName() {
		return "PredictorRandom";
	}
}
