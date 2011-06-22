package nl.bstoi.poiparser.core.strategy.annotation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import nl.bstoi.poiparser.core.InitialWritePoiParserException;
import nl.bstoi.poiparser.core.ReadPoiParserException;
import nl.bstoi.poiparser.core.RequiredFieldPoiParserException;
import nl.bstoi.poiparser.core.WritePoiParserException;

import org.junit.Test;

public class AnnotatedPoiFileParserTest {
	
	private static final String filePath = "/excel/";
	
	private AnnotatedPoiFileParser<TestRow> annotationExcelParser = new AnnotatedPoiFileParser<TestRow>();
	
	private final static int numberOfCreatedObjects = 10;
	private final static String writeSheet1 = "sheet-001";
	private final static String writeSheet2 = "sheet-002";
	
	@Test
	public void testReadExcelFile() throws URISyntaxException,IOException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException{		
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);
		Assert.assertEquals(2, testRowClasses.size());
	}
	
	@Test
	public void testReadExcelFileWithEmptyRows() throws URISyntaxException,IOException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException{		
		final String fileName = "test-excel-001.xlsx";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet3", TestRow.class);
		Assert.assertEquals(4, testRowClasses.size());
	}
	
	@Test(expected=RequiredFieldPoiParserException.class)
	public void testReadExcelFileWithEmptyRowsAndMissingRequiredField() throws URISyntaxException,IOException, InstantiationException, IllegalAccessException, RequiredFieldPoiParserException, ReadPoiParserException{		
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		annotationExcelParser.readExcelFile(excelFile, "Sheet4", TestRow.class);
	}
	
	@Test
	public void testReadSupportedTypes() throws FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, URISyntaxException, ReadPoiParserException{
		AnnotatedPoiFileParser<TypeTestRow> typeTestRowAnnotatedExcelFileParser = new AnnotatedPoiFileParser<TypeTestRow>();
		typeTestRowAnnotatedExcelFileParser.setIgnoreFirstRow(false);
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TypeTestRow> typeTestRows = typeTestRowAnnotatedExcelFileParser.readExcelFile(excelFile, "TypeTestSheet", TypeTestRow.class);
		
		Assert.assertEquals(2, typeTestRows.size());
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2010);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH,5);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		// Check if values are correctly read.
		Assert.assertEquals(new Short("1234"), typeTestRows.get(0).getShortValue());
		Assert.assertEquals(new Integer(123456), typeTestRows.get(0).getIntegerValue());
		Assert.assertEquals(new Long(12345678910L), typeTestRows.get(0).getLongValue());
		Assert.assertEquals(calendar.getTime(), typeTestRows.get(0).getDateValue());
		// Assert.assertEquals(new BigDecimal("12.21"), typeTestRows.get(0).getBigDecimalValue()); Cannot be done because of in precise double value in Apache POI
		Assert.assertEquals(new String("stringValue"), typeTestRows.get(0).getStringValue());
		Assert.assertEquals(new Boolean(true), typeTestRows.get(0).getBooleanValue());
		
		
	}
	
	@Test
	public void testGetExcelMappingForClass(){
		annotationExcelParser.parseExcelMappingForClass(TestRow.class);
		Map<String,Integer> excelMapping = annotationExcelParser.getColumnFieldNameMapping();
		Assert.assertEquals(new Integer(0), excelMapping.get("id"));
		Assert.assertEquals(new Integer(1), excelMapping.get("name"));
		Assert.assertEquals(new Integer(2), excelMapping.get("methodField"));
		Assert.assertEquals(new Integer(7), excelMapping.get("fieldWithOnlyGet"));
		Assert.assertEquals(new Integer(8), excelMapping.get("fieldWithOnlySet"));
		Assert.assertEquals(new Integer(9), excelMapping.get("fieldWithReadIngnore"));
		Assert.assertEquals(new Integer(10), excelMapping.get("fieldWithWriteIngnore"));
		
		// Ignored properties read
		Assert.assertTrue(annotationExcelParser.getReadIgnoreColumns().contains(4));
		Assert.assertTrue(annotationExcelParser.getReadIgnoreColumns().contains(7));
		Assert.assertTrue(annotationExcelParser.getReadIgnoreColumns().contains(9));
		// Ignored properties write
		Assert.assertTrue(annotationExcelParser.getWriteIgnoreColumns().contains(8));
		Assert.assertTrue(annotationExcelParser.getWriteIgnoreColumns().contains(10));
		
	}
	
	
	@Test
	public void testGetExcelMappingForExtendedClass(){
		AnnotatedPoiFileParser<ExtendTestRow> annotationReadStrategy = new AnnotatedPoiFileParser<ExtendTestRow>();
		annotationReadStrategy.parseExcelMappingForClass(ExtendTestRow.class);
		Map<String,Integer> excelMapping = annotationReadStrategy.getColumnFieldNameMapping();
		Assert.assertEquals(new Integer(0), excelMapping.get("id"));
		Assert.assertEquals(new Integer(1), excelMapping.get("name"));
		Assert.assertEquals(new Integer(2), excelMapping.get("year"));
		Assert.assertEquals(new Integer(2), excelMapping.get("methodField"));
		Assert.assertEquals(new Integer(7), excelMapping.get("fieldWithOnlyGet"));
		Assert.assertEquals(new Integer(8), excelMapping.get("fieldWithOnlySet"));
		
		// Ignored properties read
		Assert.assertTrue(annotationReadStrategy.getReadIgnoreColumns().contains(4));
		Assert.assertTrue(annotationReadStrategy.getReadIgnoreColumns().contains(7));
		Assert.assertTrue(annotationReadStrategy.getReadIgnoreColumns().contains(9));
		// Ignored properties write
		Assert.assertTrue(annotationReadStrategy.getWriteIgnoreColumns().contains(8));
		Assert.assertTrue(annotationReadStrategy.getWriteIgnoreColumns().contains(10));
	}
	
	@Test
	public void testWriteExcelFile() throws URISyntaxException, IOException, RequiredFieldPoiParserException, InstantiationException, IllegalAccessException, InitialWritePoiParserException, ReadPoiParserException,WritePoiParserException{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		annotationExcelParser.writeExcelFile(bos, getTestRowFixture(), TestRow.class);
		
		bos.close();
		
		List<TestRow> writtenTestRowClassList = annotationExcelParser.readExcelFile(new ByteArrayInputStream(bos.toByteArray()), writeSheet1, TestRow.class);
		Assert.assertEquals(numberOfCreatedObjects, writtenTestRowClassList.size());
		
		Assert.assertEquals(new Long(0), writtenTestRowClassList.get(0).getId());
		Assert.assertEquals(new String("sheet-001-name-0"), writtenTestRowClassList.get(0).getName());
		Assert.assertEquals(new String("sheet-001-field-0"), writtenTestRowClassList.get(0).getMethodField());
		Assert.assertEquals(new String("sheet-001-secondName-0"), writtenTestRowClassList.get(0).getSecondName());
		Assert.assertNull(writtenTestRowClassList.get(0).getFieldWithWriteIngnore());		
		Assert.assertNull(writtenTestRowClassList.get(0).fieldWithOnlySet);		
		
	}
	
	@Test(expected=InitialWritePoiParserException.class)
	public void testWriteWithDuplicateColumns() throws URISyntaxException, IOException, RequiredFieldPoiParserException, InstantiationException, IllegalAccessException, InitialWritePoiParserException,WritePoiParserException{
		AnnotatedPoiFileParser<DuplicateColumnTestRow> annotationExcelParser = new AnnotatedPoiFileParser<DuplicateColumnTestRow>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		LinkedHashMap<String, List<DuplicateColumnTestRow>> workbook = new LinkedHashMap<String, List<DuplicateColumnTestRow>>();
		List<DuplicateColumnTestRow> duplicateColumnTestRows = new ArrayList<DuplicateColumnTestRow>();
		duplicateColumnTestRows.add(new DuplicateColumnTestRow());
		workbook.put(writeSheet1, duplicateColumnTestRows);
		annotationExcelParser.writeExcelFile(bos, workbook, DuplicateColumnTestRow.class);
		
	}
	
	@Test
	public void testWriteExcelFileWithoutEmptyColumns() throws URISyntaxException, IOException, RequiredFieldPoiParserException, InstantiationException, IllegalAccessException, InitialWritePoiParserException, ReadPoiParserException, WritePoiParserException{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		annotationExcelParser.setRemoveWriteIgnoreColumns(true);
		annotationExcelParser.writeExcelFile(bos, getTestRowFixture(), TestRow.class);		
		bos.close();
		
		annotationExcelParser = new AnnotatedPoiFileParser<TestRow>();
		List<TestRow> writtenTestRowClassList = annotationExcelParser.readExcelFile(new ByteArrayInputStream(bos.toByteArray()), writeSheet1, TestRow.class);
		Assert.assertEquals(numberOfCreatedObjects, writtenTestRowClassList.size());
		
		Assert.assertEquals(new Long(0), writtenTestRowClassList.get(0).getId());
		Assert.assertEquals(new String("sheet-001-name-0"), writtenTestRowClassList.get(0).getName());
		Assert.assertEquals(new String("sheet-001-field-0"), writtenTestRowClassList.get(0).getMethodField());
		Assert.assertNull(writtenTestRowClassList.get(0).getSecondName()); // Second name is placed on a different column
		Assert.assertNull(writtenTestRowClassList.get(0).getFieldWithWriteIngnore());		
		Assert.assertNull(writtenTestRowClassList.get(0).fieldWithOnlySet);		
		
	}
	
	@Test
	public void testFieldRead() throws URISyntaxException, FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, ReadPoiParserException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);
		Assert.assertEquals(new Long(1), testRowClasses.get(0).getId());
		Assert.assertEquals(new String("Naam1"), testRowClasses.get(0).getName());
		Assert.assertNull(testRowClasses.get(0).getFieldWithReadIngnore());		
		Assert.assertNull(testRowClasses.get(0).getFieldWithOnlyGet());		
	}
	
	@Test
	public void testFieldReadWithoutIgnoreFirstRow() throws URISyntaxException, FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, ReadPoiParserException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		annotationExcelParser.setIgnoreFirstRow(false);
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);
		Assert.assertEquals(new Long(0), testRowClasses.get(0).getId());
		Assert.assertEquals(new String("z"), testRowClasses.get(0).getName());
		Assert.assertNull(testRowClasses.get(0).getFieldWithReadIngnore());		
		Assert.assertNull(testRowClasses.get(0).getFieldWithOnlyGet());		
	}
	
	@Test
	public void testMethodRead() throws URISyntaxException, FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, ReadPoiParserException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);
		Assert.assertEquals(new String("2013"), testRowClasses.get(0).getMethodField());
		Assert.assertNull(testRowClasses.get(0).getFieldWithReadIngnore());		
		Assert.assertNull(testRowClasses.get(0).getFieldWithOnlyGet());		
	}
	
	@Test
	public void testMethodReadWithoutIgnoreFirstRow() throws URISyntaxException, FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, ReadPoiParserException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		annotationExcelParser.setIgnoreFirstRow(false);
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);		
		Assert.assertEquals(new String("2010"), testRowClasses.get(0).getMethodField());
		Assert.assertNull(testRowClasses.get(0).getFieldWithReadIngnore());		
		Assert.assertNull(testRowClasses.get(0).getFieldWithOnlyGet());		
	}
	
	@Test
	public void testMethodReadWithoutField() throws URISyntaxException, FileNotFoundException, RequiredFieldPoiParserException, IOException, InstantiationException, IllegalAccessException, ReadPoiParserException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(AnnotatedPoiFileParserTest.class.getResource(filePath+fileName).toURI());
		List<TestRow> testRowClasses = annotationExcelParser.readExcelFile(excelFile, "Sheet2", TestRow.class);
		Assert.assertEquals(new String("2013"), testRowClasses.get(0).getMethodField());
		Assert.assertNull(testRowClasses.get(0).getFieldWithReadIngnore());		
		Assert.assertNull(testRowClasses.get(0).getFieldWithOnlyGet());		
	}
	
	private LinkedHashMap<String, List<TestRow>> getTestRowFixture(){
		LinkedHashMap<String, List<TestRow>> excelDataMap = new LinkedHashMap<String, List<TestRow>>();
		
		excelDataMap.put(writeSheet1, getTestRowClassListFixture(writeSheet1));		
		excelDataMap.put(writeSheet2, getTestRowClassListFixture(writeSheet2));		
		
		return excelDataMap;
	}
	
	private List<TestRow> getTestRowClassListFixture(String sheet){
		List<TestRow> dataRows = new ArrayList<TestRow>();
		for(int i = 0;i<numberOfCreatedObjects;i++){
			dataRows.add(getTestRowClassFixture(new Long(i)
				, sheet+"-name-"+i
				, sheet+"-field-"+i
				, sheet+"-writeIgnore-"+i
				, sheet+"-fieldWithOnlySet-"+i
				, sheet+"-secondName-"+i));
		}
		return dataRows;
	}
	
	private TestRow getTestRowClassFixture(Long id, String name,String methodField,String fieldWithWriteIngnore,String fieldWithOnlySet,String secondName){
		TestRow testRowClass = new TestRow();
		testRowClass.setId(id);
		testRowClass.setName(name);
		testRowClass.setMethodField(methodField);
		testRowClass.setFieldWithWriteIngnore(fieldWithWriteIngnore);
		testRowClass.setFieldWithOnlySet(fieldWithOnlySet);
		testRowClass.setSecondName(secondName);
		return testRowClass;
	}
}
