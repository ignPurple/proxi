package net.ignpurple.proxi.mongodb.model;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.entity.annotation.Id;
import net.ignpurple.proxi.database.model.EntityModel;
import net.ignpurple.proxi.mongodb.annotation.EntityData;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import net.ignpurple.proxi.mongodb.exception.NoEntityAnnotationException;
import net.ignpurple.proxi.mongodb.util.CamelCase;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoEntityModel implements EntityModel {
    private final MongoDatastore datastore;
    private final Class<? extends IDEntity<?>> entityClass;
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final Map<String, FieldModel> propertyModelsByName;
    private final Map<String, FieldModel> propertyModelsByMappedName;
    private final EntityData entityData;
    private final String collectionName;

    public MongoEntityModel(MongoDatastore datastore, Class<? extends IDEntity<?>> entityClass) {
        this.datastore = datastore;
        this.entityClass = entityClass;
        if (!this.entityClass.isAnnotationPresent(EntityData.class)) {
            throw new NoEntityAnnotationException(entityClass);
        }

        this.annotations = new HashMap<>();
        for (final Annotation annotation : this.entityClass.getAnnotations()) {
            this.annotations.put(annotation.annotationType(), annotation);
        }

        this.propertyModelsByName = new ConcurrentHashMap<>();
        this.propertyModelsByMappedName = new ConcurrentHashMap<>();
        this.registerProperties();

        this.entityData = (EntityData) this.annotations.get(EntityData.class);
        this.collectionName = this.entityData.value();
    }

    @Override
    public Class<? extends IDEntity<?>> getType() {
        return this.entityClass;
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
    }

    @Override
    public Collection<FieldModel> getProperties() {
        return this.propertyModelsByName.values();
    }

    @Override
    public FieldModel getProperty(String name) {
        return this.propertyModelsByName.getOrDefault(name, this.propertyModelsByMappedName.get(name));
    }

    private void registerProperties() {
        for (final Field field : this.entityClass.getDeclaredFields()) {
            if (isTransient(field) || isStatic(field)) {
                continue;
            }

            final String mappedName = discoverMappedName(field);
            final FieldModel fieldModel = new FieldModel(
                field,
                this.datastore.getCodecRegistry().get(field.getType()),
                mappedName
            );
            this.propertyModelsByName.put(field.getName(), fieldModel);
            this.propertyModelsByMappedName.put(mappedName, fieldModel);
        }
    }

    static String discoverMappedName(Field field) {
        if (field.getAnnotation(Id.class) != null) {
            return "_id";
        }

        return CamelCase.convert(field.getName());
    }

    static boolean isTransient(Field field) {
        return field.getDeclaredAnnotation(Transient.class) != null
            || Modifier.isTransient(field.getModifiers());
    }

    static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
}
