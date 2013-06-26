package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.strategy.ReadPoiParser;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
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
        Assert.assertEquals(3, testRowClasses.size());
        Assert.assertThat(testRowClasses, Matchers.hasItem(TestHelper.createTestRowMatcher(0L, "z", null, "2010", "no setter", null, null, null, null)));
        Assert.assertThat(testRowClasses, Matchers.hasItem(TestHelper.createTestRowMatcher(1L, "Naam1", null, "2013", "no setter", null, null, null, null)));
        Assert.assertThat(testRowClasses, Matchers.hasItem(TestHelper.createTestRowMatcher(2L, "Naam2", null, "2011", "no setter", null, null, null, null)));

    }

    @Test
    public void testReadWithEmbedded() throws Exception {
        final String fileName = "test-excel-001.xls";
        final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath + fileName).toURI());
        AnnotatedPoiParserFactory<EmbeddedTestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<EmbeddedTestRow>(EmbeddedTestRow.class);
        ReadPoiParser<EmbeddedTestRow> testRowAnnotatedPoiParser = testRowAnnotatedPoiParserFactory.createReadPoiParser(new FileInputStream(excelFile), "EmbeddedTest");

        List<EmbeddedTestRow> testRowClasses = testRowAnnotatedPoiParser.read();
        Assert.assertEquals(2, testRowClasses.size());
        Assert.assertThat(testRowClasses, Matchers.hasItem(TestHelper.createEmbeddedTestRowMatcher(new Short("111"), new Integer("222"), new Long("333"), new BigDecimal("444"))));
        Assert.assertThat(testRowClasses, Matchers.hasItem(TestHelper.createEmbeddedTestRowMatcher(new Short("555"), new Integer("666"), null,null)));

    }
}
