package nl.bstoi.poiparser.core.strategy.annotation;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;
import nl.bstoi.poiparser.core.InitialWritePoiParserException;
import nl.bstoi.poiparser.core.ReadPoiParserException;
import nl.bstoi.poiparser.core.RequiredFieldPoiParserException;
import nl.bstoi.poiparser.core.WritePoiParserException;
import nl.bstoi.poiparser.core.strategy.AbstractPoiFileParser;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class AnnotatedPoiFileParser<T> extends AbstractPoiFileParser<T>{
	
	/**
	 * Default constructor
	 */
	public AnnotatedPoiFileParser() {
		// Default constructor
	}
	
	/**
	 * Use a different column field mapping and ignore the @Cell values in class.
	 * @param columnFieldNameMapping
	 */
	public AnnotatedPoiFileParser(Map<String,Integer> columnFieldNameMapping) {
		super(columnFieldNameMapping);
	}
	
	/**
	 * Use a different column field mapping and ignore the @Cell values in class.
	 * @param columnFieldNameMapping
	 */
	public AnnotatedPoiFileParser(Map<String,Integer> columnFieldNameMapping,Set<Integer> requiredColumnNumbers) {
		super(columnFieldNameMapping,requiredColumnNumbers);
	}	
	
	@Override
	public List<T> readExcelFile(File excelFile,String sheetName,Class<T> clazz) throws IOException,FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException{
		FileInputStream fis = null;
		fis = new FileInputStream(excelFile);		
		List<T> dimensionList = readExcelFile(fis, sheetName, clazz);		
		fis.close(); // Close file stream
		return dimensionList;
	}
	
	
	
	@Override
	public List<T> readExcelFile(File excelFile, String sheetName,Class<T> clazz, int startRow, int endRow) throws IOException,FileNotFoundException, InstantiationException,IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException {
		FileInputStream fis = null;
		fis = new FileInputStream(excelFile);		
		List<T> dimensionList = readExcelFile(fis, sheetName, clazz,startRow,endRow);		
		fis.close(); // Close file stream
		return dimensionList;
	}
	
	@Override
	public List<T> readExcelFile(File excelFile, String sheetName,Class<T> clazz, int startRow) throws IOException,FileNotFoundException, InstantiationException,IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException {
		FileInputStream fis = null;
		fis = new FileInputStream(excelFile);		
		List<T> dimensionList = readExcelFile(fis, sheetName, clazz,startRow);		
		fis.close(); // Close file stream
		return dimensionList;
	}
	
	@Override
	public List<T> readExcelFile(InputStream inputStream,String sheetName,Class<T> clazz) throws IOException,FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException{
		Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);		
		return readSheet(sheet, clazz);
	}
	
	@Override
	public List<T> readExcelFile(InputStream inputStream, String sheetName,Class<T> clazz, int startRow, int endRow) throws IOException,FileNotFoundException, InstantiationException,IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException {
		Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);		
		return readSheet(sheet, clazz,startRow,endRow);
	}
	
	@Override
	public List<T> readExcelFile(InputStream inputStream, String sheetName,Class<T> clazz, int startRow) throws IOException,FileNotFoundException, InstantiationException,IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException {
		Sheet sheet = getSheetFromInputStream(inputStream, sheetName, clazz);		
		return readSheet(sheet, clazz,startRow);
	}

	private Sheet getSheetFromInputStream(InputStream inputStream,String sheetName, Class<T> clazz) throws IOException {
		// Get mapping for classfile
		if(getColumnFieldNameMapping().isEmpty()) parseExcelMappingForClass(clazz);		
		// Open excel file		
		
		// Read work book
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(inputStream);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		// Get correct sheet.
		Sheet sheet=workbook.getSheet(sheetName);
		return sheet;
	}
	
	@Override
	public void writeExcelFile(OutputStream outputStream,LinkedHashMap<String, List<T>> excelDataMap,Class<T> clazz) throws IOException, InitialWritePoiParserException,WritePoiParserException{
		if(getColumnFieldNameMapping().isEmpty()) parseExcelMappingForClass(clazz);
		writeWorkbook(excelDataMap).write(outputStream);
	}
	
	/**
	 * Recursive read of super classes that have excel parser information
	 * @param clazz
	 * @param excelMapping
	 */
	protected void parseExcelMappingForClass(Class clazz){
		final Set<String> uniquePropertiesForClass = new HashSet<String>();
		// Parse super class first and override configuration when needed
		if(clazz.getSuperclass()!=Object.class)	parseExcelMappingForClass(clazz.getSuperclass());
		for(PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(clazz)){			
			if(!new String("class").equals(propertyDescriptor.getName())){
				Cell cell = null;
				try {
					
					Field field = clazz.getDeclaredField(propertyDescriptor.getDisplayName());
					cell = field.getAnnotation(Cell.class);
					if(cell==null){
						// When field has no annotation then check method
						Method method = propertyDescriptor.getReadMethod();
						if(null!=method)cell = method.getAnnotation(Cell.class);
					}					
					
					
				} catch (SecurityException e) {
					throw e; // Rethrow security exception
				} catch (NoSuchFieldException e) {
					// If field does not exist try to get value from getter(method)
					Method method = propertyDescriptor.getReadMethod();
					if(null!=method)cell = method.getAnnotation(Cell.class);
				}
				// If annotation is set then add column field mapping
				if(cell!=null){
					boolean ignoreRead = (null==propertyDescriptor.getWriteMethod()||cell.readIgnore()); // Whether the property can be read from excel and be written to the bean 
					boolean ignoreWrite = (null==propertyDescriptor.getReadMethod()||cell.writeIgnore()); // Whether the property can be written excel and be read to from bean
					if(!uniquePropertiesForClass.contains(propertyDescriptor.getName())){
						uniquePropertiesForClass.add(propertyDescriptor.getName());
						addColumnFieldMapping(propertyDescriptor.getName(), cell.columnNumber(), cell.required(), ignoreRead, ignoreWrite, cell.regex());
					}else{
						// Throw exception when field 
						throw new IllegalStateException("Field name ["+propertyDescriptor.getName()+"] is defined more than once for parsing");
					}
				}
			}			
		}
		
	}
	
	/**
	 * Adds a new field to the abstract excel parser.
	 * @param fieldName
	 * @param columnNumber
	 * @param required
	 * @param readIgnore
	 * @param writeIgnore
	 * @param fieldRegex
	 */
	private void addColumnFieldMapping(String fieldName,Integer columnNumber,boolean required,boolean readIgnore,boolean writeIgnore,String fieldRegex){
		addColumnFieldNameMapping(fieldName, columnNumber);
		if(required)addRequiredField(columnNumber);
		if(readIgnore)addReadIgnoreColumn(columnNumber);
		if(writeIgnore)addWriteIgnoreColumn(columnNumber);
		if(null!=fieldRegex&&!fieldRegex.isEmpty())  addColumnFieldRegex(fieldName, fieldRegex);
	}


}
