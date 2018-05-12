import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BoggleSolver {
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    
    private final List<String> dictionaryList;
    private final Set<String> dictionarySet;


    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException(" dictionary should not be null");
        dictionaryList = new ArrayList<>(dictionary.length);
        dictionarySet = new HashSet<>(dictionary.length);
        for(int i = 0; i < dictionary.length; i++){
            String d = dictionary[i];
            dictionarySet.add(d);
            if(d.length() <=2 || d.charAt(d.length()-1)=='Q')continue;
            dictionaryList.add(d);
        }
        dictionaryList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        }.reversed());
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException("board should not be null");
        int countQ = 0;
        Map<Character, Queue<BoardPoint>> st = new Hashtable<>();
        for(int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                char c = board.getLetter(i,j);
                if(c =='Q')countQ++;
                if(!st.containsKey(c)){
                    st.put(c,new Queue<>());
                }
                st.get(c).enqueue(new BoardPoint(i,j));
            }
        }
        int maxLength = board.cols() * board.rows() + countQ;
        Set<String> queue = new HashSet<>();
        for(String d : dictionaryList){
            if(d.length() > maxLength) continue;
            if(isWordContains(st,queue,d)){
                queue.add(d);
                continue;
            }
            int i = search(st,null,null,d,0);
            if(i == d.length()){
                queue.add(d);
            }
        }
        return queue;
    }

    private boolean isWordContains(Map<Character,Queue<BoardPoint>> st, Set<String> set, String d){
        if(st.containsKey('Q') && d.startsWith("U"))return false;
        for(String s : set){
            if(s.contains(d) && st.containsKey(d.charAt(0)))return true;
        }
        return false;
    }


    private int search(Map<Character,Queue<BoardPoint>> st, Set<BoardPoint> set,
                       List<BoardPoint> arrays, String d, int index){
        if(d.length() == index)return index;
        char c = d.charAt(index);
        if(!st.containsKey(c))return -1;
        boolean isQu = isQu(d,index);
        if(c=='Q' && !isQu)return -1;
        int cCount = st.get(c).size();
        for (Iterator<BoardPoint> iterator = st.get(c).iterator(); iterator.hasNext();) {
            BoardPoint boardPoint = iterator.next();
            if(set!=null && set.contains(boardPoint))continue;
            if(index > 0 && isAdjacent(arrays.get(index - 1).row, arrays.get(index - 1).col,
                    boardPoint.row, boardPoint.col) || index ==0) {
                Set<BoardPoint> s = null;

                if(set == null){
                    s = new HashSet<>();
                }else{
                    if(cCount > 1) {
                        s = new HashSet<>(set);
                    }else{
                        s = set;
                    }
                }
                List<BoardPoint> list = null;
                if(arrays == null){
                    list = new ArrayList<>();
                }else{
                    if(cCount > 1) {
                        list = new ArrayList<>(arrays);
                    }else{
                        list = arrays;
                    }
                }
                list.add(boardPoint);
                if(isQu){
                    list.add(boardPoint);
                }
                s.add(boardPoint);
                int result = search(st, s, list, d, isQu?index + 2:index + 1);
                if(result != -1)return result;
            }
        }
        return -1;
    }

    private boolean isQu(String d, int index){
        return index < d.length() - 1 && d.charAt(index) =='Q' && d.charAt(index + 1) =='U';
    }

    private class BoardPoint{

        private int row;
        private int col;

        BoardPoint(int row,int col){
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BoardPoint that = (BoardPoint) o;
            return row == that.row &&
                    col == that.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
    
    private boolean isAdjacent(int row,int col,int preRow,int preCol){
        return Math.abs(row - preRow) <= 1 && Math.abs(col - preCol) <=1;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("word should not be null");
        if (word.length() <= 2 || !dictionarySet.contains(word)) return 0;
        else if(word.length() <= 4) return 1;
        else if(word.length() == 5) return 2;
        else if(word.length() == 6) return 3;
        else if(word.length() == 7) return 5;
        else                      return 11;
    }
    
    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board_2.txt");
        long time = System.currentTimeMillis();
        solver.getAllValidWords(board);
        StdOut.println("time = " + (System.currentTimeMillis() - time));
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            count ++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("count = " + count);
    }
}