package business.z_old1.impl.emulator.monitors;


import java.util.ArrayList;
import java.util.List;

import technical.impl.math.VarStatistic;
import technical.impl.utils.StringUtils;
import business.api.IAccount;
import business.z_old1.api.internal.IPlayerMonitor;


public class PlayerMonitor_TangensBased implements IPlayerMonitor<Double> {
	
	private double balanceBegin;
	private long ratesCount;
	
	private List<VarStatistic> stats;
	
	public PlayerMonitor_TangensBased() {
		stats = new ArrayList<VarStatistic>();
	}
	
	@Override
	public VarStatUnion getRewardStability() {
		return new VarStatUnion(stats);
	}
	
	@Override
	public void startSequence(IAccount account) {
		balanceBegin = account.getBalance();
		ratesCount = 0;
		stats.add(new VarStatistic(false)); //New stat per sequence
	}
	
	@Override
	public void stopSequence(IAccount account) {
		//System.out.println(stat);
	}
	
	@Override
	public void newRateAdded(int order, IAccount account, Double playerMetaInf) {
		
		ratesCount++;
		
		double deltaBalance = account.getBalance() - balanceBegin; 
		double tangens = deltaBalance / ratesCount;
		
		getLastStat().addValue(tangens, tangens);
	}
	
	private VarStatistic getLastStat() {
		return stats.get(stats.size() - 1);
	}
	
	private static class VarStatUnion {
		
		private double avg;
		private double stddev;
		
		public VarStatUnion(List<VarStatistic> stats) {
			int count = stats.size();
			double avg_sum = 0;
			double stddev_sum = 0;
			
			for (int i=0; i<count; i++) {
				avg_sum += stats.get(i).getEntropy();
				stddev_sum += stats.get(i).getSTDEV();
			}
			
			avg = avg_sum / count;
			stddev = stddev_sum / count;
		}
		
		@Override
		public String toString() {
			String result = "";
			result += "  " + StringUtils.align(avg);
			result += "  " + StringUtils.align(stddev);
			return result;
		}
	}
}
