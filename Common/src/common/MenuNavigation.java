package common;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class MenuNavigation {

	public static void menuNav(String menuID) {
		String MenuTab = "";
		if (Config.props.getProperty("Version") != null) {
			MenuTab = "_" + Config.props.getProperty("Version");
		}

		ExcelProcessing objExcel = new ExcelProcessing("TestData/TestData.xlsx", "Menu_Nav" + MenuTab);
		try {
			String strHtml = "";
			boolean navigatemenu = true;
			String[][] sMenu = new String[1][5];
			sMenu = objExcel.findCell(menuID, 0, 1, 5);
			if (Config.props.getProperty("SystemUnderTest").equals("NetSolution")) {

				if (HashTableRepository.findHash("CurrentMenu")) {
					if (HashTableRepository.getHash("CurrentMenu").equals(menuID)) {
						navigatemenu = false;
					}
				}

				if (navigatemenu) {
					if (Config.props.getProperty("Version").equals("Old")) {
						CommonLib.setFrame("rtop");
						CommonLib.LinkClick(By.xpath("(//a[contains(text(), '" + sMenu[0][0] + "')][" + 1 + "])"));
						CommonLib.setFrame("left");
						for (int intC = 1; intC < 4; intC++) {
							if (sMenu[0][intC] != "") {

								strHtml = CommonLib.GetParent(sMenu[0][intC]).getAttribute("innerHTML");
								if (strHtml.contains("dnarrow.gif") == false) {
									if (((intC == 3) || (intC < 3 && sMenu[0][intC + 1].equals(""))) && (!(sMenu[0][4].equals("")))) {
										CommonLib.LinkClick(By.xpath("//a[contains(@href, '" + sMenu[0][4] + "') and contains(text(),'" + sMenu[0][intC] + "') ]"));
									} else {
										CommonLib.LinkClick(By.xpath("//a[contains(text(), '" + sMenu[0][intC] + "')]"));
									}
								}
								if ((intC == 1) && (sMenu[0][intC + 1] == "") && (!(sMenu[0][4].equals("")))) {
									CommonLib.LinkClick(By.xpath("//a[contains(@href, '" + sMenu[0][4] + "')  ]"));
								}
							}
						}
					} else {
						CommonLib.setFrame("rtop");
						WebElement topMenu = CommonLib.getElement(By.xpath("(//a[text()= '" + sMenu[0][0] + "'])"));
						topMenu.click();
						// CommonLib.LinkClick(By.xpath("(//a[contains(text(),
						// '" + sMenu[0][0] + "')][" + 1 + "])"));
						CommonLib.staticWait(10);
						// CommonLib.LinkClick(By.xpath("(//a[contains(text(),
						// '" + sMenu[0][1] + "')][" + 1 + "])"));
						if (!(sMenu[0][1].equals(""))) {
							WebElement SecondLevel = CommonLib.getElement(By.xpath("(//a[text()='" + sMenu[0][1] + "'])"));
							SecondLevel.click();
						}
						CommonLib.staticWait(10);
						CommonLib.setFrameToDefault();
						for (int intC = 2; intC < 4; intC++) {
							if (sMenu[0][intC] != "") {

								if (!(sMenu[0][4].equals(""))) {
									CommonLib.LinkClick(By.xpath("//a[contains( @linkinfo, '" + sMenu[0][4] + "' )]"));
									break;
								} else {
									CommonLib.LinkClick(By.xpath("//a[text()= '" + sMenu[0][intC] + "']"));
								}
							} else {
								break;
							}
							Thread.sleep(CommonLib.interval * 4);
						}
					}
					HashTableRepository.setHash("CurrentMenu", menuID);
				}
				CommonLib.setFrame("rbottom");
			} else {
				CommonLib.staticWait(5);
				CommonLib.setFrameToDefault();
				WebElement element;
				for (int intC = 0; intC < 5; intC++) {
					if (!(sMenu[0][intC].equals(""))) {
						
						// changed********
						
						String str = sMenu[0][intC];
						
						if(str.startsWith("/")) {
							element = CommonLib.getElement(By.xpath(str));
							
						}
						
						// changed********
						else {
							element = CommonLib.getElement(By.xpath("//td[text()='" + sMenu[0][intC] + "']"));
						}
						JavascriptExecutor js = (JavascriptExecutor) CommonLib.GetDriver();
						String mouseOverScript = "if(document.createEvent){var evObj=document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);}else if(document.createEventObject) {arguments[0].fireEvent('onmouseover');}";
						js.executeScript(mouseOverScript, element);
						String onClickScript = "if(document.createEvent){var evObj=document.createEvent('MouseEvents');evObj.initEvent('click',true, false); arguments[0].dispatchEvent(evObj);}else if(document.createEventObject) {arguments[0].fireEvent('onclick');}";
						js.executeScript(onClickScript, element);
						CustomReporter.MessageLogger("Menu :" + CommonLib.logFormator(sMenu[0][intC]) + " Clicked !", CustomReporter.status.Information);

					} else {
						break;
					}
				}
				CommonLib.setFrame(Config.props.getProperty("MainFrame"));
				CommonLib.staticWait(3);
				// Thread.sleep(CommonLib.interval * 5);
				// CommonLib.setFrameToDefault();
				// WebElement element;
				// // To do : Have to work on the level 3 menu.
				// for (int intC = 0; intC < 5; intC++) {
				// if (!(sMenu[0][intC].equals(""))) {
				// // Thread.sleep(CommonLib.interval * 4);
				// element =
				// CommonLib.getElement(By.xpath("//td[starts-with(text(),'" +
				// sMenu[0][intC] + "')]"));
				//
				// JavascriptExecutor js = (JavascriptExecutor)
				// CommonLib.GetDriver();
				// String mouseOverScript = "if(document.createEvent){var evObj
				// =
				// document.createEvent('MouseEvents');evObj.initEvent('mouseover',
				// true, false); arguments[0].dispatchEvent(evObj);} else
				// if(document.createEventObject) {
				// arguments[0].fireEvent('onmouseover');}";
				// js.executeScript(mouseOverScript, element);
				// Thread.sleep(CommonLib.interval * 5);
				// // CommonLib.clickIcon(element.getAttribute("id"));
				// element.click();
				//
				// CustomReporter.MessageLogger("Menu :" +
				// CommonLib.logFormator(sMenu[0][intC]) + " Clicked !",
				// CustomReporter.status.Information);
				//
				// } else {
				// break;
				// }
				// }
				// CommonLib.setFrame(Config.props.getProperty("MainFrame"));
			}

		} catch (Exception e) {
			CustomReporter.MessageLogger(e.getMessage(), CustomReporter.status.Error);
		}
	}
}
