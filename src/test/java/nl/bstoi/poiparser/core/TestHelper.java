package nl.bstoi.poiparser.core;

import nl.bstoi.poiparser.core.matcher.CellDescriptorMatcher;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 16:54
 */
public class TestHelper {

    public static CellDescriptorMatcher createCellDescriptorMatcher(String fieldName, int columnNumber, Class type, boolean required, boolean readIgnore, boolean writeIgnore, boolean embedded, String regex) {
        CellDescriptor cellDescriptor = createCellDescriptor(fieldName, columnNumber, type, required, readIgnore, writeIgnore, embedded, regex);
        return new CellDescriptorMatcher(cellDescriptor);
    }

    public static CellDescriptor createCellDescriptor(String fieldName, int columnNumber, Class type, boolean required, boolean readIgnore, boolean writeIgnore, boolean embedded, String regex) {
        CellDescriptor cellDescriptor = new CellDescriptor(fieldName, columnNumber, type);
        cellDescriptor.setRequired(required);
        cellDescriptor.setReadIgnore(readIgnore);
        cellDescriptor.setWriteIgnore(writeIgnore);
        cellDescriptor.setEmbedded(embedded);
        cellDescriptor.setRegex(regex);
        return cellDescriptor;
    }
}
