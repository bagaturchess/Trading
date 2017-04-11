package technical.impl.prediction.continuation;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.utils.SequenceUtils;
import technical.impl.utils.StringUtils;


public class PatternsStatic {
	
	private int patternLength;
	
	private int totalRepetitionCount = 0;
	private Set<String> states_ids;
	private Map<String, Double> states_repetition_percent;
	private Map<String, Boolean> states_go_up_flag;
	private Map<String, Double> states_go_up_percent;
	
	private Map<String, Map<String, Double>> states_transitions;
	private Map<String, Double> states_transitions_count;
	
	
	public PatternsStatic(double[] sequence, int _patternLength) {
		
		patternLength = _patternLength;
		
		//Init arrays
		states_ids = new HashSet<String>();
		states_repetition_percent = new HashMap<String, Double>();
		states_transitions = new HashMap<String, Map<String,Double>>();
		states_transitions_count = new HashMap<String, Double>();
		states_go_up_flag = new HashMap<String, Boolean>();
		states_go_up_percent = new HashMap<String, Double>();
		
		
		//Add all states
		double[] buffer_rates = new double[patternLength];
		int[] buffer_order = new int[patternLength];
		
		for (int i = 0; i <= sequence.length - patternLength; i++) {
			
			System.arraycopy(sequence, i, buffer_rates, 0, patternLength);
			SequenceUtils.buildOrderList(buffer_rates, buffer_order);
			String state_name = orderArrayToStateName(buffer_order);
			
			if (!states_ids.contains(state_name)) {
				states_ids.add(state_name);
				states_repetition_percent.put(state_name, 0D);
			}
			
			states_repetition_percent.put(state_name, states_repetition_percent.get(state_name) + 1);
			totalRepetitionCount++;
		}
		

		//Set go up flag
		for (int i = 0; i <= sequence.length - patternLength; i++) {
			
			System.arraycopy(sequence, i, buffer_rates, 0, patternLength);
			SequenceUtils.buildOrderList(buffer_rates, buffer_order);
			String state_name = orderArrayToStateName(buffer_order);

			if (buffer_order[buffer_order.length - 1] > buffer_order[buffer_order.length - 2]) {
				states_go_up_flag.put(state_name, true);
			} else {
				states_go_up_flag.put(state_name, false);
			}
		}
		
		//Convert repetition count to percent
		Set<String> keys = states_repetition_percent.keySet();
		for (String state_name: keys) {
			double repetition_count = states_repetition_percent.get(state_name);
			states_repetition_percent.put(state_name, repetition_count / totalRepetitionCount);
		}
		
		
		//Fill transitions
		double[] buffer_rates1 = new double[patternLength];
		int[] buffer_order1 = new int[patternLength];
		double[] buffer_rates2 = new double[patternLength];
		int[] buffer_order2 = new int[patternLength];
		for (int i = 0; i <= sequence.length - patternLength - 1; i++) {
			System.arraycopy(sequence, i, buffer_rates1, 0, patternLength);
			SequenceUtils.buildOrderList(buffer_rates1, buffer_order1);
			System.arraycopy(sequence, i + 1, buffer_rates2, 0, patternLength);
			SequenceUtils.buildOrderList(buffer_rates2, buffer_order2);
			String state_name1 = orderArrayToStateName(buffer_order1);
			String state_name2 = orderArrayToStateName(buffer_order2);
						
			Map<String, Double> to_list = states_transitions.get(state_name1);
			if (to_list == null) {
				to_list = new HashMap<String, Double>();
				states_transitions.put(state_name1, to_list);
			}
			
			Double count_to = to_list.get(state_name2);
			if (count_to == null) {
				to_list.put(state_name2, 1D);
			} else {
				to_list.put(state_name2, count_to + 1);
			}
			
			Double count_all = states_transitions_count.get(state_name1);
			if (count_all == null) {
				states_transitions_count.put(state_name1, 1D);
			} else {
				states_transitions_count.put(state_name1, count_all + 1);
			}
		}
		
		
		//Sum go up probs per each 'from' state
		Set<String> from_states = states_transitions.keySet();
		for (String from_state_name: from_states) {
			
			Map<String, Double> to_states_list = states_transitions.get(from_state_name);
			
			double go_up_percent = 0;
			Set<String> to_states = to_states_list.keySet();
			for (String to_state_name: to_states) {
				if (states_go_up_flag.get(to_state_name)) {
					go_up_percent += to_states_list.get(to_state_name);
				}
			}
			
			Double count_all = states_transitions_count.get(from_state_name);
			if (count_all > 0) {
				states_go_up_percent.put(from_state_name, go_up_percent / count_all);
			}
		}
	}
	
	public void printInfo() {
		for (String state_name: states_ids) {
			System.out.println(state_name + " -> "
					+ StringUtils.align(states_repetition_percent.get(state_name))
					+ ", " + StringUtils.align(states_go_up_percent.get(state_name)));
		}
	}
	
	public double getNextUpProb(double[] rates) {
		int[] order = new int[patternLength];
		SequenceUtils.buildOrderList(rates, order);
		
		String state_name = orderArrayToStateName(order);
		return states_go_up_percent.get(state_name);
	}
	
	private String orderArrayToStateName(int[] array) {
		String result = "";
		for (int i=0; i<array.length; i++) {
			result += array[i];
			if (i != array.length - 1) {
				result += "_";
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		//double[] sequence = GlobalConstants.eur2usd;
		double[] sequence = HistoricalCurrenciesPairs.eur2cad;
		PatternsStatic pattern = new PatternsStatic(sequence, 10);
		pattern.printInfo();
	}
}
