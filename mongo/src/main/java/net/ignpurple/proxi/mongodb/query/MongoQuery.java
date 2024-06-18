package net.ignpurple.proxi.mongodb.query;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.query.Query;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;

public class MongoQuery<T extends IDEntity<?>> implements Query<T> {
    private final MongoDatastore mongoDatastore;
    private final Class<T> entityClass;
    private final MongoCollection<T> collection;

    public MongoQuery(MongoDatastore mongoDatastore, Class<T> entityClass) {
        this.mongoDatastore = mongoDatastore;
        this.entityClass = entityClass;
        this.collection = this.mongoDatastore.getStore(this.entityClass);
    }

    @Override
    public long count() {
        return this.collection.countDocuments();
    }

    @Override
    public MongoCursor<T> iterator() {
        return this.collection.find().iterator();
    }
}
