package com.oracle.opc.automation.test.testcase.baremetal.action;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.WebDriverManager;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.actions.AbstractAutomationAction;
import com.oracle.opc.automation.test.component.actions.AutomationAction;

public class BMCreateTerminateStorageAction extends AbstractAutomationAction
		implements AutomationAction {

	Locator status;
	WebElement statusElement;
	private static final long TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS = 10L;
	public static final String BASE_UPLOAD_DIR = "./classes/Upload";

	public BMCreateTerminateStorageAction(CloudService cloudService) {
		super(cloudService);
	}

	public BMCreateTerminateStorageAction() {
		super();
	}

	// by xiong qin
	public void BM_Create_BlockVolume(String ADname)
			throws InterruptedException, IOException {
		ActionUtils.gotoUrl(
				Constants.P_FILE.getProperties().getProperty("bmconsole.url"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Login
		// Login
		Locator usernameInput = LocatorManager.getInstance()
				.getLocator("bmc.login.username");
		Locator passwordInput = LocatorManager.getInstance()
				.getLocator("bmc.login.password");
		Locator signInButton = LocatorManager.getInstance()
				.getLocator("bmc.login.signin");
		ActionUtils.type(usernameInput, Constants.P_FILE.getProperties()
				.getProperty("opc.bm.user.name"));
		ActionUtils.type(passwordInput, Constants.P_FILE.getProperties()
				.getProperty("service.confidential.pwd"));
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click StorageVolume from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();

		Locator blockInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.block.instance.link");
		ActionUtils.findElement(blockInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		// create instance
		logger.info(LoggerType.COMMENT, "Add one Block Volume instance");
		Locator createInstancebutton = LocatorManager.getInstance()
				.getLocator("bmc.block.createinstance.btn");
		ActionUtils.findElement(createInstancebutton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator displayNameblock = LocatorManager.getInstance()
				.getLocator("bmc.block.displayname");
		ActionUtils.type(displayNameblock, Constants.P_FILE.getProperties()
				.getProperty("blockVolumeName"));
		Locator availabilityDomainBlock = LocatorManager.getInstance()
				.getLocator("bmc.block.avilabilitydomain");
		// ActionUtils.select(availabilityDomainBlock, Constants.P_FILE
		// .getProperties().getProperty("availabilitydomain"));
		ActionUtils.select(availabilityDomainBlock, ADname);
		Locator sizeBlock = LocatorManager.getInstance()
				.getLocator("bmc.block.size");
		ActionUtils.type(sizeBlock, Constants.P_FILE.getProperties()
				.getProperty("blockVolumeSize"));
		Locator addOKButton = LocatorManager.getInstance()
				.getLocator("bmc.block.addOk.btn");
		ActionUtils.findElement(addOKButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		status = LocatorManager.getInstance().getLocator("bmc.block.status");

		statusElement = ActionUtils.findElement(status);
		checkInstanceStatus(statusElement, "AVAILABLE");

	}

	public void BM_Terminate_Block_Instance()
			throws InterruptedException, IOException {

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click StorageVolume from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();

		// find the specified instance name
		Locator blockInstance = new Locator("BLOCKVOLUME_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("blockVolumeName")));

		if (ActionUtils.isElementPresent(blockInstance)) {
			ActionUtils.findElement(blockInstance).click();
			// terminate instance
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.block.terminate.button");
			Locator confirmTerminate = LocatorManager.getInstance()
					.getLocator("bmc.block.terminate.confirm");
			ActionUtils.findElement(terminateButton).click();
			ActionUtils.findElement(confirmTerminate).click();

			status = LocatorManager.getInstance()
					.getLocator("bmc.block.status");
			statusElement = ActionUtils.findElement(status);
			logger.info(LoggerType.COMMENT,
					"Status is " + statusElement.getText());
			checkInstanceStatus(statusElement, "TERMINATED");
		}

	}

	public void BM_Create_ObjectStorage()
			throws InterruptedException, IOException {
		ActionUtils.gotoUrl(
				Constants.P_FILE.getProperties().getProperty("bmconsole.url"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		// Login
		Locator usernameInput = LocatorManager.getInstance()
				.getLocator("bmc.login.username");
		Locator passwordInput = LocatorManager.getInstance()
				.getLocator("bmc.login.password");
		Locator signInButton = LocatorManager.getInstance()
				.getLocator("bmc.login.signin");
		ActionUtils.type(usernameInput, Constants.P_FILE.getProperties()
				.getProperty("opc.bm.user.name"));
		ActionUtils.type(passwordInput, Constants.P_FILE.getProperties()
				.getProperty("service.confidential.pwd"));
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		// Go to menu
		logger.info(LoggerType.COMMENT, "Going to click Storage from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		logger.info(LoggerType.COMMENT,
				"Going to click Object Storage in the left");
		Locator objectInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.object.instance.link");
		ActionUtils.findElement(objectInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		// create instance
		logger.info(LoggerType.COMMENT, "Add one object storage bucket");
		Locator createInstancebutton = LocatorManager.getInstance()
				.getLocator("bmc.object.createinstance.btn");
		ActionUtils.findElement(createInstancebutton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		Locator displayNameobject = LocatorManager.getInstance()
				.getLocator("bmc.object.displayname");
		ActionUtils.type(displayNameobject, Constants.P_FILE.getProperties()
				.getProperty("objectBucketName"));

		System.out.println("the Tier is " + Constants.P_FILE.getProperties()
				.getProperty("objectStorageTier"));
		if (Constants.P_FILE.getProperties()
				.getProperty("objectStorageTier") == "ARCHIVE") {
			Locator storageTier = LocatorManager.getInstance()
					.getLocator("bmc.object.archive");
			ActionUtils.findElement(storageTier).click();
		}
		Locator addOKButton = LocatorManager.getInstance()
				.getLocator("bmc.object.addOk.btn");
		ActionUtils.findElement(addOKButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		Locator objectStorageInstance = new Locator("ObjectStorage_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("objectBucketName")));
		System.out.println("Instance name is "
				+ ActionUtils.getText(objectStorageInstance));
		System.out.println("Instance name is " + Constants.P_FILE
				.getProperties().getProperty("objectBucketName"));
		System.out.println("Instance name existing "
				+ ActionUtils.isElementPresent(objectStorageInstance));
		if (ActionUtils.isElementPresent(objectStorageInstance)) {
			logger.info(LoggerType.COMMENT,
					"Object Bucket created successfully");
		} else {
			logger.info(LoggerType.COMMENT, "Object Bucket creation failed");
		}
	}

	public void terminateObjectStorageInstance() {

		// Go to menu
		logger.info(LoggerType.COMMENT, "Going to click Storage from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();
		Locator objectInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.object.instance.link");
		ActionUtils.findElement(objectInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		Locator objectStorageInstance = new Locator("ObjectStorage_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("objectBucketName")));

		if (ActionUtils.isElementPresent(objectStorageInstance)) {
			ActionUtils.findElement(objectStorageInstance).click();
			// terminate instance
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.object.terminate.btn");
			ActionUtils.findElement(terminateButton).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);
			Locator confirmTerminate = LocatorManager.getInstance()
					.getLocator("bmc.object.terminate.confirm");
			ActionUtils.findElement(confirmTerminate).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 15);

			if (ActionUtils.isElementPresent(objectStorageInstance)) {
				logger.info(LoggerType.COMMENT,
						"Object Bucket termination failed");
			} else {
				logger.info(LoggerType.COMMENT,
						"Object Bucket terminate successfully");
			}

		}

	}

	public void BM_Upload_Object() throws InterruptedException, IOException {

		// Go to menu
		logger.info(LoggerType.COMMENT, "Going to click Storage from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();
		Locator objectInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.object.instance.link");
		ActionUtils.findElement(objectInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		// create instance
		Locator objectStorageInstance = new Locator("ObjectStorage_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("objectBucketName")));
		if (ActionUtils.isElementPresent(objectStorageInstance)) {
			// upload object
			ActionUtils.findElement(objectStorageInstance).click();

			Locator uploadButton = LocatorManager.getInstance()
					.getLocator("bmc.object.upload.button");
			ActionUtils.findElement(uploadButton).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				Locator uploadBrowse = LocatorManager.getInstance()
						.getLocator("bmc.object.upload.browse");

				ActionUtils.findElement(uploadBrowse).click();
				System.out.println(
						Constants.P_FILE.getProperties().getProperty("autoIt"));
				/// upload file with upload.exe
				Runtime.getRuntime()
						.exec(Constants.P_FILE.getProperties()
								.getProperty("autoIt") + " " + "firefox" + " "
								+ Constants.P_FILE.getProperties()
										.getProperty("filePath"));
			} else {
				driver.findElement(By.id("f-drop")).sendKeys(Constants.P_FILE
						.getProperties().getProperty("filePath"));
			}
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);
			Locator uploadReady = LocatorManager.getInstance()
					.getLocator("bmc.object.upload.ready");
			ActionUtils.findElement(uploadReady).click();

			ActionUtils.waitFor(TimeUnit.SECONDS, 6);

			// check upload status
			Locator checkFileName = new Locator("uploadFileName",
					getNameLocator(Constants.P_FILE.getProperties()
							.getProperty("fileName")));
			if (ActionUtils.isElementPresent(checkFileName)) {
				logger.info(LoggerType.COMMENT, "Upload file successfully!");

			} else {
				logger.info(LoggerType.COMMENT, "Upload file fail!");
			}

		} else {
			logger.info(LoggerType.COMMENT, "Object Bucket creation failed");
		}
	}

	public void BM_Download_Object() throws InterruptedException, IOException {

		// Go to menu

		logger.info(LoggerType.COMMENT, "Going to click Storage from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();

		Locator objectInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.object.instance.link");
		ActionUtils.findElement(objectInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		// saw instance
		Locator objectStorageInstance = new Locator("ObjectStorage_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("objectBucketName")));
		if (ActionUtils.isElementPresent(objectStorageInstance)) {

			ActionUtils.findElement(objectStorageInstance).click();
			// download object

			Locator actionButton = new Locator("ActionButton", getNameLocator(
					Constants.P_FILE.getProperties().getProperty("fileName"))
					+ "/../../div[contains(@class,'action-menu-cell')]");

			Locator DOWNLOAD_BUTTON = new Locator("DOWNLOAD_BUTTON",
					getNameLocator(Constants.P_FILE.getProperties()
							.getProperty("fileName"))
							+ "/../../div[contains(@class,'action-menu-cell')]"
							+ "/div/button[2]");
			ActionUtils.findElement(actionButton).click();
			ActionUtils.findElement(DOWNLOAD_BUTTON).click();

			ActionUtils.waitFor(TimeUnit.SECONDS, 20);

			// check download file exists or not
			File file = new File(Constants.P_FILE.getProperties()
					.getProperty("downloadpath"));
			if (file.exists()) {
				logger.info(LoggerType.COMMENT, "Download file successfully");
			} else {
				logger.info(LoggerType.COMMENT, "Download file fail");
			}

		} else {
			logger.info(LoggerType.COMMENT, "Object Bucket don't exist");
		}
	}

	public void BM_Delete_Object() throws InterruptedException, IOException {

		// Go to menu
		logger.info(LoggerType.COMMENT, "Going to click Storage from the menu");
		Locator storageTab = LocatorManager.getInstance()
				.getLocator("bmc.block.tab.menu");
		ActionUtils.findElement(storageTab).click();
		Locator objectInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.object.instance.link");
		ActionUtils.findElement(objectInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		selectCompartment();

		// saw instance
		Locator objectStorageInstance = new Locator("ObjectStorage_INSTANCE",
				getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("objectBucketName")));
		if (ActionUtils.isElementPresent(objectStorageInstance)) {

			ActionUtils.findElement(objectStorageInstance).click();

			Locator actionButton = new Locator("ActionButton", getNameLocator(
					Constants.P_FILE.getProperties().getProperty("fileName"))
					+ "/../../div[contains(@class,'action-menu-cell')]");

			Locator DELETE_BUTTON = new Locator("DOWNLOAD_BUTTON",
					getNameLocator(Constants.P_FILE.getProperties()
							.getProperty("fileName"))
							+ "/../../div[contains(@class,'action-menu-cell')]"
							+ "/div/button[4]");
			Locator DELETE_READY = LocatorManager.getInstance()
					.getLocator("bmc.object.delete.ready");
			ActionUtils.findElement(actionButton).click();
			ActionUtils.findElement(DELETE_BUTTON).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 2);

			ActionUtils.findElement(DELETE_READY).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);

			// check file
			Locator objectName = new Locator("OBJECT", getDBInstanceDiv(
					Constants.P_FILE.getProperties().getProperty("fileName")));

			if (ActionUtils.isElementPresent(objectName)) {
				logger.info(LoggerType.COMMENT, "Object Deletion failed");
			} else {
				logger.info(LoggerType.COMMENT, "Object Delete successfully");
			}

		} else {
			logger.info(LoggerType.COMMENT, "Object Bucket don't exist");
		}
	}

	private String getNameLocator(String Name) {
		StringBuilder sb = new StringBuilder();
		sb.append("//*/div[contains(.,'" + Name + "')] ");
		return sb.toString();
	}

	private void selectCompartment() {
		logger.info(LoggerType.COMMENT, "Select root compartment");
		Locator compartmentBox = LocatorManager.getInstance()
				.getLocator("bmc.compartment.box");
		ActionUtils.findElement(compartmentBox).click();
		Locator compartmentSelect = LocatorManager.getInstance()
				.getLocator("bmc.compartment.select");
		ActionUtils.findElement(compartmentSelect).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
	}

	private String getDBInstanceDiv(String dbinstanceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("//a[contains(.,'" + dbinstanceName + "')] ");
		return sb.toString();
	}

	public static String getPrivateKeyPath() {
		String pKeyPath = Constants.P_FILE.getProperties()
				.getProperty("privatekey.path");
		return (pKeyPath == null
				|| Constants.NULL_STRING_VALUE.equals(pKeyPath))
						? "~/.ssh/dbtestkey"
						: pKeyPath;
	}

	private void checkInstanceStatus(WebElement el, String expected) {
		logger.info(LoggerType.CHECKPOINT,
				"Check the instance status is " + expected.toString());

		long start = System.currentTimeMillis();
		boolean isRightStatus = false;
		try {
			isRightStatus = new WebDriverWait(
					WebDriverManager.getCurrentDriver(),
					TimeUnit.MINUTES.toSeconds(
							TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS),
					TimeUnit.SECONDS.toMillis(Constants.DEFAULT_SHORT_TIMEOUT))
							.until(new ExpectedCondition<Boolean>() {
								String actualStatus = "";

								@Override
								public Boolean apply(WebDriver input) {
									actualStatus = el.getText();
									if (actualStatus.contains(expected)) {
										return true;
									} else {
										logger.info(LoggerType.COMMENT,
												"The actual instance status is "
														+ actualStatus);
										return false;
									}
								}
							});
		} catch (TimeoutException timeoutExpt) {
			logger.error(LoggerType.EXCEPTION, "The instance status is NOT "
					+ expected.toString() + " within "
					+ TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS + " mins");
			ActionUtils.captureScreenshot();
			Assert.assertTrue(false);
		}

		if (isRightStatus) {
			logger.info(LoggerType.STEP, "[End] Check instance status in ["
					+ expected + "] and took " + TimeUnit.MILLISECONDS
							.toSeconds(System.currentTimeMillis() - start)
					+ " seconds");
		}
	}

}