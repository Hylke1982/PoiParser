package nl.bstoi.poiparser.core.strategy.converter;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;

public class StringConverter implements Converter<String> {

    public String readCell(final Cell cell) {
        return readCell(cell, null);
    }

    public String readCell(final Cell cell, final String regex) {
        Pattern pattern = null;
        if (null != regex && !regex.isEmpty()) pattern = Pattern.compile(regex);
        if (null != cell && cell.getCellType() == Cell.CELL_TYPE_STRING) {
            if (null != pattern) {
                if (pattern.matcher(cell.getRichStringCellValue().getString()).matches()) {
                    return cell.getRichStringCellValue().getString();
                } else {
                    return null;
                }
            }
            return cell.getRichStringCellValue().getString();

        } else if (null != cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))) {
            return cell.getDateCellValue().toString();
        } else if (null != cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && !DateUtil.isCellDateFormatted(cell))) {
            // Format the decimal and disable the number grouping
            final NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            return numberFormat.format(cell.getNumericCellValue());
        }
        return null;
    }

    public void writeCell(final Cell cell, final String value) {
        CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
        cell.setCellValue(creationHelper.createRichTextString(value));
    }
}
