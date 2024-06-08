package net.ignpurple.proxi.api.exception;

import net.ignpurple.proxi.api.entity.Entity;

public class NoConstructorFoundException extends RuntimeException {

    public NoConstructorFoundException(Class<? extends Entity> entityClass, Exception exception) {
        super("Cannot find empty (public|protected) constructor for class " + entityClass.getSimpleName(), exception);
    }
}
