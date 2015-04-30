package nl.bstoi.poiparser.core.exception;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class PoiParserExceptionTest {

    private PoiParserException poiParserException1, poiParserException2, poiParserException3, poiParserException4;

    @Before
    public void setUp() throws Exception {
        poiParserException1 = new PoiParserException("message1");
        poiParserException2 = new PoiParserException("message2", new IllegalStateException());
        poiParserException3 = new PoiParserException(123, 456, "message3");
        poiParserException4 = new PoiParserException(345, 567, "message4", new NullPointerException());
    }

    @Test
    public void testGetRowNumber1() throws Exception {
        assertEquals(0, poiParserException1.getRowNumber());
    }

    @Test
    public void testGetRowNumber2() throws Exception {
        assertEquals(0, poiParserException2.getRowNumber());
    }

    @Test
    public void testGetRowNumber3() throws Exception {
        assertEquals(123, poiParserException3.getRowNumber());
    }

    @Test
    public void testGetRowNumber4() throws Exception {
        assertEquals(345, poiParserException4.getRowNumber());
    }

    @Test
    public void testGetColumnNumber1() throws Exception {
        assertEquals(0, poiParserException1.getColumnNumber());
    }

    @Test
    public void testGetColumnNumber2() throws Exception {
        assertEquals(0, poiParserException2.getColumnNumber());
    }

    @Test
    public void testGetColumnNumber3() throws Exception {
        assertEquals(456, poiParserException3.getColumnNumber());
    }

    @Test
    public void testGetColumnNumber4() throws Exception {
        assertEquals(567, poiParserException4.getColumnNumber());
    }

    @Test
    public void testGetMessage1() {
        assertEquals("message1", poiParserException1.getMessage());
    }

    @Test
    public void testGetMessage2() {
        assertEquals("message2", poiParserException2.getMessage());
    }

    @Test
    public void testGetMessage3() {
        assertEquals("message3", poiParserException3.getMessage());
    }

    @Test
    public void testGetMessage4() {
        assertEquals("message4", poiParserException4.getMessage());
    }

    @Test
    public void testGetCause1() {
        assertNull(poiParserException1.getCause());
    }

    @Test
    public void testGetCause2() {
        assertTrue(poiParserException2.getCause() instanceof IllegalStateException);
    }

    @Test
    public void testGetCause3() {
        assertNull(poiParserException3.getCause());
    }

    @Test
    public void testGetCause4() {
        assertTrue(poiParserException4.getCause() instanceof NullPointerException);
    }

}