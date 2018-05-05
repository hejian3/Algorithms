
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
   
    private final double meanValue;
    private final double stddevValue;
    private final double confidenceLoValue;
    private final double confidenceHiValue;
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        double [] percolationArr = new double [trials];
        for (int i = 0; i < percolationArr.length; i++) {
            percolationArr[i] = getPercolationCount(n);
        }
        meanValue = StdStats.mean(percolationArr);
        stddevValue = StdStats.stddev(percolationArr);
        double d = 1.96*meanValue/Math.sqrt(percolationArr.length);
        confidenceLoValue = meanValue - d;
        confidenceHiValue = meanValue + d;
    }
    
    private double getPercolationCount(int n) {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int row = StdRandom.uniform(n)+1;
            int col = StdRandom.uniform(n)+1;
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites()/n/n;
    }
    
    public double mean() {
        return meanValue; 
    }
    
    public double stddev() {
        return stddevValue;
    }
    
    public double confidenceLo() {
        return confidenceLoValue;
    }
    
    public double confidenceHi() {
        return confidenceHiValue;
    }
    
    public static void main(String []args) {
        print(20, 10);
    }
    
    private static void print(int n, int trials) {
        PercolationStats p = new PercolationStats(n, trials);
        StdOut.printf("mean                    = %1f", p.mean());
        StdOut.println();
        StdOut.printf("stddev                  = %1f", p.stddev());
        StdOut.println();
        StdOut.println("95% confidence interval = ["+p.confidenceLo() +", "
                           + p.confidenceHi() +"]");
        StdOut.println();
    }
}