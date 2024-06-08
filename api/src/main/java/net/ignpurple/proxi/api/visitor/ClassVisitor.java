package net.ignpurple.proxi.api.visitor;

import net.bytebuddy.dynamic.DynamicType;

public interface ClassVisitor<T> {
    /**
     * Visit the class to intercept methods and fields inside the entity.
     * Allowing easy manipulation of the class and its methods.
     * </p>
     * One of the uses is to intercept the getMetadata method to return the
     * metadata object assigned to the entity.
     *
     * @param builder The byte buddy class builder
     * @return The new class builder with interceptions or fields
     */
    DynamicType.Builder<T> visit(DynamicType.Builder<T> builder);
}
