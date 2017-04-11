package business.impl.emulator;


import java.awt.Color;

import technical.impl.visual.VisualSequence;
import business.impl.players.Player_Prediction;
import business.impl.players.Player_Trending;
import business.impl.players.Player_Trending_ByMiddleTrend_MultipliedBySupportResistence;
import business.impl.players.Player_Trending_ByMiddleTrend;
import business.impl.players.Player_Trending_ByMiddleTrend_test1;


public class AssessPlayers {
	
	
	public static void main(String[] args) {
		try {
			
			VisualSequence visual = new VisualSequence(1);
			
			//PlayerAssessor assessor = new PlayerAssessor(Player_Prediction.class, new Object[0]);
			//PlayerAssessor assessor1 = new PlayerAssessor(Player_Trending.class, new Object[0]);
			PlayerAssessor assessor1 = new PlayerAssessor(Player_Trending_ByMiddleTrend_test1.class, new Object[0]);
			PlayerAssessor assessor2 = new PlayerAssessor(Player_Trending_ByMiddleTrend_MultipliedBySupportResistence.class, new Object[0]);
			//PlayerAssessor assessor3 = new PlayerAssessor(Player_Trending_ByMiddleTrend.class, new Object[0]);
			
			assessor1.execute();
			assessor2.execute();
			//assessor3.execute();
			
			visual.addLine(assessor1.getFinalResult().getBalances(), new Color(255, 0, 0), true, false);
			System.out.println(assessor1.getFinalResult().getWaste());
			
			visual.addLine(assessor2.getFinalResult().getBalances(), new Color(0, 255, 0), true, false);
			System.out.println(assessor2.getFinalResult().getWaste());

			//visual.addLine(assessor3.getFinalResult().getBalances(), new Color(0, 0, 255), true, false);
			//System.out.println(assessor3.getFinalResult().getWaste());

			
			visual.visualize();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
