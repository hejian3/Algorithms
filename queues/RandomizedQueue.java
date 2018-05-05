
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private static final double P = 0.25;
    private Item[] arr;
    private int size;
    private int first;
    private int last;
    
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
        size = 0;
        first = -1;
        last  = -1;
    }
    
    private class Itr implements Iterator<Item> {
        
        private final int [] indexArr;
        private int current = -1;
        
        public Itr() {
            indexArr = StdRandom.permutation(size);
        }
        
        public boolean hasNext() {
            return current < size -1;
        }
    
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            current++;
            return arr[indexArr[current]];
        }
    
        public void remove() {
            throw new java.lang.UnsupportedOperationException();   
        }
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        if (size == arr.length) resizeArr(size*2);
        
        if (isEmpty()) {
            first = 0;
            last  = 0;
            size++;
            arr[first] = item;
        }
        else {
            if (first > 0) {
                arr[0] = item;
            } 
            else if (last-first >= size) {
                for (int i = first; i <= last; i++) {
                    if (arr[i] == null) {
                        arr[i] = item;
                        break;
                    }
                }
            } 
            else {
                last++;
                arr[last] = item;
            }
            size++;
        }
    }
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        
        int removedIndex = -1;
        
        if (size == 1) {
            removedIndex = first;
        }
        else {
            removedIndex = StdRandom.uniform(first, last+1);
            if (arr[removedIndex] == null) {
                removedIndex = chooseIndex(removedIndex);
            }
        }
        
        if (removedIndex == -1) {
            throw new java.util.NoSuchElementException();
        }
        
        Item removed = arr[removedIndex];
        if (removedIndex == first) {
            first++;
        }
        else if (removedIndex == last) {
            last--;
        }
        arr[removedIndex] = null;
        size--;
        
        double rate = size *1D / arr.length;
        
        if (rate > 0 && rate <= P) {
            resizeArr(arr.length/2);
        }
        return removed;
    }
    
    private int chooseIndex(int removedIndex) {
        int index = -1;
        if (removedIndex > (first + last)/2) {
            index = chooseRight(removedIndex);
            if (index == -1) {
                return chooseLeft(removedIndex);
            }
            return index;
        }
        else {
            index = chooseLeft(removedIndex);
            if (index == -1) {
                return chooseRight(removedIndex);
            }
            return index;
        }
    }
    
    private int chooseLeft(int removedIndex) {
        for (int i = first; i < removedIndex; i++) {
            if (arr[i] != null) {
                return i;
            }
        }
        return -1;
    }
    
    private int chooseRight(int removedIndex) {
        for (int i = removedIndex + 1; i <= last; i--) {
            if (arr[i] != null) {
                return i;
            }
        }
        return -1;
    }
    
    private void resizeArr(int cap) {
        Item[] arrTemp = (Item[]) new Object[cap];
        int index = 0;
        for (int i = first; i <= last; i++) {
            if (arr[i] != null) {
                arrTemp[index++] = arr[i];
            }
        }
        first = 0;
        last = first + size - 1;
        arr = arrTemp;
    }
    
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (size == 1) {
            return arr[first];
        }
        
        Item sample = null;
        do {
            sample = arr[StdRandom.uniform(first, first+size)];
        }
        while (sample == null);
        return sample;
    }
    
    public Iterator<Item> iterator() {
        return new Itr();
    }
    
    public static void main(String[] args) {     
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        double [] probabilities = new double[]{0.1, 0.6, 0.1, 0.1, 0.1};
        for (int i = 0; i < 500; i++) {
            int index = StdRandom.discrete(probabilities);
            StdOut.println("index="+index);
            if (index == 1) {
                rq.enqueue("A");
            }
            else if (index == 2) {
                rq.dequeue();
            }
            else if (index == 3) {
                rq.sample();
            }
            else if (index == 4) {
                rq.isEmpty();
            }
            else if (index == 5) {
                rq.size();
            }
        }
    }
}