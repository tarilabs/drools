package org.kie.dmn.core.codegen.dmn11;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.modelcompiler.builder.KieBaseBuilder;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.impl.DMNRuntimeImpl;

public class PositiveNegativeDTEvaluator implements DMNExpressionEvaluator {

    @Override
    public EvaluatorResult evaluate(DMNRuntimeEventManager dmrem, DMNResult result) {
        System.out.println("### PROOF OF WORK ###");
        // TODO: this is written in all "evaluator" impls: in case an exception is thrown, the parent node will report it
        DMNRuntimeImpl dmnRuntime = ((DMNRuntimeImpl) dmrem.getRuntime());
        DMNContext dmnContext = result.getContext();

        DMNUnit dmnUnit = new PositiveNegativeDTUnit(dmnContext);

        EvaluatorResult unitResult = dmnUnit.evaluate(getRuleUnitExecutor());

        return unitResult;
    }

    public RuleUnitExecutor getRuleUnitExecutor() {
        InternalKnowledgeBase kbase = KieBaseBuilder.createKieBaseFromModel(new PositiveNegativeDTModel());
        System.out.println("### has units? " + kbase.hasUnits());
        return RuleUnitExecutor.create().bind(kbase);
    }
}
