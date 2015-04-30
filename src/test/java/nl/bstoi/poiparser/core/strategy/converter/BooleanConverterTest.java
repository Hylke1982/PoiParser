package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 23/04/15.
 */
public class BooleanConverterTest {

    private BooleanConverter booleanConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        booleanConverter = new BooleanConverter();
    }

    @Test
    public void testReadCellAsDefault() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(1.);
        assertTrue(booleanConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(mockCell.getNumericCellValue()).thenReturn(1.);
        assertTrue(booleanConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellAsNumericFalseValue() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(mockCell.getNumericCellValue()).thenReturn(2.);
        assertFalse(booleanConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellWithNullCell() throws Exception {
        assertFalse(booleanConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNullCellWithRegex() throws Exception {
        assertFalse(booleanConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellAsBooleanWithoutRegex() {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_BOOLEAN);
        when(mockCell.getBooleanCellValue()).thenReturn(Boolean.TRUE);
        assertTrue(booleanConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellAsBooleanWithRegex() {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_BOOLEAN);
        when(mockCell.getBooleanCellValue()).thenReturn(Boolean.TRUE);
        assertTrue(booleanConverter.readCell(mockCell, ".*"));
    }

    @Test
    public void testReadCellAsStringWithoutRegexValueTrue() {
        validateBooleanAsStringValue("true", true);
    }

    @Test
    public void testReadCellAsStringWithoutRegexValue1() {
        validateBooleanAsStringValue("1", true);
    }

    @Test
    public void testReadCellAsStringWithoutRegexValueYes() {
        validateBooleanAsStringValue("yes", true);
    }

    @Test
    public void testReadCellAsStringWithoutRegexValueOther() {
        validateBooleanAsStringValue("other", false);
    }

    @Test
    public void testReadCellAsStringWithoutRegexValueNull() {
        validateBooleanAsStringValue(null, false);
    }

    private void validateBooleanAsStringValue(final String value, boolean expectedResult) {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString(value));
        assertEquals(expectedResult, booleanConverter.readCell(mockCell, ".*"));
    }


    @Test
    public void testWriteCell() throws Exception {
        booleanConverter.writeCell(mockCell, true);
        verify(mockCell).setCellValue(true);
    }

    @Test
    public void testWriteCellWithNull() throws Exception {
        booleanConverter.writeCell(mockCell, null);
        verify(mockCell, never()).setCellValue(anyBoolean());
    }
}