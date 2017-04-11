package business.api;

public interface IAccount {
	public int getCurrencyType();
	public double getBalance();
	public ICurrency getMoney(double amount);
	public void putMoney(ICurrency amount);
	//public void resetToInitialAmount();
}
