package net.ignpurple.proxi.core.entity.metadata;

import net.ignpurple.proxi.api.entity.metadata.Metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EntityMetadata implements Metadata {
    private final long firstCreated;
    private final List<String> fields;
    private final Map<String, Object> customData;

    public EntityMetadata(long firstCreated, List<String> fields, Map<String, Object> customData) {
        this.firstCreated = firstCreated;
        this.fields = fields;
        this.customData = customData;
    }

    @Override
    public long getFirstCreated() {
        return this.firstCreated;
    }

    @Override
    public List<String> getFields() {
        return this.fields;
    }

    @Override
    public Object getCustomData(String key) {
        return this.customData.get(key.toLowerCase(Locale.ROOT));
    }

    @Override
    public <T> T getCustomDataOrDefault(String key, T data) {
        return (T) this.customData.getOrDefault(key.toLowerCase(Locale.ROOT), data);
    }

    @Override
    public <T> void setCustomData(String key, T value) {
        this.customData.put(key.toLowerCase(Locale.ROOT), value);
    }

    public static Metadata createMetadata(List<String> fields) {
        return new EntityMetadata(
            System.currentTimeMillis(),
            fields,
            new HashMap<>()
        );
    }
}
