/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.core.ast;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kie.api.pmml.PMML4Field;
import org.kie.api.pmml.PMML4Result;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.api.EvaluatorResult.ResultType;
import org.kie.dmn.core.ast.DMNFunctionDefinitionEvaluator.FormalParameter;
import org.kie.dmn.model.api.DMNElement;
import org.kie.internal.io.ResourceFactory;
import org.kie.pmml.pmml_4_2.PMML4ExecutionHelper;
import org.kie.pmml.pmml_4_2.PMML4ExecutionHelper.PMML4ExecutionHelperFactory;
import org.kie.pmml.pmml_4_2.PMMLRequestDataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNKiePMMLInvocationEvaluator implements DMNExpressionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger( DMNKiePMMLInvocationEvaluator.class );

    private final String name;
    private final List<FormalParameter> parameters = new ArrayList<>();

    private URL document;
    private String model;

    private DMNElement node;

    private String dmnNS;

    private PMML4ExecutionHelper helper;

    public DMNKiePMMLInvocationEvaluator(String dmnNS, String nodeName, DMNElement node, URL url, String model) {
        this.dmnNS = dmnNS;
        this.name = nodeName;
        this.node = node;
        this.document = url;
        this.model = model;
        init();
    }

    private void init() {
        try {
            helper = PMML4ExecutionHelperFactory.getExecutionHelper(model,
                                                                    ResourceFactory.newUrlResource(document),
                                                                    null);
            helper.addPossiblePackageName("org.drools.scorecards.example"); // TODO this is hardcoded in the .pmml file ?!
            helper.initModel();
        } catch (NoClassDefFoundError e) {
            // TODO error reporting.
        }
    }

    public DMNType getParameterType(String name) {
        for (FormalParameter fp : parameters) {
            if (fp.name.equals(name)) {
                return fp.type;
            }
        }
        return null;
    }

    public List<List<String>> getParameterNames() {
        return Collections.singletonList(parameters.stream().map(p -> p.name).collect(Collectors.toList()));
    }

    public List<List<DMNType>> getParameterTypes() {
        return Collections.singletonList(parameters.stream().map(p -> p.type).collect(Collectors.toList()));
    }

    public void addParameter(String name, DMNType dmnType) {
        this.parameters.add(new FormalParameter(name, dmnType));
    }

    @Override
    public EvaluatorResult evaluate(DMNRuntimeEventManager eventManager, DMNResult dmnr) {
        PMMLRequestDataBuilder request = new PMMLRequestDataBuilder(UUID.randomUUID().toString(),
                                                                    model);

        for (FormalParameter p : parameters) {
            Object pValue = dmnr.getContext().get(p.name);
            if (pValue instanceof BigDecimal) {
                pValue = ((BigDecimal) pValue).doubleValue();
            }
            Class class1 = pValue.getClass();
            request.addParameter(p.name, pValue, class1);
        }
        PMML4Result resultHolder = helper.submitRequest(request.build());

        Map<String, Object> resultVariables = resultHolder.getResultVariables();
        Map<String, Object> result = new HashMap<>();
        for (Object r : resultVariables.values()) {
            if (r instanceof PMML4Field) {
                PMML4Field pmml4Field = (PMML4Field) r;
                if (pmml4Field.getName() != null && !pmml4Field.getName().isEmpty()) {
                    String name = pmml4Field.getName();
                    try {
                        Method method = r.getClass().getMethod("getValue");
                        Object value = method.invoke(r);
                        result.put(name, value);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return new EvaluatorResultImpl(result, ResultType.SUCCESS);
    }

}
