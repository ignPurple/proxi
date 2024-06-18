package net.ignpurple.proxi.mongodb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CamelCase {

    public static String convert(String value) {
        List<String> groups = groupByCapitals(value);

        final StringJoiner joiner = new StringJoiner("");
        joiner.add(groups.get(0).toLowerCase());
        for (int i = 1; i < groups.size(); i++) {
            joiner.add(groups.get(i));
        }

        return joiner.toString();
    }

    static List<String> groupByCapitals(String value) {
        final List<String> groups = new ArrayList<>();
        final int length = value.length();
        StringBuilder builder = new StringBuilder();
        int index = 0;

        while (index < length) {
            char current = value.charAt(index);
            if (index == 0 || Character.isUpperCase(current)) {
                if (!builder.isEmpty()) {
                    groups.add(builder.toString());
                }

                builder = new StringBuilder();
            }
            builder.append(current);
            index++;
        }
        groups.add(builder.toString());

        return groups;
    }
}
