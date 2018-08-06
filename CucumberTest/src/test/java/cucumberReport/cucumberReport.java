package cucumberReport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import common.CommonLib;
import common.Config;
import common.CustomReporter;
import common.CustomReporter.status;
import common.MenuNavigation;
import common.TableActions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class cucumberReport {
	WebDriver driver = null;

	@Given("^Verify login page open$")
	public void openBrowser() {
		if (driver == null) {
			driver = CommonLib.GetDriver();
		}
		// driver.navigate().to(Config.props.getProperty("URL"));
	}

	@When("Login controls are avaliable")
	public void login_controls_are_avaliable() {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
	}

	// @When("^Login controls are avaliable$")
	// public void goToFacebook() {
	//
	// }

	@Then("^Login sucessfully$")
	public void loginButton() {
		CustomReporter.initiateTest("Verify Login to system");
		if (driver.findElement(By.id(Config.Repository.getProperty("UserID"))).isDisplayed()) {
			CommonLib.setValue(Config.Repository.getProperty("UserID"), "", "automation");
			CustomReporter.MessageLogger("Login User Set.", CustomReporter.status.Pass);
		} else {
			CustomReporter.MessageLogger("Failed to set Login User.", CustomReporter.status.Fail);
		}
		if (driver.findElement(By.id(Config.Repository.getProperty("txtPassword"))).isDisplayed()) {
			CommonLib.setValue(Config.Repository.getProperty("txtPassword"), "", "automation");
			CustomReporter.MessageLogger("Login password Set.", CustomReporter.status.Pass);
		} else {
			CustomReporter.MessageLogger("Failed to set Login password.", CustomReporter.status.Fail);
		}
		CommonLib.clickButton(By.id(Config.Repository.getProperty("cmdSignIn")));
	}

	@And("^click on sign in$")
	public void clickSignIn() {
		CommonLib.clickButton(By.id(Config.Repository.getProperty("cmdSignIn")));
	}

	@Then("^Sign to facility sucessfully$")
	public void forgotPWD() {
		CustomReporter.initiateTest("Sign-in to facility");
		try {
			CommonLib.selectfrmDropdwn("cboFacility", -1, "", Config.props.getProperty("Facility"));

			CommonLib.clickButton(By.id("cmdFacSel"));

			CustomReporter.MessageLogger("Sucessfully log into the facility.", CustomReporter.status.Pass);
		} catch (Exception e) {
			CustomReporter.MessageLogger("Test 1 Fail", CustomReporter.status.Fail);
		}
		if (!(Config.props.getProperty("user").equals("automation"))) {
			CommonLib.securityUser = true;
		} else {
			CommonLib.securityUser = false;
		}

		MenuNavigation.menuNav("Licensing");

		int index = new TableActions().listIndex(By.xpath("//*[contains(@id,'grdDataVersion_lblFacCode_')]"),
				Config.props.getProperty("Facility").split(" - ")[0]);
		CommonLib.setVersion(CommonLib.getText(Config.Repository.getProperty("grdDataVersion_lblVersion_") + index));
		CustomReporter.MessageLogger("Application version save successfully", status.Pass);

		MenuNavigation.menuNav("SelectPatient");

	}
}
