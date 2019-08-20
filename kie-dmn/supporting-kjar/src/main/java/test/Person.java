package test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.kie.dmn.feel.lang.FEELPropertyAccessible;
import org.kie.dmn.feel.util.EvalHelper.PropertyValueResult;

public class Person implements FEELPropertyAccessible {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public PropertyValueResult getFEELProperty(String property) {
        switch (property) {
            case "name":
                return PropertyValueResult.ofValue(getName());
            default:
                return PropertyValueResult.notDefined();
        }
    }

    @Override
    public Map<String, Object> allFEELProperties() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", getName());
        return result;
    }

}
