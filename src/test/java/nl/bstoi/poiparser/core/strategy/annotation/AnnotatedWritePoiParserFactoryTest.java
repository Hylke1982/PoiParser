package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.strategy.WritePoiParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.OutputStream;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AnnotatedWritePoiParserFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWritePoiParserWithNullOutputStream() {
        AnnotatedWritePoiParserFactory annotatedWritePoiParserFactory = new AnnotatedWritePoiParserFactory();
        annotatedWritePoiParserFactory.createWritePoiParser(null);
    }

    @Test
    public void testCreateWritePoiParser() {
        final OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
        AnnotatedWritePoiParserFactory annotatedWritePoiParserFactory = new AnnotatedWritePoiParserFactory();
        WritePoiParser annotatedWritePoiParser = annotatedWritePoiParserFactory.createWritePoiParser(mockOutputStream);
        Assert.assertNotNull("Annotated writer poi parser cannot be null", annotatedWritePoiParser);
    }

}
