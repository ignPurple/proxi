package net.ignpurple.core.test.entity;

import net.ignpurple.proxi.api.entity.Entity;

public class TestEntity implements Entity {
    private String key;

    public TestEntity(String key) {
        this.key = key;
    }

    protected TestEntity() {}
}
