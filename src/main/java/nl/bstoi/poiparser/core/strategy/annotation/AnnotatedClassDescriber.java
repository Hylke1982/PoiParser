package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;
import nl.bstoi.poiparser.api.strategy.annotations.Embedded;
import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class AnnotatedClassDescriber {

    private final static String[] DEFAULT_IGNORED_PROPERTYNAMES = {"class"};
    private String[] ignorePropertyName = DEFAULT_IGNORED_PROPERTYNAMES;


    private static AnnotatedClassDescriber instance;

    private AnnotatedClassDescriber() {

    }

    public static AnnotatedClassDescriber getInstance() {
        if (null == instance) instance = new AnnotatedClassDescriber();
        return instance;

    }

    public Set<CellDescriptor> getCellDescriptorsForClass(final Class clazz) {
        return getCellDescriptorsForClass(null, clazz);
    }


    private Set<CellDescriptor> getCellDescriptorsForClass(final String propertyName, final Class clazz) {
        Set<CellDescriptor> cellDescriptors = new HashSet<CellDescriptor>();
        if (clazz.getSuperclass() != Object.class) {
            // Check superclasses first
            addEmbeddedCellDescriptors(cellDescriptors, getCellDescriptorsForClass(propertyName, clazz.getSuperclass()));
        }
        for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(clazz)) {

            if (isNotIgnoredProperty(propertyDescriptor)) {
                Cell cell = null;
                Class type = null;
                try {

                    Field field = clazz.getDeclaredField(propertyDescriptor.getDisplayName());
                    cell = field.getAnnotation(Cell.class);


                    if (isCorrectEmbeddedField(field) && hasNoTypeRecursion(clazz, field.getType())) {
                        // Call method recursive when correct embedded type
                        addEmbeddedCellDescriptors(cellDescriptors, getCellDescriptorsForClass(field.getName(), field.getType()));
                    }


                    if (cell == null) {
                        // When field has no annotation then check method
                        Method method = propertyDescriptor.getReadMethod();
                        if (null != method && method.getDeclaringClass().equals(clazz)) {
                            cell = method.getAnnotation(Cell.class);
                            type = method.getReturnType();
                        }
                    } else {
                        type = field.getType();
                    }

                } catch (SecurityException e) {
                    throw e; // Rethrow security exception
                } catch (NoSuchFieldException e) {
                    // If field does not exist try to get value from getter(method)
                    Method method = propertyDescriptor.getReadMethod();
                    if (null != method && method.getDeclaringClass().equals(clazz)) {
                        cell = method.getAnnotation(Cell.class);
                        type = method.getReturnType();
                    }
                }
                if (null != cell) {
                    addCellDescriptor(cellDescriptors, createCellDescriptor(getPropertyName(propertyName, propertyDescriptor.getDisplayName()), cell, type));
                }
            }
        }
        return cellDescriptors;
    }

    private void addEmbeddedCellDescriptors(Set<CellDescriptor> cellDescriptors, Set<CellDescriptor> embeddedCellDescriptors) {
        for (CellDescriptor embeddedCellDescriptor : embeddedCellDescriptors) {
            addCellDescriptor(cellDescriptors, embeddedCellDescriptor);
        }
    }

    private void addCellDescriptor(Set<CellDescriptor> cellDescriptors, CellDescriptor embeddedCellDescriptor) {
        if (!cellDescriptors.contains(embeddedCellDescriptor)) {
            cellDescriptors.add(embeddedCellDescriptor);
        } else {
            throw new PoiParserRuntimeException(String.format("Duplicate column definition found column %s is defined more than once", embeddedCellDescriptor.getColumnNumber()));
        }
    }

    private CellDescriptor createCellDescriptor(final String propertyName, final Cell cell, final Class type) {
        CellDescriptor cellDescriptor = new CellDescriptor(propertyName, cell.columnNumber(), type);
        cellDescriptor.setRequired(cell.required());
        cellDescriptor.setReadIgnore(cell.readIgnore());
        cellDescriptor.setWriteIgnore(cell.writeIgnore());
        cellDescriptor.setRegex(cell.regex());
        return cellDescriptor;
    }

    private String getPropertyName(final String propertyName, final String fieldName) {
        if (StringUtils.isEmpty(propertyName)) {
            return fieldName;
        }
        return propertyName + "." + fieldName;
    }

    private boolean isNotIgnoredProperty(final PropertyDescriptor propertyDescriptor) {
        return !ArrayUtils.contains(ignorePropertyName, propertyDescriptor.getDisplayName());
    }

    private boolean isEmbeddedField(final Field field) {
        if (null != field) {
            return (null != field.getAnnotation(Embedded.class));
        }
        return false;
    }

    private boolean hasCellAnnotationOnField(final Field field) {
        if (null != field) {
            return (null != field.getAnnotation(Cell.class));
        }
        return false;
    }

    private boolean isCorrectEmbeddedField(final Field field) {
        if (isEmbeddedField(field)) {
            if (!hasCellAnnotationOnField(field)) {
                return true;
            } else {
                throw new PoiParserRuntimeException("A field cannot be annotated with @Cell and @Embedded");
            }
        }
        return false;

    }

    private boolean hasNoTypeRecursion(final Class declaringClass, final Class fieldType) {
        if (!declaringClass.equals(fieldType)) {
            return true;
        }
        throw new PoiParserRuntimeException("Declaring class cannot be the same as the field type (recursion is not supported)");
    }
}
