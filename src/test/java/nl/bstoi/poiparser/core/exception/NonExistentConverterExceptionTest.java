package nl.bstoi.poiparser.core.exception;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 30/04/15.
 */
public class NonExistentConverterExceptionTest {

    public NonExistentConverterException nonExistentConverterException1, nonExistentConverterException2;

    @Before
    public void before() {
        nonExistentConverterException1 = new NonExistentConverterException("message1");
        nonExistentConverterException2 = new NonExistentConverterException("message2", new IllegalStateException());
    }

    @Test
    public void testGetMessage1() {
        assertEquals("message1", nonExistentConverterException1.getMessage());
    }

    @Test
    public void testGetMessage2() {
        assertEquals("message2", nonExistentConverterException2.getMessage());
    }

    @Test
    public void getCause1() {
        assertNull(nonExistentConverterException1.getCause());
    }

    @Test
    public void getCause2() {
        assertTrue(nonExistentConverterException2.getCause() instanceof IllegalStateException);
    }

}