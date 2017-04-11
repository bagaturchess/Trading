package business.impl.operational;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.api.IBank;
import business.api.IBroker;
import business.api.ICurrency;


public class Broker implements IBroker {
	
	
	private CurrenciesRatesProvider ratesProvider;
	private IBank bank;
	private double max_autoclose_margin;
	
	private Map<Object, Positions> positionsPerOwner;
	
	
	public Broker(CurrenciesRatesProvider _ratesProvider, IBank _bank, double _max_autoclose_margin) {
		ratesProvider = _ratesProvider;
		bank = _bank;
		max_autoclose_margin = _max_autoclose_margin;
		
		positionsPerOwner = new HashMap<Object, Positions>();
	}

	
	@Override
	public void openPosition(Object owner, int fromCurrencyType, int toCurrencyType, double fromCurrencyAmount, double autocloseMargin) {
		
		if (autocloseMargin > max_autoclose_margin) {
			throw new IllegalStateException("autocloseMargin=" + autocloseMargin);
		}
		
		Positions positions = positionsPerOwner.get(owner);
		if (positions == null) {
			positions = new Positions();
			positionsPerOwner.put(owner, positions);
		}
		
		double rate_open = ratesProvider.getRate(fromCurrencyType, toCurrencyType);
		ICurrency currency_open = bank.getCurrency(null, toCurrencyType, fromCurrencyAmount * rate_open);
		double spread = ratesProvider.getSpread(fromCurrencyType, toCurrencyType);
		if (spread <= 0) { 
			throw new IllegalStateException("spread=" + spread);
		}
		Position pos = new Position(fromCurrencyType, currency_open, rate_open, spread, autocloseMargin);
		
		positions.addOpenedPosition(pos);
	}
	
	
	@Override
	public List<Position> getOpenedPositions(Object owner) {
		Positions positions = positionsPerOwner.get(owner);
		if (positions == null) {
			positions = new Positions();
			positionsPerOwner.put(owner, positions);
		}
		return positions.getOpenedPositions();
	}
	
	
	@Override
	public void closePosition(Object owner, Position position) {
		Positions positions = positionsPerOwner.get(owner);
		
		int fromCurrencyType = position.getFromCurrencyType();
		int toCurrencyType = position.getToCurrencyType();
		double rate_close = ratesProvider.getRate(fromCurrencyType, toCurrencyType);
		double rate_open = position.getRateOpen();
		double delta_rate = rate_close - rate_open;
		
		if (delta_rate <= -position.getAutocloseMargin()) {
			throw new IllegalStateException("closePosition, delta_rate=" + delta_rate + ", position.getAutocloseMargin()=" + position.getAutocloseMargin());
		}
		
		int positionStatus = -1;
		if (delta_rate > position.getSpread()) {
			positionStatus = PositionStatus.CLOSED_VARIANT_WIN;
		} else if (delta_rate >= 0) {
			positionStatus = PositionStatus.CLOSED_VARIANT_IN_SPREAD;
		} else {
			positionStatus = PositionStatus.CLOSED_VARIANT_LOSE;
		}
		
		ICurrency currency_close = bank.convertCurrency(position.getCurrencyOpen(), fromCurrencyType);
		position.close(currency_close, rate_close, positionStatus);
		
		positions.moveToClosedPositions(position, positionStatus);
	}
	
	protected void autoclosePosition(Object owner, Position position) {
		Positions positions = positionsPerOwner.get(owner);
		
		int fromCurrencyType = position.getFromCurrencyType();
		int toCurrencyType = position.getToCurrencyType();
		double rate_close = ratesProvider.getRate(fromCurrencyType, toCurrencyType);
		double rate_open = position.getRateOpen();
		double delta_rate = rate_close - rate_open;
		
		if (delta_rate > -position.getAutocloseMargin()) {
			throw new IllegalStateException("autoclosePosition, delta_rate=" + delta_rate + ", position.getAutocloseMargin()=" + position.getAutocloseMargin());
		}
		
		ICurrency currency_close = bank.convertCurrency(position.getCurrencyOpen(), fromCurrencyType);
		position.close(currency_close, rate_close, PositionStatus.CLOSED_VARIANT_AUTOCLOSED);
		
		positions.moveToClosedPositions(position, PositionStatus.CLOSED_VARIANT_AUTOCLOSED);
	}
	
	@Override
	public Positions getAllPositions(Object owner) {
		Positions positions = positionsPerOwner.get(owner);
		if (positions == null) {
			positions = new Positions();
			positionsPerOwner.put(owner, positions);
		}
		return positions;
	}
	
	
	@Override
	public double getRate(int fromCurrencyType, int toCurrencyType) {
		return ratesProvider.getRate(fromCurrencyType, toCurrencyType);
	}
	
	
	@Override
	public void beforeNextRate() {
		for (Object owner: positionsPerOwner.keySet()) {
			
			List<Position> positionsForClosing = new ArrayList<Position>();
			Positions positions = positionsPerOwner.get(owner);
			List<Position> opened_positions = positions.getOpenedPositions();
			
			for (Position position: opened_positions) {
				int fromCurrencyType = position.getFromCurrencyType();
				int toCurrencyType = position.getToCurrencyType();
				double rate_open = position.getRateOpen();
				double autoclose = position.getAutocloseMargin();
				if (ratesProvider.getRate(fromCurrencyType, toCurrencyType) - rate_open <= -autoclose) {
					positionsForClosing.add(position);
				}
			}
			for (Position cur: positionsForClosing) {
				autoclosePosition(owner, cur);
			}
		}
	}
}
