package com.oracle.opc.automation.test.testcase.baremetal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.actions.BMConsoleAction;
import com.oracle.opc.automation.test.component.actions.BareMetalAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;

public class BMAccounLCM extends BaseTest {

	AutomationLogger logger = new AutomationLogger(BMAccounLCM.class);

	CloudService bareMetal;

	public BMAccounLCM() {

		// BMAccount = initializeBMCompute();

	}

	// public CloudService initializeBMCompute() {
	// BMComputeMBE = new CloudService(new ServiceSummary(),
	// CloudServiceType.BMCSAC, QuoteType.PREPAID,
	// AccountInformation.getConstantInstance(), new CloudServiceType[] {
	// CloudServiceType.BMCSAC }, null);
	// BMComputeMBE.setQuotaPlan(ServiceQuotaPlan.BMMBE);
	// BMComputeMBE.getSummary().setServiceName(CloudServiceType.BMCSAC.getShortName());
	//
	// return BMComputeMBE;
	//
	// }

	@Test
	public void BM_Account_DisableAll() throws InterruptedException, IOException {

		AutomationActionFactory.getInstance().getAction(bareMetal, BareMetalAction.class).disableAllBMAccount();

		String expectedTitle = "Planned Outage";

		AutomationActionFactory.getInstance().getAction(bareMetal, BareMetalAction.class).loginMyService(expectedTitle,
				1);

		String expectedStatus = "Your account has been disabled";

		AutomationActionFactory.getInstance().getAction(bareMetal, BMConsoleAction.class)
				.checkBMConsole(expectedStatus);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

	}

	@Test
	public void BM_Account_EnableAll() throws InterruptedException, IOException {

		AutomationActionFactory.getInstance().getAction(bareMetal, BareMetalAction.class).enableAllBMAccount();

		// String expectedTitle = "My Services - Dashboard";
		String expectedTitle = "Identity Cloud Service";

		AutomationActionFactory.getInstance().getAction(bareMetal, BareMetalAction.class).loginMyService(expectedTitle,
				1);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		AutomationActionFactory.getInstance().getAction(bareMetal, BMConsoleAction.class).loginBMConsole();

	}

}
