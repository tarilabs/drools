package org.kie.dmn.core.pmml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.MiningField.UsageType;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.OpType;
import org.dmg.pmml.PMML;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.core.impl.DMNModelImpl;
import org.kie.dmn.feel.lang.types.BuiltInType;
import org.kie.dmn.feel.util.Either;
import org.kie.dmn.model.api.Import;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNImportPMMLInfo extends PMMLInfo<DMNPMMLModelInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(DMNImportPMMLInfo.class);

    private final Import i;

    public DMNImportPMMLInfo(Import i, Collection<DMNPMMLModelInfo> models) {
        super(models);
        this.i = i;
    }

    public static Either<Exception, DMNImportPMMLInfo> from(InputStream is, DMNModelImpl model, Import i) {
        try {
            PMML pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
            List<DMNPMMLModelInfo> models = new ArrayList<>();
            for (Model pm : pmml.getModels()) {
                MiningSchema miningSchema = pm.getMiningSchema();
                Map<String, DMNType> inputFields = new HashMap<>();
                miningSchema.getMiningFields()
                            .stream()
                            .filter(mf -> mf.getUsageType() == UsageType.ACTIVE)
                            .forEach(fn -> inputFields.put(fn.getName().getValue(), model.getTypeRegistry().unknown()));
                models.add(new DMNPMMLModelInfo(pm.getModelName(), inputFields));
            }

            DataDictionary dd = pmml.getDataDictionary();
            for (DataField df : dd.getDataFields()) {
                BuiltInType baseFeelType = BuiltInType.UNKNOWN;
                switch (df.getDataType()) {
                    case BOOLEAN:
                        baseFeelType = BuiltInType.BOOLEAN;
                        break;
                    case DOUBLE:
                    case FLOAT:
                    case INTEGER:
                        baseFeelType = BuiltInType.NUMBER;
                        break;
                    case STRING:
                        baseFeelType = BuiltInType.STRING;
                        break;
                    default:
                        LOG.warn("{}", df.getDataType());
                        break;
                }
                if (df.getOpType() == OpType.CATEGORICAL) {

                }
            }
            DMNImportPMMLInfo info = new DMNImportPMMLInfo(i, models);
            return Either.ofRight(info);
        } catch (Throwable e) {
            e.printStackTrace();
            return Either.ofLeft(new Exception(e));
        }
    }

    public String getImportName() {
        return i.getName();
    }

}
