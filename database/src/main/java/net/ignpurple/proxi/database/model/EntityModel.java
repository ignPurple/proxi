package net.ignpurple.proxi.database.model;

import net.ignpurple.proxi.api.entity.IDEntity;
import net.ignpurple.proxi.database.model.property.PropertyModel;

import java.util.Collection;

public interface EntityModel {

    Class<? extends IDEntity<?>> getType();

    String getCollectionName();

    <T extends PropertyModel> Collection<T> getProperties();

    <T extends PropertyModel> T getProperty(String name);
}
