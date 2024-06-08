package net.ignpurple.proxi.api.entity.metadata;

import java.util.List;

public interface Metadata {
    /**
     * Information of when the entity was created.
     * @return Returns the time unit of when the class was first created
     */
    long getFirstCreated();

    /**
     * Information of what fields the entity has stored.
     * @return Returns the list of fields stored inside the entity
     */
    List<String> getFields();

    /**
     * Get the custom data the user has stored inside the entity.
     * @param key The key of the custom data is stored along-side
     * @return The custom data stored inside the metadata of the entity
     */
    Object getCustomData(String key);

    /**
     * Get the custom data the user has stored inside the entity.
     * @param <T> The object that is stored. Allowing it to be cast to any object.
     * @param key The key of the custom data is stored along-side
     * @param data The default data to return if it doesn't already exist
     * @return The custom data stored inside the metadata of the entity
     */
    <T> T getCustomDataOrDefault(String key, T data);

    /**
     * Set the custom data the user wants to store inside the entity.
     * @param key The key of the custom data should be store along-side
     * @param value The data that should be stored
     */
    <T> void setCustomData(String key, T value);
}
