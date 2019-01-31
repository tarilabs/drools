package org.kie.dmn.feel.lang;

import java.util.Map;

import org.kie.dmn.api.feel.lang.Type;

/**
 * A composite type interface, i.e., a type that contains fields
 */
public interface CompositeType
        extends Type {

    Map<String, Type> getFields();

}
