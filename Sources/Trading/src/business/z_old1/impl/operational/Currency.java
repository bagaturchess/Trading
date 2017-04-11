package business.z_old1.impl.operational;

import business.api.CurrencyType;
import business.api.ICurrency;


public class Currency implements ICurrency {
	
	private int type;
	private double amount;

	public Currency(int _type, double _amount) {
		type = _type;
		amount = _amount;
	}

	@Override
	public int getType() {
		return type;
	}
	
	@Override
	public double getAmount() {
		return amount;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += (long) amount + "" + CurrencyType.toString(type);
		return result;
	}
}
