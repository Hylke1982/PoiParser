package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.strategy.AbstractPoiParserFactory;
import nl.bstoi.poiparser.core.strategy.WritePoiParser;
import nl.bstoi.poiparser.core.strategy.WritePoiParserFactory;

import java.io.OutputStream;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AnnotatedWritePoiParserFactory extends AbstractPoiParserFactory implements WritePoiParserFactory {
    public WritePoiParser createWritePoiParser(OutputStream outputStream) {
        if (null == outputStream) throw new IllegalArgumentException("Output stream cannot be null.");
        return new AnnotatedWritePoiParser(outputStream, createNewWorkBook(getPoiType()));
    }
}
