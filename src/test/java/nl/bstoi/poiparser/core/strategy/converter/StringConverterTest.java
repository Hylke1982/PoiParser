package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 30/04/15.
 */
public class StringConverterTest {


    private StringConverter stringConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stringConverter = new StringConverter();
    }

    @Test
    public void testReadCellWithNullCell() throws Exception {
        assertNull(stringConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNullCellAndRegex() throws Exception {
        assertNull(stringConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellWithUnsupportedType() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_BOOLEAN);
        assertNull(stringConverter.readCell(mockCell));
    }

    @Test(expected = PatternSyntaxException.class)
    public void testReadCellWithIncorrectRegex() {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("bla"));
        stringConverter.readCell(mockCell, "(");
    }

    @Test
    public void testReadCellWithNonMatchingRegex() {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("bla"));
        assertNull(stringConverter.readCell(mockCell, "[not]{3}"));
    }

    @Test
    public void testReadCellWithMatchingRegex() {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("bla"));
        final String returnValue = stringConverter.readCell(mockCell, "[bla]{3}");
        assertNotNull(returnValue);
        assertEquals("bla", returnValue);
    }

    @Test
    public void testReadCellAsNumericAsDate() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        final HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        final HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        final Date currentDate = new Date();
        cellStyle.setDataFormat(hssfWorkbook.getCreationHelper().createDataFormat().getFormat("m/d/yyyy h:mm"));
        when(mockCell.getCellStyle()).thenReturn(cellStyle);
        when(mockCell.getDateCellValue()).thenReturn(currentDate);
        assertNotNull(stringConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(mockCell.getNumericCellValue()).thenReturn(34534.5);
        final String returnValue = stringConverter.readCell(mockCell);
        assertNotNull(returnValue);
        assertEquals("34534.5", returnValue);
    }

    @Test
    public void testWriteCell() throws Exception {

    }
}