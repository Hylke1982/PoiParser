package nl.bstoi.poiparser.core.matcher;

import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.strategy.annotation.structures.TestRow;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Hylke Stapersma (codecentric nl)
 * hylke.stapersma@codecentric.nl
 */
public class TestRowMatcher extends BaseMatcher<TestRow> {

    private final Long id;
    private final String name;
    private final String description;
    private final String methodField;
    private final String fieldWithOnlyGet;
    private final String fieldWithOnlySet;
    private final String fieldWithReadIngnore;
    private final String fieldWithWriteIngnore;
    private final String secondName;

    public TestRowMatcher(Long id, String name, String description, String methodField, String fieldWithOnlyGet, String fieldWithOnlySet, String fieldWithReadIngnore, String fieldWithWriteIngnore, String secondName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.methodField = methodField;
        this.fieldWithOnlyGet = fieldWithOnlyGet;
        this.fieldWithOnlySet = fieldWithOnlySet;
        this.fieldWithReadIngnore = fieldWithReadIngnore;
        this.fieldWithWriteIngnore = fieldWithWriteIngnore;
        this.secondName = secondName;
    }

    public boolean matches(Object o) {
        if (o instanceof TestRow) {
            final TestRow testRow = (TestRow) o;
            boolean matches = TestHelper.objectEqual(testRow.getId(), id);
            matches &= TestHelper.objectEqual(testRow.getName(), name);
            matches &= TestHelper.objectEqual(testRow.getMethodField(), methodField);
            matches &= TestHelper.objectEqual(testRow.getFieldWithOnlyGet(), fieldWithOnlyGet);
            matches &= TestHelper.objectEqual(testRow.fieldWithOnlySet, fieldWithOnlySet);
            matches &= TestHelper.objectEqual(testRow.getFieldWithReadIngnore(), fieldWithReadIngnore);
            matches &= TestHelper.objectEqual(testRow.getFieldWithWriteIngnore(), fieldWithWriteIngnore);
            matches &= TestHelper.objectEqual(testRow.getSecondName(), secondName);
            return matches;

        }
        return false;
    }

    public void describeTo(Description description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
