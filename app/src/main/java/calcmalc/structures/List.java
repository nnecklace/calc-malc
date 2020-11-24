package calcmalc.structures;

/**
 * List data structure. This is the same as javas ArrayList
 * All operations are done in constant time O(1) apart from the grow function which is only called when the size of the list is a power of 2
 * @param <T> the generic type to be contained in the list
 */
public class List<T> implements Listable<T> {
    private T[] list;
    private int head;
    private int size;

    public List() {
        size = 8; // start at 8, usually start at 1 but can be started on any other power of 2
        list = (T[]) new Object[size]; // should not be done, but Java doesn't support generic arrays
        head = 0;
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
     * Method for setting values at any index in the list
     * @param index to be placed
     * @param value to be inserted
     */
    public void set(int index, T value) {
        if (index >= 0) {
            if (index > size) {
                grow(index);
            }
            list[index] = value;
            if (index >= head) {
                head = index + 1;
            }
        } else {
            throw new IllegalArgumentException("Index out of range");
        }
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
        if (head == size) {
            grow();
        }

        list[head++] = element;
    }

    /**
     * Method is same as {@link grow} but calls it with default parameter 
     */
    private void grow() {
        grow(size + 1);
    }

    /**
     * Method increases the size of the list by doubling it until it is larger than the new requested size.
     * Method creates a new list and copies the elements from the old list to the new list, and discards the old list.
     */
    private void grow(int newSize) {
        while (newSize > size) {
            size *= 2;
        }

        T[] copy = (T[]) new Object[size];

        for (int i = 0; i < size / 2; ++i) {
            copy[i] = list[i]; 
        }

        list = copy;
    }

    /**
     * Boolean function to check wheather lis is empty or not
     * @return true if head is at position 0 meaning that the list is empty
     */
    public boolean isEmpty() {
        return head == 0;
    }
}
