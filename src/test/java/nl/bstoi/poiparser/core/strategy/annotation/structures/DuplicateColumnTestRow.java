package nl.bstoi.poiparser.core.strategy.annotation.structures;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;

public class DuplicateColumnTestRow {
	
	@Cell(columnNumber=0)
	private String id;
	
	@Cell(columnNumber=1)
	private String name;
	
	@Cell(columnNumber=1)
	private String name2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
	
	

}
