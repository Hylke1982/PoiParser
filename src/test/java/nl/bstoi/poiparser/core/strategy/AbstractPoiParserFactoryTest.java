package nl.bstoi.poiparser.core.strategy;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AbstractPoiParserFactoryTest {

    private AbstractPoiParserFactory abstractPoiParserFactory;

    @Before
    public void before() {
        abstractPoiParserFactory = new AbstractPoiParserFactory() {
        };
    }

    @Test
    public void testCreateNewWorkBookDefault() throws Exception {
        Assert.assertEquals(HSSFWorkbook.class, abstractPoiParserFactory.createNewWorkBook(null).getClass());
    }

    @Test
    public void testCreateNewWorkBookSXSSF() throws Exception {
        Assert.assertEquals(SXSSFWorkbook.class, abstractPoiParserFactory.createNewWorkBook(PoiType.SXSSF).getClass());
    }

    @Test
    public void testCreateNewWorkBookXSSF() throws Exception {
        Assert.assertEquals(XSSFWorkbook.class, abstractPoiParserFactory.createNewWorkBook(PoiType.XSSF).getClass());
    }

    @Test
    public void testCreateNewWorkBookHSSF() throws Exception {
        Assert.assertEquals(HSSFWorkbook.class, abstractPoiParserFactory.createNewWorkBook(PoiType.HSSF).getClass());
    }
}
