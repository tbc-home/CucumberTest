package common;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class CustomReporter {
	// public static int loglevel = 0;
	private static String strTestDesc = "";
	private static int iTestId = 0;
	private static ArrayList<String> arr = new ArrayList<String>();

	public static enum status {
		Pass, Fail, Warning, Information, Error, Debug, Fatal, Skip
	}

	private static String strhtmlfile = HashTableRepository.getHash("ReportFolder") + "\\" + Config.props.getProperty("SystemUnderTest") + ".html";
	private static ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(strhtmlfile);
	private static ExtentReports extent = new ExtentReports();
	private static ExtentTest test;

	public static void initiatextent() {
		extent.attachReporter(htmlReporter);
	}

	public static int getTestId() {
		return iTestId;
	}

	public static void MessageLogger(String msg, status istatus) {
		try {
			Reporter.log(msg, false);
			// System.out.println(msg);
			switch (Config.props.getProperty("LogLevel")) {
			case "0":
				if (istatus != status.Information) {
					test.log(getStatus(istatus), istatus + " - " + msg + "\n");
				} else {
					test.log(getStatus(istatus), istatus + " - " + msg + "\n");
				}
				break;
			case "1":
				if (istatus != status.Information) {
					test.log(getStatus(istatus), istatus + " - " + msg + "\n");
					break;
				}
			case "2":
				if (istatus != status.Information && istatus != status.Pass) {
					test.log(getStatus(istatus), istatus + " - " + msg + "\n");
					break;
				}
			}
			if (istatus == status.Error || istatus == status.Fail) {
				captureAndDisplayScreenShot(CommonLib.GetDriver(), test);
			}
		} catch (Exception ex) {
			System.out.print(ex.getStackTrace());
		}
	}

	public static void initiateTest(String strTest) {
		if (iTestId > 0) {

			closeTest(test.getStatus());
		}

		iTestId = iTestId + 1;
		test = extent.createTest("Test Case ID : " + iTestId, "Test Description : " + strTest);

		strTestDesc = strTest;
	}

	public static void setSkip(String SkipReason) {
		test.skip(SkipReason);
	}

	public static void CloseExtent(String ReportName) {
		// strhtmlfile = strhtmlfile + CommonLib.GetDriver().getTitle() +
		// ".html";//CommonLib.GetDriver().getTitle()
		closeTest(test.getStatus());
		try {
			Thread.sleep(CommonLib.interval);
			htmlReporter.config().setReportName("    " + CommonLib.GetDriver().getTitle() + " :   v." + CommonLib.getVersion().replaceAll("<BR>", ""));
			htmlReporter.config().setDocumentTitle(CommonLib.GetDriver().getTitle());
			extent.setSystemInfo("URL", CommonLib.GetDriver().getCurrentUrl());
		} catch (Exception e) {
		}

		extent.setSystemInfo("Browser", Config.props.getProperty("iBrowser"));

		extent.setSystemInfo("Version", CommonLib.getVersion());
		extent.setSystemInfo("User", Config.props.getProperty("user"));
		extent.setSystemInfo("Test Data", "<a target='_blank' href='" + HashTableRepository.getHash("ReportFolder")
				+ "\\TestData.xml" + "'>Test Data</a>");
		// extent.setSystemInfo("Resource Utilization",
		// "<a target='_blank' href='" + HashTableRepository.getHash("ReportFolder") +
		// "\\ResourceUtilization.html"
		// + "'>Resource Utilization</a>");

		extent.flush();
		createLocal();
	}

	public static void closeTest(Status status) {
		// if (CommonLib.CampaignName != null)
		// arr.add(iTestId + " : " +
		// Config.Repository.getProperty(CommonLib.CampaignName.toString().split(";")[1])
		// + ";"
		// + test.getStatus() + ";" + CommonLib.CampaignName.split(";")[0]);
		// else

		arr.add(iTestId + " : " + strTestDesc + ";" + test.getStatus() + ";" + strTestDesc);
		// arr.add(iTestId + " : " + strTestDesc + ";" + test.getStatus());
		switch (status) {
		case PASS:
			test.pass("Test Case : " + iTestId + " : " + strTestDesc + " Passed!");
			break;
		case FAIL:
			test.fail("Test Case : " + iTestId + " : " + strTestDesc + " Failed!");
			break;
		case ERROR:
			test.fatal("Test Case : " + iTestId + " : " + strTestDesc + " Fatal Error!");
			break;
		case WARNING:
			test.pass("Test Case : " + iTestId + " : " + strTestDesc + " Passed with Warning!");
			break;
		case SKIP:
			test.skip("Test Case : " + iTestId + " : " + strTestDesc + " Skiped!");
			break;
		default:
			test.info("Test Case :" + iTestId + " : " + strTestDesc + " Info!");
			break;
		}
	}

	private static Status getStatus(status strStatus) {
		switch (strStatus) {
		case Information:
			return Status.INFO;
		case Fail:
			return Status.FAIL;
		case Pass:
			return Status.PASS;
		case Warning:
			return Status.WARNING;
		case Error:
			return Status.ERROR;
		case Debug:
			return Status.DEBUG;
		case Fatal:
			return Status.FATAL;
		case Skip:
			return Status.SKIP;

		}
		return Status.INFO;
	}

	public static void captureAndDisplayScreenShot(WebDriver ldriver, ExtentTest eTest) {
		String extentReportImage = HashTableRepository.getHash("ReportFolder") + "\\Screenshots\\" + System.currentTimeMillis() + ".png";
		String logextentReportImage = extentReportImage.replace("", "");// "Screenshots/"
																		// +
																		// System.currentTimeMillis()
																		// +
																		// ".png";
		// Take screenshot and store as a file format
		try {
			File src = ((TakesScreenshot) ldriver).getScreenshotAs(OutputType.FILE);
			// now copy the screenshot to desired location using copyFile method
			FileUtils.copyFile(src, new File(extentReportImage));
			eTest.log(test.getStatus(), "Screenshot from : " + logextentReportImage, MediaEntityBuilder.createScreenCaptureFromPath(logextentReportImage).build());
		} catch (Exception e) {
			System.out.println("Error in the captureAndDisplayScreenShot method: " + e.getStackTrace());
		}
	}

	private static void createLocal() {
		try {
			File file = new File(strhtmlfile);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("https://fonts.googleapis.com") >= 0) {
					if (line.indexOf("css") >= 0) {
						oldtext += "<link href='Support/fonts.css' rel='stylesheet' type='text/css'>" + "\n";
					} else if (line.indexOf("icon") >= 0) {
						oldtext += "<link href='Support/icons.css' rel='stylesheet'>\n" + "<link href='Support/tree.css' rel='stylesheet'>\n";
					}

				} else {
					if (line.indexOf(">Extent</a>") < 0) {
						oldtext += line + "";
					}
				}
			}
			reader.close();
			oldtext = oldtext.replaceAll("https://cdn.rawgit.com/anshooarora/extentreports-java/bee7a4abdc590e21eec8618a90c81ff4d16e500a/dist/css/extent.css", "Support/EntentReport.css");
			oldtext = oldtext.replaceAll("<span class='label blue darken-3'>v3.0.7</span>", "");

			oldtext = oldtext.replaceAll("<script src='https://cdn.rawgit.com/anshooarora/extentreports-java/fca20fb7653aade98810546ab96a2a4360e3e712/dist/js/extent.js' type='text/javascript'></script>",
					"<script src='Support/extent.js' type='text/javascript'></script>");
			oldtext = oldtext.replaceAll("</div>	</div></div><!-- dashboard view -->", createTree() + "</div>	</div></div><!-- dashboard view -->");
			oldtext = FormatTimeTaken(oldtext);
			FileWriter writer = new FileWriter(strhtmlfile);
			writer.write(oldtext);
			writer.close();
			testdataXML();
			File htmlFile = new File(strhtmlfile);
			Desktop.getDesktop().browse(htmlFile.toURI());

		} catch (

		IOException ioe) {
			// ioe.printStackTrace();
		}

	}

	private static String FormatTimeTaken(String sHTML) {
		String sTimeTaken;
		int iTime, iMinute, iSecond;
		String[] sHtm = sHTML.split("Time Taken					<div class='panel-lead'>");
		sTimeTaken = sHtm[1].substring(0, sHtm[1].indexOf("</div>"));
		sHtm[1] = sHtm[1].replace(sTimeTaken, "");
		sTimeTaken = sTimeTaken.replaceAll("ms", "");
		sTimeTaken = sTimeTaken.replaceAll(",", "");
		iTime = Integer.parseInt(sTimeTaken);
		iTime = iTime / 1000;
		iMinute = iTime / 60;
		iSecond = iTime % 60;
		sTimeTaken = iMinute + "m" + iSecond + "s";
		sHTML = sHtm[0] + "Time Taken					<div class='panel-lead'>" + sTimeTaken + sHtm[1];
		return sHTML;
	}

	private static String createTree() {
		String strTree = "<div class='col s6'><div class='card-panel test-time-info tree'> <ol class='tree'>";
		String strTag;
		int testid;
		int iCampaign = 1;
		String sCampaign = "";
		for (int intC = 0; intC < arr.size(); intC++) {
			if (!(sCampaign.equals(arr.get(intC).toString().split(";")[2]))) {
				if (iCampaign != 1) {
					strTree += "</ol></li>";
				}
				strTree += "<li><label style='font:16px bold; font-family: Calibri;  font-style: normal;  font-weight: 600;color:#000' for='menu-" + iCampaign + "'>" + arr.get(intC).toString().split(";")[2]
						+ "</label><input type='checkbox' checked id='menu-" + iCampaign + "' /><ol>";
				sCampaign = arr.get(intC).toString().split(";")[2];
				iCampaign++;
			}
			testid = intC + 1;
			strTag = "href='' class='label' style='font-weight: 600;' onclick='selectCurrentTest(" + testid + ");return false;'";

			// strTree += "<li class='file'><a style='font-weight: 800;border:
			// 3px solid #00c853;' " + strTag + " >" +
			// arr.get(intC).toString().split(";")[0] + "</a></li>";

			if (arr.get(intC).toString().split(";")[1].equals(Status.PASS.toString())) {
				// strTree += "<ul><li><a style='font-weight: 800;border: 3px
				// solid #00c853;' " + strTag + " >" +
				// arr.get(intC).toString().split(";")[0] + "</a>";
				strTree += "<li class='file'><a class='Pass' style='font-weight: 800;' " + strTag + " >" + arr.get(intC).toString().split(";")[0] + "</a></li>";
			} else if (arr.get(intC).toString().split(";")[1].equals(Status.SKIP.toString()) || arr.get(intC).toString().split(";")[1].equals(Status.WARNING.toString())) {
				// strTree += "<ul><li><a style='font-weight: 800;border: 3px
				// solid #fbc02d;' " + strTag + ">" +
				// arr.get(intC).toString().split(";")[0] + "</a>";
				strTree += "<li class='file'><a class='Skip' style='font-weight: 800;' " + strTag + " >" + arr.get(intC).toString().split(";")[0] + "</a></li>";
			} else if (arr.get(intC).toString().split(";")[1].equals(Status.FAIL.toString())) {
				// strTree += "<ul><li><a style='font-weight: 800;border: 3px
				// solid #fbc02d;' " + strTag + ">" +
				// arr.get(intC).toString().split(";")[0] + "</a>";
				strTree += "<li class='file'><a class='Fail' style='font-weight: 800;' " + strTag + " >" + arr.get(intC).toString().split(";")[0] + "</a></li>";
			} else {
				// strTree += "<ul><li><a style='font-weight: 800;border: 3px
				// solid #ef5350;' " + strTag + ">" +
				// arr.get(intC).toString().split(";")[0] + "</a>";
				strTree += "<li class='file'><a class='Fatal' style='font-weight: 800;' " + strTag + " >" + arr.get(intC).toString().split(";")[0] + "</a></li>";
			}

		}
		strTree += "</ol></li>";
		strTree += "</ol></div></div>";
		return strTree;

	}

	private static void testdataXML() {
		String sXML = "<?xml version='1.0' encoding='UTF-8' standalone='no'?><TextData>";
		String xmlPath = HashTableRepository.getHash("ReportFolder") + "\\TestData.xml";

		Iterator<Entry<String, String>> it = HashTableRepository.HashCampaign.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();

			sXML = sXML + "<" + CommonLib.protectSpecialCharacters(pair.getKey().split(";")[0]) + ">";
			for (int intCount = 0; intCount <= pair.getValue().split(",").length - 1; intCount++) {
				sXML = sXML + "<" + CommonLib.protectSpecialCharacters(pair.getValue().split(",")[intCount]) + ">" + CommonLib.protectSpecialCharacters(HashTableRepository.getHash(pair.getValue().split(",")[intCount])) + "</"
						+ CommonLib.protectSpecialCharacters(pair.getValue().split(",")[intCount]) + ">" + System.lineSeparator();
			}

			// + CommonLib.protectSpecialCharacters(pair.getValue())
			sXML = sXML + "</" + CommonLib.protectSpecialCharacters(pair.getKey().split(";")[0]) + ">" + System.lineSeparator();
			it.remove();
		}
		sXML = sXML + "</TextData>";
		FileWriter writer;
		try {
			writer = new FileWriter(xmlPath);
			writer.write(sXML);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ResourceUtilization();
	}

	// private static void ResourceUtilization() {
	// String sXML = "<!DOCTYPE HTML><html><head> <script>window.onload = function
	// () {var chart = new CanvasJS.Chart('chartContainer',
	// {theme:'light2',animationEnabled: true, exportEnabled: true, title: { text:
	// 'Resource Utilization Chart' }, axisX: { valueFormatString: 'HH:mm:ss' },
	// axisY: { title: 'Memory & CPU Utilization', prefix: '', suffix: '%' },
	// toolTip: { shared: true }, legend: { cursor: 'pointer', itemclick:
	// toogleDataSeries }, data: [";
	// String xmlPath = HashTableRepository.getHash("ReportFolder") +
	// "\\ResourceUtilization.html";
	//
	// Iterator<Entry<String, String>> it =
	// ResourceMonitor.HashResource.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
	//
	// sXML = sXML + " { type:'spline', axisYType: 'primary', name: '" +
	// pair.getKey() + "', showInLegend: true, markerSize: 0, yValueFormatString:
	// '', dataPoints: [ ";
	// for (int intCount = 0; intCount <= pair.getValue().split(";").length - 1;
	// intCount++) {
	// if (intCount == 0) {
	// sXML = sXML +
	// CommonLib.protectSpecialCharacters(pair.getValue().split(";")[intCount]) +
	// System.lineSeparator();
	// } else {
	// sXML = sXML + "," +
	// CommonLib.protectSpecialCharacters(pair.getValue().split(";")[intCount]) +
	// System.lineSeparator();
	// }
	// }
	//
	// // + CommonLib.protectSpecialCharacters(pair.getValue())
	// sXML = sXML + " ] },";
	// it.remove();
	// }
	//
	// // sXML = sXML.substring(0, sXML.length() - 2);
	// sXML = sXML
	// + "] });chart.render(); function toogleDataSeries(e){ if
	// (typeof(e.dataSeries.visible) === 'undefined' || e.dataSeries.visible) {
	// e.dataSeries.visible = false; } else{ e.dataSeries.visible = true; }
	// chart.render();}}</script></head><body><div id='chartContainer'
	// style='height: 370px; width: 100%;'></div><script
	// src='Support/canvasjs.min.js'></script></body></html>";
	// FileWriter writer;
	//
	// try {
	// writer = new FileWriter(xmlPath);
	// writer.write(sXML);
	// writer.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

}
