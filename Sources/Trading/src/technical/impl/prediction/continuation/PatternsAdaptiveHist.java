package technical.impl.prediction.continuation;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.utils.SequenceUtils;
import technical.impl.utils.StringUtils;


public class PatternsAdaptiveHist {
	
	
	private int patternLength;
	
	private int totalRepetitionCount = 0;
	
	private Set<String> states_ids;
	private Map<String, Double> states_repetition_count;
	private Map<String, Boolean> states_go_up_flag;
	private Map<String, Map<String, Double>> states_transitions;
	private Map<String, Double> states_transitions_count;
	private LinkedList<String> history;
	private Map<String, Double> history_counts;
	private double history_size_percent;
	private int history_size;
	
	private String last_state_name;
	
	//Temp data structures
	private int[] temp_order;
	
	
	public PatternsAdaptiveHist(int _patternLength, double _history_size_percent) {
		
		patternLength = _patternLength;
		history_size_percent = _history_size_percent;
		
		//Init arrays
		states_ids = new HashSet<String>();
		states_repetition_count = new HashMap<String, Double>();
		states_transitions = new HashMap<String, Map<String,Double>>();
		states_transitions_count = new HashMap<String, Double>();
		states_go_up_flag = new HashMap<String, Boolean>();
		
		history = new LinkedList<String>();
		history_counts = new HashMap<String, Double>();
		
		temp_order = new int[patternLength];
	}
	
	
	public void add(double[] to_rates) {
		
		
		//Build order and generate id
		SequenceUtils.buildOrderList(to_rates, temp_order);
		String to_state_name = orderArrayToStateName(temp_order);
		
		
		//Set go up flag
		if (!states_go_up_flag.containsKey(to_state_name)) {
			if (temp_order[temp_order.length - 1] > temp_order[temp_order.length - 2]) {
				states_go_up_flag.put(to_state_name, true);
			} else {
				states_go_up_flag.put(to_state_name, false);
			}
		}
		
		
		//Put id and repetitions
		totalRepetitionCount++;
		history_size = (int) (history_size_percent * totalRepetitionCount);
		if (!states_ids.contains(to_state_name)) {
			states_ids.add(to_state_name);
			states_repetition_count.put(to_state_name, 1D);
		} else {
			states_repetition_count.put(to_state_name, states_repetition_count.get(to_state_name) + 1);
		}
		
		
		//Transitions
		String from_state_name = last_state_name;
		
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
		
		
		//keep last state name and update history
		last_state_name = to_state_name;
		
		history.addLast(to_state_name);
		Double cur_hist_count = history_counts.get(to_state_name);
		if (cur_hist_count == null) {
			history_counts.put(to_state_name, 1D);
		} else {
			history_counts.put(to_state_name, cur_hist_count + 1D);
		}
		if (history.size() > history_size) {
			String removed_state_name = history.removeFirst();
			double removed_count = history_counts.get(removed_state_name);
			if (removed_count == 1D) {
				history_counts.remove(removed_state_name);
			} else {
				history_counts.put(removed_state_name, removed_count - 1D);
			}
		}
	}
	
	
	public Double getNextUpProb(double[] rates) {
		int[] order = new int[patternLength];
		SequenceUtils.buildOrderList(rates, order);
		String state_name = orderArrayToStateName(order);
		return getGoUpProb(state_name);
	}
	
	
	private Double getGoUpProb(String from_state_name) {
		
		//Sum go up probs for all to states
		
		Map<String, Double> to_states_list = states_transitions.get(from_state_name);
		
		if (to_states_list == null) {
			return null;
		}
		
		Double count_all_tos = states_transitions_count.get(from_state_name);
		if (count_all_tos <= 0) {
			throw new IllegalStateException("count_all_tos=" + count_all_tos);
		}
		
		double prob_go_up_total = 0;
		Set<String> to_states = to_states_list.keySet();
		for (String to_state_name: to_states) {
			if (states_go_up_flag.get(to_state_name)) {
				
				double cur_prob_go_up_expected = to_states_list.get(to_state_name) / count_all_tos;
				
				Double occurences_actual = history_counts.get(to_state_name);
				if (occurences_actual != null) {
				
					Double occurences_expected = states_repetition_count.get(to_state_name);
					
					//double factor = ((occurences_expected * totalRepetitionCount) + (occurences_actual * history.size())) / (totalRepetitionCount + history.size());
					double total_occurence_percent = occurences_expected / totalRepetitionCount;
					double local_occurence_percent = occurences_actual / history.size();
					
					double factor = total_occurence_percent / local_occurence_percent;//(occurences_actual / history.size()); 
					
					cur_prob_go_up_expected = factor * cur_prob_go_up_expected;
				}
				
				prob_go_up_total += cur_prob_go_up_expected;	
			}
		}
		
		if (prob_go_up_total > 1 || prob_go_up_total < 0) {
			//throw new IllegalStateException("prob_go_up_total=" + prob_go_up_total);
			prob_go_up_total = 1;
		}
		
		return prob_go_up_total;
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
		PatternsAdaptiveHist patterns = new PatternsAdaptiveHist(count, 30);
		
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
