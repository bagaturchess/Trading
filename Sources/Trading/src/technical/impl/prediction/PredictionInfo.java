package technical.impl.prediction;

public class PredictionInfo implements Comparable<PredictionInfo> {
	
	
	public String name;
	public int sum_prediction_count;
	public int sum_total_count;
	public double sum_mistake_percent;
	public int sequences_count;
	public double deviation;
	
	
	public PredictionInfo() {
		
	}
	
	
	private double getDeviation() {
		return deviation / sequences_count;
	}
	
	private double getMistakePercent() {
		return sum_mistake_percent / sequences_count;
	}
	
	private double getWinPercent() {
		return 1 - getMistakePercent();
	}
	
	private int getPredictionsCount() {
		return sum_prediction_count / sequences_count;
	}
	
	private int getTotalCount() {
		return sum_total_count / sequences_count;
	}
	
	private double winFactor() {
		double win_percent = getWinPercent();
		
		//if (win_percent > 0.5) {
			double win = win_percent - 0.5;
			win = win * (getPredictionsCount() / (double)getTotalCount());
			win = win / getDeviation();
			
			return win;
		//}
		
		//return win;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		result += name + "> " + getMistakePercent() + "	" + getPredictionsCount() + "	" + winFactor();
		
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PredictionInfo) {
			return name.equals(((PredictionInfo)other).name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public int compareTo(PredictionInfo other) {
		
		if (other == this) {
			return 0;
		}
		
		int win = (int) (10000 * (winFactor() - other.winFactor()));
		if (win != 0) {
			return (int) win;
		}
		
		int diff = (int) (10000 * (getMistakePercent() - other.getMistakePercent()));
		
		if (diff == 0) {
			return -1;
		}
		
		return diff;
		
		/*int result = (int) (1000 * (getMistakePercent() - other.getMistakePercent());
		
		if (result != 0) {
			return result;
		}
		
		return getPredictionsCount() - other.getPredictionsCount();*/
	}
}
