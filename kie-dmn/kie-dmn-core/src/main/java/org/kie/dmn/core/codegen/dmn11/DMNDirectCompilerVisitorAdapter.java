package org.kie.dmn.core.codegen.dmn11;

import org.kie.dmn.model.v1_1.DRGElement;
import org.kie.dmn.model.v1_1.Decision;
import org.kie.dmn.model.v1_1.Definitions;
import org.kie.dmn.model.v1_1.InputData;

public class DMNDirectCompilerVisitorAdapter {

    public DMNDirectCompilerVisitorAdapter() {
        // will put context here
    }

    public DMNDirectCompilerResult visit(Definitions dmnModel) {
        for (DRGElement drgElement : dmnModel.getDrgElement()) {
            visit(drgElement);
        }
        return null;
    }

    public DMNDirectCompilerResult visit(DRGElement drgElement) {
        if (drgElement instanceof InputData) {
            // do nothing.
        } else if (drgElement instanceof Decision) {
            return visit((Decision) drgElement);
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    public DMNDirectCompilerResult visit(Decision drgElement) {

        return null;
    }
}
