package com.application.components;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

public class EditTicketFormTest {

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
    public void testEditTicketForm() {

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


        // Find all ticket elements
        List<WebElement> ticketElements = driver.findElements(By.cssSelector("tr[part='row']"));

        // Iterate over the ticket elements and click on each one
        for (WebElement ticketElement : ticketElements) {
            // Click on the ticket element to open the Edit Ticket Form
            ticketElement.click();

            // Wait for the Edit Ticket Form to appear (you may need to add a suitable wait here)
            WebDriverWait waitTicketForm = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitTicketForm.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#input-vaadin-text-field-139")));

            // Find the input fields and buttons
            WebElement typeField = driver.findElement(By.cssSelector("input#input-vaadin-text-field-139"));
            WebElement statusDropdown = driver.findElement(By.cssSelector("input#input-vaadin-combo-box-144"));
            WebElement assigneesDropdown = driver.findElement(By.cssSelector("input#input-vaadin-multi-select-combo-box-149"));
            WebElement saveButton = driver.findElement(By.cssSelector("vaadin-button[theme='primary']"));
            WebElement cancelButton = driver.findElement(By.cssSelector("vaadin-button[theme='tertiary']"));

            // Store the initial values
            String initialType = typeField.getAttribute("value");
            String initialStatus = statusDropdown.getAttribute("value");
            String initialAssignees = assigneesDropdown.getAttribute("value");

            // Make changes to the dropdowns
            typeField.sendKeys("TestBug");
            statusDropdown.sendKeys("Success");
            //will be completed when there are more users available
            // assigneesDropdown.sendKeys("John Doe");

            // Click the save button
            saveButton.click();

            // Verify that the values were updated
            String updatedType = typeField.getAttribute("value");
            String updatedStatus = statusDropdown.getAttribute("value");
            // String updatedAssignees = assigneesDropdown.getAttribute("value");

            assertEquals("TestBug", updatedType);
            assertEquals("Success", updatedStatus);
            //assertEquals("John Doe", updatedAssignees);

            // Optionally, you can also check if the initial values are not equal to the updated values
            assertTrue(!initialType.equals(updatedType));
            assertTrue(!initialStatus.equals(updatedStatus));
            //assertTrue(!initialAssignees.equals(updatedAssignees));

        }
    }
}
