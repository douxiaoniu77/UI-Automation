package com.oracle.opc.automation.test.component.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.jcraft.jsch.Session;
import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.common.utils.ActionUtils.CheckType;
import com.oracle.opc.automation.common.utils.JSCHUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.CloudRegion;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.entity.enums.ServiceStatus;
import com.oracle.opc.automation.test.component.actions.service.LoginServiceAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.component.factory.AutomationPageFactory;
import com.oracle.opc.automation.test.component.pages.portal.DashboardPage;
import com.oracle.opc.automation.test.component.pages.portal.MyAccountPage;
import com.oracle.opc.automation.test.component.pages.portal.MyServicePage;
import com.oracle.opc.automation.test.component.pages.portal.ServiceDetailPage;
import com.oracle.opc.automation.test.entity.enums.dashboardMyAccountEntry;

public class BareMetalAction extends AbstractAutomationAction implements AutomationAction {

	String userName;
	String userPassword;
	String MyServiceUrl;

	public BareMetalAction() {
		super();
		userName = Constants.P_FILE.getProperties().getProperty("opc.bm.user.name");
		userPassword = Constants.P_FILE.getProperties().getProperty("service.confidential.pwd");
		MyServiceUrl = Constants.P_FILE.getProperties().getProperty("opc.myservice.idcs.url.prefix")
				+ Constants.P_FILE.getProperties().getProperty("opc.myservice.idcs.url.accountId")
				+ Constants.P_FILE.getProperties().getProperty("opc.myservice.idcs.url.suffix");

	}

	public BareMetalAction(CloudService cloudService) {
		super(cloudService);
	}

	public BareMetalAction checkSubstTypeMyAccount(CloudService service) throws InterruptedException {

		String subscription = AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class)
				.getDashboardMyAccountEntry(service).get(dashboardMyAccountEntry.SUBSCRIPTION);

		boolean flag = false;

		logger.info(LoggerType.COMMENT,
				"The service [" + cloudService.getServiceType().getFullName() + "] Subscription  is " + subscription);

		if (subscription.contains(service.getQuoteType().getShowName())) {

			logger.info(LoggerType.COMMENT, "Subcription Type is: " + service.getQuoteType().getShowName());
			flag = true;

		}
		Assert.assertTrue(flag);

		return this;
	}

	public final String mqsUser = Constants.P_FILE.getProperties().getProperty("mqs.api.user");
	public final String mqsPassword = Constants.P_FILE.getProperties().getProperty("mqs.api.password");
	public final String mqsBaseUrl = Constants.P_FILE.getProperties().getProperty("mqs.api.url");
	public final String accountID = Constants.P_FILE.getProperties().getProperty("cloud.account.id");

	public BareMetalAction checkBMReporting(String dataCenterId) throws InterruptedException, IOException {

		logger.info(LoggerType.COMMENT, "Check State of mqs api Call");
		Session session = loginScriptHost();

		// Define date format
		SimpleDateFormat dateFormatExpected = new SimpleDateFormat("yyyy-MM-dd'T'HH", Locale.US);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date = new Date();
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		logger.info(LoggerType.COMMENT, "Current System Time(UTC) is: " + (dateFormatExpected.format(date)));// new

		// Initialize Calendar
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new
		calendar.setTime(date); // assigns calendar to given date
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Get Expected Dates
		// Get 2 hours back
		calendar.add(Calendar.HOUR, -2);
		String expectedDate = dateFormatExpected.format(calendar.getTime());
		String expectedDayMQS = dateFormat.format(calendar.getTime());
		// Get 3 hours back
		calendar.add(Calendar.HOUR, -1);
		String expectedPreviousDate = dateFormatExpected.format(calendar.getTime());

		logger.info(LoggerType.COMMENT, "Expected (based on UTC timeZone): " + expectedDate);
		logger.info(LoggerType.COMMENT, "Previous (based on UTC timeZone): " + expectedPreviousDate);

		// Execute CURL request
		// String curlUrl = mqsBaseUrl + accountID +
		// "?serviceName=COMPUTEBAREMETAL&dataCenter=" + dataCenterId
		// + "&startTime=" + expectedDayMQS + "T&timeZone=UTC";

		String timeSuffix = ":00:00.000Z";
		String curlUrl = mqsBaseUrl + accountID + "?serviceName=COMPUTEBAREMETAL&dataCenter=" + dataCenterId
				+ "&startTime=" + expectedPreviousDate + timeSuffix + "&endTime=" + (dateFormatExpected.format(date))
				+ timeSuffix + "&timeZone=UTC&usageType=HOURLY";

		curlUrl = java.net.URLDecoder.decode(curlUrl, "UTF-8");

		// String resourceName = "PIC_COMPUTE_X7_VM_STANDARD";
		// String curlUrlCompute = mqsBaseUrl + accountID +
		// "?serviceName=COMPUTEBAREMETAL&resourceName=" + resourceName
		// + "&startTime=" + expectedDayMQS + "T&timeZone=UTC";

		String curlCommand = "curl -X GET -u " + mqsUser + ":" + mqsPassword + " " + "\"" + curlUrl + "\" "
				+ " |python -m json.tool  > webService.out ";
		logger.info(LoggerType.COMMENT, "Command Executed: " + curlCommand);
		JSCHUtils.execJSCH(session, curlCommand);
		logger.info(LoggerType.COMMENT, "Please wait for results");
		String emcliCheckCmd = "cat  webService.out |tail -n 40 ";
		String responseMQS = JSCHUtils.execJSCH(session, emcliCheckCmd);
		logger.info(LoggerType.COMMENT, "Checking Result in " + dataCenterId + ": \n" + responseMQS);

		// Close Session
		JSCHUtils.closeSession(session);

		// Validate results
		Assert.assertTrue((responseMQS.contains(expectedDate) || responseMQS.contains(expectedPreviousDate))
				&& responseMQS.contains(dataCenterId));

		return this;
	}

	public BareMetalAction validateTAS_BMSM() throws InterruptedException, IOException {

		logger.info(LoggerType.COMMENT, "Check State of BM SM api Call");

		Session session = loginScriptHost();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);// 设置日期格式
		Date date = new Date();
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		logger.info(LoggerType.COMMENT, "Current System Time(UTC) is: " + (df.format(date)));// new
		String currentDay = df.format(date);
		String scriptDir = "/home/oracle/xueniu/Connection";

		String curlCommand = " cd " + scriptDir + ";  sh curlR1.sh fake-account-id" + " 2>&1 | tee webService.out";
		logger.info(LoggerType.COMMENT, "Command Executed: " + curlCommand);

		JSCHUtils.execJSCH(session, curlCommand);

		logger.info(LoggerType.COMMENT, currentDay + "Please wait for results, Expected(based on UTC timeZone): ");

		// String expectedStr1 = "SSL certificate verify ok";
		String expectedStr2 = "fake-account-id not found";

		String emcliCheckCmd = "cat  " + scriptDir + "/webService.out";
		String s = JSCHUtils.execJSCH(session, emcliCheckCmd);
		logger.info(LoggerType.COMMENT, "Checking Result: \n" + s);
		JSCHUtils.closeSession(session);

		// Assert.assertTrue(s.contains(expectedStr1) &&
		// s.contains(expectedStr2));
		Assert.assertTrue(s.contains(expectedStr2));

		return this;
	}

	String scriptHost = Constants.P_FILE.getProperties().getProperty("fab.script.host.name") + ".us.oracle.com";
	String username = Constants.P_FILE.getProperties().getProperty("fab.script.host.username");
	String password = Constants.P_FILE.getProperties().getProperty("fab.script.host.password");

	public Session loginScriptHost(String host, String user, String password) {

		logger.info(LoggerType.STEP, "Login Script Host: " + host);

		Session session = JSCHUtils.getJSCHSession(user, password, host);

		return session;

	}

	public Session loginScriptHost() {

		String scriptHost = Constants.P_FILE.getProperties().getProperty("curl.api.host.name") + ".us.oracle.com";
		String username = Constants.P_FILE.getProperties().getProperty("curl.api.host.username");
		String password = Constants.P_FILE.getProperties().getProperty("curl.api.host.password");

		logger.info(LoggerType.STEP, "Login Script Host: " + scriptHost);

		Session session = JSCHUtils.getJSCHSession(username, password, scriptHost);

		return session;

	}

	public BareMetalAction checkBMListMyAccount(CloudService service) throws InterruptedException {

		boolean flag = false;

		WebElement w = AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class)
				.checkBMListMyAccount(service);
		if (w != null) {

			logger.info(LoggerType.COMMENT, "Finding BM:" + ActionUtils.getText(w));
			flag = true;
		}

		Assert.assertTrue(flag);

		return this;
	}

	// public BareMetalAction checkServiceStatusMyAccount(CloudService service)
	// throws InterruptedException {
	//
	// // String status =
	// // AutomationPageFactory.getInstance().getPage(cloudService,
	// // MyAccountPage.class)
	// // .getAllDetailInfo().get(DetailInfoEntry.STATUS);
	//
	// String str = "Status";
	// ActionUtils.waitFor(TimeUnit.SECONDS, 2 *
	// Constants.DEFAULT_SHORT_TIMEOUT);
	// String status = AutomationPageFactory.getInstance().getPage(cloudService,
	// MyAccountPage.class)
	// .getSpecifigDetailInfo(str);
	//
	// logger.info(LoggerType.COMMENT,
	// "The service [" + cloudService.getSummary().getServiceName() + "] status
	// is " + status);
	// Assert.assertTrue(ServiceStatus.ACTIVE.getNickName().equals(status));
	// return this;
	//
	// }

	public BareMetalAction checkServiceStatusDetailsPage(CloudService service) throws InterruptedException {

		String str = "Status";

		ActionUtils.waitFor(TimeUnit.SECONDS, 2 * Constants.DEFAULT_SHORT_TIMEOUT);

		String status = AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class)
				.getSpecifigDetailInfo(str);

		logger.info(LoggerType.COMMENT,
				"The service [" + cloudService.getSummary().getServiceName() + "] status is " + status);
		Assert.assertTrue(ServiceStatus.ACTIVE.getNickName().equals(status));
		return this;

	}

	public BareMetalAction AccountResourceQuotaValidation(CloudService service) throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class).gotoResourceQuotaPages();

		logger.info(LoggerType.STEP, "Checking Resource Quota Details");
		AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class).getResourceQuota();

		return this;

	}

	public BareMetalAction checkEstimateBalanceMyAccount(CloudService service) throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class).gotoEstimateBalancePage();

		logger.info(LoggerType.STEP, "Checking Estimate Balance Page");
		AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class).checkEstimateBalancePage();

		return this;

	}
	//
	// public BareMetalAction checkAccountBalanceMyService(CloudService service,
	// String label, String url)
	// throws InterruptedException {
	//
	//
	//
	// return this;
	//
	// }

	public BareMetalAction checkBMViewDetailsMyAccount(CloudService service) throws InterruptedException {

		ActionUtils.waitFor(TimeUnit.SECONDS, 2 * Constants.DEFAULT_SHORT_TIMEOUT);

		String[] myStrings = new String[] { "Plan", "Identity Domain Name" };

		List<String> mylist = Arrays.asList(myStrings);

		Iterator<String> itr = mylist.iterator();// getting an iterator object
													// to browse list items

		while (itr.hasNext()) {

			String key = itr.next();
			// String temp =
			// AutomationPageFactory.getInstance().getPage(cloudService,
			// MyAccountPage.class)
			// .getAllDetailInfo().get(key);
			String value = getExpectedString(AutomationPageFactory.getInstance()
					.getPage(cloudService, MyAccountPage.class).getSpecifigDetailInfo(key));

			logger.info(LoggerType.STEP,
					"Checking:" + key + ":" + service.getServiceType().getFullName() + ":" + value);
			if (service.getServiceType().getFullName().contains(value)
					|| value.equals(service.getSummary().getDomainName())) {

				logger.info(LoggerType.COMMENT, "Showing correctly" + ":" + value);
			} else {

				logger.info(LoggerType.EXCEPTION, "Showing incorrectly, Please check more");
				Assert.assertTrue(false);
			}

		}

		return this;
	}

	public BareMetalAction gotoBMServiceConsole(CloudService service) throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(cloudService, MyServicePage.class).gotoBMServiceConsole();

		return this;
	}

	public void subscriptionValidation(CloudService service, String url) throws InterruptedException {

		// ActionUtils.gotoUrl(url);

		AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoServiceDetail(service, url);

		AutomationPageFactory.getInstance().getPage(service, MyServicePage.class)
				.checkSubscriptionStatusMyServices(service);

	}

	public BareMetalAction checkSFTPViewDetails(CloudService service) throws InterruptedException {

		ActionUtils.waitFor(TimeUnit.SECONDS, 2 * Constants.DEFAULT_SHORT_TIMEOUT);

		String str = "Domain SFTP User Name";

		String value = getExpectedString(AutomationPageFactory.getInstance().getPage(cloudService, MyAccountPage.class)
				.getSpecifigDetailInfo(str));

		logger.info(LoggerType.STEP, "Checking SFTP Account:");
		if (value != null) {

			logger.info(LoggerType.COMMENT, " SFTP Account:" + value);
		} else {

			logger.info(LoggerType.EXCEPTION, "Showing incorrectly, Please check more");
			Assert.assertTrue(false);
		}

		return this;
	}

	private static Locator MYACCOUNT_METERING_PURCHASED_STORAGE_TABLE = new Locator(
			"MYACCOUNT_METERING_PURCHASED_STORAGE_TABLE",
			"//span[@id = 'pt1:pt2:currentTabHTMLContents:suit0a:0:i1a:0:msLabel']");

	private static Locator MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_MONTH = new Locator(
			"MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_MONTH", "./../../../tr[4]/td[2]/span");
	private static Locator MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_LATEST = new Locator(
			"MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_LATEST", "./../../../tr[4]/td[3]/span");
	private static Locator MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_SIXTH_DAY = new Locator(
			"MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_LATEST", "./../../../tr[4]/td[9]");

	private static String needCheckMetering = "PIC_BLOCK_STORAGE_STANDARD";
	private static Locator MYACCOUNT_STARTDATE = new Locator("MYACCOUNT_STARTDATE",
			"//tr[@id='pt1:pt2:currentTabHTMLName:ot6']/td[2]");

	public void loopCheckMeteringData(CloudService service, int times, String keys, String url) {
		for (int i = 0; i < times; i++) {

			ActionUtils.waitFor(TimeUnit.SECONDS, 30l);
			getMeteringData(service, keys, url);
		}
	}

	private void getMeteringData(CloudService service, String keys, String url) {

		if ((Constants.TITLE_MYSERVICE_LOGIN_IDCS.equals(driver.getTitle())
				&& CloudRegion.SYDDC_LA001.equals(CloudRegion.getCloudRegionByEnv()))
				|| ActionUtils.checkPageTitle(Constants.TITLE_MYSERVICE_LOGIN_V31, CheckType.EQUAL, false)
				|| ActionUtils.checkPageTitle(Constants.TITLE_MYSERVICE_LOGIN, CheckType.EQUAL, false)) {
			AutomationActionFactory.getInstance().getAction(service, LoginServiceAction.class)
					.loginMyServiceWithFullInfo(service, url);
			logger.info(LoggerType.STEP, " Getting Billing Metrics in Dashboard Page");
		}

		Map<String, String> ms = AutomationPageFactory.getInstance().getPage(service, DashboardPage.class)
				.getAllLatestMetrics(url, service);
		for (Entry<String, String> e : ms.entrySet()) {
			logger.info(LoggerType.COMMENT, e.getKey() + ": " + e.getValue());
		}

		List<String> meteringDataList = AutomationPageFactory.getInstance().getPage(service, MyServicePage.class)
				.getMeteringUsage(url, true, true);

		// List<String> data =
		// AutomationPageFactory.getInstance().getPage(service,
		// ServiceDetailPage.class).getMeteringUsage(url, true,
		// false);
		AutomationPageFactory.getInstance().getPage(service, ServiceDetailPage.class).printMetering(Arrays.asList(keys),
				meteringDataList);
	}

	public boolean validateBMMetering(CloudService service, String url) {

		boolean flag = false;
		int i = 0;

		List<String> meteringDataList = AutomationPageFactory.getInstance().getPage(service, MyServicePage.class)
				.getMeteringUsage(url, true, true);

		ActionUtils.waitFor(TimeUnit.SECONDS, 5l);

		String startdate = ActionUtils.getText(MYACCOUNT_STARTDATE);
		// String today = getDate("America/Los_Angeles");
		logger.info(LoggerType.STEP, "The Start date is " + startdate + ",today is " + getDate("America/Los_Angeles"));

		// Map<Integer, String> map = new HashMap<Integer, String>();
		while (i < meteringDataList.size()) {

			String rowData = meteringDataList.get(i).trim();

			if (rowData.contains(needCheckMetering)) {

				logger.info(LoggerType.COMMENT, "In list The" + i + "element value is" + rowData);

				String[] values = rowData.split("\\|");

				if (hasDigit(values[3])) {

					logger.info(LoggerType.COMMENT,
							needCheckMetering + " the " + values[2] + "value is showing " + values[3]);
					flag = true;

				} else {

					logger.info(LoggerType.COMMENT,
							"Against " + needCheckMetering + " There has Unexpected Metering value" + rowData);

					flag = false;
					Assert.assertTrue(flag);

				} // if-else

			} // if

			i++;

		} // while

		return flag;

	}

	public List<String> getMeteringUsage(CloudService service, String url) {
		List<String> meteringDataList = AutomationPageFactory.getInstance().getPage(service, MyServicePage.class)
				.getMeteringUsage(url, true, true);
		return meteringDataList;
	}

	public boolean validateMetering(List<String> meteringDataList, String SKU) {

		boolean flag = false;
		int i = 0;

		logger.info(LoggerType.COMMENT, "Validating Metetering data for: " + SKU + meteringDataList.size());
		ActionUtils.waitFor(TimeUnit.SECONDS, 5l);

		// String startdate = ActionUtils.getText(MYACCOUNT_STARTDATE);
		// // String today = getDate("America/Los_Angeles");
		// logger.info(LoggerType.STEP, "The Start date is " + startdate +
		// ",today is " + getDate("America/Los_Angeles"));

		// Map<Integer, String> map = new HashMap<Integer, String>();
		while (i < meteringDataList.size()) {

			String rowData = meteringDataList.get(i).trim();

			if (rowData.contains(SKU)) {

				logger.info(LoggerType.COMMENT, "In list The" + i + "element value is" + rowData);

				String[] values = rowData.split("\\|");

				if (hasDigit(values[3])) {

					logger.info(LoggerType.COMMENT, SKU + " the " + values[2] + "value is showing " + values[3]);
					flag = true;

				} else {

					logger.info(LoggerType.COMMENT,
							"Against " + SKU + " There has Unexpected Metering value" + rowData);

					flag = false;
					Assert.assertTrue(flag);

				} // if-else

			} // if

			i++;

		} // while

		return flag;

	}

	public void validateBMMetering(List<String> meteringDataList, String SKU) {

		logger.info(LoggerType.COMMENT, "Validating..." + meteringDataList.size());
		ActionUtils.waitFor(TimeUnit.SECONDS, 5l);

		// Map<Integer, String> map = new HashMap<Integer, String>();

		boolean valid_sku_data = false;
		for (int i = 0; i < meteringDataList.size(); i++) {
			String row = meteringDataList.get(i);
			String[] row_elements = row.split("\t");
			if (row_elements[0].contains(SKU)) {
				logger.info(LoggerType.COMMENT, "Found in line " + i);
				logger.info(LoggerType.COMMENT, "Element to check: '" + row_elements[0] + "'");
				logger.info(LoggerType.COMMENT, "SKU: '" + row_elements[1] + "'");
				logger.info(LoggerType.COMMENT, "Value: '" + row_elements[2] + "'");
				if (hasDigit(row_elements[2])) {
					valid_sku_data = true;
					logger.info(LoggerType.COMMENT, "SKU '" + SKU + "' has valid data");
				} else {
					logger.info(LoggerType.COMMENT, "SKU '" + SKU + "' doesn't has valid data");
				}
				break;
			}

		}
		Assert.assertTrue(valid_sku_data);

	}

	public String getDate(String TZ) {// LA TIMEZONE
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone(TZ));

		String sDate = sdf.format(new Date());
		return sDate;
	}

	// public boolean isDigit(String strNum) {
	//
	// logger.info(LoggerType.COMMENT, "Against" + "value is showing "
	// + strNum);
	//
	// return strNum.contains("[0-9]{1,}");
	// }

	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

	private static Locator MYACCOUNT_DETAILPAGE_METERING_TAB = new Locator("MYACCOUNT_DETAILPAGE_METERING_TAB",
			"//span[@id='pt1:pt2:ot01']");

	public void BM_Metering_Validation() {

		// go to metering tab
		ActionUtils.click(MYACCOUNT_DETAILPAGE_METERING_TAB);
		ActionUtils.waitFor(TimeUnit.SECONDS, 20);

		// check metering usage table
		WebElement f = ActionUtils.findElement(MYACCOUNT_METERING_PURCHASED_STORAGE_TABLE);

		String errorStr = "--";

		// 1. check the 2 span
		String tmp = ActionUtils.getText(f, MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_MONTH);
		if (tmp.equals(errorStr)) {
			logger.info(LoggerType.COMMENT,
					"The Month to Date of PIC_BLOCK_STORAGE_STANDARD of MBE myaccount is not correct: " + tmp);
		} else {
			logger.info(LoggerType.COMMENT,
					"The Month to Date of PIC_BLOCK_STORAGE_STANDARDof MBE myaccount is correct: " + tmp);
		}
		Assert.assertFalse(tmp.equals(errorStr));

		tmp = ActionUtils.getText(f, MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_LATEST);
		if (tmp.equals(errorStr)) {
			logger.info(LoggerType.COMMENT,
					"The Latest for Today of PIC_BLOCK_STORAGE_STANDARD of MBE myaccount is not correct: " + tmp);
		} else {
			logger.info(LoggerType.COMMENT,
					"The Latest for Today of PIC_BLOCK_STORAGE_STANDARD  of MBE myaccount is correct: " + tmp);
		}
		Assert.assertFalse(tmp.equals(errorStr));

		// 2. check the weekly data
		tmp = ActionUtils.getText(f, MYACCOUNT_METERING_PIC_BLOCK_STORAGE_STANDARD_SIXTH_DAY);
		if (tmp.equals(errorStr)) {
			logger.info(LoggerType.COMMENT,
					"The last day of this week of Purchased Storage of MBE myaccount is not correct: " + tmp);
		} else {
			logger.info(LoggerType.COMMENT,
					"The last day of this week of Purchased Storage of MBE myaccount is correct: " + tmp);
		}
		Assert.assertFalse(tmp.equals(errorStr));

	}

	public String getExpectedString(String str) {
		String replace = "...";
		str = str.replace(replace, "");
		return str;
	}

	String accountName = Constants.P_FILE.getProperties().getProperty("opc.bm.account.name");

	public BareMetalAction disableAllBMAccount() throws InterruptedException, IOException {

		logger.info(LoggerType.COMMENT, "Starting Account disableAll Test");
		Session session = loginScriptHost(scriptHost, username, password);

		// Execute CURL request
		String cmdDisable_All = "cd /home/oracle/xueniu/latest;   fab -f tasCommand.py disable_all:" + accountName;
		String results = JSCHUtils.execJSCH(session, cmdDisable_All);
		logger.info(LoggerType.COMMENT, "Starting Account SuspendAll Test" + results);

		// Close Session
		JSCHUtils.closeSession(session);
		Assert.assertTrue(results.contains("Successfully submitted the request for account modification"));

		ActionUtils.waitFor(TimeUnit.MINUTES, 10);

		logger.info(LoggerType.COMMENT, "Checking OCI Console Login");

		return this;
	}

	public BareMetalAction enableAllBMAccount() throws InterruptedException, IOException {

		logger.info(LoggerType.COMMENT, "Starting Account enableAll Test");
		Session session = loginScriptHost(scriptHost, username, password);

		// Execute CURL request
		String cmdDisable_All = "cd /home/oracle/xueniu/latest;   fab -f tasCommand.py enable_all:" + accountName;
		String results = JSCHUtils.execJSCH(session, cmdDisable_All);
		logger.info(LoggerType.COMMENT, "Starting Account SuspendAll Test" + results);

		// Close Session
		JSCHUtils.closeSession(session);
		Assert.assertTrue(results.contains("Successfully submitted the request for account modification"));

		ActionUtils.waitFor(TimeUnit.MINUTES, 10);

		return this;
	}

	Locator usernameInput = LocatorManager.getInstance().getLocator("bmc.login.username");
	Locator passwordInput = LocatorManager.getInstance().getLocator("bmc.login.password");
	// Locator signInButton =
	// LocatorManager.getInstance().getLocator("bmc.login.signin");

	private static Locator MYSERVICE_IDCS_MESSAGE_DISABLE_ALL = new Locator("MYSERVICE_IDCS_MESSAGE_DISABLE_ALL",
			"//div[@class='sec']");

	private static Locator MYSERVICE_IDCS_MESSAGE_ENABLE_ALL = new Locator("MYSERVICE_IDCS_MESSAGE_ENABLE_ALL",
			"//div[@class='idcs-signin-header-text-container']");

	public BareMetalAction loginMyService(String expectedTitle, int times) throws InterruptedException {

		logger.info(LoggerType.COMMENT, "The userName is " + userName);
		logger.info(LoggerType.COMMENT, "The password is " + userPassword);
		logger.info(LoggerType.COMMENT, "The myservice url is " + MyServiceUrl);

		logger.info(LoggerType.CHECKPOINT, "Check the title is: " + expectedTitle.toString());

		if (times > 3) {
			logger.error(LoggerType.EXCEPTION, "Did not get the expected state in 3 times");

			logger.info(LoggerType.COMMENT, "Try Login Page");

			// AutomationPageFactory.getInstance().getPage(cloudService,
			// MyServiceLoginPage.class)
			// .loginMyServiceWithFullInfo(MyServiceUrl, userName, userPassword,
			// "");
			// logger.info(LoggerType.COMMENT, "The title is " +
			// ActionUtils.getTitle());
			// Assert.assertTrue(expectedTitle.equals(ActionUtils.getTitle()));
			Assert.assertTrue(false);
		}

		logger.info(LoggerType.STEP, "Go to url[" + MyServiceUrl + "]");
		driver.manage().deleteAllCookies();
		ActionUtils.gotoUrl(MyServiceUrl);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		if (ActionUtils.getTitle().contains(expectedTitle)) {

			logger.info(LoggerType.COMMENT, "The title is " + ActionUtils.getTitle());

			if (ActionUtils.isElementPresent(MYSERVICE_IDCS_MESSAGE_DISABLE_ALL)) {

				logger.info(LoggerType.COMMENT, "MyService Page Message is:  "
						+ ActionUtils.findElement(MYSERVICE_IDCS_MESSAGE_DISABLE_ALL).getText());
			} else if (ActionUtils.isElementPresent(MYSERVICE_IDCS_MESSAGE_ENABLE_ALL)) {

				logger.info(LoggerType.COMMENT, "MyService Page Message is:  "
						+ ActionUtils.findElement(MYSERVICE_IDCS_MESSAGE_ENABLE_ALL).getText());
			}

			Assert.assertTrue(true);

		} else {
			logger.error(LoggerType.EXCEPTION, "Did not get the expected state");

			ActionUtils.waitFor(TimeUnit.SECONDS, 30);

			logger.info(LoggerType.COMMENT, "Try to login again, this is " + times + " time");
			ActionUtils.waitFor(TimeUnit.MINUTES, 1);
			loginMyService(expectedTitle, ++times);

		}

		return this;
	}

}
