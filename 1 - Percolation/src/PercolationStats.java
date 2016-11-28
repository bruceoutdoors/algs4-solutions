import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private Percolation perc;
    private int t;
    private int total;
    private int opened;
    private double[] fractionArray;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException("N or T input invalid");
        
        total = n * n;
        this.t = t;
        fractionArray = new double[t];
        for (int i = 0; i < t; i++) {
            opened = 0;
            perc = new Percolation(n);
            int row;
            int col;
            while (!perc.percolates()) {

                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                } while (perc.isOpen(row, col));

                perc.open(row, col);
                opened++;
            }
            // StdOut.println("System has percolated!");
            double mean = (double) opened / total;
            fractionArray[i] = mean;
            // StdOut.println("stored " + fraction_array[i] + " in " + i);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double totalMean = 0;
        for (double i : fractionArray) {
            totalMean += i;
        }
        return totalMean / t;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double mean = mean();
        double v = 0.0;
        for (double i : fractionArray) {
            v += (i - mean) * (i - mean);
        }
        return Math.sqrt(v / (t - 1));
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(t));
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(t));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("Please enter 2 arguments, N followed by T.");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats perStats = new PercolationStats(n, t);
        StdOut.println("mean \t\t = " + perStats.mean());
        StdOut.println("stddev \t\t = " + perStats.stddev());
        StdOut.println("95% confidence interval \t = "
                + perStats.confidenceLo() + ", " + perStats.confidenceHi());
    }
}
