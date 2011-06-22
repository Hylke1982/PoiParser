package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;

import org.apache.poi.ss.usermodel.Cell;

public class BooleanConverter implements Converter<Boolean>{
	
	public final static String[] trueStringValue = {"yes","ja","true","1"};
	public final static int[] trueIntegerValue = {1};
	
	public Boolean readCell(Cell cell) {
		if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
			return cell.getBooleanCellValue();
		}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			Double cellValue = cell.getNumericCellValue();
			boolean booleanValue = false;
			for(int trueValue : trueIntegerValue){
				if(trueValue == cellValue.intValue()){
					booleanValue = true;
					break;
				}					
			}
			return booleanValue;
		}else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			boolean booleanValue = false;
			for(String trueValue : trueStringValue){
				if(trueValue.equalsIgnoreCase(cell.getRichStringCellValue().getString().trim())){
					booleanValue = true;
					break;
				}					
			}
			return booleanValue;
		}
		return false;
	}
	
	public void writeCell(Cell cell, Boolean value) {
		if(null!=value) cell.setCellValue(value);
		
	}

}
