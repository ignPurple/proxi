package net.ignpurple.proxi.mongodb.dao;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.core.dao.AbstractIDEntityDAO;

public abstract class MongoIDEntityDAO<K, V extends IDEntity<K>> extends AbstractIDEntityDAO<K, V> {

    public MongoIDEntityDAO(Class<V> entityClass) {
        super(entityClass);
    }
}
