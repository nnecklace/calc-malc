package calcmalc.structures;

/**
 * QueueNode represents a node inside the linked list queue data structure
 * Each Node contains a pointer to the follwing node in the list
 * @author nnecklace
 */
public class QueueNode<T> {
    /**
     * The value contained in the queue node
     */
    private T value;
    /**
     * The pointer to the next node in the linked list
     */
    private QueueNode<T> next;

    /**
     * The contructor for the linked list node
     * @param value to be contained inside the node
     */
    public QueueNode(T value) {
        this.value = value;
    }

    /**
     * Getter method for the value property
     * @return the value stored inside the node
     */
    public T getValue() {
        return value;
    }

    /**
     * Inserts a new node into the linked list
     * The method works like a setter for the next property
     * @param next node to be placed in the queue
     */
    public void next(QueueNode<T> next) {
        this.next = next;
    } 

    /**
     * Getter for the next node in the linked list (relative to this node)
     * @return This node's next node property
     */
    public QueueNode<T> getNext() {
        return next;
    }
    
}
