package rules;

import validator.EmailRule;

public class NoSpacesRule implements EmailRule {
    @Override
    public boolean validate(String email) {
        return email != null && !email.contains(" ");
    }

    @Override
    public String getErrorMessage() {
        return "Email must not contain spaces.";
    }
}