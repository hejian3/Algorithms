import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;


import java.util.Iterator;

/**
 * Created by hejian on 2018/4/19.
 */
public class WordNet {

    private final ST<String,SET<Integer>> st;
    private final String[]nounArr;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("argument is null");
        In nounIn = new In(synsets);
        String[] synsetArr = nounIn.readAllLines();
        st = new ST<>();
        nounArr =  new String[synsetArr.length];
        for (int i = 0; i < synsetArr.length; i++) {
            String[] temp = synsetArr[i].split(",");
            nounArr[i] = temp[1];
            String[] nonuArr = temp[1].split(" ");
            for(int j = 0; j < nonuArr.length; j++){
                if(!st.contains(nonuArr[j])) {
                    st.put(nonuArr[j], new SET<Integer>());
                }
                st.get(nonuArr[j]).add(i);
            }
        }
        Digraph digraph = new Digraph(synsetArr.length);
        initHypernyms(digraph, hypernyms);
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if(directedCycle.hasCycle()){
            throw new IllegalArgumentException("has cycle");
        }
        sap = new SAP(digraph);
    }

    private void initHypernyms(Digraph digraph, String hypernyms) {
        In hypernymsIn = new In(hypernyms);
        String[] hypernymsArr = hypernymsIn.readAllLines();
        for (int i = 0; i < hypernymsArr.length; i++) {
            String[] temp = hypernymsArr[i].split(",");
            if(hypernymsArr.length ==1 && temp.length > 1){
                throw new IllegalArgumentException("two root");
            }
            int nounIndex = Integer.parseInt(temp[0]);
            for (int j = 1; j < temp.length; j++) {
                int hypernymIndex = Integer.parseInt(temp[j]);
                if(nounIndex == hypernymIndex){
                    throw new IllegalArgumentException("has cycle");
                }
                digraph.addEdge(nounIndex, hypernymIndex);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        if (st == null) return null;
        return st.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (st ==null) return false;
        return st.contains(word);
    }

    private void checkNounAB(String nounA, String nounB){
        if (nounA == null || !isNoun(nounA)
                || nounB ==null || !isNoun(nounB))
            throw new IllegalArgumentException("argument is null");
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNounAB(nounA,nounB);
        Bag<Integer> vBag = new Bag<>();
        Bag<Integer> wBag = new Bag<>();
        for(Iterator<Integer> it = st.get(nounA).iterator();it.hasNext();) {
            vBag.add(it.next());
        }

        for(Iterator<Integer> it = st.get(nounB).iterator();it.hasNext();){
            wBag.add(it.next());
        }
        return sap.length(vBag,wBag);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNounAB(nounA,nounB);
        Bag<Integer> vBag = new Bag<>();
        Bag<Integer> wBag = new Bag<>();
        for(Iterator<Integer> it = st.get(nounA).iterator();it.hasNext();) {
            vBag.add(it.next());
        }

        for(Iterator<Integer> it = st.get(nounB).iterator();it.hasNext();){
            wBag.add(it.next());
        }
        int ancestor = sap.ancestor(vBag, wBag);
        if (ancestor == -1) return null;
        return nounArr[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt","hypernyms.txt");
        String s = wordNet.sap("loincloth","Adapin");
        StdOut.println("s="+s);
    }
}