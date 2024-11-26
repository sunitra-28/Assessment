package com;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Assesment {
	WebDriver driver;
	WebDriverWait wait;

	// Initialize WebDriver
	@BeforeTest
	public void beforeTest() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Open the web browser and navigate to FitPeo Homepage.
		driver.get("https://www.fitpeo.com/");
	}

	@Test
	public void test() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);
		// From the homepage, navigate to the Revenue Calculator Page.
		WebElement calculator = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Revenue Calculator')]")));
		calculator.click();
		// Scroll down the page until the revenue calculator slider is visible.
		WebElement sliderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//div[@class='MuiBox-root css-j7qwjs']//following-sibling::span[2]")));
		js.executeScript("window.scrollBy(0, 250)");
		// Adjust the slider
		WebElement slider1 = driver.findElement(By.xpath("//*[@class=\"MuiSlider-track css-10opxo5\"]"));
		actions.dragAndDropBy(slider1, 108, 0).build().perform();
		// Update the Text Field
		WebElement slidetextbox = driver.findElement(By.id(":r0:"));
		slidetextbox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, "560");

		wait.until(ExpectedConditions.attributeToBe(slidetextbox, "value", "560"));

		// Validate the updated slider position
		WebElement sliderThumb = driver.findElement(By.xpath("//*[contains(@class,'MuiSlider-thumb')]"));
		int updatedPosition = sliderThumb.getLocation().getX();
		System.out.println("Updated slider position: " + updatedPosition);

		slidetextbox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, "820");

		wait.until(ExpectedConditions.attributeToBe(slidetextbox, "value", "820"));

		// Select CPT Codes:
		js.executeScript("window.scrollBy(0, 500)");
		selectCPTCode(1);
		selectCPTCode(2);
		selectCPTCode(3);
		selectCPTCode(8);

		// Validate Total Recurring Reimbursement
		WebElement totalReimbursement = driver.findElement(By.xpath("//div[@class='MuiBox-root css-m1khva']"));
		String totalAmount = totalReimbursement.getText();
		validateTotalReimbursement(totalAmount);
	}

	// method for select CPT codes
	private void selectCPTCode(int index) {
		WebElement cptCodeCheckbox = driver
				.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[" + index + "]"));
		if (!cptCodeCheckbox.isSelected()) {
			cptCodeCheckbox.click();
		}
	}

	// Validate method Total Recurring Reimbursement:
	private void validateTotalReimbursement(String totalAmount) {
		if (totalAmount.contains("$110700")) {
			System.out.println("Total Recurring Reimbursement is correctly displayed: " + totalAmount);
		} else {
			System.out.println("Total Recurring Reimbursement is incorrect: " + totalAmount);
		}
	}

	@AfterTest
	public void afterTest() {
		driver.quit();
	}
}
