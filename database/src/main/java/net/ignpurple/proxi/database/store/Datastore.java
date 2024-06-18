package net.ignpurple.proxi.database.store;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.EntityModelStore;
import net.ignpurple.proxi.database.query.Query;

public interface Datastore {

    <T> T getClient();

    String getDatabase();

    EntityModelStore getModelStore();

    <T extends IDEntity<?>> Query<T> find(Class<T> entityClass);

    <T extends IDEntity<?>> void save(T entity);

    <T, E extends IDEntity<?>> T getStore(Class<E> type);
}
