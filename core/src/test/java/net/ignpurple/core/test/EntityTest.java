package net.ignpurple.core.test;

import net.ignpurple.core.test.entity.TestEntity;
import net.ignpurple.proxi.api.entity.metadata.Metadata;
import net.ignpurple.proxi.api.factory.EntityFactory;
import net.ignpurple.proxi.core.Proxi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntityTest {

    @Test
    public void factoryCreation() {
        final Proxi proxi = Proxi.getInstance();
        final EntityFactory<TestEntity> newFactory = proxi.getFactoryStorage().create(TestEntity.class);
        final TestEntity entity = newFactory.createEntity();

        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(entity.getMetadata());
    }

    @Test
    public void entityMetadata() {
        final Proxi proxi = Proxi.getInstance();
        final EntityFactory<TestEntity> newFactory = proxi.getFactoryStorage().create(TestEntity.class);
        final TestEntity entity = newFactory.createEntity();

        entity.getMetadata().setCustomData("test", "Hi");
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
}
