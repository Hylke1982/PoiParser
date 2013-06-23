package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;
import nl.bstoi.poiparser.api.strategy.annotations.Embedded;
import nl.bstoi.poiparser.core.*;
import nl.bstoi.poiparser.core.exception.ReadPoiParserException;
import nl.bstoi.poiparser.core.strategy.AbstractPoiFileParser;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


public class AnnotatedPoiFileParser<T> extends AbstractPoiFileParser<T> {

    /**
     * Default constructor
     */
    public AnnotatedPoiFileParser() {
        // Default constructor
    }

    /**
     * Use a different column field mapping and ignore the @Cell values in class.
     *
     * @param columnFieldNameMapping
     */
    public AnnotatedPoiFileParser(Map<String, Integer> columnFieldNameMapping) {
        super(columnFieldNameMapping);
    }

    /**
     * Use a different column field mapping and ignore the @Cell values in class.
     *
     * @param columnFieldNameMapping
     */
    public AnnotatedPoiFileParser(Map<String, Integer> columnFieldNameMapping, Set<Integer> requiredColumnNumbers) {
        super(columnFieldNameMapping, requiredColumnNumbers);
    }

    @Override
    public List<T> readExcelFile(File excelFile, String sheetName, Class<T> clazz) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        FileInputStream fis = null;
        fis = new FileInputStream(excelFile);
        List<T> dimensionList = null;
        try {
            dimensionList = readExcelFile(fis, sheetName, clazz);
        } finally {
            fis.close(); // Close file stream

        }
        if (null == dimensionList) throw new ReadPoiParserRuntimeException();
        return dimensionList;
    }


    @Override
    public List<T> readExcelFile(File excelFile, String sheetName, Class<T> clazz, int startRow, int endRow) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        FileInputStream fis = null;
        fis = new FileInputStream(excelFile);
        List<T> dimensionList = null;
        try {

            dimensionList = readExcelFile(fis, sheetName, clazz, startRow, endRow);
        } finally {
            fis.close(); // Close file stream
        }
        if (null == dimensionList) throw new ReadPoiParserRuntimeException();
        return dimensionList;
    }

    @Override
    public List<T> readExcelFile(File excelFile, String sheetName, Class<T> clazz, int startRow) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        FileInputStream fis = null;
        fis = new FileInputStream(excelFile);
        List<T> dimensionList = null;
        try {
            dimensionList = readExcelFile(fis, sheetName, clazz, startRow);
        } finally {
            fis.close(); // Close file stream

        }
        if (null == dimensionList) throw new ReadPoiParserRuntimeException();
        return dimensionList;
    }

    @Override
    public List<T> readExcelFile(InputStream inputStream, String sheetName, Class<T> clazz) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);
        return readSheet(sheet, clazz);
    }

    @Override
    public List<T> readExcelFile(InputStream inputStream, String sheetName, Class<T> clazz, int startRow, int endRow) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);
        return readSheet(sheet, clazz, startRow, endRow);
    }

    @Override
    public List<T> readExcelFile(InputStream inputStream, String sheetName, Class<T> clazz, int startRow) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException {
        Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);
        return readSheet(sheet, clazz, startRow);
    }

    private Sheet getSheetFromInputStream(InputStream inputStream, String sheetName, Class<T> clazz) throws IOException {
        // Get mapping for classfile
        if (getColumnFieldNameMapping().isEmpty()) parseExcelMappingForClass(clazz, null);
        // Open excel file

        // Read work book
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        // Get correct sheet.
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    @Override
    public void writeExcelFile(OutputStream outputStream, LinkedHashMap<String, List<T>> excelDataMap, Class<T> clazz) throws IOException, InitialWritePoiParserException, WritePoiParserException {
        if (getColumnFieldNameMapping().isEmpty()) parseExcelMappingForClass(clazz, null);
        writeWorkbook(excelDataMap).write(outputStream);
    }

    /**
     * Recursive read of super classes that have excel parser information
     *
     * @param clazz
     * @param embeddedFieldName
     */
    protected void parseExcelMappingForClass(Class clazz, String embeddedFieldName) {
        final Set<String> uniquePropertiesForClass = new HashSet<String>();
        // Parse super class first and override configuration when needed
        if (clazz.getSuperclass() != Object.class) parseExcelMappingForClass(clazz.getSuperclass(), null);
        for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(clazz)) {
            if (isNotIgnoredProperty(propertyDescriptor)) {
                Cell cell = null;
                try {

                    Field field = clazz.getDeclaredField(propertyDescriptor.getDisplayName());
                    cell = field.getAnnotation(Cell.class);
                    if (cell == null) {
                        // When field has no annotation then check method
                        Method method = propertyDescriptor.getReadMethod();
                        if (null != method) cell = method.getAnnotation(Cell.class);
                    }

                    Embedded embedded = field.getAnnotation(Embedded.class);
                    if (isCorrectEmbeddedField(cell, embedded) && hasNoTypeRecursion(clazz, field.getType())) {
                        parseExcelMappingForClass(field.getType(), field.getName());
                    }


                } catch (SecurityException e) {
                    throw e; // Rethrow security exception
                } catch (NoSuchFieldException e) {
                    // If field does not exist try to get value from getter(method)
                    Method method = propertyDescriptor.getReadMethod();
                    if (null != method) cell = method.getAnnotation(Cell.class);
                }
                readCell(uniquePropertiesForClass, propertyDescriptor, cell, embeddedFieldName);

            }
        }

    }

    private boolean isCorrectEmbeddedField(Cell cell, Embedded embedded) {
        return null == cell && null != embedded;
    }

    private boolean hasNoTypeRecursion(Class declaringClass, Class fieldType) {
        return !declaringClass.equals(fieldType);
    }

    private void readCell(Set<String> uniquePropertiesForClass, PropertyDescriptor propertyDescriptor, Cell cell, String embeddedFieldName) {
        // If annotation is set then add column field mapping
        if (cell != null) {
            boolean ignoreRead = (null == propertyDescriptor.getWriteMethod() || cell.readIgnore()); // Whether the property can be read from excel and be written to the bean
            boolean ignoreWrite = (null == propertyDescriptor.getReadMethod() || cell.writeIgnore()); // Whether the property can be written excel and be read to from bean
            if (!uniquePropertiesForClass.contains(propertyDescriptor.getName())) {
                uniquePropertiesForClass.add(propertyDescriptor.getName());
                addColumnFieldMapping(embeddedPropertyName(embeddedFieldName, propertyDescriptor.getName()), cell.columnNumber(), cell.required(), ignoreRead, ignoreWrite, cell.regex());
            } else {
                // Throw exception when field
                throw new IllegalStateException("Field name [" + propertyDescriptor.getName() + "] is defined more than once for parsing");
            }
        }
    }

    private String embeddedPropertyName(String embeddedFieldName, String propertyName) {
        if (null == embeddedFieldName) return propertyName;
        return embeddedFieldName + "." + propertyName;
    }

    private boolean isNotIgnoredProperty(PropertyDescriptor propertyDescriptor) {
        return !new String("class").equals(propertyDescriptor.getName());
    }

    /**
     * Adds a new field to the abstract excel parser.
     *
     * @param fieldName
     * @param columnNumber
     * @param required
     * @param readIgnore
     * @param writeIgnore
     * @param fieldRegex
     */
    private void addColumnFieldMapping(String fieldName, Integer columnNumber, boolean required, boolean readIgnore, boolean writeIgnore, String fieldRegex) {
        addColumnFieldNameMapping(fieldName, columnNumber);
        if (required) addRequiredField(columnNumber);
        if (readIgnore) addReadIgnoreColumn(columnNumber);
        if (writeIgnore) addWriteIgnoreColumn(columnNumber);
        if (null != fieldRegex && !fieldRegex.isEmpty()) addColumnFieldRegex(fieldName, fieldRegex);
    }


}
