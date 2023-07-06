package com.application.views;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserProfileTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Run Chrome in headless mode
        driver = new ChromeDriver(options);
    }

    @After
    public void tearDown() {
        // Close the WebDriver after each test
        driver.quit();
    }

    @Test
    public void testUserProfileView() {

        // Open the login page
        driver.get("http://localhost:8080/login");

        // Find the username and password input fields
        WebDriverWait waitLogIn = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameInput = waitLogIn.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='username']")));
        WebElement passwordInput = waitLogIn.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));

        // Enter the login credentials
        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("password");

        // Find and click the submit button
        WebElement submitButton = driver.findElement(By.cssSelector("vaadin-button[theme~='primary'][theme~='contained'][theme~='submit']"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", submitButton);

        // Wait for the redirect to the assigned tickets page
        // Find and click on the drawer toggle
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement drawerToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-drawer-toggle[aria-expanded='false']")));
        drawerToggle.click();

        // Find and click on the "My Profile" link
        WebDriverWait menu = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement projectOverviewLink = menu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-menu-bar-item[contains(., 'My Profile')]")));
        projectOverviewLink.click();

        // Find the form fields
        WebDriverWait fields = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement userNameField = fields.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-text-field input[aria-labelledby='label-vaadin-text-field-30']")));
        WebElement firstNameField = driver.findElement(By.cssSelector("vaadin-text-field input[aria-labelledby='label-vaadin-text-field-33']"));
        WebElement lastNameField = driver.findElement(By.cssSelector("vaadin-text-field input[aria-labelledby='label-vaadin-text-field-36']"));
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field input[aria-labelledby='label-vaadin-email-field-39']"));
        WebElement changePasswordField = driver.findElement(By.cssSelector("vaadin-password-field input[aria-labelledby='label-vaadin-password-field-42']"));

        // Verify all fields (except ChangePassword) are not empty
        Assert.assertFalse(userNameField.getAttribute("value").isEmpty());
        Assert.assertFalse(firstNameField.getAttribute("value").isEmpty());
        Assert.assertFalse(lastNameField.getAttribute("value").isEmpty());
        Assert.assertFalse(emailField.getAttribute("value").isEmpty());
        Assert.assertTrue(changePasswordField.getAttribute("value").isEmpty());


    }
}
