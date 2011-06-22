package nl.bstoi.poiparser.core.util;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import nl.bstoi.poiparser.core.util.PoiImportExportUtil;

import org.junit.Test;

public class PoiImportExportUtilTest {
	
	private static final String filePath = "/excel/";
	
	@Test
	public void testReadExcelFiles() throws URISyntaxException{
		final File excelFile = new File(PoiImportExportUtil.class.getResource(filePath).toURI());
		List<String> fileList = PoiImportExportUtil.readExcelFiles(excelFile.getAbsolutePath());
		assertTrue(fileList.contains(new String("test-excel-001.xls")));
		assertTrue(fileList.contains(new String("test-excel-001.xlsx")));
	}
	
	@Test
	public void testGetFileHash() throws URISyntaxException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(PoiImportExportUtil.class.getResource(filePath+fileName).toURI());
		String fileHash = PoiImportExportUtil.getFileHash(excelFile);
		assertNotNull(fileHash);
	}
	
	@Test
	public void testGetSheetNames() throws URISyntaxException{
		final String fileName = "test-excel-001.xls";
		final File excelFile = new File(PoiImportExportUtilTest.class.getResource(filePath+fileName).toURI());
		List<String> sheetNames = PoiImportExportUtil.getSheetNames(excelFile);
		
		assertEquals("Sheet1", sheetNames.get(0));
		assertEquals("Sheet2", sheetNames.get(1));
		assertEquals("Sheet3", sheetNames.get(2));
		
	}
	
	
}