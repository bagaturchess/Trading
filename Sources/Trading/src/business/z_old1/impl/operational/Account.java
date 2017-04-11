package business.z_old1.impl.operational;

import business.api.CurrencyType;
import business.api.IAccount;
import business.api.ICurrency;

public class Account implements IAccount {
	
	private int currencyType;
	private double initialAmount;
	private double balance;
	
	/*public Account(int _currencyType) {
		this(_currencyType, 0);
	}*/
	
	public Account(int _currencyType, double _initialAmount) {
		currencyType = _currencyType;
		initialAmount = _initialAmount;
		putMoney(new Currency(currencyType, initialAmount));
	}
	
	public int getCurrencyType() {
		return currencyType;
	}

	public double getBalance() {
		return balance;
	}
	
	/*@Override
	public void resetToInitialAmount() {
		balance = initialAmount; 
	}*/
	
	/*public void reflect(double amount) {
		balance += amount;
	}*/

	@Override
	public ICurrency getMoney(double amount) {
		
		if (amount <= 0) {
			throw new IllegalStateException("amount=" + amount);
		}
		
		balance -= amount;
		
		if (balance < 0) {
			throw new IllegalStateException("Zero account");
		}
		
		return new Currency(currencyType, amount);
	}

	@Override
	public void putMoney(ICurrency amount) {
		
		if (amount.getAmount() <= 0) {
			throw new IllegalStateException();
		}
		
		balance += amount.getAmount();
	}
	
	@Override
	public String toString() {
		String result = "";
		result += (long) balance + "" + CurrencyType.toString(currencyType);
		return result;
	}
}
