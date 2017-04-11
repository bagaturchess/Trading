package business.z_old1.impl.players.position;


import java.util.List;


public class OpenCloseMetainf {
	
	
	private List<Position> win_positions;
	private List<Position> lose_positions;
	
	
	OpenCloseMetainf(List<Position> _lose_positions, List<Position> _win_positions) {
		lose_positions = _lose_positions;
		win_positions = _win_positions;
	}
	
	public double getWinRate() {
		if (win_positions.size() == 0) {
			return 0;
		}
		return win_positions.size() / ((double)(win_positions.size() + lose_positions.size()));
	}
	
	public double getWinAmount() {
		double win = 0;
		for (Position pos: win_positions) {
			win += pos.getAmount();
		}
		
		double lose = 0;
		for (Position pos: lose_positions) {
			lose += pos.getAmount();
		}
		
		return win - lose;
	}
	
	public int getWinCount() {
		return win_positions.size();
	}
	
	public int getDiffCount() {
		return win_positions.size() - lose_positions.size();
	}
	
	public String toString() {
		String result = "";
		result += "Win Count: " + win_positions.size()
			+ ", Win Rate: " + getWinRate();
		
		return result;
	}
}
