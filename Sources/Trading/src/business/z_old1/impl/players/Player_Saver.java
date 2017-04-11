package business.z_old1.impl.players;


import business.api.IAccount;
import business.z_old1.impl.players.virtual.Player_BaseImpl;


public class Player_Saver extends Player_BaseImpl<String> {
	
	public Player_Saver(String _name) {
		super(_name, -1);
	}
	
	@Override
	public void addNewRate(IAccount account, int order, double rate) {
		//Do nothing
	}
	
	@Override
	public void startRateSequence(IAccount account) {
		//Do nothing
	}
	
	@Override
	public void endRateSequence(IAccount account) {
		//Do nothing
	}
}
