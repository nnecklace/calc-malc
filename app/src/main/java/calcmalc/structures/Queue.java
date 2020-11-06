package calcmalc.structures;

import calcmalc.structures.Listable;

public class Queue<T> {
    private Listable<T> list;
    private int head;
    private int tail;

    public Queue(Listable<T> list) {
        this.list = list;
        this.tail = 0;
    }

    public void enqueue(T element) {
        list.push(element);
        head++;
    }

    public T dequeue() {
        if (tail == head) {
            return null;
        }

        T rear = list.get(tail);
        list.remove(tail);
        tail++;
        return rear;
    }

    public int getHead() {
        return head;
    }

    public int getTail() {
        return tail;
    }
}