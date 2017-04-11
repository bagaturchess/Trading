package business.z_old1.api.internal;

import business.api.IAccount;

public interface IPlayerMonitor<T> {
	public void startSequence(IAccount account);
	public void newRateAdded(int order, IAccount account, T playerMetaInf);
	public void stopSequence(IAccount account);
	
	public Object getRewardStability();
}
