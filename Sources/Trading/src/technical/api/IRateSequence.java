package technical.api;

public interface IRateSequence {
	public IRate[] getSequence();
	public double getSequenceStability();
	public ITrend getBottomsTrend();
	public ITrend getPeaksTrend();
}
