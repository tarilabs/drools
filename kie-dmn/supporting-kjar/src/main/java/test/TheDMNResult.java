package test;

import java.util.List;

import org.kie.dmn.api.core.DMNDecisionResult;

/**
 * MEGADUMMY
 */
public interface TheDMNResult {

    TheDMNContext2 getContext();

    List<DMNDecisionResult> getDecisionResults();

}
