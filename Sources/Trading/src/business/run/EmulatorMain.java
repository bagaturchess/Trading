package business.run;


import technical.impl.input.HistoricalCurrenciesPairs;
import business.api.CurrencyType;
import business.z_old1.api.IBroker;
import business.z_old1.api.IPlayer;
import business.z_old1.impl.emulator.Emulator;
import business.z_old1.impl.operational.Broker;
import business.z_old1.impl.players.Player_AVG;
import business.z_old1.impl.players.Player_Saver;
import business.z_old1.impl.players.Player_StupidBuySell;
import business.z_old1.impl.players.position.Player_OpenClose_ManyPos;
import business.z_old1.impl.players.position.Player_OpenClose_ManyPos_2AVG;
import business.z_old1.impl.players.position.Player_OpenClose_ManyPos_3AVG;
import business.z_old1.impl.players.position.Player_OpenClose_Prediction;
import business.z_old1.impl.players.position.Player_OpenClose_SinglePos;


public class EmulatorMain {
	
	public static void main(String[] args) {
		
		IBroker broker = new Broker();
		
		/*IPlayer[] players = new IPlayer[] {
				new Player_Saver("EUR/USD Player_Saver"
						),
				new Player_StupidBuySell("EUR/USD Player_StupidBuySell",
						CurrencyType.USD, broker, 0.02
						),
				new Player_AVG("EUR/USD Player_AVG",
						CurrencyType.USD, broker, 0.055,
						1
						),
				new Player_AVG("EUR/USD Player_AVG",
						CurrencyType.USD, broker, 0.04,
						2
						),
				};*/
		
		/*
		 * BEST
	 			new Player_OpenClose_SinglePos("EUR/USD OpenClose",
					CurrencyType.USD, broker,
					8
				),
				new Player_OpenClose_ManyPos("EUR/USD OpenClose Many",
					CurrencyType.USD, broker,
					10
				),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many 2AVG",
					CurrencyType.USD, broker,
					10, 16
				),
				new Player_OpenClose_ManyPos_3AVG("EUR/USD OpenClose Many 2AVG",
					CurrencyType.USD, broker,
					10, 16, 18
				),
		 */
		
		/*IPlayer[] players = new IPlayer[] {				
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						2, 4
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						3, 6
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						4, 8
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						5, 10
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						6, 12
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						7, 14
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						8, 16
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						9, 18
						),
				new Player_OpenClose_ManyPos_2AVG("EUR/USD OpenClose Many",
						CurrencyType.USD, broker,
						10, 20
						),
		};*/
		
		IPlayer[] players = new IPlayer[] {
				new Player_OpenClose_Prediction("EUR/USD OpenClose Prediction",
				CurrencyType.USD, broker),
		};
		
		/*IPlayer[] players = new IPlayer[15];
		
		int count = 0;
		for (int i=0; i<5; i++) {
			for (double j=0.02; j<0.08; j+= 0.02) {
				players[count++] = new Player_AVG("EUR/USD Player_AVG",
						new Account(CurrencyType.EUR, 100), CurrencyType.USD, broker, j,
						i * i + 2
					);
			}
		}*/
		
		/*IPlayer[] players = new IPlayer[10];
		
		int count = 0;
		for (int i=0; i<players.length; i++) {
			players[count++] = new Player_AVG("EUR/USD Player_AVG",
						new Account(CurrencyType.EUR, 100), CurrencyType.USD, broker, 0.055,
						i * i + 1
					);
		}*/

		
		double[][] rateSequences = new double[][] {HistoricalCurrenciesPairs.eur2usd};
		
		Emulator emulator = new Emulator(broker, players, rateSequences);
	}
}
