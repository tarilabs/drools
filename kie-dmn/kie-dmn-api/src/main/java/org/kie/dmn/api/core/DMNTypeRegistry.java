package org.kie.dmn.api.core;

public interface DMNTypeRegistry {

    DMNType unknown();

    DMNType registerType(DMNType type);

    DMNType resolveType(String namespace, String name);

    DMNType resolveType(String name);

}
