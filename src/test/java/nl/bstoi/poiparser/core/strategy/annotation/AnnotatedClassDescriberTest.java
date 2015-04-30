package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import nl.bstoi.poiparser.core.strategy.annotation.structures.EmbeddedAndCellOnSingleField;
import nl.bstoi.poiparser.core.strategy.annotation.structures.EmbeddedRecursionTestRow;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hylke on 21/04/15.
 */
public class AnnotatedClassDescriberTest {

    private AnnotatedClassDescriber annotatedClassDescriber;

    @Before
    public void setUp() throws Exception {
        annotatedClassDescriber = AnnotatedClassDescriber.getInstance();
    }

    @Test
    public void testSameInstance() throws Exception {
        assertSame(annotatedClassDescriber, AnnotatedClassDescriber.getInstance());
    }

    @Test
    public void testGetCellDescriptorsForClassWithNotAllowedRecursion() {
        try {
            annotatedClassDescriber.getCellDescriptorsForClass(EmbeddedRecursionTestRow.class);
            fail("Should not reach this point");
        } catch (final PoiParserRuntimeException e) {
            assertEquals("Declaring class cannot be the same as the field type (recursion is not supported)", e.getMessage());
        }
    }

    @Test
    public void testCellAndEmbeddedOnFieldOrPropertyShouldThrowExeception(){
        try {
            annotatedClassDescriber.getCellDescriptorsForClass(EmbeddedAndCellOnSingleField.class);
            fail("Should not reach this point");
        } catch (final PoiParserRuntimeException e) {
            assertEquals("A field cannot be annotated with @Cell and @Embedded", e.getMessage());
        }
    }
}