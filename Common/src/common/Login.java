package common;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import common.CustomReporter.status;

public class Login {

	@BeforeMethod
	@AfterMethod
	public void initlogin(String username, String pswd, boolean flag) {
		if (Config.props.getProperty("SystemUnderTest").equals("NetSolution")) {

			CommonLib.setValue(Config.Repository.getProperty("UserID"), "", username);
			CommonLib.setValue(Config.Repository.getProperty("txtPassword"), "", pswd);
			CommonLib.clickButton(By.id(Config.Repository.getProperty("cmdSignIn")));
			try {
				CommonLib.selectfrmDropdwn("cboFacility", -1, "", Config.props.getProperty("Facility"));
				if (Config.props.getProperty("Version").equals("Old")) {
					CommonLib.clickButton(By.id(Config.Repository.getProperty("cmdSignIn")));
				} else {
					CommonLib.clickButton(By.id("cmdFacSel"));
				}
			} catch (Exception e) {

			}
			CustomReporter.MessageLogger("Logged into the application successfully", status.Pass);

			if (!(Config.props.getProperty("user").equals(username))) {
				CommonLib.securityUser = true;
			} else {
				CommonLib.securityUser = false;
			}
			if (flag == true) {
				MenuNavigation.menuNav("Licensing");

				int index = new TableActions().listIndex(By.xpath("//*[contains(@id,'grdDataVersion_lblFacCode_')]"), Config.props.getProperty("Facility").split(" - ")[0]);
				CommonLib.setVersion(CommonLib.getText(Config.Repository.getProperty("grdDataVersion_lblVersion_") + index));
				CustomReporter.MessageLogger("Application version save successfully", status.Pass);
			}
			MenuNavigation.menuNav("SelectPatient");

		} else if (Config.props.getProperty("SystemUnderTest").equals("RCM")) {
			// CommonLib.clickButton(By.id("button.Version"));
			try {

				HashTableRepository.setHash("MainWindow", CommonLib.GetDriver().getWindowHandle().toString());

				// Alert alert = CommonLib.GetDriver().switchTo().alert();
				// String strVersion = alert.getText();
				// strVersion = strVersion.replace("Optimum Version
				// Information", "");
				// strVersion = strVersion.replaceFirst(" ", "");
				// strVersion = strVersion.replaceAll("\n", ";");
				// strVersion = strVersion.replaceAll(";;", "");
				// strVersion = strVersion.replaceAll(";", ";<BR>");
				// alert.accept();
				// CommonLib.setVersion(strVersion);
			} catch (Exception e) {
			}
			// RCM Login
			CommonLib.setValue("", "//input[@name='userid']", username);
			CommonLib.setValue("", "//input[@name='password']", pswd);
			CommonLib.clickButton(By.id("button.Submit"));
			CommonLib.setFrame(Config.props.getProperty("MainFrame"));
			try {
				CommonLib.LinkClick(By.linkText("000190 QA02_Site1"));
				HashTableRepository.setHash("MainWindow", CommonLib.GetDriver().getWindowHandle().toString());
				try {
					String windowLocator = "";
					windowLocator = CommonLib.getLatestWindowHandle();
					if (!(windowLocator.equals(""))) {
						CommonLib.switchWindow(windowLocator);
						CommonLib.GetDriver().close();
					}
					CommonLib.switchWindow(HashTableRepository.getHash("MainWindow"));
				} catch (Exception e) {

				}
			} catch (Exception e) {

			}
		} else if (Config.props.getProperty("SystemUnderTest").equals("LAB")) {

			// LAB Login
			CommonLib.setValue("", "//input[@name='j_username']", username);
			CommonLib.setValue("", "//input[@name='j_password']", pswd);
			CommonLib.selectfrmDropdwn("txtCompanyNumber", -1, "", Config.props.getProperty("Facility"));
			CommonLib.clickButton(By.id("button.submit"));
			try {
				String windowLocator = "";
				windowLocator = CommonLib.getLatestWindowHandle();
				if (!(windowLocator.equals(""))) {
					CommonLib.switchWindow(windowLocator);
					// CommonLib.clickButton(By.id("cmdCancel"));
					// CommonLib.OperateInWindow(By.id("cmdCancel"));
					CommonLib.GetDriver().close();
				}
				CommonLib.switchWindow(HashTableRepository.getHash("MainWindow"));
			} catch (Exception e) {

			}
		} else {

			if (Config.props.getProperty("Environment").equals("Framework")) {
				CommonLib.clickButton(By.id("button.Version"));
				try {
					Alert alert = CommonLib.GetDriver().switchTo().alert();
					String strVersion = alert.getText();
					strVersion = strVersion.replace("Optimum Version Information", "");
					strVersion = strVersion.replaceFirst(" ", "");
					strVersion = strVersion.replaceAll("\n", ";");
					strVersion = strVersion.replaceAll(";;", "");
					strVersion = strVersion.replaceAll(";", ";<BR>");
					alert.accept();
					CommonLib.setVersion(strVersion);
				} catch (Exception e) {
				}
				HashTableRepository.setHash("MainWindow", CommonLib.GetDriver().getWindowHandle().toString());
				CommonLib.setValue("", "//input[@name='userid']", username);
				CommonLib.setValue("", "//input[@name='password']", pswd);
				CommonLib.selectfrmDropdwn("", 2, "//select[@name='company']", Config.props.getProperty("Company"));
				try {
					CommonLib.clickButton(By.id("button.Submit"));
				} catch (Exception e) {
				}

				try {
					String mainWinHander = CommonLib.GetDriver().getWindowHandle().toString();
					Set<String> handles = CommonLib.GetDriver().getWindowHandles();

					for (String handle : handles) {
						if (!mainWinHander.equals(handle)) {
							WebDriver popup = CommonLib.switchWindow(handle);
							CustomReporter.MessageLogger("" + CommonLib.logFormator(popup.findElement(By.xpath("//table/tbody/tr/td[3]")).getText()) + " !", CustomReporter.status.Warning);
							popup.close();
							break;
						}
					}
					CommonLib.switchWindow(mainWinHander);
				} catch (Exception e) {
				}
			} else {

				// CommonLib.setVersion(CommonLib.getText("//*[@id=\"loginForm\"]/table/tbody/tr[3]/td[2]"));
				HashTableRepository.setHash("MainWindow", CommonLib.GetDriver().getWindowHandle().toString());
				CommonLib.setValue("", "//input[@name='j_username']", username);
				CommonLib.setValue("", "//input[@name='j_password']", pswd);
				CommonLib.selectfrmDropdwn("", 0, "//select[@name='txtCompanyNumber']", "");
				CommonLib.clickButton(By.xpath("//*[@id='button.submit']"));

				// try {
				// String mainWinHander =
				// CommonLib.GetDriver().getWindowHandle().toString();
				// Set<String> handles =
				// CommonLib.GetDriver().getWindowHandles();
				//
				// for (String handle : handles) {
				// if (!mainWinHander.equals(handle)) {
				// WebDriver popup = CommonLib.switchWindow(handle);
				// CustomReporter.MessageLogger("" +
				// CommonLib.logFormator(popup.findElement(By.xpath("//table/tbody/tr/td[3]")).getText())
				// + " !", CustomReporter.status.Warning);
				// popup.close();
				// break;
				// }
				// }
				// CommonLib.switchWindow(mainWinHander);
				// } catch (Exception e) {
				// }
			}
		}
	}

	@BeforeMethod
	@AfterMethod
	public void logout() {
		if (Config.props.getProperty("SystemUnderTest").equals("NetSolution")) {
			boolean flag = false;
			CommonLib.setFrame("left");
			int rowcount = CommonLib.getElements(By.xpath("//div[@id='TimePanel']/table/tbody//tr")).size();
			for (int i = 0; i < rowcount; i++) {
				String x = CommonLib.getElement(By.xpath("//div[@id='TimePanel']/table/tbody//tr[" + (i + 1) + "]/following::a[1]")).getText();
				if (x.equals("Sign Out")) {

					CommonLib.clickButton(By.id("grdMenu_lnkMenuItem_" + (i + 1)));
					flag = true;

					CustomReporter.MessageLogger("Application signed out successfully", CustomReporter.status.Pass);
					break;
				}

			}
			if (flag == false) {
				CustomReporter.MessageLogger("Application did not signed out successfully", CustomReporter.status.Fail);
			}
		} else {
			MenuNavigation.menuNav("Logoff");
			// try {
			// String mainWinHander = CommonLib.GetDriver().getWindowHandle();
			// Set<String> handles = CommonLib.GetDriver().getWindowHandles();
			//
			// for (String handle : handles) {
			// if (!mainWinHander.equals(handle)) {
			// WebDriver popup =
			// CommonLib.GetDriver().switchTo().window(handle);
			// CustomReporter.MessageLogger("" +
			// CommonLib.logFormator(popup.findElement(By.xpath("//table/tbody/tr/td[3]")).getText())
			// + " !", CustomReporter.status.Information);
			// // popup.close();
			// popup.findElement(By.xpath("//table[@id='Table2']/tbody/tr/td[1]/input")).click();
			// break;
			// }
			// }
			// CommonLib.switchWindow(mainWinHander);
			// } catch (Exception e) {
			// }
		}
	}

}
