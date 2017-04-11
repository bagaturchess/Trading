package business.z_old1.impl.emulator.monitors;


import business.api.IAccount;
import business.z_old1.api.internal.IPlayerMonitor;


public class PlayerMonitor_PathBased implements IPlayerMonitor<Double> {
	
	
	private double oldAccountBalance = -1;
	private double balancePath = 0;
	private double balanceBegin;
	private double balanceEnd;
	private long ratesCount;
	
	
	public PlayerMonitor_PathBased() {
		
	}
	
	
	@Override
	public Object getRewardStability() {
		double idealPath = Math.sqrt(Math.pow(Math.abs(getBalanceDiff()), 2) + Math.pow(getRatesCount() - 1, 2));
		return idealPath / getBalancePath();
	}
	
	private long getRatesCount() {
		return ratesCount;
	}
	
	private double getBalancePath() {
		return balancePath;
	}
	
	private double getBalanceDiff() {
		return balanceEnd - balanceBegin;
	}
	
	@Override
	public void startSequence(IAccount account) {
		oldAccountBalance = -1;
		balancePath = 0;
		balanceBegin = account.getBalance();
		balanceEnd = balanceBegin;
		ratesCount = 0;
	}
	
	@Override
	public void stopSequence(IAccount account) {
		balanceEnd = account.getBalance();
	}
	
	@Override
	public void newRateAdded(int order, IAccount account, Double playerMetaInf) {
		
		ratesCount++;//use "order + 1"
		
		if (oldAccountBalance != -1) {
			double deltaBalance = Math.abs(oldAccountBalance - account.getBalance());
			balancePath += Math.sqrt(1 + Math.pow(deltaBalance, 2));
		}
		
		oldAccountBalance = account.getBalance();
	}
}
