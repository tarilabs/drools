package org.kie.dmn.feel.lang;

import java.util.Map;

import org.kie.dmn.feel.util.EvalHelper;

public interface FEELPropertyAccessible {

    EvalHelper.PropertyValueResult getFEELProperty(String property);

    Map<String, Object> allFEELProperties();
}
