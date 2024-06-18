package net.ignpurple.proxi.mongodb.codec.provider;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.mongodb.codec.ObjectCodec;
import net.ignpurple.proxi.mongodb.codec.ProxiCodec;
import net.ignpurple.proxi.mongodb.datastore.MongoDatastore;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.IdentityHashMap;
import java.util.Map;

public class ProxiCodecProvider implements CodecProvider {
    private final MongoDatastore datastore;
    private final Map<Class<?>, Codec<?>> codecs;

    public ProxiCodecProvider(MongoDatastore datastore) {
        this.datastore = datastore;
        this.codecs = new IdentityHashMap<>();
        this.codecs.put(Object.class, new ObjectCodec(this.datastore));
    }

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry registry) {
        final Codec<?> codec = this.codecs.get(type);
        if (codec == null && IDEntity.class.isAssignableFrom(type)) {
            final Class<? extends IDEntity<?>> entity = (Class<? extends IDEntity<?>>) type;
            final ProxiCodec<?> entityCodec = new ProxiCodec<>(this.datastore, entity);
            this.codecs.put(entity, entityCodec);
            return (Codec<T>) entityCodec;
        }

        return (Codec<T>) codec;
    }
}
