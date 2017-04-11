package business.api;


public interface IBank {
	public ICurrency getCurrency(IAccount account, int currencyType, double amount);
	public ICurrency convertCurrency(ICurrency fromAmount, int toCurrencyType);
}
