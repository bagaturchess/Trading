package business.z_old1.impl.operational;

import business.api.CurrencyType;
import business.api.ICurrency;
import business.z_old1.api.IBroker;

public class Broker implements IBroker {

	private double[][] rateMatrix;
	
	
	public Broker() {
		rateMatrix = new double[CurrencyType.SIZE][CurrencyType.SIZE];
	}
	
	
	@Override
	public void updateBroker(int fromType, int toType, double rate) {
		rateMatrix[fromType][toType] = rate;
	}
	
	/*public ICurrency createCurrency(int currencyType, double amount) {
		return new Currency(currencyType, amount);
	}*/
	
	@Override
	public ICurrency convert(ICurrency from, int toType) {
		
		int fromType = from.getType();
		
		if (fromType == toType) {
			throw new IllegalStateException();
		}
		
		double amount = from.getAmount() * rateMatrix[fromType][toType];
		                     
		return new Currency(toType, amount);
	}


	@Override
	public double getRate(int fromType, int toType) {
		return rateMatrix[fromType][toType];
	}
}
