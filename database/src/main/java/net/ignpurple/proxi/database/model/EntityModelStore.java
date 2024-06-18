package net.ignpurple.proxi.database.model;

import net.ignpurple.proxi.api.entity.IDEntity;

public interface EntityModelStore {

    <T extends IDEntity<?>> EntityModel getModel(Class<T> type);

    EntityModel register(EntityModel entityModel);

    <T extends IDEntity<?>> EntityModel createEntityModel(Class<T> type);
}
