package net.ignpurple.proxi.mongodb.codec;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.mongodb.codec.entity.EntityDecoder;
import net.ignpurple.proxi.mongodb.codec.entity.EntityEncoder;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import net.ignpurple.proxi.mongodb.model.MongoEntityModel;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class ProxiCodec<T extends IDEntity<?>> implements CollectibleCodec<T> {
    private final EntityEncoder<T> entityEncoder;
    private final EntityDecoder<T> entityDecoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxiCodec.class);

    public ProxiCodec(MongoDatastore datastore, Class<? extends IDEntity<?>> entityClass) {
        final MongoEntityModel entityModel = (MongoEntityModel) datastore.getModelStore().getModel(entityClass);
        this.entityEncoder = new EntityEncoder<>(datastore, entityModel, this);
        this.entityDecoder = new EntityDecoder<>(datastore, entityModel, this);
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        this.entityEncoder.encode(writer, value, encoderContext);
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return this.entityDecoder.decode(reader, decoderContext);
    }

    @Override
    public T generateIdIfAbsentFromDocument(T entity) {
        if (!this.documentHasId(entity)) {
            LOGGER.warn("There is no @Id inside of the entity (" + entity.getClass().getSimpleName() + ")");
        }
        return entity;
    }

    @Override
    public boolean documentHasId(T entity) {
        if (entity == null) {
            return false;
        }

        return ((IDEntity<?>) entity).getId() != null;
    }

    @Override
    public BsonValue getDocumentId(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<T> getEncoderClass() {
        return (Class<T>) ((IDEntity<Object>) () -> null).getClass();
    }
}
