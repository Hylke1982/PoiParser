package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class ShortConverter implements Converter<Short> {
	
	@Override
	public Short readCell(Cell cell) {
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
		if(null!=cellValue) return cellValue.shortValue();
		return null;
	}
	
	@Override
	public void writeCell(Cell cell, Short value) {
		if(null!=value)cell.setCellValue(value.doubleValue());		
	}

}
