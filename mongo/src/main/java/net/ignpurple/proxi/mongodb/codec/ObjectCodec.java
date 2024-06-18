package net.ignpurple.proxi.mongodb.codec;

import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import org.bson.*;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import static net.ignpurple.proxi.mongodb.Mongo.DISCRIMINATOR_KEY;

/**
 * Defines a generic codec for Objects that will attempt to discover and use the correct codec.
 */
public class ObjectCodec implements Codec<Object> {

    private final BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap();
    private MongoDatastore datastore;

    public ObjectCodec(MongoDatastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Object decode(BsonReader reader, DecoderContext decoderContext) {
        final BsonType bsonType = reader.getCurrentBsonType();

        if (bsonType != BsonType.DOCUMENT) {
            return this.datastore.getCodecRegistry()
                .get(this.bsonTypeClassMap.get(bsonType))
                .decode(reader, decoderContext);
        }

        Class<?> type = Document.class;
        final BsonReaderMark mark = reader.getMark();
        reader.readStartDocument();
        while (type.equals(Document.class) && reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (!reader.readName().equals(DISCRIMINATOR_KEY)) {
                reader.skipValue();
                continue;
            }

            try {
                type = Class.forName(reader.readString());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        mark.reset();

        return this.datastore.getCodecRegistry()
            .get(type)
            .decode(reader, decoderContext);
    }

    @Override
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        final Codec codec = this.datastore.getCodecRegistry().get(value.getClass());
        codec.encode(writer, value, encoderContext);
    }

    @Override
    public Class<Object> getEncoderClass() {
        return Object.class;
    }
}
