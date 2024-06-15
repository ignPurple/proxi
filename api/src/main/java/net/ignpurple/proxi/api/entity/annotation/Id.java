package net.ignpurple.proxi.api.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.ignpurple.proxi.api.entity.IDEntity;

/**
 * This is used to define which field inside
 * an entity is meant to be the identifier,
 * this is used mostly for mongodb sake as the _id
 * of the object will be replaced with the field
 * associated with this annotation.
 * </p>
 * This is a required annotation present on all {@link IDEntity}'s.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
}
