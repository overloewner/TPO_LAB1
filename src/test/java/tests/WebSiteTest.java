package tests;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class WebSiteTest extends BaseTest {

    @Test
    @DisplayName("1. Проверка заголовка главной страницы")
    public void testPageTitle() {
        driver.get(BASE_URL);
        String expectedTitle = "The Internet";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle, "Заголовок страницы не соответствует ожидаемому");
    }

    @Test
    @DisplayName("2. Проверка видимости основных элементов")
    public void testElementsVisibility() {
        driver.get(BASE_URL);

        // Проверяем видимость заголовка
        WebElement heading = driver.findElement(By.tagName("h1"));
        assertTrue(heading.isDisplayed(), "Главный заголовок не отображается");
        assertEquals("Welcome to the-internet", heading.getText());

        // Проверяем видимость списка ссылок
        WebElement linksList = driver.findElement(By.tagName("ul"));
        assertTrue(linksList.isDisplayed(), "Список ссылок не отображается");
    }

    @Test
    @DisplayName("3. Переход по ссылке на страницу логина")
    public void testNavigationToLoginPage() {
        driver.get(BASE_URL);

        WebElement loginLink = driver.findElement(By.linkText("Form Authentication"));
        loginLink.click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/login"), "Переход на страницу логина не выполнен");

        WebElement loginForm = driver.findElement(By.id("login"));
        assertTrue(loginForm.isDisplayed(), "Форма логина не отображается");
    }

    @Test
    @DisplayName("4. Заполнение текстовых полей формы логина")
    public void testFormFieldsFilling() {
        driver.get(BASE_URL + "/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));

        String testUsername = "tomsmith";
        String testPassword = "SuperSecretPassword!";

        usernameField.sendKeys(testUsername);
        passwordField.sendKeys(testPassword);

        assertEquals(testUsername, usernameField.getAttribute("value"), "Имя пользователя введено некорректно");
        assertEquals(testPassword, passwordField.getAttribute("value"), "Пароль введен некорректно");
    }

    @Test
    @DisplayName("5. Эмуляция нажатия кнопки входа")
    public void testLoginButtonClick() {
        driver.get(BASE_URL + "/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        WebElement loginButton = driver.findElement(By.className("radius"));
        loginButton.click();

        // Ожидаем появления сообщения об успешном входе
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flash")));
        assertTrue(successMessage.getText().contains("You logged into a secure area!"));
    }

    @Test
    @DisplayName("6. Проверка отображения сообщения об ошибке при неверных данных")
    public void testInvalidLoginError() {
        driver.get(BASE_URL + "/login");

        driver.findElement(By.id("username")).sendKeys("invaliduser");
        driver.findElement(By.id("password")).sendKeys("invalidpass");
        driver.findElement(By.className("radius")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flash")));
        assertTrue(errorMessage.getText().contains("Your username is invalid!"));
    }

    @Test
    @DisplayName("7. Проверка работы выпадающего списка")
    public void testDropdownFunctionality() {
        driver.get(BASE_URL + "/dropdown");

        WebElement dropdown = driver.findElement(By.id("dropdown"));
        Select select = new Select(dropdown);

        // Проверяем начальное состояние
        assertEquals("Please select an option", select.getFirstSelectedOption().getText());

        // Выбираем Option 1
        select.selectByVisibleText("Option 1");
        assertEquals("Option 1", select.getFirstSelectedOption().getText());

        // Выбираем Option 2
        select.selectByValue("2");
        assertEquals("Option 2", select.getFirstSelectedOption().getText());
    }

    @Test
    @DisplayName("8. Проверка работы чекбоксов")
    public void testCheckboxesFunctionality() {
        driver.get(BASE_URL + "/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        assertEquals(2, checkboxes.size(), "Должно быть 2 чекбокса");

        WebElement firstCheckbox = checkboxes.get(0);
        WebElement secondCheckbox = checkboxes.get(1);

        // Первый чекбокс изначально не отмечен
        assertFalse(firstCheckbox.isSelected(), "Первый чекбокс должен быть не отмечен");

        // Второй чекбокс изначально отмечен
        assertTrue(secondCheckbox.isSelected(), "Второй чекбокс должен быть отмечен");

        // Отмечаем первый чекбокс
        firstCheckbox.click();
        assertTrue(firstCheckbox.isSelected(), "Первый чекбокс должен быть отмечен после клика");
    }

    @Test
    @DisplayName("9. Проверка наличия и содержимого футера")
    public void testFooterContent() {
        driver.get(BASE_URL);

        WebElement footer = driver.findElement(By.id("page-footer"));
        assertTrue(footer.isDisplayed(), "Футер не отображается");

        WebElement footerText = footer.findElement(By.tagName("div"));
        assertTrue(footerText.getText().contains("Powered by"), "Текст футера не содержит ожидаемую информацию");
    }


    @Test
    @DisplayName("11. Проверка работы кнопки выхода после входа")
    public void testLogoutFunctionality() {
        // Сначала входим в систему
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();

        // Проверяем наличие кнопки выхода
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout")));
        assertTrue(logoutButton.isDisplayed(), "Кнопка выхода не отображается");

        // Нажимаем кнопку выхода
        logoutButton.click();

        // Проверяем, что мы вернулись на страницу логина
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flash")));
        assertTrue(successMessage.getText().contains("You logged out of the secure area!"));
    }

    @Test
    @DisplayName("12. Проверка загрузки страницы с динамическим контентом")
    public void testDynamicContentPage() {
        driver.get(BASE_URL + "/dynamic_content");

        // Проверяем наличие контейнеров с контентом
        List<WebElement> contentRows = driver.findElements(By.className("large-10"));
        assertTrue(!contentRows.isEmpty(), "Динамический контент не загружен");

        // Проверяем наличие изображений
        List<WebElement> images = driver.findElements(By.tagName("img"));
        assertTrue(!images.isEmpty(), "Изображения не загружены");

        // Проверяем, что первое изображение отображается
        WebElement firstImage = images.get(0);
        assertTrue(firstImage.isDisplayed(), "Первое изображение не отображается");
    }
}