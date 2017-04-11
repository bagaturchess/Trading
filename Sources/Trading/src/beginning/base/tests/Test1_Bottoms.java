package beginning.base.tests;


import org.junit.Test;

import beginning.base.Rate;
import beginning.base.Sequence;
import beginning.base.SequenceStat;


public class Test1_Bottoms {
	@Test
	public void testBottoms_1() {
		//GRAPHIC
		//4      o                          
		//3 o o o x                         
		//2  x x                           
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
		Rate[] bottoms = seqstat.getBottoms();
		
		if (bottoms.length != 3) {
			throw new IllegalStateException("bottoms.length=" + bottoms.length);
		}
		
		if (bottoms[0].getID() != 1) {
			throw new IllegalStateException("bottoms[0].getID()=" + bottoms[0].getID());
		}
		
		if (bottoms[0].getValue() != 2) {
			throw new IllegalStateException("bottoms[0].getValue()=" + bottoms[0].getValue());
		}
		
		if (bottoms[1].getID() != 3) {
			throw new IllegalStateException("bottoms[1].getID()=" + bottoms[1].getID());
		}
		
		if (bottoms[1].getValue() != 2) {
			throw new IllegalStateException("bottoms[1].getValue()=" + bottoms[1].getValue());
		}
		
		if (bottoms[2].getID() != 6) {
			throw new IllegalStateException("bottoms[3].getID()=" + bottoms[3].getID());
		}
		
		if (bottoms[2].getValue() != 3) {
			throw new IllegalStateException("bottoms[3].getValue()=" + bottoms[3].getValue());
		}
	}
}
