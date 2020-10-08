/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.openapi;

import org.kie.dmn.api.core.DMNType;

public class TempNamingPolicy {

    public String getName(DMNType type) {
        // TODO what if an anonymous inner type?
        return type.getName();
    }

    public String getRef(DMNType type) {
        return "#/definitions/" + getName(type); // TODO escape name as uri
    }
}
