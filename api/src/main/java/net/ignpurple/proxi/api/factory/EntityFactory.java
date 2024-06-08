package net.ignpurple.proxi.api.factory;

import net.ignpurple.proxi.api.entity.Entity;

public interface EntityFactory<T extends Entity> {

    /**
     * This is used to create an instance of the class stored inside the factory.
     * Allowing easy creation of entities, to be used inside the DAO System.
     * @return The newly created entity
     */
    T createEntity();
}
