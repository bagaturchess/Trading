package beginning.base.tests;



import org.junit.Test;

import beginning.base.Sequence;
import beginning.base.SequenceStat;



//
public class Test3_Trends {
	
	@Test
	public void testPeaksTrend_UP() {
		//GRAPHIC
		//4      x                          
		//3 o x o o                         
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
		double trend = seqstat.getPeaksTrend();
		
		if (trend <= 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
	
	@Test
	public void testPeaksTrend_DOWN() {
		//GRAPHIC
		//4    x                           
		//3 x o o x                       
		//2  o   o o                        
		//1                              
		double[] input = new double[] {
			3,
			2,
			3,
			4,
			3,
			2,
			3,
			2,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		double trend = seqstat.getPeaksTrend();
		
		if (trend >= 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
	
	@Test
	public void testPeaksTrend_ZERO() {
		//GRAPHIC
		//4                               
		//3 o x x                        
		//2  o o o                         
		//1                              
		double[] input = new double[] {
			3,
			2,
			3,
			2,
			3,
			2,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		double trend = seqstat.getPeaksTrend();
		
		if (trend != 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
	
	@Test
	public void testBottomsTrend_UP() {
		//GRAPHIC
		//4    o o                          
		//3 o o x o                         
		//2  x                            
		//1                              
		double[] input = new double[] {
			3,
			2,
			3,
			4,
			3,
			4,
			3,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		double trend = seqstat.getBottomsTrend();
		
		if (trend <= 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
	
	@Test
	public void testBottomsTrend_DOWN() {
		//GRAPHIC
		//4                               
		//3 o o   o                        
		//2  x o o o                         
		//1     x                         
		double[] input = new double[] {
			3,
			2,
			3,
			2,
			1,
			2,
			3,
			2,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		double trend = seqstat.getBottomsTrend();
		
		if (trend >= 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
	
	@Test
	public void testBottomsTrend_ZERO() {
		//GRAPHIC
		//4                               
		//3 o o o                        
		//2  x x o                         
		//1                              
		double[] input = new double[] {
			3,
			2,
			3,
			2,
			3,
			2,
		};
		
		Sequence seq = new Sequence(input);
		SequenceStat seqstat = new SequenceStat(seq);
		double trend = seqstat.getBottomsTrend();
		
		if (trend != 0) {
			throw new IllegalStateException("trend=" + trend);
		}
	}
}
