package org.kie.dmn.pmml;

import java.util.Map;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.internal.assembler.KieAssemblers;
import org.kie.api.internal.utils.ServiceRegistry;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.api.DMNFactory;
import org.kie.dmn.core.assembler.DMNAssemblerService;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.kie.internal.builder.IncrementalResults;
import org.kie.internal.builder.InternalKieBuilder;
import org.kie.internal.services.KieAssemblersImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DMNRuntimePMMLTest {

    public DMNRuntimePMMLTest() {
        super();
    }

    public static final Logger LOG = LoggerFactory.getLogger(DMNRuntimePMMLTest.class);

    @Test
    public void testSimpleItemDefinition() {
        final DMNRuntime runtime = DMNRuntimeUtil.createRuntimeWithAdditionalResources("KiePMMLScoreCard.dmn",
                                                                                       this.getClass(),
                                                                                       "test_scorecard.pmml");
        runDMNModelInvokingPMML(runtime);
    }

    private void runDMNModelInvokingPMML(final DMNRuntime runtime) {
        final DMNModel dmnModel = runtime.getModel("http://www.trisotech.com/definitions/_ca466dbe-20b4-4e88-a43f-4ce3aff26e4f", "KiePMMLScoreCard");
        assertThat( dmnModel, notNullValue() );
        assertThat( DMNRuntimeUtil.formatMessages( dmnModel.getMessages() ), dmnModel.hasErrors(), is( false ) );

        final DMNContext emptyContext = DMNFactory.newContext();

        final DMNResult dmnResult = runtime.evaluateAll(dmnModel, emptyContext);
        LOG.debug("{}", dmnResult);
        assertThat(DMNRuntimeUtil.formatMessages(dmnResult.getMessages()), dmnResult.hasErrors(), is(false));

        final DMNContext result = dmnResult.getContext();
        assertThat((Map<String, Object>) result.get("my decision"), hasEntry("calculatedScore", 41.345));
    }

    /**
     * test to use same building steps of BC/WB
     */
    @Test
    public void testSteppedCompilation() {
        final KieAssemblersImpl assemblers = (KieAssemblersImpl) ServiceRegistry.getInstance().get(KieAssemblers.class);
        assemblers.accept(new DMNAssemblerService());

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();

        kfs.write("src/main/resources/test_scorecard.pmml", ks.getResources().newClassPathResource("test_scorecard.pmml", this.getClass()));
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        assertEquals(0, kieBuilder.getResults().getMessages(org.kie.api.builder.Message.Level.ERROR).size());

        kfs.write("src/main/resources/KiePMMLScoreCard.dmn", ks.getResources().newClassPathResource("KiePMMLScoreCard.dmn", this.getClass()));
        IncrementalResults addResults = ((InternalKieBuilder) kieBuilder).createFileSet("src/main/resources/KiePMMLScoreCard.dmn").build();
        assertEquals(0, addResults.getAddedMessages().size());
        assertEquals(0, addResults.getRemovedMessages().size());

        KieRepository kr = ks.getRepository();
        KieContainer kieContainer = ks.newKieContainer(kr.getDefaultReleaseId());

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.getKieBase()).get(DMNRuntime.class);

        runDMNModelInvokingPMML(dmnRuntime);
    }
}
