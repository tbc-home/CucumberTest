package annotation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import common.CommonLib;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class annotation {
	WebDriver driver = null;

	@Given("^Verify login page$")
	public void openBrowser() {
		if (driver == null) {
			driver = CommonLib.GetDriver();
		}
		driver.navigate().to("https://www.linkedin.com/");
	}

	@When("^I enter username as \"(.*)\"$")
	public void enterUsername(String arg1) {
		driver.findElement(By.id("login-email")).sendKeys(arg1);
	}

	@And("^I enter password as \"(.*)\"$")
	public void enterPassword(String arg1) {
		driver.findElement(By.id("login-password")).sendKeys(arg1);
		driver.findElement(By.id("login-submit")).click();
	}

	@Then("^Login should fail$")
	public void checkFail() {
		if (driver.getCurrentUrl().equalsIgnoreCase("https://www.linkedin.com/uas/login-submit")) {
			// CommonLib.staticWait(30);
			System.out.println("Test1 Pass");

		} else {
			System.out.println("Test1 Failed");
		}

	}

	@But("^Relogin option should be available$")
	public void checkRelogin() {
		if (driver.getCurrentUrl().equalsIgnoreCase("https://www.linkedin.com/uas/login-submit1")) {
			// CommonLib.getElement(By.xpath("//span[contains(@id,'login-error')]")).getText()
			// .contains("Please enter a valid email address.");
			// CommonLib.staticWait(30);
			// driver.navigate().refresh();
			System.out.println("Test2 Pass");
		} else {
			System.out.println("Test2 Failed");
		}
		// driver.close();
	}
}