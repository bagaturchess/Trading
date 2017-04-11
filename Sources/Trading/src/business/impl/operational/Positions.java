package business.impl.operational;


import java.util.ArrayList;
import java.util.List;


public class Positions {
	
	
	private List<Position>[] positions;
	
	
	@SuppressWarnings("unchecked")
	public Positions() {
		positions = new List[PositionStatus.MAX_INDEX];
		for (int i=0; i<positions.length; i++) {
			positions[i] = new ArrayList<Position>();
		}
	}
	
	
	public void addOpenedPosition(Position pos) {
		positions[PositionStatus.OPENED].add(pos);
	}
	
	
	public void moveToClosedPositions(Position pos, int positionStatus) {
		boolean removed = positions[PositionStatus.OPENED].remove(pos);
		if (!removed) {
			throw new IllegalStateException("The position is not open and cannot be closed. Position object: " + pos);
		}
		positions[positionStatus].add(pos);
	}
	
	
	public List<Position> getOpenedPositions() {
		return positions[PositionStatus.OPENED];
	}
	
	
	public double getFinalBalance(double currentRate) {
		double balance = 0;
		for (int i=0; i<positions.length; i++) {
			
			if (i == PositionStatus.OPENED) {
				//TODO: implement
				for (Position cur: positions[i]) {
					balance += cur.getCurrentBalance(currentRate);
				}
				continue;
			}
			
			for (Position cur: positions[i]) {
				balance += cur.getFinalBalance();
			}
		}
		
		return balance;
	}
	
	
	@Override
	public String toString() {
		String result = "[O, CW, CS, CL, CA, C]	";
		result += positions[PositionStatus.OPENED].size();
		result += "	" + positions[PositionStatus.CLOSED_VARIANT_WIN].size();
		result += "	" + positions[PositionStatus.CLOSED_VARIANT_IN_SPREAD].size();
		result += "	" + positions[PositionStatus.CLOSED_VARIANT_LOSE].size();
		result += "	" + positions[PositionStatus.CLOSED_VARIANT_AUTOCLOSED].size();
		result += "	" + positions[PositionStatus.CLOSED].size();

		return result;
	}
}
