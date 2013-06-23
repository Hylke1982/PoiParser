package nl.bstoi.poiparser.core.strategy;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@codecentric.nl]
 * Date: 23-06-13
 * Time: 12:48
 */
public class CellDescriptor {

    private final String fieldName;
    private final int columnNumber;
    private final Class type;
    private boolean required, readIgnore, writeIgnore, embedded;
    private String regex;

    public CellDescriptor(String fieldName, int columnNumber, Class type) {
        this.fieldName = fieldName;
        this.columnNumber = columnNumber;
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public Class getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadIgnore() {
        return readIgnore;
    }

    public void setReadIgnore(boolean readIgnore) {
        this.readIgnore = readIgnore;
    }

    public boolean isWriteIgnore() {
        return writeIgnore;
    }

    public void setWriteIgnore(boolean writeIgnore) {
        this.writeIgnore = writeIgnore;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellDescriptor that = (CellDescriptor) o;

        if (columnNumber != that.columnNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return columnNumber;
    }
}
