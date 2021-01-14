package com.oracle.opc.automation.test.testcase.baremetal.action;

import java.util.concurrent.TimeUnit;

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

public class BMCreateTerminateNetworkingAction extends AbstractAutomationAction
		implements AutomationAction {

	Locator status;
	WebElement statusElement;
	BMConsoleAction consoleHelper;
	private static final long TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS = 10L;

	public BMCreateTerminateNetworkingAction(CloudService cloudService) {
		super(cloudService);
		consoleHelper = new BMConsoleAction();
	}

	public BMCreateTerminateNetworkingAction() {
		super();
		consoleHelper = new BMConsoleAction();
	}
	
	public void createVCNInstance() {
		// Go to Login page
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
		logger.info(LoggerType.COMMENT, "Going to click Network from the menu");
		Locator networkInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.network.tab.menu");
		ActionUtils.findElement(networkInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show VCN instances
		logger.info(LoggerType.COMMENT, "Going to click Network Systems");
		Locator networkVCNLink = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn");
		ActionUtils.findElement(networkVCNLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		
		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		// Create VCN Instance
		logger.info(LoggerType.COMMENT, "Create VCN Instance");
		Locator vcnCreateButton = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn.createInstanceBtn");
		ActionUtils.findElement(vcnCreateButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Set VCN default values
		Locator displayNameVCN = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn.nameInput");
		ActionUtils.type(displayNameVCN,
				Constants.P_FILE.getProperties().getProperty("displayname"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator defaultValuesRadio = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn.allResourcesRadio");
		ActionUtils.findElement(defaultValuesRadio).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator createVCNbtn = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn.createVNC");
		ActionUtils.findElement(createVCNbtn).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
	}

	public void terminateVCNInstance() {
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
		logger.info(LoggerType.COMMENT, "Going to click Network from the menu");
		Locator vcnInstanceTab = LocatorManager.getInstance()
				.getLocator("bmc.network.tab.menu");
		ActionUtils.findElement(vcnInstanceTab).click();

		// Show instances
		Locator vcnInstancesLink = LocatorManager.getInstance()
				.getLocator("bmc.network.vcn.link");
		ActionUtils.findElement(vcnInstancesLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();
		
		Locator vcnInstance = new Locator("VCN_INSTANCE", getInstanceDiv(
				Constants.P_FILE.getProperties().getProperty("displayname")));

		if (ActionUtils.isElementPresent(vcnInstance)) {
			ActionUtils.findElement(vcnInstance).click();
			// terminate instance
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.network.terminate.button");
			Locator confirmTerminate = LocatorManager.getInstance()
					.getLocator("bmc.network.terminate.confirm");
			ActionUtils.findElement(terminateButton).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);
			ActionUtils.findElement(confirmTerminate).click();
			ActionUtils.waitFor(TimeUnit.MINUTES, 3);
		}
	}

	public void createLBInstance() {
		// Go to Login page
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
		logger.info(LoggerType.COMMENT, "Going to click Network from the menu");
		Locator networkInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.network.tab.menu");
		ActionUtils.findElement(networkInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show LB instances
		logger.info(LoggerType.COMMENT, "Going to click Load Balancers");
		Locator lbLink = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.link");
		ActionUtils.findElement(lbLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		
		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		// Create Load Balancer Instance
		logger.info(LoggerType.COMMENT, "Create LoadBalancer Instance");
		Locator vcnCreateButton = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.createInstanceBtn");
		ActionUtils.findElement(vcnCreateButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Set default values
		Locator displayNameLB = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.nameInput");
		ActionUtils.type(displayNameLB,
				Constants.P_FILE.getProperties().getProperty("displayname"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator shape = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.shape");
		ActionUtils.select(shape,Constants.P_FILE.getProperties().getProperty("shape"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		
		Locator vcn = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.vcn");
		ActionUtils.select(vcn,Constants.P_FILE.getProperties().getProperty("vcn"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		
		Locator subnet1 = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.subnet1");
		ActionUtils.selectContains(subnet1,Constants.P_FILE.getProperties().getProperty("subnet1"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);
		
		Locator subnet2 = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.subnet2");
		ActionUtils.selectContains(subnet2,Constants.P_FILE.getProperties().getProperty("subnet2"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator createLBButton = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.create");
		ActionUtils.findElement(createLBButton).click();
		ActionUtils.waitFor(TimeUnit.MINUTES, 2);

		Locator lbInstance = new Locator("LB_INSTANCE", getInstanceDiv(
				Constants.P_FILE.getProperties().getProperty("displayname")));
		
		status = LocatorManager.getInstance().getLocator("bmc.network.lb.status");
		statusElement = ActionUtils.findElement(status);
		consoleHelper.checkInstanceStatus(statusElement, "ACTIVE");
		

		/*if (ActionUtils.isElementPresent(lbInstance)) {
			logger.info(LoggerType.COMMENT, "LB Instance successfully launched");
		} else {
			logger.info(LoggerType.COMMENT, "Failed to create LB Instance");
		}
		
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		ActionUtils.findElement(lbInstance).click();
		status = LocatorManager.getInstance().getLocator("bmc.network.lb.status");
		statusElement = ActionUtils.findElement(status);
		checkInstanceStatus(statusElement, "ACTIVE");*/

	}

	public void terminateLBInstance() {
		// Go to Login page
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
		logger.info(LoggerType.COMMENT, "Going to click Network from the menu");
		Locator networkInstanceDashboardTab = LocatorManager.getInstance()
				.getLocator("bmc.network.tab.menu");
		ActionUtils.findElement(networkInstanceDashboardTab).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		// Show LB instances
		logger.info(LoggerType.COMMENT, "Going to click Load Balancers");
		Locator lbLink = LocatorManager.getInstance()
				.getLocator("bmc.network.lb.link");
		ActionUtils.findElement(lbLink).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		Locator dbCompartmentLink = LocatorManager.getInstance()
				.getLocator("bmc.compartment");
		ActionUtils.findElement(dbCompartmentLink).click();
		Locator dbCompValue = LocatorManager.getInstance()
				.getLocator("bmc.comp.value");
		ActionUtils.findElement(dbCompValue).click();

		Locator lbInstance = new Locator("LB_INSTANCE", getInstanceDiv(
				Constants.P_FILE.getProperties().getProperty("displayname")));

		if (ActionUtils.isElementPresent(lbInstance)) {
			ActionUtils.findElement(lbInstance).click();
			// terminate instance
			logger.info(LoggerType.COMMENT, "Going to terminate Load Balancer");
			Locator terminateButton = LocatorManager.getInstance()
					.getLocator("bmc.network.lb.terminate.button");
			ActionUtils.findElement(terminateButton).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 5);
			Locator confirmTerminate = LocatorManager.getInstance()
					.getLocator("bmc.network.lb.terminate.confirm");
			ActionUtils.findElement(confirmTerminate).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 15);
			
		}
	}

	private String getInstanceDiv(String instanceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("//a[contains(.,'" + instanceName + "')] ");
		return sb.toString();
	}

	public static String getPrivateKeyPath() {
		String pKeyPath = Constants.P_FILE.getProperties()
				.getProperty("privatekey.path");
		return (pKeyPath == null
				|| Constants.NULL_STRING_VALUE.equals(pKeyPath))
						? "~/.ssh/dbtestkey" : pKeyPath;
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
