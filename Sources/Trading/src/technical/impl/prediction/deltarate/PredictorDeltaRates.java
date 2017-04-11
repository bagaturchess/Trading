package technical.impl.prediction.deltarate;

import technical.api.IPredictor;

public class PredictorDeltaRates implements IPredictor {
	
	public PredictorDeltaRates() {
		
	}
	
	@Override
	public String getName() {
		return "PredictorDeltaRates";
	}
	
	public Double next(double[] sequence) {
		
		int lastIdx = sequence.length - 1;
		double lastDelta = sequence[lastIdx] - sequence[lastIdx - 1];
		
		if (lastDelta == 0) {
			return 0.5;
		}
		
		int sign = 0;
		if (lastDelta > 0) {
			sign = 1;
		} else {
			sign = -1;
		}
		
		double peak = 0;
		if (sign == 1) {
			
			peak = -100;
			
			int counter = lastIdx;
			while (sequence[counter] - sequence[counter-1] > 0) {
				if (sequence[counter] - sequence[counter-1] > peak) {
					peak = sequence[counter] - sequence[counter-1];
				}
				counter--;
			}
		} else {
			
			peak = 100;
			
			int counter = lastIdx;
			while (sequence[counter] - sequence[counter-1] < 0) {
				if (sequence[counter] - sequence[counter-1] < peak) {
					peak = sequence[counter] - sequence[counter-1];
				}
				counter--;
			}
		}
		
		double prob = 0;
		if (sign == 1) {
			if (lastDelta < 0 || peak < 0) {
				throw new IllegalStateException("peak=" + peak + ", lastDelta=" + lastDelta);
			}
			double percent = lastDelta / peak;
			if (percent > 1) {
				throw new IllegalStateException("percent=" + percent);
			}
			prob = percent;//0.5 + percent / 2;
		} else {
			if (lastDelta > 0 || peak > 0) {
				throw new IllegalStateException("peak=" + peak + ", lastDelta=" + lastDelta);
			}
			double percent = lastDelta / peak;
			if (percent > 1) {
				throw new IllegalStateException("percent=" + percent);
			}
			prob = 1 - percent;//0.5 - percent / 2;
		}
		
		/*if (prob == 0 || prob == 1) {
			return null;
		}*/
		
		//System.out.println(prob);
		
		return prob;
	}
}
