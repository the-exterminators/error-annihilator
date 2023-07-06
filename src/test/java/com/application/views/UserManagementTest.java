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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagementTest {

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
    public void testUserManagementView() {

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

        // Find and click on the "Management" to open Submenu
        WebDriverWait menu = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement managementLayout = menu.until(ExpectedConditions.visibilityOfElementLocated(By.className("management-submenu")));

        // Find and click on the "Management" link within the layout
        WebDriverWait waitClick = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement managementLink = waitClick.until(ExpectedConditions.elementToBeClickable(By.xpath(".//p[contains(text(), 'Management')]")));
        managementLink.click();

        // Find and click on the "User Management" link within the layout
        WebElement userManagementLink = managementLayout.findElement(By.xpath(".//vaadin-menu-bar-item[contains(text(), 'User Management')]"));
        userManagementLink.click();

        // Find the parent vaadin-vertical-layout element
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement verticalLayout = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-vertical-layout.content")));

        // Find the title element and assert its text
        WebElement titleElement = verticalLayout.findElement(By.tagName("h1"));
        String titleText = titleElement.getText();
        assertEquals("User Management", titleText);

        // Find the grid element and assert its presence
        WebElement gridElement = driver.findElement(By.tagName("vaadin-grid"));
        assertTrue(gridElement.isDisplayed());

        // Find all rows in the grid
        List<WebElement> rows = driver.findElements(By.cssSelector("vaadin-grid-row"));

        // Iterate over each row and check if all three columns are filled out
        for (WebElement row : rows) {
            WebElement usernameCell = row.findElement(By.cssSelector("td[part='cell body-cell first-column-cell']"));
            WebElement firstNameCell = row.findElement(By.cssSelector("td[part='cell body-cell']"));
            WebElement lastNameCell = row.findElement(By.cssSelector("td[part='cell body-cell'] + td"));
            WebElement emailCell = row.findElement(By.cssSelector("td[part='cell body-cell'] + td + td"));
            WebElement userRoleCell = row.findElement(By.cssSelector("td[part='cell body-cell last-column-cell']"));

            String usernameCellValue = usernameCell.getText();
            String firstNameCellValue = firstNameCell.getText();
            String lastNameCellValue = lastNameCell.getText();
            String emailCellValue = emailCell.getText();
            String userRoleCellValue = userRoleCell.getText();


            assertTrue(!usernameCellValue.isEmpty());
            assertTrue(!firstNameCellValue.isEmpty());
            assertTrue(!lastNameCellValue.isEmpty());
            assertTrue(!emailCellValue.isEmpty());
            assertTrue(!userRoleCellValue.isEmpty());

            // Click on the current row
            row.click();

            // Assert that the form is displayed
            WebElement formElement = driver.findElement(By.tagName("edit-user-form"));
            assertTrue(formElement.isDisplayed());

        }
    }

    //Filter Tests like in the AssignedTicketsTest should be added. Due to time constraints this has to be done at a later date.
}
