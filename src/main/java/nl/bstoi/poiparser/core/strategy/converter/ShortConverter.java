package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class ShortConverter implements Converter<Short> {

    public Short readCell(final Cell cell) {
        Double cellValue = null;
        if (null != cell) {
            try {
                // First try to read as a numeric
                cellValue = cell.getNumericCellValue();
            } catch (final IllegalStateException isex) {
                // Other wise do string conversion
                cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
            }
        }
        if (null != cellValue) return cellValue.shortValue();
        return null;
    }

    public Short readCell(final Cell cell, final String regex) {
        return readCell(cell);
    }

    public void writeCell(final Cell cell, final Short value) {
        if (null != value) cell.setCellValue(value.doubleValue());
    }

}
