package common;

import java.io.IOException;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

public class Listener implements ITestListener, ISuiteListener, IInvokedMethodListener {

	// This belongs to ISuiteListener and will execute before the Suite start
	@Override
	public void onStart(ISuite arg0) {
		// CustomReporter.initiatextent();
		try {
			Config.ConfigRead("Resources/config.properties");
			String current = "";
			try {
				current = new java.io.File(".").getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HashTableRepository.setHash("ReportFolder", current + "\\ExtentReport\\" + System.currentTimeMillis());
			CustomReporter.initiatextent();
			CommonLib.InitializeDriverObj();
			Config.ReadRepository("Resources/OR.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// This belongs to ISuiteListener and will execute, once the Suite is
	// finished
	@Override
	public void onFinish(ISuite arg0) {
		CustomReporter.CloseExtent("");
		// if (CustomReporter.getTestId() == 20) {
		CommonLib.CloseDriverObj();
		// }
	}

	// This belongs to ITestListener and will execute before starting of Test
	// set/batch
	public void onStart(ITestContext arg0) {
		Reporter.log("About to begin executing Test " + arg0.getName(), true);
	}

	// This belongs to ITestListener and will execute, once the Test set/batch
	// is finished
	public void onFinish(ITestContext arg0) {
		Reporter.log("Completed executing test " + arg0.getName(), true);
	}

	// This belongs to ITestListener and will execute only when the test is pass
	public void onTestSuccess(ITestResult arg0) {
		// This is calling the printTestResults method
		printTestResults(arg0);
	}

	// This belongs to ITestListener and will execute only on the event of fail
	// test
	public void onTestFailure(ITestResult arg0) {
		// This is calling the printTestResults method
		printTestResults(arg0);
		Reporter.log(arg0.getThrowable().getMessage());
		CustomReporter.MessageLogger(arg0.getThrowable().getMessage(), CustomReporter.status.Error);
		// if ((Config.props.getProperty("SystemUnderTest").equals("NetSolution"))) {
		// MenuNavigation.menuNav("ResidentSettings");
		// }
		HashTableRepository.setHash("CurrentMenu", "Default");
		try {
			Thread.sleep(CommonLib.interval * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// This belongs to ITestListener and will execute before the main test start
	// (@Test)
	public void onTestStart(ITestResult arg0) {
		CustomReporter.initiateTest(Config.Repository.getProperty(arg0.getName()));
		// }
		// arg0.
		// System.out.println(this.pr);
	}

	// This belongs to ITestListener and will execute only if any of the main
	// test(@Test) get skipped
	public void onTestSkipped(ITestResult arg0) {
		CustomReporter.initiateTest(Config.Repository.getProperty(arg0.getName()));
		CustomReporter.setSkip(Config.Repository.getProperty(arg0.getName()));
		printTestResults(arg0);
	}

	// This is just a piece of shit, ignore this
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	// This is the method which will be executed in case of test pass or fail
	// This will provide the information on the test
	private void printTestResults(ITestResult result) {
		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);
		if (result.getParameters().length != 0) {
			String params = null;
			for (Object parameter : result.getParameters()) {
				params += parameter.toString() + ",";
			}
			Reporter.log("Test Method had the following parameters : " + params, true);
		}
		String status = null;
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			status = "Pass";
			break;
		case ITestResult.FAILURE:
			status = "Failed";
			break;
		case ITestResult.SKIP:
			status = "Skipped";
		}
		Reporter.log("Test Status: " + status, true);
	}

	// This belongs to IInvokedMethodListener and will execute before every
	// method including @Before @After @Test
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		String textMsg = "About to begin executing following method : " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
		CustomReporter.MessageLogger(textMsg, CustomReporter.status.Information);
	}

	// This belongs to IInvokedMethodListener and will execute after every
	// method including @Before @After @Test
	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
		if (!(arg0.getTestMethod().getMethodName().equals("removeGroup"))) {
			CommonLib.CampaignName = arg0.getTestMethod().getRealClass().getSimpleName() + ";"
					+ arg0.getTestMethod().getMethodName(); // .getRealClass().getSimpleName();
		}
		String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());
		Reporter.log(textMsg, true);
		CustomReporter.MessageLogger(textMsg, CustomReporter.status.Information);
	}

	// This will return method names to the calling function
	private String returnMethodName(ITestNGMethod method) {
		return method.getRealClass().getSimpleName() + "." + method.getMethodName();
	}

}