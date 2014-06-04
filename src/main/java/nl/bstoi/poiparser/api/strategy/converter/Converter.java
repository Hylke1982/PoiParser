package nl.bstoi.poiparser.api.strategy.converter;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Convert a Cell from/to a field.
 * 
 * @author Hylke Stapersma
 *
 * @param <T> Type to convert into or convert from
 */
public interface Converter<T> {

	/**
	 * Read a Cell and convert to [T]
	 * @param cell to read
	 * @param regex regex validation
	 * @return a converted object [T] from a cell
	 */
	public T readCell(Cell cell, String regex);

    /**
     * Read a Cell and convert to [T]
     * @param cell to read
     * @return a converted object [T] from a cell
     */
	public T readCell(Cell cell);
	
	/**
	 * Write a [T] value to cell
	 * @param cell to write
	 * @param value value to convert to cell
	 */
	public void writeCell(Cell cell, T value);
	
}
