package com.oracle.opc.automation.test.component.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.jcraft.jsch.Session;
import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.common.utils.JSCHUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.component.factory.AutomationPageFactory;
import com.oracle.opc.automation.test.component.pages.BMConsolePage;

import net.sf.json.JSONObject;

public class BMConsoleAction extends AbstractAutomationAction implements AutomationAction {

	String userName;
	String userPassword;
	String account;
	String consoleUrl;
	private String oci_user_page;;
	public final static String TITLE_BM_CONSOLE = "Oracle Cloud Infrastructure";
	public final static String TITLE_BM_CONSOLE_LOGIN = "Oracle Cloud Infrastructure | Sign In";

	public static String EMAIL_ADDRESS;

	private static final SimpleDateFormat DATE_FORMATOR_FOR_START = new SimpleDateFormat("yyyy-MM-dd--HH:mm");
	private static final SimpleDateFormat DATE_FORMATOR_FOR_PROCESSING = new SimpleDateFormat("yyyy-MM-dd");

	public BMConsoleAction() {
		// super();
		userName = Constants.P_FILE.getProperties().getProperty("opc.bm.user.name");
		userPassword = Constants.P_FILE.getProperties().getProperty("service.confidential.pwd");
		account = Constants.P_FILE.getProperties().getProperty("opc.bm.account.name");
		String region = Constants.P_FILE.getProperties().getProperty("opc.bm.region");

		consoleUrl = "https://" + region + "/?tenant=" + account;
		oci_user_page = "https://" + region + "/a/identity/users";

	}

	public BMConsoleAction(CloudService cloudService) {
		super(cloudService);

	}

	// Regular
	Locator usernameInput = LocatorManager.getInstance().getLocator("bmc.login.username");
	Locator passwordInput = LocatorManager.getInstance().getLocator("bmc.login.password");
	Locator signInButton = LocatorManager.getInstance().getLocator("bmc.login.signin");

	// IDCS Federation
	Locator usernameInputIDCS = LocatorManager.getInstance().getLocator("bmc.login.idcs.username");
	Locator passwordInputIDCS = LocatorManager.getInstance().getLocator("bmc.login.idcs.password");
	Locator signInButtonIDCS = LocatorManager.getInstance().getLocator("bmc.login.idcs.signin");

	Locator BMC_USER_BUTTON = new Locator("BMC_USER_BUTTON", ".//span[@data-oui-icon='icon: user']");

	public BMConsoleAction loginBMConsole() {

		logger.info(LoggerType.STEP, "Go to BM Console [" + consoleUrl + "]");
		driver.manage().deleteAllCookies();
		ActionUtils.gotoUrl(consoleUrl);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		ActionUtils.type(usernameInput, userName);
		ActionUtils.type(passwordInput, userPassword);
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		if (ActionUtils.getTitle().equals(TITLE_BM_CONSOLE)) {

			logger.info(LoggerType.STEP, "Login BM Console [" + consoleUrl + "] successfully");
			Assert.assertTrue(true);

		} else {
			logger.error(LoggerType.EXCEPTION, "Failed to login BM console, Please do more check....");
			Assert.assertTrue(false);

		}
		return this;
	}

	private static Locator BM_CONSOLE_LOGIN_MESSAGE = new Locator("BM_CONSOLE_LOGIN_MESSAGE",
			"//div[@class='error-message']");

	public BMConsoleAction checkBMConsole(String expectedStatus) {

		logger.info(LoggerType.COMMENT, "Checking OCI Console Login" + "expectedStatus is: " + expectedStatus);

		logger.info(LoggerType.STEP, "Go to BM Console [" + consoleUrl + "]");
		driver.manage().deleteAllCookies();
		ActionUtils.gotoUrl(consoleUrl);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		ActionUtils.type(usernameInput, userName);
		ActionUtils.type(passwordInput, userPassword);
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		if (ActionUtils.isElementPresent(BM_CONSOLE_LOGIN_MESSAGE)) {

			logger.info(LoggerType.STEP,
					"OCI Console Login Message is: " + ActionUtils.findElement(BM_CONSOLE_LOGIN_MESSAGE).getText());

			Assert.assertTrue(ActionUtils.findElement(BM_CONSOLE_LOGIN_MESSAGE).getText().contains(expectedStatus));

			logger.info(LoggerType.STEP, "OCI Console Login Test PASS");

		} else {
			logger.error(LoggerType.EXCEPTION, "No Disable Message, Please do more check....");
			Assert.assertTrue(false);

		}
		return this;
	}

	public BMConsoleAction logoutBMConsole() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(cloudService, BMConsolePage.class).logoutDomain(consoleUrl,
				"BareMetal Console");
		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);
		String title = ActionUtils.getTitle();
		logger.info(LoggerType.COMMENT, "Check the title is " + title);
		Assert.assertTrue(TITLE_BM_CONSOLE_LOGIN.equals(title));

		return this;
	}

	private static Locator BMC_COMPARTMENT_SELECTION = new Locator("BMC_COMPARTMENT_SELECTION",
			"//*[@id='compartment-select']");

	// Locator ACCOUNT_MANAGEMENT_BUCKET_ELEMENT = new Locator(
	// "ACCOUNT_MANAGEMENT_BUCKET_ELEMENT_" + label,
	// "//li[contains(@class,'oj-menu-item')]/a[contains(@id,'"
	// + label.toUpperCase() + "')]");

	public BMConsoleAction selectCompartment() throws InterruptedException {

		String compartmentToSelect = account + " (root)";

		if (ActionUtils.isElementPresent(BMC_COMPARTMENT_SELECTION)) {

			logger.info(LoggerType.STEP, "Going to select the expected Compartment " + compartmentToSelect);
			// ActionUtils.click(BMC_COMPARTMENT_SELECTION);
			ActionUtils.select(BMC_COMPARTMENT_SELECTION, compartmentToSelect);

		} else {

			logger.info(LoggerType.EXCEPTION, "Not finding the compartment selection, Please check..... ");
			Assert.assertTrue(false);

		}

		return this;
	}

	private static Locator BMC_EMAIL_TAB_MENUE = new Locator("BMC_EMAIL_TAB_MENUE",
			"//*[@id='baseplate-root']/div/header/nav/ul/li[8]/a");

	private static Locator BMC_EMAIL_ADD_APPROVED_SENDER_BUTTON = new Locator("BMC_EMAIL_ADD_APPROVED_SENDER_BUTTON",
			"//*[@id='senders-add-sender']");

	private static Locator BMC_EMAIL_ADD_APPROVED_SENDER_POPUP_DIV = new Locator(
			"BMC_EMAIL_ADD_APPROVED_SENDER_POPUP_DIV", "//form[contains(., 'Add Sender')]");
	private static Locator BMC_APPROVED_SENDER_EMAIL_ADDRESS_INPUT = new Locator(
			"BMC_APPROVED_SENDER_EMAIL_ADDRESS_INPUT",
			".//div[@class='uk-form-controls']/input[@id='add-sender-address']");

	private static Locator BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_BUTTON = new Locator(
			"BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_BUTTON", ".//button[contains(.,'Add')]");

	private static Locator BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_ERROR = new Locator(
			"BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_ERROR", ".//div[@class='uk-grid']/p");

	public BMConsoleAction addApprovedSenders() throws InterruptedException {

		boolean flag = false;

		ActionUtils.click(BMC_EMAIL_TAB_MENUE);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		logger.info(LoggerType.STEP, "Going to Select the compartment");
		selectCompartment();

		logger.info(LoggerType.STEP, "Going to click add sender button");
		ActionUtils.findElement(BMC_EMAIL_ADD_APPROVED_SENDER_BUTTON).click();

		WebElement popupDiv = ActionUtils.findElement(BMC_EMAIL_ADD_APPROVED_SENDER_POPUP_DIV);

		logger.info(LoggerType.STEP, "Inputting email address for new Approved Sender");

		if (ActionUtils.isElementPresent(popupDiv, BMC_APPROVED_SENDER_EMAIL_ADDRESS_INPUT)) {

			ActionUtils.type(BMC_APPROVED_SENDER_EMAIL_ADDRESS_INPUT, getEmailAddress());

			ActionUtils.click(popupDiv, BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_BUTTON);
		}

		// If Error pops up or Adding succeeds
		if (!isErrorPopUp(BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_ERROR, "adding the new Approved Sender")) {

			Locator NEW_APPROVED_SENDER = new Locator("NEW_APPROVED_SENDER", getBMEmailSenderDiv(EMAIL_ADDRESS));

			logger.info(LoggerType.COMMENT, "Goint to check if new sender is added");
			if (ActionUtils.isElementPresent(NEW_APPROVED_SENDER)) {

				logger.info(LoggerType.COMMENT, "New Approved Sender is added successfully");

				flag = true;
				ActionUtils.captureScreenshot();
			}

		}

		Assert.assertTrue(flag);

		return this;

	}

	private static Locator NEW_APPROVED_SENDER_LINE_ACTION = new Locator("NEW_APPROVED_SENDER_LINE_ACTION",
			".//a[contains(@class, 'email-action-menu-link')]");

	private static Locator NEW_APPROVED_SENDER_LINE_DELETE = new Locator("NEW_APPROVED_SENDER_LINE_DELETE",
			".//a[@class='dropdown-danger-item' and contains(.,'Delete')]");

	private static Locator NEW_APPROVED_SENDER_DELETE_CONFIRMATION = new Locator(
			"NEW_APPROVED_SENDER_DELETE_CONFIRMATION",
			"//*[@id='senders-delete-modal']/form/div[3]/div/div/div/button[2]");
	// private static Locator NEW_APPROVED_SENDER_DELETE_ERROR = new Locator(
	// "NEW_APPROVED_SENDER_DELETE_ERROR", "../../p");

	public BMConsoleAction terminateApprovedSenders() throws InterruptedException {

		boolean flag = true;

		Locator NEW_APPROVED_SENDER_LINE = new Locator("NEW_APPROVED_SENDER_LINE",
				"//tr[contains(.,'" + EMAIL_ADDRESS + "')]");

		ActionUtils.click(BMC_EMAIL_TAB_MENUE);
		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		logger.info(LoggerType.STEP, "Going to Select the compartment");
		selectCompartment();

		logger.info(LoggerType.COMMENT, "Going to find the expected Sender Instance: " + EMAIL_ADDRESS);
		if (ActionUtils.findElement(NEW_APPROVED_SENDER_LINE) != null) {

			WebElement senderDiv = ActionUtils.findElement(NEW_APPROVED_SENDER_LINE);

			logger.info(LoggerType.STEP, "Choosing the Action against the Instance");

			ActionUtils.findElement(senderDiv, NEW_APPROVED_SENDER_LINE_ACTION).click();

			logger.info(LoggerType.STEP, "Going to click Terminate against the Instance");

			ActionUtils.findElement(senderDiv, NEW_APPROVED_SENDER_LINE_DELETE).click();

			ActionUtils.waitFor(TimeUnit.SECONDS, 3);

			logger.info(LoggerType.STEP, "Going to click Confirmation button");
			ActionUtils.findElement(NEW_APPROVED_SENDER_DELETE_CONFIRMATION).click();

		} else {

			flag = false;
			logger.error(LoggerType.EXCEPTION, "Failed to find the expected Sender,");

			ActionUtils.captureScreenshot();
		}

		// WebElement confirmationPopUp = ActionUtils.findElement(
		// NEW_APPROVED_SENDER_DELETE_CONFIRMATION);

		if (isErrorPopUp(BMC_APPROVED_SENDER_EMAIL_ADDRESS_ADD_ERROR, "adding the new Approved Sender")) {

			// if (ActionUtils.findElement(confirmationPopUp,
			// NEW_APPROVED_SENDER_DELETE_ERROR) != null) {
			//
			// logger.error(LoggerType.EXCEPTION, "Failed to Delete Sender,");
			//
			// logger.info(LoggerType.COMMENT, "Error message is " + ActionUtils
			// .findElement(confirmationPopUp,
			// NEW_APPROVED_SENDER_DELETE_ERROR).getText());
			// ActionUtils.captureScreenshot();
			flag = false;
		}

		Assert.assertTrue(flag);
		return this;

	}

	private static Locator BMC_EMAIL_SMTP_CONFIGURATION = new Locator("BMC_EMAIL_ADD_APPROVED_SENDER_POPUP_DIV",
			"//*[@id='email-left-nav']/ul/li[2]/a");

	private static Locator BMC_EMAIL_MANAGE_SMTP_BUTTON = new Locator("BMC_EMAIL_MANAGE_SMTP_BUTTON",
			"//*[@id='configuration-manage-credentials-button']");

	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_BUTTON = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_BUTTON",
			"//div[@class='listing-batch-actions']/button[contains(.,'Generate SMTP Credentials')]");

	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_DIV = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_DIV", "//div[@class='card-body']");
	// and contains(.,'smtp-credentials')
	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_INPUT = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_INPUT", ".//input[contains(@id,'smtp-credentials')]");

	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_SUBMIT_BUTTON = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_SUBMIT_BUTTON", ".//input[@class='action']");

	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_USERNAME = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_USERNAME", "//input[@id='generated-username']");
	// div[@class='field-wrapper' and contains(.,'Username')]
	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_PASSWORD = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_PASSWORD", "//input[@id='generated-password']");

	private static Locator BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_ERROR = new Locator(
			"BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_ERROR", ".//div[@class='card-body']/form/p");

	public BMConsoleAction configureEmailSMTP() throws InterruptedException {

		boolean flag = false;

		ActionUtils.click(BMC_EMAIL_TAB_MENUE);
		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		logger.info(LoggerType.STEP, "Going to Select the compartment");
		selectCompartment();

		logger.info(LoggerType.COMMENT, "Going to Configuration Page");
		ActionUtils.findElement(BMC_EMAIL_SMTP_CONFIGURATION).click();

		logger.info(LoggerType.COMMENT, "Going to click Manage button");
		ActionUtils.findElement(BMC_EMAIL_MANAGE_SMTP_BUTTON).click();

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);
		logger.info(LoggerType.COMMENT, "Going to Click Generate New SMTP Credentials button");
		ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_BUTTON).click();

		if (ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_DIV) != null) {

			WebElement popUpDiv = ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_DIV);

			logger.info(LoggerType.COMMENT, "Going to input Description");

			ActionUtils.type(popUpDiv, BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_INPUT,
					"For Testing" + Long.toString(System.currentTimeMillis()).substring(0));

			ActionUtils.findElement(popUpDiv, BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_POPUP_SUBMIT_BUTTON).click();

			if (!isErrorPopUp(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_ERROR, "generating SMTP Credential")
					&& ActionUtils.isElementPresent(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_USERNAME)) {

				logger.info(LoggerType.COMMENT, "UserName: "
						+ ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_USERNAME).getAttribute("value"));

				// ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_USERNAME).getAttribute(arg0)
				logger.info(LoggerType.COMMENT, "Password: "
						+ ActionUtils.findElement(BMC_EMAIL_GENERATE_SMTP_CREDENTIALS_PASSWORD).getAttribute("value"));

				flag = true;
			}

		} else {

			logger.error(LoggerType.EXCEPTION, "There is not sender line, Please check");
		}
		Assert.assertTrue(flag);

		return this;

	}

	private String getBMEmailSenderDiv(String instanceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("//table[@id='email-sender-table' and contains(.,'" + instanceName + "')] ");
		return sb.toString();
	}

	private String getBMInstanceDiv(String instanceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("//a[contains(.,'" + instanceName + "')] ");
		return sb.toString();
	}

	public String getEmailAddress() {

		// test@dyn.com
		final String EMAIL_ADDRESS_PREFIX = "cdcqatest";
		final String EMAIL_ADDRESS_SUFFIX = "@dyn.com";

		String timestamp = Long.toString(System.currentTimeMillis());
		String randomSuffix = timestamp.substring(0);

		EMAIL_ADDRESS = EMAIL_ADDRESS_PREFIX + randomSuffix + EMAIL_ADDRESS_SUFFIX;

		return EMAIL_ADDRESS.toString();
	}

	public boolean isErrorPopUp(Locator locator, String text) {

		if (ActionUtils.isElementPresent(locator)) {

			logger.error(LoggerType.EXCEPTION, "There was an error while " + text);

			logger.info(LoggerType.COMMENT, "Error message is " + ActionUtils.findElement(locator).getText());
			ActionUtils.captureScreenshot();

			return true;
		}

		return false;

	}

	public BMConsoleAction sendEmailViaSwaks() throws IOException {

		logger.info(LoggerType.COMMENT, "Check State of BM SM api Call");

		String scriptHost = Constants.P_FILE.getProperties().getProperty("opc.bm.email.dev.host");
		String username = Constants.P_FILE.getProperties().getProperty("opc.bm.email.dev.host.user");
		String password = Constants.P_FILE.getProperties().getProperty("opc.bm.email.dev.host.password");
		String accountName = Constants.P_FILE.getProperties().getProperty("opc.bm.account.name");

		Session session = AutomationActionFactory.getInstance().getAction(cloudService, BareMetalAction.class)
				.loginScriptHost(scriptHost, username, password);
		String sendEmailNum = Constants.P_FILE.getProperties().getProperty("opc.bm.email.send.num");

		String sender = Constants.P_FILE.getProperties().getProperty("opc.bm.email.sender");

		Date date = new Date();
		DATE_FORMATOR_FOR_START.setTimeZone(TimeZone.getTimeZone("UTC"));
		logger.info(LoggerType.COMMENT, "Current System Time(UTC) is: " + (DATE_FORMATOR_FOR_START.format(date)));// new

		String outputFile = "sending" + sendEmailNum + "_" + DATE_FORMATOR_FOR_START.format(date) + ".log";
		// String currentDay = df.format(date);
		logger.info(LoggerType.COMMENT, "Will send email for account: " + accountName);// new

		String curlCommand = " for i in {1.." + sendEmailNum
				+ "};  do  ./swaks --server   r1.smtp.us-phoenix-1.oraclecloud.com:25  --from  " + sender
				+ " --to blackhole@dyn.com   --auth-user " + "'"
				+ Constants.P_FILE.getProperties().getProperty("opc.bm.email.auth.user") + "'" + "  --auth-password "
				+ "'" + Constants.P_FILE.getProperties().getProperty("opc.bm.email.auth.password") + "'" + "; done >"
				+ outputFile;

		logger.info(LoggerType.COMMENT, "command is: " + curlCommand);

		JSCHUtils.execJSCH(session, curlCommand);

		String erroStr = "535 Authentication credentials invalid";
		String expectedStr = "This is a test mailing";

		// String strCheckCmd = "cat ./" + outputFile + " | tail -n 30";

		String strCheckCmd = "cat ./" + outputFile + "  | grep -c  " + " '" + expectedStr + "' ";
		String strSendNum = JSCHUtils.execJSCH(session, strCheckCmd);

		logger.info(LoggerType.COMMENT, strCheckCmd + "  Checking Result: \n" + strSendNum);
		JSCHUtils.closeSession(session);

		Assert.assertTrue(strSendNum.trim().equals(sendEmailNum));

		return this;

	}

	// private static Locator BMC_SANDWICH_BUTTON = new
	// Locator("BMC_SANDWICH_BUTTON",
	// ".//*[@id='duplo-mobile-navigation']/div[1]/a");

	private static Locator BMC_SANDWICH_BUTTON = new Locator("BMC_SANDWICH_BUTTON",
			".//*[@id='console-nav-container']/div[1]/span");

	private static Locator BMC_IDENTITY_TAB = new Locator("BMC_IDENTITY_TAB",
			".//*[@id='console-nav-menu-container']/div[2]/ul/li[contains(., 'Identity')]");

	private static Locator BMC_USER_LINK = new Locator("BMC_USER_LINK", ".//a[contains(., 'User')]");

	private static Locator BMC_ADD_PUBLIC_KEY_BUTTON = new Locator("BMC_ADD_PUBLIC_KEY_BUTTON",
			".//button[@class='action create' and contains(., 'Add Public Key')]");

	private static Locator BMC_ADD_PUBLIC_KEY_TEXTAREA = new Locator("BMC_ADD_PUBLIC_KEY_TEXTAREA",
			".//*[@id='new-user-api-key']");

	private static Locator BMC_ADD_PUBLIC_KEY_ADD_BUTTON = new Locator("BMC_ADD_PUBLIC_KEY_ADD_BUTTON",
			".//*[@id='mountpoint']/div/div[2]/div/div/div[2]/form/input");

	private static Locator BMC_ADD_PUBLIC_KEY_ERROR = new Locator("BMC_ADD_PUBLIC_KEY_ERROR",
			".//*[@id='mountpoint']/div/div[2]/div/div/div[2]/form/p[2]");

	// private static Locator BMC_USER_CREATE_BUTTON = new
	// Locator("BMC_USER_CREATE_BUTTON",
	// ".//button[@class='action create' and contains(., 'Create User')]");

	public BMConsoleAction uploadPublicKeyUser() throws IOException {

		// Define date format

		String BMCUser = Constants.P_FILE.getProperties().getProperty("opc.bm.user.name");

		// Locator BMC_USER_EXPECTED_LINE = new Locator("BMC_USER_EXPECTED_LINE",
		// ".//a[contains(.,'\" + BMCUser + \"')]");
		// Locator BMC_USER_EXPECTED_LINE = new Locator("BMC_USER_EXPECTED_LINE",
		// ".//a[contains(.,'" + BMCUser + "')]");
		Locator BMC_USER_EXPECTED_LINE = new Locator("BMC_USER_EXPECTED_LINE",
				".//div[@class='list-cell-item']/a[contains(.,'" + BMCUser + "')]");

		Locator BMC_SHOW_USER_OCID = new Locator("BMC_SHOW_USER_OCID", ".//a[contains(.,'Show')]");
		Locator BMC_USER_OCID = new Locator("BMC_USER_OCID", ".//span[contains(.,'ocid1.user')]");
		Locator BMC_CANCEL_ADD = new Locator("BMC_TENANCY_OCID", ".//a[contains(.,'Cancel')]");

		logger.info(LoggerType.STEP, "Go to identity user page [" + oci_user_page + "]");
		ActionUtils.gotoUrl(oci_user_page);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.STEP, "Going to Expected User: " + BMCUser + " Details Page");

		ActionUtils.findElement(BMC_USER_EXPECTED_LINE).click();

		if (ActionUtils.isElementPresent(BMC_ADD_PUBLIC_KEY_BUTTON)) {
			logger.info(LoggerType.STEP, "Clicking Add Public Key Button");

			ActionUtils.click(BMC_ADD_PUBLIC_KEY_BUTTON);

			if (ActionUtils.isElementPresent(BMC_ADD_PUBLIC_KEY_TEXTAREA)) {

				logger.info(LoggerType.STEP, "Inputting Public Key ");
				// getKey();
				ActionUtils.type(BMC_ADD_PUBLIC_KEY_TEXTAREA, getKeyFromFile());
				ActionUtils.findElement(BMC_ADD_PUBLIC_KEY_ADD_BUTTON).click();

				// Get the timestamp of adding public key
				String addingTimeStamp = getCurrentTimeUTC();

				Locator BMC_USER_FINGERPRINT = new Locator("BMC_USER_FINGERPRINT",
						".//div[@class='listing' and contains(.,'" + addingTimeStamp + "')]");

				ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

				if (ActionUtils.isElementPresent(BMC_ADD_PUBLIC_KEY_ERROR)) {
					logger.info(LoggerType.EXCEPTION,
							"Failed to add public key: " + ActionUtils.getText(BMC_ADD_PUBLIC_KEY_ERROR));
					ActionUtils.findElement(BMC_CANCEL_ADD).click();
				} else if (ActionUtils.isElementPresent(BMC_USER_FINGERPRINT)) {

					logger.info(LoggerType.COMMENT, "Added Successfully, Generated FingerPrint is: "
							+ ActionUtils.getText(BMC_USER_FINGERPRINT));

				}
			}

		}

		logger.info(LoggerType.STEP, "Click to get user OCID");
		ActionUtils.findElement(BMC_SHOW_USER_OCID).click();
		String user_ocid = ActionUtils.findElement(BMC_USER_OCID).getText();
		user_ocid = user_ocid.substring(0, user_ocid.length() - 8);
		String tenancy_ocid = getTenancyOCID();
		logger.info(LoggerType.COMMENT, "User_OCID: '" + user_ocid + "'");
		logger.info(LoggerType.COMMENT, "Tenancy_OCID: '" + tenancy_ocid + "'");
		String region = Constants.P_FILE.getProperties().getProperty("opc.bm.region");
		createAccountConfigFile(user_ocid, tenancy_ocid, region);

		return this;

	}

	private String getTenancyOCID() {
		String accountName = Constants.P_FILE.getProperties().getProperty("opc.bm.account.name");
		Locator BMC_ACCOUNT_EXPECTED_LINE = new Locator("BMC_ACCOUNT_EXPECTED_LINE",
				".//div//a[contains(.,'" + accountName + "')]");
		Locator BMC_SHOW_TENANCY_OCID = new Locator("BMC_SHOW_TENANCY_OCID", ".//a[contains(.,'Show')]");
		Locator BMC_TENANCY_OCID = new Locator("BMC_TENANCY_OCID", ".//span[contains(.,'ocid1.tenancy')]");

		logger.info(LoggerType.STEP, "Clicking the User Button in top of the page ");
		WebElement bmc_user_button = ActionUtils.findElement(BMC_USER_BUTTON);
		ActionUtils.moveToElement(bmc_user_button, 5);
		// bmc_user_button.click();
		// ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.STEP, "Going to tenant name: " + accountName + " Details Page");

		ActionUtils.findElement(BMC_ACCOUNT_EXPECTED_LINE).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.STEP, "Click to get Tenancy OCID");
		ActionUtils.findElement(BMC_SHOW_TENANCY_OCID).click();
		String tenancy_ocid = ActionUtils.findElement(BMC_TENANCY_OCID).getText();
		tenancy_ocid = tenancy_ocid.substring(0, tenancy_ocid.length() - 8);

		return tenancy_ocid;
	}

	private void createAccountConfigFile(String user_ocid, String tenancy_ocid, String region) throws IOException {
		String apiRegion = region;
		if (region.equals("console.r1.oracleiaas.com"))
			apiRegion = "r1.oracleiaas.com";
		else if (region.equals("console.us-ashburn-1.oraclecloud.com"))
			apiRegion = "us-ashburn-1";

		JSONObject account_config_obj = new JSONObject();
		account_config_obj.put("user", user_ocid);
		account_config_obj.put("key_file", "config/oci_api_key.pem");
		account_config_obj.put("fingerprint", "09:e4:86:07:74:59:8a:94:5f:7f:06:4e:cc:ae:9e:16");
		account_config_obj.put("tenancy", tenancy_ocid);
		account_config_obj.put("region", apiRegion);

		String fileName = Constants.P_FILE.getProperties().getProperty("py.account.config");
		try (FileWriter file = new FileWriter(fileName)) {
			file.write(account_config_obj.toString());
			System.out.println("Successfully Copied JSON Object to File...");
		}

	}

	public String getKeyFromFile() throws IOException {
		// Read key from file

		logger.info(LoggerType.COMMENT, "Loading public key file");

		String fileName = Constants.P_FILE.getProperties().getProperty("api.publicKey.pem.path");
		String strKeyPEM = "";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = br.readLine()) != null) {
			strKeyPEM += line + "\n";
		}
		br.close();

		return strKeyPEM;
	}

	public String getCurrentTimeUTC() {

		SimpleDateFormat dateFormatExpected = new SimpleDateFormat("HH:mm", Locale.US);
		Date date = new Date();
		dateFormatExpected.setTimeZone(TimeZone.getTimeZone("GMT"));
		logger.info(LoggerType.COMMENT, "Key added Time(GMT) is: " + (dateFormatExpected.format(date)));// new

		return dateFormatExpected.format(date);
	}

	public void loginBMFederation() {
		String consoleUrlIDCS = consoleUrl + "&provider=OracleIdentityCloudService";
		logger.info(LoggerType.STEP, "Go to BM Console federation [" + consoleUrlIDCS + "]");
		driver.manage().deleteAllCookies();
		ActionUtils.gotoUrl(consoleUrlIDCS);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		ActionUtils.type(usernameInputIDCS, userName);
		ActionUtils.type(passwordInputIDCS, userPassword);
		ActionUtils.findElement(signInButtonIDCS).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		if (ActionUtils.getTitle().equals(TITLE_BM_CONSOLE)) {

			logger.info(LoggerType.STEP, "Login BM Console [" + consoleUrl + "] successfully");
			Assert.assertTrue(true);

		} else {
			logger.error(LoggerType.EXCEPTION, "Failed to login BM console, Please do more check....");
			Assert.assertTrue(false);

		}
	}

	// public RSAPublicKey getPublicKeyFromString(String key) throws
	// IOException,
	// GeneralSecurityException {
	// String publicKeyPEM = key;
	// publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
	// publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
	// byte[] encoded = Base64.decodeBase64(publicKeyPEM);
	// KeyFactory kf = KeyFactory.getInstance("RSA");
	// RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new
	// X509EncodedKeySpec(encoded));
	// return pubKey;
	// }

	// public static RSAPublicKey getPublicKey() throws IOException,
	// GeneralSecurityException {
	// String fileName =
	// Constants.P_FILE.getProperties().getProperty("api.publicKey.pem.path");
	// String publicKeyPEM = getKey(fileName);
	// return getPublicKeyFromString(publicKeyPEM);
	// }

}
