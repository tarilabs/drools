package test20190810;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.impl.DMNContextFPAImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.Person;
import test.TheDMNContext;

public class MyTest {

    public static final Logger LOG = LoggerFactory.getLogger(MyTest.class);

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "http://www.trisotech.com/definitions/_2ceee5b6-0f0d-41ef-890e-2cd6fb1adb10";
        String modelName = "Drawing 1";

        DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);
        TheDMNContext context = new TheDMNContext();
        Person pojo = new Person();
        pojo.setName("Mr. x");
        context.setP(pojo);

        DMNResult evaluateAll = dmnRuntime.evaluateAll(dmnModel, new DMNContextFPAImpl(context));
        LOG.info("{}", evaluateAll);
    }
}
