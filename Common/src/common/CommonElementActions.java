package common;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import common.CustomReporter.status;

public class CommonElementActions {

	public static void switchToLastFrame() {
		CommonLib.setFrameToDefault();
		List<WebElement> frames = CommonLib.getElements(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::iframe[1]"));
		CommonLib.GetDriver().switchTo().frame(frames.get(frames.size() - 1));

	}
	// Function Name : setPopUpFrame
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose : CommonFunction for setting pop up frame.

	public static String getConfirmTExt() {
		return CommonLib.getText(By.xpath("//div[@id='divMessage']"));
	}

	public static boolean setPopUpFrame(int interval)

	{
		boolean flag = false;
		CommonLib.changeimplicitwait(interval);
		try {
			CommonLib.setFrame(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::iframe[1]"));
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		CommonLib.resetImplicitWait();
		return flag;
	}

	// Function Name : setBottomFrame
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose : CommonFunction for setting bottom frame.
	public static void setBottomFrame() {
		CommonLib.setFrame("rbottom");
	}

	public static void clickClose() {
		CommonLib.setFrameToDefault();
		CommonLib.clickButton(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::iframe[1]//preceding-sibling::div//button[@title='Close']"));
		setBottomFrame();
	}

	// public static void menuNvaigationCheck(String menu) {
	// if (!HashTableRepository.getHash("CurrentMenu").equals(menu))
	// MenuNavigation.menuNav(menu);
	// }

	public static void accpetPopUp(int interval) {
		{

			CommonLib.changeimplicitwait(interval);
			try {
				CommonLib.setFrame(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::iframe[1]"));
				CommonLib.clickButton(By.id("button.Submit"));
				setBottomFrame();
			} catch (Exception e) {
				setBottomFrame();
			}
			CommonLib.resetImplicitWait();

		}
	}

	public static void acceptYes(int interval) {
		{

			CommonLib.changeimplicitwait(interval);
			try {
				CommonLib.setFrame(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::iframe[1]"));
				CommonLib.clickButton(By.id("cmdYes"));
				setBottomFrame();
			} catch (Exception e) {
				setBottomFrame();
			}
			CommonLib.resetImplicitWait();

		}
	}

	// Function Name : clickSubmitButton
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose : CommonFunction for clicking submit button .
	public static void clickSubmitButton() {

		CommonLib.clickButton(By.id("button.Submit"));
	}

	// Function Name :acceptAlert
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose : CommonFunction for clicking on OK button of alert as alert in
	// NetSolution is not actually a Alert it's just like other webElement. .

	public static void acceptAlert() {
		CommonLib.clickButton(By.xpath("//*[text()='OK']"));
	}

	public static void acceptAlert(int interval)

	{

		CommonLib.changeimplicitwait(interval);
		try {

			CommonLib.clickButton(By.xpath("//*[text()='OK']"));
		} catch (Exception e) {

		}
		CommonLib.resetImplicitWait();
	}

	// Function Name :getAlertText
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose : CommonFunction for getting text of alert as alert in
	// NetSolution is not actually a Alert it's just like other webElement. .
	public static String getAlertText(int interval) {
		CommonLib.changeimplicitwait(interval);
		String text = null;
		try {
			text = CommonLib.getText(By.xpath("//div[@role='dialog']/div[contains(@id,'ui-id-')]"));
		} catch (Exception e) {
		}
		CommonLib.resetImplicitWait();
		return text;
	}

	public static String getAlertText() {
		String text = null;
		try {
			text = CommonLib.getText(By.xpath("//div[@role='dialog']/div[contains(@id,'ui-id-')]"));
		} catch (Exception e) {
			// CustomReporter.MessageLogger("No alert is present", status.Fail);
		}
		return text;
	}

	public static String getAlertTextWithoutTry() {
		String text = null;
		text = CommonLib.getText(By.xpath("//div[@role='dialog']/div[contains(@id,'ui-id-')]"));
		return text;
	}

	// Function Name :clickButtonSave
	// Parameter : None
	// Created By : Shashwat Chadha
	// Purpose :Generic method forclicking save btton
	public static void clickButtonSave() {
		CommonLib.clickButton(By.id("btnSave"));
	}

	public static void clickCancel() {
		Library.clickButton(By.id("button.Cancel"), "Cancel button");
	}

	public static void closeReportFrame() {

		CommonLib.setFrameToDefault();
		CommonLib.clickButton(By.xpath("//button[@title='Close']"));
		CommonLib.setFrame("rbottom");
	}

	public static void closeLastPopUp() {
		CommonLib.setFrameToDefault();
		List<WebElement> close = CommonLib.getElements(By.xpath("//button[@title='Close']"));

		close.get(close.size() - 1).click();

	}

	// Function Name :getalert
	// Parameter : None
	// Created By : Priya Bansal
	// Purpose :getting text of alert in baseline parameter

	public static String getalert() {
		// CommonLib.setFrame("//div[@class='ui-widget-overlay
		// ui-front']//preceding::iframe[1]");
		// CommonLib.setFrame(By.xpath("//*[@id='iframeDialog0']"));
		return CommonLib.getText(By.xpath("//*[@id='divMessage']"));
		// return
		// CommonLib.getText(By.xpath("//div[contains(@id,'divMessage'])"));
	}

	public static String getalertuid() {
		return CommonLib.getText(By.xpath("//*[@id='ui-id-1']"));
	}

	// Function Name :okAlert
	// Parameter : None
	// Created By : Priya Bansal
	// Purpose :click ok {outside baseline message}

	public static void okAlert()

	{
		CommonLib.clickButton(By.xpath("//input[@id='button.Submit']"));
		CommonLib.setFrame("rbottom");
	}

	public static void validateMandatoryFieldMessage(String actualMessage, String expectedMessage) {
		if (actualMessage.contains(expectedMessage))
			CustomReporter.MessageLogger("Mandatory field validation message  is: " + actualMessage, status.Pass);
		else
			CustomReporter.MessageLogger("Mandatory field validation message is: " + actualMessage + " but is should be: " + expectedMessage, status.Fail);
	}

	public static void validateDeleteAlerMessage(String actualMessage, String expectedMessage) {
		if (actualMessage.contains(expectedMessage))
			CustomReporter.MessageLogger("Delete alert message  is: " + actualMessage, status.Pass);
		else
			CustomReporter.MessageLogger("Delete alert message is: " + actualMessage + " but is should be: " + expectedMessage, status.Fail);
	}

	public static void validateDropDownAddition(ArrayList<String> dropDownList, String expectedData) {
		boolean dataFound = false;
		for (String dropDownValue : dropDownList) {
			if (dropDownValue.equals(expectedData)) {
				dataFound = true;
				CustomReporter.MessageLogger("Data has been added in the dropdown", status.Pass);
			}
		}

		if (dataFound == false)
			CustomReporter.MessageLogger("Data has not been added in the dropdown", status.Fail);

	}

	public static void validatedropDownDeletion(ArrayList<String> dropDownList, String expectedData) {
		boolean dataFound = false;
		for (String dropDownValue : dropDownList) {
			if (dropDownValue.equals(expectedData)) {
				dataFound = true;
				CustomReporter.MessageLogger("Data has not been deleted from the dropdown", status.Fail);
			}
		}

		if (dataFound == false)
			CustomReporter.MessageLogger("Data has  been deleted from the dropdown", status.Pass);

	}

	public static void validateDialogTitle(String expectedTitle) {
		CommonLib.setFrameToDefault();
		String popupText = CommonLib.getText(By.xpath("//span[@class='ui-dialog-title']"));
		if (popupText.equals(expectedTitle))
			CustomReporter.MessageLogger(popupText + " dialogbox opened", status.Information);

		else
			CustomReporter.MessageLogger("Dialogbox title should be " + expectedTitle + " but it is " + popupText, status.Information);

		CommonElementActions.setPopUpFrame(5);
	}

	// public static void waitUntil(int timeOutInSeconds,ExpectedCondition
	// expectedCondition)
	// {
	// WebDriverWait wait = new WebDriverWait(CommonLib.GetDriver(),
	// timeOutInSeconds);
	// wait.until(expectedCondition);
	// }
	public static void menuNvaigationCheck(String menu) {
		if (!HashTableRepository.getHash("CurrentMenu").equals(menu))
			MenuNavigation.menuNav(menu);
	}

}
