package net.ignpurple.proxi.core.storage.factory;

import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.api.factory.ProxyWrapperFactory;
import net.ignpurple.proxi.api.visitor.ClassVisitor;
import net.ignpurple.proxi.api.storage.Storage;
import net.ignpurple.proxi.core.factory.EntityProxyFactory;
import net.ignpurple.proxi.core.visitor.MetadataVisitor;

import java.util.List;

@SuppressWarnings("unchecked")
public class FactoryStorage extends Storage<Class<? extends Entity>, ProxyWrapper<? extends Entity>> {

    public <T extends Entity> ProxyWrapper<T> makeFactory(Class<T> entityClass) {
        return this.makeFactory(entityClass, List.of(new MetadataVisitor<>()));
    }

    public <T extends Entity> ProxyWrapper<T> makeFactory(Class<T> entityClass, List<ClassVisitor<T>> visitors) {
        final ProxyWrapperFactory<T> factory = new EntityProxyFactory<>(entityClass, visitors);
        return factory.make();
    }

    public <T extends Entity, F extends ProxyWrapper<T>> F create(Class<T> entityClass) {
        return (F) this.storage.computeIfAbsent(entityClass, ($) -> this.makeFactory(entityClass));
    }
}
