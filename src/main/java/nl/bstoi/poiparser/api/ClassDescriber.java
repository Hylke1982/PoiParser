package nl.bstoi.poiparser.api;

import nl.bstoi.poiparser.core.strategy.CellDescriptor;

import java.util.Set;

/**
 * Created by hylke on 01/05/15.
 */
public interface ClassDescriber {
    Set<CellDescriptor> getCellDescriptorsForClass(Class clazz);
}
