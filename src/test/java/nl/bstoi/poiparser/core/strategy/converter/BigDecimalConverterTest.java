package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by hylke on 23/04/15.
 */
public class BigDecimalConverterTest {

    private BigDecimalConverter bigDecimalConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        bigDecimalConverter = new BigDecimalConverter();
    }

    @Test
    public void testReadCellAsNumeric() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(234.5);
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final BigDecimal value = bigDecimalConverter.readCell(mockCell);
        assertEquals(new BigDecimal("234.5"), value);
    }

    @Test
    public void testReadCellAsNumericWithIgnoredRegex() throws Exception {
        when(mockCell.getNumericCellValue()).thenReturn(234.5);
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final BigDecimal value = bigDecimalConverter.readCell(mockCell, "bla");
        assertEquals(new BigDecimal("234.5"), value);
    }

    @Test
    public void testReadCellAsNumericWithIllegalValueWithoutRegex() throws Exception {
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456.5"));
        final BigDecimal value = bigDecimalConverter.readCell(mockCell);
        assertEquals(new BigDecimal("456.5"), value);
    }

    @Test
    public void testReadCellAsNumericWithIllegalValueWithRegex() throws Exception {
        when(mockCell.getNumericCellValue()).thenThrow(new IllegalStateException());
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456.5"));
        final BigDecimal value = bigDecimalConverter.readCell(mockCell, ".*");
        assertEquals(new BigDecimal("456.5"), value);
    }

    @Test
    public void testReadCellAsStringWithIllegalValueWithoutRegex() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456.5"));
        final BigDecimal value = bigDecimalConverter.readCell(mockCell);
        assertEquals(new BigDecimal("456.5"), value);
    }

    @Test
    public void testReadCellAsStringWithIllegalValueWithRegex() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.STRING);
        when(mockCell.getRichStringCellValue()).thenReturn(new HSSFRichTextString("456.5"));
        final BigDecimal value = bigDecimalConverter.readCell(mockCell, ".*");
        assertEquals(new BigDecimal("456.5"), value);
    }

    @Test
    public void testWriteCell() {
        bigDecimalConverter.writeCell(mockCell, new BigDecimal("1.5"));
        verify(mockCell).setCellValue(1.5);
    }

    @Test
    public void testWriteCellWithNullValue() {
        bigDecimalConverter.writeCell(mockCell, null);
        verify(mockCell, never()).setCellValue(anyDouble());
    }
}