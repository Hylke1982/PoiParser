package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.strategy.annotation.structures.TestRow;
import nl.bstoi.poiparser.core.strategy.util.TypedArrayList;
import nl.bstoi.poiparser.core.strategy.util.TypedList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println("T: " + tempOutputFile.getAbsolutePath());
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
        testRows.add(TestHelper.createTestRow(234L,"name1","methodFieldABC"));
        testRows.add(TestHelper.createTestRow(789L,"name1","methodFieldZXF"));
        data.put(SHEET_ABC, testRows);
        annotatedWritePoiParser.write(data);

        Workbook workbook = WorkbookFactory.create(new FileInputStream(tempOutputFile));
        Sheet sheet = workbook.getSheet(SHEET_ABC);
        Assert.assertNotNull("Sheet 'sheet_abc' must exists",sheet);
        Assert.assertEquals("Sheet 'sheet_abc' must contain 2 rows",2,sheet.getLastRowNum()+1);
    }


}
