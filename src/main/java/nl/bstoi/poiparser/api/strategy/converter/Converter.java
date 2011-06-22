package nl.bstoi.poiparser.api.strategy.converter;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Convert a Cell from/to a field.
 * 
 * @author Hylke Stapersma
 *
 * @param <T>
 */
public interface Converter<T> {

	/**
	 * Read a Cell and convert to <T>
	 * @param cell
	 * @return
	 */
	public T readCell(Cell cell);
	
	/**
	 * Write a <T> value to cell
	 * @param cell
	 * @param value
	 */
	public void writeCell(Cell cell, T value);
	
}
