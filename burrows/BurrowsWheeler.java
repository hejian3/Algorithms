import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class BurrowsWheeler {

    /**
     * apply Burrows-Wheeler transform, reading from standard input and writing to standard output
     */
    public static void transform(){

        String s = BinaryStdIn.readString();

        char [] transformArr = new char[s.length()];

        int first = -1;

        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);

        for(int i =0;i<s.length();i++){
            int index  = circularSuffixArray.index(i);
            if(index == 0){
                first = i;
                transformArr[i] = s.charAt(s.length()-1);
            }else{
                transformArr[i] = s.charAt(index-1);
            }
        }

        BinaryStdOut.write(first);
        for(int i =0 ;i < transformArr.length;i++){
            BinaryStdOut.write(transformArr[i]);
        }
        BinaryStdOut.flush();
    }


    /**
     * apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
     */
    public static void inverseTransform(){
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char [] t = s.toCharArray();

        int [] next  = new int[s.length()];

        Map<Character,Deque<Integer>> map = new HashMap<>();
        for(int i = 0; i < s.length();i++){
            char c = t[i];
            if(!map.containsKey(c)){
                map.put(c,new LinkedList<>());
            }
            map.get(c).add(i);
        }

        Arrays.sort(t);

        for(int i = 0; i < s.length(); i++){
            char c = t[i];
            next[i] = map.get(c).removeFirst();
        }

        int prev = first;
        BinaryStdOut.write(t[first]);
        for(int i = 1; i < s.length(); i++){
            prev = next[prev];
            BinaryStdOut.write(t[prev]);
        }
        BinaryStdOut.flush();
    }

    /**
     * if args[0] is '-', apply Burrows-Wheeler transform
     * if args[0] is '+', apply Burrows-Wheeler inverse transform
     * @param args
     */
    public static void main(String[] args){
        if(args[0].equals("-"))transform();
        if(args[0].equals("+"))inverseTransform();
    }
}
