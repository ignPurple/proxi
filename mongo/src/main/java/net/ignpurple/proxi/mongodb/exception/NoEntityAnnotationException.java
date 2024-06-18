package net.ignpurple.proxi.mongodb.exception;

import net.ignpurple.proxi.api.entity.IDEntity;

public class NoEntityAnnotationException extends RuntimeException {

    public NoEntityAnnotationException(Class<? extends IDEntity<?>> entityClass) {
        super("No @EntityData annotation found on class " + entityClass.getSimpleName());
    }
}
