package org.kie.dmn.model.api;

public interface NamedExpression extends DMNModelInstrumentedBase {

    String getName();

    void setName(String name);

    Expression getExpression();

    void setExpression(Expression exp);

}
