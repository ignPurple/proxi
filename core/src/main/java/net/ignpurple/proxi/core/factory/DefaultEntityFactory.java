package net.ignpurple.proxi.core.factory;

import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.core.entity.metadata.EntityMetadata;

import java.lang.reflect.Constructor;
import java.util.List;

public class DefaultEntityFactory<T extends Entity> implements EntityFactory<T> {
    private final Class<? extends T> proxiedClass;
    private final List<String> fields;
    private final Constructor<T> constructor;

    public DefaultEntityFactory(Class<? extends T> proxiedClass, List<String> fields, Constructor<T> constructor) {
        this.proxiedClass = proxiedClass;
        this.fields = fields;
        this.constructor = constructor;
    }

    @Override
    public T createEntity() {
        try {
            final T entity = this.constructor.newInstance();
            Proxi.getInstance().getMetadataStorage().register(entity, EntityMetadata.createMetadata(this.fields));
            return entity;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
