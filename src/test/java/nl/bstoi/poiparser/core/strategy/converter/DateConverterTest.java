package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 23/04/15.
 */
public class DateConverterTest {

    private DateConverter dateConverter;

    @Mock
    private Cell mockCell;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dateConverter = new DateConverter();
    }

    @Test
    public void testReadCellWithNull() throws Exception {
        assertNull(dateConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNotNumericValue() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        assertNull(dateConverter.readCell(mockCell));
    }

    @Test
    public void testReadCellWithAsNonDateFormatted() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        when(mockCell.getNumericCellValue()).thenReturn(-5.0E-324D);
        assertNull(dateConverter.readCell(mockCell, ".*"));
    }

    @Test
    public void testReadCell() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.NUMERIC);
        final HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        final HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        final Date currentDate = new Date();
        cellStyle.setDataFormat(hssfWorkbook.getCreationHelper().createDataFormat().getFormat("m/d/yyyy h:mm"));
        when(mockCell.getCellStyle()).thenReturn(cellStyle);
        when(mockCell.getDateCellValue()).thenReturn(currentDate);
        final Date date = dateConverter.readCell(mockCell);
        assertNotNull(date);
        assertEquals(currentDate.getTime(), date.getTime());
    }

    @Test
    public void testWriteCell() throws Exception {
        final Date date = new Date();
        dateConverter.writeCell(mockCell, date);
        verify(mockCell).setCellValue(date);
    }

    @Test
    public void testWriteCellWithNullValue() throws Exception {
        final Date date = null;
        dateConverter.writeCell(mockCell, date);
        verify(mockCell, never()).setCellValue(any(Date.class));
    }
}