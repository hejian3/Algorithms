
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first;
    private Node last;
    private int size;
    
    public Deque() {
        size = 0;
    }
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    private class Itr implements Iterator<Item> {
        private Node current;
        
        public boolean hasNext() {
            return current != last;
        }
    
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            if (current == null) {
                current = first;
            }
            else {
                current = current.next;
            }
            return current.item;
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
    
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            last = first;
        } 
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
        } 
        size++;
    }
    
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        if (isEmpty()) {
            last = new Node();
            last.item = item;
            first = last;
        } 
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.prev = oldLast;
            oldLast.next = last;
        }
        size++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node removed = first;
        first = first.next;
        size--;
        if (isEmpty()) {
            last = null;
        } 
        else {
            first.prev = null;
        }
        return removed.item;
    }
    
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node removed = last;
        last = last.prev;
        size--;
        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        return removed.item;
    }
    
    public Iterator<Item> iterator() {
        return new Itr();
    }
    
    public static void main(String [] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
    }
}