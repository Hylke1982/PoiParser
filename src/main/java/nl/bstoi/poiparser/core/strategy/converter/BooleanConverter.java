package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class BooleanConverter extends AbstractConverter<Boolean> {

    public final static String[] trueStringValue = {"yes", "true", "1"};
    public final static int[] trueIntegerValue = {1};

    private final static CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC, CellType.BOOLEAN, CellType.STRING};


    public BooleanConverter() {
        super(supportedCellTypes);
    }

    public Boolean readCell(final Cell cell) {
        boolean returnValue = false;
        if (isValidCell(cell)) {
            final CellType cellType = getCellType(cell);
            if (CellType.BOOLEAN == cellType) {
                returnValue = cell.getBooleanCellValue();
            } else if (CellType.NUMERIC == cellType) {
                returnValue = getReturnValueAsNumeric(cell, returnValue);
            } else if (CellType.STRING == cellType) {
                returnValue = getReturnValueAsString(cell, returnValue);
            }
        }

        return returnValue;
    }

    private boolean getReturnValueAsString(Cell cell, boolean returnValue) {
        for (String trueValue : trueStringValue) {
            if (trueValue.equalsIgnoreCase(cell.getRichStringCellValue().getString().trim())) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    private boolean getReturnValueAsNumeric(Cell cell, boolean returnValue) {
        Double cellValue = cell.getNumericCellValue();
        for (int trueValue : trueIntegerValue) {
            if (trueValue == cellValue.intValue()) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    private boolean isValidCell(Cell cell) {
        return null != cell && isCellTypeSupported(cell);
    }

    public Boolean readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Boolean value) {
        if (null != value) cell.setCellValue(value);

    }

}
