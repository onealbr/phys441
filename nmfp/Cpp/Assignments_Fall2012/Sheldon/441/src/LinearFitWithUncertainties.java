import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;


public class LinearFitWithUncertainties {
	public static void main(String[] args){
		double[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		double[] y = {1.6711, 2.00994, 2.26241, 2.18851, 2.33006, 2.42660, 2.48424, 2.63729, 2.77163, 2.89610, 2.89083, 3.08081, 3.05305, 3.24079, 3.36212};
		double[] sigma = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
		
		double y_sum = 0.0000;
		double x_sum = 0.0000;
		double xy_sum = 0.0000;
		double x_squared_sum = 0.0000;
		double s = 0;
		double sigmaterm = 0;
		double sx = 0;
		double sy = 0;
		double sxy = 0;
		double sxx = 0;
		double y_bar = 0;
		double x_bar = 0;
		double xx_bar = 0;
		double xy_bar = 0;
		double yy_bar = 0;
		int n = 0;
		
		//summations
		for(int i = 0; i < y.length; i++){
			sigmaterm = 1 / (sigma[i] * sigma[i]);
			s += sigmaterm;
			sx += x[i] * sigmaterm;
			sy += y[i] * sigmaterm;
			sxy += x[i] * y[i] * sigmaterm;
			sxx += x[i] * x[i] * sigmaterm;
			y_sum += y[i];
			x_sum += x[i];
			xy_sum += (x[i] * y[i]);
			x_squared_sum += (x[i] * x[i]);
			n++;
		}
		double denominator = s * sxx - sx * sx;
		
		/*System.out.println(x_sum);
		System.out.println(y_sum);
		System.out.println(xy_sum);
		System.out.println(x_sum * y_sum);
		System.out.println(x_squared_sum);
		*/

		double slope = (s * sxy - sx * sy) / denominator;
		double y_int = (sxx * sy - sx * sxy) / denominator;
		
		//error for slope and intercept;
		double intercept_e = Math.sqrt(sxx / denominator);
		double slope_e = Math.sqrt(s / denominator);
		double y_line[] = new double[15];
		
		System.out.println("Error on the slope: " +intercept_e);
		System.out.println("Error on the intercept: " +slope_e);
		
		for(int i = 0; i < y_line.length; i++){
			y_line[i] = (slope * x[i]) + y_int;
		}
		
		y_bar = y_sum / n;
		x_bar = x_sum / n;
		

		//statistical analysis
		for(int i = 0; i < y.length; i++){
			xx_bar += (x[i] - x_bar) * (x[i] - x_bar);
			xy_bar += (x[i] - x_bar) * (y[i] - y_bar);
			yy_bar += (y[i] - y_bar) * (y[i] - y_bar);
		}
		
		int df = n - 2;
		double residualss = 0;
		double regression = 0;
		
		for (int i = 0; i < n; i++){
			double line = slope * x[i] + y_int;
			residualss += (line - y[i]) * (line - y[i]);
			regression += (line - y_bar) * (line - y_bar);
		}
		
		double r_squared = regression / yy_bar;
		double slope_error = Math.sqrt((residualss / df) / xx_bar);
		double y_int_error = Math.sqrt((residualss / df) / n + (x_bar * x_bar * slope_error));
		
		System.out.println("y = " +slope+ "x + " +y_int);
		System.out.println("r^2 = " +r_squared);
		System.out.println("Standard error on slope: " +slope_error);
		System.out.println("Standard error on y-intercept: " +y_int_error);
		System.out.println("Regression Sum of Squares: " +regression);
		System.out.println("Sum of Squares Error: " +residualss);
		System.out.println("Sum of Squares Total: " +yy_bar);
		
		
		Plot2DPanel plot = new Plot2DPanel();
		plot.addScatterPlot("Raw Data", x, y);
		plot.addLinePlot("Linear Regression", x, y_line);
		
		JFrame frame = new JFrame("y = " + slope +"x + " +y_int);
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
}