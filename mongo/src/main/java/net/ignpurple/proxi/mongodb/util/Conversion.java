package net.ignpurple.proxi.mongodb.util;

import org.bson.types.ObjectId;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Conversion {
    private static final Map<Class<?>, Map<Class<?>, Function<?, ?>>> CONVERSIONS = new ConcurrentHashMap<>();
    static {
        Conversion.register(String.class, ObjectId.class, ObjectId::new);
        Conversion.register(String.class, UUID.class, UUID::fromString);
    }

    public static <S, T> void register(Class<S> source, Class<T> target, Function<S, T> function) {
        CONVERSIONS.computeIfAbsent(source, ($) -> new ConcurrentHashMap<>()).put(target, function);
    }

    public static <T> T convert(Object value, Class<T> targetType) {
        final Class<?> fromType = value.getClass();
        if (fromType.equals(targetType)) {
            return (T) value;
        }

        final Function function = CONVERSIONS
            .computeIfAbsent(fromType, (f) -> new ConcurrentHashMap<>())
            .get(targetType);
        if (function == null) {
            if (targetType.equals(String.class)) {
                return (T) value.toString();
            }

            if (targetType.isEnum() && fromType.equals(String.class)) {
                return (T) Enum.valueOf((Class<? extends Enum>) targetType, (String) value);
            }

            return (T) value;
        }

        return (T) function.apply(value);
    }
}
