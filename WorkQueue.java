import java.util.ArrayList;
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
    
    // Validate 2 arguments in input
    if (args.length != 2) {
      printUsageAndExit();
    } 

    // Check that arguments are integers
    try {
      num_producers = Integer.parseInt(args[0] + "");
      num_consumers = Integer.parseInt(args[1] + "");
      //System.out.println("args[0]: " + args[0] + ", args[1]: " + args[1]);
    } catch (NumberFormatException e) {
      printUsageAndExit();
    }

    counter.val = 0;
    
    // arrays to hold consumers and producers
    Consumer[] consumers = new Consumer[num_consumers];
    Producer[] producers = new Producer[num_producers];

    // just some structures to hold the threads so we can Thread.join them
    ArrayList<Thread> consumer_threads = new ArrayList<Thread>();
    ArrayList<Thread> producer_threads = new ArrayList<Thread>();

    // create all the consumers
    for (int i = 0; i < num_consumers; i++) {
      consumers[i] = new Consumer(queue, counter);

      // create the consumer threads, add them to the structure, 
      // then start them. they should immediately wait since queue is empty
      Thread consumer_thread = new Thread(consumers[i]);
      consumer_threads.add(consumer_thread);
      consumer_thread.start();
    }

    // create all the producers
    for (int i = 0; i < num_producers; i++) {
      producers[i] = new Producer(queue, counter);

      // create the producer threads, add them to the structure,
      // then start them. they should start to produce once started
      Thread producer_thread = new Thread(producers[i]);
      producer_threads.add(producer_thread);
      producer_thread.start();
    }

    // main thread wait for all producers to terminate
    for (Thread pt : producer_threads) {
      try {
        pt.join();
      } catch (InterruptedException e) {};
    }
    // at this point, all producer threads should be finished
    // and therefore nothing will should get enqueued

    // wait for queue to be emptied by consumers
    while (!queue.isEmpty()) { }

    // set this to true so the while loop in dequeue can break out and
    // the thread can terminate
    queue.shouldTerminate = true;

    // signal all waiting consumers to wake up
    try {
      queue.empty.signalAll();
    } catch (IllegalMonitorStateException e) { /* this is expected */ }

    // wait for the consumer_threads to return and join them
    for (Thread ct : consumer_threads) {
      try {
        ct.join();
      } catch (InterruptedException e) {};
    }

    System.out.println("\nMain thread terminating!");
  }

  private static void printUsageAndExit() {
    System.err.println("Usage: java WorkQueue <#producers> <#consumers>");
    System.exit(1);
  }

}