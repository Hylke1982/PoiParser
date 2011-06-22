package nl.bstoi.poiparser.core.strategy.converter;

import java.math.BigDecimal;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class BigDecimalConvertor implements Converter<BigDecimal>{
	
	@Override
	public BigDecimal readCell(Cell cell) {
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
		if(null!=cellValue) return new BigDecimal(cellValue);
		return null;
	}
	
	
	@Override
	public void writeCell(Cell cell, BigDecimal value) {
		if(null!=value) cell.setCellValue(value.doubleValue());
	}

}
