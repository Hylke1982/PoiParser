package nl.bstoi.poiparser.core.strategy.converter;

import java.text.NumberFormat;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;

public class StringConverter implements Converter<String>{

	public String readCell(Cell cell) {
		
		if(null!=cell && cell.getCellType() == Cell.CELL_TYPE_STRING){				
			return cell.getRichStringCellValue().getString();
		}else if(null!=cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))){
			return cell.getDateCellValue().toString();
		}else if(null!=cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && !DateUtil.isCellDateFormatted(cell))){
			// Format the decimal and disable the number grouping
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setGroupingUsed(false);
			return numberFormat.format(cell.getNumericCellValue());
		}
		return null;
	}
	
	public void writeCell(Cell cell, String value) {
		CreationHelper creationHelper = cell.getSheet().getWorkbook().getCreationHelper();
		cell.setCellValue(creationHelper.createRichTextString(value));		
	}
}
