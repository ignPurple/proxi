package net.ignpurple.proxi.mongodb.model;

import net.ignpurple.proxi.database.model.property.PropertyModel;
import net.ignpurple.proxi.mongodb.accessor.FieldAccessor;
import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyAccessor;

import java.lang.reflect.Field;

public class FieldModel implements PropertyModel {
    private final Field field;
    private final PropertyAccessor<Object> accessor;
    private final Codec<?> codec;
    private final String mappedName;

    public FieldModel(Field field, Codec<?> codec, String mappedName) {
        this.field = field;
        this.accessor = new FieldAccessor(field);
        this.codec = codec;
        this.mappedName = mappedName;
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public Codec<?> getCodec() {
        return this.codec;
    }

    @Override
    public PropertyAccessor<Object> getAccessor() {
        return this.accessor;
    }

    @Override
    public String getMappedName() {
        return this.mappedName;
    }
}
