package validator;

import java.util.ArrayList;
import java.util.List;

public class EmailValidator {
    private final List<EmailRule> rules;

    public EmailValidator(List<EmailRule> rules) {
        this.rules = rules;
    }

    public boolean validate(String email) {
        return rules.stream().allMatch(rule -> rule.validate(email));
    }

    public List<String> getValidationMessages(String email) {
        List<String> messages = new ArrayList<>();
        for (EmailRule rule : rules) {
            if (!rule.validate(email)) {
                messages.add(rule.getErrorMessage());
            }
        }
        return messages;
    }
}