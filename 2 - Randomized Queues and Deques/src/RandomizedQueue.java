import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a; // array of items
    private int n; // number of elements on stack

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("add something!!");
        if (n == a.length)
            resize(2 * a.length); // double size of array if necessary
        a[n++] = item; // add item
    }

    // delete and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        exchange();
        Item item = a[n - 1];
        a[n - 1] = null; // to avoid loitering
        n--;
        // shrink size of array if necessary
        if (n > 0 && n == a.length / 4)
            resize(a.length / 2);
        return item;
    }

    // return (but do not delete) a random item
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        exchange();
        Item item = a[n - 1];
        return item;
    }

    private void exchange() {
        // generate a random number from 0 to N-2 to exchange with a[N-1]
        if (n < 1) return;
        int randInt = StdRandom.uniform(n);
        Item temp = a[randInt];
        a[randInt] = a[n - 1];
        a[n - 1] = temp;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private int i;
        private int[] seq;

        public ListIterator() {
            i = n;
            seq = new int[n];
            for (int j = 0; j < n; j++) seq[j] = j;
            StdRandom.shuffle(seq);
        }

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return a[seq[--i]];
        }
    }

    /**
     * A test client.
     */
    public static void main(String[] args) {
        RandomizedQueue<String> s1 = new RandomizedQueue<>();
        
        s1.enqueue("dog");
        s1.enqueue("likes");
        s1.enqueue("to");
        s1.enqueue("eat");
        s1.enqueue("beef");
        
        for (String i : s1) 
            StdOut.println(i);
        StdOut.println();
        for (String i : s1) 
            StdOut.println(i);
        
//        while (!StdIn.isEmpty()) {
//            String item = StdIn.readString();
//            if (!item.equals("-"))
//                s.enqueue(item);
//            else if (!s.isEmpty())
//                StdOut.print(s.dequeue() + " ");
//        }
//        StdOut.println("(" + s.size() + " left on stack):");
//        for (String item : s) 
//            StdOut.println(item);
    }
}