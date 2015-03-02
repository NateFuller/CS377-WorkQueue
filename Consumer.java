public class Consumer implements Runnable {
  
  private LimitedQueue queue;
  private WorkQueue.Counter counter;

  Consumer(LimitedQueue queue, WorkQueue.Counter counter) {
    this.queue = queue;
    this.counter = counter; 
  }

  public void run() {
    Character assignment = queue.dequeue();
    if (assignment == null)
      return;
    counter.val++;
    switch(assignment) {
      case 'a': doJobA(counter.val);
        return;
      case 'b': doJobB(counter.val);
        return;
      case 'c': doJobC(counter.val);
        return;
      default: System.out.println("Invalid Work: " + assignment);
        return;
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
