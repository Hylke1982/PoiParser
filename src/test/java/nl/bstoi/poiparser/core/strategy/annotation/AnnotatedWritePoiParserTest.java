package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.strategy.ColumnHeaderProperties;
import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.strategy.annotation.structures.EmbeddedTestRow;
import nl.bstoi.poiparser.core.strategy.annotation.structures.TestRow;
import nl.bstoi.poiparser.core.strategy.util.TypedArrayList;
import nl.bstoi.poiparser.core.strategy.util.TypedList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AnnotatedWritePoiParserTest {

    private AnnotatedWritePoiParser annotatedWritePoiParser;
    private File tempOutputFile;

    @Before
    public void setUp() throws Exception {
        final Workbook workbook = new HSSFWorkbook();
        tempOutputFile = File.createTempFile("test-data", ".xls");
        annotatedWritePoiParser = new AnnotatedWritePoiParser(new FileOutputStream(tempOutputFile), workbook);

    }

    @After
    public void after() {
        tempOutputFile.delete();
    }

    @Test
    public void testWrite() throws Exception {
        final String SHEET_ABC = "sheet_abc";
        final Map<String, TypedList<?>> data = new HashMap<String, TypedList<?>>();
        final TypedList<TestRow> testRows = new TypedArrayList<TestRow>(TestRow.class);
        testRows.add(TestHelper.createTestRow(234L, "name1", "methodFieldABC", "fieldWithOnlySet1"));
        testRows.add(TestHelper.createTestRow(789L, "name2", "methodFieldZXF", "fieldWithOnlySet2"));
        data.put(SHEET_ABC, testRows);
        annotatedWritePoiParser.write(data);

        Workbook workbook = WorkbookFactory.create(new FileInputStream(tempOutputFile));
        Sheet sheet = workbook.getSheet(SHEET_ABC);
        Assert.assertNotNull("Sheet 'sheet_abc' must exists", sheet);
        Assert.assertEquals("Sheet 'sheet_abc' must contain 2 rows", 2, sheet.getLastRowNum() + 1);


        assertDatarow(sheet, 0);
    }


    @Test
    public void testWriteWithPropertyNameColumnHeaders() throws Exception {
        final String SHEET_ABC = "sheet_abc";
        final Map<String, TypedList<?>> data = new HashMap<String, TypedList<?>>();
        final TypedList<TestRow> testRows = new TypedArrayList<TestRow>(TestRow.class);
        testRows.add(TestHelper.createTestRow(234L, "name1", "methodFieldABC", "fieldWithOnlySet1"));
        testRows.add(TestHelper.createTestRow(789L, "name2", "methodFieldZXF", "fieldWithOnlySet2"));
        data.put(SHEET_ABC, testRows);
        annotatedWritePoiParser.setCreateHeaderRow(true);
        annotatedWritePoiParser.write(data);

        Workbook workbook = WorkbookFactory.create(new FileInputStream(tempOutputFile));
        Sheet sheet = workbook.getSheet(SHEET_ABC);
        Assert.assertNotNull("Sheet 'sheet_abc' must exists", sheet);
        Assert.assertEquals("Sheet 'sheet_abc' must contain 3 rows", 3, sheet.getLastRowNum() + 1);

        Assert.assertEquals("id", getStringValueFromCell(sheet, 0, 0));
        assertHeaderRow(sheet);
        assertDatarow(sheet, 1);
    }


    @Test
    public void testWriteWithCustomColumnHeaders() throws Exception {
        final String SHEET_ABC = "sheet_abc";

        final Properties properties = new Properties();
        properties.setProperty(SHEET_ABC + ColumnHeaderProperties.DOT + "id", "hallo");
        ColumnHeaderProperties columnHeaderProperties = new ColumnHeaderProperties(properties);


        final Map<String, TypedList<?>> data = new HashMap<String, TypedList<?>>();
        final TypedList<TestRow> testRows = new TypedArrayList<TestRow>(TestRow.class);
        testRows.add(TestHelper.createTestRow(234L, "name1", "methodFieldABC", "fieldWithOnlySet1"));
        testRows.add(TestHelper.createTestRow(789L, "name2", "methodFieldZXF", "fieldWithOnlySet2"));
        data.put(SHEET_ABC, testRows);
        annotatedWritePoiParser.setCreateHeaderRow(true);
        annotatedWritePoiParser.setColumnHeaderProperties(columnHeaderProperties);
        annotatedWritePoiParser.write(data);

        Workbook workbook = WorkbookFactory.create(new FileInputStream(tempOutputFile));
        Sheet sheet = workbook.getSheet(SHEET_ABC);
        Assert.assertNotNull("Sheet 'sheet_abc' must exists", sheet);
        Assert.assertEquals("Sheet 'sheet_abc' must contain 3 rows", 3, sheet.getLastRowNum() + 1);

        Assert.assertEquals("hallo", getStringValueFromCell(sheet, 0, 0));
        assertHeaderRow(sheet);
        assertDatarow(sheet, 1);
    }

    @Test
    public void testWriteWithEmbedded() throws Exception {
        final String SHEET_EMBEDDED = "sheet_embedded";
        final Map<String, TypedList<?>> data = new HashMap<String, TypedList<?>>();
        final TypedList<EmbeddedTestRow> embeddedTestRows = new TypedArrayList<EmbeddedTestRow>(EmbeddedTestRow.class);
        embeddedTestRows.add(TestHelper.createEmbeddedTestRow(new Short("123"), new Integer("345"), new Long("567"), new BigDecimal("678.89")));
        data.put(SHEET_EMBEDDED, embeddedTestRows);
        annotatedWritePoiParser.write(data);

        Workbook workbook = WorkbookFactory.create(new FileInputStream(tempOutputFile));
        Sheet sheet = workbook.getSheet(SHEET_EMBEDDED);
        Assert.assertNotNull("Sheet 'sheet_embedded' must exists", sheet);
        Assert.assertEquals("Sheet 'sheet_embedded' must contain 1 row", 1, sheet.getLastRowNum() + 1);

        Assert.assertEquals(new Short("123"),new Short(new Double(getNumericValueFromCell(sheet,0,0)).shortValue()));
        Assert.assertEquals(new Integer("345"),new Integer(new Double(getNumericValueFromCell(sheet,0,1)).intValue()));
        Assert.assertEquals(new Long("567"),new Long(new Double(getNumericValueFromCell(sheet,0,2)).longValue()));
    }

    private String getStringValueFromCell(final Sheet sheet, int rowNumber, int columnNumber) {
        Row row = sheet.getRow(rowNumber);
        Cell cell = row.getCell(columnNumber);
        return cell.getStringCellValue();
    }

    private double getNumericValueFromCell(final Sheet sheet, int rowNumber, int columnNumber) {
        Row row = sheet.getRow(rowNumber);
        Cell cell = row.getCell(columnNumber);
        return cell.getNumericCellValue();
    }

    private void assertHeaderRow(Sheet sheet) {
        Assert.assertEquals("name", getStringValueFromCell(sheet, 0, 1));
        Assert.assertEquals("methodField", getStringValueFromCell(sheet, 0, 2));
        Assert.assertEquals("methodField2", getStringValueFromCell(sheet, 0, 4));
        Assert.assertEquals("fieldWithOnlyGet", getStringValueFromCell(sheet, 0, 7));
        Assert.assertEquals("fieldWithOnlySet", getStringValueFromCell(sheet, 0, 8));
        Assert.assertEquals("fieldWithReadIngnore", getStringValueFromCell(sheet, 0, 9));
        Assert.assertEquals("secondName", getStringValueFromCell(sheet, 0, 11));
    }

    private void assertDatarow(Sheet sheet, int rowNumber) {
        Assert.assertEquals(new Long(234L), Long.valueOf(Math.round(getNumericValueFromCell(sheet, rowNumber, 0))));
        Assert.assertEquals("name1", getStringValueFromCell(sheet, rowNumber, 1));
        Assert.assertEquals("methodFieldABC", getStringValueFromCell(sheet, rowNumber, 2));
        Assert.assertEquals("name1methodFieldABC", getStringValueFromCell(sheet, rowNumber, 4));
        Assert.assertEquals("fieldWithOnlySet1", getStringValueFromCell(sheet, rowNumber, 8));
    }
}
