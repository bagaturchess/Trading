package business.api;

public class CurrencyType {
	
	public static final int EUR = 0;
	public static final int USD = 1;
	public static final int JPY = 2;
	public static final int BGN = 3;
	public static final int CZK = 4;
	public static final int DKK = 5;
	public static final int GBP = 6;
	public static final int HUF = 7;
	public static final int LTL = 8;
	public static final int LVL = 9;
	public static final int PLN = 10;
	public static final int RON = 11;
	public static final int SEK = 12;
	public static final int CHF = 13;
	public static final int NOK = 14;
	public static final int HRK = 15;
	public static final int RUB = 16;
	public static final int TRY = 17;
	public static final int AUD = 18;
	public static final int BRL = 19;
	public static final int CAD = 20;
	public static final int CNY = 21;
	public static final int HKD = 22;
	public static final int IDR = 23;
	public static final int ILS = 24;
	public static final int INR = 25;
	public static final int KRW = 26;
	public static final int MXN = 27;
	public static final int MYR = 28;
	public static final int NZD = 29;
	public static final int PHP = 30;
	public static final int SGD = 31;
	public static final int THB = 32;
	public static final int ZAR = 33;
	
	public static final int SIZE = 34;
	
	private static int[] all_ids = new int[] {EUR, USD, JPY, BGN, CZK, DKK, GBP, HUF, LTL, LVL, PLN, RON, SEK, CHF, NOK, HRK, RUB, TRY, AUD, BRL, CAD, CNY, HKD, IDR, ILS, INR, KRW, MXN, MYR, NZD, PHP, SGD, THB, ZAR};
	private static String[] all_names = new String[] {"EUR", "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "LTL", "LVL", "PLN", "RON", "SEK", "CHF", "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD", "CNY", "HKD", "IDR", "ILS", "INR", "KRW", "MXN", "MYR", "NZD", "PHP", "SGD", "THB", "ZAR"};
	
	static {
		
		if (SIZE != all_ids.length) {
			throw new IllegalStateException();
		}
		
		if (all_names.length != all_ids.length) {
			throw new IllegalStateException();
		}
		
		for (int i=0; i<all_ids.length; i++) {
			if (i != all_ids[i]) {
				throw new IllegalStateException();
			}
		}
	}
	
	
	public static int toID(String name) {
		for (int i=0; i<all_names.length; i++) {
			if (all_names[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	
	public static final String toString(int currencyType) {
		if (currencyType < 0 || currencyType >= all_names.length) {
			throw new IllegalStateException("currencyType=" + currencyType);
		}
		return all_names[currencyType];
	}
}
