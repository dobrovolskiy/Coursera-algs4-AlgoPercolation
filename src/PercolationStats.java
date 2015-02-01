
/**
 * Created by Dmitry Dobrovolsky on 31.01.2015.
 */
public class PercolationStats {

    private static final double CONSTANT = 1.96;

    private final int t;
    private double[] thresholds;

    public PercolationStats(int N, int T) {
        // perform T independent experiments on an N-by-N grid
        this.t = T;
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Wrong argument value");
        }
        thresholds = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            double steps = 0;
            while (!percolation.percolates()) {
                int x = StdRandom.uniform(1, N + 1);
                int y = StdRandom.uniform(1, N + 1);
                if (!percolation.isOpen(x, y)) {
                    percolation.open(x, y);
                    steps++;
                }
            }
            thresholds[i] = steps / (N * N);
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return thresholds.length > 1 ? StdStats.stddev(thresholds) : Double.NaN;
    }

    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return this.mean() - (CONSTANT * this.stddev()) / Math.sqrt(t);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return this.mean() + (CONSTANT * this.stddev()) / Math.sqrt(t);
    }

    public static void main(final String[] args) {
        // test client (described below)
        int t = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[0]);
        PercolationStats stats = new PercolationStats(n, t);
        double mean = stats.mean();
        StdOut.println("mean = " + mean);
        double stddev = stats.stddev();
        StdOut.println("stddev = " + stddev);
        StdOut.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }
}