package net.ignpurple.proxi.database.store;

import net.ignpurple.proxi.database.model.EntityModelStore;

public interface Datastore {

    <T> T getClient();

    String getDatabase();

    EntityModelStore getModelStore();
}
