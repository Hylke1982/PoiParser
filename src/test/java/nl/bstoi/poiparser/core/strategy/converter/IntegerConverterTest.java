package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 22/04/15.
 */
public class IntegerConverterTest {

    private IntegerConverter integerConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        integerConverter = new IntegerConverter();
    }

    @Test
    public void testReadCellWithUnsupportedType() {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        assertNull(integerConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellAsString() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.STRING);
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456"));
        final Integer value = integerConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Integer(456), value);
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(232.3);
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final Integer value = integerConverter.readCell(mockCell);
        assertNotNull(value);
        assertEquals(new Integer(232), value);
    }

    @Test
    public void testReadCellAsStringWithRegex() throws Exception {
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456"));
        final Integer value = integerConverter.readCell(mockCell, "");
        assertNotNull(value);
        assertEquals(new Integer(456), value);
    }

    @Test
    public void testReadCellAsNumericWithRegex() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(232.3);
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final Integer value = integerConverter.readCell(mockCell, "");
        assertNotNull(value);
        assertEquals(new Integer(232), value);
    }
}