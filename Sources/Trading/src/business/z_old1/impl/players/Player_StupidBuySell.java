package business.z_old1.impl.players;


import business.api.IAccount;
import business.api.ICurrency;
import business.z_old1.api.IBroker;
import business.z_old1.impl.operational.Currency;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_StupidBuySell extends Player_BuySellImpl<String> {
	
	
	private ICurrency riskedMoney;
	
	
	public Player_StupidBuySell(String _name, int toCurrencyType, IBroker _broker, double _riskPercent) {
		super(_name, toCurrencyType, _broker, _riskPercent);
	}
	
	public void addNewRate(IAccount account, int order, double rate) {
		
		//Get win or lose
		if (riskedMoney != null) { //Not the first time
			ICurrency exchanged = getBroker().convert(riskedMoney, getToCurrencyType());
			account.putMoney(exchanged);
		}
		
		//Buy new currency amount
		double moneyToRisk_d = getRiskPercent() * account.getBalance();
		riskedMoney = account.getMoney(moneyToRisk_d);
	}
	
	
	@Override
	public void startRateSequence(IAccount account) {
		riskedMoney = null;
	}
	
	@Override
	public void endRateSequence(IAccount account) {
		if (riskedMoney != null) {
			double capitalization = getBroker().convert(riskedMoney, getToCurrencyType()).getAmount();
			account.putMoney(new Currency(account.getCurrencyType(), capitalization));
		}
	}
}
