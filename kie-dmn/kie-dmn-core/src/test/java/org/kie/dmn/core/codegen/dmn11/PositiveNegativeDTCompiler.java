package org.kie.dmn.core.codegen.dmn11;

import org.kie.dmn.api.core.ast.DMNNode;
import org.kie.dmn.core.ast.DecisionNodeImpl;
import org.kie.dmn.core.compiler.DMNCompilerContext;
import org.kie.dmn.core.compiler.DMNCompilerImpl;
import org.kie.dmn.core.compiler.DecisionCompiler;
import org.kie.dmn.core.impl.DMNModelImpl;

public class PositiveNegativeDTCompiler extends DecisionCompiler {

    @Override
    public boolean accept(DMNNode node) {
        boolean shouldAccept = "_f9f209df-1d64-4c27-90e9-3ad42cb47c07".equals(node.getId());
        System.out.println("### shoudl accept? " + shouldAccept);
        return shouldAccept;
    }

    @Override
    public void compileEvaluator(DMNNode node, DMNCompilerImpl compiler, DMNCompilerContext ctx, DMNModelImpl model) {
        DecisionNodeImpl di = (DecisionNodeImpl) node;
        System.out.println("### setting evaluator ###");
        di.setEvaluator(new PositiveNegativeDTEvaluator());
    }

}
