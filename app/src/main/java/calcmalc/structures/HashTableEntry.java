package calcmalc.structures;

/**
 * Generic container class for HashTable
 * @param <K,V> key value pair to store in the HashTable
 */
public class HashTableEntry<K, V> {
    private V value;
    private K key;
    private int hash;

    public HashTableEntry(K key, V value, int hash) {
        this.key = key;
        this.value = value;
        this.hash = hash;
    }

    /**
     * Getter for key property
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * Getter for value property
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * Setter for value property
     * @param value to be set
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Hashcode function is actually just a getter for hash property
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
