package nl.bstoi.poiparser.core.strategy;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AbstractWritePoiParserTest {

    private AbstractWritePoiParser abstractWritePoiParser;


    @Before
    public void before(){
        Workbook workbook = Mockito.mock(Workbook.class);
        abstractWritePoiParser = new AbstractWritePoiParser(workbook) {};
    }

    @Test
    public void testGetSplittedPropertyNameSingleElement() throws Exception {
        final String propertyName = "single";
        String[] splittedPropertyName = abstractWritePoiParser.getSplittedPropertyName(propertyName);
        Assert.assertEquals(1,splittedPropertyName.length);
        Assert.assertEquals("single",splittedPropertyName[0]);
    }

    @Test
    public void testGetSplittedPropertyNameMultiElement() throws Exception {
        final String propertyName = "multi.elements.hallo";
        String[] splittedPropertyName = abstractWritePoiParser.getSplittedPropertyName(propertyName);
        Assert.assertEquals(3,splittedPropertyName.length);
        Assert.assertEquals("multi",splittedPropertyName[0]);
        Assert.assertEquals("elements",splittedPropertyName[1]);
        Assert.assertEquals("hallo",splittedPropertyName[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSplittedPropertyNameEmptyPropertyName() throws Exception {
        final String propertyName = "";
        abstractWritePoiParser.getSplittedPropertyName(propertyName);
    }
}
