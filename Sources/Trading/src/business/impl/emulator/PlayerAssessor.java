package business.impl.emulator;


import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.List;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.visual.VisualSequence;

import business.api.IBroker;
import business.api.ICurrencyPairMetadata;
import business.api.IPlayer;
import business.impl.operational.Bank;
import business.impl.operational.Broker;
import business.impl.operational.CurrenciesRatesProvider;
import business.impl.operational.Positions;
import business.impl.players.Player_RandomBuy;


public class PlayerAssessor {
	
	
	private double max_autoclose_margin = 0.01;
	
	
	private Class<? extends IPlayer> playerClass;
	private ICurrencyPairMetadata[] rates;
	private CurrenciesRatesProvider[] ratesProviders;
	private IBroker[] brokers;
	private IPlayer[] players;
	private CurrencyRateAssess[] assesses;
	private CurrencyRateAssess finalResult;
	
	
	public PlayerAssessor(Class<? extends IPlayer> _playerClass, Object[] additionalArgs) throws Exception {
		
		playerClass = _playerClass;
		
		rates = HistoricalCurrenciesPairs.getPairsRates();
		ratesProviders = new CurrenciesRatesProvider[rates.length];
		brokers = new IBroker[rates.length];
		players = new IPlayer[rates.length];
		assesses = new CurrencyRateAssess[rates.length];
		
		for (int i=0; i<brokers.length; i++) {
			ratesProviders[i] = new CurrenciesRatesProvider();
		}
		
		for (int i=0; i<brokers.length; i++) {
			brokers[i] = new Broker(ratesProviders[i], new Bank(ratesProviders[i]), max_autoclose_margin);
		}
		
		for (int i=0; i<players.length; i++) {
			
			Object args_mandatory[] = new Object[] {brokers[i], rates[i].getFromCurrencyType(), rates[i].getToCurrencyType()};
			Object args_all[] = new Object[args_mandatory.length + additionalArgs.length];
			System.arraycopy(args_mandatory, 0, args_all, 0, args_mandatory.length);
			System.arraycopy(additionalArgs, 0, args_all, args_mandatory.length, additionalArgs.length);
			
			players[i] = createPlayer(args_all);
			//System.out.println(players[i].getName());
		}
		
		for (int i=0; i<assesses.length; i++) {
			assesses[i] = new CurrencyRateAssess();
		}
	}
	
	
	public void execute() {
		for (int i=0; i<rates.length; i++) {
			
			ICurrencyPairMetadata pairRate = rates[i];
			ratesProviders[i].updateSpread(pairRate.getFromCurrencyType(), pairRate.getToCurrencyType(), pairRate.getSpread());
			
			double[] ratesData = pairRate.getRatesData();
			for (int j=0; j<ratesData.length; j++) {
				
				double cur_rate_data = ratesData[j];
				
				ratesProviders[i].updateRate(pairRate.getFromCurrencyType(), pairRate.getToCurrencyType(), cur_rate_data);
				
				brokers[i].beforeNextRate();
				players[i].addNewRate(j, cur_rate_data);
				
				Positions positions = brokers[i].getAllPositions(players[i]);
				double balance = positions.getFinalBalance(ratesProviders[i].getRate(pairRate.getFromCurrencyType(), pairRate.getToCurrencyType()));
				//System.out.println(j + "	" + positions + "	B=" + balance);
				assesses[i].add(balance);
			}
		}
		
		int max = 0;
		for (int i=0; i<assesses.length; i++) {
			CurrencyRateAssess cur = assesses[i];
			if (cur.getBalances().size() > max) {
				max = cur.getBalances().size();
			}
		}
		
		System.out.println("******************************************");
		for (int i=0; i<rates.length; i++) {
			Positions positions = brokers[i].getAllPositions(players[i]);
			System.out.println(positions);
		}
		System.out.println("******************************************");
		
		finalResult = new CurrencyRateAssess();
		for (int i=0; i<max; i++) {
			double avg_val = 0;
			double count = 0;
			for (int j=0; j<assesses.length; j++) {
				List<Double> cur_assess = assesses[j].getBalances();
				if (i < cur_assess.size()) {
					double cur_val = cur_assess.get(i);
					avg_val += cur_val;
					count++;
				}
			}
			finalResult.add(avg_val / count);
		}
	}
	
	
	public CurrencyRateAssess getFinalResult() {
		return finalResult;
	}

	
	private IPlayer createPlayer(Object args) throws Exception {
		Constructor<? extends IPlayer> constructor = playerClass.getConstructor((new Object[0]).getClass());
		return constructor.newInstance(args);
	}
	
	
	public static void main(String[] args) {
		try {
			
			VisualSequence visual = new VisualSequence(1);
			
			PlayerAssessor assessor = new PlayerAssessor(Player_RandomBuy.class, new Object[0]);
			assessor.execute();
			
			Color[] colours = new Color[] {new Color(255, 0, 0), new Color(0, 255, 0)};
			
			for (int i=0; i<assessor.assesses.length; i++) {
				CurrencyRateAssess assess = assessor.assesses[i];
				List<Double> balances = assess.getBalances();
				
				double[] sequence = new double[balances.size()];
				for (int j=0; j<sequence.length; j++) {
					sequence[j] = balances.get(j);
				}
				
				visual.addLine(sequence, colours[i], true);
				System.out.println(assessor.assesses[i].getWaste());
			}
			
			visual.addLine(assessor.getFinalResult().getBalances(), new Color(0, 0, 255), true, false);
			System.out.println(assessor.getFinalResult().getWaste());
			
			visual.visualize();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
