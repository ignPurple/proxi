package net.ignpurple.proxi.api.entity;

public interface IDEntity<T> extends Entity {

    /**
     * The identifier for the entity, to seperate it from other entities
     * that might exist in a database.
     * @return The unique identifier of the entity
     */
    T getId();
}
