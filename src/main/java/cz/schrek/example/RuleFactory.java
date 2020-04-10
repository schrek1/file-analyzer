package cz.schrek.example;

import java.util.List;

public class RuleFactory {

    public static Rule PA_RULE = text -> {
        if (text == null) return false;
        return text.startsWith("pa");
    };
    public static Rule WA_RULE = text -> {
        if (text == null) return false;
        return text.contains("we");
    };

    public static List<Rule> getDefaultRules() {
        return List.of(PA_RULE, WA_RULE);
    }

}
