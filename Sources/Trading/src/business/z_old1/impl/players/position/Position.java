package business.z_old1.impl.players.position;


public class Position {
	
	
	private double amount;
	private double rate_open;
	private double rate_close;
	
	
	public Position(double _amount, double _rate_open) {
		amount = _amount;
		rate_open = _rate_open;
		
		amount = amount * rate_open;
	}
	
	
	public void close(double _rate_close) {
		rate_close = _rate_close;
		
		amount = amount * _rate_close;
	}


	public double getAmount() {
		return amount;
	}


	public double getOpenRate() {
		return rate_open;
	}
}
