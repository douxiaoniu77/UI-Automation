package com.oracle.opc.automation.test.component.pages.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.actions.BareMetalAction;
import com.oracle.opc.automation.test.component.pages.AbstractAutomationPage;
import com.oracle.opc.automation.test.component.pages.AutomationPage;

public class MyServicePage extends AbstractAutomationPage
		implements AutomationPage {

	public MyServicePage(CloudService cloudService) {
		super(cloudService);
	}

	private static final Locator DETAILS_DROP_BUTTON_PORTAL = LocatorManager
			.getInstance().getLocator("myservice.detail.drop.button");

	private static final Locator MYSERVICE_TOP_DETAILS_DIV = LocatorManager
			.getInstance().getLocator("myservice.top.details.div");

	private static final Locator SUBS_LINE_TOP_DETAILS = new Locator(
			"SUBS_LINE_TOP_DETAILS", "./tr[contains(., 'Subscription')]");

	public void checkSubscriptionStatusMyServices(CloudService service) {

		ActionUtils.waitFor(TimeUnit.SECONDS,
				Constants.DEFAULT_CHANGE_PAGE_TIMEOUT);
		logger.info(LoggerType.STEP, "Click the drop button");
		ActionUtils.findElement(DETAILS_DROP_BUTTON_PORTAL).click();

		logger.info(LoggerType.CHECKPOINT,
				service.getQuoteType().getShowName());

		WebElement w = ActionUtils.findElement(MYSERVICE_TOP_DETAILS_DIV);

		if (ActionUtils.isElementPresent(w, SUBS_LINE_TOP_DETAILS)) {

			String s = ActionUtils.getText(w, SUBS_LINE_TOP_DETAILS);

			if (s.contains(service.getQuoteType().getShowName())) {

				logger.info(LoggerType.CHECKPOINT, "Subscription is:"
						+ service.getQuoteType().getShowName());

				Assert.assertTrue(true);

			}

		}

	}

	private static final Locator MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_COLLAPSE = new Locator(
			"MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_COLLAPSE",
			"//span[contains(@class, 'oj-button-text') and contains(.,'Account Management')]");

	private static final Locator MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_EXPAND = new Locator(
			"MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_EXPAND",
			"//h1[contains(.,'Account Management')]");

	public void goToMyServiceAccountManagement() throws InterruptedException {

		// Locator locator = new Locator("myaccount.detail.service." +
		// bucketName,
		// ".//a[@class='dash-smry-metric-link oj-filmstrip-item' and
		// contains(.,'" + bucketName + "')]");
		// WebElement we = ActionUtils.findElement(MYSERVICE_DASHBOARD_TOP_DIV);
		//
		// if (ActionUtils.findElement(we, locator) != null) {
		//
		// logger.info(LoggerType.STEP, "Clicking Balance Link");
		//
		// ActionUtils.findElement(we, locator).click();
		//
		// ActionUtils.waitFor(TimeUnit.SECONDS,
		// Constants.DEFAULT_CHECK_BRIEF_EMAIL_TIMEOUT_IN_MINUTES);
		//
		// ActionUtils.captureScreenshot();
		//
		// }

		logger.info(LoggerType.COMMENT,
				"Finding Account Management in Dashboard:");

		if (ActionUtils.findElement(
				MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_COLLAPSE) != null) {

			ActionUtils
					.findElement(
							MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_COLLAPSE)
					.click();
		}

		else if (ActionUtils.findElement(
				MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_EXPAND) != null) {

			ActionUtils
					.findElement(MYSERVICE_DASHBOARD_ACCOUNT_MANAGEMENT_EXPAND)
					.click();
		} else {

			logger.info(LoggerType.EXCEPTION,
					"Not found Account Management Link");
			Assert.assertTrue(false);
		}

	}

	public void checkAccountManagementPage(String label)
			throws InterruptedException {
		logger.info(LoggerType.STEP, "Checking Account Management Page");

		// Select the proper bucket to display data
		Locator ACCOUNT_MANAGEMENT_DROPDOWN = new Locator(
				"ACCOUNT_MANAGEMENT_DROPDOWN",
				"//span[contains(@class,'oj-button-icon oj-end cp-img-account')]");
		logger.info(LoggerType.COMMENT, "Finding Dropdown Menu");
		WebElement bucketSelect = ActionUtils
				.findElement(ACCOUNT_MANAGEMENT_DROPDOWN);
		ActionUtils.click(bucketSelect);

		Locator ACCOUNT_MANAGEMENT_BUCKET_ELEMENT = new Locator(
				"ACCOUNT_MANAGEMENT_BUCKET_ELEMENT_" + label,
				"//li[contains(@class,'oj-menu-item')]/a[contains(@id,'"
						+ label.toUpperCase() + "')]");
		logger.info(LoggerType.COMMENT,
				"Finding Dropdown Element for '" + label.toUpperCase() + "'");
		WebElement elementSelect = ActionUtils
				.findElement(ACCOUNT_MANAGEMENT_BUCKET_ELEMENT);
		ActionUtils.click(elementSelect);

		// Get usage balance
		Locator ACCOUNT_MANAGEMENT_SERVICE_BALANCE_USAGE_SPAN = new Locator(
				"ACCOUNT_MANAGEMENT_SERVICE_BALANCE_USAGE_SPAN_" + label,
				"//span[contains(@data-bind,'usageCharge')]");
		WebElement usageLabel = ActionUtils
				.findElement(ACCOUNT_MANAGEMENT_SERVICE_BALANCE_USAGE_SPAN);
		String usageLabelText = usageLabel.getText();
		logger.info(LoggerType.COMMENT,
				"Usage balance is : '" + usageLabelText + "'");
		Assert.assertTrue(BareMetalAction.hasDigit(usageLabelText));

		// Get overage balance
		Locator ACCOUNT_MANAGEMENT_SERVICE_BALANCE_OVERAGE_SPAN = new Locator(
				"ACCOUNT_MANAGEMENT_SERVICE_BALANCE_OVERAGE_SPAN_" + label,
				"//span[contains(@data-bind,'overageCharge')]");
		WebElement overageLabel = ActionUtils
				.findElement(ACCOUNT_MANAGEMENT_SERVICE_BALANCE_OVERAGE_SPAN);
		String overageLabelText = overageLabel.getText();
		logger.info(LoggerType.COMMENT,
				"Overage balance is : '" + overageLabelText + "'");
		Assert.assertTrue(BareMetalAction.hasDigit(overageLabelText));

	}

	private static final Locator MYSERVICE_VIEWDETAILS_BUTTON_OPENSERVICECONSOLE = LocatorManager
			.getInstance()
			.getLocator("myservice.viewdetails.service.button.openconsole");

	public void gotoBMServiceConsole() throws InterruptedException {

		ActionUtils.waitFor(TimeUnit.SECONDS,
				2 * Constants.DEFAULT_SHORT_TIMEOUT);

		logger.info(LoggerType.COMMENT, "Checking service console button");

		if (ActionUtils.findElement(
				MYSERVICE_VIEWDETAILS_BUTTON_OPENSERVICECONSOLE) != null) {

			logger.info(LoggerType.STEP, "Clicking service console button");

			ActionUtils
					.findElement(
							MYSERVICE_VIEWDETAILS_BUTTON_OPENSERVICECONSOLE)
					.click();

		} else {
			logger.info(LoggerType.EXCEPTION, "Not found Expected Link");
			Assert.assertTrue(false);

		}

	}

	private static final Locator SDP_METERING_BUTTON = LocatorManager
			.getInstance().getLocator("detailspage.metering.button");
	private static final Locator SDP_METERING_USAGE_TABLE = LocatorManager
			.getInstance().getLocator("detailspage.metering.usage.table");

	public List<String> getMeteringUsage(String url,
			boolean needGotoDetailPage) {
		return getMeteringUsage(url, needGotoDetailPage, true);
	}

	public List<String> getMeteringUsage(String url, boolean needGotoDetailPage,
			boolean needPrintout) {

		logger.info(LoggerType.STEP, "Getting Billing Metrics in DetailsPage");

		if (needGotoDetailPage) {
			// AutomationPageFactory.getInstance().getPage(cloudService,
			// DashboardPage.class)
			// .gotoServiceDetail(cloudService, url);

			ActionUtils.click(SDP_METERING_BUTTON);
			ActionUtils.waitFor(TimeUnit.SECONDS,
					Constants.DEFAULT_SHORT_TIMEOUT);
		}

		logger.info(LoggerType.COMMENT, "Get Metering Usage");
		List<String> retList = null;

		// if
		// (ActionUtils.areElementsPresentWithPageloaded(SDP_METERING_USAGE_TABLE))
		// {
		if (ActionUtils.findElement(SDP_METERING_USAGE_TABLE) != null) {

			ActionUtils.captureScreenshot();
			retList = printUsageTable(
					ActionUtils.findElements(SDP_METERING_USAGE_TABLE),
					needPrintout);
		} else {
			logger.info(LoggerType.COMMENT,
					"There is no any utilization data in metering usage");
		}
		return retList;
	}

	private List<String> printUsageTable(List<WebElement> tables,
			boolean needPrintout) {
		List<String> retList = new ArrayList<String>();
		for (WebElement t : tables) {
			List<WebElement> rows = ActionUtils.findElements(t,
					new Locator("TABLE_TRS",
							"..//table[@class='table table-metric']/tbody/tr"));
			for (int i = 0; i < rows.size(); i++) {
				String cells_locator = "..//table[@class='table table-metric']/tbody/tr["
						+ (i + 1) + "]/td";
				List<WebElement> cells = ActionUtils.findElements(t,
						new Locator("TABLE_TDS_" + i, cells_locator));
				;
				String sku_name = ActionUtils.getText(cells.get(0));
				String sku_value = ActionUtils.getText(cells.get(1));
				String quantity = ActionUtils.getText(cells.get(2));
				String usage = sku_name + "\t" + sku_value + "\t" + quantity;
				System.out.println(usage);
				retList.add(usage);
			}
		}
		return retList;
	}
}
