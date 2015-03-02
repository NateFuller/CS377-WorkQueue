class LimitedQueue {
  static int queue_cap = 10; // queue capacity
  int head = 0; // pointer to the front of the queue
  int size = 0; // current usage
  char[] elements = new char[queue_cap]; // container for elements

  public void debug() {
    System.out.print ("head= " + head + " size= " + size);
    System.out.print("[ ");
    for (char element : elements) 
      System.out.print("" + element + ", ");
    System.out.println("]");
  }

  public boolean enqueue(char elem) {
    if (isFull())
      return false;
    elements[head] = elem;
    head = head == 0 ? queue_cap-1 : head-1;
    size++;
    return true;
  }

  public Character dequeue() {
    if (isEmpty())
      return null;
    char to_return = elements[(head+size)%queue_cap]; 
    size--;
    return to_return;
  }

  public boolean isFull() {
    return size == queue_cap;
  }

  public boolean isEmpty() {
    return size == 0;
  }

}
