package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class LongConverter extends AbstractConverter<Long> {

    private final static CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC, CellType.STRING};

    public LongConverter() {
        super(supportedCellTypes);
    }

    public Long readCell(final Cell cell) {
        Long returnValue = null;
        if (isValidCell(cell)) {
            final CellType cellType = getCellType(cell);
            if (CellType.NUMERIC == cellType) {
                returnValue = getValueAsNumeric(cell);
            } else if (CellType.STRING == cellType) {
                returnValue = getCellValueAsString(cell);
            }
        }

        return returnValue;
    }

    private Long getCellValueAsString(Cell cell) {
        Long returnValue = null;
        Double cellValue;
        cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        if (null != cellValue) returnValue = cellValue.longValue();
        return returnValue;
    }

    private Long getValueAsNumeric(Cell cell) {
        Double cellValue;
        Long returnValue = null;
        try {
            // First try to read as a numeric
            cellValue = cell.getNumericCellValue();
        } catch (final IllegalStateException isex) {
            // Other wise do string conversion
            cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        }
        if (null != cellValue) returnValue = cellValue.longValue();
        return returnValue;
    }

    private boolean isValidCell(Cell cell) {
        return null != cell && isCellTypeSupported(cell);
    }

    public Long readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Long value) {
        if (null != value) cell.setCellValue(value);
    }

}
