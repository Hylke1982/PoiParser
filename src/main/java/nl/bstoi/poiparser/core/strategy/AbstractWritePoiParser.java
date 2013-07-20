package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import nl.bstoi.poiparser.core.strategy.factory.DefaultConverterFactory;
import nl.bstoi.poiparser.core.strategy.util.TypedList;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public abstract class AbstractWritePoiParser {
    private final static Log log = LogFactory.getLog(AbstractWritePoiParser.class);

    private final DefaultConverterFactory DEFAULTCONVERTERFACTORY = new DefaultConverterFactory();

    private boolean createHeaderRow = false;
    private final Workbook workbook;
    private ColumnHeaderProperties columnHeaderProperties;

    protected AbstractWritePoiParser(Workbook workbook) {
        this.workbook = workbook;
    }

    protected void writeSheet(String sheetName, TypedList<?> values, Set<CellDescriptor> sheetCellDescriptors) {
        Sheet sheet = workbook.createSheet(sheetName);
        if (isCreateHeaderRow()) {
            writeHeaderRow(sheet, sheetCellDescriptors);
        }
        writeDataRows(sheet, values, sheetCellDescriptors);

    }

    protected void writeHeaderRow(Sheet sheet, Set<CellDescriptor> sheetCellDescriptors) {
        if (sheet != null) {
            Row headerRow = sheet.createRow(0);
            for (CellDescriptor sheetCellDescriptor : sheetCellDescriptors) {
                writeHeaderCell(sheet.getSheetName(), headerRow, sheetCellDescriptor);
            }
        }
    }

    protected void writeHeaderCell(final String sheetName, Row headerRow, CellDescriptor sheetCellDescriptor) {
        try {
            if (!sheetCellDescriptor.isWriteIgnore()) {
                Cell cell = headerRow.createCell(sheetCellDescriptor.getColumnNumber());
                String headerColumnName = sheetCellDescriptor.getFieldName();
                if (hasColumnHeaderProperties() && getColumnHeaderProperties().containsColumnHeader(sheetName, headerColumnName)) {
                    headerColumnName = getColumnHeaderProperties().getColumnHeader(sheetName, headerColumnName);
                }
                Converter converter = DEFAULTCONVERTERFACTORY.getConverter(String.class);
                converter.writeCell(cell, headerColumnName);
            }
        } catch (InstantiationException e) {
            log.trace(String.format("Error writing column header on row %s and column %s with propertyname %s", 0, sheetCellDescriptor.getColumnNumber(), sheetCellDescriptor.getFieldName()), e);
        } catch (IllegalAccessException e) {
            log.trace(String.format("Error writing column header on row %s and column %s with propertyname %s", 0, sheetCellDescriptor.getColumnNumber(), sheetCellDescriptor.getFieldName()), e);
        }

    }

    protected void writeDataRows(Sheet sheet, TypedList<?> values, Set<CellDescriptor> sheetCellDescriptors) {
        if (null != sheet) {
            int index = isCreateHeaderRow() ? 1 : 0;
            for (Object value : values) {
                writeDataRow(sheet, index, value, sheetCellDescriptors);
                index++;
            }
        }
    }

    protected void writeDataRow(Sheet sheet, int index, Object value, Set<CellDescriptor> sheetCellDescriptors) {
        Row row = sheet.createRow(index);
        for (CellDescriptor cellDescriptor : sheetCellDescriptors) {
            Object cellValue = readCellValueFromObjectProperty(value, cellDescriptor.getFieldName());
            writeDataCell(row, cellValue, cellDescriptor);
        }
    }

    protected void writeDataCell(Row row, Object cellValue, CellDescriptor cellDescriptor) {
        Cell cell = row.createCell(cellDescriptor.getColumnNumber());
        if (!cellDescriptor.isWriteIgnore()) {
            try {
                Converter converter = DEFAULTCONVERTERFACTORY.getConverter(cellDescriptor.getType());
                converter.writeCell(cell, cellValue);
            } catch (InstantiationException e) {
                log.trace(String.format("Error writing cell on row %s and column %s with propertyname %s", row.getRowNum(), cellDescriptor.getColumnNumber(), cellDescriptor.getFieldName()), e);
            } catch (IllegalAccessException e) {
                log.trace(String.format("Error writing cell on row %s and column %s with propertyname %s", row.getRowNum(), cellDescriptor.getColumnNumber(), cellDescriptor.getFieldName()), e);
            }
        }
    }

    protected Object readCellValueFromObjectProperty(Object object, String propertyName) {
        if (null == object) throw new IllegalArgumentException("Object cannot be null");
        if (StringUtils.isEmpty(propertyName)) throw new IllegalArgumentException("Property name cannot be null");
        try {
            return PropertyUtils.getNestedProperty(object, propertyName);
        } catch (IllegalAccessException e) {
            throw new PoiParserRuntimeException(String.format("Property %s cannot be read", propertyName), e);
        } catch (InvocationTargetException e) {
            throw new PoiParserRuntimeException(String.format("Property %s cannot be read", propertyName), e);
        } catch (NoSuchMethodException e) {
            return readCellValueFromObjectField(object, propertyName);
        }
    }

    protected Object readCellValueFromObjectField(Object object, String fieldName) {
        String[] splittedFieldNames = getSplittedPropertyName(fieldName);
        Object returnObject = null;
        for (String splittedFieldName : splittedFieldNames) {
            returnObject = getFieldFromObject(object, splittedFieldName);
            if (returnObject == null) {
                return null;
            }
        }
        return returnObject;
    }

    private Object getFieldFromObject(Object object, String fieldName) {
        try {
            Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
        return null;

    }

    public boolean isCreateHeaderRow() {
        return createHeaderRow;
    }

    public void setCreateHeaderRow(boolean createHeaderRow) {
        this.createHeaderRow = createHeaderRow;
    }

    protected Workbook getWorkbook() {
        return workbook;
    }


    protected String[] getSplittedPropertyName(final String propertyName) {
        if (StringUtils.isEmpty(propertyName)) throw new IllegalArgumentException("Property name cannot be empty");
        return propertyName.split("\\.");
    }

    private ColumnHeaderProperties getColumnHeaderProperties() {
        return this.columnHeaderProperties;
    }

    private boolean hasColumnHeaderProperties() {
        return (null != this.columnHeaderProperties);
    }

    public void setColumnHeaderProperties(ColumnHeaderProperties columnHeaderProperties) {
        this.columnHeaderProperties = columnHeaderProperties;
    }
}
