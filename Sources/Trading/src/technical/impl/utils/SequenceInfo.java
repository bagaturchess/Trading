package technical.impl.utils;

import technical.impl.math.VarStatistic;

public class SequenceInfo {
	
	
	private VarStatistic periodLengthStat;
	private VarStatistic periodRateDiffStat;
	
	
	public SequenceInfo(VarStatistic _periodLengthStat, VarStatistic _periodRateDiffStat) {
		periodLengthStat = _periodLengthStat;
		periodRateDiffStat = _periodRateDiffStat;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		result += "COUNT  ";
		result += (int)periodLengthStat.getCount();
		result += "  ";
		result += (periodLengthStat.getEntropy() / periodLengthStat.getSTDEV());
		result += "	LENGTH  ";
		result += "  " + StringUtils.align(periodLengthStat.getEntropy());
		result += "  " + StringUtils.align(periodLengthStat.getSTDEV());
		result += "  " + StringUtils.align(periodLengthStat.getMinVal());
		result += "  " + StringUtils.align(periodLengthStat.getMaxVal());
		result += "	RATE	";
		result += (periodRateDiffStat.getEntropy() / periodRateDiffStat.getSTDEV());
		result += "  " + StringUtils.align(periodRateDiffStat.getEntropy());
		result += "  " + StringUtils.align(periodRateDiffStat.getSTDEV());
		//result += "  " + StringUtils.align(periodRateDiffStat.getMinVal());
		//result += "  " + StringUtils.align(periodRateDiffStat.getMaxVal());
		
		return result;
	}
}
