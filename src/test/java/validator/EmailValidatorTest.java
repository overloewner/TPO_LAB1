package validator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rules.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailValidatorTest {

    @Test
    void validEmailShouldPass() {
        List<EmailRule> rules = List.of(
                new LengthRule(),
                new AtSignRule(),
                new DomainRule(),
                new LocalPartRule(),
                new NoSpacesRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertTrue(validator.validate("test@example.com"));
    }

    @Test
    void invalidEmailShouldFail() {
        List<EmailRule> rules = List.of(
                new AtSignRule(),
                new DomainRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertFalse(validator.validate("invalid-email")); // no @ symbol
    }

    @Test
    void getValidationMessagesShouldReturnErrors() {
        List<EmailRule> rules = List.of(
                new LengthRule(),
                new AtSignRule(),
                new DomainRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        List<String> messages = validator.getValidationMessages("ab");

        assertEquals(3, messages.size());
        assertTrue(messages.contains("Email must be between 5 and 254 characters long."));
        assertTrue(messages.contains("Email must contain exactly one @ symbol."));
        assertTrue(messages.contains("Email must have a valid domain with dot."));
    }

    @Test
    void mockRuleTest() {
        EmailRule mockRule = Mockito.mock(EmailRule.class);
        when(mockRule.validate(anyString())).thenReturn(false);
        when(mockRule.getErrorMessage()).thenReturn("Mock error");

        EmailValidator validator = new EmailValidator(List.of(mockRule));
        assertFalse(validator.validate("test@example.com"));
        assertEquals(List.of("Mock error"), validator.getValidationMessages("test@example.com"));

        verify(mockRule, times(2)).validate("test@example.com");
    }

    @Test
    void atSignRuleShouldValidateCorrectly() {
        AtSignRule rule = new AtSignRule();
        assertTrue(rule.validate("test@example.com"));
        assertFalse(rule.validate("testexample.com"));
        assertFalse(rule.validate("test@@example.com"));
        assertEquals("Email must contain exactly one @ symbol.", rule.getErrorMessage());
    }

    @Test
    void domainRuleShouldValidateCorrectly() {
        DomainRule rule = new DomainRule();
        assertTrue(rule.validate("test@example.com"));
        assertFalse(rule.validate("test@example"));
        assertFalse(rule.validate("test@.example.com"));
        assertEquals("Email must have a valid domain with dot.", rule.getErrorMessage());
    }

    @Test
    void lengthRuleShouldValidateCorrectly() {
        LengthRule rule = new LengthRule();
        assertTrue(rule.validate("a@b.c"));
        assertFalse(rule.validate("a@b"));
        assertEquals("Email must be between 5 and 254 characters long.", rule.getErrorMessage());
    }

    @Test
    void localPartRuleShouldValidateCorrectly() {
        LocalPartRule rule = new LocalPartRule();
        assertTrue(rule.validate("test@example.com"));
        assertFalse(rule.validate("@example.com"));
        assertFalse(rule.validate(".test@example.com"));
        assertEquals("Email local part must not be empty or start/end with dot.", rule.getErrorMessage());
    }

    @Test
    void noSpacesRuleShouldValidateCorrectly() {
        NoSpacesRule rule = new NoSpacesRule();
        assertTrue(rule.validate("test@example.com"));
        assertFalse(rule.validate("test @example.com"));
        assertEquals("Email must not contain spaces.", rule.getErrorMessage());
    }

    @Test
    void nullEmailShouldBeInvalidForAllRules() {
        List<EmailRule> rules = List.of(
                new LengthRule(),
                new AtSignRule(),
                new DomainRule(),
                new LocalPartRule(),
                new NoSpacesRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertFalse(validator.validate(null));

        List<String> messages = validator.getValidationMessages(null);
        assertEquals(5, messages.size());
    }

    @Test
    void emptyEmailShouldBeInvalidForMostRules() {
        List<EmailRule> rules = List.of(
                new LengthRule(),
                new AtSignRule(),
                new DomainRule(),
                new LocalPartRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertFalse(validator.validate(""));

        List<String> messages = validator.getValidationMessages("");
        assertEquals(4, messages.size());
    }

    @Test
    void emailValidatorWithEmptyRulesShouldAlwaysPass() {
        EmailValidator validator = new EmailValidator(List.of());
        assertTrue(validator.validate("anyemail@test.com"));
        assertTrue(validator.validate(null));
        assertTrue(validator.validate(""));
        assertTrue(validator.getValidationMessages("any").isEmpty());
    }

    @Test
    void complexValidEmailShouldPass() {
        List<EmailRule> rules = List.of(
                new LengthRule(),
                new AtSignRule(),
                new DomainRule(),
                new LocalPartRule(),
                new NoSpacesRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertTrue(validator.validate("user.name@domain.co.uk"));
        assertTrue(validator.validate("test123@example.org"));
    }

    @Test
    void complexInvalidEmailShouldFail() {
        List<EmailRule> rules = List.of(
                new AtSignRule(),
                new DomainRule(),
                new LocalPartRule()
        );
        EmailValidator validator = new EmailValidator(rules);
        assertFalse(validator.validate("user@"));
        assertFalse(validator.validate("@domain.com"));
        assertFalse(validator.validate("user@domain"));
    }
}