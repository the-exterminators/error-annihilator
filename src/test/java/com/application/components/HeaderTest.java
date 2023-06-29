package com.application.components;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderTest {
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateHeader() {
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


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[src='images/logo_big_mixed.svg']")));

        // Locate the image element using the src attribute value
        WebElement imageElement = driver.findElement(By.cssSelector("img[src='images/logo_big_mixed.svg']"));
        assertTrue(imageElement.isDisplayed());
        // Add more assertions as needed
    }

    @Test
    public void testCreateDrawer() {

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

        /*driver.get("http://localhost:8080"); // Replace with your actual URL

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-vaadin-text-field-12")));

        // Expand the toggle
        WebElement toggleButton = driver.findElement(By.xpath("//vaadin-drawer-toggle[@aria-expanded='false']")); // Replace "toggle-button" with the actual class name of the toggle button
        toggleButton.click();*/

        // Wait for the toggle to expand and the ticket search input to become visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[slot='input'][placeholder='Ticket Number']")));

        // Locate the ticket search input field
        WebElement ticketSearchInput = driver.findElement(By.cssSelector("input[slot='input'][placeholder='Ticket Number']"));
        assertTrue(ticketSearchInput.isDisplayed());
        // Add more assertions or interactions with the ticket search input as needed

        // Locate the search button
        WebElement searchButton = driver.findElement(By.cssSelector("vaadin-icon[slot='prefix'][icon='vaadin:search']"));
        assertTrue(searchButton.isDisplayed());
        // Add more assertions or interactions with the search button as needed

        // Locate the router links
        List<WebElement> routerLinks = driver.findElements(By.tagName("a"));
        assertEquals(7, routerLinks.size());

        // Locate the logout button
        WebElement logoutButton = driver.findElement(By.cssSelector("vaadin-button[tabindex='0'][role='button']"));
        assertTrue(logoutButton.isDisplayed());
    }
}

