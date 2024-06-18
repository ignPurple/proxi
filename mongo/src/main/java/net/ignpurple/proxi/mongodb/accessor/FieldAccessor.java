package net.ignpurple.proxi.mongodb.accessor;

import org.bson.codecs.pojo.PropertyAccessor;

import java.lang.reflect.Field;

public class FieldAccessor implements PropertyAccessor<Object> {
    private final Field field;

    public FieldAccessor(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    public Object get(Object instance) {
        try {
            return instance != null ? this.field.get(instance) : null;
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        try {
            this.field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
