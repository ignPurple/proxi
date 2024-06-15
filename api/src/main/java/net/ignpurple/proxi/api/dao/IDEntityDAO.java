package net.ignpurple.proxi.api.dao;

import net.ignpurple.proxi.api.entity.IDEntity;

public interface IDEntityDAO<K, V extends IDEntity<K>> {

    V createDefaultEntity();

    Class<V> getEntityClass();
}
