package technical.impl.prediction.trending;


import technical.impl.input.HistoricalCurrenciesPairs;
import technical.impl.math.Trend;


public class Trending {
	
	
	private int GROUPS_COUNT;
	
	private double[][] groups_up_goup;
	private double[][] groups_down_goup;
	private double[][] groups_up_all;
	private double[][] groups_down_all;
	
	//private int trendLength;
	private double[] current_rates_for_trending;
	private int last_dir = -1;
	
	
	public Trending(int goupsCount) {
		GROUPS_COUNT = goupsCount + 1;
		
		groups_up_goup = new double[2][GROUPS_COUNT];
		groups_down_goup = new double[2][GROUPS_COUNT];
		groups_up_all = new double[2][GROUPS_COUNT];
		groups_down_all = new double[2][GROUPS_COUNT];
	}
	
	
	public void add(double[] rates_for_trending) {
		double[] cur = current_rates_for_trending;
		int cur_dir = last_dir;
		
		if (cur != null) {
			double new_rate = rates_for_trending[rates_for_trending.length - 1];
			
			Trend trend = new Trend(cur);
			int lastindex = cur.length - 1;
			double up = trend.getTrendResistence().calculate(lastindex);
			double mid = trend.getTrendMiddle().calculate(lastindex);
			double down = trend.getTrendSupport().calculate(lastindex);
			
			if (up > mid && mid > down) {
				double zone_up = (up - mid);
				double zone_down = (mid - down);
				
				double last_rate = cur[cur.length - 1];
				if (last_rate > mid) {
					double deltaMid = last_rate - mid;
					int up_index = (int) (GROUPS_COUNT * (deltaMid / zone_up));
					if (up_index > groups_up_goup[cur_dir].length - 1) {
						up_index = groups_up_goup[cur_dir].length - 1;
					}
					if (new_rate > last_rate) {
						groups_up_goup[cur_dir][up_index]++;
					}
					groups_up_all[cur_dir][up_index]++;
				} else if (last_rate < mid) {
					double deltaMid = mid - last_rate;
					int down_index = (int) (GROUPS_COUNT * (deltaMid / zone_down));
					if (down_index > groups_down_goup[cur_dir].length - 1) {
						down_index = groups_down_goup[cur_dir].length - 1;
					}
					if (new_rate > last_rate) {
						groups_down_goup[cur_dir][down_index]++;
					}
					groups_down_all[cur_dir][down_index]++;
				} else {
					//TODO: consider, possibly skip this value
				}
				              
			}
		}
		
		current_rates_for_trending = rates_for_trending;
		last_dir = isDirectionUp(current_rates_for_trending) ? 0 : 1;
	}
	
	
	public Double getNextUpProb(double[] rates_for_trending) {
		
		if (current_rates_for_trending != rates_for_trending) {
			throw new IllegalStateException("current_rates_for_trending != rates_for_trending");
		}
		
		int cur_dir = last_dir;
		
		Trend trend = new Trend(rates_for_trending);
		int lastindex = rates_for_trending.length - 1;
		double up = trend.getTrendResistence().calculate(lastindex);
		double mid = trend.getTrendMiddle().calculate(lastindex);
		double down = trend.getTrendSupport().calculate(lastindex);
		
		if (up > mid && mid > down) {
			double last_rate = rates_for_trending[rates_for_trending.length - 1];
			if (last_rate > mid) {
				double deltaMid = last_rate - mid;
				double zone_up = (up - mid);
				int up_index = (int) (GROUPS_COUNT * (deltaMid / zone_up));
				if (up_index > groups_up_goup[cur_dir].length - 1) {
					up_index = groups_up_goup[cur_dir].length - 1;
				}
				if (groups_up_all[cur_dir][up_index] == 0) {
					return 0.5;
				}
				return groups_up_goup[cur_dir][up_index] / groups_up_all[cur_dir][up_index];
			} else if (last_rate < mid) {
				double deltaMid = mid - last_rate;
				double zone_down = (mid - down);
				int down_index = (int) (GROUPS_COUNT * (deltaMid / zone_down));
				if (down_index > groups_down_goup[cur_dir].length - 1) {
					down_index = groups_down_goup[cur_dir].length - 1;
				}
				if (groups_down_all[cur_dir][down_index] == 0) {
					return 0.5;
				}
				return groups_down_goup[cur_dir][down_index] / groups_down_all[cur_dir][down_index];
			} else {
				//TODO: consider, against "add" method implementation
				return 0.5;
			}
		} else {
			return null;
		}
	}
	
	
	private static boolean isDirectionUp(double[] rates) {
		
		Trend trend = new Trend(rates);
		int lastindex = rates.length - 1;
		double last_rate = rates[lastindex];
		
		double up = trend.getTrendResistence().calculate(lastindex);
		double down = trend.getTrendSupport().calculate(lastindex);
		
		if (last_rate >= up) {
			double max_up = last_rate;
			for (int i = lastindex; i>=0; i--) {
				if (rates[i] <= trend.getTrendResistence().calculate(i)) {
					break;
				}
				if (rates[i] > max_up) {
					max_up = rates[i];
				}
			}
			
			if (last_rate < max_up) {
				return false;
			} else {
				return true;
			}
		} else if (last_rate > down) { //last_rate < up && last_rate > down
			for (int i = lastindex; i>=0; i--) {
				if (rates[i] >= trend.getTrendResistence().calculate(i)) {
					return false;
				}
				if (rates[i] <= trend.getTrendSupport().calculate(i)) {
					return true;
				}
			}
		} else { // last_rate <= down
			double min_down = last_rate;
			for (int i = lastindex; i>=0; i--) {
				if (rates[i] >= trend.getTrendSupport().calculate(i)) {
					break;
				}
				if (rates[i] < min_down) {
					min_down = rates[i];
				}
			}
			
			if (last_rate > min_down) {
				return true;
			} else {
				return false;
			}
		}
		
		return true;
		//throw new IllegalStateException();
	}
	
	
	public void printInfo() {
		String result = "";
		
		result += "************************************** GO UP *************************************\r\n";
		result += "UP SIDE:\r\n";
		for (int i=0; i<GROUPS_COUNT; i++) {
			if (groups_up_all[0][i] == 0) {
				result += "0	0\r\n";
			} else {
				result += groups_up_all[0][i] + "	" + groups_up_goup[0][i] / groups_up_all[0][i] + "\r\n";
			}
		}
		
		result += "\r\nDOWN SIDE:\r\n";
		for (int i=0; i<GROUPS_COUNT; i++) {
			if (groups_down_all[0][i] == 0) {
				result += "0	0\r\n";
			} else {
				result += groups_down_all[0][i] + "	" + groups_down_goup[0][i] / groups_down_all[0][i] + "\r\n";
			}
		}
		
		result += "\r\n************************************** GO DOWN *************************************\r\n";
		result += "UP SIDE:\r\n";
		for (int i=0; i<GROUPS_COUNT; i++) {
			if (groups_up_all[1][i] == 0) {
				result += "0\r\n";
			} else {
				result += groups_up_all[0][i] + "	" + groups_up_goup[1][i] / groups_up_all[1][i] + "\r\n";
			}
		}
		
		result += "\r\nDOWN SIDE:\r\n";
		for (int i=0; i<GROUPS_COUNT; i++) {
			if (groups_down_all[1][i] == 0) {
				result += "0	0\r\n";
			} else {
				result += groups_down_all[0][i] + "	" + groups_down_goup[1][i] / groups_down_all[1][i] + "\r\n";
			}
		}
		
		System.out.println(result);
	}
	
	
	public static void main(String[] args) {
		
		int count = 100;
		Trending trending = new Trending(1);
		
		double[] sequence = HistoricalCurrenciesPairs.eur2cad;
		
		for (int i=0; i<sequence.length - count - 1; i++) {
			double[] rates = new double[count];
			System.arraycopy(sequence, i, rates, 0, count);
			trending.add(rates);
		}

		trending.printInfo();
	}
}
