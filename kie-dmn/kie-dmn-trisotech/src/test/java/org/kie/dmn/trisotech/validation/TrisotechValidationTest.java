package org.kie.dmn.trisotech.validation;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.marshalling.DMNMarshaller;
import org.kie.dmn.core.assembler.DMNAssemblerService;
import org.kie.dmn.core.compiler.DMNProfile;
import org.kie.dmn.trisotech.TrisotechDMNProfile;
import org.kie.dmn.trisotech.core.compiler.TrisotechDMNEvaluatorCompilerFactory;
import org.kie.dmn.validation.DMNValidator;
import org.kie.dmn.validation.DMNValidatorFactory;

import static org.junit.Assert.assertEquals;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class TrisotechValidationTest {

    protected static DMNValidator validator;
    protected static DMNMarshaller marshaller;

    @BeforeClass
    public static void init() {
        List<DMNProfile> dmnProfiles = Arrays.asList(new TrisotechDMNProfile());
        Properties p = new Properties();
        p.put(DMNAssemblerService.DMN_DECISION_LOGIC_COMPILER, TrisotechDMNEvaluatorCompilerFactory.class.getCanonicalName());
        validator = DMNValidatorFactory.newValidator(dmnProfiles, p);
    }

    @AfterClass
    public static void dispose() {
        validator.dispose();
    }

    protected Reader getReader(final String resourceFileName) {
        return getReader(resourceFileName, this.getClass());
    }

    protected Reader getReader(final String resourceFileName, Class<?> clazz) {
        return new InputStreamReader(clazz.getResourceAsStream(resourceFileName));
    }

    @Test
    public void testBoxedExtension_Conditional13() {
        List<DMNMessage> validate = validator.validateUsing(VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION)
                                             .usingSchema(TrisotechSchema.INSTANCEv1_3)
                                             .theseModels(getReader("boxedcontextextension/conditional.dmn"));
        assertEquals(0, validate.size());
    }

    @Test
    public void testBoxedExtension_Iterator13() {
        List<DMNMessage> validate = validator.validateUsing(VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION)
                                             .usingSchema(TrisotechSchema.INSTANCEv1_3)
                                             .theseModels(getReader("boxedcontextextension/iterator.dmn"));
        assertEquals(0, validate.size());
    }

    @Test
    public void testBoxedExtension_Filter13() {
        List<DMNMessage> validate = validator.validateUsing(VALIDATE_SCHEMA, VALIDATE_MODEL, VALIDATE_COMPILATION)
                                             .usingSchema(TrisotechSchema.INSTANCEv1_3)
                                             .theseModels(getReader("boxedcontextextension/filter.dmn"));
        assertEquals(0, validate.size());
    }
}
