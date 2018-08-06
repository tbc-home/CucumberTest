package common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import common.CustomReporter.status;

public class Library {

	public static void setDropDown(By dropDownPath, String value, String dropDownName) {
		CommonLib.setDropDown(dropDownPath, value);
		CustomReporter.MessageLogger("Selected: " + value + " from " + dropDownName + " drop down.", CustomReporter.status.Information);
	}

	public static void setData(By locator, String data, String textBoxName) {

		CommonLib.setData(locator, data);
		if (!data.equals(""))
			CustomReporter.MessageLogger("Entered: " + data + " in " + textBoxName + " field.", status.Information);

	}

	public static void clickButton(By locator, String buttonName) {

		CommonLib.clickButton(locator);
		CustomReporter.MessageLogger("Clicked: " + buttonName, status.Information);

	}

	public static String getAttribute(By locator, String attribute) {
		return CommonLib.GetDriver().findElement(locator).getAttribute(attribute);
	}

	public static void checkCheckBox(By locator, boolean flag, String checkBoxName) {
		WebElement checkBox = CommonLib.getElement(locator);

		boolean initialCheckStatus = checkBox.isSelected();
		if (flag != initialCheckStatus) {
			CommonLib.clickButton(locator);
			CustomReporter.MessageLogger("Clicked: " + checkBoxName, status.Information);
		}

	}
}
