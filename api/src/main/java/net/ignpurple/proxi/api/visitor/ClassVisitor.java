package net.ignpurple.proxi.api.visitor;

import net.bytebuddy.dynamic.DynamicType;
import net.ignpurple.proxi.api.entity.Entity;

public interface ClassVisitor<T> {
    /**
     * Visit the class to intercept fields inside the entity.
     * This method allows manipulation of fields within the class.
     * </p>
     * One of the use cases is to read and modify the fields accordingly.
     * Allowing for proxying of the fields inside the class to be stored elsewhere.
     *
     * @param builder The byte buddy class builder
     * @return The new class builder with fields being visited
     */
    default DynamicType.Builder<T> visitFields(DynamicType.Builder<T> builder) {
        return builder;
    }

    /**
     * Visit the class to intercept methods inside the entity.
     * This method allows manipulation of methods within the class.
     * </p>
     * One of the use cases is to intercept the {@link Entity#getMetadata} method to return the
     * metadata object assigned to the entity.
     *
     * @param builder The byte buddy class builder
     * @return The new class builder with methods being visited
     */
    default DynamicType.Builder<T> visitMethods(DynamicType.Builder<T> builder) {
        return builder;
    }
}
