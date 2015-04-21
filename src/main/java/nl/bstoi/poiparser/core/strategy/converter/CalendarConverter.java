package nl.bstoi.poiparser.core.strategy.converter;

import java.util.Calendar;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class CalendarConverter implements Converter<Calendar> {

    public Calendar readCell(final Cell cell) {
        if (null != cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(cell.getDateCellValue());
        }
        return null;
    }

    public Calendar readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Calendar value) {
        if (null != value) cell.setCellValue(value);
    }

}
