import java.util.concurrent.locks.*;

class LimitedQueue {
  static int queue_cap = 10; // queue capacity
  int head = 0; // pointer to the front of the queue
  int size = 0; // current usage
  char[] elements = new char[queue_cap]; // container for elements
  public boolean shouldTerminate = false;

  private final Lock lock = new ReentrantLock();
  private final Condition full = lock.newCondition();
  public final Condition empty = lock.newCondition();

  public void debug() {
    System.out.print ("head= " + head + " size= " + size);
    System.out.print("[ ");
    for (char element : elements) 
      System.out.print("" + element + ", ");
    System.out.println("]");
  }

  // inspired by page 14 of lec07b-monitors-2015.pdf
  // http://lass.cs.umass.edu/~dganesan/courses/spring15/lectures/lec07b-monitors-2015.pdf
  public boolean enqueue(char elem) {
    // acquire lock
    lock.lock(); 
    
    try {
      // if the buffer is full, wait for a signal from a consumer
      while (isFull()) {
        try {
          full.await();
        } catch (InterruptedException e) {}
      }
      //System.out.println("Thread " + Thread.currentThread().getId() + " enqueuing " + elem);
      // insert the element
      elements[head] = elem;
      head = head == 0 ? queue_cap-1 : head-1;
      size++;

      // signal to a waiting consumer that there is something to be consumed
      empty.signal();
      return true;
    } finally {
      // release lock
      lock.unlock();
    }
  }

  public Character dequeue() throws InterruptedException{
    // acquire lock
    lock.lock();
    char to_return;
    
    try {
      // if the buffer is empty, wait for a signal from a producer
      while (isEmpty() && !shouldTerminate) {
          empty.await();
      }

      // when LimitedQueue finds out that the queue is empty,
      // it will shouldTerminate to true and signal all consumers
      // to wake up. They'll break out of the loop above and
      // return null in the if statment below, which will terminate
      // them in Consumer.java
      if (shouldTerminate) {
            return null;
      } else {
        // "remove" the element
        to_return = elements[(head+size)%queue_cap]; 
        size--;
        
        // signal to a waiting producer that there is space for production
        full.signal();
        return to_return;
      }
    } finally {
      // release lock
      lock.unlock();
    }
  }

  public boolean isFull() {
    return size == queue_cap;
  }

  public boolean isEmpty() {
    return size == 0;
  }

}
