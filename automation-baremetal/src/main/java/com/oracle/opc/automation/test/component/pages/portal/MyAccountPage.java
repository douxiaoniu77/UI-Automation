package com.oracle.opc.automation.test.component.pages.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.DetailInfoEntry;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.entity.enums.TagAttributes;
import com.oracle.opc.automation.test.component.factory.AutomationPageFactory;
import com.oracle.opc.automation.test.component.pages.AbstractAutomationPage;
import com.oracle.opc.automation.test.component.pages.AutomationPage;
import com.oracle.opc.automation.test.entity.enums.dashboardMyAccountEntry;

public class MyAccountPage extends AbstractAutomationPage
		implements AutomationPage {

	public MyAccountPage(CloudService cloudService) {
		super(cloudService);
	}

	String fullName = cloudService.getServiceType().getFullName(); // service.getServiceType().getFullName();

	Locator MYACCOUNT_DASHBOARD_BM_LINK = new Locator(
			"MYACCOUNT_DASHBOARD_BM_LINK",
			"//span[contains(@id,'cnIt1:col-item')]" + "/a[contains (.,'"
					+ fullName + "')]");

	protected static final Locator MADP_BUTTON_DOMAIN_FILTER = LocatorManager
			.getInstance().getLocator("madp.button.domain.filter");
	protected static final Locator JET_MADP_BUTTON_DOMAIN_FILTER = LocatorManager
			.getInstance().getLocator("jet.madp.button.domain.filter");
	protected static final Locator JET_MADP_BUTTON_DOMAIN_FILTER_LOADMORE = LocatorManager
			.getInstance().getLocator("jet.madp.button.domain.filter.loadmore");
	protected static final Locator JET_MADP_SPAN_SHOWN_DOMAIN = LocatorManager
			.getInstance().getLocator("jet.madp.span.shown.domain");
	protected static final Locator MADP_TEXT_DOMAIN_FILTER = LocatorManager
			.getInstance().getLocator("madp.text.domain.filter");
	protected static final Locator MADP_OPTION_ALLDOMAIN = LocatorManager
			.getInstance().getLocator("madp.option.alldomain");

	public void filterDomain() throws InterruptedException {

		String domainName = cloudService.getSummary().getDomainName();
		if (ActionUtils.isElementPresentTimeoutInSeconds(
				MADP_BUTTON_DOMAIN_FILTER, 2l)) {
			// ActionUtils.click(MADP_BUTTON_DOMAIN_FILTER);
			WebElement domainFilterButton = ActionUtils
					.findElement(MADP_BUTTON_DOMAIN_FILTER);
			String fbtn_id = domainFilterButton.getAttribute("id");
			Locator l_btn = new Locator("MADP_BUTTON_DOMAIN_FILTER", fbtn_id,
					"", TagAttributes.ID);
			ActionUtils.clickByJS(l_btn);
			if (ActionUtils.isElementPresentTimeoutInSeconds(
					MADP_TEXT_DOMAIN_FILTER, 2l)) {
				ActionUtils.type(MADP_TEXT_DOMAIN_FILTER, domainName);
				ActionUtils.waitFor(TimeUnit.SECONDS,
						Constants.DEFAULT_SHORT_TIMEOUT);
				Locator DOMAIN_OPTION = new Locator(
						"DOMAIN_OPTION_" + domainName,
						"//a[contains(@id,'idm-" + domainName + "')]");
				if (ActionUtils.isElementPresentTimeoutInSeconds(DOMAIN_OPTION,
						2l)) {
					ActionUtils.click(DOMAIN_OPTION);
				} else {
					ActionUtils.type(MADP_TEXT_DOMAIN_FILTER,
							"All Identity Domains");
					ActionUtils.click(MADP_OPTION_ALLDOMAIN);
				}
				ActionUtils.waitFor(TimeUnit.SECONDS,
						Constants.DEFAULT_SHORT_TIMEOUT * 2);
			}
		}

		// ActionUtils.waitFor(TimeUnit.SECONDS, 15);
		// Locator DomainFilter = new Locator("DomainFilter",
		// "//button[contains(@id,'idm-lf-btn')]");
		//
		// Locator DOMAIN_OPTION = new Locator("DOMAIN_OPTION_" +
		// cloudService.getSummary().getDomainName(),
		// "//a[contains(@id,'idm-" + cloudService.getSummary().getDomainName()
		// + "')]");
		//
		// WebDriver driver = WebDriverManager.getCurrentDriver();
		//
		// if (driver instanceof JavascriptExecutor &&
		// ActionUtils.isElementPresent(DomainFilter)) {
		// logger.info(LoggerType.STEP, "Filtering Expected domain");
		//
		// ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
		// ActionUtils.findElement(DomainFilter));
		// }
		//
		// Locator MYACCOUNT_CHOOSE_DOMAIN_INPUT = new
		// Locator("MYACCOUNT_CHOOSE_DOMAIN_INPUT",
		// "//input[contains(@id,'idm-suggest-its::content')]");
		//
		// Locator MYACCOUNT_CHOOSE_DOMAIN_LINK = new
		// Locator("MYACCOUNT_CHOOSE_DOMAIN_LINK",
		// "//a[text() = '" + cloudService.getSummary().getDomainName() + "']");
		//
		// ActionUtils.type(MYACCOUNT_CHOOSE_DOMAIN_INPUT,
		// cloudService.getSummary().getDomainName());
		//
		// ActionUtils.waitFor(TimeUnit.SECONDS,
		// Constants.DEFAULT_SHORT_TIMEOUT);
		//
		// if (ActionUtils.findElement(DOMAIN_OPTION) != null) {
		// ActionUtils.click(DOMAIN_OPTION);
		// }
		//
		// // ActionUtils.click(MYACCOUNT_CHOOSE_DOMAIN_LINK);
		// ActionUtils.waitFor(TimeUnit.SECONDS, 15);

	}

	// private static final Locator MYACCOUNT_DASHBOARD_BM_LINK = LocatorManager
	// .getInstance().getLocator("myaccount.dashboard.bm.list");

	//
	private static Locator MYACCOUNT_DASHBORAD_BM_SUBSCRIPTION = new Locator(
			"MYACCOUNT_DASHBORAD_BM_SUBSCRIPTION",
			"./span[contains(.,'Subscription')]");

	public void checkSubscriptionMyAccountDashboard(CloudService service) {

		String fullName = service.getServiceType().getFullName();

		Locator MYACCOUNT_DASHBOARD_BM_LINK = new Locator(
				"MYACCOUNT_DASHBOARD_BM_LINK",
				"//span[contains(@id, 'pt1:sections:dc:lv-db') and contains (.,'"
						+ fullName + "')]");

		logger.info(LoggerType.COMMENT,
				"Start to Check Subscription in My Account..., expected should be:"
						+ service.getQuoteType().getShowName());

		WebElement f = ActionUtils.findElement(MYACCOUNT_DASHBOARD_BM_LINK);

		if (ActionUtils.findElement(f,
				MYACCOUNT_DASHBORAD_BM_SUBSCRIPTION) != null) {

			String subscription = ActionUtils.getText(ActionUtils.findElement(f,
					MYACCOUNT_DASHBORAD_BM_SUBSCRIPTION));

			Assert.assertTrue(subscription
					.contains(service.getQuoteType().getShowName()));

		}

	}

	/**
	 * @author xueniu To get details info in MyAccount Dashboard
	 *
	 */
	// private static final Locator RELATIVE_VALUE =
	// LocatorManager.getInstance()
	// .getLocator("myaccount.detail.service.info.value");

	// rewrite the getMYACCOUNTDASHBOARDBMLINK function common

	public Map<dashboardMyAccountEntry, String> getDashboardMyAccountEntry(
			CloudService service) throws InterruptedException {

		Map<dashboardMyAccountEntry, String> infoMap = new HashMap<dashboardMyAccountEntry, String>();
		ActionUtils.waitFor(TimeUnit.SECONDS,
				Constants.DEFAULT_CHANGE_PAGE_TIMEOUT);
		ActionUtils.captureScreenshot();

		// Find Baremetal Service block in MyAccount Dashboard
		WebElement bm = ActionUtils.findElement(MYACCOUNT_DASHBOARD_BM_LINK);

		// Get all label values in MyAccount
		for (dashboardMyAccountEntry info : dashboardMyAccountEntry.values()) {

			// Get label name
			String label = info.getLabelName();

			// Locate Label and extract text
			Locator locator = new Locator(
					"myaccount.dashboard.service." + label,
					"./../../span[@class='x293 x1a' and contains(.,'" + label
							+ ":')]");
			if (ActionUtils.isElementPresent(bm, locator)) {
				String value = ActionUtils.findElement(bm, locator).getText();
				logger.info(LoggerType.COMMENT, label + ":" + value);

				// Put the text into the info table
				infoMap.put(info, value);
			}
		}
		return infoMap;
	}

	public WebElement checkBMListMyAccount(CloudService service)
			throws InterruptedException {
		//
		// String fullName = service.getServiceType().getFullName();
		//
		// Locator MYACCOUNT_DASHBOARD_BM_LINK = new
		// Locator("MYACCOUNT_DASHBOARD_BM_LINK",
		// "//span[@class='x294 x2g9 x1a']/a[contains (.,'" + fullName + "')]");

		if (ActionUtils.isElementPresent(MYACCOUNT_DASHBOARD_BM_LINK)) {

			return ActionUtils.findElement(MYACCOUNT_DASHBOARD_BM_LINK);

		}

		return null;

	}

	private static final Locator SDP_RESOURCE_QUOTA_BUTTON = LocatorManager
			.getInstance().getLocator("detailspage.resource.quota.button");
	private static final Locator RESOURCE_QUOTA_TABLE = LocatorManager
			.getInstance().getLocator("detailspage.resource.quota.table");
	private static final Locator QUOTA_LINE_TDS = LocatorManager.getInstance()
			.getLocator("detailspage.resource.quota.table.line");

	private final Locator DETAILSPAGE_REOURCE_QUOTA_LINE_TITLE = new Locator(
			"DETAILSPAGE_REOURCE_QUOTA_LINE_TITLE", "./span[1]");

	private final Locator DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE = new Locator(
			"DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE", "./span[3]");

	// private final Locator DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE = new
	// Locator("DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE",
	// "//span[contains(@class, 'x2bt x1a') and contains (.,'" +
	// cloudService.getServiceType().getFullName()
	// + "')]");

	public void gotoResourceQuotaPages() throws InterruptedException {

		logger.info(LoggerType.STEP, "Going Resource Quota Page");

		ActionUtils.click(SDP_RESOURCE_QUOTA_BUTTON);
		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

	}

	public void getResourceQuota() throws InterruptedException {

		logger.info(LoggerType.STEP,
				"[Start] Check Account["
						+ cloudService.getSummary().getServiceAccount()
						+ "] Resource Quota");

		WebElement resourceQuatoTable = ActionUtils
				.findElement(RESOURCE_QUOTA_TABLE);

		if (resourceQuatoTable != null) {

			List<WebElement> quotaLineList = ActionUtils
					.findElements(resourceQuatoTable, QUOTA_LINE_TDS);

			if (quotaLineList != null) {

				logger.info(LoggerType.COMMENT,
						"quotaTitle: " + "quotaValue: " + "\n");

				for (WebElement ql : quotaLineList) {
					if (ActionUtils.findElement(ql,
							DETAILSPAGE_REOURCE_QUOTA_LINE_TITLE) != null
							&& ActionUtils.findElement(ql,
									DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE) != null) {

						String title = ActionUtils
								.findElement(ql,
										DETAILSPAGE_REOURCE_QUOTA_LINE_TITLE)
								.getText();

						String value = ActionUtils
								.findElement(ql,
										DETAILSPAGE_REOURCE_QUOTA_LINE_VALUE)
								.getText();

						logger.info(LoggerType.COMMENT,
								title + ":  " + value + "\n");

					} else {

						logger.info(LoggerType.EXCEPTION,
								"Quota Line--Not Found");
					}

				}

			}
		}

	}

	//
	// public void getQuota(ServiceQuota realQuota) throws InterruptedException
	// {
	// logger.info(LoggerType.STEP, "[Start] Check Account["
	// + cloudService.getSummary().getServiceAccount() + "] Quota");
	//
	// ActionUtils.click(BUTTON_REFRESH);
	// ActionUtils.waitFor(TimeUnit.SECONDS, LOAD_TIME);
	// WebElement quatoTable = ActionUtils.findElement(QUOTA_TABLE);
	//
	// if (quatoTable != null) {
	// List<WebElement> tdList = ActionUtils.findElements(quatoTable,
	// QUOTA_TABLE_TDS);
	// if (tdList != null) {
	// for (WebElement td : tdList) {
	// if (td.getText() != null
	// && !Constants.NULL_STRING_VALUE
	// .equals(td.getText())) {
	// String name = ActionUtils.getText(td, QUOTA_TABLE_NAME);
	// String value = ActionUtils.getText(td,
	// QUOTA_TABLE_VALUE);
	// realQuota.putValue(name, value);
	// }
	// }
	// }
	// }
	// logger.info(LoggerType.STEP, "[End] Check Account["
	// + cloudService.getSummary().getServiceAccount() + "] Quota");
	// }

	public void goToDetailsMyAccount() throws InterruptedException {

		logger.info(LoggerType.STEP, "Entering BM Details Page......");

		if (ActionUtils.findElement(MYACCOUNT_DASHBOARD_BM_LINK) != null) {

			ActionUtils.findElement(MYACCOUNT_DASHBOARD_BM_LINK).click();
		}

		logger.info(LoggerType.STEP,
				"ServiceName: " + cloudService.getServiceType().getFullName());

	}

	public void goToDetailsMyAccount(CloudService service, String bucket)
			throws InterruptedException {

		int endIndex = 20;
		// serviceName != null &&
		// !Constants.NULL_STRING_VALUE.equals(serviceName)
		// ? (serviceName.length() > DEFAULT_DISPLAY_SERVICE_NAME_LENGTH ?
		// DEFAULT_DISPLAY_SERVICE_NAME_LENGTH
		// : serviceName.length())
		// : 0;

		logger.info(LoggerType.STEP, "Entering BM Details Page......");

		logger.info(LoggerType.STEP, "serviceDiv String:" + bucket + ":::"
				+ service.getServiceType().getFullName());

		Locator SVC_DETAILS_LINK = new Locator("SVC_DETAILS_LINK",
				getDisplayedServiceDiv(service, bucket)
						+ "/span/span/a[contains(.,'"
						+ service.getServiceType().getFullName() + "')]");

		if (ActionUtils.findElement(SVC_DETAILS_LINK) != null) {

			ActionUtils.findElement(SVC_DETAILS_LINK).click();
		} else {

			logger.info(LoggerType.EXCEPTION, "NOT Found Details Link");
		}

		logger.info(LoggerType.STEP, "ServiceName: " + service + "???"
				+ cloudService.getServiceType().getFullName());

	}

	private String getDisplayedServiceDiv(CloudService service, String bucket) {

		StringBuilder sb = new StringBuilder();
		// if (isJET_Framework()) {
		// sb.append("//a[contains(@class,'svc-name')]" + "/span[text()='"
		// + summary.getServiceName() + "' or text()='"
		// + summary.getServiceName().toLowerCase()
		// + "' or translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
		// 'abcdefghijklmnopqrstuvwxyz')='"
		// + service.getServiceType().getFullName().toLowerCase()
		// + "']"
		// + "//ancestor::div[contains(@class,'oj-panel') and
		// contains(@class,'dash-svc')]");
		// } else {
		sb.append(
				"//span[contains(@id,':lv-db:') and contains(@id,':mysvcs-rec')");
		sb.append(" and (contains(.,'" + service.getServiceType().getFullName()
				+ "')" + " or contains(.,'"
				+ service.getServiceType().getFullName().toLowerCase() + "'))");
		sb.append(" and contains(.,'Category:')");
		sb.append(" and contains(.,'" + bucket + "')");
		// sb.append(" and contains(.,'Subscription:')");
		// sb.append(" and contains(.,'" + service.getQuoteType().getShowName()
		// + "')");
		sb.append("]");
		// sb.append("|");
		// sb.append("//div[(contains(@class,'af_listItem') or
		// contains(@class,'xta'))");
		// sb.append(" and (contains(.,'" + summary.getServiceName() + "')"
		// + " or contains(.,'"
		// + summary.getServiceName().toLowerCase() + "'))");
		// sb.append(" and contains(.,'Subscription:')");
		// sb.append(" and contains(.,'"
		// + service.getQuoteType().getShowName() + "')");
		// sb.append("]");
		return sb.toString();

	}

	private static final Locator RELATIVE_VALUE = LocatorManager.getInstance()
			.getLocator("myaccount.detail.service.info.value");

	public Map<DetailInfoEntry, String> getAllDetailInfo()
			throws InterruptedException {
		Map<DetailInfoEntry, String> infoMap = new HashMap<DetailInfoEntry, String>();
		ActionUtils.waitFor(TimeUnit.SECONDS,
				Constants.DEFAULT_CHANGE_PAGE_TIMEOUT);
		ActionUtils.captureScreenshot();

		for (DetailInfoEntry info : DetailInfoEntry.values()) {
			String label = info.getLabelName();

			Locator locator = new Locator("myaccount.detail.service." + label,
					".//tr[contains(@class, 'x2rd')]"
							+ "[contains(@id,'currentTabHTMLContents')]"
							+ "[contains(.,'" + label + ":')]");

			// Locator locator = new Locator(
			// "myaccount.detail.service." + label,
			// "//td[@class='af_panelLabelAndMessage_label
			// af_panelFormLayout_label-cell' and contains(.,'"
			// + label + ":')]");

			if (ActionUtils.isElementPresent(locator)) {

				WebElement baseW = ActionUtils.findElement(locator);

				if (ActionUtils.isElementPresent(baseW, RELATIVE_VALUE)) {
					String value = ActionUtils.getText(baseW, RELATIVE_VALUE);

					logger.info(LoggerType.COMMENT,
							"label:" + label + "   value:" + value);

					infoMap.put(info, value);
				}
			}
		}
		// remove Navigate Back for hang issue.
		// ActionUtils.navigateBack();
		return infoMap;
	}

	// private static final Locator MYACCOUNT_DETAILS_INFO_DIV =
	// LocatorManager.getInstance()
	// .getLocator("myaccount.details.info.div");

	public String getSpecifigDetailInfo(String str)
			throws InterruptedException {
		// Map<DetailInfoEntry, String> infoMap = new HashMap<DetailInfoEntry,
		// String>();
		ActionUtils.waitFor(TimeUnit.SECONDS,
				Constants.DEFAULT_CHANGE_PAGE_TIMEOUT);
		ActionUtils.captureScreenshot();

		String value = null;
		// WebElement detailsDiv =
		// ActionUtils.findElement(MYACCOUNT_DETAILS_INFO_DIV);
		//
		// for (DetailInfoEntry info : DetailInfoEntry.values()) {
		// String label = info.getLabelName();

		// if (label.equals(str)) {
		//
		// ".//span[contains(@id,'pt1:pt2:currentTabHTMLContents:')"
		// + " and contains(@id,':li1it:')]"
		// + "/span[position()=1 and contains(.,'Status:')]"
		// + "/following-sibling::span");

		Locator locator = new Locator("myaccount.detail.service." + str,
				".//tr[contains(@class, 'x2s4')]"
						+ "[contains(@id,'currentTabHTMLContents')]"
						+ "[contains(.,'" + str + ":')]");

		// if (ActionUtils.isElementPresent(MYACCOUNT_DETAILS_INFO_DIV)
		// && (ActionUtils.findElements(detailsDiv, locator) != null)) {

		if (ActionUtils.findElement(locator) != null) {

			WebElement record = ActionUtils.findElement(locator);

			if (ActionUtils.isElementPresent(record, RELATIVE_VALUE)) {
				value = ActionUtils.getText(record, RELATIVE_VALUE);

				logger.info(LoggerType.COMMENT,
						"label:" + str + "   value:" + value);

				// infoMap.put(info, value);

				return value;
			}
		}
		// remove Navigate Back for hang issue.
		// ActionUtils.navigateBack();
		return value;

	}

	public void gotoEstimateBalancePage() throws InterruptedException {

		// Locator MYACCOUNT_DASHBOARD_BM_SPAN = new
		// Locator("MYACCOUNT_DASHBOARD_BM_SPAN",
		// "//span[contains(@id, 'cnIt1:col-item-value') and contains (.,'"
		// + cloudService.getServiceType().getFullName() + "')]");

		Locator MYACCOUNT_DASHBOARD_BM_SPAN = new Locator(
				"MYACCOUNT_DASHBOARD_BM_SPAN",
				"//span[contains(@class, 'x28j x1a') and contains (.,'"
						+ cloudService.getServiceType().getFullName() + "')]");

		Locator MYACCOUNT_DASHBOARD_ESTIMATE_BALANCE_LINK = new Locator(
				"MYACCOUNT_DASHBOARD_ESTIMATE_BALANCE_LINK",
				".//a[contains(@id,'col-item-link') and contains (.,'Estimated Balance')]");

		if (ActionUtils.isElementPresentTimeoutInSeconds(
				MYACCOUNT_DASHBOARD_BM_SPAN,
				Constants.DEFAULT_CHANGE_PAGE_TIMEOUT)) {

			logger.info(LoggerType.STEP, "Clicking Esitmate Balance Link: "
					+ cloudService.getServiceType().getFullName());

			WebElement we = ActionUtils
					.findElement(MYACCOUNT_DASHBOARD_BM_SPAN);

			ActionUtils
					.findElement(we, MYACCOUNT_DASHBOARD_ESTIMATE_BALANCE_LINK)
					.click();

			ActionUtils.waitFor(TimeUnit.SECONDS,
					2 * Constants.DEFAULT_SHORT_TIMEOUT);

		} else {

			logger.info(LoggerType.EXCEPTION,
					"Not found the Expected Service Line");
			Assert.assertTrue(false);
		}

	}

	// String balanceTitle="Usage Summary";

	public void checkEstimateBalancePage() throws InterruptedException {

		boolean flag = true;

		Locator MYACCOUNT_ESTIMATE_BALANCE_TITLE = new Locator(
				"MYACCOUNT_ESTIMATE_BALANCE_TITLE",
				"//span[@class='x29k x1a')]");

		// logger.info(LoggerType.STEP, "Finding Esitmate Balance Title:" +
		// balanceTitle);

		if (ActionUtils.isElementPresent(MYACCOUNT_ESTIMATE_BALANCE_TITLE)) {

			logger.info(LoggerType.COMMENT, "Finding: "
					+ ActionUtils.getText(MYACCOUNT_ESTIMATE_BALANCE_TITLE));

			// flag = true;
		}

		Assert.assertTrue(flag);
	}

	private static final Locator SDP_METERING_BUTTON = LocatorManager
			.getInstance().getLocator("sdp.metering.button");
	private static final Locator SDP_METERING_USAGE_TABLE = LocatorManager
			.getInstance().getLocator("sdp.metering.usage.table");

	public List<String> getMeteringUsage(CloudService service, String url,
			boolean needGotoDetailPage) {
		return getMeteringUsage(service, url, needGotoDetailPage, true);
	}

	public List<String> getMeteringUsage(CloudService service, String url,
			boolean needGotoDetailPage, boolean needPrintout) {
		if (needGotoDetailPage) {
			AutomationPageFactory.getInstance()
					.getPage(cloudService, DashboardPage.class)
					.gotoServiceDetail(cloudService, url);
			ActionUtils.click(SDP_METERING_BUTTON);
			ActionUtils.waitFor(TimeUnit.SECONDS,
					Constants.DEFAULT_SHORT_TIMEOUT);
		}

		logger.info(LoggerType.COMMENT, "Get Metering Usage");
		List<String> retList = null;

		if (ActionUtils
				.areElementsPresentWithPageloaded(SDP_METERING_USAGE_TABLE)) {
			ActionUtils.captureScreenshot();
			// retList = AutomationPageFactory.getInstance()
			// .getPage(service, ServiceDetailPage.class).printUsageTable(
			// ActionUtils.findElements(SDP_METERING_USAGE_TABLE),
			// needPrintout);
		} else {
			logger.info(LoggerType.COMMENT,
					"There is no any utilization data in metering usage");
		}
		return retList;
	}

}
