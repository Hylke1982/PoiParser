package nl.bstoi.poiparser.core.strategy.converter;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;

public class StringConverter extends AbstractConverter<String> {

    private static final CellType[] supportedCellTypes = new CellType[]{CellType.STRING, CellType.NUMERIC};

    public StringConverter() {
        super(supportedCellTypes);
    }

    public String readCell(final Cell cell) {
        return readCell(cell, null);
    }

    public String readCell(final Cell cell, final String regex) {
        String returnValue = null;
        if (isValidCell(cell)) {
            final CellType cellType = getCellType(cell);
            if (isCellOfTypeStringAndHasRegex(regex, cellType)) {
                returnValue = readCellValueAsStringWithRegex(cell, regex, returnValue);
            } else if (CellType.STRING == cellType) {
                returnValue = cell.getRichStringCellValue().getString();
            } else if (isCellNumericAndDateFormatted(cell, cellType)) {
                returnValue = cell.getDateCellValue().toString();
            } else if (CellType.NUMERIC == cellType) {
                returnValue = getCellValueAsNumeric(cell);
            }
        }

        return returnValue;
    }

    private String readCellValueAsStringWithRegex(Cell cell, String regex, String returnValue) {
        final Pattern pattern = Pattern.compile(regex);
        if (pattern.matcher(cell.getRichStringCellValue().getString()).matches()) {
            returnValue = cell.getRichStringCellValue().getString();
        }
        return returnValue;
    }

    private String getCellValueAsNumeric(Cell cell) {
        // Format the decimal and disable the number grouping
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(cell.getNumericCellValue());
    }

    private boolean isCellNumericAndDateFormatted(Cell cell, CellType cellType) {
        return CellType.NUMERIC == cellType && DateUtil.isCellDateFormatted(cell);
    }

    private boolean isCellOfTypeStringAndHasRegex(String regex, CellType cellType) {
        return CellType.STRING == cellType && StringUtils.isNotBlank(regex);
    }

    private boolean isValidCell(final Cell cell) {
        return null != cell && isCellTypeSupported(cell);
    }

    public void writeCell(final Cell cell, final String value) {
        if (cell != null) {
            CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
            cell.setCellValue(creationHelper.createRichTextString(value));
        }
    }
}
