package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class LongConverter implements Converter<Long> {
	
	@Override
	public Long readCell(Cell cell) {
		Double cellValue = null;
		if(null!=cell){
			try{
				// First try to read as a numeric
				cellValue = cell.getNumericCellValue();
			}catch(IllegalStateException isex){
				// Other wise do string conversion
				cellValue = Double.parseDouble(cell.getRichStringCellValue().getString().trim());
			}
		}
		if(null!=cellValue) return cellValue.longValue();
		return null;
	}
	
	@Override
	public void writeCell(Cell cell, Long value) {
		if(null!=value) cell.setCellValue(value);
	}

}
