package business.impl.operational;


import business.api.ICurrency;


public class Position {
	
	
	private int fromCurrencyType;
	private int toCurrencyType;
	private ICurrency currencyOpen;
	private ICurrency currencyClose;
	private double rate_open;
	private double spread;
	private double autoclose_margin;
	private double rate_close;
	private int status = PositionStatus.OPENED;
	private double finalBalance;
	
	
	public Position(int _fromCurrencyType, ICurrency _currencyOpen, double _rate_open, double _spread, double _autoclose_margin) {
		fromCurrencyType = _fromCurrencyType;
		toCurrencyType = _currencyOpen.getType();
		currencyOpen = _currencyOpen;
		rate_open = _rate_open;
		spread = _spread;
		autoclose_margin = _autoclose_margin;
	}


	public void close(ICurrency _currencyClose, double _rate_close, int _status) {
		if (_currencyClose.getType() != fromCurrencyType) {
			throw new IllegalStateException();
		}
		currencyClose = _currencyClose;
		rate_close = _rate_close;
		status = _status;
		finalBalance = currencyClose.getAmount() - (currencyOpen.getAmount() / rate_open);
	}
	
	
	/*public double getCurrentCurrency(double currentRate) {
		return currencyOpen.getAmount() / currentRate;
	}*/
	
	public double getCurrentBalance(double currentRate) {
		return currencyOpen.getAmount() / currentRate - currencyOpen.getAmount() / rate_open;
	}
	
	
	public double getFinalBalance() {
		if (status == PositionStatus.OPENED) {
			throw new IllegalStateException();
		}
		return finalBalance;
	}
	
	
	public int getFromCurrencyType() {
		return fromCurrencyType;
	}


	public int getToCurrencyType() {
		return toCurrencyType;
	}

	public double getRateOpen() {
		return rate_open;
	}

	public double getRateClose() {
		return rate_close;
	}
	
	public ICurrency getCurrencyOpen() {
		return currencyOpen;
	}

	public double getSpread() {
		return spread;
	}
	
	public double getAutocloseMargin() {
		return autoclose_margin;
	}
}
