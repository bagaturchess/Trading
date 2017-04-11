package technical.api;

public interface IPredictor {
	public String getName();
	public Double next(double[] sequence);
}
