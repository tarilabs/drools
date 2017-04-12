package org.kie.dmn.feel.lang;

/**
 * A generic type (limited to single generic support)
 */
public class GenericType
        implements Type {
    
    private Type baseType;
    private Type genericType;

    public GenericType(Type baseType, Type genericType) {
        this.baseType = baseType;
        this.genericType = genericType;
    }
    
    public Type getBaseType() {
        return baseType;
    }
    
    public Type getGenericType() {
        return genericType;
    }

    @Override
    public String getName() {
        return baseType.getName();
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return baseType.isInstanceOf(o);
    }

}
