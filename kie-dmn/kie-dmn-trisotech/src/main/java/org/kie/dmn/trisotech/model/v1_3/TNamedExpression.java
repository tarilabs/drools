package org.kie.dmn.trisotech.model.v1_3;

import org.kie.dmn.model.api.Expression;
import org.kie.dmn.trisotech.model.api.NamedExpression;
import org.kie.dmn.model.v1_3.KieDMNModelInstrumentedBase;

public class TNamedExpression extends KieDMNModelInstrumentedBase implements NamedExpression {

    private String name;
    private Expression expression;

    public TNamedExpression() {};

    public TNamedExpression(String name, Expression exp) {
        this.name = name;
        this.expression = exp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public void setExpression(Expression expr) {
        this.expression = expr;
    }

}
