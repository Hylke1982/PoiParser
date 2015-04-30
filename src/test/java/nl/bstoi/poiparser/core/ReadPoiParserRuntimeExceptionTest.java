package nl.bstoi.poiparser.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class ReadPoiParserRuntimeExceptionTest {


    @Test
    public void getMessage1() {
        final ReadPoiParserRuntimeException readPoiParserRuntimeException = new ReadPoiParserRuntimeException();
        assertEquals("Error while reading excel file", readPoiParserRuntimeException.getMessage());
    }

    @Test
    public void getMessage2() {
        final ReadPoiParserRuntimeException readPoiParserRuntimeException = new ReadPoiParserRuntimeException("other message");
        assertEquals("other message", readPoiParserRuntimeException.getMessage());
    }

}