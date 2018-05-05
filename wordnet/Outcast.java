import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by hejian on 2018/4/19.
 */
public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)    {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        if(nouns == null)
            throw new IllegalArgumentException("argument is null");
        String result = null;
        int maxDist = -1;
        for(int i =0; i < nouns.length; i++) {
            String temp1 = nouns[i];
            int dist = 0;
            for (int j =0;j<nouns.length;j++){
                String temp2 = nouns[j];
                int d = wordnet.distance(temp1,temp2);
                dist += d;
            }
            if(dist > maxDist){
                result = temp1;
                maxDist = dist;
            }
        }
        return result;
    }

    // see test client below
    public static void main(String[] args){
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String [] fileArr = new String[]{"outcast5.txt", "outcast8.txt", "outcast11.txt"};
        for (int t = 0; t < 1; t++) {
            In in = new In(fileArr[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(fileArr[t] + ": " + outcast.outcast(nouns));
        }
    }
}