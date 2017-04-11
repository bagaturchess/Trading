package business.impl.emulator;

import java.util.ArrayList;
import java.util.List;

public class CurrencyRateAssess {
	
	
	private List<Double> balances;
	private double waste;
	
	
	public CurrencyRateAssess() {
		balances = new ArrayList<Double>(); 
	}


	public void add(double balance) {
		
		for (int i = balances.size() - 1; i >= 0; i--) {
			Double cur_old_balance = balances.get(i);
			if (balance < cur_old_balance) {
				double	deltaBalance = Math.abs(cur_old_balance - balance) /
								Math.max(Math.abs(cur_old_balance), Math.abs(cur_old_balance - balance));
				double deltaTime = (balances.size() - i) / (double) balances.size();
				
				if (deltaBalance <= 0 || deltaBalance > 1) {
					throw new IllegalStateException("deltaBalance=" + deltaBalance);
				}
				if (deltaTime <= 0 || deltaTime > 1) {
					throw new IllegalStateException("deltaTime=" + deltaTime);
				}
				
				waste += (deltaBalance * deltaTime);
			}
		}
		
		balances.add(balance);
	}
	
	
	public List<Double> getBalances() {
		return balances;
	}


	public double getWaste() {
		return waste;
	}
}
