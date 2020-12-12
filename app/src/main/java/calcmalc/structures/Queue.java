package calcmalc.structures;

/**
 * Queue data structure
 * Queue is a FIFO data structure similar to a list. Each operation is a constant time O(1) operation.
 * Elements are added to the start of the queue and pulled from the end of the queue
 * @param <T> the generic type to be contained in the queue
 */
public class Queue<T> {
    private QueueNode<T> head;
    private QueueNode<T> tail;
    private int size = 0;

    /**
     * Method adds element to the "head" or start of the queue
     * @param element The generic element to be added
     */
    public void enqueue(T element) {
        QueueNode<T> next = new QueueNode<>(element);
        if (head == null) {
            head = tail = next;
        } else {
            if (size == 1) {
                head.next(next);
            } else {
                tail.next(next);
            }
            tail = next;
        }
        size++;
    }

    /**
     * Checks the next element in the queue to dequeue
     * @return the next element in the queue
     */
    public T peekFirst() {
        return head != null ? head.getValue() : null;
    }

    public T peekLast() {
        return tail != null ? tail.getValue() : null;
    }

    /**
     * Method pulls element from the end of the queue. The element is not removed from the underlying list
     * @return T the generic element at the end of the queue or null
     */
    public T dequeue() {
        if (head != null) {
            size--;
            T current = head.getValue();
            head = head.getNext();

            if (size == 0) {
                tail = null;
            } 

            return current;
        }

        return null;
    }

    /**
     * Method checks if queue is empty. Queue is always empty if head and tail point to the same position in the underlying arras
     * @return true if empty, otherwise false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Method gets size of the queue
     * @return size (#elements) of the queue
     */
    public int size() {
        return size;
    }
}