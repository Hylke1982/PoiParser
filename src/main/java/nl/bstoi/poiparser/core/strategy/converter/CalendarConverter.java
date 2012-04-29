package nl.bstoi.poiparser.core.strategy.converter;

import java.util.Calendar;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class CalendarConverter implements Converter<Calendar> {
	
	public Calendar readCell(Cell cell) {
		if(null!=cell && (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell))){
			Calendar cal = Calendar.getInstance();
			cal.setTime(cell.getDateCellValue());
		}
		return null;
	}
	
	public Calendar readCell(Cell cell, String regex) {
		return readCell(cell);
	}
	
	public void writeCell(Cell cell, Calendar value) {
		if(null!=value) cell.setCellValue(value);	
	}

}
