package net.ignpurple.core.test.entity;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.entity.annotation.Id;

import java.util.UUID;

public class IDTestEntity implements IDEntity<UUID> {
    @Id
    private UUID id;

    public IDTestEntity(UUID id) {
        this.id = id;
    }

    protected IDTestEntity() {}

    @Override
    public UUID getId() {
        return this.id;
    }
}
