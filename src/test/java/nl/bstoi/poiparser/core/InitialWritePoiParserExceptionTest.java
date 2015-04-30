package nl.bstoi.poiparser.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class InitialWritePoiParserExceptionTest {


    private InitialWritePoiParserException initialWritePoiParserException;

    @Test
    public void testGetMessage() {
        initialWritePoiParserException = new InitialWritePoiParserException(2323);
        assertEquals("Column with number 2323 is defined more than once, " +
                "use writeIgnore or change columnNumber", initialWritePoiParserException.getMessage());
    }

    @Test
    public void testGetColumnNumber() {
        initialWritePoiParserException = new InitialWritePoiParserException(2323);
        assertEquals(2323, initialWritePoiParserException.getColumnNumber());
    }

    @Test
    public void testGetRowNumber() {
        initialWritePoiParserException = new InitialWritePoiParserException(2323);
        assertEquals(0, initialWritePoiParserException.getRowNumber());
    }


}