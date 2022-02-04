package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 23/04/15.
 */
public class LongConverterTest {


    private LongConverter longConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        longConverter = new LongConverter();
    }

    @Test
    public void testReadCellWithNull() throws Exception {
        assertNull(longConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNullWithRegx() throws Exception {
        assertNull(longConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellWithUnsupportedType() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        assertNull(longConverter.readCell(null));
    }

    @Test
    public void testReadCellWithUnsupportedTypeWithRegex() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        assertNull(longConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(2336.);
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final Long value = longConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Long(2336), value);
    }

    @Test
    public void testReadCellAsNumericWithStringFallback() throws Exception {
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456.5"));
        final Long value = longConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Long(456), value);
    }

    @Test
    public void testReadCellAsExplicitNumeric() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockCell.getNumericCellValue()).thenReturn(56542336.);
        final Long value = longConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Long(56542336), value);
    }

    @Test
    public void testReadCellAsString() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("32423"));
        final Long value = longConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Long(32423), value);
    }

    @Test
    public void testWriteCell() throws Exception {
        final Long value = new Long("345");
        longConverter.writeCell(mockCell, value);
        verify(mockCell).setCellValue(value);
    }

    @Test
    public void testWriteCellWithNull() throws Exception {
        longConverter.writeCell(mockCell, null);
        verify(mockCell, never()).setCellValue(anyLong());
    }
}