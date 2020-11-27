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
        HashTable<String, String> h = new HashTable<String, String>();
        String entry = h.get("Test");
        assertNull(entry);
    }

    @Test
    public void testHashTablePlaceOnEmpty() {
        HashTable<String, String> h = new HashTable<String, String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Test");
        assertEquals("Not test", entry);
    }

    @Test
    public void testHashTablPlaceAndFindMany() {
        HashTable<String, String> h = new HashTable<String, String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Test");
        assertEquals("Not test", entry);
        h.placeOrUpdate("Test 1", "really is test ");
        entry = h.get("Test");
        assertEquals("Not test", entry);
    }

    @Test
    public void testHashTablPlaceAndFindWithCollision() {
        HashTable<String, String> h = new HashTable<String, String>();
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
        HashTable<String, Integer> h = new HashTable<>();
        HashMap<String, Integer> map = new HashMap<>();
        List<String> list = new List<>();
        for (int i = 1; i <= 30000; ++i) {
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));
            h.placeOrUpdate(generatedString, i);
            map.put(generatedString, i);
            list.push(generatedString);
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
        HashTable<String, String> h = new HashTable<String, String>();
        h.placeOrUpdate("Test", "Not test");
        String entry = h.get("Who dis?");
        assertNull(entry);
    }

    @Test
    public void testHashTableShouldReturnNullWhenCollisionButDifferentKey() {
        HashTable<String, String> h = new HashTable<String, String>();
        h.placeOrUpdate("Siblings", "Not test");
        // same hashcode different string
        assertNull(h.get("Teheran"));
    }

    @Test
    public void testHashTableShouldReturnNullWhenStringSameDifferentHash() {
        HashTable<TestObj, String> h = new HashTable<>();

        TestObj t = new TestObj() {
            public String getValue() {
                return "Test";
            }

            public boolean equals(Object o) {
                TestObj z = (TestObj) o;
                return z.getValue().equals(getValue());
            }

            public int hashCode() {
                return 2;
            }
        };

        h.placeOrUpdate(t, "test");

        TestObj t2 = new TestObj() {
            public String getValue() {
                return "Test";
            }

            public boolean equals(Object o) {
                TestObj z = (TestObj) o;
                return z.getValue().equals(getValue());
            }

            public int hashCode() {
                return 3;
            }
        };

        assertNull(h.get(t2));
    }

    @Test
    public void testHashTableReturnNullEvenWithSameIndex() {
        HashTable<String, String> h = new HashTable<>();
        h.placeOrUpdate("Test", "Hello");
        // Test and Dest will be placed in the same index
        assertNull(h.get("Dest"));
    }
}
