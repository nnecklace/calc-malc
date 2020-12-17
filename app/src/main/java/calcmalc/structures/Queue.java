package calcmalc.structures;

/**
 * Queue data structure
 * Queue is a FIFO data structure similar to a list. Each operation is a constant time O(1) operation.
 * Elements are added to the start of the queue and pulled from the end of the queue
 * The underlying queue is actually a singly linked list data structure
 * @param <T> the generic type to be contained in the queue
 * @author nnecklace
 */
public class Queue<T> {
    /**
     * The head node of the queue, also means the start of the queue
     */
    private QueueNode<T> head;
    /**
     * The tail node of the queue, also means the end of the queue
     */
    private QueueNode<T> tail;
    /**
     * The size of the queue, i.e., how many nodes inside the queue
     */
    private int size = 0;

    /**
     * Method adds element to the "head" or start of the queue
     * If queue is empty the head and tail of the queue will be the same element
     * If the queue contains 1 element, the following node will be placed next to the head
     * Next node will always be the tail of the queue
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
     * Checks the next element in the queue to dequeue, also means to check the head of the queue
     * @return the next (head) value in the queue
     */
    public T peekFirst() {
        return head != null ? head.getValue() : null;
    }

    /**
     * Check the last element in the queue, also means to check the tail of the queue
     * @return the tail node value of the last element in the queue
     */
    public T peekLast() {
        return tail != null ? tail.getValue() : null;
    }

    /**
     * Method pulls head from the the queue, meaning getting the next element in the queue. 
     * When the last element is pulled, both tail and head will be null
     * @return T the generic head node element at the of the queue or null if the queue is empty
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
     * Method checks if queue is empty by checking the size of the queue. 
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