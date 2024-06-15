package net.ignpurple.proxi.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import net.ignpurple.proxi.database.store.Datastore;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;

public class Mongo {

    public static Datastore createDatastore(MongoClient mongoClient, String database) {
        return new MongoDatastore(mongoClient, database);//Mongo.createDatastore(mongoClient, database);
    }

    /*public static Datastore createDatastore(MongoClient mongoClient, String database) {
       TODO: Create options for the custom datastore
    }*/
}
