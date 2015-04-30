package nl.bstoi.poiparser.core.strategy.converter;

import java.util.Calendar;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class CalendarConverter extends AbstractConverter<Calendar> {

    private final static CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC};

    public CalendarConverter() {
        super(supportedCellTypes);
    }

    public Calendar readCell(final Cell cell) {
        Calendar returnValue = null;
        if (hasValidCell(cell)) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(cell.getDateCellValue());
            returnValue = cal;
        }
        return returnValue;
    }

    private boolean hasValidCell(Cell cell) {
        return null != cell && (isCellTypeSupported(cell) && DateUtil.isCellDateFormatted(cell));
    }

    public Calendar readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Calendar value) {
        if (null != value) cell.setCellValue(value);
    }

}
