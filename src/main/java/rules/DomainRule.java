package rules;

import validator.EmailRule;

public class DomainRule implements EmailRule {
    @Override
    public boolean validate(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String domain = email.substring(email.indexOf("@") + 1);
        return domain.contains(".") && !domain.startsWith(".") && !domain.endsWith(".");
    }

    @Override
    public String getErrorMessage() {
        return "Email must have a valid domain with dot.";
    }
}