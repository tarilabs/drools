package org.kie.dmn.core.codegen.dmn11;

import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.impl.DMNRuntimeImpl;

public class TempManualTestLiteral implements DMNExpressionEvaluator {

    @Override
    public EvaluatorResult evaluate(DMNRuntimeEventManager dmrem, DMNResult result) {
        // in case an exception is thrown, the parent node will report it
        DMNRuntimeImpl dmnRuntime = ((DMNRuntimeImpl) dmrem.getRuntime());
        DMNContext dmnContext = result.getContext();

        return null;
    }

}
