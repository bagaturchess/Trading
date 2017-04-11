package business.z_old1.impl.emulator;


import business.api.CurrencyType;
import business.api.IAccount;
import business.z_old1.api.IBroker;
import business.z_old1.api.IPlayer;
import business.z_old1.api.internal.IPlayerMonitor;
import business.z_old1.impl.emulator.monitors.PlayerMonitor_OpenCloseCounter;
import business.z_old1.impl.emulator.monitors.PlayerMonitor_PathBased;
import business.z_old1.impl.emulator.monitors.PlayerMonitor_TangensBased;
import business.z_old1.impl.operational.Account;


public class Emulator {
	
	public Emulator(IBroker broker, IPlayer[] players, double[][] rateSequences) {
		
		IPlayerMonitor[] monitors = new IPlayerMonitor[players.length];
		
		for (int i=0; i<monitors.length; i++) {
			monitors[i] = new PlayerMonitor_OpenCloseCounter();
			//monitors[i] = new PlayerMonitor_TangensBased();
			//monitors[i] = new PlayerMonitor_PathBased();
		}
		
		
		for (int p=0; p<players.length; p++) {
			System.out.println("PLAYER MONITOR> " + monitors[p]);
			String dumpStr = "Running: " + players[p].getName() + "	";
			
			processSinglePlayer(broker, players[p], monitors[p], rateSequences);
			
			dumpStr += monitors[p].getRewardStability();
			System.out.println(dumpStr);
		}
	}
	
	private static void processSinglePlayer(IBroker broker, IPlayer player, IPlayerMonitor monitor, double[][] rateSequences) {
		
		for (int r=0; r<rateSequences.length; r++) {
			
			double[] rate = rateSequences[r];
			
			IAccount account = new Account(CurrencyType.EUR, 100);
			
			monitor.startSequence(account);
			player.startRateSequence(account);
			for (int i=0; i<rate.length; i++) {
				broker.updateBroker(CurrencyType.EUR, CurrencyType.USD, rate[i]);
				player.addNewRate(account, i, rate[i]);
				monitor.newRateAdded(i, account, player.getMetaInf());
			}
			player.endRateSequence(account);
			monitor.stopSequence(account);
		}
	}
}
