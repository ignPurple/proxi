package net.ignpurple.proxi.api.exception;

import net.ignpurple.proxi.api.entity.Entity;

public class EntityCreationException extends RuntimeException {

    public EntityCreationException(Class<? extends Entity> entityClass, Throwable exception) {
        super("Unable to create entity " + entityClass.getSimpleName(), exception);
    }
}
