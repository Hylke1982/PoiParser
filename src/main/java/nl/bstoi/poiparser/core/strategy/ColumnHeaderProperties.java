package nl.bstoi.poiparser.core.strategy;

import java.util.Properties;

/**
 * Hylke Stapersma (codecentric nl)
 * hylke.stapersma@codecentric.nl
 */
public class ColumnHeaderProperties {

    private final Properties properties;

    public ColumnHeaderProperties(final Properties properties) {
        if (null == properties) throw new IllegalArgumentException("Properties cannot be null.");
        this.properties = properties;
    }

    public final static String DOT = ".";

    public void addColumnHeader(final String sheetName, final String propertyName, String value) {
        properties.setProperty(getColumnHeaderKey(sheetName, propertyName), value);
    }

    private String getColumnHeaderKey(final String sheetName, final String propertyName) {
        return sheetName + DOT + propertyName;
    }

    public String getColumnHeader(final String sheetName, final String propertyName) {
        return properties.getProperty(getColumnHeaderKey(sheetName, propertyName));
    }

    public boolean containsColumnHeader(final String sheetName, final String propertyName) {
        return properties.containsKey(getColumnHeaderKey(sheetName, propertyName));
    }

}
