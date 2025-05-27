
package validator;

public interface EmailRule {
    boolean validate(String email);
    String getErrorMessage();
}