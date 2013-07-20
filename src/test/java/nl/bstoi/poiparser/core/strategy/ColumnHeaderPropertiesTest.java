package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.strategy.ColumnHeaderProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * Hylke Stapersma (codecentric nl)
 * hylke.stapersma@codecentric.nl
 */
public class ColumnHeaderPropertiesTest {

    private Properties properties = new Properties();
    private ColumnHeaderProperties columnHeaderProperties;

    @Before
    public void before() {
        columnHeaderProperties = new ColumnHeaderProperties(properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullProperties() {
        new ColumnHeaderProperties(null);
    }


    @Test
    public void testAddColumnHeader() throws Exception {
        columnHeaderProperties.addColumnHeader("sheetA", "bla", "hallo");
        Assert.assertEquals("hallo", properties.getProperty("sheetA.bla"));
    }

    @Test
    public void testGetColumnHeader() throws Exception {
        properties.setProperty("sheetA.bla", "hallo");
        Assert.assertEquals("hallo", columnHeaderProperties.getColumnHeader("sheetA", "bla"));
    }

    @Test
    public void testContainsColumnHeader() throws Exception {
        properties.setProperty("sheetA.bla", "hallo");
        Assert.assertTrue(columnHeaderProperties.containsColumnHeader("sheetA", "bla"));
    }

    @Test
    public void testContainsColumnHeaderWithUnknownKey() throws Exception {
        Assert.assertFalse(columnHeaderProperties.containsColumnHeader("sheetA", "bla"));
    }
}
