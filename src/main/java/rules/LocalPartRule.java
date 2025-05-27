package rules;

import validator.EmailRule;

public class LocalPartRule implements EmailRule {
    @Override
    public boolean validate(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String localPart = email.substring(0, email.indexOf("@"));
        return !localPart.isEmpty() && !localPart.startsWith(".") && !localPart.endsWith(".");
    }

    @Override
    public String getErrorMessage() {
        return "Email local part must not be empty or start/end with dot.";
    }
}