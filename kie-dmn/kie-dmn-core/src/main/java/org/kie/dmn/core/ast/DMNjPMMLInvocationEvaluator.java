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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.DefaultVisitorBattery;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.OutputField;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.event.DMNRuntimeEventManager;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.core.api.EvaluatorResult;
import org.kie.dmn.core.api.EvaluatorResult.ResultType;
import org.kie.dmn.core.ast.DMNFunctionDefinitionEvaluator.FormalParameter;
import org.kie.dmn.model.api.DMNElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNjPMMLInvocationEvaluator implements DMNExpressionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger( DMNjPMMLInvocationEvaluator.class );

    private final String name;
    private final List<FormalParameter> parameters = new ArrayList<>();

    private String document;
    private String model;

    private DMNElement node;

    private String dmnNS;

    public DMNjPMMLInvocationEvaluator(String dmnNS, String nodeName, DMNElement node, String document, String model) {
        this.dmnNS = dmnNS;
        this.name = nodeName;
        this.node = node;
        this.document = document;
        this.model = model;
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
        Evaluator evaluator;
        try {
            evaluator = new LoadingModelEvaluatorBuilder()
                                                          .setLocatable(false)
                                                          .setVisitors(new DefaultVisitorBattery())
                                                          //.setOutputFilter(OutputFilters.KEEP_FINAL_RESULTS)
                                                          .load(new URL(document).openStream())
                                                          .build();
            evaluator.verify();
        } catch (Exception e) {
            e.printStackTrace();
            return new EvaluatorResultImpl(null, ResultType.FAILURE);
        }

        List<? extends InputField> inputFields = evaluator.getInputFields();

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            Object rawValue = dmnr.getContext().get(inputName.getValue());
            if (rawValue instanceof BigDecimal) {
                rawValue = ((BigDecimal) rawValue).doubleValue();
            }
            FieldValue inputValue = inputField.prepare(rawValue);
            System.out.println(inputName);
            System.out.println(inputValue);
            arguments.put(inputName, inputValue);
        }
        Map<FieldName, ?> results = evaluator.evaluate(arguments);

        Map<String, Object> result = new HashMap<>();
        for (OutputField of : evaluator.getOutputFields()) {
            String outputFieldName = of.getName().getValue();
            Optional<FieldName> fnKey = results.keySet().stream().filter(fn -> fn.getValue().equals(outputFieldName)).findFirst();
            result.put(outputFieldName, fnKey.map(results::get).orElse(null));
        }

        return new EvaluatorResultImpl(result, ResultType.SUCCESS);
    }

}
