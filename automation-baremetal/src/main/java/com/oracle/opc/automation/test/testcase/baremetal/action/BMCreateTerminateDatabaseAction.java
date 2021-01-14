package com.oracle.opc.automation.test.testcase.baremetal.action;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.actions.AbstractAutomationAction;
import com.oracle.opc.automation.test.component.actions.AutomationAction;
import com.oracle.opc.automation.test.entity.enums.Shapes;

public class BMCreateTerminateDatabaseAction extends AbstractAutomationAction
		implements AutomationAction {

	Locator status;
	WebElement statusElement;
	BMConsoleAction consoleHelper;

	public BMCreateTerminateDatabaseAction(CloudService cloudService) {
		super(cloudService);
		consoleHelper = new BMConsoleAction(cloudService);
	}

	public BMCreateTerminateDatabaseAction() {
		super();
		consoleHelper = new BMConsoleAction();
	}

	public void createDatabaseInstance(String shape, boolean byol) {

		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click Database from the menu");
		Locator dbInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.tab.menu.database");
		ActionUtils.findElement(dbInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show instances
		logger.info(LoggerType.COMMENT, "Going to click DB Systems");
		Locator dbSystemLink = LocatorManager.getInstance()
				.getLocator("bmc.database.system");
		ActionUtils.findElement(dbSystemLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		// Create Instance
		logger.info(LoggerType.COMMENT, "Going to launch DB System");
		Locator dbCreateButton = LocatorManager.getInstance()
				.getLocator("bmc.database.createInstanceBtn");
		ActionUtils.findElement(dbCreateButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator displayNameL = LocatorManager.getInstance()
				.getLocator("bmc.database.displayname");
		ActionUtils.type(displayNameL,
				Constants.P_FILE.getProperties().getProperty("displayname"));
		Locator availabilityDomainL = LocatorManager.getInstance()
				.getLocator("bmc.database.availabilitydomain");

		ActionUtils.selectContains(availabilityDomainL, Constants.P_FILE
				.getProperties().getProperty("availabilitydomain"));

		String shape_type;
		if (shape.startsWith("VM")) {
			logger.info(LoggerType.COMMENT, "Going to select VM");
			shape_type = "bmc.database.vm";
		} else {
			logger.info(LoggerType.COMMENT, "Going to select BM");
			shape_type = "bmc.database.physical";
		}
		Locator machineTypeL = LocatorManager.getInstance()
				.getLocator(shape_type);
		ActionUtils.click(machineTypeL);

		Locator shapeL = LocatorManager.getInstance()
				.getLocator("bmc.database.shape");
		ActionUtils.selectContains(shapeL, shape);

		if (shape.startsWith("VM")) {
			Locator nodeCountL = LocatorManager.getInstance()
					.getLocator("bmc.database.nodecount");
			ActionUtils.select(nodeCountL, Constants.P_FILE.getProperties()
					.getProperty("bmc.database.nodecount"));
			Locator storageL = LocatorManager.getInstance()
					.getLocator("bmc.database.storage");
			ActionUtils.select(storageL, Constants.P_FILE.getProperties()
					.getProperty("bmc.database.storage"));
		}

		Locator softwareEditionL = LocatorManager.getInstance()
				.getLocator("bmc.database.softwareedition");
		ActionUtils.select(softwareEditionL, Constants.P_FILE.getProperties()
				.getProperty("softwareedition"));

		if (byol) {
			Locator byolL = LocatorManager.getInstance()
					.getLocator("bmc.database.byol");
			ActionUtils.click(byolL);
		}
		Locator sshKeyRadioButton = LocatorManager.getInstance()
				.getLocator("bmc.database.sshkey");
		ActionUtils.click(sshKeyRadioButton);

		Locator textArea = LocatorManager.getInstance()
				.getLocator("bmc.database.sshtextarea");
		ActionUtils.type(textArea, Constants.P_FILE.getProperties()
				.getProperty("baremetal.publickey"));

		logger.info(LoggerType.COMMENT, "Going to select vcn1");
		Locator vcnL = LocatorManager.getInstance()
				.getLocator("bmc.database.vcn");
		ActionUtils.select(vcnL,
				Constants.P_FILE.getProperties().getProperty("vcn"));

		Locator clientSubnetL = LocatorManager.getInstance()
				.getLocator("bmc.database.clientsubnet");
		ActionUtils.selectContains(clientSubnetL, Constants.P_FILE
				.getProperties().getProperty("availabilitydomain"));

		if (Shapes.Quarter.getShapeName().equals(shape)
				|| Shapes.Half.getShapeName().equals(shape)
				|| Shapes.Full.getShapeName().equals(shape)) {
			Locator backupSubnetL = LocatorManager.getInstance()
					.getLocator("bmc.database.backupsubnet");
			ActionUtils.selectContains(backupSubnetL, Constants.P_FILE
					.getProperties().getProperty("backupsubnet"));
		}

		Locator hostnamePrefixL = LocatorManager.getInstance()
				.getLocator("bmc.database.hostnameprefix");
		ActionUtils.type(hostnamePrefixL,
				Constants.P_FILE.getProperties().getProperty("hostnameprefix"));

		Locator databaseNameL = LocatorManager.getInstance()
				.getLocator("bmc.database.dbname");
		ActionUtils.type(databaseNameL,
				Constants.P_FILE.getProperties().getProperty("dbname"));

		Locator dbVersionL = LocatorManager.getInstance()
				.getLocator("bmc.database.dbversion");
		ActionUtils.select(dbVersionL,
				Constants.P_FILE.getProperties().getProperty("dbversion"));

		Locator adminPasswordL = LocatorManager.getInstance()
				.getLocator("bmc.database.adminpassword");
		ActionUtils.type(adminPasswordL,
				Constants.P_FILE.getProperties().getProperty("adminpassword"));

		Locator confirmPasswordL = LocatorManager.getInstance()
				.getLocator("bmc.database.confirmpassword");
		ActionUtils.type(confirmPasswordL,
				Constants.P_FILE.getProperties().getProperty("adminpassword"));

		if (Constants.P_FILE.getProperties().getProperty("production")
				.equals("false")) {
			Locator serialNumberL = LocatorManager.getInstance()
					.getLocator("bmc.database.serialnumber");
			ActionUtils.type(serialNumberL, Constants.P_FILE.getProperties()
					.getProperty("serialnumber"));
		}

		Locator launchDBButton = LocatorManager.getInstance()
				.getLocator("bmc.database.launchdbbutton");
		ActionUtils.click(launchDBButton);

		ActionUtils.waitFor(TimeUnit.SECONDS, 60);

		logger.info(LoggerType.COMMENT, "[step] Get display name");
		Locator instance = new Locator("COMPUTE_INSTANCE",
				consoleHelper.getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("displayname")));

		if (ActionUtils.isElementPresent(instance)) {
			ActionUtils.findElement(instance).click();

			logger.info(LoggerType.COMMENT, "[step] Check instance status");
			status = LocatorManager.getInstance()
					.getLocator("bmc.database.status");
			statusElement = ActionUtils.findElement(status);
			logger.info(LoggerType.COMMENT,
					"[step] Status is " + statusElement.getText());
			consoleHelper.checkInstanceStatus(statusElement, "AVAILABLE");
		}
	}

	public void scaleUpDatabaseInstance() {
		this.scaleUpDatabaseInstance(
				Constants.P_FILE.getProperties().getProperty("displayname"));
	}

	public void scaleUpDatabaseInstance(String displayName) {

		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click Database from the menu");
		Locator dbInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.tab.menu.database");
		ActionUtils.findElement(dbInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show instances
		logger.info(LoggerType.COMMENT, "Going to click DB Systems");
		Locator dbSystemLink = LocatorManager.getInstance()
				.getLocator("bmc.database.system");
		ActionUtils.findElement(dbSystemLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		Locator dbInstance = new Locator("DB_INSTANCE",
				consoleHelper.getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("displayname")));
		ActionUtils.findElement(dbInstance).click();
		Locator cpuCoreCount = LocatorManager.getInstance()
				.getLocator("bmc.database.cpu");
		WebElement cpuCount = ActionUtils.findElement(cpuCoreCount);
		String[] cpufields = StringUtils.split(cpuCount.getText(), ":");
		String cpuCountValue = cpufields[1].trim();
		Locator scaleButton = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.button");
		ActionUtils.click(scaleButton);
		Locator scaleText = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.text");
		Locator shapeLocator = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.shape");
		WebElement shapeElement = ActionUtils.findElement(shapeLocator);
		String shape = StringUtils.split(shapeElement.getText(), ":")[1].trim();
		int count = 0;
		if (Shapes.HighIO.getShapeName().equals(shape)
				|| Shapes.DenseIO.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 36) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the maximum value, cannot scale up from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) + 2;
		} else if (Shapes.RAC.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 72) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the maximum value, cannot scale up from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) + 4;
		} else if (Shapes.Quarter.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 84) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the maximum value, cannot scale up from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) + 2;
		} else if (Shapes.Half.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 168) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the maximum value, cannot scale up from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) + 4;

		} else if (Shapes.Full.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 336) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the maximum value, cannot scale up from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) + 8;
		}

		ActionUtils.type(scaleText, String.valueOf(count));
		Locator submitScale = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.submit");
		ActionUtils.click(submitScale);
		ActionUtils.waitFor(TimeUnit.MINUTES, 2);

		status = LocatorManager.getInstance().getLocator("bmc.database.status");
		statusElement = ActionUtils.findElement(status);
		logger.info(LoggerType.COMMENT, "Status is " + statusElement.getText());
		consoleHelper.checkInstanceStatus(statusElement, "AVAILABLE");

		cpuCount = ActionUtils.findElement(cpuCoreCount);
		logger.info(LoggerType.COMMENT,
				"CPU Core Count is " + cpuCount.getText());
		cpufields = StringUtils.split(cpuCount.getText(), ":");
		logger.info(LoggerType.COMMENT,
				"CPU core count is " + cpufields[1].trim());
		cpuCountValue = cpufields[1].trim();

		if (String.valueOf(count).equals(cpuCountValue)) {
			logger.info(LoggerType.COMMENT,
					"DB Instance was successfully scaled up");
		} else {
			logger.info(LoggerType.COMMENT,
					"DB Instance could not be scaled up");
		}
	}

	public void scaleDownDatabaseInstance() {
		this.scaleDownDatabaseInstance(
				Constants.P_FILE.getProperties().getProperty("displayname"));
	}

	public void scaleDownDatabaseInstance(String displayName) {
		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click Database from the menu");
		Locator dbInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.tab.menu.database");
		ActionUtils.findElement(dbInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show instances
		logger.info(LoggerType.COMMENT, "Going to click DB Systems");
		Locator dbSystemLink = LocatorManager.getInstance()
				.getLocator("bmc.database.system");
		ActionUtils.findElement(dbSystemLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		Locator dbInstance = new Locator("DB_INSTANCE",
				consoleHelper.getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("displayname")));
		ActionUtils.findElement(dbInstance).click();
		Locator cpuCoreCount = LocatorManager.getInstance()
				.getLocator("bmc.database.cpu");
		WebElement cpuCount = ActionUtils.findElement(cpuCoreCount);
		String[] cpufields = StringUtils.split(cpuCount.getText(), ":");
		String cpuCountValue = cpufields[1].trim();
		Locator scaleButton = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.button");
		ActionUtils.click(scaleButton);
		Locator scaleText = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.text");
		Locator shapeLocator = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.shape");
		WebElement shapeElement = ActionUtils.findElement(shapeLocator);
		String shape = StringUtils.split(shapeElement.getText(), ":")[1].trim();
		int count = 0;
		if (Shapes.HighIO.getShapeName().equals(shape)
				|| Shapes.DenseIO.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 2) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the minimum value, cannot scale down from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) - 2;
		} else if (Shapes.RAC.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 4) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the minimum value, cannot scale down from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) - 4;
		} else if (Shapes.Quarter.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 22) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the minimum value, cannot scale down from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) - 2;
		} else if (Shapes.Half.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 44) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the minimum value, cannot scale down from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) - 4;

		} else if (Shapes.Full.getShapeName().equals(shape)) {
			if (Integer.parseInt(cpuCountValue) == 88) {
				logger.error(LoggerType.COMMENT,
						"CPU core count is at the minimum value, cannot scale down from here");
				Assert.assertTrue(false);
			}
			count = Integer.parseInt(cpuCountValue) - 8;
		}

		ActionUtils.type(scaleText, String.valueOf(count));
		Locator submitScale = LocatorManager.getInstance()
				.getLocator("bmc.database.scale.submit");
		ActionUtils.click(submitScale);
		ActionUtils.waitFor(TimeUnit.MINUTES, 2);

		status = LocatorManager.getInstance().getLocator("bmc.database.status");
		statusElement = ActionUtils.findElement(status);
		logger.info(LoggerType.COMMENT, "Status is " + statusElement.getText());
		consoleHelper.checkInstanceStatus(statusElement, "AVAILABLE");

		cpuCount = ActionUtils.findElement(cpuCoreCount);
		logger.info(LoggerType.COMMENT,
				"CPU Core Count is " + cpuCount.getText());
		cpufields = StringUtils.split(cpuCount.getText(), ":");
		logger.info(LoggerType.COMMENT,
				"CPU core count is " + cpufields[1].trim());
		cpuCountValue = cpufields[1].trim();

		if (String.valueOf(count).equals(cpuCountValue)) {
			logger.info(LoggerType.COMMENT,
					"DB Instance was successfully scaled up");
		} else {
			logger.info(LoggerType.COMMENT,
					"DB Instance could not be scaled up");
		}
	}

	public void terminateDatabaseInstance() {

		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"Going to click Database from the menu");
		Locator dbInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.tab.menu.database");
		ActionUtils.findElement(dbInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show instances
		logger.info(LoggerType.COMMENT, "Going to click DB Systems");
		Locator dbSystemLink = LocatorManager.getInstance()
				.getLocator("bmc.database.system");
		ActionUtils.findElement(dbSystemLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.COMMENT, "[step] Select compartment");
		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		String displayName = Constants.P_FILE.getProperties()
				.getProperty("displayname");

		logger.info(LoggerType.COMMENT, "[step] Get display name");
		Locator instance = new Locator("COMPUTE_INSTANCE",
				consoleHelper.getDBInstanceDiv(displayName));

		if (ActionUtils.isElementPresent(instance)) {
			ActionUtils.findElement(instance).click();
			// terminate instance
			logger.info(LoggerType.COMMENT,
					"[step] Click terminate instance button");
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.database.terminate.button");
			ActionUtils.findElement(terminateButton).click();

			logger.info(LoggerType.COMMENT, "[step] Type instance name");
			Locator DB_INSTANCE_TERMINATE_TEXT = LocatorManager.getInstance()
					.getLocator("bmc.database.terminate.text");
			if (ActionUtils.isElementPresentTimeoutInSeconds(
					DB_INSTANCE_TERMINATE_TEXT, 5l)) {
				ActionUtils.type(DB_INSTANCE_TERMINATE_TEXT, displayName);
			}

			logger.info(LoggerType.COMMENT, "[step] Click confirmation button");
			Locator DB_INSTANCE_TERMINATE_BUTTON = LocatorManager.getInstance()
					.getLocator("bmc.database.terminate.confirm.button");
			if (ActionUtils.isElementPresentTimeoutInSeconds(
					DB_INSTANCE_TERMINATE_BUTTON, 5l)) {
				ActionUtils.click(DB_INSTANCE_TERMINATE_BUTTON);
			}

			logger.info(LoggerType.COMMENT, "[step] Check instance status");
			status = LocatorManager.getInstance()
					.getLocator("bmc.database.status");
			statusElement = ActionUtils.findElement(status);
			logger.info(LoggerType.COMMENT,
					"[step] Status is " + statusElement.getText());
			consoleHelper.checkInstanceStatus(statusElement, "TERMINATED");
		}
	}

}