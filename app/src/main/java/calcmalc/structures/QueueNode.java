package calcmalc.structures;

public class QueueNode<T> {
    private T value;
    private QueueNode<T> next;

    public QueueNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void next(QueueNode<T> next) {
        this.next = next;
    } 

    public QueueNode<T> getNext() {
        return next;
    }
    
}
