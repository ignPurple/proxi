package net.ignpurple.proxi.api.factory;

import net.ignpurple.proxi.api.entity.Entity;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;

public interface EntityFactoryMaker<T extends Entity> {

    /**
     * Create the main Entity Factory to allow the creation of entity objects.
     * @return The factory to create new entities.
     */
    EntityFactory<T> make();

    /**
     * Finds the main (empty) constructor of the entity, this is to ensure that the entity has
     * an empty constructor, to allow the entity can be created.
     * @return The constructor that is found inside the entity.
     */
    MethodHandle findConstructor(Class<? extends T> proxiedClass);
}
