package net.ignpurple.mongodb.test.dao.entity;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.entity.annotation.Id;
import net.ignpurple.proxi.mongodb.annotation.EntityData;

import java.util.UUID;

@EntityData("test-dao-entities")
public class TestDAOEntity implements IDEntity<UUID> {
    @Id
    private UUID id;
    private String test = "abc";

    public TestDAOEntity(UUID id) {
        this.id = id;
    }

    protected TestDAOEntity() {}

    @Override
    public UUID getId() {
        return this.id;
    }
}
