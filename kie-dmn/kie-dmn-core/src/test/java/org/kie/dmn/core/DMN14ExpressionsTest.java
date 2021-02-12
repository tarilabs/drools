/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.core;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.kie.dmn.api.core.DMNMessageType;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.impl.DMNContextImpl;
import org.kie.dmn.core.util.DMNRuntimeUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DMN14ExpressionsTest extends BaseInterpretedVsCompiledTest {

    private DMNRuntime runtime;
    private DMNModel model;

    public DMN14ExpressionsTest(final boolean useExecModelCompiler) {
        super(useExecModelCompiler);
    }

    @Before
    public void setup() {
        runtime = DMNRuntimeUtil.createRuntime("dmn14expressions.dmn", this.getClass());
        model = runtime.getModel("http://www.trisotech.com/definitions/_3404349f-5046-4ad3-ad15-7f1e27291ab5", "DMN 1.4 expressions");
    }

    @Test
    public void testConditionalWithInput() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Boolean Input", true)), "Conditional");
        assertEquals("Conditional evaluated to TRUE", results.getDecisionResultByName("Conditional").getResult());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Boolean Input", false)), "Conditional");
        assertEquals("Conditional evaluated to FALSE", results.getDecisionResultByName("Conditional").getResult());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Boolean Input", null)), "Conditional");
        assertEquals("Conditional evaluated to FALSE", results.getDecisionResultByName("Conditional").getResult());

    }

    @Test
    public void testConditionalNonBooleanIf() throws Throwable {
        final DMNRuntime runtime = DMNRuntimeUtil.createRuntime("dmn14expressions.dmn", this.getClass());
        DMNModel model = runtime.getModel("http://www.trisotech.com/definitions/_3404349f-5046-4ad3-ad15-7f1e27291ab5", "DMN 1.4 expressions");
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(), "Non boolean");
        assertEquals(1, results.getMessages().size());
        assertEquals(DMNMessageType.ERROR_EVAL_NODE, results.getMessages().iterator().next().getMessageType());

    }

    @Test
    public void testIteratorFor() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 1)), "Addition");
        assertEquals(Arrays.asList(2, 3, 4, 5).toString(), results.getDecisionResultByName("Addition").getResult().toString());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 2)), "Addition");
        assertEquals(Arrays.asList(3, 4, 5, 6).toString(), results.getDecisionResultByName("Addition").getResult().toString());
    }

    @Test
    public void testIteratorForPartial() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 0)), "Addition Partial");
        assertEquals(Arrays.asList(1, 3, 6, 10).toString(), results.getDecisionResultByName("Addition Partial").getResult().toString());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 5)), "Addition Partial");
        assertEquals(Arrays.asList(1, 8, 16, 25).toString(), results.getDecisionResultByName("Addition Partial").getResult().toString());
    }
    
    @Test
    public void testIteratorForInRangeClose() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 1)), "Addition Range Close");
        assertEquals(Arrays.asList(2, 3, 4, 5).toString(), results.getDecisionResultByName("Addition Range Close").getResult().toString());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 2)), "Addition Range Close");
        assertEquals(Arrays.asList(3, 4, 5, 6).toString(), results.getDecisionResultByName("Addition Range Close").getResult().toString());
    } 

    @Test
    public void testIteratorForInRangeOpen() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 1)), "Addition Range Open");
        assertEquals(Arrays.asList(2, 3, 4, 5).toString(), results.getDecisionResultByName("Addition Range Open").getResult().toString());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 2)), "Addition Range Open");
        assertEquals(Arrays.asList(3, 4, 5, 6).toString(), results.getDecisionResultByName("Addition Range Open").getResult().toString());
    } 

    
    @Test
    public void testIteratorSome() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 5)), "Number Greater Exists");
        assertTrue(((Boolean) results.getDecisionResultByName("Number Greater Exists").getResult()).booleanValue());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", -1)), "Number Greater Exists");
        assertTrue(((Boolean) results.getDecisionResultByName("Number Greater Exists").getResult()).booleanValue());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 11)), "Number Greater Exists");
        assertFalse(((Boolean) results.getDecisionResultByName("Number Greater Exists").getResult()).booleanValue());
    }

    @Test
    public void testIteratorEvery() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 5)), "All Greater");
        assertFalse(((Boolean) results.getDecisionResultByName("All Greater").getResult()).booleanValue());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", -1)), "All Greater");
        assertTrue(((Boolean) results.getDecisionResultByName("All Greater").getResult()).booleanValue());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 11)), "All Greater");
        assertFalse(((Boolean) results.getDecisionResultByName("All Greater").getResult()).booleanValue());
    }

    @Test
    public void testFilterIndex() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 5)), "Match by index");
        assertEquals(new BigDecimal(5), results.getDecisionResultByName("Match by index").getResult());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", -2)), "Match by index");
        assertEquals(new BigDecimal(9), results.getDecisionResultByName("Match by index").getResult());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 0)), "Match by index");
        assertTrue(results.getMessages().size() > 0);

    }

    @Test
    public void testFilterExpression() throws Throwable {
        DMNResult results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 5)), "Match by Fnct");
        assertEquals(Arrays.asList(6, 7, 8, 9, 10).toString(), results.getDecisionResultByName("Match by Fnct").getResult().toString());

        results = runtime.evaluateByName(model, new DMNContextImpl(Collections.singletonMap("Number Input", 11)), "Match by Fnct");
        assertEquals(Arrays.asList().toString(), results.getDecisionResultByName("Match by Fnct").getResult().toString());

    }
}
