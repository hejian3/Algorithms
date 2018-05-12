import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class BoggleBoard {

    private final char[][] cArr;
    private final int rows;
    private final int cols;
    private final String toString;

    // Initializes a random 4-by-4 Boggle board.
    // (by rolling the Hasbro dice)
    public BoggleBoard() {
        this(4, 4);
    }

    // Initializes a random m-by-n Boggle board.
    // (using the frequency of letters in the English language)
    public BoggleBoard(int m, int n) {
        rows = m;
        cols = n;
        cArr = new char[rows][cols];
        toString = initToString();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = (char) StdRandom.uniform('A', 'Z' + 1);
                cArr[i][j] = c;
            }
        }
    }

    // Initializes a Boggle board from the specified filename.
    public BoggleBoard(String filename) {
        if (filename == null) throw new IllegalArgumentException("filename should not be null");
        In in = new In(filename);
        rows = in.readInt();
        cols = in.readInt();
        cArr = new char[rows][];
        int row = 0;
        while (in.hasNextLine() && row < rows) {
            int col = 0;
            cArr[row] = new char[cols];
            while (in.hasNextChar()) {
                if(col ==cols)break;
                char c = in.readChar();
                if(c =='Q')in.readChar();
                if(c == 10 || c == 32)continue;
                cArr[row][col++] = c;
            }
            row++;
        }
        toString = initToString();
    }

    // Initializes a Boggle board from the 2d char array.
    // (with 'Q' representing the two-letter sequence "Qu")
    public BoggleBoard(char[][] a) {
        if (a == null) throw new IllegalArgumentException(" a should not be null");
        cArr = new char[a.length][];
        rows = a.length;
        cols = a[0].length;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                cArr[i][j] = a[i][j];
            }
        }
        toString = initToString();
    }

    // Returns the number of rows.
    public int rows() {
        return rows;
    }

    // Returns the number of columns.
    public int cols() {
        return cols;
    }

    // Returns the letter in row i and column j.
    // (with 'Q' representing the two-letter sequence "Qu")
    public char getLetter(int i, int j) {
        if (i < 0 || i > rows) throw new IllegalArgumentException(" invalid row " + i);
        if (j < 0 || j > cols) throw new IllegalArgumentException(" invalid col " + j);
        return cArr[i][j];
    }

    private String initToString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j > 0) {
                    sb.append(" ");
                }
                char c = cArr[i][j];
                if (c == 'Q') {
                    sb.append("Qu");
                } else {
                    sb.append(cArr[i][j]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Returns a string representation of the board.
    public String toString() {
        return toString();
    }
}