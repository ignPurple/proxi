package net.ignpurple.proxi.database.dao;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.core.dao.AbstractIDEntityDAO;
import net.ignpurple.proxi.database.store.Datastore;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class MultiDatastoreDAO<K, V extends IDEntity<K>> extends AbstractIDEntityDAO<K, V> {
    private final List<Datastore> datastores;

    public MultiDatastoreDAO(Class<V> entityClass) {
        super(entityClass);

        this.datastores = new LinkedList<>();
    }

    /**
     * Use this to add what datastore's you want to use in order.
     * </p>
     * The MultiDatastoreDAO will use the datastore's in order of when
     * this method was called.
     * </p>
     * E.g.
     *  - addDao(redisDatastore);
     *  - addDao(mongoDatastore);
     * Redis would be used first before MongoDB to retrieve objects,
     * but they will be saved into both dao's
     *
     * @param datastore The datastore to use for retrieving and saving objects
     */
    public MultiDatastoreDAO<K, V> addDatastore(Datastore datastore) {
        this.datastores.add(datastore);
        return this;
    }
}
