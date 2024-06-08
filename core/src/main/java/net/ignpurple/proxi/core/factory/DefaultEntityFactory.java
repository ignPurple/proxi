package net.ignpurple.proxi.core.factory;

import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.exception.EntityCreationException;
import net.ignpurple.proxi.api.exception.NoConstructorFoundException;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.core.entity.metadata.EntityMetadata;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public class DefaultEntityFactory<T extends Entity> implements EntityFactory<T> {
    private final Class<? extends T> originalClass;
    private final List<String> fields;
    private final MethodHandle constructor;

    public DefaultEntityFactory(Class<? extends T> originalClass, List<String> fields, MethodHandle constructor) {
        this.originalClass = originalClass;
        this.fields = fields;
        this.constructor = constructor;
    }

    @Override
    public T createEntity() {
        if (this.constructor == null) {
            new NoConstructorFoundException(this.originalClass, null).printStackTrace();
            return null;
        }

        try {
            final T entity = (T) this.constructor.invoke();
            Proxi.getInstance().getMetadataStorage().register(entity, EntityMetadata.createMetadata(this.fields));
            return entity;
        } catch (Throwable exception) {
            new EntityCreationException(this.originalClass, exception).printStackTrace();
        }

        return null;
    }
}
