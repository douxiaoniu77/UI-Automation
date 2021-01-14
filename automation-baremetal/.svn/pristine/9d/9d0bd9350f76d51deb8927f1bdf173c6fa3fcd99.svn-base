
package com.oracle.opc.automation.test.testcase.test;

import java.io.IOException;

import org.testng.annotations.Test;

import com.oracle.opc.automation.common.annotation.NeedCloseDriver;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.actions.OCIConsoleAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.testcase.baremetal.BMAccounLifecycle2;

public class OCIConsolePreRequisite extends BaseTest {

	AutomationLogger logger = new AutomationLogger(BMAccounLifecycle2.class);
	CloudService bareMetal;

	public OCIConsolePreRequisite() {
		// Required configuration
		// oci.user.name - User name or email
		// oci.user.password - Password to be used for login
		// oci.user.tempPassword - Temporary password (can be same as password)
		// oci.user.apiKey - Path to Public PEM key
		// oci.account.name - Tenancy or identity domain name
		// oci.account.region - OCI region of the account
		// oci.account.configuration - Path to Configuration File

		// Proxy configuration in Browser
		System.setProperty("browser", "firefox");
		System.setProperty("proxy.type", "MANUAL");
		System.setProperty("proxy.http", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.ssl", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.noproxy", "*.oracleiaas.com*");

	}

	@Test
	public void ociChangeTemporaryPassword() {
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, OCIConsoleAction.class)
				.changeTemporaryPassword();
	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void ociConsoleLogin() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, OCIConsoleAction.class).loginBMConsole();

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void ociFederationLogin() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, OCIConsoleAction.class)
				.loginBMFederation();
	}

	@Test
	public void ociConfigureApiAccount()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, OCIConsoleAction.class)
				.setupAccountConfigFile();
	}

	@Test
	public void ociConfigurePipeline()
			throws InterruptedException, IOException {
		// Required configuration
		// pipeline.configuration - Path to the pipeline configuration file
		// pipeline.service - Service to be tested by Pipeline
		// pipeline.testsuite - Testsuite to be used (Consume or Release)
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, OCIConsoleAction.class).setupPipeline();
	}

}
