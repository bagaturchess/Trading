package technical.impl.prediction.trending;


import technical.api.IPredictor;


public class PredictorTrending implements IPredictor {
	
	
	private int trendLength;
	private int groupsCount;
	private Trending trending;
	
	
	public PredictorTrending(int _trendLength, int _groupsCount) {
		trendLength = _trendLength;
		groupsCount = _groupsCount;
		trending = new Trending(groupsCount);
	}
	
	@Override
	public String getName() {
		return "PredictorTrending" + trendLength + "/" + groupsCount;
	}
	
	public Double next(double[] sequence) {
		
		double[] rates = new double[trendLength];
		System.arraycopy(sequence, sequence.length - trendLength, rates, 0, trendLength);
		
		trending.add(rates);
		
		return trending.getNextUpProb(rates);
	}
}
