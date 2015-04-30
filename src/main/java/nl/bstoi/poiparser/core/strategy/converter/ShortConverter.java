package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class ShortConverter extends AbstractConverter<Short> {

    private final static CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC, CellType.STRING};


    public ShortConverter() {
        super(supportedCellTypes);
    }

    public Short readCell(final Cell cell) {
        Short returnValue = null;
        if (isValidCell(cell)) {
            final CellType cellType = getCellType(cell);
            if (CellType.NUMERIC == cellType) {
                returnValue = getCellValueNumeric(cell);
            } else if (CellType.STRING == cellType) {
                returnValue = getCellValueAsString(cell);
            }
        }
        return returnValue;
    }

    private Short getCellValueAsString(Cell cell) {
        Double bla = null;
        bla = Double.parseDouble(cell.getRichStringCellValue().toString());
        return bla != null ? bla.shortValue() : null;
    }

    private Short getCellValueNumeric(Cell cell) {
        Double cellValue = null;
        try {
            // First try to read as a numeric
            cellValue = cell.getNumericCellValue();
        } catch (final IllegalStateException isex) {
            // Other wise do string conversion
            cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        }
        return cellValue != null ? cellValue.shortValue() : null;
    }

    private boolean isValidCell(final Cell cell) {
        return cell != null && isCellTypeSupported(cell);
    }

    public Short readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Short value) {
        if (null != value) cell.setCellValue(value.doubleValue());
    }

}
