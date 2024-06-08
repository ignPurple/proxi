package net.ignpurple.proxi.core.visitor;

import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.ignpurple.proxi.api.entity.Entity;
import net.ignpurple.proxi.api.entity.metadata.Metadata;
import net.ignpurple.proxi.api.visitor.ClassVisitor;
import net.ignpurple.proxi.core.Proxi;

import java.lang.reflect.Method;

public class MetadataVisitor<T> implements ClassVisitor<T> {

    @Override
    public DynamicType.Builder<T> visitMethods(DynamicType.Builder<T> builder) {
        return builder
            .method(ElementMatchers.isMethod()
                .and(ElementMatchers.returns(Metadata.class))
            ).intercept(MethodDelegation.to(new Interceptor()));
    }

    public static class Interceptor {
        @RuntimeType
        public Object intercept(@This Object self, @AllArguments Object[] args, @Origin Method method) {
            return Proxi.getInstance().getMetadataStorage().get((Entity) self);
        }
    }
}
