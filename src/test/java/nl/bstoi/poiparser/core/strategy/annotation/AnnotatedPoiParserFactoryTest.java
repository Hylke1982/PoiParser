package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import nl.bstoi.poiparser.core.matcher.CellDescriptorMatcher;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:32
 */
public class AnnotatedPoiParserFactoryTest {


    public void testCreate() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullInputStream() throws Exception {
        AnnotatedPoiParserFactory<TestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<TestRow>(TestRow.class);
        testRowAnnotatedPoiParserFactory.createReadPoiParser(null, "sheet");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullSheetName() throws Exception {
        AnnotatedPoiParserFactory<TestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<TestRow>(TestRow.class);
        InputStream is = Mockito.mock(InputStream.class);
        testRowAnnotatedPoiParserFactory.createReadPoiParser(is, null);
    }

    @Test
    public void testGetCellDescriptors() throws Exception {
        AnnotatedPoiParserFactory<TestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<TestRow>(TestRow.class);
        Set<CellDescriptor> cellDescriptors = testRowAnnotatedPoiParserFactory.getCellDescriptors();
        Assert.assertNotNull("Expect cell descriptor not to be null", cellDescriptors);
//        Assert.assertEquals("Expected 9 cell descriptor elements", 9, cellDescriptors.size());
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("id", 0, Long.class, true, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("name", 1, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("methodField", 2, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("methodField2", 4, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithOnlyGet", 7, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithOnlySet", 8, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithReadIngnore", 9, String.class, false, true, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithWriteIngnore", 10, String.class, false, false, true, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("secondName", 11, String.class, false, false, false, false, null)));
    }

    @Test
    public void testGetCellDescriptorsWithEmbedded() throws Exception {
        AnnotatedPoiParserFactory<EmbeddedTestRow> embeddedTestRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<EmbeddedTestRow>(EmbeddedTestRow.class);
        Set<CellDescriptor> cellDescriptors = embeddedTestRowAnnotatedPoiParserFactory.getCellDescriptors();
        Assert.assertNotNull("Expect cell descriptor not to be null", cellDescriptors);
        Assert.assertEquals("Expected 4 cell descriptor elements", 4, cellDescriptors.size());
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("field1", 0, Short.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("field2", 1, Integer.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("embeddableTestRow.field3", 2, Long.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("embeddableTestRow.field4", 3, BigDecimal.class, false, false, false, false, null)));
    }

    @Test
    public void testGetCellDescriptorsWithExtended() throws Exception {
        AnnotatedPoiParserFactory<ExtendTestRow> testRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<ExtendTestRow>(ExtendTestRow.class);
        Set<CellDescriptor> cellDescriptors = testRowAnnotatedPoiParserFactory.getCellDescriptors();
        Assert.assertNotNull("Expect cell descriptor not to be null", cellDescriptors);
        Assert.assertEquals("Expected 10 cell descriptor elements", 10, cellDescriptors.size());
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("id", 0, Long.class, true, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("name", 1, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("methodField", 2, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("methodField2", 4, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithOnlyGet", 7, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithOnlySet", 8, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithReadIngnore", 9, String.class, false, true, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("fieldWithWriteIngnore", 10, String.class, false, false, true, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("secondName", 11, String.class, false, false, false, false, null)));
        Assert.assertThat(cellDescriptors, Matchers.hasItem(TestHelper.createCellDescriptorMatcher("year", 27, Short.class, false, false, false, false, null)));
    }

    @Test
    public void testGetCellDescriptorsWithOverriddenCellDescriptors() {
        final Set<CellDescriptor> overrideCellDescriptors = new HashSet<CellDescriptor>();
        CellDescriptor overrideCellDescriptor = TestHelper.createCellDescriptor("field1", 0, Short.class, false, false, false, false, null);
        CellDescriptorMatcher overrideCellDescriptorMatcher = new CellDescriptorMatcher(overrideCellDescriptor);
        overrideCellDescriptors.add(overrideCellDescriptor);
        AnnotatedPoiParserFactory<EmbeddedTestRow> embeddedTestRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<EmbeddedTestRow>(EmbeddedTestRow.class);
        embeddedTestRowAnnotatedPoiParserFactory.setOverrideCellDescriptors(overrideCellDescriptors);
        Set<CellDescriptor> cellDescriptors = embeddedTestRowAnnotatedPoiParserFactory.getCellDescriptors();
        Assert.assertNotNull("Expect cell descriptor not to be null", cellDescriptors);
        Assert.assertEquals("Expected 1 cell descriptor element", 1, cellDescriptors.size());
        Assert.assertThat(cellDescriptors, Matchers.hasItem(overrideCellDescriptorMatcher));
    }

    @Test(expected = PoiParserRuntimeException.class)
    public void testGetCellDescriptorsWithDuplicateColumn() {
        AnnotatedPoiParserFactory<DuplicateColumnTestRow> duplicateColumnTestRowAnnotatedPoiParserFactory = new AnnotatedPoiParserFactory<DuplicateColumnTestRow>(DuplicateColumnTestRow.class);
        Set<CellDescriptor> cellDescriptors = duplicateColumnTestRowAnnotatedPoiParserFactory.getCellDescriptors();
    }


}
