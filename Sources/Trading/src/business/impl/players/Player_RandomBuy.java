package business.impl.players;


import java.util.List;

import business.impl.operational.Position;
import business.impl.players.virtual.Player_BaseImpl;


public class Player_RandomBuy extends Player_BaseImpl {
	
	
	public Player_RandomBuy(Object[] args) {
		super(new Object[] {Player_RandomBuy.class.getName(), args[0], args[1], args[2]});
	}
	
	
	@Override
	public void addNewRate(int order, double rate) {
		
		double random = Math.random();
		if (random >= 0.5) {
			openPosition(1, 0.0005);
		}
		
		List<Position> openedPositions = getOpenedPositions();
		for (Position cur: openedPositions) {
			double rate_open = cur.getRateOpen();
			double spread = cur.getSpread();
			double current_rate = getRate();
			
			if (current_rate - rate_open > spread) {
				addPositionForClosing(cur);
			}
		}
		
		closePositions();
	}
}
