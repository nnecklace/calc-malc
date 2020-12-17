package calcmalc.structures;

/**
 * Generic container class for a HashTable entry
 * @param <V> value of the entry to store in the HashTable
 * @author nnecklace
 */
public class HashTableEntry<V> {
    /**
     * The value stored in this entry
     */
    private V value;
    /**
     * The key string associated with the entry
     */
    private String key;
    /**
     * The hashcode associated with the key for this entry
     */
    private int hash;

    /**
     * Constructor for a HashTableEntry element 
     * @param key the key associated with the entry
     * @param value  the value associated with the entry
     * @param hash the hashcode for the entry
     */
    public HashTableEntry(String key, V value, int hash) {
        this.key = key;
        this.value = value;
        this.hash = hash;
    }

    /**
     * Getter for key property
     * @return key
     */
    public String getKey() {
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
