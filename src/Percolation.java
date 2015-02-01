/**
 * Created by Dmitry Dobrovolsky on 31.01.2015.
 */
public class Percolation {
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF backWashCheckWQUUF;
    private static final int VIRTUAL_TOP_SITE_INDEX = 0;
    private static final int VIRTUAL_BOTTOM_SITE_INDEX = 1;
    private int n;
    private boolean[][] gridElems;

    public Percolation(int N) { // create n-by-n grid, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException("N <=0!");
        }
        this.n = N;
        gridElems = new boolean[N][N];
        weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
        backWashCheckWQUUF = new WeightedQuickUnionUF(N * N + 2);
    }

    public void open(int i, int j) {
        // open site (row i, column j) if it is not open already
        checkArguments(i, j);
        if (isOpen(i, j)) {
            return;
        }

        gridElems[i - 1][j - 1] = true;
        connectWithNeighbour(i, j, i - 1, j);
        connectWithNeighbour(i, j, i, j - 1);
        connectWithNeighbour(i, j, i + 1, j);
        connectWithNeighbour(i, j, i, j + 1);

        if (i == 1) { //  top row
            weightedQuickUnionUF.union(VIRTUAL_TOP_SITE_INDEX, toIndex(i, j));
            backWashCheckWQUUF.union(VIRTUAL_TOP_SITE_INDEX, toIndex(i, j));
        }
        if (i == n) { // bottom row
            weightedQuickUnionUF.union(
                    VIRTUAL_BOTTOM_SITE_INDEX, toIndex(i, j));
        }
    }

    public boolean isOpen(int i, int j)      // is site (row i, column j) open?
    {
        checkArguments(i, j);
        return gridElems[i - 1][j - 1];
    }

    public boolean isFull(int i, int j)      // is site (row i, column j) full?
    {
        checkArguments(i, j);
        return backWashCheckWQUUF.connected(
                VIRTUAL_TOP_SITE_INDEX, toIndex(i, j));
    }

    public boolean percolates() {            // does the system percolate?
        return weightedQuickUnionUF.connected(
                VIRTUAL_TOP_SITE_INDEX, VIRTUAL_BOTTOM_SITE_INDEX);
    }

    private void checkArguments(final int i, final int j) {
        if (i <= 0 || i > n || j <= 0 || j > n) {
            throw new IndexOutOfBoundsException("Illegal argument value!");
        }
    }

    private void connectWithNeighbour(
            final int i, final int j, final int k, final int l) {
        if (k > 0 && k <= n && l <= n && l > 0 && isOpen(k, l)) {
            weightedQuickUnionUF.union(toIndex(i, j), toIndex(k, l));
            backWashCheckWQUUF.union(toIndex(i, j), toIndex(k, l));
        }
    }

    private int toIndex(final int i, final int j) {
        return (i - 1) * n + j + 1;
    }

    public static void main(final String[] args) {

    }   // test client (optional)
}
