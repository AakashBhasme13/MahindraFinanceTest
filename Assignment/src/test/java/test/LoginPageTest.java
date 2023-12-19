package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginPageTest {

	public WebDriver driver;
	public String loginUrl = "login page url";

	public LoginPageTest(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "username")
	WebElement usernameInput;

	@FindBy(id = "password")
	WebElement passwordInput;

	@FindBy(id = "loginButton")
	WebElement loginButton;

	@FindBy(id = "errorMessage")
	WebElement errorMessage;

	@FindBy(id = "forgotPasswordLink")
	WebElement forgotPasswordLink;

	@FindBy(id = "email")
	WebElement email;

	@FindBy(id = "recoverButton")
	WebElement recoverButton;

	@FindBy(id = "successMessage")
	WebElement successMessage;

	@BeforeClass
	public void setUp() {
		// Set up WebDriver
		System.setProperty("webdriver.chrome.driver", "chromedriver path");
		driver = new ChromeDriver();
		driver.get(loginUrl);
	}

	@AfterClass
	public void tearDown() {
		// Close the WebDriver
		if (driver != null) {
			driver.quit();
		}
	}

	@Test(priority = 1)
	public void testSuccessfulLogin() {
		// Test Case 1: Successful Login

		usernameInput.sendKeys("validUsername");
		passwordInput.sendKeys("validPassword");
		loginButton.click();

		// Assuming successful login redirects to a dashboard page
		String actualUrl = driver.getCurrentUrl();
		String expectedUrl = "expected_dashboard_url";
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	@Test(priority = 2)
	public void testInvalidLogin() {
		// Test Case 2: Invalid Login

		usernameInput.sendKeys("invalidUsername");
		passwordInput.sendKeys("invalidPassword");
		loginButton.click();

		Assert.assertTrue(errorMessage.isDisplayed());
	}

	@Test(priority = 3)
	public void testRetainedFieldsAfterFailedLogin() {
		// Test Case 3: Retained Fields After Failed Login
		// Enter invalid credentials, click login, and assert fields are retained

		usernameInput.sendKeys("invalidUsername");
		passwordInput.sendKeys("invalidPassword");
		loginButton.click();

		// Verify that username and password fields retain their values
		String actualUsername = usernameInput.getAttribute("value");
		String actualPassword = passwordInput.getAttribute("value");

		Assert.assertEquals(actualUsername, "invalidUsername");
		Assert.assertEquals(actualPassword, "invalidPassword");
	}

	@Test(priority = 4)
	public void testLoginButtonDisabled() {
		// Test Case 4: Login Button Disabled when fields are empty
		// Assuming fields are empty
		Assert.assertFalse(loginButton.isEnabled());
	}

	@Test(priority = 5)
	public void testLoginButtonEnabled() {
		// Test Case 5: Login Button Enabled when fields are filled with valid input
		// Assuming valid input
		usernameInput.sendKeys("validUsername");
		passwordInput.sendKeys("validPassword");

		Assert.assertTrue(loginButton.isEnabled());
	}

	@Test(priority = 6)
	public void testForgotPasswordLink() {
		// Test Case 6: Forgot Password Link redirection to the recovery page

		forgotPasswordLink.click();

		// Assuming the URL changes after clicking the link
		String actualUrl = driver.getCurrentUrl();
		String expectedUrl = "expected_recovery_page_url";
		Assert.assertEquals(actualUrl, expectedUrl);
	}

	@Test(priority = 7)
	public void testPasswordRecovery() {
		// Test Case 7: Password recovery functionality

		forgotPasswordLink.click();
		email.sendKeys("email id");
		recoverButton.click();
		String actualMessage = successMessage.getText();
		String expectedMessage = "An email with password recovery instructions has been sent";

		Assert.assertEquals(actualMessage, expectedMessage);

		// Assume that user successfully recovered password by accessing entered email id

		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, loginUrl);

		usernameInput.sendKeys("validUsername");
		passwordInput.sendKeys("validPassword");
		loginButton.click();

		// Assuming successful login redirects to a dashboard page
		String currentUrl = driver.getCurrentUrl();
		String expectedUrl = "expected_dashboard_url";
		Assert.assertEquals(currentUrl, expectedUrl);

	}

}