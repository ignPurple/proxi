package net.ignpurple.mongodb.test.entity;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.entity.annotation.Id;
import net.ignpurple.proxi.mongodb.annotation.EntityData;

import java.util.UUID;

@EntityData("mongo-test")
public class MongoTestEntity implements IDEntity<UUID> {
    @Id
    private UUID id;

    public MongoTestEntity(UUID id) {
        this.id = id;
    }

    protected MongoTestEntity() {}

    @Override
    public UUID getId() {
        return this.id;
    }
}
