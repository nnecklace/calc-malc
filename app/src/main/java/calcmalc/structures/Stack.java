package calcmalc.structures;

import calcmalc.structures.Listable;

public class Stack<T> {
    private Listable<T> list;

    public Stack(Listable<T> list) {
        this.list = list;
    }

    public void push(T element) {
        list.push(element);
    }

    public T pop() {
        T top = list.getLast();

        if (top == null) {
            return null;
        }

        int head = list.getHead();
        list.remove(head);
        return top;
    }

    public T peek() {
        return list.getLast();
    }
}