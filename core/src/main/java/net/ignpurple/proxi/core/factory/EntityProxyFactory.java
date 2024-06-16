package net.ignpurple.proxi.core.factory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.exception.NoConstructorFoundException;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.api.factory.ProxyWrapperFactory;
import net.ignpurple.proxi.api.visitor.ClassVisitor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class EntityProxyFactory<T extends Entity> implements ProxyWrapperFactory<T> {
    private final Class<T> entityClass;
    private DynamicType.Builder<T> entityBuilder;

    private final List<ClassVisitor<T>> visitors;

    private static final MethodHandles.Lookup METHOD_HANDLE_LOOKUP = MethodHandles.lookup();

    public EntityProxyFactory(Class<T> entityClass, List<ClassVisitor<T>> visitors) {
        this.entityClass = entityClass;
        this.entityBuilder = this.createEntityBuilder();
        this.visitors = visitors;
    }

    @Override
    public ProxyWrapper<T> make() {
        for (final ClassVisitor<T> visitor : this.visitors) {
            this.entityBuilder = visitor.visitFields(this.entityBuilder);
            this.entityBuilder = visitor.visitMethods(this.entityBuilder);
        }

        final Class<? extends T> proxiedClass = this.entityBuilder.make()
            .load(this.getClass().getClassLoader())
            .getLoaded();

        final EntityProxy<T> proxy = new EntityProxy<>(
            Arrays.stream(this.entityClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList())
        );

        proxy.setOriginalClass(this.entityClass);
        proxy.setProxiedClass(proxiedClass);
        proxy.setConstructor(this.findConstructor(proxiedClass));
        return proxy;
    }

    @Override
    public MethodHandle findConstructor(Class<? extends T> proxiedClass) {
        try {
            return METHOD_HANDLE_LOOKUP.findConstructor(proxiedClass, MethodType.methodType(void.class));
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            throw new NoConstructorFoundException(this.entityClass, exception);
        }
    }

    private DynamicType.Builder<T> createEntityBuilder() {
        return new ByteBuddy()
            .subclass(this.entityClass);
    }
}
