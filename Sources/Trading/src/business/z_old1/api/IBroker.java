package business.z_old1.api;

import business.api.ICurrency;

public interface IBroker {
	//public ICurrency createCurrency(int currencyType, double amount);
	public ICurrency convert(ICurrency from, int toType);
	public void updateBroker(int fromType, int toType, double rate);
	public double getRate(int fromType, int toType);
}
