package beginning.base;

public class Rate {
	
	private double id;
	private double value;
	
	public Rate(double _id, double _value) {
		id = _id;
		value = _value;
	}
	
	public double getID() {
		return id;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "[" + id + ", " + value + "]";
		return result;
	}
}
