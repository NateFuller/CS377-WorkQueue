import java.io.FileReader;
import java.io.IOException;

public class Producer implements Runnable {

  private FileReader in;
  private LimitedQueue queue;
  private WorkQueue.Counter counter;
  public boolean isDone;

  Producer(LimitedQueue queue, WorkQueue.Counter counter) {
    try {
      in = new FileReader("work");
    } catch (IOException e) {
      System.err.println(e.getLocalizedMessage());
      System.exit(1);
    }
    this.queue = queue;
    this.counter = counter; 
  }

  public void run() {

    try {
        Thread.sleep(2000); // delay on input, maintain this behavior 
    } catch (InterruptedException e) {}

  /* Fills the queue with input from the work file.
   * Sets isDone if the file is empty. */
    while (true) {

      if (queue.isFull())
        return;
    
      int input = 0; // initialized to zero to satisfy overzealous compiler
      try {
        input = in.read(); // actually gets set to character value from read()
      } catch (IOException e) {
        System.err.println(e.getLocalizedMessage());
        System.exit(1);
      }

      if (input == '\n' || input == -1) { // if at end of file
        isDone = true;
        return;
      }
      if ((char)input == 's') {
        try {
          Thread.sleep(6000); // if a sleep character is found, producer sleeps for 6 seconds
        } catch (InterruptedException e) {}
      } 
      else 
        queue.enqueue((char)input);
    }
  }

}