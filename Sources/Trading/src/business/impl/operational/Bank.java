package business.impl.operational;


import business.api.IAccount;
import business.api.IBank;
import business.api.ICurrency;
import business.z_old1.impl.operational.Currency;


public class Bank implements IBank {
	
	
	private CurrenciesRatesProvider ratesProvider;
	
	
	public Bank(CurrenciesRatesProvider _ratesProvider) {
		ratesProvider = _ratesProvider;
	}
	
	
	@Override
	public ICurrency getCurrency(IAccount account, int currencyType, double amount) {
		return createCurrency(currencyType, amount);
	}
	
	
	@Override
	public ICurrency convertCurrency(ICurrency fromAmount, int toCurrencyType) {
		ICurrency toAmount = createCurrency(toCurrencyType, fromAmount.getAmount() * ratesProvider.getRate(fromAmount.getType(), toCurrencyType)); //Opposite rate transition
		return toAmount;
	}
	
	
	private ICurrency createCurrency(int currencyType, double amount) {
		ICurrency currency = new Currency(currencyType, amount);
		return currency;
	}
}
