package net.ignpurple.proxi.mongodb.codec.entity;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.api.factory.ProxyWrapper;
import net.ignpurple.proxi.core.Proxi;
import net.ignpurple.proxi.mongodb.codec.ProxiCodec;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import net.ignpurple.proxi.mongodb.model.FieldModel;
import net.ignpurple.proxi.mongodb.model.MongoEntityModel;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;

import static net.ignpurple.proxi.mongodb.Mongo.DISCRIMINATOR_KEY;

public class EntityEncoder<T extends IDEntity<?>> implements Encoder<T> {
    private final Proxi proxi;
    private final MongoEntityModel entityModel;
    private final MongoDatastore datastore;
    private final ProxiCodec<T> proxiCodec;
    private final ProxyWrapper<T> proxyWrapper;

    private static final ObjectIdGenerator OBJECT_ID_GENERATOR = new ObjectIdGenerator();

    public EntityEncoder(MongoDatastore datastore, MongoEntityModel entityModel, ProxiCodec<T> proxiCodec) {
        this.proxi = Proxi.getInstance();
        this.entityModel = entityModel;
        this.datastore = datastore;
        this.proxiCodec = proxiCodec;

        this.proxyWrapper = (ProxyWrapper<T>) this.proxi.getFactoryStorage().create(this.entityModel.getType());
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        if (!this.entityModel.getType().isAssignableFrom(value.getClass())) {
            return;
        }

        writer.writeStartDocument();

        final FieldModel idModel = this.entityModel.getProperty("_id");
        this.encodeIdProperty(writer, value, encoderContext, idModel);
        this.encodeDiscriminator(writer, this.entityModel.getType());
        for (final FieldModel fieldModel : this.entityModel.getProperties()) {
            if (fieldModel.getMappedName().equalsIgnoreCase("_id")) {
                continue;
            }

            this.writeValue(writer, encoderContext, fieldModel, fieldModel.getAccessor().get(value));
        }

        // TODO: Implement encoding metadata if the entity is a proxy.

        writer.writeEndDocument();
    }

    private void encodeIdProperty(BsonWriter writer, T value, EncoderContext encoderContext, FieldModel idModel) {
        if (idModel == null) {
            return;
        }

        final Object id = idModel.getAccessor().get(value);
        if (id == null && encoderContext.isEncodingCollectibleDocument() && idModel.getType().isAssignableFrom(ObjectId.class)) {
            idModel.getAccessor().set(value, OBJECT_ID_GENERATOR.generate());
        }

        this.writeValue(writer, encoderContext, idModel, id);
    }

    private void encodeDiscriminator(BsonWriter writer, Class<?> type) {
        writer.writeString(DISCRIMINATOR_KEY, type.getName());
    }

    private void writeValue(BsonWriter writer, EncoderContext encoderContext, FieldModel model, Object value) {
        writer.writeName(model.getMappedName());
        if (value == null) {
            writer.writeNull();
            return;
        }

        final Codec<? super Object> codec = (Codec<? super Object>) model.getCodec();
        encoderContext.encodeWithChildContext(codec, writer, value);
    }

    @Override
    public Class<T> getEncoderClass() {
        return (Class<T>) this.entityModel.getType();
    }
}
