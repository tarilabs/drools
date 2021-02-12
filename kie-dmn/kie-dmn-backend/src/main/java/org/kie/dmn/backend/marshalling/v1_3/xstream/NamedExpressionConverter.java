package org.kie.dmn.backend.marshalling.v1_3.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Expression;
import org.kie.dmn.model.api.NamedExpression;
import org.kie.dmn.model.v1_3.TNamedExpression;

public class NamedExpressionConverter extends DMNModelInstrumentedBaseConverter {

    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        NamedExpression namedExp = (NamedExpression) parent;

        if (child instanceof Expression) {
            namedExp.setExpression((Expression) child);
            namedExp.setName(nodeName);
        } else
            super.assignChildElement(parent, nodeName, child);
    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        NamedExpression namedExp = (NamedExpression) parent;        
        writeChildrenNode(writer, context, namedExp.getExpression(), MarshallingUtils.defineExpressionNodeName(namedExp.getExpression()));
    }
    
    public NamedExpressionConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TNamedExpression();
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(TNamedExpression.class);
    }

}
