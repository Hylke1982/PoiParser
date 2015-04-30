package nl.bstoi.poiparser.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class WritePoiParserExceptionTest {

    private WritePoiParserException writePoiParserException1, writePoiParserException2;

    @Before
    public void before() {
        writePoiParserException1 = new WritePoiParserException(234, 5426, "message1");
        writePoiParserException2 = new WritePoiParserException(345, 6543, "message2", new IllegalStateException());
    }

    @Test
    public void testGetRowNumber1() {
        assertEquals(234, writePoiParserException1.getRowNumber());
    }

    @Test
    public void testGetRowNumber2() {
        assertEquals(345, writePoiParserException2.getRowNumber());
    }

    @Test
    public void testGetCause1() {
        assertNull(writePoiParserException1.getCause());
    }

    @Test
    public void testGetCause2() {
        assertTrue(writePoiParserException2.getCause() instanceof IllegalStateException);
    }

    @Test
    public void testGetMessage1() {
        assertEquals("message1", writePoiParserException1.getMessage());
    }

    @Test
    public void testGetMessage2() {
        assertEquals("message2", writePoiParserException2.getMessage());
    }

    @Test
    public void testGetColumnNumber1() {
        assertEquals(5426, writePoiParserException1.getColumnNumber());
    }

    @Test
    public void testGetColumnNumber2() {
        assertEquals(6543, writePoiParserException2.getColumnNumber());
    }


}