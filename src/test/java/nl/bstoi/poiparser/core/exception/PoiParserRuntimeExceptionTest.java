package nl.bstoi.poiparser.core.exception;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class PoiParserRuntimeExceptionTest {

    private PoiParserRuntimeException poiParserRuntimeException1, poiParserRuntimeException2, poiParserRuntimeException3;

    @Before
    public void setUp() throws Exception {
        poiParserRuntimeException1 = new PoiParserRuntimeException("message1");
        poiParserRuntimeException2 = new PoiParserRuntimeException(new NullPointerException());
        poiParserRuntimeException3 = new PoiParserRuntimeException("message3", new IllegalStateException());
    }

    @Test
    public void testGetMessage1() {
        assertEquals("message1", poiParserRuntimeException1.getMessage());
    }

    @Test
    public void testGetMessage2() {
        assertEquals("java.lang.NullPointerException", poiParserRuntimeException2.getMessage());
    }

    @Test
    public void testGetMessage3() {
        assertEquals("message3", poiParserRuntimeException3.getMessage());
    }

    @Test
    public void getCause1() {
        assertNull(poiParserRuntimeException1.getCause());
    }

    @Test
    public void getCause2() {
        assertTrue(poiParserRuntimeException2.getCause() instanceof NullPointerException);
    }

    @Test
    public void getCause3() {
        assertTrue(poiParserRuntimeException3.getCause() instanceof IllegalStateException);
    }
}