package net.ignpurple.proxi.mongodb.codec.entity;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.mongodb.codec.ProxiCodec;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import net.ignpurple.proxi.mongodb.model.FieldModel;
import net.ignpurple.proxi.mongodb.model.MongoEntityModel;
import net.ignpurple.proxi.mongodb.util.Conversion;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

import static net.ignpurple.proxi.mongodb.Mongo.DISCRIMINATOR_KEY;

public class EntityDecoder<T extends IDEntity<?>> implements Decoder<T> {
    private final Proxi proxi;
    private final MongoEntityModel entityModel;
    private final MongoDatastore datastore;
    private final ProxiCodec<T> proxiCodec;
    private final ProxyWrapper<T> proxyWrapper;

    public EntityDecoder(MongoDatastore datastore, MongoEntityModel entityModel, ProxiCodec<T> proxiCodec) {
        this.proxi = Proxi.getInstance();
        this.entityModel = entityModel;
        this.datastore = datastore;
        this.proxiCodec = proxiCodec;

        this.proxyWrapper = (ProxyWrapper<T>) this.proxi.getFactoryStorage().create(this.entityModel.getType());
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        try {
            if (!decoderContext.hasCheckedDiscriminator()) {
                return this.decode(reader, DecoderContext.builder().checkedDiscriminator(true).build());
            }

            final T entity = this.proxyWrapper.createEntity();
            this.decodeProperties(reader, decoderContext, entity);
            //decoderContext.decodeWithChildContext(this.entityModel.getProperty(key).getCodec(), reader);
            return entity;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void decodeProperties(BsonReader reader, DecoderContext decoderContext, T entity) {
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            final String name = reader.readName();
            if (name.equalsIgnoreCase(DISCRIMINATOR_KEY)) {
                reader.readString();
                continue;
            }

            this.decodeModel(reader, decoderContext, entity, this.entityModel.getProperty(name));
        }

        reader.readEndDocument();
    }

    private void decodeModel(BsonReader reader, DecoderContext decoderContext, T entity, FieldModel fieldModel) {
        if (fieldModel == null) {
            reader.skipValue();
            return;
        }

        final BsonReaderMark mark = reader.getMark();
        try {
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                reader.readNull();
                return;
            }

            final Object value = decoderContext.decodeWithChildContext(fieldModel.getCodec(), reader);
            fieldModel.getAccessor().set(entity, value);
        } catch (BsonInvalidOperationException exception) {
            mark.reset();
            final Object value = this.datastore.getCodecRegistry().get(Object.class).decode(reader, decoderContext);
            fieldModel.getAccessor().set(entity, Conversion.convert(value, fieldModel.getType()));
        }
    }
}
