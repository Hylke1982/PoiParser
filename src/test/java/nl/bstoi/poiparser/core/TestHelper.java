package nl.bstoi.poiparser.core;

import nl.bstoi.poiparser.core.matcher.CellDescriptorMatcher;
import nl.bstoi.poiparser.core.matcher.EmbeddedTestRowMatcher;
import nl.bstoi.poiparser.core.matcher.TestRowMatcher;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;

import java.math.BigDecimal;

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

    public static TestRowMatcher createTestRowMatcher(Long id, String name, String description, String methodField, String fieldWithOnlyGet, String fieldWithOnlySet, String fieldWithReadIngnore, String fieldWithWriteIngnore, String secondName) {
        return new TestRowMatcher(id, name, description, methodField, fieldWithOnlyGet, fieldWithOnlySet, fieldWithReadIngnore, fieldWithWriteIngnore, secondName);
    }

    public static EmbeddedTestRowMatcher createEmbeddedTestRowMatcher(Short field1, Integer field2, Long field3, BigDecimal field4) {
        return new EmbeddedTestRowMatcher(field1, field2, field3, field4);
    }

    public static boolean objectEqual(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if (o1 != null) {
            return o1.equals(o2);
        }
        return false;
    }
}
