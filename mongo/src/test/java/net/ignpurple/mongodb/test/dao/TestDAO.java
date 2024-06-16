package net.ignpurple.mongodb.test.dao;

import net.ignpurple.mongodb.test.dao.entity.TestDAOEntity;
import net.ignpurple.proxi.database.dao.MultiDatastoreDAO;
import net.ignpurple.proxi.database.store.Datastore;

import java.util.UUID;

public class TestDAO extends MultiDatastoreDAO<UUID, TestDAOEntity> {

    public TestDAO(Class<TestDAOEntity> entityClass, Datastore mongoDatastore, Datastore redisDatastore) {
        super(entityClass);
        this.addDatastore(mongoDatastore)
            .addDatastore(redisDatastore);
    }

    @Override
    public TestDAOEntity createDefaultEntity() {
        return new TestDAOEntity(UUID.randomUUID());
    }
}
