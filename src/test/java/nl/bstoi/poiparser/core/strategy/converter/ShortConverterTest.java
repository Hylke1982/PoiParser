package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 30/04/15.
 */
public class ShortConverterTest {
    
    private ShortConverter shortConverter;
    
    @Mock
    private Cell mockCell;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        shortConverter = new ShortConverter();
    }

    @Test
    public void testReadCellWithNull() throws Exception {
        assertNull(shortConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNullWithRegx() throws Exception {
        assertNull(shortConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellWithUnsupportedType() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_BOOLEAN);
        assertNull(shortConverter.readCell(null));
    }

    @Test
    public void testReadCellWithUnsupportedTypeWithRegex() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_BOOLEAN);
        assertNull(shortConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(5635.);
        final Short value = shortConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Short("5635"), value);
    }

    @Test
    public void testReadCellAsNumericWithStringFallback() throws Exception {
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("3456.8"));
        final Short value = shortConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Short("3456"), value);
    }

    @Test
    public void testReadCellAsExplicitNumeric() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
        when(mockCell.getNumericCellValue()).thenReturn(5462.);
        final Short value = shortConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Short("5462"), value);
    }

    @Test
    public void testReadCellAsString() throws Exception {
        when(mockCell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("3456"));
        final Short value = shortConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Short("3456"), value);
    }

    @Test
    public void testWriteCell() throws Exception {
        final Short value = new Short("345");
        shortConverter.writeCell(mockCell, value);
        verify(mockCell).setCellValue(value);
    }

    @Test
    public void testWriteCellWithNull() throws Exception {
        shortConverter.writeCell(mockCell, null);
        verify(mockCell, never()).setCellValue(anyLong());
    }
}