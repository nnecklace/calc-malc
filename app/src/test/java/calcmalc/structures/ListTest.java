package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

public class ListTest {
    @Test
    public void testListIsCreatedWithCorrectSize() {
        List<Integer> list = new List<>();
        assertEquals(list.getSpace(), 8);
    }
    
    @Test
    public void testElementCanBePushedToList() {
        List<Integer> list = new List<>();
        list.append(1);
        assertEquals(list.get(0), (Integer) 1);
    }

    @Test
    public void testElementsCanBePushedToList() {
        List<Integer> list = new List<>();
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        assertEquals(list.get(0), (Integer) 1);
        assertEquals(list.get(1), (Integer) 2);
        assertEquals(list.get(2), (Integer) 3);
        assertEquals(list.get(3), (Integer) 4);
    }

    @Test
    public void testListSizeGetExponentiallyLarger() {
        List<Integer> list = new List<>();
        for (int i = 0; i <= 8; ++i) {
            list.append(i);
        }
        assertEquals(list.getSpace(), 16);

        for (int i = 8; i < 16; ++i) {
            list.append(i);
        }

        assertEquals(list.getSpace(), 32);

        for (int i = 16; i < 32; ++i) {
            list.append(i);
        }

        assertEquals(list.getSpace(), 64);
    }

    @Test
    public void testGetElementAtIncorrectIndex() {
        List<Integer> list = new List<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer i = list.get(-1);
        });
    }

    @Test
    public void testGetElementAtIncorrectIndex2() {
        List<Integer> list = new List<>();
        list.append(1);
        list.append(2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer i = list.get(2);
        });
    }

    @Test
    public void testGetElementAtIncorrectIndex3() {
        List<Integer> list = new List<>();
        list.append(1);
        list.append(2);
        assertEquals((Integer) 1, list.get(0));
        assertEquals((Integer) 2, list.get(1));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer i = list.get(8);
        });
    }

    @Test
    public void testSetInsertsValueAtIndex() {
        List<Integer> list = new List<>();
        list.set(0, 2);
        assertEquals((Integer) 2, list.get(0));
    }

    @Test
    public void testSetReplacesValueAtIndex() {
        List<Integer> list = new List<>();
        list.append(1);
        list.append(5);
        list.append(7);
        assertEquals((Integer) 7, list.get(2));
        list.set(2, 2);
        assertEquals((Integer) 2, list.get(2));
    }

    @Test
    public void testListSetToPowerOfTwo() {
        List<Integer> list = new List<>();
        list.append(1); // head = 0
        list.append(1); // head = 1
        list.append(1); // head = 2
        list.append(1); // head = 3
        list.append(1); // head = 4
        list.append(1); // head = 5
        list.append(1); // head = 6
        assertEquals(8, list.getSpace());
        list.append(1); // head = 7
        list.set(7, 2);
        assertEquals(8, list.size());
        assertEquals((Integer) 2, list.get(7));
        assertEquals(16, list.getSpace());
    }

    @Test
    public void testListIsEmpty() {
        List<Integer> list = new List<>();

        assertTrue(list.isEmpty());
        list.append(0);
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testSetThrowsOnIllegalIndex() {
        List<Integer> list = new List<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(-1, 2);
        });
    }

    @Test
    public void testSetThrowsOnIllegalIndex2() {
        List<Integer> list = new List<>();

        list.append(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(2, 2);
        });
    }

    @Test
    public void testSetIncreasesTheSizeOfTheArray() {
        List<Integer> list = new List<>();

        list.append(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(2, 2);
        });
    }
}
