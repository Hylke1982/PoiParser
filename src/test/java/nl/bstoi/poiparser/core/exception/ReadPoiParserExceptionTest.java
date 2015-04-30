package nl.bstoi.poiparser.core.exception;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class ReadPoiParserExceptionTest {

    private ReadPoiParserException readPoiParserException1, readPoiParserException2, readPoiParserException3;

    @Before
    public void setUp() throws Exception {

        readPoiParserException1 = new ReadPoiParserException("message1");
        readPoiParserException2 = new ReadPoiParserException("message2", new NullPointerException());
        readPoiParserException3 = new ReadPoiParserException(123, 456, new IllegalStateException());

    }

    @Test
    public void testGetMessage1() {
        assertEquals("message1", readPoiParserException1.getMessage());
    }

    @Test
    public void testGetMessage2() {
        assertEquals("message2", readPoiParserException2.getMessage());
    }

    @Test
    public void testGetMessage3() {
        assertEquals("Unable to read row 123 at column 456", readPoiParserException3.getMessage());
    }

    @Test
    public void getRowNumber1() {
        assertEquals(0, readPoiParserException1.getRowNumber());
    }

    @Test
    public void getRowNumber2() {
        assertEquals(0, readPoiParserException2.getRowNumber());
    }

    @Test
    public void getRowNumber3() {
        assertEquals(123, readPoiParserException3.getRowNumber());
    }

    @Test
    public void getColumnNumber1() {
        assertEquals(0, readPoiParserException1.getColumnNumber());
    }

    @Test
    public void getColumnNumber2() {
        assertEquals(0, readPoiParserException2.getColumnNumber());
    }

    @Test
    public void getColumnNumber3() {
        assertEquals(456, readPoiParserException3.getColumnNumber());
    }

    @Test
    public void getCause1() {
        assertNull(readPoiParserException1.getCause());
    }

    @Test
    public void getCause2() {
        assertTrue(readPoiParserException2.getCause() instanceof NullPointerException);
    }

    @Test
    public void getCause3() {
        assertTrue(readPoiParserException3.getCause() instanceof IllegalStateException);
    }
}