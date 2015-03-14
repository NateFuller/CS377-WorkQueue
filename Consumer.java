import java.util.concurrent.locks.*;

public class Consumer implements Runnable {
  
  private LimitedQueue queue;
  private WorkQueue.Counter counter;

  private static final Lock lock = new ReentrantLock();

  Consumer(LimitedQueue queue, WorkQueue.Counter counter) {
    this.queue = queue;
    this.counter = counter;
  }

  public void run() {
    while (true) {
      Character assignment;
      try {
        assignment = queue.dequeue();
      } catch (InterruptedException e) {
        System.out.println ("Consumer thread interrupted!");
        return;
      }

      if (assignment == null) {
        return;
      }

      lock.lock(); // lock before incrementing counter to avoid interleaving between consumers
      try {
        counter.val++;
      } finally {
        lock.unlock();
      }

      switch(assignment) {
        case 'a': 
          doJobA(counter.val);
          break;
        case 'b': 
          doJobB(counter.val);
          break;
        case 'c': 
          doJobC(counter.val);
          break;
        default: 
          System.out.println("Invalid Work: " + assignment);
          return;
      }
    }
  }

  private void doJobA(int counter) {
    System.out.println("Thread " + Thread.currentThread().getId() + " Starting Job A (#" + counter + ")");
    // quick work!
    System.out.println("Thread " + Thread.currentThread().getId() + " Ending Job A (#" + counter + ")");
  }

  private void doJobB(int counter) {
    System.out.println("Thread " + Thread.currentThread().getId() + " Starting Job B (#" + counter + ")");
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {}
    System.out.println("Thread " + Thread.currentThread().getId() + " Ending Job B (#" + counter + ")");
  }

  private void doJobC(int counter) {
    System.out.println("Thread " + Thread.currentThread().getId() + " Starting Job C (#" + counter + ")");

    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {}
    System.out.println("Thread " + Thread.currentThread().getId() + " Ending Job C (#" + counter + ")");
  }

}
