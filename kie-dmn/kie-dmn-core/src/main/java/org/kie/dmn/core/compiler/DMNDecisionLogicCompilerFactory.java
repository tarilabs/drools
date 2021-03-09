package org.kie.dmn.core.compiler;

import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.ast.DMNBaseNode;
import org.kie.dmn.core.impl.DMNModelImpl;
import org.kie.dmn.model.api.Expression;

/**
 * for internal use
 */
public interface DMNDecisionLogicCompilerFactory {

    DMNDecisionLogicCompiler newDMNDecisionLogicCompiler(DMNCompilerImpl dmnCompiler, DMNCompilerConfigurationImpl dmnCompilerConfig);

}
