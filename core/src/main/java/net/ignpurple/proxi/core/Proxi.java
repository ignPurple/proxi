package net.ignpurple.proxi.core;

import net.ignpurple.proxi.core.storage.factory.FactoryStorage;
import net.ignpurple.proxi.core.storage.metadata.MetadataStorage;

public class Proxi {
    private final MetadataStorage metadataStorage;
    private final FactoryStorage factoryStorage;

    private static Proxi INSTANCE;

    Proxi() {
        this.metadataStorage = new MetadataStorage();
        this.factoryStorage = new FactoryStorage();
    }

    public static Proxi getInstance() {
        return Proxi.INSTANCE;
    }

    public MetadataStorage getMetadataStorage() {
        return this.metadataStorage;
    }

    public FactoryStorage getFactoryStorage() {
        return this.factoryStorage;
    }

    static {
        Proxi.INSTANCE = new Proxi();
    }
}
