package calcmalc.structures;

public interface Listable<T> {
    public T get(int index);
    public void set(int index, T value);
    public int size();
    public int getSpace();
    public void push(T element);
    public T remove(int index);
    public boolean isEmpty();
}