package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 23/04/15.
 */
public class CalendarConverterTest {


    private short bla = 39;


    private CalendarConverter calendarConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        calendarConverter = new CalendarConverter();
    }

    @Test
    public void testReadCellWithNullCell() throws Exception {
        assertNull(calendarConverter.readCell(null));
    }

    @Test
    public void testReadCellWithNullCellAndRegex() throws Exception {
        assertNull(calendarConverter.readCell(null, ".*"));
    }

    @Test
    public void testReadCellWithAsNonNumeric() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        assertNull(calendarConverter.readCell(mockCell, ".*"));
    }

    @Test
    public void testReadCellWithAsNonDateFormatted() throws Exception {
        when(mockCell.getCellType()).thenReturn(CellType.BOOLEAN);
        when(mockCell.getNumericCellValue()).thenReturn(-5.0E-324D);
        assertNull(calendarConverter.readCell(mockCell, ".*"));
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
        final Calendar calendar = calendarConverter.readCell(mockCell);
        assertNotNull(calendar);
        assertEquals(currentDate.getTime(), calendar.getTimeInMillis());
    }

    @Test
    public void testWriteCell() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendarConverter.writeCell(mockCell, calendar);
        verify(mockCell).setCellValue(calendar);
    }

    @Test
    public void testWriteCellWithNull() throws Exception {
        calendarConverter.writeCell(mockCell, null);
        verify(mockCell, never()).setCellValue(any(Calendar.class));
    }
}