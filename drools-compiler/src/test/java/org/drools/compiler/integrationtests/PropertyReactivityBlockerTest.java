/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.compiler.integrationtests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.drools.compiler.Address;
import org.drools.compiler.CommonTestMethodBase;
import org.drools.compiler.Person;
import org.drools.core.factmodel.traits.Traitable;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.io.impl.ByteArrayResource;
import org.drools.core.reteoo.ReteDumper;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.Modifies;
import org.kie.api.definition.type.PropertyReactive;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.utils.KieHelper;

public class PropertyReactivityBlockerTest extends CommonTestMethodBase {
    
    @Test()
    public void testA_NotWorking() {
        // DROOLS-644
        String drl =
                "import " + Person.class.getCanonicalName() + ";\n" +
                "global java.util.List list;\n" +
                "rule R when\n" +
                "    $p1 : Person( name == \"Mario\" ) \n" +
                "    $p2 : Person( age > $p1.age ) \n" +
                "then\n" +
                "    list.add(\"t0\");\n" +
                "end\n";

        KieSession ksession = new KieHelper().addContent(drl, ResourceType.DRL)
                .build()
                .newKieSession();
        
        ReteDumper.dumpRete(ksession);

        List<String> list = new ArrayList<String>();
        ksession.setGlobal("list", list);

        Person mario = new Person("Mario", 40);
        Person mark = new Person("Mark", 37);
        FactHandle fh_mario = ksession.insert(mario);
        ksession.insert(mark);
        ksession.fireAllRules();
        
        mario.setAge(35);
        ksession.update(fh_mario, mario, "age");
        
        int x = ksession.fireAllRules();
        assertEquals("t0", list.get(0));
    }
    
    @Test()
    public void testA_Working() {
        // DROOLS-644
        String drl =
                "import " + Person.class.getCanonicalName() + ";\n" +
                "global java.util.List list;\n" +
                "rule R when\n" +
                "    $p1 : Person( name == \"Mario\", $a1: age ) \n" +
                "    $p2 : Person( age > $a1 ) \n" +
                "then\n" +
                "    list.add(\"t0\");\n" +
                "end\n";

        KieSession ksession = new KieHelper().addContent(drl, ResourceType.DRL)
                .build()
                .newKieSession();
        
        ReteDumper.dumpRete(ksession);

        List<String> list = new ArrayList<String>();
        ksession.setGlobal("list", list);

        Person mario = new Person("Mario", 40);
        Person mark = new Person("Mark", 37);
        FactHandle fh_mario = ksession.insert(mario);
        ksession.insert(mark);
        ksession.fireAllRules();
        
        mario.setAge(35);
        ksession.update(fh_mario, mario, "age");
        
        int x = ksession.fireAllRules();
        assertEquals("t0", list.get(0));
    }
}
