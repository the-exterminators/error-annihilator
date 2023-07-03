package com.application.views;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class CreateTicketTest {
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
    public void createTicketTest() {

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


        // Find and fill the ticket title field
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement ticketTitleField = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id^='input-vaadin-text-field']")));
        ticketTitleField.sendKeys("Sample Ticket");

        // Find and fill the description field
        WebElement descriptionField = driver.findElement(By.cssSelector("textarea[id^='textarea-vaadin-text-area']"));
        descriptionField.sendKeys("Sample Description");

        // Find and click the create button
        WebElement createButton = driver.findElement(By.cssSelector("vaadin-menu-bar-button[role='menuitem'] vaadin-menu-bar-item"));
        JavascriptExecutor createExecutor = (JavascriptExecutor) driver;
        createExecutor.executeScript("arguments[0].click();", createButton);
        createButton.click();

        // Wait for the notification to appear
        WebDriverWait waitNotification = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement notification = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-notification-card")));

        // Assert the notification message
        assertEquals("Ticket created successfully!", notification.getText());

        // Verify that the fields are cleared
        assertEquals("", ticketTitleField.getAttribute("value"));
        assertEquals("", descriptionField.getText());
    }

}
