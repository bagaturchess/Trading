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
import technical.impl.utils.LineUtils;


public class VisualSequence extends JFrame {
	
	
	private static final long serialVersionUID = 5646317753424799606L;
	
	private int WEIGHT_FRAME = 1100;
	private int HEIGHT_FRAME = 600;
	
	private int WEIGHT_PANEL;
	private int HEIGHT_PANEL;
	
	private double yscale;
	
	private double min_x = Double.MAX_VALUE;
	private double max_x = Double.MIN_VALUE;
	private double min_y = Double.MAX_VALUE;
	private double max_y = Double.MIN_VALUE;
	
	private double scale_x;
	private double scale_y;
	private double shift_y;
	
	private double panel_usage_percent = 0.97;
	
	private List<List<Point2D>> lines;
	private List<Color> lines_colours;
	private List<Boolean> lines_ellipsePoints;
	
	
	public VisualSequence(double _yscale) {
		
		yscale = _yscale;
		
		setSize(WEIGHT_FRAME, HEIGHT_FRAME);
		setLocation(50, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DrawPanel dp = new DrawPanel();
		Container cp = this.getContentPane();
		cp.add(dp);
		
		lines = new ArrayList<List<Point2D>>();
		lines_colours = new ArrayList<Color>();
		lines_ellipsePoints = new ArrayList<Boolean>();
		
		System.out.println("VISUAL Y SCALE FUNCTION: F(2)=" + scaleYFunction(2) + ", F(1/2)=" + scaleYFunction(1/(double)2));
	}
	
	private double scaleYFunction(double Y) {
		double result = Math.pow(Y, yscale);
		if (Y < 0) {
			if (result > 0) {
				result = -result;
			}
		}
		return result;
	}
	
	public void addLine(List<Double> sequence, Color colour, Boolean ellipsePoint, boolean fake) {
		addLine(LineUtils.lines_FromSequence(sequence), colour, ellipsePoint);
	}
	
	public void addLine(double[] sequence, Color colour, Boolean ellipsePoint) {
		addLine(LineUtils.lines_FromSequence(sequence), colour, ellipsePoint);
	}
	
	public void addLine(List<Point2D> sequence, Color colour, Boolean ellipsePoint) {
		lines.add(sequence);
		lines_colours.add(colour);
		lines_ellipsePoints.add(ellipsePoint);
	}
	
	private void setupVisualParams(List<Point2D> points) {
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
		
		double delta_y = 0;
		if (min_y < 0) {
			delta_y = max_y - min_y;
			shift_y = (HEIGHT_PANEL * panel_usage_percent) * (Math.abs(min_y) / delta_y);
		} else {
			delta_y = max_y;
			shift_y = 0;
		}
		
		
		scale_x = ((WEIGHT_PANEL * panel_usage_percent) / (max_x));
		scale_y = ((HEIGHT_PANEL * panel_usage_percent) / (delta_y));
	}
	
	public void visualize() {
		this.setVisible(true);
	}
	
	
	public class DrawPanel extends JPanel {
		
		private static final long serialVersionUID = -4917734981326246173L;
		
		private boolean paramsOK = false;
		
		public DrawPanel() {
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (!paramsOK) {
				WEIGHT_PANEL = (int) getSize().getWidth();
				HEIGHT_PANEL = (int) getSize().getHeight();
				//System.out.println("PANEL: " + WEIGHT_PANEL + ", " + HEIGHT_PANEL);
				
				for (int i=0; i<lines.size(); i++) {
					setupVisualParams(lines.get(i));
				}
				
				List<Point2D> points = new ArrayList<Point2D>();
				points.add(new Point2D(0, 0));
				points.add(new Point2D(max_x, 0));
				
				lines.add(points);
				lines_colours.add(new Color(0, 0, 0));
				lines_ellipsePoints.add(true);
				
				paramsOK = true;
			}
			
			Graphics2D g2 = (Graphics2D) g;
			
			for (int i=0; i<lines.size(); i++) {
				List<Point2D> cur_line = lines.get(i);
				Color cur_colour = lines_colours.get(i);
				Boolean ellipsePoint = lines_ellipsePoints.get(i);
				g2.setColor(cur_colour);
				paintSequence(g2, cur_line, ellipsePoint);
			}
		}

		private void paintSequence(Graphics2D g2, List<Point2D> points, Boolean ellipsePoint) {
			
			int x_visual_shift = (int) ((WEIGHT_PANEL * (1 - panel_usage_percent)) / 2);
			int y_visual_shift = (int) ((HEIGHT_PANEL * (1 - panel_usage_percent)) / 2);
			
			int counter = 0;
			Iterator<Point2D> itr = points.iterator();
			Point2D prev = itr.next();
			Point2D curr = null;
			int rad = 3;
			while (itr.hasNext()) {
				curr = itr.next();
				
				g2.drawLine(x_visual_shift + /*(int) (scale_distance * WEIGHT_PANEL) - */ (int) (scale_x * prev.getX()),
						y_visual_shift + (int) (shift_y + scale_y * scaleYFunction(prev.getY())),
						x_visual_shift + /*(int) (scale_distance * WEIGHT_PANEL) -*/ (int) (scale_x * curr.getX()),
						y_visual_shift + (int) (shift_y + scale_y * scaleYFunction(curr.getY())));
				
				//Visualize points
				if (ellipsePoint) {
					g2.draw(new Ellipse2D.Double(
							x_visual_shift + ((int) (scale_x * prev.getX())) - rad/2,
							y_visual_shift + ((int) (shift_y + scale_y * scaleYFunction(prev.getY()))) - rad/2,
							rad, rad));
					g2.draw(new Ellipse2D.Double(
							x_visual_shift + ((int) (scale_x * curr.getX())) - rad/2,
							y_visual_shift + ((int) (shift_y + scale_y * scaleYFunction(curr.getY()))) - rad/2,
							rad, rad));
				}
				
				prev = curr;
				counter++;
				//if (counter > 1) {
				//	break;
				//}
			}
		}
	}
}
