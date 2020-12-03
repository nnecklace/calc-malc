package calcmalc.structures;

/**
 * @author nnecklace
 */
public interface Listable<T> {
    /**
     * Getter for an index of the list
     * @param index location to get
     * @return T element at the specified index
     */
    public T get(int index);
    /**
     * Setter  for  an index of the list
     * @param index the index to set
     * @param value the value to set
     */
    public void set(int index, T value);
    /**
     * Should return the count of elements in a list
     * @return the count of elements
     */
    public int size();
    /**
     * Should ideally return the size of the underlying array
     * @return look above
     */
    public int getSpace();
    /**
     * Should ideally insert an element at the end of a list
     * @param element the element to insert
     */
    public void push(T element);
    /**
     * Should remove an element at a specified location
     * @param index the location to remove
     * @return The removed element
     */
    public T remove(int index);
    /**
     * Should tell if list contains no elements
     * @return ideally true if no elements otherwise false
     */
    public boolean isEmpty();
}