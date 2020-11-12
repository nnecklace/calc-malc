package calcmalc.structures;

import calcmalc.structures.Listable;

/**
 * List data structure. This is the same as javas ArrayList
 * All operations are done in constant time O(1) apart from the grow function which is only called when the size of the list is a power of 2
 * @param <T> the generic type to be contained in the list
 */
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

    /**
     * Method retrives an element from the list at the desired index
     * @param index the position of the element to be retrieved
     * @return T the generic element at the given position of the list or null if index is negitive or above the current size of the list
     */
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return list[index];
    }
    
    /**
     * Method gets the last element from the list
     * @return T the last element in the list or null if the list is empty
     */
    public T getLast() {
        if (head == 0) {
            return null;
        }

        return list[head - 1];
    }

    /**
     * Method getter for size property
     * @return size property
     */
    public int getSpace() {
        return size;
    }

    /**
     * Method getter for head property
     * @return head property
     */
    public int size() {
        return head;
    }

    /**
     * Method "removes" element at the given index. Removes implies that the element will be null
     * @param index index of the element to be removed
     */
    public void remove(int index) {
        if (index < size && index >= 0) {
            list[index] = null;
            if (index == head) {
                head--;
            }
        }
    }

    /**
     * Method pushes element to the end of the list. If list is full list remains unchanged.
     * If head has reached the end of the list, the list size will be increased.
     * @param element The element to push to the end of the list
     */
    public void push(T element) {
        if (isFull) {
            return;
        }

        if (head == size) {
            grow();
        }

        list[head++] = element;
    }

    /**
     * Method increases the size of the list by doubling it.
     * Method creates a new list and copies the elements from the old list to the new list, and discards the old list.
     */
    private void grow() {
        if (size * 2 > MAX_SIZE) {
            size = MAX_SIZE;
            isFull = true;
        } else {
            size *= 2;
        }

        T[] copy = (T[]) new Object[size];

        for (int i = 0; i < size / 2; ++i) {
            copy[i] = list[i]; 
        }

        list = copy;
    }

    /**
     * Method getter for isFull property
     * @return isFull property
     */
    public boolean isFull() {
        return isFull;
    }

    public boolean isEmpty() {
        return head == 0;
    }
}
