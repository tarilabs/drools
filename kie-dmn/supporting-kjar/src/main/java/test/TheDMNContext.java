package test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.kie.dmn.feel.lang.FEELPropertyAccessible;
import org.kie.dmn.feel.util.EvalHelper.PropertyValueResult;

public class TheDMNContext implements FEELPropertyAccessible {

    private Person p;

    public Person getP() {
        return p;
    }

    public void setP(Person p) {
        this.p = p;
    }

    @Override
    public PropertyValueResult getFEELProperty(String property) {
        switch (property) {
            case "p":
                return PropertyValueResult.ofValue(getP());
            default:
                return PropertyValueResult.notDefined();
        }
    }

    @Override
    public Map<String, Object> allFEELProperties() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("p", getP());
        return result;
    }

}
