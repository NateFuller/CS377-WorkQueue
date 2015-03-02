import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;

class WorkQueue {

  public static class Counter {
    public int val;
  }

  private static LimitedQueue queue = new LimitedQueue();
  private static Counter counter = new Counter(); // counts tasks as they are taken by consumers

  public static void main (String[] args) throws IOException {
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
