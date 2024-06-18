package net.ignpurple.proxi.api.entity;

import net.ignpurple.proxi.api.entity.metadata.Metadata;

public interface Entity {

    /**
     * The custom metadata for the entity, allowing the user
     * to store their own custom data alongside the entity without
     * any extra fields, and storing important information along-side
     * the entity.
     * <p>
     * Such as -
     * - When it was first created
     * - The fields that currently exist, allowing for versioning
     * - And other custom data the user wants to store.
     *
     * @return The metadata object to allow the user to interact with useful information
     */
    default Metadata getMetadata() {
        return null;
    }

    /**
     * Gets the class of the entity, use this instead of {@link Object#getClass()},
     * in case the entity is proxied, and you need the class you
     * expect the entity to be.
     * @return The class of the entity, if it's proxied; the superclass.
     */
    default <T> Class<T> getType() {
        return (Class<T>) this.getClass();
    }
}
