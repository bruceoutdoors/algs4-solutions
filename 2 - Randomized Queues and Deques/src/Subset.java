
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    /**
     * A test client.
     */
    public static void main(String[] args) {
        
        if (args.length != 1) {
            StdOut.println("Please enter only 1 argument.");
            return;
        }
        
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> s = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            s.enqueue(item);
        }
            
        for (int i = 0; i < k; i++) 
            StdOut.println(s.dequeue());
    }
}
