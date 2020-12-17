package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

interface TestObj {
    public boolean equals(Object o);
    public int hashCode();
    public String getValue();
}

public class HashTableTest {
    @Test
    public void testHashTableGetOnEmpty() {
        HashTable<String> h = new HashTable<String>();
        String entry = h.get("Test");
        assertNull(entry);
    }

    @Test
    public void testHashTablePlaceOnEmpty() {
        HashTable<String> h = new HashTable<String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Test");
        assertEquals("Not test", entry);
    }

    @Test
    public void testHashTablPlaceAndFindMany() {
        HashTable<String> h = new HashTable<String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Test");
        assertEquals("Not test", entry);
        h.placeOrUpdate("Test 1", "really is test ");
        entry = h.get("Test");
        assertEquals("Not test", entry);
    }

    @Test
    public void testHashTablPlaceAndFindWithCollision() {
        HashTable<String> h = new HashTable<String>();
        h.placeOrUpdate("Siblings", "Not test");
        String entry = h.get("Siblings"); // these two strings have the same hashcode
        assertEquals("Not test", entry);
        h.placeOrUpdate("Teheran", "really is test");
        entry = h.get("Teheran");
        assertEquals("really is test", entry);
    }

    @Test
    public void testHashTableAgainstHashMap() {
        byte[] array = new byte[7]; // length is bounded by 7
        HashTable<Integer> h = new HashTable<>();
        HashMap<String, Integer> map = new HashMap<>();
        List<String> list = new List<>();
        for (int i = 1; i <= 30000; ++i) {
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));
            h.placeOrUpdate(generatedString, i);
            map.put(generatedString, i);
            list.append(generatedString);
        }

        int failCount = 0;

        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            if (h.get(s).intValue() != map.get(s).intValue()) {
                failCount++;
            }
        }

        assertEquals(0, failCount);
    }

    @Test
    public void testHashTableShouldReturnNullOnNonExistingEntries() {
        HashTable<String> h = new HashTable<String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Who dis?");
        assertNull(entry);
    }

    @Test
    public void testHashTableShouldReturnNullWhenCollisionButDifferentKey() {
        HashTable<String> h = new HashTable<String>();
        h.placeOrUpdate("Siblings", "Not test");
        // same hashcode different string
        assertNull(h.get("Teheran"));
    }

    @Test
    public void testHashTableReturnNullEvenWithSameIndex() {
        HashTable<String> h = new HashTable<>();
        h.placeOrUpdate("Test", "Hello");
        // Test and Dest will be placed in the same index
        assertNull(h.get("Dest"));
    }

    @Test
    public void testHashTableReturnNullEvenWithSameIndex2() {
        HashTable<String> h = new HashTable<>();
        h.placeOrUpdate("Test", "Hello");
        h.placeOrUpdate("Dest", "World");
        // Test and Dest will be placed in the same index
        assertEquals("World", h.get("Dest"));
    }

    @Test
    public void testHashTableSameKeyManyTimes() {
        HashTable<String> h = new HashTable<>();
        h.placeOrUpdate("Test", "Hello");
        h.placeOrUpdate("Test", "World");
        assertEquals("World", h.get("Test"));
        h.placeOrUpdate("Test", "World1");
        h.placeOrUpdate("Test", "really test this time");
        assertEquals("really test this time", h.get("Test"));
    }
}
