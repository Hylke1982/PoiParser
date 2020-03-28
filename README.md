PoiParser
=========

PoiParser is a library that eases the use of [Apache Poi](http://poi.apache.org/), it allows you to read and write
two-dimensional datasets to excel using annotations.

Changes
-------

The following changes have been made.

* PoiParserReaders and PoiParserWriters are now created by a factory
* Classes can now be embedded in other classes
* Only input and outputstream can now be used to read or write a excel
* Headers can now be set by property files
* TypedLists must now be used for writing a excel

Requirements
------------

- jdk 8+

Maven
-----

To include PoiParser in your project using maven.

```
<dependency>
    <groupId>nl.bstoi.poiparser</groupId>
    <artifactId>PoiParser</artifactId>
    <version>4.1.2</version>
</dependency>
```


Reading a excel file
--------------------

Reading a excel file

```
final File excelFile = new File("somefile.xls");
AnnotatedReadPoiParserFactory<TestRow> testRowAnnotatedPoiParserFactory = new AnnotatedReadPoiParserFactory<TestRow>(TestRow.class);
ReadPoiParser<TestRow> testRowAnnotatedPoiParser = testRowAnnotatedPoiParserFactory.createReadPoiParser(new FileInputStream(excelFile), "Sheet2");
testRowAnnotatedPoiParser.read();

public class EmbeddedTestRow {

    @Cell(columnNumber = 0)
    private Short field1;

    @Cell(columnNumber = 1)
    private Integer field2;

    @Embedded
    private EmbeddableTestRow embeddableTestRow;
}

```

Writing a excel file
--------------------

Writing a excel file

```

// Init writer
annotatedWritePoiParser = new AnnotatedWritePoiParser(new FileOutputStream(tempOutputFile), workbook);
annotatedWritePoiParser.write(data);
final Map<String, TypedList<?>> data = new HashMap<String, TypedList<?>>();
final TypedList<TestRow> testRows = new TypedArrayList<TestRow>(TestRow.class);
data.put(SHEET_ABC, testRows);

```


TODO
----

The following things could be improved.

- A cell to support a specific read and/or type converter
- Better support for formulas

