package rules;


import validator.EmailRule;

public class AtSignRule implements EmailRule {
    @Override
    public boolean validate(String email) {
        return email != null && email.contains("@") && email.indexOf("@") == email.lastIndexOf("@");
    }

    @Override
    public String getErrorMessage() {
        return "Email must contain exactly one @ symbol.";
    }
}