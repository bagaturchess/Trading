package technical.impl.prediction.average;

import technical.api.IPredictor;
import technical.impl.utils.SequenceUtils;

public class PredictorAVG implements IPredictor {
	
	private int avgCount;
	
	public PredictorAVG(int _avgCount) {
		avgCount = _avgCount;
	}
	
	public Double next(double[] sequence) {
		double[] avg_sequence = SequenceUtils.buildAVGSequence(sequence, avgCount);
		
		int lastIdx = avg_sequence.length - 1;
		double deltaRate = avg_sequence[lastIdx] - avg_sequence[lastIdx - 1];
		
		return (deltaRate > 0 ? 1D : 0D);
	}

	@Override
	public String getName() {
		return "PredictorAVG";
	}
}
