package com.application.components;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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

/*public class HeaderTest {
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
        driver.get("http://localhost:8080"); // Replace with your actual URL

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[src='images/logo_big_mixed.svg']")));

        // Locate the image element using the src attribute value
        WebElement imageElement = driver.findElement(By.cssSelector("img[src='images/logo_big_mixed.svg']"));
        assertTrue(imageElement.isDisplayed());
        // Add more assertions as needed
    }

    @Test
    public void testCreateDrawer() {
        driver.get("http://localhost:8080"); // Replace with your actual URL

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-vaadin-text-field-12")));

        // Expand the toggle
        WebElement toggleButton = driver.findElement(By.xpath("//vaadin-drawer-toggle[@aria-expanded='false']")); // Replace "toggle-button" with the actual class name of the toggle button
        toggleButton.click();

        // Wait for the toggle to expand and the ticket search input to become visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-vaadin-text-field-12")));

        // Locate the ticket search input field
        WebElement ticketSearchInput = driver.findElement(By.id("input-vaadin-text-field-12"));
        assertTrue(ticketSearchInput.isDisplayed());
        // Add more assertions or interactions with the ticket search input as needed

        // Locate the search button
        WebElement searchButton = driver.findElement(By.cssSelector("vaadin-icon[slot='prefix'][icon='vaadin:search']"));
        assertTrue(searchButton.isDisplayed());
        // Add more assertions or interactions with the search button as needed

        // Locate the router links
        List<WebElement> routerLinks = driver.findElements(By.tagName("a"));
        assertEquals(3, routerLinks.size());

        // Locate the logout button
        WebElement logoutButton = driver.findElement(By.cssSelector("vaadin-button[tabindex='0'][role='button']"));
        assertTrue(logoutButton.isDisplayed());
    }
}*/

