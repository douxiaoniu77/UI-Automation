/**

 * automation-baremetal
 * BMCSTest.java   for Bare Metal Compute Service Sanity Check 
 * 2017/06/29
 
 */

package com.oracle.opc.automation.test.testcase.baremetal;

import java.util.List;

import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.annotation.CBMetaData;
import com.oracle.opc.automation.common.annotation.NeedCloseDriver;
import com.oracle.opc.automation.entity.AccountInformation;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.SSOInfo;
import com.oracle.opc.automation.entity.ServiceSummary;
import com.oracle.opc.automation.entity.enums.CloudServiceType;
import com.oracle.opc.automation.entity.enums.QuoteType;
import com.oracle.opc.automation.entity.enums.ServiceQuotaPlan;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.actions.BareMetalAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.component.factory.AutomationPageFactory;
import com.oracle.opc.automation.test.component.pages.LoginSSOPage;
import com.oracle.opc.automation.test.component.pages.portal.DashboardPage;
import com.oracle.opc.automation.test.component.pages.portal.MyAccountPage;
import com.oracle.opc.automation.test.entity.enums.bmExadataBillingMetrics;

/**
 * @author Ulises
 *
 */

public class BMEXAMBEMeteringTest extends BaseTest {

	CloudService bmExadataMBE;
	CloudService bmExadataSBE;
	String bucketName;
	String myServiceURL;
	String myAccountUrl;
	List<String> meteringListMyAccount;
	List<String> meteringListMyService;

	public BMEXAMBEMeteringTest() {

		bmExadataMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMEXAAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMEXAAC }, null);

		bmExadataMBE.setQuotaPlan(ServiceQuotaPlan.BMDBMBE);

		bmExadataMBE.getSummary()
				.setSubscriptionId(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.subscription.id"));

		bmExadataMBE.getSummary()
				.setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmExadataMBE.getAccountInfo().setUserName(Constants.P_FILE.getProperties().getProperty("opc.bm.user.name"));
		bmExadataMBE.getSummary()
				.setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmExadataMBE.setAlreadyFirstLogin(true);

		bucketName = CloudServiceType.BMEXAAC.getFullName();

		myServiceURL = Constants.P_FILE.getProperties().getProperty("myservice.domain") + Constants.MYSERVICE_DASHBOARD;
		myAccountUrl = Constants.P_FILE.getProperties().getProperty("sso.url");

	}
	// ----------------BM DB Metering -- MyAccount Part-------------//

	// Basic: Get all Metering data from MyAccount Details Page

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMExadata_MBE_MyAccount_Metering")
	public void BMExadata_MBE_MyAccount_Metering() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmExadataMBE, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());
		// AutomationPageFactory.getInstance().getPage(bmExadataMBE, DashboardPage.class).gotoServiceDetail(bmExadataMBE,
		// myAccountUrl);

		AutomationPageFactory.getInstance().getPage(bmExadataMBE, MyAccountPage.class).filterDomain();
		AutomationPageFactory.getInstance().getPage(bmExadataMBE, MyAccountPage.class)
				.goToDetailsMyAccount(bmExadataMBE, bucketName);

		meteringListMyAccount = AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class)
				.getMeteringUsage(bmExadataMBE, myAccountUrl);
	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_QUARTER_RACK_X6_Portal_MyAccount")
	public void BMExadata_MBE_PIC_EXADATA_QUARTER_RACK_X6_Portal_MyAccount() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_QUARTER_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_HALF_RACK_X6_Portal_MyAccount")
	public void BMExadata_MBE_PIC_EXADATA_HALF_RACK_X6_Portal_MyAccount() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_HALF_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_FULL_RACK_X6_Portal_MyAccount")
	public void BMExadata_MBE_PIC_EXADATA_FULL_RACK_X6_Portal_MyAccount() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_FULL_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY_Portal_MyAccount")
	public void BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY_Portal_MyAccount()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY_Portal_MyAccount")
	public void BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY_Portal_MyAccount() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY"));

	}

	// ----------------BM DB Metering -- MyServices Part-------------//

	// Basic: Get all Metering data from MyServices Details Page
	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MBE_MyService_Metering")
	public void BMExadata_MBE_MyService_Metering() throws InterruptedException {
		AutomationPageFactory.getInstance().getPage(bmExadataMBE, DashboardPage.class).gotoServiceDetail(bmExadataMBE,
				myServiceURL);

		meteringListMyService = AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class)
				.getMeteringUsage(bmExadataMBE, myServiceURL);
	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_QUARTER_RACK_X6_Portal_MyService")
	public void BMExadata_MBE_PIC_EXADATA_QUARTER_RACK_X6_Portal_MyService() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_QUARTER_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_HALF_RACK_X6_Portal_MyService")
	public void BMExadata_MBE_PIC_EXADATA_HALF_RACK_X6_Portal_MyService() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_HALF_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_FULL_RACK_X6_Portal_MyService")
	public void BMExadata_MBE_PIC_EXADATA_FULL_RACK_X6_Portal_MyService() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_FULL_RACK_X6"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY_Portal_MyService")
	public void BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY_Portal_MyService()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY"));

	}

	@Test(dependsOnMethods = { "BMExadata_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY_Portal_MyService")
	public void BMExadata_MBE_PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY_Portal_MyService() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmExadataBillingMetrics.getMetricLabel("PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY"));

	}

	// SBE Part

}
