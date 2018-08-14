package common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import common.CustomReporter.status;

public class TableActions {

	public static int getRowCount(String tableXpathRow) {
		int rowCount = CommonLib.getElements(By.xpath(tableXpathRow)).size();
		return rowCount;
	}

	public void clickWithNExt(String xpathExpression, String category, String id, String name) {
		int index = 0;
		while (true) {
			index = new TableActions().listIndex(CommonLib.getElements(By.xpath(xpathExpression)), category);

			if (index == -1) {
				try {
					Library.clickButton(By.xpath("//a[text()='Next>']"), "Next link");
				} catch (Exception e) {
					break;
				}
			} else {
				Library.clickButton(By.id(id + index), name);
				break;
			}
		}
	}

	public void validateAdditionWithNext(String xpathExpression, String data) {
		boolean isNotFound = true;
		boolean isFound = false;
		while (isNotFound) {
			List<WebElement> list = CommonLib.getElements(By.xpath(xpathExpression));
			for (WebElement l1 : list) {
				if (l1.getText().equalsIgnoreCase(data) || l1.getText().toLowerCase().contains(data.toLowerCase())) {
					isFound = true;
					isNotFound = false;
					break;
				}
			}

			if (isFound == false) {
				int nextButton = CommonLib.getElements(By.xpath("//*[contains(text(),'Next')]")).size();
				if (nextButton > 0) {
					CommonLib.clickButton(By.xpath("//*[contains(text(),'Next')]"));
				} else
					break;

			}

		}

		if (isFound == true) {
			CustomReporter.MessageLogger("Data : " + data + " has been added", status.Pass);
		} else

			CustomReporter.MessageLogger("Data : " + data + " has not been added", status.Fail);

	}

	public String editData(String tableXpathRow, String elementId, String data, int flag1) {

		int rowCount = getRowCount(tableXpathRow);

		String afterEdit = data + " " + "Edit";
		int rowIndex = findDataIndex(elementId, rowCount, data);

		String editButton = "//td/span[@id='" + elementId + rowIndex
				+ "']//parent::td//parent::tr/td/input[contains(@src,'../images/grid/icon-edit-18x17.gif')]";

		CommonLib.clickButton(By.xpath(editButton));
		if (flag1 == 0) {

			CommonLib.setValue("", "//tr/td/input[@value='" + data + "']", afterEdit);

			CommonLib.clickButton(By.xpath("//*[@src='../images/im_update.gif']"));
			int flag = findData(elementId, afterEdit, tableXpathRow);
			if (flag == 1)
				CustomReporter.MessageLogger("Data edited successfully", CustomReporter.status.Information);
			else if (flag == 0) {
				CustomReporter.MessageLogger("Data is not edited ", CustomReporter.status.Information);
				CustomReporter.closeTest(Status.FAIL);
			}

		}
		return afterEdit;
	}

	public boolean validateAddition(By elementId, String data) {
		boolean flag = false;

		List<WebElement> l1 = CommonLib.getElements(elementId);

		for (WebElement list1 : l1) {
			if (list1.getText().equals(data)) {

				flag = true;
				break;

			}

		}
		return flag;

	}

	public boolean validateAddition(List<WebElement> dataList, String data) {
		boolean flag = false;

		for (WebElement l1 : dataList) {
			// String tem = l1.getText();
			if (l1.getText().equalsIgnoreCase(data) || l1.getText().toLowerCase().contains(data.toLowerCase())) {

				flag = true;
				break;

			}
		}

		if (flag == true) {
			CustomReporter.MessageLogger("Data : " + data + " has been added", status.Pass);
		} else

			CustomReporter.MessageLogger("Data : " + data + " has not been added", status.Fail);
		return flag;

	}

	public void validateDeletition(List<WebElement> dataList, String data) {
		boolean flag = false;

		for (WebElement l1 : dataList) {
			if (l1.getText().equals(data)) {

				flag = true;
				break;

			}
		}

		if (flag == true) {
			CustomReporter.MessageLogger("Data" + data + " has not been deleted", status.Fail);
		} else

			CustomReporter.MessageLogger("Data" + data + " has  been deleted", status.Pass);

	}

	public int getDataCount(int count, String xpath, String hashTableKey) {

		int initialCount = 0;
		// boolean flag = true;

		List<WebElement> description = CommonLib.getElements(By.xpath(xpath));
		for (WebElement reaction : description) {
			String descriptionText = reaction.getText();

			if (descriptionText.toLowerCase().contains(Config.props.getProperty("TextPre").toLowerCase())) {
				HashTableRepository.setHash(hashTableKey + initialCount, descriptionText);
				initialCount++;
				if (initialCount == count) {
					// flag = false;
					break;
				}
			}

		}
		return initialCount;

	}

	public void deleteData(String tableXpathRow, String elementId, String data) {

		int rowCount = getRowCount(tableXpathRow);

		int rowIndex = findDataIndex(elementId, rowCount, data);
		rowIndex = rowIndex + 2;
		String deleteButton = tableXpathRow + "[" + rowIndex
				+ "]/td[2]/input[@src='../images/grid/icon-delete-18x17.gif']";

		CommonLib.clickButton(By.xpath(deleteButton));
		CommonLib.setFrame(By.xpath("//*[@id='iframeDialog0']"));
		CommonLib.clickButton(By.xpath("//input[@id='button.Submit']"));
		CommonLib.setFrame("rbottom");

	}

	public void validateDeleting(String tableId, String dataID, String dataSearch) {

		int deleteResult = findData(dataID, dataSearch, tableId);

		if (deleteResult == 1) {
			CustomReporter.MessageLogger("Data is not deleted", CustomReporter.status.Information);
			CustomReporter.closeTest(Status.FAIL);
		} else if (deleteResult == 0)
			CustomReporter.MessageLogger("Data is deleted ", CustomReporter.status.Information);

	}

	public int findData(String elementId, String data, String tableXpathRow) {
		String str = null;
		int flag = 0;
		// TestingWebTable obj = new Common();
		int row = CommonLib.getRowCount(tableXpathRow);
		for (int i = 0; i < row - 1; i++) {
			str = CommonLib.getText(elementId + i);
			if (CommonLib.getElement(By.id(elementId + i)).isDisplayed()) {
				str = CommonLib.getText(elementId + i);

				if (str.equals(data)) {
					flag = 1;
					break;
				}
			}
		}
		return flag;
	}

	public int listIndex(By elementAdress, String expectedData) {
		int i = 0;
		int index = -1;
		List<WebElement> list1 = CommonLib.getElements(elementAdress);
		for (WebElement l1 : list1) {
			if (l1.getText().toLowerCase().contains(expectedData.toLowerCase())) {
				index = i;
				break;
			}
			i++;
		}
		return index;
	}

	public int listIndex(List<WebElement> dataList, String expectedData) {
		int index = -1, i = 0;
		for (WebElement l1 : dataList) {
			if (l1.getText().toLowerCase().contains(expectedData.toLowerCase())) {
				index = i;
				break;
			}
			i++;
		}
		return index;
	}

	public static int findDataIndex(String elementId, int numOfRows, String data) {
		int rowIndex = 0;
		for (int i = 0; i <= numOfRows; i++) {
			String str = CommonLib.getText(elementId + i);
			if (str.equals(data)) {
				rowIndex = i;
				break;
			}
		}
		return rowIndex;
	}
}
