package business.impl.players.virtual;


import java.util.ArrayList;
import java.util.List;

import business.api.IBroker;
import business.api.IPlayer;
import business.impl.operational.Position;


public abstract class Player_BaseImpl implements IPlayer {
	
	
	private String name;
	private int fromCurrencyType;
	private int toCurrencyType;
	private IBroker broker;
	private List<Position> positionsForClosing;
	
	
	public Player_BaseImpl(Object[] args) {
		this((String) args[0], (IBroker) args[1], (Integer) args[2], (Integer) args[3]);
		positionsForClosing = new ArrayList<Position>();
	}
	
	private Player_BaseImpl(String _name, IBroker _broker, int _fromCurrencyType, int _toCurrencyType) {
		name = _name;
		broker = _broker;
		fromCurrencyType = _fromCurrencyType;
		toCurrencyType = _toCurrencyType;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	protected void addPositionForClosing(Position position) {
		positionsForClosing.add(position);
	}
	
	
	protected void closePositions() {
		for (Position position: positionsForClosing) {
			closePosition(position);
		}
		positionsForClosing.clear();
	}
	
	protected void openPosition(double fromCurrencyAmount, double autocloseMargin) {
		getBroker().openPosition(this, getFromCurrencyType(), getToCurrencyType(), fromCurrencyAmount, autocloseMargin);
	}
	
	
	protected List<Position> getOpenedPositions() {
		return getBroker().getOpenedPositions(this);
	}
	
	
	protected double getRate() {
		return getBroker().getRate(getFromCurrencyType(), getToCurrencyType());
	}
	
	
	protected void closePosition(Position position) {
		getBroker().closePosition(this, position);
	}
	
	private IBroker getBroker() {
		return broker;
	}
	
	private int getFromCurrencyType() {
		return fromCurrencyType;
	}
	
	private int getToCurrencyType() {
		return toCurrencyType;
	}
}
