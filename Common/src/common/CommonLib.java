package common;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.CustomReporter.status;

public class CommonLib {
	private static WebDriver sDriver;// = new ChromeDriver();
	private static ThreadLocal<WebDriver> driverTh;
	private static WebDriverWait sWait;
	private static String sVersion = "";
	public static int interval = 100;
	public static String CampaignName = "Initial";
	public static boolean securityUser = false;
	public static boolean PatAdmited = true;

	/*
	 * Function Name : setVersion Parameter : Version Detail as String Created
	 * By : Tarun Gupta Purpose : Save the version for later use
	 */
	public static void setVersion(String strVersion) {
		sVersion = strVersion;
	}

	public static void VerifySecurityUser() {
		if (CommonLib.securityUser) {
			// MenuNavigation.menuNav("SignOut");
			Login objLogin = new Login();
			objLogin.logout();
			objLogin.initlogin(Config.props.getProperty("user"), Config.props.getProperty("password"), true);
			objLogin = null;
		}
	}

	public static void setDrive(WebDriver driver) {
		driverTh.set(driver);
		sDriver = driverTh.get();

	}

	public static void clickButtonTwiceForRefresh(String xpath) {
		int i = 1;
		WebElement e = CommonLib.getElement(By.xpath(xpath));
		while (i <= 2) {
			try {
				e.click();
			} catch (StaleElementReferenceException x) {
				break;
			}
			i++;
		}
	}

	// Function Name : getVersion
	// Parameter : None
	// Created By : Tarun Gupta
	// Purpose : Return the version
	public static String getVersion() {
		return sVersion;
	}

	public static void InitializeDriverObj() throws Exception {

		String current = "";
		try {
			current = new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		createFolder(HashTableRepository.getHash("ReportFolder"));
		createFolder(HashTableRepository.getHash("ReportFolder") + "\\Reports");
		createFolder(HashTableRepository.getHash("ReportFolder") + "\\Screenshots");
		File srcDir = new File(current + "\\Resources\\Support");
		File destDir = new File(HashTableRepository.getHash("ReportFolder") + "\\Support");
		FileUtils.copyDirectory(srcDir, destDir);
		String downloadFilepath = HashTableRepository.getHash("ReportFolder") + "\\Reports";
		HashMap<String, Object> Prefs = new HashMap<String, Object>();
		Prefs.put("profile.default_content_settings.popups", 1);
		Prefs.put("download.default_directory", downloadFilepath);
		Prefs.put("credentials_enable_service", false);
		Prefs.put("profile.password_manager_enabled", false);
		Prefs.put("plugins.always_open_pdf_externally", true);
		Prefs.put("safebrowsing.enabled", "true");
		switch (Config.props.getProperty("iBrowser")) {
		case "Chrome":
			ChromeOptions opsChrome = new ChromeOptions();
			opsChrome.addArguments("--disable-notifications");
			opsChrome.setExperimentalOption("prefs", Prefs);

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, opsChrome);
			// WebDriver driver = new ChromeDriver(cap);

			System.setProperty("webdriver.chrome.driver", "Resources/Driver/chrome_driver/chromedriver.exe");
			sDriver = new ChromeDriver(opsChrome);
			break;
		case "IE":
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// capabilities = DesiredCapabilities.INTERNETEXPLORER
			// capabilities.setCapability("platform", "WIN8");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("IntroduceInstabilityByIgnoringProtectedModeSettings", true);
			capabilities.setCapability("browserName", "internet explorer");
			capabilities.setCapability("nativeEvents", true);

			// Capabilities [{acceptInsecureCerts=false, browserVersion=11,
			// se:ieOptions={nativeEvents=true, browserAttachTimeout=0.0,
			// ie.ensureCleanSession=false, elementScrollBehavior=0.0,
			// enablePersistentHover=true, ie.browserCommandLineSwitches=,
			// ie.forceCreateProcessApi=false, requireWindowFocus=false,
			// initialBrowserUrl=http://localhost:48638/,
			// ignoreZoomSetting=false, ie.fileUploadDialogTimeout=3000.0,
			// ignoreProtectedModeSettings=true}, browserName=internet explorer,
			// pageLoadStrategy=normal, unhandledPromptBehavior=dismiss,
			// javascriptEnabled=true, platformName=windows, setWindowRect=true,
			// platform=ANY}]

			// capabilities.setCapability("platform", "WIN8");
			// capabilities.setCapability("platform", "WIN8");
			// capabilities.setCapability("platform", "WIN8");
			//
			// capabilities["browserName"] = "internet explorer"
			// capabilities["ignoreProtectedModeSettings"] = True
			// capabilities["IntroduceInstabilityByIgnoringProtectedModeSettings"]
			// = True
			// capabilities["nativeEvents"] = True
			// capabilities["ignoreZoomSetting"] = True
			// capabilities["requireWindowFocus"] = True
			// capabilities["INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS"]
			// = True

			// InternetExplorerOptions opsIE = new InternetExplorerOptions();
			// opsIE.addCommandSwitches(Prefs);
			// opsIE.addArguments("--disable-notifications");
			// opsIE.setExperimentalOption("prefs", Prefs);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			System.setProperty("webdriver.ie.driver", "Resources/Driver/IE_driver/IEDriverServer.exe");
			sDriver = new InternetExplorerDriver(capabilities);
			sDriver.manage().deleteAllCookies();

			break;
		default:
			System.setProperty("webdriver.ie.driver", "Resources/Driver/IE_driver/IEDriverServer.exe");
			sDriver = new InternetExplorerDriver();
		}
		try {
			interval = Integer.parseInt(Config.props.getProperty("interval"));
		} catch (Exception e) {
		}

		sDriver.manage().window().maximize();

		try {
			Runtime.getRuntime().exec(current + Config.props.getProperty("WindowsCredential"));
			sDriver.get(Config.props.getProperty("URL"));
			staticWait(10);

		} catch (Exception e) {
			sDriver.get(Config.props.getProperty("URL"));
		}

		sDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		sDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		sDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		sWait = new WebDriverWait(sDriver, 10);
		sWait.withTimeout(30, TimeUnit.SECONDS);
		// sWait.pollingEvery(10, TimeUnit.SECONDS);
		sWait.ignoring(NoSuchElementException.class);
		sWait.ignoring(StaleElementReferenceException.class);
	}

	public static void setImplicitWait(int tempInterval) {
		sDriver.manage().timeouts().implicitlyWait(tempInterval, TimeUnit.SECONDS);
	}

	public static void resetImplicitWait() {
		sDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	// Function Name : createFolder
	// Parameter : Path for creating folder
	// Created By : Tarun Gupta
	// Purpose : to create a folder in a particular directory

	public static void createFolder(String path) throws Exception {
		File dir = new File(path);
		dir.mkdir();
	}

	// Function Name : changeimplicitwait
	// Parameter : time in seconds
	// Created By : Tarun Gupta
	// Purpose : to change implicit wait
	public static void changeimplicitwait(int iWait) {
		sDriver.manage().timeouts().implicitlyWait(iWait, TimeUnit.SECONDS);
	}

	// Function Name : GetDriver
	// Parameter : none
	// Created By : Tarun Gupta
	// Purpose : to get the web driver
	//
	public static WebDriver GetDriver() {
		return sDriver;
	}
	// Function Name : CloseDriverObj
	// Parameter : none
	// Purpose : to quit browser session

	public static void CloseDriverObj() {
		sDriver.quit();
	}

	public static void staticWait(int multiplier) {
		try {
			Thread.sleep(interval * multiplier);
			//Runtime runtime;
			//runtime = Runtime.getRuntime();
			//ResourceMonitor.printUsage(runtime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// Function Name : setFrame
	// Parameter : frame name
	// Purpose : to set frame from default by frame name

	public static void setFrame(String frameName) {
		staticWait(1);
		sDriver.switchTo().defaultContent();
		sDriver.switchTo().frame(frameName);
		staticWait(1);
		// CustomReporter.MessageLogger("Selected Frame : " +
		// logFormator(frameName) + " !", CustomReporter.status.Information);
	}
	// Function Name : setFrameFromCurrent
	// Parameter : frame element
	// Purpose : to set frame from current frame and not going in default

	public static void setFrameFromCurrent(WebElement frameElement) {
		staticWait(1);
		sDriver.switchTo().frame(frameElement);
		staticWait(1);
		// CustomReporter.MessageLogger("Selected Frame : " +
		// logFormator(frameElement.toString()) + " !",
		// CustomReporter.status.Information);
	}

	// Function Name : setFrameToDefault
	// Parameter : none
	// Purpose : to set default frame
	public static void setFrameToDefault() {
		staticWait(1);
		sDriver.switchTo().defaultContent();
		staticWait(1);
		// CustomReporter.MessageLogger("Selected Frame : Default Content !",
		// CustomReporter.status.Information);
	}

	// Function Name : setFrame
	// Parameter : by frame locator
	// Purpose : to set frame from default by element locator
	public static void setFrame(By frameElement) {
		sDriver.switchTo().defaultContent();
		sDriver.switchTo().frame(sDriver.findElement(frameElement));
		staticWait(1);
		// CustomReporter.MessageLogger("Selected Frame : " +
		// logFormator(frameElement.toString()) + " !",
		// CustomReporter.status.Information);
	}

	// Function Name : GetParent
	// Parameter : link text
	// Purpose : to get parent tag of the parameter
	public static WebElement GetParent(String objId) {
		return sDriver.findElement(By.linkText(objId)).findElement(By.xpath("./.."));
	}

	// to get webElement
	public static WebElement GetObject(By obj) {
		// CustomReporter.MessageLogger("Return Object : " +
		// logFormator(obj.toString()) + " !",
		// CustomReporter.status.Information);
		return sDriver.findElement(obj);
	}

	// to select an element from the dropdown menu using xpath or id of dropdown
	public static void selectfrmDropdwn(String dropdownID, int index, String xpath, String value) {

		Select value_drpdwn;
		if (dropdownID.equals("")) {
			value_drpdwn = new Select(sDriver.findElement(By.xpath(xpath)));
		} else {
			value_drpdwn = new Select(sDriver.findElement(By.id(dropdownID)));
		}
		if (index == -1) {
			value_drpdwn.selectByVisibleText(value);
		} else {
			value_drpdwn.selectByIndex(index);
		}
		staticWait(1);
		CustomReporter.MessageLogger(logFormator(dropdownID + xpath) + " : " + logFormator(String.valueOf(index) + " " + value) + " Selected !", CustomReporter.status.Information);
	}

	// to get no of elements in a drop down
	public static int getDropDownSize(By ddlObj) {
		Select drpdwn;
		drpdwn = new Select(sDriver.findElement(ddlObj));
		staticWait(1);
		return drpdwn.getOptions().size();

	}

	// to get drop down list
	public static ArrayList<String> getDropDownOptions(By locator) {
		Select obj = new Select(CommonLib.getElement(locator));
		List<WebElement> dropDownList = obj.getOptions();
		ArrayList<String> dropDownText = new ArrayList<String>();
		for (WebElement dropDown : dropDownList) {

			String gotoList = dropDown.getText();
			if (!gotoList.equals(""))
				dropDownText.add(gotoList);
		}
		return dropDownText;

	}

	// to set a random value for all the drop downs in a screen
	public static void setAllDropdowns(By dropDownClass) {
		staticWait(1);
		List<WebElement> dropDownList = sDriver.findElements(dropDownClass);

		for (WebElement dropDown : dropDownList) {
			Select se = new Select(dropDown);
			if (se.getOptions().size() > 2) {

				sWait.until(ExpectedConditions.elementToBeSelected(dropDown));
				se.selectByIndex(new Random().nextInt(se.getOptions().size()) + 1);
			}
		}
	}

	// to set a random option in a drop down using element address of drop down
	public static String setRandomDropDown(By elementAddress) {
		staticWait(1);
		Select se = new Select(sDriver.findElement(elementAddress));
		List<WebElement> dropList = se.getOptions();
		String text = null;
		while (true) {
			if (dropList.size() - 1 > 1) {
				int randomIndex = new Random().nextInt(dropList.size() - 2);
				randomIndex++;
				if (!(dropList.get(randomIndex).getText().equals(""))) {
					CustomReporter.MessageLogger("Dropdown " + logFormator(elementAddress.toString()) + " Selected " + logFormator(dropList.get(randomIndex).getText()) + ".", CustomReporter.status.Information);
					text = dropList.get(randomIndex).getText();
					// Have to remove
					if (text.equals("Pharmacy")) {
						randomIndex--;
					}
					se.selectByIndex(randomIndex);
				}
			} else {
				if (dropList.size() == 2) {
					CustomReporter.MessageLogger("Dropdown " + logFormator(elementAddress.toString()) + logFormator(dropList.get(1).getText()) + ".", CustomReporter.status.Information);
					text = dropList.get(1).getText();
					se.selectByIndex(1);
				} else {

					if ((dropList.get(0).getText().equals(""))) {
						CustomReporter.MessageLogger("Dropdown " + logFormator(elementAddress.toString()) + " Selected Blank.", CustomReporter.status.Information);
						text = dropList.get(0).getText();
					}
				}
			}
			return text;
		}
	}

	// to select a drop down by drop down path or by its value i.e. visible text
	public static void setDropDown(By dropDownPath, String value) {
		Select select = new Select(sDriver.findElement(dropDownPath));
		select.selectByVisibleText(value);
		staticWait(1);
		CustomReporter.MessageLogger(dropDownPath + " : " + logFormator(" " + value) + " Selected !", CustomReporter.status.Information);
	}

	// to set a value of a drop down by drop down path or value
	public static void setDropDownValue(By dropDownPath, String value) {
		Select select = new Select(sDriver.findElement(dropDownPath));
		select.selectByValue(value);
		staticWait(1);
		CustomReporter.MessageLogger(dropDownPath + " : " + logFormator(" " + value) + " Selected !", CustomReporter.status.Information);
	}

	public static void OperateInWindow(By elementLocator) {
		String MainFrame = "";
		int WaitCounter = 0;
		try {
			String PreviousString = "";
			String windowLocator = "";
			MainFrame = CommonLib.GetDriver().getWindowHandle();
			windowLocator = CommonLib.getLatestWindowHandle();
			if (!(MainFrame.equals(windowLocator))) {
				while (WaitCounter <= 5) {
					if (!(windowLocator.equals(""))) {
						PreviousString = windowLocator;
						CommonLib.switchWindow(windowLocator);
						staticWait(1);
						CommonLib.clickButton(elementLocator);
					} else {
						break;
					}
					windowLocator = CommonLib.getLatestWindowHandle();
					if (windowLocator.equals(PreviousString)) {
						WaitCounter++;
					} else {
						break;
					}
				}
			}
			// staticWait(1);
		} catch (Exception e) {

		}
		if (MainFrame.equals(HashTableRepository.getHash("MainWindow"))) {
			CommonLib.switchWindow(HashTableRepository.getHash("MainWindow"));
		} else {
			CommonLib.switchWindow(MainFrame);
		}
	}

	// to switch between different windows
	public static WebDriver switchWindow(String windowLocator) {
		return sDriver.switchTo().window(windowLocator);
	}

	// to go to the latest window
	public static String getLatestWindowHandle() {
		Set<String> windowHandle;
		Iterator<String> itr;
		String windowLocator = null;
		windowHandle = sDriver.getWindowHandles();
		itr = windowHandle.iterator();
		if (windowHandle.size() > 1) {
			for (int Intc = 1; Intc <= windowHandle.size(); Intc++) {
				windowLocator = itr.next();
			}

		} else {
			windowLocator = "";
		}
		return windowLocator;
	}

	public static int getWindowHandleSize() {
		return sDriver.getWindowHandles().size();
	}

	// to check whether a particular web element is present or not!
	public static boolean isElementPresent(By selector, long timeOutInSeconds) {
		boolean elementPresence = false;
		try {
			WebDriverWait wait = new WebDriverWait(sDriver, timeOutInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
			CustomReporter.MessageLogger("Looking for WebElement: " + selector, status.Information);
			elementPresence = true;
			CustomReporter.MessageLogger("WebElement: " + selector + "is found", status.Pass);
		} catch (Exception e) {
			CustomReporter.MessageLogger("WebElement: " + selector + "is not found", status.Information);
		}
		return elementPresence;
	}

	// to get a web element
	public static WebElement getElement(By element) {
		staticWait(1);
		return sDriver.findElement(element);
	}

	// to get a list of web elements
	public static List<WebElement> getElements(By element) {
		staticWait(1);
		return sDriver.findElements(element);
	}

	// to get a list of web elements by their element address
	public static List<WebElement> getList(By elementAddress) {
		List<WebElement> elementList = sDriver.findElements(elementAddress);
		return elementList;
	}

	// to get the username of login screen
	public static String getuserName() {
		setFrame("rtop");
		return getText("txtUserName");
	}

	// to get the selected option in a drop down menu
	public static String getComboBoxText(By elementLocator) {

		Select sel = new Select(CommonLib.getElement(elementLocator));
		WebElement selected = sel.getFirstSelectedOption();
		String selectedText = selected.getText();
		return selectedText;
	}

	// to get the total no of rows in a table by its xpath
	public static int getRowCount(String tableXpathRow) {
		int rowCount = sDriver.findElements(By.xpath(tableXpathRow)).size();
		return rowCount;
	}

	// to get the text by its id
	public static String getText(String elementId) {
		String UserName = sDriver.findElement(By.id(elementId)).getText();
		CustomReporter.MessageLogger(elementId + " : " + logFormator(UserName), CustomReporter.status.Information);
		return UserName;
	}

	// to get the text by its xpath
	public static String getText(By elementxpath) {
		staticWait(1);
		String elementtext = sDriver.findElement(elementxpath).getText();
		CustomReporter.MessageLogger(logFormator(elementxpath.toString()) + " : " + logFormator(elementtext), CustomReporter.status.Information);
		return elementtext;
	}

	// to get the value of the text by its xpath
	public static String getValue(By elementxpath) {
		String elementtext = sDriver.findElement(elementxpath).getAttribute("value");
		CustomReporter.MessageLogger(elementxpath + " : " + logFormator(elementtext), CustomReporter.status.Information);
		return elementtext;
	}

	// to set the value by its xpath or control ID
	public static String setValue(String ControlID, String xpath, String sData) {
		WebElement textControl;
		if (ControlID.equals("")) {
			textControl = sDriver.findElement(By.xpath(xpath));
		} else {
			textControl = sDriver.findElement(By.id(ControlID));
		}
		JavascriptExecutor myexecutor = ((JavascriptExecutor) sDriver);
		myexecutor.executeScript("arguments[0].value='" + sData + "';", textControl);
		if (ControlID == "txtPassword") {
			sData = "********";
		}
		CustomReporter.MessageLogger(xpath + ControlID + " : " + logFormator(sData) + " !", CustomReporter.status.Information);
		staticWait(1);
		return sData;
	}

	// to set the data by element locator
	public static String setData(By elementLocator, String data) {

		WebElement textControl;

		// sDriver.findElement(elementLocator).clear();
		textControl = sDriver.findElement(elementLocator);
		textControl.clear();
		JavascriptExecutor myexecutor = ((JavascriptExecutor) sDriver);
		myexecutor.executeScript("arguments[0].value='" + data + "';", textControl);
		staticWait(1);

		String txt = sDriver.findElement(elementLocator).getAttribute("id");

		try {
			setImplicitWait(1);
			String ControlXpath = elementLocator.toString().replace("By.xpath:", "");
			txt = sDriver.findElement(By.xpath(ControlXpath + "//preceding::td[1]/span")).getText();
			// preceding::td[1]/span

		} catch (Exception ex) {

		}
		resetImplicitWait();
		if (txt.equals("") || txt == null) {
			txt = elementLocator.toString();
		}
		CustomReporter.MessageLogger("In Text Box " + logFormator(txt) + " set value " + " " + logFormator(data) + "!", CustomReporter.status.Information);
		return data;
	}

	// to set the focus on a particular element through tab
	public static void setfocusbyTab(String sXpath, int ForwardOrBack) throws InterruptedException {
		sDriver.findElement(By.xpath(sXpath)).click();
		staticWait(4);
		if (ForwardOrBack == 0)
			sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.SHIFT, Keys.TAB);
		else
			sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.TAB);
		staticWait(4);
	}

	// to check and uncheck a checkbox
	public static void setBoolFields(boolean flag, By locator) {
		WebElement fieldChangesCheck = CommonLib.getElement(locator);
		if (flag == true) {
			if (fieldChangesCheck.isSelected() == false) {
				fieldChangesCheck.click();
			}
			CustomReporter.MessageLogger(locator + " : Selected !", CustomReporter.status.Information);
		} else {
			if (fieldChangesCheck.isSelected() == true) {
				fieldChangesCheck.click();
			}
			CustomReporter.MessageLogger(locator + " : De-Selected !", CustomReporter.status.Information);
		}

	}

	// to check and uncheck a checkbox using webelement
	public static void setBoolFields(boolean flag, WebElement we) {

		if (flag == true) {
			if (we.isSelected() == false) {
				we.click();
			}
			CustomReporter.MessageLogger(we + " : Selected !", CustomReporter.status.Information);
		} else {
			if (we.isSelected() == true) {
				we.click();
			}
			CustomReporter.MessageLogger(we + " : De-Selected !", CustomReporter.status.Information);
		}

	}

	// to get the difference in time between 2 given days
	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	// to get the current date and time in the string format
	public static String getcurrentdatetime(String format, int dateadd, String dateid, int flag) {
		DateFormat df = new SimpleDateFormat(format);
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, dateadd);
		Date da = c.getTime();
		String ds = df.format(da); // date after dateadd in string format
		String dcs = df.format(dt);// current date in string format
		try {
			if (!dateid.equals("")) {
				WebElement setdate = sDriver.findElement(By.id(dateid));
				JavascriptExecutor myexecutor = ((JavascriptExecutor) sDriver);
				myexecutor.executeScript("arguments[0].value='" + ds + "';", setdate);
			}
		} catch (Exception e) {
			// System.out.print(e.getMessage());
		}
		CustomReporter.MessageLogger("As per flag: " + logFormator(String.valueOf(flag)) + " date is " + logFormator(ds) + "", CustomReporter.status.Information);
		if (flag == 0) {
			return ds;
		} else {
			return dcs;
		}
	}

	// to set the current date
	public static String setCurrentDate(String id) {
		WebElement wb;
		String xpathstring = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		wb = sDriver.findElement(By.xpath(xpathstring));
		wb.click();
		staticWait(2);
		wb.sendKeys(Keys.ARROW_DOWN);
		staticWait(4);
		Actions builder = new Actions(sDriver);
		builder.sendKeys(Keys.RETURN).perform();
		staticWait(2);
		return wb.getText();

	}

	// to change the given date from string format to date format
	public static Date StringtoDate(String datestring) {
		Date objdate = null;
		try {
			objdate = new SimpleDateFormat("MM/dd/yyyy").parse(datestring);
		} catch (Exception e) {
			CustomReporter.MessageLogger("An exception occured while converting the string date in to DATE" + e, CustomReporter.status.Information);
		}
		return objdate;
	}

	// cal.setTime(currentDate);
	// cal.add(Calendar.HOUR, -1);
	// Date oneHourBack = cal.getTime();
	// to fetch the system time in string format
	public static String systemTime(String format, int minusMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minusMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("mm");
			String time = Calendar.HOUR + ":" + sdf.format(cal.getTime());
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			// //System.out.println(sdf.format(cal.getTime()));
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	// Function Name : numberOfDaysTo
	// Parameter : String sdate1, String sdate2
	// Created By : Arya J Anil
	// Purpose : Calculating the number of days between two dates
	public static int numberOfDaysTo(String sdate1, String sdate2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy").parse(sdate1);
			date2 = new SimpleDateFormat("MM/dd/yyyy").parse(sdate2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeMilli = date2.getTime() - date1.getTime();
		long days = TimeUnit.MILLISECONDS.toDays(timeMilli);
		int idays = (int) days;
		return idays;

	}

	// to click a link and the link would be fetched through a locator
	public static void LinkClick(By locator) {
		String txt = sDriver.findElement(locator).getText();
		sDriver.findElement(locator).click();
		staticWait(1);
		CustomReporter.MessageLogger("" + logFormator(txt) + " : Clicked ! ", CustomReporter.status.Information);
	}

	// to click a button
	public static void clickButton(By elementLocator) {
		String txt = sDriver.findElement(elementLocator).getText();

		try {
			sDriver.findElement(elementLocator).click();
		} catch (StaleElementReferenceException e) {

			int i = 0;
			while (i < 5) {
				try {
					sDriver.findElement(elementLocator).click();
					break;
				} catch (StaleElementReferenceException e1) {

				}
			}
			i++;
		}
		staticWait(1);
		CustomReporter.MessageLogger(logFormator(elementLocator.toString()) + " with Text " + txt + " : " + " Clicked !", CustomReporter.status.Information);
	}

	public static void expectedComdition(By locator, int time) {
		WebDriverWait wait = new WebDriverWait(CommonLib.GetDriver(), time);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	// to click an icon
	public static void clickIcon(String sIcon) throws InterruptedException {
		staticWait(2);
		JavascriptExecutor js = (JavascriptExecutor) sDriver;
		js.executeScript("document.getElementById('" + sIcon + "').focus()");
		js.executeScript("document.getElementById('" + sIcon + "').click()");
		CustomReporter.MessageLogger("Click " + sIcon + " Icon.", CustomReporter.status.Information);
	}

	// to perform a mouse click
	public static void mouseClick(String cssLocator) {
		String locator = cssLocator;
		WebElement el = sDriver.findElement(By.cssSelector(locator));
		Actions builder = new Actions(sDriver);
		builder.moveToElement(el).release(el).click();
		builder.perform();
	}

	// to select a radio button by checking whether it is enabled or not
	public static void selectradio(By xiRadio) {
		WebElement radio;
		radio = sDriver.findElement(xiRadio);
		staticWait(1);
		boolean enabled_status = radio.isEnabled();
		if (enabled_status == true) {
			radio.click();
			staticWait(1);
			CustomReporter.MessageLogger("Radio Button clicked! " + logFormator(radio.toString()) + "", CustomReporter.status.Information);
		}
	}

	// to select a row and the row will be searched through xpath
	public static void selectrow(String rowXPath) throws InterruptedException {
		staticWait(3);
		sDriver.findElement(By.xpath(rowXPath)).click();
		staticWait(3);
	}

	// to click on the alert boxes
	public static String checkAlert() {
		String alertText = null;
		try {
			Alert alert = sDriver.switchTo().alert();
			alertText = alert.getText();
			alert.accept();
		} catch (Exception e) {
		}
		return alertText;
	}

	// to check a checkbox only when it is unchecked
	public static void setCheckbox(String checkboxes) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) sDriver;
			List<WebElement> elementToClick = sDriver.findElements(By.xpath(checkboxes));
			for (WebElement filterCheck : elementToClick) {
				// System.out.println(filterCheck.getAttribute("id") + " " +
				// filterCheck.isSelected());
				staticWait(1);
				if (filterCheck.isSelected() == false) {
					js.executeScript("document.getElementById('" + filterCheck.getAttribute("id") + "').click()");

				}
			}
		} catch (Exception e) {
			CustomReporter.MessageLogger(e.getStackTrace() + "\n", CustomReporter.status.Error);
		}
	}

	// to check all checkboxes
	public static void checkbox(String xpath) {
		staticWait(1);
		List<WebElement> elementToClick = getElements(By.xpath(xpath));
		for (WebElement AllCheck : elementToClick) {
			AllCheck.click();
		}
	}

	// to check a checkbox according to the given flag
	public static void checkbox_set(String checkboxes, boolean flag) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) sDriver;
			List<WebElement> elementToClick = sDriver.findElements(By.xpath(checkboxes));
			for (WebElement filterCheck : elementToClick) {
				// System.out.println(filterCheck.getAttribute("id") + " " +
				// filterCheck.isSelected());
				staticWait(1);
				if (filterCheck.isSelected() != flag) {
					js.executeScript("document.getElementById('" + filterCheck.getAttribute("id") + "').click()");

				}
			}
		} catch (Exception e) {
			CustomReporter.MessageLogger(e.getStackTrace() + "\n", CustomReporter.status.Error);
		}
	}

	// to generate a random number within a particular range (a-b)
	public static int generateRandom(int a, int b) {
		Random rand = new Random();
		int randomNum = rand.nextInt((b - a) + 1) + a;
		return randomNum;

	}

	// to generate a random text for case 1 and random number for case 2
	public static String RandomText(int AlphaNumaric, int var) {
		String opstrnum = "";
		int i, n;
		if (!(HashTableRepository.findHash("RandomNum"))) {
			HashTableRepository.setHash("RandomNum", "");
		}
		switch (AlphaNumaric) {
		case 1: // String
			String Letters = RandomStringUtils.randomAlphabetic(var);
			if (var > Config.props.getProperty("TextPre").length() + 2) {
				opstrnum = Config.props.getProperty("TextPre") + "_" + Letters.substring(Config.props.getProperty("TextPre").length() + 1);
			} else
				opstrnum = Letters;
			break;
		case 2: // Number
			opstrnum = "";
			final String Number = "0123456789346686923556588089823562362353647852353464758123129078975673464756856";
			for (i = 0; i < var; i++) {
				n = (int) (Math.random() * 10);
				if (i == 0) {
					opstrnum = opstrnum + Number.replaceAll("0", "").charAt(n);
				} else {
					opstrnum = opstrnum + Number.charAt(n);
				}
			}
			break;
		}
		return opstrnum;
	}

	// to drag and drop a popup or alert boxes
	public static void dragdrop(String id, int x, int y) {
		WebElement dragele = sDriver.findElement(By.id(id));
		Actions builder = new Actions(sDriver);
		builder.dragAndDropBy(dragele, x, y).build().perform();
	}

	// to format the logs
	public static String logFormator(String formatingText) {
		return "<B>" + formatingText + "</B>";
	}

	// to find the index of a particular data by xpath of data
	public static int findDataIndex(String elementdetail, String tableXpathRow, String data) {
		int rowIndex = 0;
		boolean flag = false;
		int numOfRows = getRowCount(tableXpathRow);
		for (int i = rowIndex; i < numOfRows - 1; i++) {
			for (int col = 0; col <= elementdetail.split(";").length - 1; col++) {
				String str = getText(elementdetail.split(";")[col].toString() + i + "");
				if (str.equals(data.split(";")[col])) {
					rowIndex = i;
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
			if (flag == true) {
				break;
			}

		}

		return rowIndex;
	}

	public enum ElementStatus {
		VISIBLE, NOTVISIBLE, ENABLED, NOTENABLED, PRESENT, NOTPRESENT
	}

	public static ElementStatus isElementVisible(By by, ElementStatus getStatus) {
		try {
			if (getStatus.equals(ElementStatus.ENABLED)) {
				if (sDriver.findElement(by).isEnabled())
					return ElementStatus.ENABLED;
				return ElementStatus.NOTENABLED;
			}
			if (getStatus.equals(ElementStatus.VISIBLE)) {
				if (sDriver.findElement(by).isDisplayed())
					return ElementStatus.VISIBLE;
				return ElementStatus.NOTVISIBLE;
			}
			return ElementStatus.PRESENT;
		} catch (org.openqa.selenium.NoSuchElementException nse) {
			return ElementStatus.NOTPRESENT;
		}
	}

	// to save the reports in a report folder
	public static boolean saveReport(String ReportName, boolean direct) {
		try {

			String windowLocator = "";
			int Count = 0;
			String sfilter = "viewPdf*";
			if (direct == false) {
				sfilter = "report*";
			}
			if (Config.props.getProperty("iBrowser").equals("IE")) {

				windowLocator = CommonLib.getLatestWindowHandle();
				if (!(windowLocator.equals(""))) {
					CommonLib.switchWindow(windowLocator);

					CustomReporter.MessageLogger(logFormator(ReportName) + " could not be saved on IE.", CustomReporter.status.Warning);

					sDriver.close();
				}
				CommonLib.switchWindow(HashTableRepository.getHash("MainWindow"));
				CommonLib.setFrame("rbottom");

			} else {

				FilenameFilter filenameFilter = new WildcardFileFilter(sfilter);
				File dir = new File(HashTableRepository.getHash("ReportFolder") + "\\Reports\\");
				String[] pdfFileNames;
				while (true) {
					pdfFileNames = dir.list(filenameFilter);
					if ((pdfFileNames.length > 0)) {
						// System.out.print(pdfFileNames.toString());
						// System.out.print(Count);
						break;
					} else {
						staticWait(2);
						Count++;
						if (Count > 100) {
							CustomReporter.MessageLogger(logFormator(ReportName) + " could not be generated.", CustomReporter.status.Fail);
							break;
						}
					}
				}
				if (pdfFileNames.length > 0) {
					staticWait(2);
					for (String pdfName : pdfFileNames) {
						File reportFile = new File(HashTableRepository.getHash("ReportFolder") + "\\Reports\\" + pdfName);
						File NewReportFile = new File(HashTableRepository.getHash("ReportFolder") + "\\Reports\\" + ReportName + ".pdf");
						reportFile.renameTo(NewReportFile);
					}
				}
				windowLocator = CommonLib.getLatestWindowHandle();
				if (!(windowLocator.equals(""))) {
					staticWait(2);
					CommonLib.switchWindow(windowLocator);
					sDriver.close();
				}
				CommonLib.switchWindow(HashTableRepository.getHash("MainWindow"));
				CommonLib.setFrame("rbottom");
			}
			return true;
		} catch (Exception e) {
			CustomReporter.MessageLogger(e.getMessage(), CustomReporter.status.Error);
			e.printStackTrace();
			return false;
		}
	}

	// public static WebElement getElement(String xPath) {
	// staticWait(1);
	// return sDriver.findElement(By.xpath(xPath));
	// }

	public static String protectSpecialCharacters(String originalUnprotectedString) {
		if (originalUnprotectedString == null) {
			return null;
		}
		boolean anyCharactersProtected = false;

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < originalUnprotectedString.length(); i++) {
			char ch = originalUnprotectedString.charAt(i);

			boolean controlCharacter = ch < 32;
			boolean unicodeButNotAscii = ch > 126;
			boolean characterWithSpecialMeaningInXML = ch == '<' || ch == '&' || ch == '>';

			if (characterWithSpecialMeaningInXML || unicodeButNotAscii || controlCharacter) {
				stringBuffer.append("&#" + (int) ch + ";");
				anyCharactersProtected = true;
			} else {
				stringBuffer.append(ch);
			}
		}
		if (anyCharactersProtected == false) {
			return originalUnprotectedString;
		}
		return stringBuffer.toString();
	}

	public static char getRandomCharacter() {
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int n = alpha.length();
		Random random = new Random();
		int x = random.nextInt(n);
		char rchar = alpha.charAt(x);
		return rchar;
	}

	// public static String selectValueDojo(String id, String searchValue)
	// throws InterruptedException {
	// String sXpath = "//input[@id='" + id + "' and contains(@class,'dijitReset
	// dijitInputInner')]";
	// sDriver.findElement(By.xpath(sXpath)).click();
	// for (int intC = 0; intC < ((Integer.parseInt(RandomText(2, 3)) % 10) +
	// 2); intC++) {
	// staticWait(2);
	// sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.ARROW_DOWN);
	// }
	// staticWait(3);
	// sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.RETURN);
	// staticWait(2);
	// return sDriver.findElement(By.xpath(sXpath)).getText();
	//
	// }

	public static String selectDojoListValue(String id, String searchValue) throws InterruptedException {
		WebElement listObject;
		String sXpath = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		listObject = sDriver.findElement(By.xpath(sXpath));
		listObject.clear();
		listObject.click();
		staticWait(1);
		listObject.sendKeys(Keys.ARROW_DOWN);
		staticWait(2);
		int count = dojoListCount(id);
		if (count < 3) {
			return null;
		}
		int finalcount = count - 3;
		Random rand = new Random();
		int randomNumber = rand.nextInt(finalcount - 2) + 2;
		for (int intC = 0; intC < randomNumber; intC++) {
			staticWait(1);
			listObject.sendKeys(Keys.ARROW_DOWN);
		}
		listObject.sendKeys(Keys.RETURN);
		staticWait(1);
		JavascriptExecutor js = (JavascriptExecutor) sDriver;
		String dojoValue = (String) js.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		CustomReporter.MessageLogger("For " + id + " dropdown, list count is : " + (count - 4) + " and the selected value is : " + logFormator(dojoValue), CustomReporter.status.Information);
		return dojoValue;
	}

	public static String selectDojoListValueWithoutClearing(String id, String searchValue) throws InterruptedException {
		WebElement listObject;
		String sXpath = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		listObject = sDriver.findElement(By.xpath(sXpath));
		// listObject.clear();
		/*****************/
		listObject.click();
		staticWait(1);
		listObject.sendKeys(Keys.ARROW_DOWN);
		staticWait(2);
		int count = dojoListCount(id);
		if (count < 3) {
			return null;
		}
		int finalcount = count - 3;
		Random rand = new Random();
		int randomNumber = rand.nextInt(finalcount - 2) + 2;
		for (int intC = 0; intC < randomNumber; intC++) {
			staticWait(1);
			listObject.sendKeys(Keys.ARROW_DOWN);
		}
		listObject.sendKeys(Keys.RETURN);
		staticWait(1);
		JavascriptExecutor js = (JavascriptExecutor) sDriver;
		String dojoValue = (String) js.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		CustomReporter.MessageLogger("For " + id + " dropdown, list count is : " + (count - 4) + " and the selected value is : " + logFormator(dojoValue), CustomReporter.status.Information);
		return dojoValue;
	}

	public static int dojoListCount(String id) {
		String xpath = id + "_popup";
		int count = sDriver.findElements(By.xpath("//div[contains(@id, '" + xpath + "')]")).size();
		return count;
	}

	public static String selDojoListValue(String id, int randomNumber) throws InterruptedException {
		WebElement listObject;
		String sXpath = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		listObject = CommonLib.GetDriver().findElement(By.xpath(sXpath));
		listObject.clear();
		listObject.sendKeys("");
		staticWait(2);
		Actions action = new Actions(sDriver);
		action.doubleClick(listObject);
		char ch = getRandomCharacter();
		String s = new StringBuilder().append(ch).toString();
		// listObject.click();
		listObject.sendKeys(s);
		staticWait(2);
		int count = dojoListCount(id);
		if (count < 3) {
			return null;
		}
		// int finalcount = count - 3;
		// Random rand = new Random();
		// int randomNumber = rand.nextInt(finalcount - 2) + 2;

		for (int intC = 0; intC < randomNumber; intC++) {
			staticWait(1);
			listObject.sendKeys(Keys.ARROW_DOWN);
		}
		listObject.sendKeys(Keys.RETURN);

		JavascriptExecutor js = (JavascriptExecutor) CommonLib.GetDriver();
		String dojoValue = (String) js.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		CustomReporter.MessageLogger("For " + id + " dropdown, list count is : " + (count - 4) + " and the selected value is : " + logFormator(dojoValue), CustomReporter.status.Information);
		return dojoValue;

	}

	// Function Name : systemTime
	// Parameter : String format
	// Created By : Arya J Anil
	// Purpose : Returns the system date according to the format passed as
	// argument(24 hours or 12 hours)
	public static String systemTime(String format) {
		Calendar cal = Calendar.getInstance();
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("mm");
			String time = Calendar.HOUR + ":" + sdf.format(cal.getTime());
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			// //System.out.println(sdf.format(cal.getTime()));
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	public static String selectRequiredDojoListValue(String id, String data_to_set) {
		WebElement wb;
		String xpathstring = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		wb = sDriver.findElement(By.xpath(xpathstring));
		wb.clear();
		wb.click();
		staticWait(2);
		JavascriptExecutor jse = (JavascriptExecutor) sDriver;
		jse.executeScript("dijit.byId('" + id + "').setDisplayedValue('" + data_to_set + "')");
		staticWait(2);
		String get_list_value = (String) jse.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		return get_list_value;

	}

	// Function Name : addDaysToCurrent
	// Parameter : int days
	// Created By : Arya J Anil
	// Purpose : Adds days to the current date
	public static String addDaysToCurrent(int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, days);
		String modifiedDate = sdf.format(cal.getTime());
		// System.out.println(modifiedDate);
		return modifiedDate;

	}

	public static String selectDojoListByXpath(String xpath, String id) throws InterruptedException {
		WebElement listObject;
		listObject = sDriver.findElement(By.xpath(xpath));
		listObject.click();
		staticWait(2);
		listObject.sendKeys(Keys.ARROW_DOWN);
		staticWait(2);
		int count = dojoListCount(id);
		if (count < 4) {
			return null;
		}
		int finalcount = count - 3;
		Random rand = new Random();
		int randomNumber = rand.nextInt(finalcount - 2) + 2;
		for (int intC = 0; intC < randomNumber; intC++) {
			staticWait(2);
			listObject.sendKeys(Keys.ARROW_DOWN);
		}
		listObject.sendKeys(Keys.RETURN);
		staticWait(1);
		JavascriptExecutor js = (JavascriptExecutor) sDriver;
		String dojoValue = (String) js.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		CustomReporter.MessageLogger("For " + id + " dropdown, list count is : " + (count - 4) + " and the selected value is : " + logFormator(dojoValue), CustomReporter.status.Information);
		return dojoValue;

	}

	public static String selectDojoDropDownByKeyDownNumber(String id, int downvalue) throws InterruptedException {
		WebElement listObject;
		String sXpath = "//input[@id='" + id + "' and contains(@class,'dijitReset dijitInputInner')]";
		listObject = sDriver.findElement(By.xpath(sXpath));
		listObject.clear();
		listObject.click();
		staticWait(1);
		listObject.sendKeys(Keys.ARROW_DOWN);
		staticWait(2);
		for (int intC = 0; intC < downvalue; intC++) {
			staticWait(1);
			listObject.sendKeys(Keys.ARROW_DOWN);
		}
		listObject.sendKeys(Keys.RETURN);
		staticWait(1);
		listObject.sendKeys(Keys.TAB);
		staticWait(1);
		JavascriptExecutor js = (JavascriptExecutor) sDriver;
		String dojoValue = (String) js.executeScript("return dijit.byId('" + id + "').get('displayedValue')");
		CustomReporter.MessageLogger("For " + id + " dropdown, selected value is : " + logFormator(dojoValue), CustomReporter.status.Information);
		return dojoValue;

	}

	// to select the value of dojo elements by xpath
	public static String selectValueDojo(String sXpath, String searchValue) {
		sXpath = "//input[@id='" + sXpath + "' and contains(@class,'dijitReset dijitInputInner')]";
		sDriver.findElement(By.xpath(sXpath)).click();
		for (int intC = 0; intC < ((Integer.parseInt(RandomText(2, 3)) % 10) + 2); intC++) {
			staticWait(1);
			sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.ARROW_DOWN);
		}
		staticWait(1);
		sDriver.findElement(By.xpath(sXpath)).sendKeys(Keys.RETURN);
		staticWait(1);
		return sDriver.findElement(By.xpath(sXpath)).getText();
	}

	// to set the value of dojo elements by xpath
	public static String setValueDojo(String sXpath, String searchValue) {
		sDriver.findElement(By.xpath(sXpath)).click();
		setValue("", sXpath, searchValue);
		return searchValue;
	}

	// Function Name : systemTimeMinusMinutes
	// Parameter : String format
	// Created By : Arya J Anil
	// Purpose : RETURNS SYSTEM TIME MINUS MINUTES
	public static String systemTimeMinusMinutes(String format, int minusMinutes, int days, String number) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String currentTime;
		if (format.equals("12 hour")) {
			currentTime = sdf1.format(cal.getTime());
		} else
			currentTime = sdf2.format(cal.getTime());
		cal.add(Calendar.MINUTE, minusMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			String time = sdf.format(cal.getTime());
			if (currentTime.contains("AM") && time.contains("PM")) {
				HashTableRepository.setHash("vitalDateKpi" + number, CommonLib.addDaysToCurrent(days - 1));
			}
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	public static String systemTimeMinusHoursnew(String format, int minusMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, minusMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
			String time = sdf.format(cal.getTime());
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	public static String systemTimePlusMinutes(String format, int minusMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minusMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
			String time = sdf.format(cal.getTime());
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	// Function Name : systemTimeMinusHours
	// Parameter : String format
	// Created By : Arya J Anil
	// Purpose : RETURNS SYSTEM TIME MINUS HOURS
	public static String systemTimeMinusHours(String format, int minusMinutes, int days, String number) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String currentTime;
		if (format.equals("12 hour")) {
			currentTime = sdf1.format(cal.getTime());
		} else
			currentTime = sdf2.format(cal.getTime());
		cal.add(Calendar.HOUR, minusMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			String time = sdf.format(cal.getTime());
			if (currentTime.contains("AM") && time.contains("PM")) {
				HashTableRepository.setHash("vitalDateKpi" + number, CommonLib.addDaysToCurrent(days - 1));
			}
			return time;

		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(cal.getTime());
			return time;
		}
	}

	public static String systemTimeAddMinute(String hours) {
		int intHour = Integer.parseInt(hours);

		String time = systemTimePlusMinutes("", intHour);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(d);
		cal.add(Calendar.MINUTE, intHour);
		SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
		String newtime = sdfh.format(cal.getTime());
		return newtime;
	}

	public static String systemTimeAddHours(String hours) {
		int intHour = Integer.parseInt(hours);

		String time = systemTimeMinusHoursnew("", intHour);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(d);
		cal.add(Calendar.HOUR, intHour);
		SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
		String newtime = sdfh.format(cal.getTime());
		return newtime;
	}

	// Function Name : systemTimeAddHoursMinutes
	// Parameter : String format
	// Created By : Arya J Anil
	// Purpose : RETURNS SYSTEM TIME MIN
	public static String systemTimeAddHoursMinutes(String format, String hours, String minutes, int days, String number) {
		int intHour = Integer.parseInt(hours);
		int intMinutes = Integer.parseInt(minutes);
		String time = systemTimeMinusHours(format, intHour, days, number);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(d);
		cal.add(Calendar.MINUTE, intMinutes);
		if (format.equals("12 hour")) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
			String newtime = sdf1.format(cal.getTime());
			if (time.contains("AM") && time.contains("PM")) {
				HashTableRepository.setHash("vitalDateKpi" + number, CommonLib.addDaysToCurrent(days - 1));
			}
			return newtime;

		} else {
			SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm");
			String newtime = sdfh.format(cal.getTime());
			return newtime;
		}
	}

	public static void doubleClick(String xpath) {
		// Actions action = new Actions(sDriver);
		// action.doubleClick(getElement(By.xpath(xpath)));
		Actions action = new Actions(sDriver);
		WebElement element = sDriver.findElement(By.xpath(xpath));
		// Double click
		action.doubleClick(element).perform();
	}

	public static boolean checkSorryError(String GridName) {
		// boolean bError = false;
		try {
			changeimplicitwait(1);
			if ((CommonLib.getElement(By.xpath("//div[@id='" + GridName + "']//div//div[3]//span")).getText()).equals("Sorry, an error occurred")) {
				CustomReporter.MessageLogger("Patient found in Pending Orders. Sorry an error occured! ", CustomReporter.status.Fail);
				resetImplicitWait();
				return false;
			}
		} catch (Exception ex) {
		}

		resetImplicitWait();
		return true;

	}

	public static void setWindowHandleToLast() {
		CommonLib.staticWait(7);
		Set<String> handles = CommonLib.GetDriver().getWindowHandles();
		for (String handle : handles) {
			CommonLib.staticWait(3);
			CommonLib.GetDriver().switchTo().window(handle).manage().window().maximize();
			CommonLib.staticWait(2);

		}
	}

	public static void clickOkNewPatientCopyWindow() {
		try {
			String mainWinHander = HashTableRepository.getHash("MainWindow");
			Set<String> handles = CommonLib.GetDriver().getWindowHandles();
			for (String handle : handles) {
				if (!mainWinHander.equals(handle)) {
					CommonLib.GetDriver().switchTo().window(handle);
					for (int i = 0; i < 6; i++) {
						try {
							CommonLib.clickButton(By.id("apply"));
							staticWait(10);
						} catch (Exception e) {

						}
					}

					break;
				}
			}
			CommonLib.switchWindow(mainWinHander);

		} catch (Exception e) {
		}

	}

	public static boolean closeLastWindow() {
		boolean windowClosed = false;
		int windowCountinit;
		int windowCountfinal;
		try {
			String mainWinHander = HashTableRepository.getHash("MainWindow");
			Set<String> handles = CommonLib.GetDriver().getWindowHandles();
			windowCountinit = CommonLib.getWindowHandleSize();
			for (String handle : handles) {
				if (!mainWinHander.equals(handle)) {
					WebDriver popup = CommonLib.switchWindow(handle);
					popup.close();
					break;
				}
			}
			windowCountfinal = CommonLib.getWindowHandleSize();
			CommonLib.switchWindow(mainWinHander);
			if (windowCountfinal < windowCountinit)
				windowClosed = true;
		} catch (Exception e) {
		}
		return windowClosed;
	}

	public static void checkBoxSelect(By locator, int number) {
		while (number > 0) {

			WebElement ch = CommonLib.getElement(locator);
			ch.click();
			String checkStatus = ch.getAttribute("class");

			if (checkStatus.contains("Checked"))
				break;

			CustomReporter.MessageLogger("number: " + number, status.Information);
			number--;
		}
	}

	public static void checkBoxUnselect(By locator, int number) {
		while (number > 0) {

			WebElement ch = CommonLib.getElement(locator);
			ch.click();
			String checkStatus = ch.getAttribute("class");

			if (!checkStatus.contains("Checked"))
				break;

			CustomReporter.MessageLogger("number: " + number, status.Information);
			number--;
		}
	}

	public static void acceptAlert() {

		Alert alert = sDriver.switchTo().alert();
		alert.accept();

	}

	public static void rightClickToElement(By locator) {
		WebElement web_ele = CommonLib.getElement(locator);
		web_ele.sendKeys(Keys.chord(Keys.SHIFT, Keys.F10));
	}

	public static void CheckImage(By elementLocator, String reporter) {
		WebElement ImageFile = getElement(elementLocator);
		Boolean ImagePresent = (Boolean) ((JavascriptExecutor) sDriver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", ImageFile);
		if (!ImagePresent) {
			System.out.println("Image not displayed.");
			CustomReporter.MessageLogger("" + reporter + " is not working properly", status.Fail);
		} else {
			System.out.println("Image displayed.");
			CustomReporter.MessageLogger("" + reporter + " is working properly", status.Pass);

		}
	}

	// Dynamic Wait
	public static void DynamicWait(int no, By locator) {
		WebDriverWait wait = new WebDriverWait(CommonLib.GetDriver(), 12);

		switch (no) {
		case 1: // presenceOfElementLocated
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			break;
		case 2: // invisibilityOfElementLocated
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			break;
		case 3: // alertIsPresent
			wait.until(ExpectedConditions.alertIsPresent());
		case 4:// visibilityOfElementLocated
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		case 5:// elementToBeClickable
			wait.until(ExpectedConditions.elementToBeClickable(locator));

		}

	}

	public static String getLatestWindowHandle1() {
		Set<String> windowHandle;
		Iterator<String> itr;
		String windowLocator = null;
		windowHandle = sDriver.getWindowHandles();
		itr = windowHandle.iterator();
		if (windowHandle.size() > 1) {
			for (int Intc = 1; Intc <= windowHandle.size(); Intc++) {
				windowLocator = itr.next();
			}

		} else {
			windowLocator = "";
		}
		return windowLocator;
	}

	public static void setFrameFromCurrent(String string) {
		staticWait(1);
		sDriver.switchTo().frame(string);
		staticWait(1);
	}
		public static boolean checkSearchPatient() {
		boolean flag = false;
		List<WebElement> li = CommonLib.getElements(By.xpath("//th[contains(text(),'Patient Search')]"));
		if (li.size() != 0) {
			flag = true;
		}
		return flag;

	}
}
