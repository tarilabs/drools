package org.kie.dmn.trisotech.model.api;

import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Expression;

public interface NamedExpression extends DMNModelInstrumentedBase {

    String getName();

    void setName(String name);

    Expression getExpression();

    void setExpression(Expression exp);

}
