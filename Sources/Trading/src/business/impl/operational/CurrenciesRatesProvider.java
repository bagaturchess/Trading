package business.impl.operational;


import business.api.CurrencyType;


//Universal currencies calculator: http://www.xe.com/ucc/
public class CurrenciesRatesProvider {
	
	
	private double[][] rateMatrix;
	private double[][] spreadMatrix;
	
	
	public CurrenciesRatesProvider() {
		rateMatrix = new double[CurrencyType.SIZE][CurrencyType.SIZE];
		spreadMatrix = new double[CurrencyType.SIZE][CurrencyType.SIZE];;
	}
	
	
	public void updateRate(int fromCurrencyType, int toCurrencyType, double rate) {
		if (rate <= 0) {
			throw new IllegalStateException("rate=" + rate);
		}
		
		rateMatrix[fromCurrencyType][toCurrencyType] = rate;
		rateMatrix[toCurrencyType][fromCurrencyType] = 1 / (double) rate;
	}
	
	
	public void updateSpread(int fromType, int toType, double spread) {
		if (spread <= 0) {
			throw new IllegalStateException("spread=" + spread);
		}
		
		spreadMatrix[fromType][toType] = spread;
		//TODO: implement
	}
	
	
	public double getRate(int fromType, int toType) {
		return rateMatrix[fromType][toType];
	}
	
	
	public double getSpread(int fromType, int toType) {
		return spreadMatrix[fromType][toType];
	}
}
