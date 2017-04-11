package business.z_old1.api;

import business.api.IAccount;


public interface IPlayer<T> {
	
	public String getName();
	public void startRateSequence(IAccount account);
	public void addNewRate(IAccount account, int order, double rate);
	public void endRateSequence(IAccount account);
	
	public T getMetaInf();
}
