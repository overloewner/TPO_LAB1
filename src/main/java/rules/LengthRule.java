package rules;

import validator.EmailRule;

public class LengthRule implements EmailRule {
    @Override
    public boolean validate(String email) {
        return email != null && email.length() >= 5 && email.length() <= 254;
    }

    @Override
    public String getErrorMessage() {
        return "Email must be between 5 and 254 characters long.";
    }
}