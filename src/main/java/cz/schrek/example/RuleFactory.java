package cz.schrek.example;

import java.util.List;

public class RuleFactory {

    public static Rule PA_RULE = text -> text != null && text.startsWith("pa");

    public static Rule WA_RULE = text -> text != null && text.contains("we");

    public static List<Rule> getDefaultRules() {
        return List.of(PA_RULE, WA_RULE);
    }

}
