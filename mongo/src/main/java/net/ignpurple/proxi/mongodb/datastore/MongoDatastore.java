package net.ignpurple.proxi.mongodb.datastore;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.EntityModel;
import net.ignpurple.proxi.database.model.EntityModelStore;
import net.ignpurple.proxi.database.query.Query;
import net.ignpurple.proxi.database.store.Datastore;
import net.ignpurple.proxi.mongodb.codec.provider.ProxiCodecProvider;
import net.ignpurple.proxi.mongodb.query.MongoQuery;
import net.ignpurple.proxi.mongodb.store.MongoEntityModelStore;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MongoDatastore implements Datastore {
    private final MongoClient mongoClient;
    private final String database;
    private MongoDatabase mongoDatabase;

    private final EntityModelStore modelStore;

    public MongoDatastore(MongoClient mongoClient, String database) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.mongoDatabase = this.mongoClient.getDatabase(this.database);

        final List<CodecProvider> providers = new ArrayList<>();
        providers.add(this.mongoDatabase.getCodecRegistry());
        providers.add(new ProxiCodecProvider(this));
        this.mongoDatabase = this.mongoDatabase.withCodecRegistry(CodecRegistries.fromProviders(providers));
        this.modelStore = new MongoEntityModelStore(this);
    }

    @Override
    public <T> T getClient() {
        return (T) this.mongoClient;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public EntityModelStore getModelStore() {
        return this.modelStore;
    }

    public CodecRegistry getCodecRegistry() {
        return this.mongoDatabase.getCodecRegistry();
    }

    @Override
    public <T extends IDEntity<?>> Query<T> find(Class<T> entityClass) {
        return new MongoQuery<>(this, entityClass);
    }

    @Override
    public <T extends IDEntity<?>> void save(T entity) {
        final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        final Document filter = new Document("_id", entity.getId());
        final MongoCollection<T> collection = this.getStore(entity.getType());
        collection.replaceOne(filter, entity, replaceOptions);
    }

    @Override
    public <T, E extends IDEntity<?>> T getStore(Class<E> type) {
        final EntityModel entityModel = this.modelStore.getModel(type);
        final String collectionName = entityModel.getCollectionName();
        return (T) this.mongoDatabase.getCollection(collectionName, type);
    }
}
