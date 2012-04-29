package nl.bstoi.poiparser.core.strategy.converter;

import java.util.Date;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class DateConverter implements Converter<Date>{
	
	public Date readCell(Cell cell) {
		if(null!=cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))){
			return cell.getDateCellValue();
		}
		return null;
	}
	
	public Date readCell(Cell cell, String regex) {
		return readCell(cell);
	}
	
	public void writeCell(Cell cell, Date value) {
		if(null!=value) cell.setCellValue(value);	
	}

}
