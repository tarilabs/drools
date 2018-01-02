/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.core.codegen.dmn11;

import static org.drools.model.DSL.declarationOf;
import static org.drools.model.DSL.from;
import static org.drools.model.DSL.on;
import static org.drools.model.DSL.rule;
import static org.drools.model.DSL.sourceOf;
import static org.drools.model.DSL.type;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.gt;
import static org.kie.dmn.feel.codegen.feel11.CompiledFEELSemanticMappings.lt;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.drools.model.Global;
import org.drools.model.Model;
import org.drools.model.Query;
import org.drools.model.Rule;
import org.drools.model.Source;
import org.drools.model.TypeMetaData;
import org.drools.model.Variable;
import org.kie.dmn.feel.runtime.UnaryTest;

public class PositiveNegativeDTModel implements Model {

    private static final String DT_INPUT1 = "__drools__i1";

    public PositiveNegativeDTModel() {
    }

    public static final java.math.BigDecimal K_0 = new java.math.BigDecimal(0, java.math.MathContext.DECIMAL128);

    /** FEEL unary test: <0 */
    public static final org.kie.dmn.feel.runtime.UnaryTest UT__600 = (feelExprCtx, left) -> lt(left, K_0);

    /** FEEL unary test: >0 */
    public static final org.kie.dmn.feel.runtime.UnaryTest UT__620 = (feelExprCtx, left) -> gt(left, K_0);

    /**   FEEL unary tests: >0, <0   */
    public List<UnaryTest> getUnaryTests() {
        return java.util.Arrays.asList(UT__620, UT__600);
    }

    public static Rule getR0() {
        Variable<PositiveNegativeDTUnit> unit = declarationOf(type(PositiveNegativeDTUnit.class));
        Variable<BigDecimal> my_number = declarationOf(type(BigDecimal.class));
        Source<BigDecimal> DTInput1 = sourceOf(DT_INPUT1, type(BigDecimal.class));

        Rule rule = rule("mypackage", "R0").unit(PositiveNegativeDTUnit.class)
                                           .view()
                                           .then(on(unit).execute((u) -> {
                                               System.out.println("rule are triggering");
                                           }));
        return rule;
    }

    public static Rule getR0b() {
        Variable<PositiveNegativeDTUnit> unit = declarationOf(type(PositiveNegativeDTUnit.class));
        Variable<BigDecimal> my_number = declarationOf(type(BigDecimal.class));
        Source<BigDecimal> DTInput1 = sourceOf(DT_INPUT1, type(BigDecimal.class));

        Rule rule = rule("mypackage", "R0b").unit(PositiveNegativeDTUnit.class)
                                            .view(
                                                  from(DTInput1).filter(my_number, (x) -> true))
                                            .then(on(unit).execute((u) -> {
                                                System.out.println("rule are triggering2");
                                            }));
        return rule;
    }

    public static Rule getR1() {
        Variable<PositiveNegativeDTUnit> unit = declarationOf(type(PositiveNegativeDTUnit.class));
        Variable<BigDecimal> my_number = declarationOf(type(BigDecimal.class));
        Source<BigDecimal> DTInput1 = sourceOf(DT_INPUT1, type(BigDecimal.class));

        Rule rule = rule("mypackage", "R1").unit(PositiveNegativeDTUnit.class)
                                           .view(
                                                 from(DTInput1).filter(my_number, unit, (x, u) -> {
                                                     System.out.println("Checking for x, u" + x + " " + u);
                                                     return UT__620.apply(u.getFEELEvaluationContext(), x);
                                                 }))
                                           .then(on(unit).execute((u) -> {
                                               u.get__drools__o1().insert("positive");
                                           }));
        return rule;
    }

    public static Rule getR2() {
        Variable<PositiveNegativeDTUnit> unit = declarationOf(type(PositiveNegativeDTUnit.class));
        Variable<BigDecimal> my_number = declarationOf(type(BigDecimal.class));
        Source<BigDecimal> DTInput1 = sourceOf(DT_INPUT1, type(BigDecimal.class));

        Rule rule = rule("mypackage", "R2").unit(PositiveNegativeDTUnit.class)
                                           .view(
                                                 from(DTInput1).filter(my_number, unit, (x, u) -> UT__600.apply(u.getFEELEvaluationContext(), x)))
                                           .then(on(unit).execute((u) -> {
                                               u.get__drools__o1().insert("negative");
                                           }));
        return rule;
    }

    @Override
    public String getName() {
        return "<generated ID goes here>";
    }

    @Override
    public List<Global> getGlobals() {
        return Collections.emptyList();
    }

    @Override
    public List<Rule> getRules() {
        return Arrays.asList(getR0(), getR0b(), getR1(), getR2());
    }

    @Override
    public List<Query> getQueries() {
        return Collections.emptyList();
    }

    @Override
    public List<TypeMetaData> getTypeMetaDatas() {
        return Collections.emptyList();
    }

}
