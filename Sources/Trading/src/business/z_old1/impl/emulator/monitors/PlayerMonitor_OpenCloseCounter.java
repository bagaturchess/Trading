package business.z_old1.impl.emulator.monitors;


import technical.impl.math.VarStatistic;
import business.api.IAccount;
import business.z_old1.api.internal.IPlayerMonitor;
import business.z_old1.impl.players.position.OpenCloseMetainf;


public class PlayerMonitor_OpenCloseCounter implements IPlayerMonitor<OpenCloseMetainf> {
	
	
	private double balanceBegin;
	private double balanceEnd;
	
	private VarStatistic stat;
	private OpenCloseMetainf lastPlayerMetainf;
	
	public PlayerMonitor_OpenCloseCounter() {
		
	}
	
	@Override
	public Object getRewardStability() {
		return new OpenCloseReward(stat, lastPlayerMetainf);
	}
	
	@Override
	public void startSequence(IAccount account) {
		balanceBegin = account.getBalance();
		stat = new VarStatistic(false);
	}
	
	@Override
	public void stopSequence(IAccount account) {
		balanceEnd = account.getBalance();
	}
	
	@Override
	public void newRateAdded(int order, IAccount account, OpenCloseMetainf playerMetaInf) {
		lastPlayerMetainf = playerMetaInf;
		
		System.out.println(playerMetaInf.getWinAmount());
		
		stat.addValue(playerMetaInf.getWinRate(), playerMetaInf.getWinRate());
	}
	
	public class OpenCloseReward {
		
		private VarStatistic stat;
		private OpenCloseMetainf lastPlayerMetaInf;
		
		public OpenCloseReward(VarStatistic _stat, OpenCloseMetainf _lastPlayerMetaInf) {
			stat = _stat;
			lastPlayerMetaInf = _lastPlayerMetaInf;
		}
		
		public String toString() {
			String result = "";
			result += "	" + lastPlayerMetaInf.getWinCount()+ "	" + lastPlayerMetaInf.getWinRate() + "	";
			result += "PESIMISTIC AVG WIN %: " + (stat.getEntropy() - stat.getSTDEV()) 
				+ ", AVG WIN%: " + stat.getEntropy()
				+ ", AVG WIN DEV%: " + stat.getSTDEV();
			
			return result;
		}
	}
}
