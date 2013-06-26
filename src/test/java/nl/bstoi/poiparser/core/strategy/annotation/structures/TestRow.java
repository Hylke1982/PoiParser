package nl.bstoi.poiparser.core.strategy.annotation.structures;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;

public class TestRow {
	
	@Cell(columnNumber=0,required=true)
	private Long id;
	
	@Cell(columnNumber=1)
	private String name;
	
	private String description;
	
	private String methodField;
	
	@Cell(columnNumber=7)
	private String fieldWithOnlyGet;
	
	@Cell(columnNumber=8)
	public String fieldWithOnlySet;
	
	@Cell(columnNumber=9,readIgnore=true)
	private String fieldWithReadIngnore;
	
	@Cell(columnNumber=10,writeIgnore=true)
	private String fieldWithWriteIngnore;
	
	@Cell(columnNumber=11)
	private String secondName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Cell(columnNumber=2)
	public String getMethodField() {
		return methodField;
	}

	public void setMethodField(String methodField) {
		this.methodField = methodField;
	}
	
	@Cell(columnNumber=4)
	public String getMethodField2() {
		return name+methodField;
	}
	
	public String getFieldWithOnlyGet() {
		return fieldWithOnlyGet;
	}
	
	public void setFieldWithOnlySet(String fieldWithOnlySet) {
		this.fieldWithOnlySet = fieldWithOnlySet;
	}

	public String getFieldWithReadIngnore() {
		return fieldWithReadIngnore;
	}

	public void setFieldWithReadIngnore(String fieldWithReadIngnore) {
		this.fieldWithReadIngnore = fieldWithReadIngnore;
	}

	public String getFieldWithWriteIngnore() {
		return fieldWithWriteIngnore;
	}

	public void setFieldWithWriteIngnore(String fieldWithWriteIngnore) {
		this.fieldWithWriteIngnore = fieldWithWriteIngnore;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

}
