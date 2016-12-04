import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n; // size of the stack
    private Node first; // first node
    private Node last;

    private class Node {
        private Item item;
        private Node right;
        private Node left;
    }

    /**
     * Create an empty stack.
     */
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    /**
     * Is the stack empty?
     */
    public boolean isEmpty() {
        return first == null || last == null;
    }

    /**
     * Return the number of items in the stack.
     */
    public int size() {
        return n;
    }

    /**
     * Add the item to the stack.
     */
    public void addFirst(Item item) {
        if (item == null) 
            throw new NullPointerException("You didn't add anything");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.left = oldlast;
        if (n++ != 0)
            oldlast.right = last;
        if (n == 1)
            first = last;
    }

    public void addLast(Item item) {
        if (item == null) 
            throw new NullPointerException("You didn't add anything");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.right = oldfirst;
        if (n++ != 0)
            oldfirst.left = first;
        if (n == 1)
            last = first;
    }

    /**
     * Delete and return the item most recently added to the stack.
     * 
     * @throws java.util.NoSuchElementException
     *             if stack is empty.
     */
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        Item item = first.item; // save item to return
        first = first.right; // delete first node
        if (--n != 0)
            first.left = null; // sever off old first node
        if (n == 0) last.item = null;
        return item; // return the saved item
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        Item item = last.item; // save item to return
        last = last.left; // delete first node
        if (--n != 0)
            last.right = null; // sever off old last node
        if (n == 0) first.item = null;
        return item; // return the saved item
    }

    /**
     * Return an iterator to the stack that iterates through the items in LIFO
     * order.
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = last;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.left;
            return item;
        }
    }
    
    /**
     * A test client.
     */
    public static void main(String[] args) {
        Deque<String> s = new Deque<>();
        
        s.addFirst("dog");
        s.addFirst("goes");
        s.addFirst("woof");
        
        for (String i : s) StdOut.println(i);

//        StdOut.print(s.removeFirst() + " ");
//        StdOut.print(s.removeFirst() + " ");
//        StdOut.print(s.removeFirst() + " ");
        
//        while (!StdIn.isEmpty()) {
//            String item = StdIn.readString();
//            if (item.equals("<")) StdOut.print(s.removeLast() + " ");
//            else if (item.equals(">")) StdOut.print(s.removeFirst() + " ");
//            else s.addLast(item);
//        }
//        StdOut.println("(" + s.size() + " left on stack)");
    }
}
