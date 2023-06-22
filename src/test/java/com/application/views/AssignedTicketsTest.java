package com.application.views;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
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
        options.addArguments("--headless"); // Run Chrome in headless mode
        driver = new ChromeDriver(options);
    }

    @After
    public void tearDown() {
        // Close the WebDriver after each test
        driver.quit();
    }

    @Test
    public void testAssignedTicketsView() {
        // Open the view's URL in the web driver
        driver.get("http://localhost:8080/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));

        // Find the title element and assert its text
        WebElement titleElement = driver.findElement(By.tagName("h1"));
        String titleText = titleElement.getText();
        assertEquals("Assigned Tickets", titleText);

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
}
