package technical.impl.prediction.random;

import technical.api.IPredictor;

public class Predictor50 implements IPredictor {
	
	public Double next(double[] sequence) { 
		return 0.5;
	}

	@Override
	public String getName() {
		return "Predictor50";
	}
}
