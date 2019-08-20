package test;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kie.dmn.feel.lang.FEELPropertyAccessible;
import org.kie.dmn.feel.util.EvalHelper.PropertyValueResult;

/**
 * MEGADUMMY
 */
public class TheDMNContext2 implements FEELPropertyAccessible {

    private Person p;

    @JsonbProperty("matteo 2")
    @Schema(name = "matteo 2")
    private Map d;

    public Person getP() {
        return p;
    }

    public void setP(Person p) {
        this.p = p;
    }
    
    public Map getD() {
        return d;
    }

    public void setD(Map d) {
        this.d = d;
    }

    @Override
    public PropertyValueResult getFEELProperty(String property) {
        switch (property) {
            case "p":
                return PropertyValueResult.ofValue(getP());
            case "d":
                return PropertyValueResult.ofValue(getD());
            default:
                return PropertyValueResult.notDefined();
        }
    }

    @Override
    public Map<String, Object> allFEELProperties() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("p", getP());
        result.put("d", getP());
        return result;
    }

}
