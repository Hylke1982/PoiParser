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

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public abstract class AbstractWritePoiParser extends AbstractPoiParserFactory {
    private final static Log log = LogFactory.getLog(AbstractWritePoiParser.class);

    private final DefaultConverterFactory DEFAULTCONVERTERFACTORY = new DefaultConverterFactory();

    private boolean createHeaderRow = false;
    private final Workbook workbook;

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
            System.out.println("cd: "+cellDescriptor.getColumnNumber()+" cd: "+cellDescriptor.getFieldName());
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
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            // Try field access read here
            //throw new PoiParserRuntimeException(String.format("Property %s cannot be read", propertyName), e);
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
}
