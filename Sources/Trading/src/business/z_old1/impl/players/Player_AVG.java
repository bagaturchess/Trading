package business.z_old1.impl.players;


import technical.impl.math.VarStatistic;
import business.api.IAccount;
import business.api.ICurrency;
import business.z_old1.api.IBroker;
import business.z_old1.impl.operational.Currency;
import business.z_old1.impl.players.virtual.Player_BuySellImpl;


public class Player_AVG extends Player_BuySellImpl<String> {
	
	
	private ICurrency riskedMoney;
	
	private double[] buff_avg;
	private int count = 0;
	
	private double oldAvg;
	
	public Player_AVG(String _name, /*IAccount account,*/ int toCurrencyType, IBroker _broker, double _riskPercent, int avgLength) {
		super(_name + avgLength + "/" + _riskPercent, /*account,*/ toCurrencyType, _broker, _riskPercent);
		
		if (avgLength < 1) {
			throw new IllegalStateException("avgLength=" + avgLength);
		}
		
		buff_avg = new double[avgLength];
	}
	
	public void addNewRate(IAccount account, int order, double rate) {
		
		if (account.getBalance() < 0.1) return; 
		
		if (count < buff_avg.length - 1) {//Skip last in order to have shift and put of new val in the begining of the 'else' block
			buff_avg[count++] = rate;
		} else {
			
			//Shift and put new val
			for (int i=0; i<buff_avg.length-1; i++) {
				buff_avg[i] = buff_avg[i+1];
			}
			buff_avg[buff_avg.length-1] = rate;
			
			//Calc avg
			VarStatistic stat = new VarStatistic(false);
			for (int i=0; i<buff_avg.length; i++) {
				stat.addValue(buff_avg[i], buff_avg[i]);
			}
			double curAvg = stat.getEntropy();
			
			//Decision
			if (curAvg < oldAvg) {
				//Buy
				double moneyToRisk_d = getRiskPercent() * account.getBalance();
				riskedMoney = account.getMoney(moneyToRisk_d);
			} else if (curAvg > oldAvg) {
				//Sell
				if (riskedMoney != null) { //Not the first time
					ICurrency exchanged = getBroker().convert(riskedMoney, getToCurrencyType());
					account.putMoney(exchanged);
				}
			}
			
			//Keep current avg
			oldAvg = curAvg;
		}
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		count = 0;
		oldAvg = -1;
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
