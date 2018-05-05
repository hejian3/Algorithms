
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Permutation {
    
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> que = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            que.enqueue(str);
        }
        
        if (k < 0 || k > que.size()) {
            throw new java.lang.IllegalArgumentException();
        }
        
        Iterator<String> it = que.iterator();
        
        for (int i = 0; i < k; i++) {
            StdOut.println(it.next()); 
        }
    }
}