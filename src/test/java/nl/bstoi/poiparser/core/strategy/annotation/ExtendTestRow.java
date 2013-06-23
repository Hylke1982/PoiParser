package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;

public class ExtendTestRow extends TestRow{
	
	@Cell(columnNumber=27)
	private Short year;

	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}
	

}
