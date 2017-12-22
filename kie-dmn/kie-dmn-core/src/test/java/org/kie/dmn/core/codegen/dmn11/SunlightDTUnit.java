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

import java.time.LocalDateTime;

import org.kie.api.runtime.rule.DataSource;
import org.kie.dmn.api.core.DMNContext;

public class SunlightDTUnit extends DMNUnit {

    private LocalDateTime now;

    private DataSource<Integer> sunlightInput1 = DataSource.create();
    private DataSource<String> sunlightOutput1 = DataSource.create();

    private String sunlight;

    public SunlightDTUnit(DMNContext context) {
        super(context);
        this.now = (LocalDateTime) context.get("now");
    }

    public DataSource<Integer> getSunlightInput1() {
        return sunlightInput1;
    }

    public DataSource<String> getSunlightOutput1() {
        return sunlightOutput1;
    }

    public String getSunlight() {
        return sunlight;
    }

    @Override
    public void onStart() {
        // input1: now.hour:
        sunlightInput1.insert(now.getHour());
    }

    @Override
    public void onEnd() {
        // output1: sunlight
        sunlight = sunlightOutput1.iterator().next();
    }

    @Override
    public String getResult() {
        return getSunlight();
    }

}
