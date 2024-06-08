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
}
