package business.api;


import java.util.List;

import business.impl.operational.Position;
import business.impl.operational.Positions;


public interface IBroker {
	//User operations
	public void openPosition(Object owner, int fromCurrencyType, int toCurrencyType, double fromCurrencyAmount, double autocloseMargin);
	public List<Position> getOpenedPositions(Object owner);
	public void closePosition(Object owner, Position position);
	public Positions getAllPositions(Object owner);
	public double getRate(int fromCurrencyType, int toCurrencyType);
	
	//System operations
	public void beforeNextRate();
}
