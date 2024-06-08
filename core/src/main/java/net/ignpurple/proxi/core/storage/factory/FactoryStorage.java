package net.ignpurple.proxi.core.storage.factory;

import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.api.visitor.ClassVisitor;
import net.ignpurple.proxi.api.storage.Storage;
import net.ignpurple.proxi.core.factory.DefaultEntityFactoryMaker;
import net.ignpurple.proxi.core.visitor.MetadataVisitor;

import java.util.List;

@SuppressWarnings("unchecked")
public class FactoryStorage extends Storage<Class<? extends Entity>, EntityFactory<? extends Entity>> {

    public <T extends Entity> EntityFactory<T> makeFactory(Class<T> entityClass) {
        return this.makeFactory(entityClass, List.of(new MetadataVisitor<>()));
    }

    public <T extends Entity> EntityFactory<T> makeFactory(Class<T> entityClass, List<ClassVisitor<T>> visitors) {
        final DefaultEntityFactoryMaker<T> factoryMaker = new DefaultEntityFactoryMaker<>(entityClass, visitors);
        return factoryMaker.make();
    }

    public <T extends Entity, F extends EntityFactory<T>> F create(Class<T> entityClass) {
        return (F) this.storage.computeIfAbsent(entityClass, ($) -> this.makeFactory(entityClass));
    }
}
