package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class IntegerConverter extends AbstractConverter<Integer> {

    private final static CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC, CellType.STRING};

    public IntegerConverter() {
        super(supportedCellTypes);
    }

    public Integer readCell(final Cell cell) {
        Integer returnValue = null;
        if (null != cell) {
            final CellType cellType = getCellType(cell);
            if (CellType.NUMERIC == cellType) {
                returnValue = getCellValueAsNumeric(cell);
            } else if (CellType.STRING == cellType) {
                // Other wise do string conversion
                final Double cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
                if (null != cellValue) returnValue = cellValue.intValue();
            }

        }

        return returnValue;
    }

    private Integer getCellValueAsNumeric(final Cell cell) {
        Integer returnValue = null;
        Double cellValue = null;
        try {
            // First try to read as a numeric
            cellValue = cell.getNumericCellValue();
        } catch (final IllegalStateException isex) {
            // Other wise do string conversion
            cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        }
        if (null != cellValue) returnValue = cellValue.intValue();
        return returnValue;
    }

    public Integer readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Integer value) {
        if (null != value) cell.setCellValue(value);
    }

}
