package au.com.bytecode.opencsv;

import au.com.bytecode.opencsv.annotation.CsvField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationProcessor<T> implements CSVReadProc {
    private final Class<T> clazz;
    private Collection result = new LinkedList<T>();
    private Collection<Field> fields;
    private List<String> columnNames;

    public AnnotationProcessor(Class<T> clazz) {
        this.clazz = clazz;

    }

    public void procRow(int rowIndex, String... values) {
        int index = 0;
        Constructor constructor;
        T o = null;
        try {
            constructor = clazz.getConstructor();
            o = (T) constructor.newInstance();
            if (rowIndex == 0) {
                columnNames = new ArrayList<>(List.of(values));
                return;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CSVRuntimeException(e.getMessage(), e);
        }
        for (Field field : Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(CsvField.class)).collect(Collectors.toList())) {
            field.setAccessible(true);
            try {
                if (field.getAnnotation(CsvField.class).columnName().equals(columnNames.get(index)))
                    switch (field.getAnnotation(CsvField.class).type()) {
                        case FLOAT:
                            field.setFloat(o, Float.valueOf(values[index]));
                            break;
                        case BOOLEAN:
                            field.setBoolean(o, Boolean.valueOf(values[index]));
                            break;
                        case STRING:
                            field.set(o, values[index]);
                            break;
                        case DECIMAL:
                            field.set(o, new BigDecimal(values[index]));
                            break;
                        case NUMERIC:
                            field.set(o, Long.valueOf(values[index]));
                            break;
                    }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            index++;
        }
        result.add(o);
    }

    public <T> Collection<T> getResult() {
        return this.result;
    }
}
