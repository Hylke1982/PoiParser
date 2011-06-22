package nl.bstoi.poiparser.core.strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.InitialWritePoiParserException;
import nl.bstoi.poiparser.core.ReadPoiParserException;
import nl.bstoi.poiparser.core.RequiredFieldPoiParserException;
import nl.bstoi.poiparser.core.WritePoiParserException;
import nl.bstoi.poiparser.core.strategy.factory.DefaultConverterFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractPoiFileParser<T> {
	
	private final static Log log = LogFactory.getLog(AbstractPoiFileParser.class);
		
	private Map<String,Integer> columnFieldNameMapping = new HashMap<String, Integer>();
	private Set<Integer> requiredFields = new HashSet<Integer>();
	private Set<Integer> writeIgnoreColumns = new HashSet<Integer>();
	private Set<Integer> readIgnoreColumns = new HashSet<Integer>();
	
	private Map<String,String> headerFieldNameTranslationMapping = new HashMap<String, String>();
	
	private DefaultConverterFactory defaultConverterFactory = new DefaultConverterFactory();
	
	public AbstractPoiFileParser() {
		// Default constructor
	}
	
	public AbstractPoiFileParser(Map<String,Integer> columnFieldNameMapping){
		this.columnFieldNameMapping = columnFieldNameMapping;
	}
	
	public AbstractPoiFileParser(Map<String,Integer> columnFieldNameMapping,Set<Integer> requiredFields) {
		this.columnFieldNameMapping = columnFieldNameMapping;
		this.requiredFields = requiredFields;
	}
	
	
	
	/**
	 * When values is false empty rows will become new objects.
	 */
	private boolean ignoreEmptyRows = true;
	private boolean ignoreFirstRow = true;
	private boolean createHeaderRow = true;
	private boolean removeWriteIgnoreColumns = false;
	
	//----------------------------------------------------------------------------------//
	// Interface methods
	//----------------------------------------------------------------------------------//
	public abstract List<T> readExcelFile(File excelFile,String sheetName,Class<T> clazz) throws IOException,FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException;
	
	public abstract List<T> readExcelFile(InputStream inputStream,String sheetName,Class<T> clazz) throws IOException,FileNotFoundException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException;
	
	public abstract void writeExcelFile(OutputStream outputStream,LinkedHashMap<String, List<T>> excelDataMap,Class<T> clazz) throws IOException, InitialWritePoiParserException,WritePoiParserException;
	
	//----------------------------------------------------------------------------------//
	// Read sheet
	//----------------------------------------------------------------------------------//
	/**
	 * Read Excel sheet.
	 * @param sheet
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws RequiredFieldPoiParserException
	 */
	protected List<T> readSheet(Sheet sheet,Class<T> clazz) throws InstantiationException, IllegalAccessException, RequiredFieldPoiParserException,ReadPoiParserException{
		List<T> dimensionList = new ArrayList<T>();
		if(null!=sheet){
			Iterator<Row> rowIterator = sheet.rowIterator();
			if(rowIterator.hasNext()){
				// Skip first row
				if(ignoreFirstRow)rowIterator.next();
				while(rowIterator.hasNext()){
					Row row = (Row)rowIterator.next();
					if(!ignoreRow(row)){
						T rowObject = readRow(sheet.getSheetName(),row, clazz.newInstance());
						dimensionList.add(rowObject);
					}
				}
			}
		}
		return dimensionList;
	}
	
	
	/**
	 * <p>Read a excel row and convert the selected field into a row dimension</p>
	 * @param row
	 * @param rowDimension
	 * @return
	 * @throws RequiredFieldPoiParserException 
	 */
	protected T readRow(String sheetName,Row row,T rowDimension) throws RequiredFieldPoiParserException,ReadPoiParserException{
		if(null!=row){
			log.debug("Read row with number: " +row.getRowNum());		
			for(String fieldName : columnFieldNameMapping.keySet()){
				readField(sheetName,row, rowDimension, fieldName);			
			}
		}
		return rowDimension;
	}
	
	/**
	 * <p>Read a specific field and fill it with the value in the excel</p>
	 * @param row Excel row
	 * @param rowDimension Abstract representation of a row
	 * @param fieldName Name of a field in the class <T>
	 * @throws RequiredFieldPoiParserException 
	 */
	protected void readField(String sheetName,Row row,T rowDimension,String fieldName) throws RequiredFieldPoiParserException,ReadPoiParserException{
		int columnNumber = columnFieldNameMapping.get(fieldName);
		try {
			Class type = PropertyUtils.getPropertyType(rowDimension, fieldName);
			
			if(readIgnoreColumns.contains(columnNumber)) return; // When field must be ignored when reading then do nothing.
			Cell cell = row.getCell(columnFieldNameMapping.get(fieldName),Row.RETURN_BLANK_AS_NULL);
			if(null!=cell){
				log.trace("Reading field "+fieldName+" on row " +row.getRowNum()+ " that is mapped on column "+columnFieldNameMapping.get(fieldName) + " with value: "+cell.toString());
				Converter converter = getDefaultConverterFactory().getConverter(type);
				if(null!=converter){
					PropertyUtils.setProperty(rowDimension, fieldName,converter.readCell(cell));
				}else{
					// Unsupported type
				}
			}else{
				log.trace("Reading field "+fieldName+" on row " +row.getRowNum()+ " that is mapped on column "+columnFieldNameMapping.get(fieldName) + " is empty.");
				for(Integer requiredColumnNumber : requiredFields){
					if(requiredColumnNumber.intValue()==columnNumber)throw new RequiredFieldPoiParserException(sheetName,row.getRowNum(), columnNumber);
				}			
			}
		} catch (IllegalAccessException e) {			
			throw new ReadPoiParserException(row.getRowNum(),columnNumber, e);
		} catch (InvocationTargetException e) {
			throw new ReadPoiParserException(row.getRowNum(),columnNumber, e);
		} catch (NoSuchMethodException e) {
			throw new ReadPoiParserException(row.getRowNum(),columnNumber, e);
		} catch (InstantiationException e) {
			throw new ReadPoiParserException(row.getRowNum(),columnNumber, e);
		} 
	}
	
	
	
	//----------------------------------------------------------------------------------//
	// Write sheet
	//----------------------------------------------------------------------------------//
	protected Workbook writeWorkbook(LinkedHashMap<String, List<T>> excelDataMap) throws InitialWritePoiParserException,WritePoiParserException{
		Workbook workbook = new HSSFWorkbook();
		remapColumnFieldMapForWriting();
		for(String sheetName : excelDataMap.keySet()){
			writeSheet(workbook, sheetName, excelDataMap.get(sheetName));
		}
		return workbook;		
	}
	
	protected void writeSheet(Workbook workbook,String sheetName,List<T> dimensionList)throws WritePoiParserException{
		Sheet sheet = workbook.createSheet(sheetName);
		int i = 0;
		if(createHeaderRow)writeHeaderRow(sheet, i++);
		for(T dimension : dimensionList){
			writeRow(sheet, dimension,i++);
		}
	}
	
	protected void writeRow(Sheet sheet,T dimension,Integer rowNumber)throws WritePoiParserException{
		Row row = sheet.createRow(rowNumber);
		for(String fieldName : columnFieldNameMapping.keySet()){
			writeCell(row, dimension, fieldName);
		}
	}
	
	protected void writeHeaderRow(Sheet sheet,Integer rowNumber) {
		Row row = sheet.createRow(rowNumber);
		try{
			for(String headerFieldName : columnFieldNameMapping.keySet()){
				String headerName = headerFieldName;
				if(headerFieldNameTranslationMapping.containsKey(headerFieldName)) headerName = headerFieldNameTranslationMapping.get(headerFieldName);
				Cell headerCell = row.createCell(getColumnFieldNameMapping().get(headerFieldName));
				Converter stringConverter = defaultConverterFactory.getConverter(String.class);
				stringConverter.writeCell(headerCell, headerName);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected void writeCell(Row row,T dimension,String fieldName) throws WritePoiParserException{
		int columnNumber = columnFieldNameMapping.get(fieldName);
		if(writeIgnoreColumns.contains(columnNumber)) return; // When column must be ignored when writing then do nothing;
		Cell cell = row.createCell(columnNumber);
		try {			
			Object valueObject = PropertyUtils.getProperty(dimension, fieldName);
			if(null!=valueObject){
				Converter converter = getDefaultConverterFactory().getConverter(valueObject.getClass());
				if(null!=converter) converter.writeCell(cell, valueObject);
			}
		} catch (IllegalAccessException e) {
			throw new WritePoiParserException(row.getRowNum(), columnNumber, e.getMessage(),e);
		} catch (InvocationTargetException e) {
			throw new WritePoiParserException(row.getRowNum(), columnNumber, e.getMessage(),e);
		} catch (NoSuchMethodException e) {
			throw new WritePoiParserException(row.getRowNum(), columnNumber, e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new WritePoiParserException(row.getRowNum(), columnNumber, e.getMessage(),e);
		}
	}
	
	
	
	
	//----------------------------------------------------------------------------------//
	// Parser properties and functions
	//----------------------------------------------------------------------------------//
	/**
	 * If row is empty, the ignore empty row setting will determine the adding of the row.
	 * @param hssfRow
	 * @return
	 */
	public boolean ignoreRow(Row row){
		if(isEmptyRow(row)){
			return ignoreEmptyRows;
		}
		return false;
	}
	
	/**
	 * Check whether a row is empty based on the column values
	 * @param hssfRow
	 * @return
	 */
	public boolean isEmptyRow(Row row){
		if(null!=row){
			for(String fieldName : columnFieldNameMapping.keySet()){
				Integer columnNumber = columnFieldNameMapping.get(fieldName);
				if(!isEmptyValue(row.getCell(columnNumber, Row.RETURN_BLANK_AS_NULL))) return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the hssfCell is empty or blank
	 * @param hssfCell
	 * @return
	 */
	public boolean isEmptyValue(Cell cell){
		if(null==cell) return true;
		return false;
	}
	
	/**
	 * Validate if columns are used more than once.
	 * @throws InitialWritePoiParserException
	 */
	protected void validateColumnFieldMapForWriting() throws InitialWritePoiParserException{
		Set<Integer> uniqueColumnNumbers = new HashSet<Integer>();
		for(String key : columnFieldNameMapping.keySet()){
			if(!uniqueColumnNumbers.contains(columnFieldNameMapping.get(key))){
				uniqueColumnNumbers.add(columnFieldNameMapping.get(key));
			}else{ 
				throw new InitialWritePoiParserException(columnFieldNameMapping.get(key));
			}
		}
	}
	
	/**
	 * Re-map the columns when empty columns are ignored
	 * @throws InitialWritePoiParserException
	 */
	protected void remapColumnFieldMapForWriting() throws InitialWritePoiParserException{
		validateColumnFieldMapForWriting();
		if(removeWriteIgnoreColumns){
			List<Integer> orderedColumnList = new ArrayList<Integer>();
			Map<Integer,String> reverseColumnFieldMap = new HashMap<Integer, String>();
			Map<String,Integer> rebuildColumnFieldMap = new HashMap<String, Integer>();
			
			// Fill collections for remapping data
			for(String key : columnFieldNameMapping.keySet()){
				orderedColumnList.add(columnFieldNameMapping.get(key));
				reverseColumnFieldMap.put(columnFieldNameMapping.get(key), key);
			}
			
			// Sort ordered column list
			Collections.sort(orderedColumnList);
			Integer newColumnNumber = 0;
			for(Integer columnNumber : orderedColumnList){
				if(!writeIgnoreColumns.contains(columnNumber)){
					rebuildColumnFieldMap.put(reverseColumnFieldMap.get(columnNumber),newColumnNumber);
					newColumnNumber++;
				}
			}
			
			columnFieldNameMapping = rebuildColumnFieldMap;
		}
	}

	
	//----------------------------------------------------------------------------------//
	// Getters/Setters
	//----------------------------------------------------------------------------------//
	public boolean isIgnoreEmptyRows() {
		return ignoreEmptyRows;
	}

	public void setIgnoreEmptyRows(boolean ignoreEmptyRows) {
		this.ignoreEmptyRows = ignoreEmptyRows;
	}
	
	public void setIgnoreFirstRow(boolean ignoreFirstRow) {
		this.ignoreFirstRow = ignoreFirstRow;
	}
	
	public boolean isIgnoreFirstRow() {
		return ignoreFirstRow;
	}
	
	/**
	 * If columns that are ignored must be deleted from the sheet when writing.
	 * @return
	 */
	public boolean isRemoveWriteIgnoreColumns() {
		return removeWriteIgnoreColumns;
	}

	/**
	 * Set if the parser deletes the columns when writing a sheet. 
	 * @param removeWriteIgnoreColumns
	 */
	public void setRemoveWriteIgnoreColumns(boolean removeWriteIgnoreColumns) {
		this.removeWriteIgnoreColumns = removeWriteIgnoreColumns;
	}

	public Map<String, Integer> getColumnFieldNameMapping() {
		return columnFieldNameMapping;
	}
	
	
	public Map<String, String> getHeaderFieldNameTranslationMapping() {
		return headerFieldNameTranslationMapping;
	}

	public void setHeaderFieldNameTranslationMapping(
			Map<String, String> headerFieldNameTranslationMapping) {
		this.headerFieldNameTranslationMapping = headerFieldNameTranslationMapping;
	}

	/**
	 * Add a mapping for a column
	 * @param fieldName
	 * @param columnNumber
	 */
	public void addColumnFieldNameMapping(String fieldName,Integer columnNumber){
		columnFieldNameMapping.put(fieldName, columnNumber);
	}
	
	/**
	 * Add the column number for a required field
	 * @param columnNumber
	 */
	public void addRequiredField(Integer columnNumber){
		requiredFields.add(columnNumber);		
	}
	
	/**
	 * Add a column number for ignoring when reading.
	 * @param columnNumber
	 */
	public void addReadIgnoreColumn(Integer columnNumber){
		readIgnoreColumns.add(columnNumber);
	}
	
	/**
	 * Add a column number for ignoring when writing.
	 * @param columnNumber
	 */
	public void addWriteIgnoreColumn(Integer columnNumber){
		writeIgnoreColumns.add(columnNumber);
	}
	
	public Set<Integer> getWriteIgnoreColumns() {
		return writeIgnoreColumns;
	}
	
	public Set<Integer> getReadIgnoreColumns() {
		return readIgnoreColumns;
	}
	
	public DefaultConverterFactory getDefaultConverterFactory() {
		return defaultConverterFactory;
	}
	
	

}
