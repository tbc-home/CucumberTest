package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RandomFillwithText {
	String maxsize;
	String attval;
	String caseval;
	// int count = 0;
	String finalval;

	public void FillAllElement() {

		List<WebElement> elements = CommonLib.getElements(By.xpath("//input[contains(@class,'txtTextBox')]"));
		// elements.size();
		int TabIndex = 0;
		Map<String, List<String>> hm = new HashMap<String, List<String>>();

		for (WebElement elem : elements) {
			if (elem.getAttribute("tabindex") != null) {
				List<String> values = new ArrayList<String>();
				values.add(elem.getAttribute("name"));
				values.add(elem.getAttribute("class"));

				if (TabIndex < Integer.parseInt(elem.getAttribute("tabindex"))) {
					TabIndex = Integer.parseInt(elem.getAttribute("tabindex"));
				}
				hm.put(elem.getAttribute("tabindex"), values);
			}
		}

		elements = CommonLib.getElements(By.xpath("//select[contains(@class,'cboComboBox')]"));

		for (WebElement elem : elements) {
			if (elem.getAttribute("tabindex") != null) {
				List<String> values = new ArrayList<String>();
				values.add(elem.getAttribute("name"));
				values.add(elem.getAttribute("class"));

				if (TabIndex < Integer.parseInt(elem.getAttribute("tabindex"))) {
					TabIndex = Integer.parseInt(elem.getAttribute("tabindex"));
				}
				hm.put(elem.getAttribute("tabindex"), values);
			}
		}

		for (int IntC = 1; IntC <= TabIndex; IntC++) {
			if (hm.containsKey(String.valueOf(IntC))) {
				List<String> elemet = hm.get(String.valueOf(IntC));
				if (elemet.get(1).contains("txtTextBox")) {
					FillText(elemet.get(0));
				} else {
					CommonLib.setRandomDropDown(By.id(elemet.get(0)));
				}

			}

		}

	}

	public void FillText(String elementName) {

		attval = elementName;// text.getAttribute(Config.Repository.getProperty("attrname"));

		if (!(attval.equals("grdRoles$ctl03$txtFooterRank"))) {

			if ((attval.toLowerCase().contains(Config.Repository.getProperty("attrzip"))) || (attval.toLowerCase().contains(Config.Repository.getProperty("attrphone")))
					|| (attval.toLowerCase().contains(Config.Repository.getProperty("attrnum"))) || (attval.toLowerCase().contains(Config.Repository.getProperty("attrno")))
					|| (attval.toLowerCase().contains(Config.Repository.getProperty("attrother")))) {
				caseval = "NUM";

			} else if (attval.toLowerCase().contains(Config.Repository.getProperty("attrdate"))) {
				caseval = "DATE"; // bithdate
			} else {
				caseval = "TEXT";
			}
			// count++;
			SelCasetoFillText(caseval);
		}

	}

	public void SelCasetoFillText(String caseval) {

		switch (caseval) {
		case "NUM":

			if (attval.toLowerCase().contains(Config.Repository.getProperty("attrzip"))) {
				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(2, 5));
			} else if (attval.toLowerCase().contains(Config.Repository.getProperty("attrphone"))) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(2, 10));
			}

			else if (attval.toLowerCase().contains(Config.Repository.getProperty("attrnum"))) {
				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(2, 9));
			}

			else if ((attval.toLowerCase().contains(Config.Repository.getProperty("attrno"))) && (attval.toLowerCase().contains(Config.Repository.getProperty("attrmedicare")))) {
				finalval = CommonLib.setValue("", "//input[contains(@name,'txtMedicareNo')]", CommonLib.RandomText(2, 3) + "-" + CommonLib.RandomText(2, 2) + "-" + CommonLib.RandomText(2, 4) + CommonLib.RandomText(1, 1));
			}

			else if ((attval.toLowerCase().contains(Config.Repository.getProperty("attrno"))) && (!attval.toLowerCase().contains(Config.Repository.getProperty("attrmedicare")))) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(2, 9));
			}

			else if (attval.toLowerCase().contains(Config.Repository.getProperty("attrother"))) {
				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(2, 9));
			}
			break;

		case "TEXT":

			if (attval.toLowerCase().contains(Config.Repository.getProperty("attrsuffix"))) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(1, 2));
			} else if (attval.toLowerCase().contains(Config.Repository.getProperty("attrtitle"))) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.RandomText(1, 3));
			} else if (attval.toLowerCase().contains("email")) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", Config.props.getProperty("TextPre") + CommonLib.RandomText(1, 3) + "@xyz.com");
			}

			else {
				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", Config.props.getProperty("TextPre") + CommonLib.RandomText(1, 4));
				// if ((i == 17) || (i == 2)) {
				if (attval.contains("Address"))
					HashTableRepository.setHash("Address", finalval);
				if (attval.contains("OrgName"))
					HashTableRepository.setHash("OrgName", finalval);
				// }
			}

			break;

		case "DATE":

			if (attval.toLowerCase().contains(Config.Repository.getProperty("attrbdy"))) {

				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.getcurrentdatetime("MM/dd/yyyy", -1 * Integer.parseInt(CommonLib.RandomText(2, 4)), "txtBDate", 0));
				HashTableRepository.setHash("bdate", finalval);
			}

			else {
				finalval = CommonLib.setValue("", "//input[contains(@name,'" + attval + "')]", CommonLib.getcurrentdatetime("MM/dd/yyyy", -1, attval, 0));
			}

			break;

		default:

			CustomReporter.MessageLogger("not finding fields to fill", CustomReporter.status.Error);
			break;

		}
	}

	public void saveinfo() {
		CommonLib.clickButton(By.id("btnSave"));
		try {
			CommonLib.setFrame(By.xpath("//iframe[contains(@src,'/Netsolutions/ADT/OfferNewVisit.aspx')]"));
			CommonLib.selectradio(By.id("optNewRes"));
			CommonLib.clickButton(By.xpath("//span[@id='lblMessage']/following::input[@id='btnFinish']"));
			CommonLib.setFrame("rbottom");
			CommonLib.clickButton(By.xpath("//div[@class='ui-widget-overlay ui-front']//preceding::span[1]"));
			CommonLib.RandomText(2, 9);
			CommonLib.clickButton(By.id("btnSave"));
		} catch (Exception e) {
		}
		CommonLib.setFrame("rbottom");
	}

}
