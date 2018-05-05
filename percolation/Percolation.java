
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] grid;
    private final int gridSize;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final int top;
    private final int bottom;
    private int count;
    private boolean percolates;
    private int openSiteCount;
    private boolean [] openSiteGrid;
    
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
        grid = new boolean [n][n];
        gridSize = n;
        top = n*n;
        bottom = top+1;
        openSiteGrid = new boolean[n];
        
    }
    
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new java.lang.IllegalArgumentException();
        }
        if (!isOpen(row, col) && !percolates()) {
            int value = getColumnValue(row, col);
            setValue(row, col, true);
            count++;
            
            if (!openSiteGrid[row-1]) {
                openSiteGrid[row-1] = true;
                openSiteCount++;
            }
            
            int upperRow = row-1;
            if (row > 1 && isOpen(upperRow, col)) {
                union(value, getColumnValue(upperRow, col));
            }
            int leftCol = col-1;
            if (col > 1 && isOpen(row, leftCol)) {
                union(value, getColumnValue(row, leftCol));
            }
            int rightCol = col+1;
            if (col < gridSize && isOpen(row, rightCol)) {
                union(value, getColumnValue(row, rightCol));
            }
            int underRow = row+1;
            if (row < gridSize && isOpen(underRow, col)) {
                union(value, getColumnValue(underRow, col));
            }
            
            if (row == 1) {
                union(top, value);
            }
            
            if (openSiteCount == gridSize) {
                connectBottom();
            }
        }
    }
    
    private void connectBottom() {
        for (int i = 1; i <= gridSize; i++) {
            if (isFull(gridSize, i)) {
                union(getColumnValue(gridSize, i), bottom);
                percolates = weightedQuickUnionUF.connected(top, bottom);
                break;
            }
        }
    }
    
    private void union(int value, int unionValue) {
        weightedQuickUnionUF.union(value, unionValue);
    }
    
    private int getColumnValue(int row, int col) {
        return (row-1)*gridSize + col-1;
    }
    
    private boolean getValue(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new java.lang.IllegalArgumentException();
        }
        return grid[--row][--col];
    }
    
    private void setValue(int row, int col, boolean value) {
        grid[--row][--col] = value;
    }
    
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new java.lang.IllegalArgumentException();
        }
        return getValue(row, col);
    }
    
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        return weightedQuickUnionUF.connected(getColumnValue(row, col), top);
    }
    
    public int numberOfOpenSites() {
        return count;
    }
    
    public boolean percolates() {
        return percolates;
    }
    
    private WeightedQuickUnionUF q() {
        return weightedQuickUnionUF;
    }
    
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        p.open(1, 1);
        p.open(1, 2);
        p.open(1, 4);
        p.open(1, 5);
        p.open(1, 3);
        p.open(5, 5);
        p.open(5, 1);
        p.open(5, 3);
        p.open(5, 2);
        p.open(5, 4);
        p.open(4, 1);
        p.open(4, 3);
        p.open(4, 4);
        p.open(4, 5);
        p.open(4, 2);
        p.open(2, 1);
        p.open(2, 4);
        p.open(2, 5);
        p.open(2, 3);
        p.open(3, 1);
        System.out.println(p.q().find(p.getColumnValue(3, 1))); 
        p.open(3, 2);
        p.open(3, 3);
        p.open(3, 4);
        p.open(3, 5);
        System.out.println(p.percolates());  
    }
}