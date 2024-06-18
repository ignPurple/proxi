package net.ignpurple.proxi.mongodb.store;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.EntityModel;
import net.ignpurple.proxi.database.model.EntityModelStore;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import net.ignpurple.proxi.mongodb.model.MongoEntityModel;

import java.util.HashMap;
import java.util.Map;

public class MongoEntityModelStore implements EntityModelStore {
    private final MongoDatastore datastore;
    private final Map<Class<? extends IDEntity<?>>, EntityModel> models;

    public MongoEntityModelStore(MongoDatastore datastore) {
        this.datastore = datastore;
        this.models = new HashMap<>();
    }

    @Override
    public <T extends IDEntity<?>> EntityModel getModel(Class<T> type) {
        return this.models.getOrDefault(type, this.register(this.createEntityModel(type)));
    }

    @Override
    public EntityModel register(EntityModel entityModel) {
        this.models.put(entityModel.getType(), entityModel);
        return entityModel;
    }

    @Override
    public <T extends IDEntity<?>> EntityModel createEntityModel(Class<T> type) {
        return new MongoEntityModel(this.datastore, type);
    }
}
