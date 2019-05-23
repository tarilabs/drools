package org.kie.dmn.core.pmml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.dmg.pmml.MiningField.UsageType;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;

public class PMMLInfo<M extends PMMLModelInfo> {

    protected final Collection<M> models;

    public PMMLInfo(Collection<M> models) {
        this.models = Collections.unmodifiableList(new ArrayList<>(models));
    }

    public static PMMLInfo<PMMLModelInfo> from(InputStream is) throws Exception {
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
        PMMLInfo<PMMLModelInfo> info = new PMMLInfo<>(models);
        return info;
    }

    public Collection<M> getModels() {
        return models;
    }
}
