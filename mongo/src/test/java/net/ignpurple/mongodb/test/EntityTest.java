package net.ignpurple.mongodb.test;

import net.ignpurple.mongodb.test.dao.TestDAO;
import net.ignpurple.mongodb.test.dao.entity.TestDAOEntity;
import net.ignpurple.mongodb.test.entity.MongoTestEntity;
import net.ignpurple.proxi.api.entity.metadata.Metadata;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntityTest {

    @Test
    public void mongoEntityFactoryCreation() {
        final Proxi proxi = Proxi.getInstance();
        final ProxyWrapper<MongoTestEntity> newFactory = proxi.getFactoryStorage().create(MongoTestEntity.class);
        final MongoTestEntity entity = newFactory.createEntity();

        Assertions.assertNotNull(entity);
        Assertions.assertNull(entity.getId());
        Assertions.assertNotNull(entity.getMetadata());
    }

    @Test
    public void entityMetadata() {
        final Proxi proxi = Proxi.getInstance();
        final ProxyWrapper<MongoTestEntity> newFactory = proxi.getFactoryStorage().create(MongoTestEntity.class);
        final MongoTestEntity entity = newFactory.createEntity();

        entity.getMetadata().setCustomData("Test", "Hi");
        entity.getMetadata().setCustomData("num", 123);

        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(entity.getMetadata());

        final Metadata metadata = entity.getMetadata();
        Assertions.assertEquals(metadata.getCustomData("Test"), "Hi");
        Assertions.assertEquals(metadata.getCustomData("num"), 123);
        Assertions.assertNull(metadata.getCustomData("123"));
        Assertions.assertEquals(metadata.getCustomDataOrDefault("123", 123), 123);

        entity.getMetadata().setCustomData("123", 1234);
        Assertions.assertEquals(metadata.getCustomData("123"), 1234);
    }

    @Test
    public void multiDatastoreDaoTest() {
        new TestDAO(TestDAOEntity.class, new MongoDatastore(null, null), null);
    }
}
