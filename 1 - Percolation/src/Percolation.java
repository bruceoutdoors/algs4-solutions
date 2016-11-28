import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int size, first, last;
    private WeightedQuickUnionUF weightedUnion;
    private boolean[] opened;

    // create N-by-N grid, with all sites blocked
    public Percolation(int n) {
        this.n = n;
        size = n * n;
        // last 2 id array is the first and last site respectively
        first = size;
        last = size + 1;

        // create UF size (N*N)+2
        weightedUnion = new WeightedQuickUnionUF(size + 2);

        // create an boolean array not including first and last site
        opened = new boolean[size];

        if (n < 2)
            return;
        for (int i = 0; i < n; i++) {
            // connect first site to 1st row
            weightedUnion.union(first, i);
            // connect end node to last row
            weightedUnion.union(last, size - 1 - i);
        }

    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        int id = toSingleDimension(i, j);
        // mark site as opened, if not
        if (opened[id]) {
            return;
        } else {
            opened[id] = true;
        }

        if (n == 1) {
            weightedUnion.union(id, last);
            weightedUnion.union(id, first);
            return;
        }

        // attempt to connect bottom & top site
        int bottom = id + n;
        int top = id - n;
        if (bottom < first)
            linkSite(id, bottom);
        if (!(top < 0))
            linkSite(id, top);

        // check if adjacent sites are opened, if so connect them
        // if col is 1, means left column; attempt to connect right and bottom
        // site
        if (j == 1) {
            // to right
            int right = id + 1;
            if (j != n)
                linkSite(id, right);
            return;
        }
        // if col is N, means right column; attempt to connect left and bottom
        // site
        if (j == n) {
            // to left
            int left = id - 1;
            linkSite(id, left);
            return;
        }
        // else attempt to connect left, right and bottom site
        int right = id + 1;
        int left = id - 1;
        linkSite(id, right);
        linkSite(id, left);
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        return opened[toSingleDimension(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        // does system percolate at that coordinate?
        if (isOpen(i, j)) 
            return weightedUnion.connected(first, toSingleDimension(i, j));
        return false;
    }

    // does the system percolate?
    public boolean percolates() {
        // is beginning node connected to last node?
        return weightedUnion.connected(first, last);
    }

    private int toSingleDimension(int i, int j) {
        if (i <= 0 || i > n)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > n)
            throw new IndexOutOfBoundsException("column index j out of bounds");
        // (N*(row-1))+col
        return (n * (i - 1)) + (j - 1);
    }

    private void linkSite(int id, int targetId) {
        if (opened[targetId]) {
            if (!weightedUnion.connected(id, targetId))
                weightedUnion.union(id, targetId);
        }
    }
}
