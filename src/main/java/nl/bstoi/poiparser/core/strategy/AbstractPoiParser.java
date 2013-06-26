package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.RequiredFieldPoiParserException;
import nl.bstoi.poiparser.core.strategy.factory.DefaultConverterFactory;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 21:42
 */
public abstract class AbstractPoiParser<T> {

    private final static Log log = LogFactory.getLog(AbstractPoiParser.class);

    private final Class<T> clazz;
    private final Set<CellDescriptor> cellDescriptors;
    private final Sheet sheet;
    private final boolean ignoreEmptyRows = true;
    private boolean ignoreFirstRow = false;
    private final DefaultConverterFactory DEFAULTCONVERTERFACTORY = new DefaultConverterFactory();
    private DefaultConverterFactory converterFactory = DEFAULTCONVERTERFACTORY;


    public AbstractPoiParser(Set<CellDescriptor> cellDescriptors, final Sheet sheet, final Class<T> clazz) {
        if (null == cellDescriptors) throw new IllegalArgumentException("Cell descriptors cannot be null");
        if (null == sheet) throw new IllegalArgumentException("Sheet cannot be null");
        if (null == clazz) throw new IllegalArgumentException("Clazz cannot be null");
        this.cellDescriptors = cellDescriptors;
        this.sheet = sheet;
        this.clazz = clazz;
    }

    public Set<CellDescriptor> getCellDescriptors() {
        return cellDescriptors;
    }

    protected List<T> readSheet() throws PoiParserException {
        try {
            List<T> dimensionList = new ArrayList<T>();
            if (null != getSheet()) {
                Iterator<Row> rowIterator = sheet.rowIterator();
                if (rowIterator.hasNext()) {
                    // Skip first row
                    if (ignoreFirstRow) rowIterator.next();
                    while (rowIterator.hasNext()) {
                        Row row = (Row) rowIterator.next();
                        if (!ignoreRow(row)) {
                            T rowObject = readRow(sheet.getSheetName(), row, clazz.newInstance());
                            dimensionList.add(rowObject);
                        }
                    }
                }
            }
            return dimensionList;
        } catch (InstantiationException e) {
            throw new PoiParserException("Error while creating instance of clazz", e);
        } catch (IllegalAccessException e) {
            throw new PoiParserException("Error while accessing clazz", e);
        }
    }

    protected List<T> readSheet(int startRow, int endRow) throws PoiParserException {
        try {
            List<T> dimensionList = new ArrayList<T>();
            if (null != sheet) {
                for (int i = startRow; i <= endRow; i++) {
                    Row row = (Row) sheet.getRow(i);
                    if (!ignoreRow(row)) {
                        T rowObject = readRow(sheet.getSheetName(), row, clazz.newInstance());
                        dimensionList.add(rowObject);
                    }
                }
            }
            return dimensionList;
        } catch (InstantiationException e) {
            throw new PoiParserException("Error while creating instance of clazz", e);
        } catch (IllegalAccessException e) {
            throw new PoiParserException("Error while accessing clazz", e);
        }
    }

    public boolean ignoreRow(Row row) {
        if (isEmptyRow(row)) {
            return ignoreEmptyRows;
        }
        return false;
    }


    public boolean isEmptyRow(Row row) {
        if (null != row) {
            for (CellDescriptor cellDescriptor : getCellDescriptors()) {
                Integer columnNumber = cellDescriptor.getColumnNumber();
                if (!isEmptyValue(row.getCell(columnNumber, Row.RETURN_BLANK_AS_NULL))) return false;
            }
        }
        return true;
    }

    public boolean isEmptyValue(Cell cell) {
        if (null == cell) return true;
        return false;
    }

    protected T readRow(String sheetName, Row row, T rowDimension) throws PoiParserException {
        if (null != row) {
            log.debug("Read row with number: " + row.getRowNum());
            for (CellDescriptor cellDescriptor : getCellDescriptors()) {
                readField(sheetName, row, rowDimension, cellDescriptor);
            }
        }
        return rowDimension;
    }

    protected void readField(String sheetName, Row row, T rowDimension, CellDescriptor cellDescriptor) throws PoiParserException {

        try {
            if (cellDescriptor.isReadIgnore()) {
                return; // When field must be ignored when reading then do nothing.
            }
            Cell cell = row.getCell(cellDescriptor.getColumnNumber(), Row.RETURN_BLANK_AS_NULL);
            if (null != cell) {
                log.trace("Reading field " + cellDescriptor.getFieldName() + " on row " + row.getRowNum() + " that is mapped on column " + cellDescriptor.getColumnNumber() + " with value: " + cell.toString());
                Converter converter = converterFactory.getConverter(cellDescriptor.getType());
                if (null != converter) {
                    try {
                        populateDimensionAsField(rowDimension, cellDescriptor.getFieldName(), converter, cell);
                    } catch (PoiParserException e) {
                        populateDimensionAsProperty(rowDimension, cellDescriptor.getFieldName(), converter, cell);
                    }
                } else {
                    // Unsupported type
                }
            } else {
                log.trace("Reading field " + cellDescriptor.getFieldName() + " on row " + row.getRowNum() + " that is mapped on column " + cellDescriptor.getColumnNumber() + " is empty.");
                if (cellDescriptor.isRequired()) {
                    throw new RequiredFieldPoiParserException(sheetName, row.getRowNum(), cellDescriptor.getColumnNumber());
                }
            }
        } catch (PoiParserException e) {
            throw e;
        } catch (InstantiationException e) {
            throw new PoiParserException("Error while setting field/property", e);
        } catch (IllegalAccessException e) {
            throw new PoiParserException("Error while setting field/property", e);
        }
    }

    private void populateDimensionAsField(T rowDimension, String fieldName, Converter<T> converter, Cell cell) throws PoiParserException {
        try {
            rowDimension.getClass().getDeclaredField(fieldName).setAccessible(true);
            Field field = rowDimension.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(rowDimension, converter.readCell(cell));
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new PoiParserException("Unable set field " + fieldName, e);
        } catch (NoSuchFieldException e) {
            throw new PoiParserException("Unable set field " + fieldName, e);
        }
    }

    private void populateDimensionAsProperty(T rowDimension, String propertyName, Converter<T> converter, Cell cell) throws PoiParserException {
        try {
            createRequiredUnderlyingInstancesForNestedProperties(rowDimension, propertyName);
            PropertyUtils.setNestedProperty(rowDimension, propertyName, converter.readCell(cell));
        } catch (IllegalAccessException e) {
            throw new PoiParserException("Unable set property " + propertyName, e);
        } catch (InvocationTargetException e) {
            throw new PoiParserException("Unable set property " + propertyName, e);
        } catch (NoSuchMethodException e) {
            throw new PoiParserException("Unable set property " + propertyName, e);
        }
    }

    private void createRequiredUnderlyingInstancesForNestedProperties(T rowDimension, String fieldName) {
        String concatName = null;
        String[] propertyNames = fieldName.split("\\.");
        for (String createdInstanceName : propertyNames) {
            if (fieldName.endsWith(createdInstanceName)) return;
            if (null == concatName) {
                concatName = createdInstanceName;
            } else {
                concatName += "." + createdInstanceName;
            }
            try {
                if (null == PropertyUtils.getProperty(rowDimension, concatName)) {
                    Object x = PropertyUtils.getPropertyDescriptor(rowDimension, concatName).getPropertyType().newInstance();
                    PropertyUtils.setNestedProperty(rowDimension, concatName, x);

                }
            } catch (IllegalAccessException e) {
                log.trace("Error creating underlying instance", e);
            } catch (InvocationTargetException e) {
                log.trace("Error creating underlying instance", e);
            } catch (NoSuchMethodException e) {
                log.trace("Error creating underlying instance", e);
            } catch (InstantiationException e) {
                log.trace("Error creating underlying instance", e);
            }
        }
    }

    public Sheet getSheet() {
        return sheet;
    }
}
