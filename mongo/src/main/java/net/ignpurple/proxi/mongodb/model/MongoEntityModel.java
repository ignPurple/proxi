package net.ignpurple.proxi.mongodb.model;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.EntityModel;
import net.ignpurple.proxi.mongodb.annotation.EntityData;
import net.ignpurple.proxi.mongodb.exception.NoEntityAnnotationException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class MongoEntityModel implements EntityModel {
    private final Class<IDEntity<?>> entityClass;
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final EntityData entityData;
    private final String collectionName;

    public MongoEntityModel(Class<IDEntity<?>> entityClass) {
        this.entityClass = entityClass;
        if (!entityClass.isAnnotationPresent(EntityData.class)) {
            throw new NoEntityAnnotationException(entityClass);
        }

        this.annotations = new HashMap<>();
        for (final Annotation annotation : this.entityClass.getAnnotations()) {
            this.annotations.put(annotation.getClass(), annotation);
        }

        this.entityData = (EntityData) this.annotations.get(EntityData.class);
        this.collectionName = this.entityData.value();
    }

    @Override
    public Class<IDEntity<?>> getType() {
        return this.entityClass;
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
    }
}
