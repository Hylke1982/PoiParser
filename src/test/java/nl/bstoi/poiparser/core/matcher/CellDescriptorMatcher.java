package nl.bstoi.poiparser.core.matcher;

import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 16:30
 */
public class CellDescriptorMatcher extends BaseMatcher<CellDescriptor> {

    private final CellDescriptor cellDescriptor;

    public CellDescriptorMatcher(CellDescriptor cellDescriptor) {
        this.cellDescriptor = cellDescriptor;
    }

    public boolean matches(Object arg) {
        if (arg instanceof CellDescriptor) {
            CellDescriptor other = (CellDescriptor) arg;
            boolean matches = cellDescriptor.getFieldName().equals(other.getFieldName());
            matches &= cellDescriptor.getColumnNumber() == other.getColumnNumber();
            matches &= cellDescriptor.getType().equals(other.getType());
            matches &= cellDescriptor.isEmbedded() == other.isEmbedded();
            matches &= cellDescriptor.isReadIgnore() == other.isReadIgnore();
            matches &= cellDescriptor.isRequired() == other.isRequired();
            matches &= cellDescriptor.isWriteIgnore() == other.isWriteIgnore();
            if (StringUtils.isNotEmpty(cellDescriptor.getRegex())){
                matches &= cellDescriptor.getRegex().equals(other.getRegex());
            }
            return matches;
        }
        return false;
    }

    public void describeTo(Description description) {
    }
}
