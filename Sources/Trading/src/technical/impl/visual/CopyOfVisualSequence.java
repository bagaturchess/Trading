package technical.impl.visual;


import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import technical.impl.math.Point2D;
import technical.impl.utils.SequenceUtils;


public class CopyOfVisualSequence extends JFrame {
	
	
	private static final long serialVersionUID = 5646317753424799606L;
	
	
	private int WEIGHT = 1100;
	private int HEIGHT = 600;
	
	private double yscale;
	
	private List<Point2D> points;
	
	private double min_x = Double.MAX_VALUE;
	private double max_x = Double.MIN_VALUE;
	private double min_y = Double.MAX_VALUE;
	private double max_y = Double.MIN_VALUE;
	private double scale_x;
	private double scale_y;
	private double panel_usage_percent = 0.9;
	
	private List<List<Point2D>> lines;
	private List<Color> lines_colours;
	
	
	public CopyOfVisualSequence(double _yscale) {
		
		yscale = _yscale;
		
		setSize(WEIGHT, HEIGHT);
		setLocation(50, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DrawPanel dp = new DrawPanel();
		Container cp = this.getContentPane();
		cp.add(dp);
		lines = new ArrayList<List<Point2D>>();
		lines_colours = new ArrayList<Color>();
		
		System.out.println("VISUAL Y SCALE FUNCTION: F(2)=" + scaleYFunction(2) + ", F(1/2)=" + scaleYFunction(1/(double)2));
	}
	
	private double scaleYFunction(double Y) {
		return Math.pow(Y, yscale);
	}
	
	public void addLine(List<Point2D> line, Color colour) {
		lines.add(line);
		lines_colours.add(colour);
	}
	
	public void setSequence(double[] sequence) {
		this.points = SequenceUtils.buildPointsList(sequence);
		
		for (Point2D point: points) {
			if (point.getX() > max_x) {
				max_x = point.getX();
			}
			if (point.getX() < min_x) {
				min_x = point.getX();
			}
			if (scaleYFunction(point.getY()) > max_y) {
				max_y = scaleYFunction(point.getY());
			}
			if (scaleYFunction(point.getY()) < min_y) {
				min_y = scaleYFunction(point.getY());
			}
		}
		
		scale_x = (WEIGHT / (max_x)) * panel_usage_percent;
		scale_y = (HEIGHT / (max_y)) * panel_usage_percent;
		//System.out.println("scale_x=" + scale_x + ", scale_y=" + scale_y);
	}
	
	public void visualize() {
		this.setVisible(true);
	}
	
	// The panel that will show the CHull class in action
	public class DrawPanel extends JPanel {
		
		private static final long serialVersionUID = -4917734981326246173L;
		
		int left_shift = 10;
		
		public DrawPanel() {
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//List<Point2D> array = points;
			
			Graphics2D g2 = (Graphics2D) g;
			
			//Visualize points
			int rad = 3;
			for (Point2D point: points) {
				g2.draw(new Ellipse2D.Double(
						left_shift + ((int) (scale_x * point.getX())) - rad/2,
						((int) (scale_y * scaleYFunction(point.getY()))) - rad/2,
						rad, rad));
			}
			
			//List<Point2D> chull = buildCHull(array);
			
			//Visualize lines of convex hull
			g2.setColor(new Color(0, 0, 255));
			paintSequence(g2, points);
			
			for (int i=0; i<lines.size(); i++) {
				List<Point2D> cur_line = lines.get(i);
				Color cur_colour = lines_colours.get(i);
				
				g2.setColor(cur_colour);
				paintSequence(g2, cur_line);
			}
			
			//curr = points.get(0);
			//g2.drawLine(WEIGHT - (int) (scale_x * prev.getX()), (int) (scale_y * prev.getY()),
			//		WEIGHT - (int) (scale_x * curr.getX()), (int) (scale_y * curr.getY()));
		}

		private void paintSequence(Graphics2D g2, List<Point2D> points) {
			int counter = 0;
			Iterator<Point2D> itr = points.iterator();
			Point2D prev = itr.next();
			Point2D curr = null;
			while (itr.hasNext()) {
				curr = itr.next();
				g2.drawLine(left_shift + /*(int) (scale_distance * WEIGHT) - */ (int) (scale_x * prev.getX()),
						(int) (scale_y * scaleYFunction(prev.getY())),
						left_shift + /*(int) (scale_distance * WEIGHT) -*/ (int) (scale_x * curr.getX()),
						(int) (scale_y * scaleYFunction(curr.getY())));
				prev = curr;
				counter++;
				//if (counter > 1) {
				//	break;
				//}
			}
		}
	}
}
