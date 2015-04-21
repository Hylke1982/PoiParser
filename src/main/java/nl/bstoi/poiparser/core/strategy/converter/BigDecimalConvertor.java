package nl.bstoi.poiparser.core.strategy.converter;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class BigDecimalConvertor implements Converter<BigDecimal> {

    public BigDecimal readCell(final Cell cell) {
        return readCell(cell, null);
    }

    public BigDecimal readCell(final Cell cell, final String regex) {
        Double cellValue = null;
        if (null != cell) {
            try {
                // First try to read as a numeric
                cellValue = cell.getNumericCellValue();
            } catch (final IllegalStateException isex) {
                // Other wise do string conversion
                if (null != regex && !regex.isEmpty()) {
                    Pattern pattern = Pattern.compile(regex);
                    if (pattern.matcher(cell.getRichStringCellValue().getString().trim()).matches()) {
                        cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
                    } else {
                        return null;
                    }
                } else {
                    cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
                }

            }
        }
        if (null != cellValue) return new BigDecimal(cellValue);
        return null;
    }


    public void writeCell(final Cell cell, final BigDecimal value) {
        if (null != value) cell.setCellValue(value.doubleValue());
    }

}
