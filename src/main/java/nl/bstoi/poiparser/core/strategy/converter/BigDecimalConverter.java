package nl.bstoi.poiparser.core.strategy.converter;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class BigDecimalConverter extends AbstractConverter<BigDecimal> {

    private static final CellType[] supportedCellTypes = new CellType[]{CellType.NUMERIC, CellType.STRING};

    public BigDecimalConverter() {
        super(supportedCellTypes);
    }

    public BigDecimal readCell(final Cell cell) {
        return readCell(cell, null);
    }

    public BigDecimal readCell(final Cell cell, final String regex) {
        BigDecimal returnValue = null;
        Double cellValue = null;
        if (isCorrectCellType(cell)) {
            final CellType cellType = getCellType(cell);
            switch (cellType) {
                case NUMERIC:
                    cellValue = getCellValueFromNumeric(cell, regex);
                    break;
                case STRING:
                    cellValue = getCellValueFromString(cell, regex);
                    break;
            }
        }
        if (null != cellValue) returnValue = new BigDecimal(cellValue);
        return returnValue;
    }

    private Double getCellValueFromNumeric(Cell cell, String regex) {
        Double cellValue;
        try {
            cellValue = cell.getNumericCellValue();
        } catch (final IllegalStateException e) {
            cellValue = getCellValueFromString(cell, regex);
        }
        return cellValue;
    }

    private Double getCellValueFromString(Cell cell, String regex) {
        Double cellValue;// Other wise do string conversion
        if (hasRegex(regex)) {
            cellValue = readCellWithRegex(cell, regex);
        } else {
            cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        }
        return cellValue;
    }

    private boolean hasRegex(String regex) {
        return null != regex && !regex.isEmpty();
    }

    private boolean isCorrectCellType(Cell cell) {
        return null != cell && isCellTypeSupported(cell);
    }

    private Double readCellWithRegex(Cell cell, String regex) {
        Double cellValue = null;
        final Pattern pattern = Pattern.compile(regex);
        if (pattern.matcher(cell.getRichStringCellValue().getString().trim()).matches()) {
            cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
        }
        return cellValue;
    }


    public void writeCell(final Cell cell, final BigDecimal value) {
        if (null != value) cell.setCellValue(value.doubleValue());
    }

}
