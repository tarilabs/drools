package org.kie.dmn.pmml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.dmg.pmml.MiningField.UsageType;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;

public class PMMLModelInfo {

    protected final String name;
    private final Collection<String> inputFieldNames;

    public PMMLModelInfo(String name, Collection<String> inputFieldNames) {
        this.name = name;
        this.inputFieldNames = Collections.unmodifiableList(new ArrayList<>(inputFieldNames));
    }

    public static PMMLInfo from(InputStream is) throws Exception {
        PMML pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
        List<PMMLModelInfo> models = new ArrayList<>();
        for (Model pm : pmml.getModels()) {
            MiningSchema miningSchema = pm.getMiningSchema();
            Collection<String> inputFields = new ArrayList<>();
            miningSchema.getMiningFields()
                        .stream()
                        .filter(mf -> mf.getUsageType() == UsageType.ACTIVE)
                        .forEach(fn -> inputFields.add(fn.getName().getValue()));
            models.add(new PMMLModelInfo(pm.getModelName(), inputFields));
        }
        PMMLInfo info = new PMMLInfo(models);
        return info;
    }

    public String getName() {
        return name;
    }

    public Collection<String> getInputFieldNames() {
        return inputFieldNames;
    }

}
