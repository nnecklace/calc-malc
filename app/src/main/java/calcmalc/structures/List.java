package calcmalc.structures;

import calcmalc.structures.Listable;

public class List<T> implements Listable<T> {
    private T[] list;
    private int head;
    private int size;
    private boolean isFull;
    private static final int MAX_SIZE = 10000000;

    public List() {
        size = 8;
        list = (T[]) new Object[size]; // should not be done, but Java doesn't support generic arrays
        head = 0;
        isFull = false;
    }

    public T get(int index) {
        if (index > size && index < 0) {
            return null;
        }

        return list[index];
    }
    
    public T getLast() {
        if (head == 0) {
            return null;
        }

        return list[head-1];
    }

    public int getSize() {
        return size;
    }

    public int getHead() {
        return head;
    }

    public void remove(int index) {
        if (index <= size && index >= 0) {
            list[index] = null;
            if (index == head) head--;
        }
    }

    public void push(T element) {
        if (isFull) {
            return;
        }

        if (head == size) {
            grow();
        }

        list[head++] = element;
    }

    private void grow() {
        if (size*2 > MAX_SIZE) {
            size = MAX_SIZE;
            isFull = true;
        } else {
            size *= 2;
        }

        T[] copy = (T[]) new Object[size];

        for (int i = 0; i < size/2; ++i) {
            copy[i] = list[i]; 
        }

        list = copy;
    }

    public boolean isFull() {
        return isFull;
    }
}
