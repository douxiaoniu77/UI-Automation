package com.oracle.opc.automation.test.testcase.baremetal.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.actions.AbstractAutomationAction;
import com.oracle.opc.automation.test.component.actions.AutomationAction;

public class BMCreateTerminateComputeAction extends AbstractAutomationAction
		implements AutomationAction {

	Locator status;
	WebElement statusElement;
	BMConsoleAction consoleHelper;
	private static final long TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS = 10L;

	public BMCreateTerminateComputeAction(CloudService cloudService) {
		super(cloudService);
		consoleHelper = new BMConsoleAction(cloudService);
	}

	public BMCreateTerminateComputeAction() {
		super();
		consoleHelper = new BMConsoleAction();
	}

	public void createComputeInstance(String shape) throws IOException {

		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT, "Going to click Compute from the menu");
		Locator computeInstanceTab = LocatorManager.getInstance()
				.getLocator("bmc.compute.tab.menu");
		ActionUtils.findElement(computeInstanceTab).click();

		// Show instances
		Locator computeInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.compute.instances.link");
		ActionUtils.findElement(computeInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		// create instance
		Locator createInstanceButton = LocatorManager.getInstance()
				.getLocator("bmc.compute.createinstance.btn");
		ActionUtils.findElement(createInstanceButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.COMMENT, "Tupe Display Name");
		Locator displayNameCompute = LocatorManager.getInstance()
				.getLocator("bmc.compute.displayname");
		ActionUtils.type(displayNameCompute,
				Constants.P_FILE.getProperties().getProperty("displayname"));

		logger.info(LoggerType.COMMENT, "Select AD");
		Locator availabilityDomainCompute = LocatorManager.getInstance()
				.getLocator("bmc.compute.availabilitydomain");
		ActionUtils.selectContains(availabilityDomainCompute, Constants.P_FILE
				.getProperties().getProperty("availabilitydomain"));

		logger.info(LoggerType.COMMENT, "Select OS");
		Locator computeImage = LocatorManager.getInstance()
				.getLocator("bmc.compute.image.os");
		ActionUtils.select(computeImage,
				Constants.P_FILE.getProperties().getProperty("image.os"));

		String shape_type;
		if (shape.startsWith("VM")) {
			logger.info(LoggerType.COMMENT, "Going to select VM");
			shape_type = "bmc.compute.vm";
		} else {
			logger.info(LoggerType.COMMENT, "Going to select BM");
			shape_type = "bmc.compute.physical";
		}
		Locator machineTypeL = LocatorManager.getInstance()
				.getLocator(shape_type);
		ActionUtils.click(machineTypeL);

		logger.info(LoggerType.COMMENT, "Select Shape '" + shape + "'");
		Locator shapeCompute = LocatorManager.getInstance()
				.getLocator("bmc.compute.shape");
		ActionUtils.selectContains(shapeCompute, shape);

		logger.info(LoggerType.COMMENT, "Going to select VCN");
		Locator vcnL = LocatorManager.getInstance()
				.getLocator("bmc.compute.vcn");
		ActionUtils.select(vcnL,
				Constants.P_FILE.getProperties().getProperty("vcn"));

		Locator clientSubnetL = LocatorManager.getInstance()
				.getLocator("bmc.compute.subnet");
		ActionUtils.selectContains(clientSubnetL, Constants.P_FILE
				.getProperties().getProperty("availabilitydomain"));

		Locator sshKeyRadioButton = LocatorManager.getInstance()
				.getLocator("bmc.compute.sshkey");
		ActionUtils.click(sshKeyRadioButton);

		Locator textArea = LocatorManager.getInstance()
				.getLocator("bmc.compute.sshtextarea");
		ActionUtils.type(textArea, Constants.P_FILE.getProperties()
				.getProperty("baremetal.publickey"));

		Locator launchInstanceButton = LocatorManager.getInstance()
				.getLocator("bmc.compute.launchinstance");
		ActionUtils.click(launchInstanceButton);

		ActionUtils.waitFor(TimeUnit.MINUTES, 1);

		status = LocatorManager.getInstance().getLocator("bmc.compute.status");

		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		statusElement = ActionUtils.findElement(status);
		consoleHelper.checkInstanceStatus(statusElement, "RUNNING");
		ActionUtils.waitFor(TimeUnit.MINUTES, 2);

		logger.info(LoggerType.COMMENT, "[step] Get IP address");
		Locator computeIP = LocatorManager.getInstance()
				.getLocator("bmc.compute.ipaddress");
		WebElement hostIP = ActionUtils.findElement(computeIP);
		String ipAdress = StringUtils.split(hostIP.getText(), ":")[1].trim();
		logger.info(LoggerType.COMMENT, "Actual IP address is " + ipAdress);

		logger.info(LoggerType.COMMENT, "[step] Ping test");
		consoleHelper.ping_instance(ipAdress);
	}

	public void terminateComputeInstance() {

		// Login
		consoleHelper.login();

		// Go to menu
		logger.info(LoggerType.COMMENT,
				"[step] Going to click Compute from the menu");
		Locator computeInstanceTab = LocatorManager.getInstance()
				.getLocator("bmc.compute.tab.menu");
		ActionUtils.findElement(computeInstanceTab).click();

		// Show instances
		logger.info(LoggerType.COMMENT, "[step] Get instnace Link");
		Locator computeInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.compute.instances.link");
		ActionUtils.findElement(computeInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.COMMENT, "[step] Select compartment");
		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		logger.info(LoggerType.COMMENT, "[step] Get display name");
		Locator computeInstance = new Locator("COMPUTE_INSTANCE",
				consoleHelper.getDBInstanceDiv(Constants.P_FILE.getProperties()
						.getProperty("displayname")));

		if (ActionUtils.isElementPresent(computeInstance)) {
			ActionUtils.findElement(computeInstance).click();
			// terminate instance
			logger.info(LoggerType.COMMENT,
					"[step] Click terminate instance button");
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.compute.terminate.button");
			logger.info(LoggerType.COMMENT, "[step] Click confirmation button");
			Locator confirmTerminate = LocatorManager.getInstance()
					.getLocator("bmc.compute.terminate.confirm");
			ActionUtils.findElement(terminateButton).click();
			ActionUtils.findElement(confirmTerminate).click();
			logger.info(LoggerType.COMMENT, "[step] Check instance status");
			status = LocatorManager.getInstance()
					.getLocator("bmc.compute.status");
			statusElement = ActionUtils.findElement(status);
			logger.info(LoggerType.COMMENT,
					"[step] Status is " + statusElement.getText());
			consoleHelper.checkInstanceStatus(statusElement, "TERMINATED");
		}
	}

}
