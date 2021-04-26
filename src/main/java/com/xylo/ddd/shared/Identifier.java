package com.xylo.ddd.shared;

import java.util.UUID;

public record Identifier(UUID uuid) implements Comparable<Identifier> {
    public static Identifier from(UUID uuid) {
        return new Identifier(uuid);
    }

    @Override
    public int compareTo(Identifier o) {
        return this.uuid.compareTo(o.uuid);
    }


}
