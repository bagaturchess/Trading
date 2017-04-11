package beginning.base;

import technical.impl.input.HistoricalCurrenciesPairs;


public class Main {
	
	
	public static double[] getBottoms(Sequence seq) {
		return null;
	}
	
	public static void main(String[] args) {
		
		dump(HistoricalCurrenciesPairs.eur2usd);
		
		/*int all = 0;
		int stable = 0;
		for (int i=40; i<Const.eur2usd.length; i++) {
			double[] seqArr = getFirstSequence(i, Const.eur2usd);
			boolean stab = checkTrendStability(seqArr);
			if (stab) {
				stable++;
			}
			all++;
		}
		
		System.out.println("all=" + all + ",	stable=" + stable);*/
		
		/*
		Sequence seq = new Sequence(Const.eur2usd);
		SequenceStat seqstat = new SequenceStat(seq);
		//System.out.println(seqstat);
		seqstat.dumpUpkeepLine();
		//seqstat.dumpDownkeepLine(); 
		 */
	}
	
	public static boolean checkTrendStability(double[] sequence, double[] split) {
		
		int length = sequence.length;
		
		int sign = 0;
		int curSize = length;
		while (curSize > 1) {
			double[] curSequence = getLastSequence(curSize, sequence);
			Sequence seq = new Sequence(curSequence);
			SequenceStat seqstat = new SequenceStat(seq);
			
			curSize /= 2;
			
			if (sign == 0) {
				if (seqstat.getBottomsTrend() * seqstat.getPeaksTrend() < 0) {
					return false;
				}
				if (seqstat.getBottomsTrend() != 0) {
					if (seqstat.getBottomsTrend() < 0) {
						sign = -1;
					} else {
						sign = 1;
					}
				} else if (seqstat.getPeaksTrend() != 0) {
					if (seqstat.getPeaksTrend() < 0) {
						sign = -1;
					} else {
						sign = 1;
					}
				}
			} else {
				if (seqstat.getBottomsTrend() != sign) {
					return false;
				}
				if (seqstat.getPeaksTrend() != sign) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void dump(double[] sequence) {
		
		int length = sequence.length;
		
		int curSize = length;
		while (curSize > 1) {
			double[] curSequence = getLastSequence(curSize, sequence);
			Sequence seq = new Sequence(curSequence);
			SequenceStat seqstat = new SequenceStat(seq);
			
			System.out.println("" + curSize + ">	" + seqstat.getBottomsTrend_NORM() + "	" + seqstat.getPeaksTrend_NORM()
								+ "	" + seqstat.getDistanceToKeeprs()
								+ "	" + seqstat.getDistanceBetweenKeepLines());
			
			curSize /= 2;
		}
	}
	
	private static double[] getLastSequence(int count, double[] sequence) {
		double[] result = new double[count];
		
		int counter = 0;
		for (int i=sequence.length - 1; i>sequence.length - 1 - count; i--) {
			result[count - 1 - counter] = sequence[i];
			counter++;
		}
		
		return result;
	}
	
	/*private static double[] getFirstSequence(int count, double[] sequence) {
		double[] result = new double[count];
		
		for (int i=0; i<count; i++) {
			result[i] = sequence[i];
		}
		
		return result;
	}*/
}
