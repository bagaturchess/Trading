package beginning.base.tests;


import org.junit.Test;

import beginning.base.Rate;
import beginning.base.Sequence;
import beginning.base.SequenceStat;


public class Test2_Peaks {
	@Test
	public void testPeaks_1() {
		//GRAPHIC
		//4      x                          
		//3 x x o o                         
		//2  o o                           
		//1                              
		double[] input = new double[] {
			3,
			2,
			3,
			2,
			3,
			4,
			3,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		Rate[] peaks = seqstat.getPeaks();
		
		if (peaks.length != 3) {
			throw new IllegalStateException("peaks=" + peaks.length);
		}
		
		if (peaks[0].getID() != 0) {
			throw new IllegalStateException("peaks[0].getID()=" + peaks[0].getID());
		}
		
		if (peaks[0].getValue() != 3) {
			throw new IllegalStateException("peaks[0].getValue()=" + peaks[0].getValue());
		}
		
		if (peaks[1].getID() != 2) {
			throw new IllegalStateException("peaks[1].getID()=" + peaks[1].getID());
		}
		
		if (peaks[1].getValue() != 3) {
			throw new IllegalStateException("peaks[1].getValue()=" + peaks[1].getValue());
		}
		
		if (peaks[2].getID() != 5) {
			throw new IllegalStateException("peaks[2].getID()=" + peaks[2].getID());
		}
		
		if (peaks[2].getValue() != 4) {
			throw new IllegalStateException("peaks[2].getValue()=" + peaks[2].getValue());
		}
	}
}
