package net.ignpurple.proxi.api.factory;

import net.ignpurple.proxi.api.entity.Entity;

import java.lang.invoke.MethodHandle;

public interface ProxyWrapper<T extends Entity> {

    /**
     * This is used to create an instance of the class stored inside the factory.
     * Allowing easy creation of entities, to be used inside the DAO System.
     * @return The newly created entity
     */
    T createEntity();

    void setOriginalClass(Class<? extends T> originalClass);

    void setProxiedClass(Class<? extends T> proxiedClass);

    void setConstructor(MethodHandle constructor);
}
