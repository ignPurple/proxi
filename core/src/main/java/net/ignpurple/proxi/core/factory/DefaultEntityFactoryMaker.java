package net.ignpurple.proxi.core.factory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.exception.NoConstructorFoundException;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.api.factory.EntityFactoryMaker;
import net.ignpurple.proxi.api.visitor.ClassVisitor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
            this.entityBuilder = visitor.visitFields(this.entityBuilder);
            this.entityBuilder = visitor.visitMethods(this.entityBuilder);
        }

        final Class<? extends T> proxiedClass = this.entityBuilder.make()
            .load(this.getClass().getClassLoader())
            .getLoaded();

        final MethodHandle constructor = this.findConstructor(proxiedClass);
        return new DefaultEntityFactory<>(
            this.entityClass,
            Arrays.stream(this.entityClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList()),
            constructor
        );
    }

    @Override
    public MethodHandle findConstructor(Class<? extends T> proxiedClass) {
        try {
            return MethodHandles.lookup().findConstructor(proxiedClass, MethodType.methodType(void.class));
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            new NoConstructorFoundException(this.entityClass, exception).printStackTrace();
        }

        return null;
    }

    private DynamicType.Builder<T> createEntityBuilder() {
        return new ByteBuddy()
            .subclass(this.entityClass);
    }
}
