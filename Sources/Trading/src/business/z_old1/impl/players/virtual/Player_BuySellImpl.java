package business.z_old1.impl.players.virtual;


import business.z_old1.api.IBroker;


public abstract class Player_BuySellImpl<T> extends Player_BaseImpl<T> {
	
	
	private IBroker broker;
	private double riskPercent;
	
	
	public Player_BuySellImpl(String _name, int toCurrencyType, IBroker _broker, double _riskPercent) {
		super(_name, toCurrencyType);
		broker = _broker;
		riskPercent = _riskPercent;
	}
	
	protected IBroker getBroker() {
		return broker;
	}
	
	protected double getRiskPercent() {
		return riskPercent;
	}
}
