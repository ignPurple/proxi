package net.ignpurple.proxi.mongodb.store;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.EntityModel;
import net.ignpurple.proxi.database.model.EntityModelStore;

import java.util.HashMap;
import java.util.Map;

public class MongoEntityModelStore implements EntityModelStore {
    private final Map<Class<? extends IDEntity<?>>, EntityModel> models;

    public MongoEntityModelStore() {
        this.models = new HashMap<>();
    }

    @Override
    public <T extends IDEntity<?>> EntityModel getModel(Class<T> type) {
        return this.models.get(type);
    }
}
