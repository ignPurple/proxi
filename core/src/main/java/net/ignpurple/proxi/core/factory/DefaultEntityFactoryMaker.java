package net.ignpurple.proxi.core.factory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.api.factory.EntityFactoryMaker;
import net.ignpurple.proxi.api.visitor.ClassVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DefaultEntityFactoryMaker<T extends Entity> implements EntityFactoryMaker<T> {
    private final Class<T> entityClass;
    private DynamicType.Builder<T> entityBuilder;

    private final List<ClassVisitor<T>> visitors;

    public DefaultEntityFactoryMaker(Class<T> entityClass, List<ClassVisitor<T>> visitors) {
        this.entityClass = entityClass;
        this.entityBuilder = this.createEntityBuilder();
        this.visitors = visitors;
    }

    @Override
    public EntityFactory<T> make() {
        for (final ClassVisitor<T> visitor : this.visitors) {
            this.entityBuilder = visitor.visit(this.entityBuilder);
        }

        final Class<? extends T> proxiedClass = this.entityBuilder.make()
            .load(this.getClass().getClassLoader())
            .getLoaded();

        final Constructor<T> constructor = this.findConstructor(proxiedClass);

        return new DefaultEntityFactory<>(
            proxiedClass,
            Arrays.stream(this.entityClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList()),
            constructor
        );
    }

    @Override
    public Constructor<T> findConstructor(Class<? extends T> proxiedClass) {
        Constructor<?> found = null;
        for (final Constructor<?> constructor : proxiedClass.getDeclaredConstructors()) {
            if (constructor.getParameterCount() != 0) {
                continue;
            }

            found = constructor;
            break;
        }

        if (found == null) {
            throw new IllegalStateException("Cannot find empty constructor for class " + this.entityClass.getSimpleName());
        }

        found.setAccessible(true);
        return (Constructor<T>) found;
    }

    private DynamicType.Builder<T> createEntityBuilder() {
        return new ByteBuddy()
            .subclass(this.entityClass);
    }
}
