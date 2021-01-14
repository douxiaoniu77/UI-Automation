package com.oracle.opc.automation.test.testcase.baremetal.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.jcraft.jsch.Session;
import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.WebDriverManager;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.common.utils.JSCHUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.component.actions.AbstractAutomationAction;
import com.oracle.opc.automation.test.component.actions.AutomationAction;

public class BMConsoleAction extends AbstractAutomationAction
		implements AutomationAction {

	private static final long TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS = 120L;
	private static final long SLEEPING_TIME_IN_SECS = 60L;

	public BMConsoleAction(CloudService cloudService) {
		super(cloudService);
	}

	public BMConsoleAction() {
		super();
	}

	public void login() {
		logger.info(LoggerType.COMMENT, "[START] Login to Console");

		logger.info(LoggerType.COMMENT, "[step] Go to console URL");
		ActionUtils.gotoUrl(
				Constants.P_FILE.getProperties().getProperty("bmconsole.url"));
		ActionUtils.waitFor(TimeUnit.SECONDS, 5);

		logger.info(LoggerType.COMMENT, "[step] Type the password");
		Locator usernameInput = LocatorManager.getInstance()
				.getLocator("bmc.login.username");
		ActionUtils.type(usernameInput, Constants.P_FILE.getProperties()
				.getProperty("opc.bm.user.name"));

		logger.info(LoggerType.COMMENT, "[step] Type the password");
		Locator passwordInput = LocatorManager.getInstance()
				.getLocator("bmc.login.password");
		ActionUtils.type(passwordInput, Constants.P_FILE.getProperties()
				.getProperty("service.confidential.pwd"));

		logger.info(LoggerType.COMMENT, "[step] Click in sign in button");
		Locator signInButton = LocatorManager.getInstance()
				.getLocator("bmc.login.signin");
		ActionUtils.findElement(signInButton).click();

		ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		logger.info(LoggerType.COMMENT, "[END] Login to Console");
	}

	public void ping_instance(String ipAdress) throws IOException {
		logger.info(LoggerType.COMMENT, "[START] Login to instance");
		Session session = JSCHUtils.getJSCHSessionWithPrivateKey("opc",
				ipAdress, getPrivateKeyPath());
		System.out.println(JSCHUtils.execJSCH(session, "ls -al"));
		JSCHUtils.closeSession(session);
		logger.info(LoggerType.COMMENT, "[END] Login to instance");
	}

	private static String getPrivateKeyPath() {
		String pKeyPath = Constants.P_FILE.getProperties()
				.getProperty("privatekey.path");
		return (pKeyPath == null
				|| Constants.NULL_STRING_VALUE.equals(pKeyPath))
						? "~/.ssh/dbtestkey" : pKeyPath;
	}

	public String getDBInstanceDiv(String instanceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("//a[contains(.,'" + instanceName + "')] ");
		return sb.toString();
	}

	public void checkInstanceStatus(WebElement el, String expected) {
		logger.info(LoggerType.CHECKPOINT,
				"Check the instance status is " + expected.toString());

		long start = System.currentTimeMillis();
		boolean isRightStatus = false;
		try {
			isRightStatus = new WebDriverWait(
					WebDriverManager.getCurrentDriver(),
					TimeUnit.MINUTES.toSeconds(
							TOTAL_CHECKING_INSTANCESTATUS_TIME_IN_MINS),
					TimeUnit.SECONDS.toMillis(SLEEPING_TIME_IN_SECS))
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
