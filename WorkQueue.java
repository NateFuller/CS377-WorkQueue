import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NumberFormatException;
import java.io.IOException;

class WorkQueue {

  public static class Counter {
    public int val;
  }

  private static LimitedQueue queue = new LimitedQueue();
  private static Counter counter = new Counter(); // counts tasks as they are taken by consumers

  public static void main (String[] args) throws IOException {
    int num_producers = -1;
    int num_consumers = -1;
    
    // Validate input
    if (args.length != 2) {
      System.err.println("Usage: java WorkQueue <#producers> <#consumers>");
      System.exit(1);
    } 

    // Check that arguments are integers
    try {
      num_producers = Integer.parseInt(args[0] + "");
      num_consumers = Integer.parseInt(args[1] + "");
      System.out.println("args[0]: " + args[0] + ", args[1]: " + args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Usage: java WorkQueue <#producers> <#consumers>");
      System.exit(1);
    }

    // testing only
    /*System.out.println("Number of producers = " + num_producers);
    System.out.println("Number of consumers = " + num_consumers);*/
    
    counter.val = 0;

    Consumer consumer = new Consumer(queue, counter);
    Producer producer = new Producer(queue, counter);

    while (!producer.isDone || !queue.isEmpty()) {
      producer.run();
      consumer.run();
    }

    System.out.println("" + counter.val + " Jobs Done");

  }

}
