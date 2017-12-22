package org.kie.dmn.core.codegen.dmn11;

import java.util.Collections;
import java.util.Set;

import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNDecisionResult.DecisionEvaluationStatus;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.api.EvaluatorResult.ResultType;
import org.kie.dmn.core.ast.EvaluatorResultImpl;
import org.kie.dmn.core.impl.DMNDecisionResultImpl;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.impl.EvaluationContextImpl;

public abstract class DMNUnit implements RuleUnit {

    private DMNContext context;

    protected DMNUnit(DMNContext context) {
        this.context = context;
    }

    // TODO
    public Set<String> getRequirements() {
        return Collections.emptySet();
    }

    public abstract Object getResult();

    public DMNDecisionResult execute(RuleUnitExecutor executor) {
        executor.run(this);
        DMNDecisionResultImpl result = new DMNDecisionResultImpl("x", "x", DecisionEvaluationStatus.SUCCEEDED, getResult(), Collections.emptyList());
        return result;
    }

    public EvaluatorResult evaluate(RuleUnitExecutor executor) {
        executor.run(this);
        EvaluatorResult result = new EvaluatorResultImpl(getResult(), ResultType.SUCCESS);
        return result;
    }

    public EvaluationContext getFEELEvaluationContext() {
        EvaluationContextImpl feelCtx = new EvaluationContextImpl(null);
        feelCtx.setValues(context.getAll());
        return feelCtx;
    }
}
