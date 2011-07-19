package nl.bstoi.poiparser.core.strategy.annotation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;

public class TypeTestRow {
	
	@Cell(columnNumber=0)
	private Short shortValue;
	
	@Cell(columnNumber=1)
	private Integer integerValue;
	
	@Cell(columnNumber=2)
	private Long longValue;
	
	@Cell(columnNumber=3)
	private BigDecimal bigDecimalValue;
	
	@Cell(columnNumber=4)
	private String stringValue;
	
	@Cell(columnNumber=5)
	private Date dateValue;
	
	@Cell(columnNumber=6)
	private Boolean booleanValue;
	
	@Cell(columnNumber=7)
	private Calendar calendarValue;

	public Short getShortValue() {
		return shortValue;
	}

	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Calendar getCalendarValue() {
		return calendarValue;
	}

	public void setCalendarValue(Calendar calendarValue) {
		this.calendarValue = calendarValue;
	}

}
