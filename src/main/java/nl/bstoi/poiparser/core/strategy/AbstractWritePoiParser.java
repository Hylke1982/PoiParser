package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.exception.NonExistentConverterException;
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

    protected void writeSheet(final String sheetName, final TypedList<?> values, final Set<CellDescriptor> sheetCellDescriptors) {
        Sheet sheet = workbook.createSheet(sheetName);
        if (isCreateHeaderRow()) {
            writeHeaderRow(sheet, sheetCellDescriptors);
        }
        writeDataRows(sheet, values, sheetCellDescriptors);

    }

    protected void writeHeaderRow(final Sheet sheet, final Set<CellDescriptor> sheetCellDescriptors) {
        if (sheet != null) {
            final Row headerRow = sheet.createRow(0);
            for (final CellDescriptor sheetCellDescriptor : sheetCellDescriptors) {
                writeHeaderCell(sheet.getSheetName(), headerRow, sheetCellDescriptor);
            }
        }
    }

    protected void writeHeaderCell(final String sheetName, final Row headerRow, final CellDescriptor sheetCellDescriptor) {
        try {
            if (!sheetCellDescriptor.isWriteIgnore()) {
                final Cell cell = headerRow.createCell(sheetCellDescriptor.getColumnNumber());
                String headerColumnName = sheetCellDescriptor.getFieldName();
                if (hasColumnHeaderProperties() && getColumnHeaderProperties().containsColumnHeader(sheetName, headerColumnName)) {
                    headerColumnName = getColumnHeaderProperties().getColumnHeader(sheetName, headerColumnName);
                }
                final Converter converter = DEFAULTCONVERTERFACTORY.getConverter(String.class);
                converter.writeCell(cell, headerColumnName);
            }
        } catch (final InstantiationException e) {
            log.trace(String.format("Error writing column header on row %s and column %s with propertyname %s", 0, sheetCellDescriptor.getColumnNumber(), sheetCellDescriptor.getFieldName()), e);
        } catch (final IllegalAccessException e) {
            log.trace(String.format("Error writing column header on row %s and column %s with propertyname %s", 0, sheetCellDescriptor.getColumnNumber(), sheetCellDescriptor.getFieldName()), e);
        } catch (final NonExistentConverterException e) {
            log.trace("Converter cannot be found", e);
        }

    }

    protected void writeDataRows(final Sheet sheet, final TypedList<?> values, final Set<CellDescriptor> sheetCellDescriptors) {
        if (null != sheet) {
            int index = isCreateHeaderRow() ? 1 : 0;
            for (final Object value : values) {
                writeDataRow(sheet, index, value, sheetCellDescriptors);
                index++;
            }
        }
    }

    protected void writeDataRow(final Sheet sheet, final int index, final Object value, final Set<CellDescriptor> sheetCellDescriptors) {
        final Row row = sheet.createRow(index);
        for (final CellDescriptor cellDescriptor : sheetCellDescriptors) {
            final Object cellValue = readCellValueFromObjectProperty(value, cellDescriptor.getFieldName());
            writeDataCell(row, cellValue, cellDescriptor);
        }
    }

    protected void writeDataCell(final Row row, final Object cellValue, final CellDescriptor cellDescriptor) {
        final Cell cell = row.createCell(cellDescriptor.getColumnNumber());
        if (!cellDescriptor.isWriteIgnore()) {
            try {
                final Converter converter = DEFAULTCONVERTERFACTORY.getConverter(cellDescriptor.getType());
                converter.writeCell(cell, cellValue);
            } catch (final InstantiationException e) {
                log.trace(String.format("Error writing cell on row %s and column %s with propertyname %s", row.getRowNum(), cellDescriptor.getColumnNumber(), cellDescriptor.getFieldName()), e);
            } catch (final IllegalAccessException e) {
                log.trace(String.format("Error writing cell on row %s and column %s with propertyname %s", row.getRowNum(), cellDescriptor.getColumnNumber(), cellDescriptor.getFieldName()), e);
            } catch (final NonExistentConverterException e) {
                log.trace("Converter cannot be found", e);
            }
        }
    }

    protected Object readCellValueFromObjectProperty(final Object object, final String propertyName) {
        if (null == object) throw new IllegalArgumentException("Object cannot be null");
        if (StringUtils.isEmpty(propertyName)) throw new IllegalArgumentException("Property name cannot be null");
        try {
            return PropertyUtils.getNestedProperty(object, propertyName);
        } catch (final IllegalAccessException e) {
            throw new PoiParserRuntimeException(String.format("Property %s cannot be read", propertyName), e);
        } catch (final InvocationTargetException e) {
            throw new PoiParserRuntimeException(String.format("Property %s cannot be read", propertyName), e);
        } catch (final NoSuchMethodException e) {
            return readCellValueFromObjectField(object, propertyName);
        }
    }

    protected Object readCellValueFromObjectField(final Object object, final String fieldName) {
        final String[] splittedFieldNames = getSplittedPropertyName(fieldName);
        Object returnObject = null;
        for (final String splittedFieldName : splittedFieldNames) {
            returnObject = getFieldFromObject(object, splittedFieldName);
            if (returnObject == null) {
                return null;
            }
        }
        return returnObject;
    }

    private Object getFieldFromObject(final Object object, final String fieldName) {
        try {
            final Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (final NoSuchFieldException e) {
            // Do nothing
        } catch (final IllegalAccessException e) {
            // Do nothing
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
