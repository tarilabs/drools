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

package org.kie.dmn.validation.dtanalysis.mcdc;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.marshalling.DMNExtensionRegister;
import org.kie.dmn.backend.marshalling.v1x.DMNMarshallerFactory;
import org.kie.dmn.core.assembler.DMNAssemblerService;
import org.kie.dmn.core.compiler.DMNProfile;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.kie.dmn.feel.util.ClassLoaderUtil;
import org.kie.dmn.model.api.DecisionTable;
import org.kie.dmn.validation.DMNValidator;
import org.kie.dmn.validation.DMNValidatorFactory;
import org.kie.dmn.validation.dtanalysis.AbstractDTAnalysisTest;
import org.kie.dmn.validation.dtanalysis.mcdc.ExampleMCDCTest.MCDCListener;
import org.kie.dmn.validation.dtanalysis.mcdc.MCDCAnalyser.PosNegBlock;
import org.kie.dmn.validation.dtanalysis.mcdc.MCDCAnalyser.Record;
import org.kie.dmn.validation.dtanalysis.model.DTAnalysis;
import org.kie.internal.utils.ChainedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.kie.dmn.validation.DMNValidator.Validation.ANALYZE_DECISION_TABLE;
import static org.kie.dmn.validation.DMNValidator.Validation.COMPUTE_DECISION_TABLE_MCDC;

@RunWith(AllTests.class)
public final class DecisionCampMCDCExample2Tester extends AbstractDTAnalysisTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleMCDCTest.class);
    private static DMNValidator validator;
    private static DMNRuntime runtime;
    private static MCDCListener mcdcListener;

    public static TestSuite suite() throws Exception {
        initValidator();
        final String resourceFileName = "example2.dmn";
        List<DMNMessage> validate = validator.validate(new InputStreamReader(ExampleMCDCTest.class.getResourceAsStream(resourceFileName)), ANALYZE_DECISION_TABLE, COMPUTE_DECISION_TABLE_MCDC);

        initRuntime(resourceFileName);

        DTAnalysis analysis = getAnalysis(validate, "_e31c78b7-63ef-4112-a0bc-b0546043ebe9");
        Collection<Record> mcdcCases = computeMCDCCases(analysis.getMCDCSelectedBlocks());
        assertThat(mcdcCases, hasSize(14));
        TestSuite suite = new TestSuite();
        for (Test test : findAllTestCasesRuntime(analysis.getSource(), mcdcCases)) {
            suite.addTest(test);
        }
        ExampleMCDCTest.debugOutputAndOpenXLSX(analysis.getSource(), analysis.getMCDCSelectedBlocks());
        String mcdc2tck = MCDC2TCKGenerator.mcdc2tck(analysis.getSource(), analysis.getMCDCSelectedBlocks());
        ExampleMCDCTest.debugTCKXML(analysis.getSource(), mcdc2tck);
        Desktop.getDesktop().open(new File(ExampleMCDCTest.class.getResource(resourceFileName).getFile()));
        return suite;
    }

    private static void initValidator() {
        List<DMNProfile> defaultDMNProfiles = DMNAssemblerService.getDefaultDMNProfiles(ChainedProperties.getChainedProperties(ClassLoaderUtil.findDefaultClassLoader()));
        validator = DMNValidatorFactory.newValidator(defaultDMNProfiles);
        List<DMNExtensionRegister> extensionRegisters = defaultDMNProfiles.stream().flatMap(dmnp -> dmnp.getExtensionRegisters().stream()).collect(Collectors.toList());
        if (!extensionRegisters.isEmpty()) {
            marshaller = DMNMarshallerFactory.newMarshallerWithExtensions(extensionRegisters);
        } else {
            marshaller = DMNMarshallerFactory.newDefaultMarshaller();
        }
    }

    private static void initRuntime(String resourceFileName) {
        runtime = DMNRuntimeUtil.createRuntime(resourceFileName, ExampleMCDCTest.class);
        mcdcListener = new MCDCListener();
        runtime.addListener(mcdcListener);
        DMNModel dmnModel = runtime.getModels().get(0);
        assertThat(dmnModel, notNullValue());
    }

    private static List<Test> findAllTestCasesRuntime(DecisionTable sourceDT, Collection<Record> mcdcCases) {
        List<Test> tests = new ArrayList<>();
        for (Record mcdcCase : mcdcCases) {
            tests.add(new TestCase(mcdcCase.toString()) {

                protected void runTest() throws Throwable {
                    mcdcListener.selectedRule.clear();
                    DMNContext context = runtime.newContext();
                    for (int i = 0; i < mcdcCase.enums.length; i++) {
                        context.set(sourceDT.getInput().get(i).getInputExpression().getText(), mcdcCase.enums[i]);
                    }
                    DMNResult evaluateAll = runtime.evaluateAll(runtime.getModels().get(0), context);
                    LOG.debug("{}", evaluateAll);
                    assertThat(mcdcListener.selectedRule, hasItems(mcdcCase.ruleIdx + 1));
                }
            });
        }
        return tests;
    }

    private static Collection<Record> computeMCDCCases(List<PosNegBlock> mcdcSelectedBlocks) {
        Set<Record> mcdcRecords = new LinkedHashSet<>();
        for (PosNegBlock b : mcdcSelectedBlocks) {
            mcdcRecords.add(b.posRecord);
            for (Record negRecord : b.negRecords) {
                mcdcRecords.add(negRecord);
            }
        }
        return mcdcRecords;
    }
}
