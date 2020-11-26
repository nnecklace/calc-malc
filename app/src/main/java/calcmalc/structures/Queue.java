package calcmalc.structures;

/**
 * Queue data structure
 * Queue is a FIFO data structure similar to a list. Each operation is a constant time O(1) operation.
 * Elements are added to the start of the queue and pulled from the end of the queue
 * @param <T> the generic type to be contained in the queue
 */
public class Queue<T> {
    private Listable<T> list;
    private int head;
    private int tail;

    public Queue(Listable<T> list) {
        this.list = list;
        this.tail = 0;
        this.head = list.size();
    }

    /**
     * Method adds element to the "head" or start of the queue
     * @param element The generic element to be added
     */
    public void enqueue(T element) {
        list.push(element);
        head++;
    }

    public T peek() {
        return list.get(tail);
    }

    /**
     * Method pulls element from the end of the queue. The element is not removed from the underlying list
     * @return T the generic element at the end of the queue or null
     */
    public T dequeue() {
        if (tail == head) {
            return null;
        }

        T rear = list.get(tail);

        tail++;

        return rear;
    }

    /**
     * Method getter for head property
     * @return head property
     */
    public int getHead() {
        return head;
    }

    /**
     * Method getter for tail property
     * @return tail property
     */
    public int getTail() {
        return tail;
    }

    /**
     * Method checks if queue is empty. Queue is always empty if head and tail point to the same position in the underlying arras
     * @return true if empty, otherwise false
     */
    public boolean isEmpty() {
        return head == tail;
    }

    /**
     * Method gets size of the queue
     * @return size (#elements) of the queue
     */
    public int size() {
        return head - tail;
    }
}