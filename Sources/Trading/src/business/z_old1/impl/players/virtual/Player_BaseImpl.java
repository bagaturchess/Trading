package business.z_old1.impl.players.virtual;

import business.z_old1.api.IPlayer;

public abstract class Player_BaseImpl<T> implements IPlayer<T> {
	
	private String name;
	private int toCurrencyType;
	
	
	public Player_BaseImpl(String _name, int _toCurrencyType) {
		name = _name;
		toCurrencyType = _toCurrencyType;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected int getToCurrencyType() {
		return toCurrencyType;
	}
	
	@Override
	public T getMetaInf() {
		return null;
	}
}
