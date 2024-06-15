package net.ignpurple.proxi.mongodb.datastore;

import com.mongodb.client.MongoClient;
import net.ignpurple.proxi.database.model.EntityModelStore;
import net.ignpurple.proxi.database.store.Datastore;
import net.ignpurple.proxi.mongodb.store.MongoEntityModelStore;

@SuppressWarnings("unchecked")
public class MongoDatastore implements Datastore {
    private final MongoClient mongoClient;
    private final String database;

    private final EntityModelStore modelStore;

    public MongoDatastore(MongoClient mongoClient, String database) {
        this.mongoClient = mongoClient;
        this.database = database;

        this.modelStore = new MongoEntityModelStore();
    }

    public <T> T getClient() {
        return (T) this.mongoClient;
    }

    public String getDatabase() {
        return this.database;
    }

    public EntityModelStore getModelStore() {
        return this.modelStore;
    }
}
