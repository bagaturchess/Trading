package technical.impl.prediction.continuation;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.utils.SequenceUtils;
import technical.impl.utils.StringUtils;


public class PatternsAdaptive {
	
	
	private int patternLength;
	
	private int totalRepetitionCount = 0;
	
	private Set<String> states_ids;
	private Map<String, Double> states_repetition_count;
	private Map<String, Boolean> states_go_up_flag;
	private Map<String, Map<String, Double>> states_transitions;
	private Map<String, Double> states_transitions_count;
	
	private String last_state_name;
	
	//Temp data structures
	private int[] temp_order;
	
	
	public PatternsAdaptive(int _patternLength) {
		
		patternLength = _patternLength;
		
		//Init arrays
		states_ids = new HashSet<String>();
		states_repetition_count = new HashMap<String, Double>();
		states_transitions = new HashMap<String, Map<String,Double>>();
		states_transitions_count = new HashMap<String, Double>();
		states_go_up_flag = new HashMap<String, Boolean>();
		//states_go_up_percent = new HashMap<String, Double>();
		
		temp_order = new int[patternLength];
	}

	
	public void add(double[] to_rates) {
		
		//Build order and generate id
		//SequenceUtils.buildOrderList(from_rates, temp_order);
		String from_state_name = last_state_name;//orderArrayToStateName(temp_order);
		
		//Set go up flag
		if (temp_order[temp_order.length - 1] > temp_order[temp_order.length - 2]) {
			states_go_up_flag.put(from_state_name, true);
		} else {
			states_go_up_flag.put(from_state_name, false);
		}
		
		//Build order and generate id
		SequenceUtils.buildOrderList(to_rates, temp_order);
		String to_state_name = orderArrayToStateName(temp_order);
		
		//Set go up flag
		if (temp_order[temp_order.length - 1] > temp_order[temp_order.length - 2]) {
			states_go_up_flag.put(to_state_name, true);
		} else {
			states_go_up_flag.put(to_state_name, false);
		}
		
		//Put id and repetitions
		totalRepetitionCount++;
		if (!states_ids.contains(from_state_name)) {
			states_ids.add(from_state_name);
			states_repetition_count.put(from_state_name, 1D);
		} else {
			states_repetition_count.put(from_state_name, states_repetition_count.get(from_state_name) + 1);
		}
		
		//Transitions
		Map<String, Double> to_list = states_transitions.get(from_state_name);
		if (to_list == null) {
			to_list = new HashMap<String, Double>();
			states_transitions.put(from_state_name, to_list);
		}
		
		Double count_to = to_list.get(to_state_name);
		if (count_to == null) {
			to_list.put(to_state_name, 1D);
		} else {
			to_list.put(to_state_name, count_to + 1);
		}
		
		Double count_all = states_transitions_count.get(from_state_name);
		if (count_all == null) {
			states_transitions_count.put(from_state_name, 1D);
		} else {
			states_transitions_count.put(from_state_name, count_all + 1);
		}
		
		last_state_name = to_state_name;
	}
	
	
	private Double getGoUpProb(String from_state_name) {
		
		//Sum go up probs for all to states

		Map<String, Double> to_states_list = states_transitions.get(from_state_name);
		
		if (to_states_list == null) {
			return null;
		}
		
		double go_up_percent = 0;
		Set<String> to_states = to_states_list.keySet();
		for (String to_state_name: to_states) {
			if (states_go_up_flag.get(to_state_name)) {
				go_up_percent += to_states_list.get(to_state_name);
			}
		}
		
		Double count_all = states_transitions_count.get(from_state_name);
		if (count_all <= 0) {
			throw new IllegalStateException("count_all=" + count_all);
		}
		
		return go_up_percent / count_all;
	}
	
	public Double getNextUpProb(double[] rates) {
		int[] order = new int[patternLength];
		SequenceUtils.buildOrderList(rates, order);
		String state_name = orderArrayToStateName(order);
		return getGoUpProb(state_name);
	}
	
	public void printInfo() {
		for (String state_name: states_ids) {
			System.out.println(state_name + " -> "
					+ StringUtils.align(states_repetition_count.get(state_name))
					+ ", " + StringUtils.align(getGoUpProb(state_name)));
		}
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
		
		int count = 5;
		PatternsAdaptive patterns = new PatternsAdaptive(count);
		
		double[] sequence = HistoricalCurrenciesPairs.eur2cad;
		for (int i=0; i<sequence.length - count - 1; i++) {
			double[] rates_old = new double[count];
			System.arraycopy(sequence, i, rates_old, 0, count);
			double[] rates_new = new double[count];
			System.arraycopy(sequence, i + 1, rates_new, 0, count);
			
			patterns.add(rates_new);
		}

		patterns.printInfo();
	}
}
