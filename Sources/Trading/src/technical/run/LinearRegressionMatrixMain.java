package technical.run;

import technical.impl.math.LinearRegressionMatrix;




public class LinearRegressionMatrixMain {

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {
    double[] y = {
        4.086543006, 4.345284605, 4.697843221, 4.945264478, 5.218586841,
        5.551340493, 5.889181921, 6.179613335, 6.497139087, 6.759557527,
        7.058689287, 7.378774761, 7.648870268, 7.932156476, 8.282189704,
        8.588736994, 8.804612074, 9.12992595,  9.470309804, 9.780552876
    };

    double[][] x = {
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
          1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        },
        { 0,  0.1 , 0.2 , 0.3 , 0.4, 0.5 , 0.6 , 0.7 , 0.8 , 0.9,
          1,  1.1 , 1.2 , 1.3 , 1.4, 1.5 , 1.6 , 1.7 , 1.8 , 1.9,
        },
    };

    double[] weight = {
        1,1,1,1,1,
        1,1,1,1,1,
        1,1,1,1,1,
        1,1,1,1,1,
    };
    
    System.out.println("With 2 coefficients");
    
    LinearRegressionMatrix lr = new LinearRegressionMatrix();
    boolean works = lr.regress(x[1], y);
    //boolean works = lr.regress(y, x, weight);
    
    double[] coef = lr.getCoefficients();
    System.out.println("works=" + works);
    System.out.println("Coeffecient 0: " + coef[0]);
    System.out.println("Coeffecient 1: " + coef[1]);
    System.out.println("Y = " + coef[0] + " + " + coef[1] + " x");
    System.out.println("-------------------------");

    System.out.println("i,weight,x,y,estimated_y");
    System.out.println("=========================");
    for (int i=0;i<y.length;i++) {
        //
        double estimated_y = coef[0] + coef[1] * x[1][i];
        System.out.println(
            i + "," + weight[i] + "," + x[1][i] + "," + y[i] + "," +
            estimated_y
        );
    }
}//end Main

}//end class
