package net.ignpurple.mongodb.test;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.ignpurple.mongodb.test.dao.entity.TestDAOEntity;
import net.ignpurple.mongodb.test.entity.MongoTestEntity;
import net.ignpurple.proxi.api.entity.metadata.Metadata;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.database.query.Query;
import net.ignpurple.proxi.database.store.Datastore;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import org.bson.UuidRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        final Datastore datastore = new MongoDatastore(mongoClient, "test");
        final Query<TestDAOEntity> query = datastore.find(TestDAOEntity.class);
        //new TestDAO(TestDAOEntity.class, datastore, null);

        //final UUID testUUID = UUID.fromString("d66099f7-b95d-40f1-acaa-c216a5e84bb9");
        //mongoClient.getDatabase("test").getCollection("test-dao-entities").insertOne(new Document().append("_id", this.test(testUUID)));

        for (final TestDAOEntity entity : query) {
            System.out.println("id - " + entity.getId());
        }
    }

    @Test
    public void insertDAOEntityTest() {
        final MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(new ConnectionString("mongodb://localhost:27017/"))
            .build()
        );
        final Datastore datastore = new MongoDatastore(mongoClient, "test");
        final Query<TestDAOEntity> query = datastore.find(TestDAOEntity.class);

        final UUID testUUID = UUID.fromString("d66099f7-b95d-40f1-acaa-c216a5e84bb9");
        datastore.save(new TestDAOEntity(testUUID));
        final TestDAOEntity entity = query.iterator().next();
        datastore.save(new TestDAOEntity(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        datastore.save(entity); // Saving duplicate Entity
    }
}
