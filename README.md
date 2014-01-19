jpm-utils
=========

A couple of solutions to common programming challenges:

1. Implement a method which reverses a string (e.g., reverse("abc") returns "cba") - see [StringUtils](BitResolution/jpm-utils/blob/master/src/main/java/com/bitresolution/jpm/utils/StringUtils.java)

2. Implement a blocking FIFO queue - see [FifoQueue](BitResolution/jpm-utils/blob/master/src/main/java/com/bitresolution/jpm/utils/FifoQueue.java), [ArrayListBlockingFifoQueue](BitResolution/jpm-utils/blob/master/src/main/java/com/bitresolution/jpm/utils/ArrayListBlockingFifoQueue.java) and [LinkedListBlockingFifoQueue](BitResolution/jpm-utils/blob/master/src/main/java/com/bitresolution/jpm/utils/LinkedListBlockingFifoQueue.java)
    * The queue implementation should implement the following interface:
    ```java    
    interface FIFOQueue {
        public void enqueue(Object obj);
        public Object dequeue();
    }
    ```

    * The queue must be thread-safe to >2 threads accessing it concurrently.
    * The queue must have a size, which equals the number of objects currently enqueued.
    * The queue must have a finite capacity, meaninng a maximum number of objects that can ever be enqueued at a single time.
    *  When attempting to enqueue an object when the size of the queue is equal to or greater than the capacity,
the enqueue operation should block until space becomes available within the queue.
    * When attempting to dequeue an object when the size of the queue is zero (the queue is empty), the dequeue operation should block until an object becomes available.
    * The implementation should implement the internal object storage using a List implementation
    * Use of the java.util.concurrent package is not allowed
