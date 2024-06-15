package net.ignpurple.proxi.core.dao;

import net.ignpurple.proxi.api.dao.IDEntityDAO;
import net.ignpurple.proxi.api.entity.IDEntity;

public abstract class AbstractIDEntityDAO<K, V extends IDEntity<K>> implements IDEntityDAO<K, V> {
    private final Class<V> entityClass;

    public AbstractIDEntityDAO(Class<V> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class<V> getEntityClass() {
        return this.entityClass;
    }
}
