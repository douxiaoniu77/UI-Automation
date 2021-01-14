package com.oracle.opc.automation.test.component.pages;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;

public class BMConsolePage extends AbstractAutomationPage implements
		AutomationPage {

	public BMConsolePage() {
		super();
	}

	public BMConsolePage(CloudService cloudService) {
		super(cloudService);
	}

	private static final Locator ACCOUNT_BANNER_DASHBOARD = LocatorManager
			.getInstance().getLocator("account.banner.dashboard");

	@Override
	public void logoutDomain(String url, String pageName) {
		this.LOGOUT_HEADER = LocatorManager.getInstance().getLocator(
				"bm.console.header.link.username");
		this.BUTTON_LOGOUT = LocatorManager.getInstance().getLocator(
				"bm.console.header.button.logout");

		super.logoutDomain(url, pageName);
	}

	public void addApprovedEmailUser(CloudService service) {

	}

	public void validateAccountDashboard(CloudService service) {

		boolean flag = false;
		String account = service.getSummary().getServiceAccount();

		Locator accountDashboard = new Locator("ACCOUNT_NAME_DASHBOARD",
				"//button[@id='menuButton' and contains(., '" + account
						+ "')]");

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		logger.info(LoggerType.STEP, "Finding  the account From JET Dashboard");

		if (ActionUtils.isElementPresent(ACCOUNT_BANNER_DASHBOARD)) {

			logger.info(LoggerType.STEP,
					"Finding  the account From JET Dashboard");

			if (ActionUtils.isElementPresent(accountDashboard)) {
				logger.info(LoggerType.COMMENT, "[End] Find the account["
						+ ActionUtils.getText(accountDashboard)
						+ "] From JET Dashboard");
				ActionUtils.captureScreenshot();
				flag = true;

			} else {

				logger.info(LoggerType.EXCEPTION, "[End] Find the account["
						+ ActionUtils.getText(accountDashboard)
						+ "] From JET Dashboard");
			}

		}

		Assert.assertTrue(flag);

	}

	public void validateBMTypeDashboard(CloudService service) {

		boolean flag = false;
		String serviceTypeDashboard = service.getServiceType()
				.getJetDashboardName();

		Locator BM_SERVICETYPE_DASHBOARD = new Locator(
				"BM_SERVICETYPE_DASHBOARD",
				"//div[@class='dash-svc-title-bar' and contains(., '"
						+ serviceTypeDashboard + "')]");

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		if (ActionUtils.isElementPresent(BM_SERVICETYPE_DASHBOARD)) {

			logger.info(LoggerType.COMMENT, "[End] Find the account["
					+ ActionUtils.getText(BM_SERVICETYPE_DASHBOARD)
					+ "] From JET Dashboard");
			ActionUtils.captureScreenshot();
			flag = true;

		} else {

			logger.info(LoggerType.EXCEPTION, "[End] Find the account["
					+ ActionUtils.getText(BM_SERVICETYPE_DASHBOARD)
					+ "] From JET Dashboard");
		}

		Assert.assertTrue(flag);

	}

	// Locator target = new Locator(
	// "LIST_DIV_SERVICE",
	// "//div[@class='af_listView_data-container']/div[contains(@class,'af_listItem')
	// and contains(.,'"
	// + serviceWholeName
	// + "') and contains(.,'Identity Domain: "
	// + cloudService.getSummary().getDomainName() + "')]");

	// oj-button-text
}
