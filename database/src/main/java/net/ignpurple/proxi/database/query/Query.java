package net.ignpurple.proxi.database.query;

import net.ignpurple.proxi.api.entity.IDEntity;

public interface Query<T extends IDEntity<?>> extends Iterable<T> {

    long count();
}
