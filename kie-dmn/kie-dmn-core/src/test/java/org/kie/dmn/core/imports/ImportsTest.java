package org.kie.dmn.core.imports;

import org.junit.Test;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ImportsTest {

    public static final Logger LOG = LoggerFactory.getLogger(ImportsTest.class);

    @Test
    public void testImport() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntimeWithAdditionalResources("Import_Complex.dmn",
                                                                                 this.getClass(),
                                                                                 "Model_Complex.dmn");

        //        final KieServices ks = KieServices.Factory.get();
        //
        //        final KieContainer kieContainer = KieHelper.getKieContainer(ks.newReleaseId("org.kie", "dmn-test-" + UUID.randomUUID(), "1.0"),
        //                                                                    ks.getResources().newClassPathResource("Import_Complex.dmn", this.getClass()),
        //                                                                    ks.getResources().newClassPathResource("Model_Complex.dmn", this.getClass()));
        //        DMNRuntime runtime = kieContainer.newKieSession().getKieRuntime(DMNRuntime.class);

        DMNModel importedModel = runtime.getModel("http://www.trisotech.com/dmn/definitions/_8bff4456-af07-4b88-a12a-9caa49291786",
                                                  "Import Complex");
        assertThat(importedModel, notNullValue());
        for (DMNMessage message : importedModel.getMessages()) {
            LOG.info("1 {}", message);
        }

        DMNModel dmnModel = runtime.getModel("http://www.trisotech.com/dmn/definitions/_a2c9eb9c-b448-4d2f-9bf6-491b39e4e87e",
                                             "Model Complex");
        assertThat(dmnModel, notNullValue());
        for (DMNMessage message : dmnModel.getMessages()) {
            LOG.info("2 {}", message);
        }

        DMNContext context = runtime.newContext();

        DMNResult evaluateAll = runtime.evaluateAll(dmnModel, context);
        for (DMNMessage message : evaluateAll.getMessages()) {
            LOG.info("e {}", message);
        }
        LOG.debug("{}", evaluateAll);
        assertThat(evaluateAll.getDecisionResultByName("Say").getResult(), is("Simon Ringuette"));
    }
}