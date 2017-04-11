package business.api;


public interface ICurrencyPairMetadata {

	public int getFromCurrencyType();
	public int getToCurrencyType();
	public double getSpread(); //runtime info: http://www.deltastock.com/bulgaria/client/spread.asp
	public double[] getRatesData();
	
	public double getMeaningfulIntervalLength_Min();
	public double getMeaningfulIntervalLength_Max();
	public double getMeaningfulIntervalLength_Avg();
	public double getMeaningfulIntervalLength_Stdev();
	
}
