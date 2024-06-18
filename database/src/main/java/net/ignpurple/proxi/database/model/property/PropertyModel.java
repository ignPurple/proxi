package net.ignpurple.proxi.database.model.property;

public interface PropertyModel {

    <T> T getAccessor();

    <T> T getCodec();

    String getMappedName();
}
