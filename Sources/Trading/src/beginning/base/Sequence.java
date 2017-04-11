package beginning.base;

public class Sequence {
	
	private Rate[] rates;
	
	public Sequence(double[] _values) {
		
		rates = new Rate[_values.length];
		
		for (int i=0; i<_values.length; i++) {
			rates[i] = new Rate(i, _values[i]);
		}
	}
	
	public Rate[] getValues() {
		return rates;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		result += "\r\n";
		result += "Rates: ";
		for (int i=0; i<rates.length; i++) {
			if (i != 0) result += ", ";
			result += rates[i];
		}
		
		return result;
	}
}
