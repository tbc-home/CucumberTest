package cucumberReport;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
//import org.openqa.selenium.chrome.ChromeDriver;

import common.CommonLib;
import common.Config;
import common.CustomReporter;
import common.HashTableRepository;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "html:folder1",
		"json:folder2" }, features = "src/test/java/cucumberReport", dryRun = false, monochrome = true, tags = "@door", snippets = SnippetType.UNDERSCORE)

public class runTestReport {

	@BeforeClass
	public static void setup() {
		try {
			// CustomReporter.initiatextent();

			Config.ConfigRead("Resources/config.properties");
			Config.ReadRepository("Resources/OR.properties");
			String current = "";
			try {
				current = new java.io.File(".").getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HashTableRepository.setHash("ReportFolder", current + "\\ExtentReport\\" + System.currentTimeMillis());
			CustomReporter.initiatextent();
			CommonLib.InitializeDriverObj();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ran the before");
	}

	@AfterClass
	public static void teardown() {
		CustomReporter.CloseExtent("");
		CommonLib.CloseDriverObj();
		System.out.println("Ran the after");
	}

}