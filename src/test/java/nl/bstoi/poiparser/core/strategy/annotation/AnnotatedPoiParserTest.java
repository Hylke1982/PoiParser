package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.strategy.ReadPoiParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 22:37
 */
public class AnnotatedPoiParserTest {

    private static final String filePath = "/excel/";

    @Test
    public void testRead() throws Exception {
        final String fileName = "test-excel-001.xls";
        final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath + fileName).toURI());
        AnnotatedPoiParserFactory<TestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<TestRow>(TestRow.class);
        ReadPoiParser<TestRow> testRowAnnotatedPoiParser = testRowAnnotatedPoiParserFactory.createReadPoiParser(new FileInputStream(excelFile), "Sheet2");

        List<TestRow> testRowClasses = testRowAnnotatedPoiParser.read();
        Assert.assertEquals(2, testRowClasses.size());

    }
}
