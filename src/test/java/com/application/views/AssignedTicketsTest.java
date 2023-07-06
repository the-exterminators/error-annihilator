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

public class AssignedTicketsTest {
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
    public void testAssignedTicketsView() {

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

        // Find and click on the "Assigned Tickets" link
        WebDriverWait menu = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement assignedTicketsLink = menu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-menu-bar-item[contains(., 'My Assigned Tickets')]")));
        assignedTicketsLink.click();

        // Find the parent vaadin-vertical-layout element
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement verticalLayout = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-vertical-layout.assignedTickets-view")));


        // Find the title element and assert its text
        WebElement titleElement = verticalLayout.findElement(By.tagName("h1"));
        String titleText = titleElement.getText();
        assertEquals("My Assigned Tickets", titleText);

        // Find the grid element and assert its presence
        WebElement gridElement = driver.findElement(By.tagName("vaadin-grid"));
        assertTrue(gridElement.isDisplayed());

        // Find all rows in the grid
        List<WebElement> rows = driver.findElements(By.cssSelector("vaadin-grid-row"));

        // Iterate over each row and check if all three columns are filled out
        for (WebElement row : rows) {
            WebElement titleCell = row.findElement(By.cssSelector("td[part='cell body-cell first-column-cell']"));
            WebElement descriptionCell = row.findElement(By.cssSelector("td[part='cell body-cell']"));
            WebElement statusCell = row.findElement(By.cssSelector("td[part='cell body-cell last-column-cell']"));

            String titleCellValue = titleCell.getText();
            String descriptionCellValue = descriptionCell.getText();
            String statusCellValue = statusCell.getText();

            assertTrue(!titleCellValue.isEmpty());
            assertTrue(!descriptionCellValue.isEmpty());
            assertTrue(!statusCellValue.isEmpty());

            // Click on the current row
            row.click();

            // Assert that the form is displayed
            WebElement formElement = driver.findElement(By.tagName("edit-ticket-form"));
            assertTrue(formElement.isDisplayed());

        }
    }

    @Test
    public void testTitleFilter() {
        String filterValue = "test";

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

        // Find and click on the "Assigned Tickets" link
        WebDriverWait menu = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement assignedTicketsLink = menu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-menu-bar-item[contains(., 'My Assigned Tickets')]")));
        assignedTicketsLink.click();

        // Find the parent vaadin-vertical-layout element
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement verticalLayout = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-vertical-layout.assignedTickets-view")));


        // Find the title filter input element and enter the filter value
        WebElement titleFilterInput = driver.findElement(By.id("input-vaadin-text-field-51"));
        titleFilterInput.click();
        titleFilterInput.sendKeys(filterValue);

        // Retrieve the updated grid rows and perform assertions on the filtered results
        List<WebElement> filteredRows = driver.findElements(By.cssSelector("vaadin-grid-row"));

        // Assert that the filtered rows contain only the desired results based on the applied filter
        for (WebElement row : filteredRows) {
            // Perform assertions on the filtered rows as needed
            // For example, you can assert that the title in each row contains the filter value
            WebElement titleElement = row.findElement(By.cssSelector("vaadin-grid-cell-content:first-child"));
            String titleText = titleElement.getText();
            assertTrue(titleText.toLowerCase().contains(filterValue.toLowerCase()));
        }
    }

    @Test
    public void testNumberFilter() {
        String filterValue = "1";

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

        // Find and click on the "Assigned Tickets" link
        WebDriverWait menu = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement assignedTicketsLink = menu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-menu-bar-item[contains(., 'My Assigned Tickets')]")));
        assignedTicketsLink.click();

        // Find the parent vaadin-vertical-layout element
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement verticalLayout = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-vertical-layout.assignedTickets-view")));


        // Find the title filter input element and enter the filter value
        WebElement descriptionFilterInput = driver.findElement(By.id("input-vaadin-text-field-49"));
        descriptionFilterInput.click();
        descriptionFilterInput.sendKeys(filterValue);

        // Retrieve the updated grid rows and perform assertions on the filtered results
        List<WebElement> filteredRows = driver.findElements(By.cssSelector("vaadin-grid-row"));

        // Assert that the filtered rows contain only the desired results based on the applied filter
        for (WebElement row : filteredRows) {
            // Perform assertions on the filtered rows as needed
            // For example, you can assert that the title in each row contains the filter value
            WebElement titleElement = row.findElement(By.cssSelector("vaadin-grid-cell-content:first-child"));
            String titleText = titleElement.getText();
            assertTrue(titleText.toLowerCase().contains(filterValue.toLowerCase()));
        }
    }

    @Test
    public void testStatusFilter() {
        String filterValue = "resolved";

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

        // Find and click on the "Assigned Tickets" link
        WebDriverWait menu = new WebDriverWait(driver,Duration.ofSeconds(10));
        WebElement assignedTicketsLink = menu.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-menu-bar-item[contains(., 'My Assigned Tickets')]")));
        assignedTicketsLink.click();

        // Find the parent vaadin-vertical-layout element
        WebDriverWait waitView = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        WebElement verticalLayout = waitView.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-vertical-layout.assignedTickets-view")));


        // Find the title filter input element and enter the filter value
        WebElement statusFilterInput = driver.findElement(By.id("input-vaadin-combo-box-55"));
        statusFilterInput.click();
        statusFilterInput.sendKeys(filterValue);

        // Retrieve the updated grid rows and perform assertions on the filtered results
        List<WebElement> filteredRows = driver.findElements(By.cssSelector("vaadin-grid-row"));

        // Assert that the filtered rows contain only the desired results based on the applied filter
        for (WebElement row : filteredRows) {
            // Perform assertions on the filtered rows as needed
            // For example, you can assert that the title in each row contains the filter value
            WebElement titleElement = row.findElement(By.cssSelector("vaadin-grid-cell-content:first-child"));
            String titleText = titleElement.getText();
            assertTrue(titleText.toLowerCase().contains(filterValue.toLowerCase()));
        }
    }

    //Test for further filters still need to be implemented
}
