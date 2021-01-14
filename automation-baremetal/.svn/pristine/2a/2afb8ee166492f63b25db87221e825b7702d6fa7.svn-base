/**

 * automation-baremetal
 * BMCSTest.java   for Bare Metal Compute Service Sanity Check 
 * 2016年10月19日
 
 */

package com.oracle.opc.automation.test.testcase.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.locator.LocatorManager;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.Locator;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.testcase.baremetal.BMAccounLifecycle2;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateComputeAction;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateDatabaseAction;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateNetworkingAction;

/**
 * @author xueniu
 *
 */

public class BMIBEPromoTest extends BaseTest {

	private CloudService bm;
	String shapeCompute;
	String shapeDB;
	boolean byol;

	AutomationLogger logger = new AutomationLogger(BMAccounLifecycle2.class);

	public BMIBEPromoTest() {
		System.setProperty("browser", "firefox");

		shapeCompute = Constants.P_FILE.getProperties()
				.getProperty("shape.compute");
		shapeDB = Constants.P_FILE.getProperties()
				.getProperty("shape.database");
		byol = Boolean.parseBoolean(
				Constants.P_FILE.getProperties().getProperty("byol"));

		System.setProperty("proxy.type", "MANUAL");
		System.setProperty("proxy.http", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.ssl", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.noproxy", "*.oracleiaas.com*");

	}

	@Test
	public void changeTemporaryPassword() {
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
				.getProperty("bmc.temp.password"));
		ActionUtils.findElement(signInButton).click();
		ActionUtils.waitFor(TimeUnit.SECONDS, 10);

		logger.info(LoggerType.COMMENT,
				"[Step] Check if password can be reset");
		Locator currentPassword = LocatorManager.getInstance()
				.getLocator("bmc.current.password");
		if (ActionUtils.areElementsPresent(currentPassword)) {
			logger.info(LoggerType.COMMENT, "[Step] Proceed to reset password");
			logger.info(LoggerType.COMMENT, "[Step] Type current password");
			ActionUtils.type(currentPassword, Constants.P_FILE.getProperties()
					.getProperty("bmc.temp.password"));

			logger.info(LoggerType.COMMENT, "[Step] Type new password");
			Locator newPassword = LocatorManager.getInstance()
					.getLocator("bmc.new.password");
			ActionUtils.type(newPassword, Constants.P_FILE.getProperties()
					.getProperty("service.confidential.pwd"));

			logger.info(LoggerType.COMMENT,
					"[Step] Type confirmation password");
			Locator confirmPassword = LocatorManager.getInstance()
					.getLocator("bmc.confirm.password");
			ActionUtils.type(confirmPassword, Constants.P_FILE.getProperties()
					.getProperty("service.confidential.pwd"));

			logger.info(LoggerType.COMMENT, "[Step] Save password");
			Locator savePassword = LocatorManager.getInstance()
					.getLocator("bmc.save.password");
			ActionUtils.findElement(savePassword).click();
			ActionUtils.waitFor(TimeUnit.SECONDS, 10);
		} else {
			logger.info(LoggerType.COMMENT, "[Step] No need to reset password");
		}

	}

	@Test
	public void BM_Create_VCN_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.createVCNInstance();
	}

	@Test
	public void BM_Create_Compute_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateComputeAction.class)
				.createComputeInstance(shapeCompute);

	}

	@Test
	public void BM_Create_Database_Instance()
			throws InterruptedException, IOException {
		this.BM_Create_Database_Instance(shapeDB, byol);
	}

	public void BM_Create_Database_Instance(String shape, boolean byol)
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateDatabaseAction.class)
				.createDatabaseInstance(shape, byol);
	}

	@Test
	public void BM_Terminate_Compute_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateComputeAction.class)
				.terminateComputeInstance();

	}

	@Test
	public void BM_Terminate_DB_Instance() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateDatabaseAction.class)
				.terminateDatabaseInstance();
	}

	@Test
	public void BM_Terminate_VCN_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.terminateVCNInstance();
	}

}
