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

import java.math.BigDecimal;

import org.kie.api.runtime.rule.DataSource;
import org.kie.dmn.api.core.DMNContext;

public class PositiveNegativeDTUnit extends DMNUnit {

    private BigDecimal a_number;

    private DataSource<BigDecimal> DTInput1 = DataSource.create();
    private DataSource<String> DTOutput1 = DataSource.create();

    private String positive_or_negative;

    public PositiveNegativeDTUnit(DMNContext context) {
        super(context);
        this.a_number = (BigDecimal) context.get("a number");
    }

    public DataSource<BigDecimal> getDTInput1() {
        return DTInput1;
    }

    public DataSource<String> getDTOutput1() {
        return DTOutput1;
    }

    public String getPositive_or_negative() {
        return positive_or_negative;
    }

    @Override
    public void onStart() {
        // input1: (simple assignment)
        DTInput1.insert(a_number);
    }

    @Override
    public void onEnd() {
        // output1: (simple assignment)
        positive_or_negative = DTOutput1.iterator().next();
    }

    @Override
    public String getResult() {
        return getPositive_or_negative();
    }

}
