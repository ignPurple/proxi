package net.ignpurple.proxi.database.model;

import net.ignpurple.proxi.api.entity.IDEntity;

public interface EntityModel {

    Class<IDEntity<?>> getType();

    String getCollectionName();


}
