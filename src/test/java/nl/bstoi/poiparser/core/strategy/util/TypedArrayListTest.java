package nl.bstoi.poiparser.core.strategy.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by hylke on 22/04/15.
 */
public class TypedArrayListTest {


    private TypedArrayList<String> typedArrayList1, typedArrayList2, typedArrayList3;

    @Before
    public void before() {
        typedArrayList1 = new TypedArrayList<String>(String.class);
        typedArrayList2 = new TypedArrayList<String>(String.class, new HashSet<String>());
        typedArrayList3 = new TypedArrayList<String>(String.class, 1);
    }

    @Test
    public void testGetType1() throws Exception {
        assertEquals(String.class, typedArrayList1.getType());
    }

    @Test
    public void testGetType2() throws Exception {
        assertEquals(String.class, typedArrayList2.getType());
    }

    @Test
    public void testGetType3() throws Exception {
        assertEquals(String.class, typedArrayList3.getType());
    }
}