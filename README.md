# openCsv

Now except standard csv parsing line by line you can parse csv to object:

```java
public class TestClassValue {
    @CsvField(columnName = "Col1")
    private String column1;
    @CsvField(columnName = "Col2")
    private String column2;
    @CsvField(columnName = "Col3")
    private String column3;
    
...
    getters and
    setters

```

then you can use like this:

```java
    CSVReader reader=DEFAULT.reader(new StringReader(text));
        Collection<TestClassValue> read=DEFAULT.read(reader,TestClassValue.class);
```
