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

import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;

import net.sf.json.JSONObject;

public class OCIConsoleAction extends AbstractAutomationAction
		implements AutomationAction {

	private String userName;
	private String userPassword;
	private String tempPassword;
	private String account;
	private String consoleUrl;
	private String region;
	private String ociUsersPage;
	private String ociTenancyPage;
	private String sshKeyFile;
	private String accountConfigFile;

	private final static String TITLE_BM_CONSOLE = "Oracle Cloud Infrastructure";

	public OCIConsoleAction() {
		// super();
		userName = Constants.P_FILE.getProperties()
				.getProperty("oci.user.name");
		userPassword = Constants.P_FILE.getProperties()
				.getProperty("oci.user.password");
		tempPassword = Constants.P_FILE.getProperties()
				.getProperty("oci.user.tempPassword");
		account = Constants.P_FILE.getProperties()
				.getProperty("oci.account.name");
		region = Constants.P_FILE.getProperties()
				.getProperty("oci.account.region");

		// PEM Key File
		sshKeyFile = Constants.P_FILE.getProperties()
				.getProperty("oci.user.apiKey");

		// Account configuration file
		accountConfigFile = Constants.P_FILE.getProperties()
				.getProperty("oci.account.configuration");

		// URLS and pages
		consoleUrl = "https://" + region + "/?tenant=" + account;
		ociUsersPage = "https://" + region + "/a/identity/users";
		ociTenancyPage = "https://" + region + "/a/tenancy";

		// Profile-based locators
		BMC_USER_EXPECTED_LINE = new Locator("BMC_USER_EXPECTED_LINE",
				".//div[@class='list-cell-item']/a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"
						+ userName.toLowerCase() + "')]");

	}

	public OCIConsoleAction(CloudService cloudService) {
		super(cloudService);

	}

	// OCI Console Login Locators
	private Locator usernameInput = LocatorManager.getInstance()
			.getLocator("bmc.login.username");
	private Locator passwordInput = LocatorManager.getInstance()
			.getLocator("bmc.login.password");
	private Locator signInButton = LocatorManager.getInstance()
			.getLocator("bmc.login.signin");

	// IDCS Federation Login Locators
	private Locator usernameInputIDCS = LocatorManager.getInstance()
			.getLocator("bmc.login.idcs.username");
	private Locator passwordInputIDCS = LocatorManager.getInstance()
			.getLocator("bmc.login.idcs.password");
	private Locator signInButtonIDCS = LocatorManager.getInstance()
			.getLocator("bmc.login.idcs.signin");

	// Reset Password Locators
	private Locator currentPassword = LocatorManager.getInstance()
			.getLocator("bmc.current.password");
	private Locator newPassword = LocatorManager.getInstance()
			.getLocator("bmc.new.password");
	private Locator confirmPassword = LocatorManager.getInstance()
			.getLocator("bmc.confirm.password");
	private Locator savePassword = LocatorManager.getInstance()
			.getLocator("bmc.save.password");

	// User Locator
	private Locator BMC_USER_EXPECTED_LINE;
	private Locator BMC_SHOW_USER_OCID = new Locator("BMC_SHOW_USER_OCID",
			".//a[contains(.,'Show')]");
	private Locator BMC_USER_OCID = new Locator("BMC_USER_OCID",
			".//span[contains(.,'ocid1.user')]");
	private Locator BMC_CANCEL_ADD = new Locator("BMC_TENANCY_OCID",
			".//a[contains(.,'Cancel')]");

	// API Key Locators
	private Locator BMC_ADD_PUBLIC_KEY_BUTTON = new Locator(
			"BMC_ADD_PUBLIC_KEY_BUTTON",
			".//button[@class='action create' and contains(., 'Add Public Key')]");

	private Locator BMC_ADD_PUBLIC_KEY_TEXTAREA = new Locator(
			"BMC_ADD_PUBLIC_KEY_TEXTAREA", ".//*[@id='new-user-api-key']");

	private Locator BMC_ADD_PUBLIC_KEY_ADD_BUTTON = new Locator(
			"BMC_ADD_PUBLIC_KEY_ADD_BUTTON",
			".//*[@id='mountpoint']/div/div[2]/div/div/div[2]/form/input");

	private Locator BMC_ADD_PUBLIC_KEY_ERROR = new Locator(
			"BMC_ADD_PUBLIC_KEY_ERROR",
			".//*[@id='mountpoint']/div/div[2]/div/div/div[2]/form/p[2]");

	// Tenant Locator
	private Locator BMC_SHOW_TENANCY_OCID = new Locator("BMC_SHOW_TENANCY_OCID",
			".//a[contains(.,'Show')]");
	private Locator BMC_TENANCY_OCID = new Locator("BMC_TENANCY_OCID",
			".//span[contains(.,'ocid1.tenancy')]");

	private String getKeyFingerPrint() throws IOException {
		String keyFingerPrint = null;

		// Add key
		logger.info(LoggerType.STEP, "Clicking Add Public Key Button");
		ActionUtils.click(BMC_ADD_PUBLIC_KEY_BUTTON);

		logger.info(LoggerType.STEP, "Inputting Public Key ");
		ActionUtils.type(BMC_ADD_PUBLIC_KEY_TEXTAREA, getKeyFromFile());
		ActionUtils.findElement(BMC_ADD_PUBLIC_KEY_ADD_BUTTON).click();

		// Get the timestamp of adding public key
		String addingTimeStamp = getCurrentTimeUTC();

		Locator BMC_USER_FINGERPRINT = new Locator("BMC_USER_FINGERPRINT",
				".//div[@class='listing' and contains(.,'" + addingTimeStamp
						+ "')]");

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		if (ActionUtils.isElementPresent(BMC_ADD_PUBLIC_KEY_ERROR)) {
			String addKeyError = ActionUtils.getText(BMC_ADD_PUBLIC_KEY_ERROR);
			logger.info(LoggerType.EXCEPTION,
					"Failed to add public key: [" + addKeyError + "]");
			keyFingerPrint = addKeyError.split("'")[1];
			ActionUtils.findElement(BMC_CANCEL_ADD).click();
		} else if (ActionUtils.isElementPresent(BMC_USER_FINGERPRINT)) {
			keyFingerPrint = ActionUtils.getText(BMC_USER_FINGERPRINT);
			logger.info(LoggerType.COMMENT, "Added Successfully");

		}
		logger.info(LoggerType.COMMENT,
				"Finger print is: [" + keyFingerPrint + "]");
		return keyFingerPrint;
	}

	private String getUserOCID() {
		logger.info(LoggerType.STEP, "Click to get user OCID");
		ActionUtils.findElement(BMC_SHOW_USER_OCID).click();
		String user_ocid = ActionUtils.findElement(BMC_USER_OCID).getText();
		user_ocid = user_ocid.substring(0, user_ocid.length() - 8);
		return user_ocid;
	}

	private String getTenancyOCID() {
		logger.info(LoggerType.STEP,
				"Going to tenant [" + ociTenancyPage + "] Details page");
		ActionUtils.gotoUrl(ociTenancyPage);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		logger.info(LoggerType.STEP, "Click to get Tenancy OCID");
		ActionUtils.findElement(BMC_SHOW_TENANCY_OCID).click();
		String tenancy_ocid = ActionUtils.findElement(BMC_TENANCY_OCID)
				.getText();
		tenancy_ocid = tenancy_ocid.substring(0, tenancy_ocid.length() - 8);

		return tenancy_ocid;
	}

	private void createAccountConfigFile(String keyFingerPrint,
			String user_ocid, String tenancy_ocid, String region)
			throws IOException {
		String apiRegion = region;
		if (region.equals("console.r1.oracleiaas.com"))
			apiRegion = "r1.oracleiaas.com";
		else if (region.equals("console.us-ashburn-1.oraclecloud.com"))
			apiRegion = "us-ashburn-1";

		JSONObject account_config_obj = new JSONObject();
		account_config_obj.put("user", user_ocid);
		account_config_obj.put("key_file", "config/oci_api_key.pem");
		account_config_obj.put("fingerprint", keyFingerPrint);
		account_config_obj.put("tenancy", tenancy_ocid);
		account_config_obj.put("region", apiRegion);
		account_config_obj.put("comming_from_selenium", true);

		try (FileWriter file = new FileWriter(accountConfigFile)) {
			file.write(account_config_obj.toString());
			System.out.println("Successfully Copied JSON Object to File...");
		}

	}

	private String getKeyFromFile() throws IOException {
		// Read key from file
		logger.info(LoggerType.COMMENT, "Loading public key file");
		String strKeyPEM = "";
		BufferedReader br = new BufferedReader(new FileReader(sshKeyFile));
		String line;
		while ((line = br.readLine()) != null) {
			strKeyPEM += line + "\n";
		}
		br.close();
		return strKeyPEM;
	}

	private String getCurrentTimeUTC() {
		SimpleDateFormat dateFormatExpected = new SimpleDateFormat("HH:mm",
				Locale.US);
		Date date = new Date();
		dateFormatExpected.setTimeZone(TimeZone.getTimeZone("GMT"));
		logger.info(LoggerType.COMMENT,
				"Key added Time(GMT) is: " + (dateFormatExpected.format(date)));// new
		return dateFormatExpected.format(date);
	}

	private void loginConsole(String consoleUrl, Locator usernameInput,
			Locator passwordInput, Locator signInButton,
			String currentPassword) {
		logger.info(LoggerType.STEP, "Go to BM Console [" + consoleUrl + "]");
		driver.manage().deleteAllCookies();
		ActionUtils.gotoUrl(consoleUrl);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		ActionUtils.type(usernameInput, userName);
		ActionUtils.type(passwordInput, currentPassword);
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
	}

	private void checkloginConsole(String consoleUrl, Locator usernameInput,
			Locator passwordInput, Locator signInButton) {

		// Attempt Login
		loginConsole(consoleUrl, usernameInput, passwordInput, signInButton,
				userPassword);

		// Check Login Completed
		if (ActionUtils.getTitle().equals(TITLE_BM_CONSOLE)) {
			logger.info(LoggerType.STEP,
					"Login BM Console [" + consoleUrl + "] successfully");
			Assert.assertTrue(true);
		} else {
			logger.error(LoggerType.EXCEPTION,
					"Failed to login BM console, Please do more check....");
			Assert.assertTrue(false);

		}
	}

	public void changeTemporaryPassword() {
		// Attempt Login
		loginConsole(consoleUrl, usernameInput, passwordInput, signInButton,
				tempPassword);
		logger.info(LoggerType.COMMENT,
				"[Step] Check if password can be reset");
		if (ActionUtils.areElementsPresent(currentPassword)) {
			logger.info(LoggerType.COMMENT, "[Step] Proceed to reset password");
			logger.info(LoggerType.COMMENT, "[Step] Type current password");
			ActionUtils.type(currentPassword, tempPassword);
			logger.info(LoggerType.COMMENT, "[Step] Type new password");
			ActionUtils.type(newPassword, userPassword);
			logger.info(LoggerType.COMMENT,
					"[Step] Type confirmation password");
			ActionUtils.type(confirmPassword, userPassword);
			logger.info(LoggerType.COMMENT, "[Step] Save password");
			ActionUtils.findElement(savePassword).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		} else {
			logger.info(LoggerType.COMMENT, "[Step] No need to reset password");
		}
	}

	public void loginBMFederation() {
		logger.info(LoggerType.STEP, "Using IDCS Federation Login");
		String consoleUrlIDCS = consoleUrl
				+ "&provider=OracleIdentityCloudService";
		checkloginConsole(consoleUrlIDCS, usernameInputIDCS, passwordInputIDCS,
				signInButtonIDCS);
	}

	public void loginBMConsole() {
		logger.info(LoggerType.STEP, "Using Identity Service Login");
		checkloginConsole(consoleUrl, usernameInput, passwordInput,
				signInButton);
	}

	public void setupAccountConfigFile() throws IOException {
		// Go to Users Page
		logger.info(LoggerType.STEP,
				"Go to identity user page [" + ociUsersPage + "]");
		ActionUtils.gotoUrl(ociUsersPage);
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Open OCI User
		logger.info(LoggerType.STEP,
				"Going to Expected User: " + userName + " Details Page");
		ActionUtils.findElement(BMC_USER_EXPECTED_LINE).click();

		// Get Details
		String keyFingerPrint = getKeyFingerPrint();
		String user_ocid = getUserOCID();
		String tenancy_ocid = getTenancyOCID();

		// Summary
		logger.info(LoggerType.COMMENT,
				"Finger Print: '" + keyFingerPrint + "'");
		logger.info(LoggerType.COMMENT, "User OCID: '" + user_ocid + "'");
		logger.info(LoggerType.COMMENT, "Tenancy OCID: '" + tenancy_ocid + "'");

		// Write Account Configuration File
		createAccountConfigFile(keyFingerPrint, user_ocid, tenancy_ocid,
				region);
	}

	public void setupPipeline() throws IOException {
		logger.info(LoggerType.COMMENT, "Create pipeline configuration file");

		// Get details from configuration file
		String pipelineService = Constants.P_FILE.getProperties()
				.getProperty("pipeline.service").toUpperCase();
		logger.info(LoggerType.COMMENT,
				"Pipeline Service: [" + pipelineService + "]");

		String pipelineTestsuite = Constants.P_FILE.getProperties()
				.getProperty("pipeline.testsuite").toUpperCase();
		logger.info(LoggerType.COMMENT,
				"Pipeline Testsuite: [" + pipelineTestsuite + "]");

		String pipelineConfigurationFile = Constants.P_FILE.getProperties()
				.getProperty("pipeline.configuration");
		logger.info(LoggerType.COMMENT, "Pipeline Configuration File: ["
				+ pipelineConfigurationFile + "]");

		// Build JSON
		JSONObject pipelineConfiguration = new JSONObject();
		pipelineConfiguration.put("SERVICE", pipelineService);
		pipelineConfiguration.put("TESTSUITE", pipelineTestsuite);

		// Write JSON
		try (FileWriter file = new FileWriter(pipelineConfigurationFile)) {
			file.write(pipelineConfiguration.toString());
			logger.info(LoggerType.COMMENT,
					"Successfully Copied JSON Object to File...");
		}
	}

}
