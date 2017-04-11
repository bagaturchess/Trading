package business.api;

import technical.impl.math.VarStatistic;

public class CurrencyPairMetadataFactory {
	
	
	public static final ICurrencyPairMetadata create(int _fromCurrencyType, int _toCurrencyType, double _spread, double[] _ratesData) {
		return new CurrencyMetadata(_fromCurrencyType, _toCurrencyType, _spread, _ratesData);
	}
	
	
	private static class CurrencyMetadata implements ICurrencyPairMetadata {
		
		private int fromCurrencyType;
		private int toCurrencyType;
		private double spread;
		private double[] ratesData;
		private VarStatistic stat;
		
		
		private CurrencyMetadata(int _fromCurrencyType, int _toCurrencyType, double _spread, double[] _ratesData) {
			
			if (_ratesData.length < 10) {
				throw new IllegalStateException("_sequence=" + _ratesData.length);
			}
			
			if (_spread <= 0) {
				throw new IllegalStateException("_spread=" + _spread);
			}
			
			fromCurrencyType = _fromCurrencyType;
			toCurrencyType = _toCurrencyType;
			spread = _spread;
			ratesData = _ratesData;
			spread = _spread;
			
			stat = buildMetaData();
		}
		
		
		@Override
		public int getFromCurrencyType() {
			return fromCurrencyType;
		}

		
		@Override
		public int getToCurrencyType() {
			return toCurrencyType;
		}
		
		
		@Override
		public double getSpread() {
			return spread;
		}

		
		@Override
		public double[] getRatesData() {
			return ratesData;
		}
		
		
		@Override
		public double getMeaningfulIntervalLength_Min() {
			return stat.getMinVal();
		}


		@Override
		public double getMeaningfulIntervalLength_Max() {
			return stat.getMaxVal();
		}


		@Override
		public double getMeaningfulIntervalLength_Avg() {
			return stat.getEntropy();
		}


		@Override
		public double getMeaningfulIntervalLength_Stdev() {
			return stat.getSTDEV();
		}
		
		
		@Override
		public String toString() {
			String result = "";
			result += "SPREAD    : " + getSpread() + "\r\n";
			result += "MIL_MIN   : " + getMeaningfulIntervalLength_Min() + "\r\n";
			result += "MIL_MAX   : " + getMeaningfulIntervalLength_Max() + "\r\n";
			result += "MIL_AVG   : " + getMeaningfulIntervalLength_Avg() + "\r\n";
			result += "MIL_STDEV : " + getMeaningfulIntervalLength_Stdev() + "\r\n";
			return result;
		}
		
		
		private VarStatistic buildMetaData() {
			
			VarStatistic stat = new VarStatistic(false);
			
			for (int i=0; i<ratesData.length - 1; i++) {
				double rate1 = ratesData[i];
				for (int j=i+1; j<ratesData.length; j++) {
					double rate2 = ratesData[j];
					double curDelta = Math.abs(rate1 - rate2);
					if (curDelta >= spread) {
						int deltaLength = j - i;
						stat.addValue(deltaLength, deltaLength);
						break;
					}
				}
			}
			
			return stat;
		}
	}
}
