
import edu.princeton.cs.algs4.LinkedStack;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bruceoutdoors
 */
public class Board {

    final int n;
    final int[][] tiles;
    final int[][] goal;

    final int manhatt;
    final int hamm;
    final Boolean isgoal;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        assert blocks.length == blocks[0].length;

        n = blocks.length;
        tiles = copyArr(blocks);

        goal = new int[n][n];
        calcGoal();

        manhatt = calcManhattan();
        hamm = calcHamming();
        isgoal = calcIsGoal();
    }

    private int[][] copyArr(int[][] blocks) {
        int[][] ts = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(blocks[i], 0, ts[i], 0, n);
        }

        return ts;
    }

    private void calcGoal() {
        int incr = 1;
        int stop = n * n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                goal[i][j] = incr++;

                if (incr == stop) {
                    break;
                }
            }
        }

        goal[n - 1][n - 1] = 0;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hamm;
    }

    private int calcHamming() {
        int ham = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (val == 0) {
                    continue;
                }

                if (val != goal[i][j]) {
                    ++ham;
                }
            }
        }

        return ham;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhatt;
    }

    private int calcManhattan() {
        int man = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                int val = tiles[i][j] - 1;
                int r = val / n;
                int c = val % n;

                int distance = Math.abs(i - r) + Math.abs(j - c);

                man += distance;
            }
        }

        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isgoal;
    }

    private boolean calcIsGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != goal[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        int[][] twinTiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, twinTiles[i], 0, n);
        }

        // exchange the first 2 tiles of the 1st row, or 2nd row
        if (twinTiles[0][0] != 0 && twinTiles[0][1] != 0) {
            int temp = twinTiles[0][0];
            twinTiles[0][0] = twinTiles[0][1];
            twinTiles[0][1] = temp;
        } else {
            int temp = twinTiles[1][0];
            twinTiles[1][0] = twinTiles[1][1];
            twinTiles[1][1] = temp;
        }

        Board twin = new Board(twinTiles);

        return twin;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) other;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedStack<Board> neighs = new LinkedStack<>();

        // find position of blank tile
        int r = 0;
        int c = 0;
        
        outerloop:
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    r = i;
                    c = j;
                    break outerloop;
                }
            }
        }

        if (r > 0) {
            int[][] west = copyArr(tiles);
            west[r][c] = west[r - 1][c];
            west[r - 1][c] = 0;
            neighs.push(new Board(west));
        }

        if (c > 0) {
            int[][] north = copyArr(tiles);
            north[r][c] = north[r][c - 1];
            north[r][c - 1] = 0;
            neighs.push(new Board(north));
        }

        if (r < n - 1) {
            int[][] east = copyArr(tiles);
            east[r][c] = east[r + 1][c];
            east[r + 1][c] = 0;
            neighs.push(new Board(east));
        }
        
        if (c < n - 1) {
            int[][] south = copyArr(tiles);
            south[r][c] = south[r][c + 1];
            south[r][c + 1] = 0;
            neighs.push(new Board(south));
        }

        return neighs;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

        Board solvedBoard = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });

        assert solvedBoard.isGoal();

        Board twin = solvedBoard.twin();

        assert solvedBoard.equals(twin.twin());

        Board b = new Board(new int[][]{
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        });

        assert b.hamming() == 5;
        assert b.manhattan() == 10;
        
        Board d = new Board(new int[][]{
            {8, 1, 3},
            {4, 2,0},
            {7, 6, 5}
        });
        Iterable<Board> bs = d.neighbors();
        int i = 0;
    }
}
