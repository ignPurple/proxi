package net.ignpurple.proxi.core.factory;

import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.exception.EntityCreationException;
import net.ignpurple.proxi.api.exception.NoConstructorFoundException;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.core.entity.metadata.EntityMetadata;

import java.lang.invoke.MethodHandle;
import java.util.List;

@SuppressWarnings("unchecked")
public class EntityProxy<T extends Entity> implements ProxyWrapper<T> {
    private Class<? extends T> originalClass;
    private Class<? extends T> proxiedClass;
    private MethodHandle constructor;
    private final List<String> fields;

    public EntityProxy(List<String> fields) {
        this.fields = fields;
    }

    @Override
    public T createEntity() {
        if (this.constructor == null) {
            throw new NoConstructorFoundException(this.originalClass, null);
        }

        try {
            final T entity = (T) this.constructor.invoke();
            Proxi.getInstance().getMetadataStorage().register(entity, EntityMetadata.createMetadata(this.fields));
            return entity;
        } catch (Throwable exception) {
            throw new EntityCreationException(this.originalClass, exception);
        }
    }

    @Override
    public void setOriginalClass(Class<? extends T> originalClass) {
        this.originalClass = originalClass;
    }

    @Override
    public void setProxiedClass(Class<? extends T> proxiedClass) {
        this.proxiedClass = proxiedClass;
    }

    @Override
    public void setConstructor(MethodHandle constructor) {
        this.constructor = constructor;
    }
}
