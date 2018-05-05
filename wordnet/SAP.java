import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hejian on 2018/4/19.
 */
public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if(G ==null)
            throw new IllegalArgumentException("argument is null");
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        checkV(v);
        checkV(w);
        Bag<Integer> vBag = new Bag<>();
        vBag.add(v);
        Bag<Integer> wBag = new Bag<>();
        wBag.add(w);
        return length(vBag,wBag);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        checkV(v);
        checkV(w);
        Bag<Integer> vBag = new Bag<>();
        vBag.add(v);
        Bag<Integer> wBag = new Bag<>();
        wBag.add(w);
        return ancestor(vBag,wBag);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        Hashtable<Integer,Stack<Integer>> st = getAncestorST(v,w);
        int [] ret = ancestor(st);
        return ret[1];
    }

    private void checkV(int v){
        if(v < 0 || v >= G.V()) throw new IllegalArgumentException("argument is null");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        Map<Integer,Stack<Integer>> st = getAncestorST(v,w);
        int [] ret = ancestor(st);
        return ret[0];
    }

    private int[] ancestor(Map<Integer,Stack<Integer>> st){
        int [] ret = new int[2];
        if(st == null ||st.isEmpty()){
            ret[0] = -1;
            ret[1] = -1;
            return ret;
        }
        Map<Integer,Integer> lengthST = getLengthST(st);
        ret[0] = 0;
        ret[1] = Integer.MAX_VALUE;
        for(Iterator<Integer> it = lengthST.keySet().iterator();it.hasNext();){
            int a = it.next();
            int len = lengthST.get(a);
            if(len < ret[1]){
                ret[0] = a;
                ret[1] = len;
            }
        }
        return ret;
    }

    private Map<Integer,Integer> getLengthST(Map<Integer,Stack<Integer>> st){
        Map<Integer,Integer> lengthST = new Hashtable<>();
        for(Iterator<Integer> it = st.keySet().iterator();it.hasNext();){
            int ancestor = it.next();
            Stack<Integer> stack = st.get(ancestor);
            int size = stack.size();
            lengthST.put(ancestor, size-2);
        }
        return lengthST;
    }

    private Hashtable<Integer,Stack<Integer>> getAncestorST(Iterable<Integer> v, Iterable<Integer> w){
        if(v ==null || w ==null)
            throw new IllegalArgumentException("argument is null");
        Hashtable<Integer,Stack<Integer>> st = new Hashtable<>();
        ancestor(new Stack<>(),v,w,st);
        if(st.isEmpty())return null;
        return st;
    }

    private void ancestor(Stack<Integer> current,Iterable<Integer> v,
                          Iterable<Integer> w,Map<Integer,Stack<Integer>> st){
        int count = 0;
        for(Iterator<Integer> vIt = v.iterator(); vIt.hasNext();) {
            vIt.next();
            count++;
        }
        Stack<Integer> [] nodes = new Stack[count];
        if(count ==1){
            nodes[0] = current;
        }else {
            for (int i = 0; i < count; i++) {
                nodes[i] = copyStack(current);
            }
        }

        int index = 0;
        for(Iterator<Integer> vIt = v.iterator(); vIt.hasNext();){
            int vInt = vIt.next();
            checkV(vInt);
            if(hasV(nodes[index],vInt)|| abort(nodes[index],st))return ;
            Stack<Integer> stack = copyStack(nodes[index]);
            stack.push(vInt);
            Map<Integer,Stack<Integer>> stackST = new Hashtable<>();
            ancestor(vInt,w,stack,stackST);
            int ret[] = ancestor(stackST);
            if (ret[0] != -1 && ret[1] !=-1
                    && (!abort(stackST.get(ret[0]),st))) {
                st.put(ret[0], stackST.get(ret[0]));
            }
            index++;
        }
        index = 0;
        for(Iterator<Integer> vIt = v.iterator(); vIt.hasNext();) {
            int vInt = vIt.next();
            Iterable<Integer> temp = G.adj(vInt);
            if (temp != null ) {
                nodes[index].push(vInt);
                ancestor(nodes[index],temp, w,st);
            }
            index++;
        }
    }

    private Stack<Integer> copyStack(Stack<Integer> node){
        if(node == null)return new Stack<>();
        Stack<Integer> stack = new Stack<>();
        int [] arr = new int [node.size()];
        int index = 0;
        for (Iterator<Integer> it = node.iterator();it.hasNext();){
            arr[index++] = it.next();
        }
        for(int i =index-1;i >=0;i--){
            stack.push(arr[i]);
        }
        return stack;
    }

    private boolean hasV(Stack<Integer> stack,int v){
        if(stack ==null || stack.size()==0)return false;
        for(Iterator<Integer> it = stack.iterator(); it.hasNext();){
            int w = it.next();
            if(v == w)return true;
        }
        return false;
    }

    private boolean abort(Stack<Integer> stack,Map<Integer,Stack<Integer>> st){
        if(st == null || st.isEmpty() || stack ==null || stack.isEmpty())return false;
        for(Iterator<Integer> iterator = st.keySet().iterator(); iterator.hasNext();){
            if(st.get(iterator.next()).size()< stack.size())return true;
        }
        return false;
    }

    /**
     *
     * @param v
     * @param w
     * @param stack
     * @return
     */
    private void ancestor(int v, Iterable<Integer> w,Stack<Integer> stack,
                          Map<Integer,Stack<Integer>> st){
        int count = 0;
        for(Iterator<Integer> wIt = w.iterator(); wIt.hasNext();) {
            wIt.next();
            count++;
        }
        Stack<Integer> [] nodes = new Stack[count];
        if(count ==1){
            nodes[0] = stack;
        }else {
            for (int i = 0; i < count; i++) {
                nodes[i] = copyStack(stack);
            }
        }

        int index = 0;
        for(Iterator<Integer> wIt = w.iterator(); wIt.hasNext();){
            int wInt = wIt.next();
            checkV(wInt);
            if(v == wInt) {
                nodes[index].push(v);
                if(!st.containsKey(v) || st.get(v).size() > nodes[index].size()){
                    st.put(v,nodes[index]);
                }
                return;
            }
            index++;
        }
        index = 0;
        for(Iterator<Integer> wIt = w.iterator(); wIt.hasNext();){
            int wInt = wIt.next();
            if(hasV(nodes[index],wInt)|| abort(nodes[index],st))return;
            nodes[index].push(wInt);
            Iterable<Integer> temp = G.adj(wInt);
            if (temp != null && temp.iterator().hasNext()) {
                ancestor(v, temp, nodes[index], st);
            }
            index++;
        }
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In("digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        StdOut.printf("ancestor(7,0) %d \n", sap.ancestor(7,0));
        StdOut.printf("length(7,0) %d \n", sap.length(7,0));
    }
}