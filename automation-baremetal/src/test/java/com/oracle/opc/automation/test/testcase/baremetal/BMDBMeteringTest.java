/**

 * automation-baremetal
 * BMCSTest.java   for Bare Metal Compute Service Sanity Check 
 * 2016年10月19日
 
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
import com.oracle.opc.automation.test.entity.enums.bmDBBillingMetrics;

/**
 * @author xuezhen.x.niu@oracle.com
 *
 */

public class BMDBMeteringTest extends BaseTest {

	CloudService bmDBMBE;
	CloudService bmDBSBE;
	String bucketName;
	String myServiceURL;
	String myAccountUrl;
	List<String> meteringListMyAccount;
	List<String> meteringListMyService;

	public BMDBMeteringTest() {

		bmDBMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMDBAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMDBAC }, null);

		bmDBMBE.setQuotaPlan(ServiceQuotaPlan.BMDBMBE);

		bmDBMBE.getSummary()
				.setSubscriptionId(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.subscription.id"));

		bmDBMBE.getSummary().setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmDBMBE.getAccountInfo().setUserName(Constants.P_FILE.getProperties().getProperty("opc.bm.user.name"));
		bmDBMBE.getSummary().setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmDBMBE.setAlreadyFirstLogin(true);

		bucketName = CloudServiceType.BMDBAC.getFullName();

		myServiceURL = Constants.P_FILE.getProperties().getProperty("myservice.domain") + Constants.MYSERVICE_DASHBOARD;
		myAccountUrl = Constants.P_FILE.getProperties().getProperty("sso.url");

	}

	// @Test
	// @NeedCloseDriver(isNeeded = false)
	// public void MyServices_Login() throws InterruptedException {
	//
	// AutomationActionFactory.getInstance().getAction(bmDBMBE, LoginServiceAction.class)
	// .loginMyServiceWithFullInfo(bmDBMBE, myServiceURL);
	// }

	// ----------------BM DB Metering -- MyAccount Part-------------//

	// Basic: Get all Metering data from MyAccount Details Page

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MBE_MyAccount_Metering")
	public void BMDB_MBE_MyAccount_Metering() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmDBMBE, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());
		// AutomationPageFactory.getInstance().getPage(bmDBMBE, DashboardPage.class).gotoServiceDetail(bmDBMBE,
		// myAccountUrl);

		AutomationPageFactory.getInstance().getPage(bmDBMBE, MyAccountPage.class).filterDomain();
		AutomationPageFactory.getInstance().getPage(bmDBMBE, MyAccountPage.class).goToDetailsMyAccount(bmDBMBE,
				bucketName);

		meteringListMyAccount = AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class)
				.getMeteringUsage(bmDBMBE, myAccountUrl);
	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_STANDARD_HIGH_IO")
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_STANDARD_HIGH_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_STANDARD_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_STANDARD_DENSE_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_Metering_PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_DENSE_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY")
	public void BMDB_MyAccount_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY"));
	}

	// ----------------BM DB Metering -- MyServices Part-------------//

	// Basic: Get all Metering data from MyServices Details Page
	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MBE_MyService_Metering")
	public void BMDB_MBE_MyService_Metering() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmDBMBE, DashboardPage.class).gotoServiceDetail(bmDBMBE,
				myServiceURL);
		
		meteringListMyService = AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class)
				.getMeteringUsage(bmDBMBE, myServiceURL);
	}

	// public void MyServiceMeteringValidation(CloudService service, String key) throws InterruptedException {
	//
	// AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class).loopCheckMeteringData(service,
	// 1, key, myServiceURL);
	//
	// }

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_STANDARD_HIGH_IO")
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_STANDARD_HIGH_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_HIGH_IO"));
	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_STANDARD_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_STANDARD_DENSE_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_Metering_PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_DENSE_IO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY"));
	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO()
			throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO()
			throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY()
			throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO()
			throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO")
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO"));

	}

	@Test(dependsOnMethods = { "BMDB_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY")
	public void BMDB_MyServices_MBE_Metering_PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY()
			throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService,
				bmDBBillingMetrics.getMetricLabel("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY"));

	}

	// MyAccount BM DB Part

	// public void MyAccountMeteringValidation(CloudService service, String key) throws InterruptedException {
	//
	// AutomationPageFactory.getInstance().getPage(service, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());
	//
	// AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoAppliedService(service,
	// myAccountUrl);
	//
	// AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).goToDetailsMyAccount();
	//
	// AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class).loopCheckMeteringData(service,
	// 5, key, myAccountUrl);
	//
	// }

	// @Test
	// @CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering")
	// public void BM_MyService_MBE_Metering() throws InterruptedException {
	//
	// AutomationActionFactory.getInstance().getAction(bareMetalMBE, LoginServiceAction.class)
	// .loginMyServiceWithFullInfo(bareMetalMBE, myServiceURL);
	//
	// // AutomationActionFactory.getInstance()
	// // .getAction(doc_service, DocCIMAction.class)
	// // .myAccount_checkSubscription_SBE()
	// // .checkAccount(expectedMyaccount_AccountName_SBE)
	// // .checkCatagory(expectedMyaccount_Category_SBE);
	// AutomationActionFactory.getInstance().getAction(bareMetalMBE, BareMetalAction.class)
	// .validateBMMetering(bareMetalMBE, myServiceURL);
	//
	// }
	//
	// @Test
	// @CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering")
	// public void BM_MyAccount_MBE_Metering() throws InterruptedException {
	//
	// String myAccountUrl = Constants.P_FILE.getProperties().getProperty("sso.url");
	//
	// AutomationPageFactory.getInstance().getPage(bareMetalMBE, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());
	//
	// AutomationActionFactory.getInstance().getAction(bareMetalMBE, BareMetalAction.class)
	// .validateBMMetering(bareMetalMBE, myAccountUrl);
	//
	// }

	// SBE Part

}
