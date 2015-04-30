package nl.bstoi.poiparser.core.strategy.converter;

import java.util.Date;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class DateConverter extends AbstractConverter<Date> {

    private static final CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC};

    protected DateConverter() {
        super(supportedCellTypes);
    }

    public Date readCell(final Cell cell) {
        Date returnValue = null;
        if (hasValidCell(cell)) {
            returnValue = cell.getDateCellValue();
        }
        return returnValue;
    }

    private boolean hasValidCell(Cell cell) {
        return null != cell && (isCellTypeSupported(cell) && DateUtil.isCellDateFormatted(cell));
    }

    public Date readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Date value) {
        if (null != value) cell.setCellValue(value);
    }

}
