package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.exception.ReadPoiParserException;
import nl.bstoi.poiparser.core.RequiredFieldPoiParserException;
import nl.bstoi.poiparser.core.strategy.factory.DefaultConverterFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
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
    private boolean ignoreFirstRow = true;
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
            if (null != sheet) {
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

    protected T readRow(String sheetName, Row row, T rowDimension) throws RequiredFieldPoiParserException, ReadPoiParserException {
        if (null != row) {
            log.debug("Read row with number: " + row.getRowNum());
            for (CellDescriptor cellDescriptor : getCellDescriptors()) {
                readField(sheetName, row, rowDimension, cellDescriptor);
            }
        }
        return rowDimension;
    }

    protected void readField(String sheetName, Row row, T rowDimension, CellDescriptor cellDescriptor) throws RequiredFieldPoiParserException, ReadPoiParserException {
        try {


            if (cellDescriptor.isReadIgnore()) {
                return; // When field must be ignored when reading then do nothing.
            }
            Cell cell = row.getCell(cellDescriptor.getColumnNumber(), Row.RETURN_BLANK_AS_NULL);
            if (null != cell) {
                log.trace("Reading field " + cellDescriptor.getFieldName() + " on row " + row.getRowNum() + " that is mapped on column " + cellDescriptor.getColumnNumber() + " with value: " + cell.toString());
                Converter converter = converterFactory.getConverter(cellDescriptor.getType());
                if (null != converter) {
                    PropertyUtils.setNestedProperty(rowDimension, cellDescriptor.getFieldName(), converter.readCell(cell));
                } else {
                    // Unsupported type
                }
            } else {
                log.trace("Reading field " + cellDescriptor.getFieldName() + " on row " + row.getRowNum() + " that is mapped on column " + cellDescriptor.getColumnNumber() + " is empty.");
                if (cellDescriptor.isRequired()) {
                    throw new RequiredFieldPoiParserException(sheetName, row.getRowNum(), cellDescriptor.getColumnNumber());
                }
            }
        } catch (IllegalAccessException e) {
            throw new ReadPoiParserException(row.getRowNum(), cellDescriptor.getColumnNumber(), e);
        } catch (InvocationTargetException e) {
            throw new ReadPoiParserException(row.getRowNum(), cellDescriptor.getColumnNumber(), e);
        } catch (NoSuchMethodException e) {
            throw new ReadPoiParserException(row.getRowNum(), cellDescriptor.getColumnNumber(), e);
        } catch (InstantiationException e) {
            throw new ReadPoiParserException(row.getRowNum(), cellDescriptor.getColumnNumber(), e);
        }
    }

    public Sheet getSheet() {
        return sheet;
    }
}
