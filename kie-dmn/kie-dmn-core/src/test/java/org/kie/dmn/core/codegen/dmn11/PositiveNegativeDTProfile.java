package org.kie.dmn.core.codegen.dmn11;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.kie.dmn.api.marshalling.v1_1.DMNExtensionRegister;
import org.kie.dmn.core.compiler.DMNProfile;
import org.kie.dmn.core.compiler.DRGElementCompiler;
import org.kie.dmn.feel.runtime.FEELFunction;

public class PositiveNegativeDTProfile implements DMNProfile {

    @Override
    public List<FEELFunction> getFEELFunctions() {
        return Collections.emptyList();
    }

    @Override
    public List<DMNExtensionRegister> getExtensionRegisters() {
        return Collections.emptyList();
    }

    @Override
    public List<DRGElementCompiler> getDRGElementCompilers() {
        return Arrays.asList(new PositiveNegativeDTCompiler());
    }

}
