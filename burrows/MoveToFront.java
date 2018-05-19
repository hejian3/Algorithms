import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    /**
     * apply move-to-front encoding, reading from standard input and writing to standard output
     */
    public static void encode(){
        LinkedList<Character> list = getWordList();
        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar(8);
            int index = 0;
            for(char r : list){
                if(r == c){
                    if(index > 0) {
                        list.remove(index);
                        list.addFirst(c);
                    }
                    BinaryStdOut.write(index,8);
                    break;
                }
                index++;
            }
        }
        BinaryStdOut.flush();
    }

    /**
     * apply move-to-front decoding, reading from standard input and writing to standard output
     */
    public static void decode() {
        LinkedList<Character> list = getWordList();
        while (!BinaryStdIn.isEmpty()){
            int i = BinaryStdIn.readChar(8);
            int index = 0;
            for(char r :list){
                if(i == index){
                    if(index > 0){
                        list.remove(index);
                        list.addFirst(r);
                    }
                    BinaryStdOut.write(r);
                    break;
                }
                index++;
            }
        }
        BinaryStdOut.flush();
    }

    private static LinkedList<Character> getWordList(){
        LinkedList<Character> wordList = new LinkedList<>();
        for(int i = 0; i < 256; i++){
            char c  = (char)i;
            wordList.add(c);
        }
        return wordList;
    }

    /**
     *
     * if args[0] is '-', apply move-to-front encoding
     * if args[0] is '+', apply move-to-front decoding
     * @param args
     */
    public static void main(String[] args){
        if(args[0].equals("-"))encode();
        if(args[0].equals("+"))decode();
    }
}
