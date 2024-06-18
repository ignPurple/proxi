package net.ignpurple.proxi.api.entity;

public interface IDEntity<T> extends Entity {

    /**
     * The identifier for the entity, to separate it from other entities
     * that might exist in a database.
     * </p>
     * @return The unique identifier of the entity
     */
    T getId();
}
