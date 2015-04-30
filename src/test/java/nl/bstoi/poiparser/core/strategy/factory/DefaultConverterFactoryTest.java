package nl.bstoi.poiparser.core.strategy.factory;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.exception.NonExistentConverterException;
import nl.bstoi.poiparser.core.strategy.converter.StringConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Created by hylke on 22/04/15.
 */
public class DefaultConverterFactoryTest {

    private DefaultConverterFactory defaultConverterFactory;

    @Before
    public void setUp() throws Exception {
        defaultConverterFactory = new DefaultConverterFactory();
    }

    @Test
    public void testGetConverter() throws Exception {
        final Converter converter = defaultConverterFactory.getConverter(String.class);
        assertNotNull(converter);
        assertTrue(converter instanceof StringConverter);
    }

    @Test
    public void testGetConverterForNonExistingClass() throws Exception {
        try {
            defaultConverterFactory.getConverter(Vector.class);
            fail("Should not read this point");
        } catch (final NonExistentConverterException e) {
            assertEquals("No converter found for type java.util.Vector", e.getMessage());
        }
    }

    @Test
    public void testRegisterConverter() throws Exception {
        try {
            defaultConverterFactory.getConverter(Character.class);
            fail("Should not read this point");
        } catch (final NonExistentConverterException e) {
            assertEquals("No converter found for type java.lang.Character", e.getMessage());
        }
        defaultConverterFactory.registerConverter(Character.class, StringConverter.class);
        final Converter converter = defaultConverterFactory.getConverter(Character.class);
        assertNotNull(converter);
        assertTrue(converter instanceof StringConverter);
    }
}