/**

 * automation-baremetal
 * BMCSTest.java   for Bare Metal Compute Service Sanity Check 
 * 2016年10月19日
 
 */

package com.oracle.opc.automation.test.testcase.baremetal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.annotation.CBMetaData;
import com.oracle.opc.automation.common.annotation.NeedCloseDriver;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.AccountInformation;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.SSOInfo;
import com.oracle.opc.automation.entity.ServiceSummary;
import com.oracle.opc.automation.entity.enums.CloudServiceType;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.entity.enums.QuoteType;
import com.oracle.opc.automation.entity.enums.ServiceQuotaPlan;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.actions.BareMetalAction;
import com.oracle.opc.automation.test.component.actions.service.LoginServiceAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.component.factory.AutomationPageFactory;
import com.oracle.opc.automation.test.component.pages.LoginSSOPage;
import com.oracle.opc.automation.test.component.pages.portal.DashboardPage;
import com.oracle.opc.automation.test.component.pages.portal.MyAccountPage;
import com.oracle.opc.automation.test.component.pages.portal.MyServicePage;
import com.oracle.opc.automation.test.entity.enums.Buckets;

/**
 * @author xueniu
 *
 */

public class BMSanityTest extends BaseTest {

	AutomationLogger logger = new AutomationLogger(BMAccounLifecycle2.class);

	CloudService bmDBMBE;
	CloudService bmDBSBE;
	CloudService bmComputeMBE;
	CloudService bmIAASCM;
	CloudService bmExadataMBE;
	CloudService bmExadataSBE;
	String myServiceURL;
	String myAccountUrl;
	String userName;
	String mbeAccount;

	String labelAccountManagement;
	String bucketName = Buckets.BMC.getBucketName();

	public BMSanityTest() {

		userName = Constants.P_FILE.getProperties().getProperty("opc.bm.user.name");
		mbeAccount = Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name");

		// BMCompute MBE Initialization
		bmComputeMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMCSAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMCSAC }, null);
		bmComputeMBE.setQuotaPlan(ServiceQuotaPlan.BMMBE);
		bmComputeMBE.getSummary().setServiceAccount(mbeAccount);
		bmComputeMBE.getAccountInfo().setUserName(userName);
		bmComputeMBE.getSummary().setDomainName(mbeAccount);
		bmComputeMBE.setAlreadyFirstLogin(true);

		// BMCompute IAASCM Initialization
		bmIAASCM = new CloudService(new ServiceSummary(), CloudServiceType.IAASCM, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.IAASCM }, null);
		bmIAASCM.setQuotaPlan(ServiceQuotaPlan.IAASCM);
		bmIAASCM.getSummary()
				.setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bmdb.sbe.account.name"));
		bmIAASCM.getAccountInfo().setUserName(userName);
		bmIAASCM.getSummary().setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bmdb.sbe.account.name"));
		bmIAASCM.setAlreadyFirstLogin(true);

		// BMDB MBE Initialization
		bmDBMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMDBAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMDBAC }, null);
		bmDBMBE.setQuotaPlan(ServiceQuotaPlan.BMDBMBE);
		// bmDBMBE.getSummary()
		// .setSubscriptionId(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.subscription.id"));
		bmDBMBE.getSummary().setServiceAccount(mbeAccount);
		bmDBMBE.getAccountInfo().setUserName(userName);
		bmDBMBE.getSummary().setDomainName(mbeAccount);

		bmDBMBE.setAlreadyFirstLogin(true);

		// BMExadata MBE Initialization
		bmExadataMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMEXAAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMEXAAC }, null);
		bmExadataMBE.setQuotaPlan(ServiceQuotaPlan.BMEXAMBE);
		bmExadataMBE.getSummary().setServiceAccount(mbeAccount);
		bmExadataMBE.getAccountInfo().setUserName(userName);
		bmExadataMBE.getSummary().setDomainName(mbeAccount);
		bmExadataMBE.setAlreadyFirstLogin(true);

		// BMDB SBE Initialization
		bmDBSBE = new CloudService(new ServiceSummary(), CloudServiceType.BMDBAC, QuoteType.PAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMDBAC }, null);
		bmDBSBE.setQuotaPlan(ServiceQuotaPlan.BMDBSBE);
		bmDBSBE.getSummary()
				.setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bmdb.sbe.account.name"));
		bmDBSBE.getAccountInfo().setUserName(userName);
		bmDBSBE.getSummary().setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bmdb.sbe.account.name"));
		bmDBSBE.setAlreadyFirstLogin(true);

		// BMExadata SBE Initialization
		bmExadataSBE = new CloudService(new ServiceSummary(), CloudServiceType.BMEXAAC, QuoteType.PAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMEXAAC }, null);
		bmExadataSBE.setQuotaPlan(ServiceQuotaPlan.BMEXASBE);
		bmExadataSBE.getSummary()
				.setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bmexadata.sbe.account.name"));
		bmExadataSBE.getAccountInfo().setUserName(userName);
		bmExadataSBE.getSummary()
				.setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bmexadata.sbe.account.name"));
		bmExadataSBE.setAlreadyFirstLogin(true);

		// bmDBMBE.getSummary().setServiceName(CloudServiceType.BMCSAC
		// .getJetDashboardName());

		myServiceURL = Constants.P_FILE.getProperties().getProperty("myservice.domain") + Constants.MYSERVICE_DASHBOARD;
		myAccountUrl = Constants.P_FILE.getProperties().getProperty("sso.url");

	}

	@Test
	@Parameters("dataCenterId")
	public void BM_UsageReport(String dataCenterId) throws InterruptedException, IOException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).checkBMReporting(dataCenterId);

	}

	@Test
	public void BM_TAS_SM_Validate() throws InterruptedException, IOException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateTAS_BMSM();

	}

	@Test
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_Login_SM_Console")
	public void BM_Login_ServiceConsole() throws InterruptedException {

		// String serviceConsoleUrl =
		// Constants.P_FILE.getProperties().getProperty("opc.baremetal.console.url_prefix")
		// +
		// Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name");

		// AutomationActionFactory.getInstance().getAction(bmDBMBE,
		// LoginServiceAction.class)
		// .loginMyServiceWithFullInfo(bmDBMBE, serviceConsoleUrl);

		AutomationActionFactory.getInstance().getAction(bmDBMBE, LoginServiceAction.class)
				.loginMyServiceWithFullInfo(bmDBMBE, myServiceURL);

		AutomationPageFactory.getInstance().getPage(bmDBMBE, DashboardPage.class).gotoServiceDetail(bmDBMBE,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).gotoBMServiceConsole(bmDBMBE);

	}

	/*
	 * Portal Sanity Test -- MyAccount Validation Part
	 */

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void MyAccount_Login() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());

		// AutomationPageFactory.getInstance().getPage(bmComputeMBE,
		// LoginSSOPage.class).loginSSO(myAccountUrl);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

		// AutomationPageFactory.getInstance().getPage(bmComputeMBE,
		// DashboardPage.class).setPreference(language, dcTimezone, timezone);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_Dashboard_List")
	public void BM_MyAccount_Dashboard_MBE_List() throws InterruptedException {

		MyAccountDashboardServiceList(bmComputeMBE);

	}

	public void MyAccountDashboardServiceList(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);
		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).filterDomain();

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class).checkBMListMyAccount(service);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MBE_MyAccount_Subscription")
	public void BM_MyAccount_Dashboard_MBE_Subscription() throws InterruptedException {

		MyAccountDashboardSubscriptionCheck(bmComputeMBE);

	}

	@Test(dependsOnMethods = { "BM_MyAccount_Dashboard_MBE_Subscription" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_Subscription")
	public void BMDB_MyAccount_Subscription() throws InterruptedException {

		logger.info(LoggerType.COMMENT, "domainName: " + bmDBMBE.getSummary().getDomainName());
		MyAccountDashboardSubscriptionCheck(bmDBMBE);

	}

	@Test(dependsOnMethods = { "BM_MyAccount_Dashboard_MBE_Subscription" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_MyAccount_Subscription")
	public void BMExadata_MBE_MyAccount_Subscription() throws InterruptedException {

		MyAccountDashboardSubscriptionCheck(bmExadataMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_MyAccount_Subscription")
	public void BMDB_SBE_MyAccount_Subscription() throws InterruptedException {

		MyAccountDashboardSubscriptionCheck(bmDBSBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_MyAccount_Subscription")
	public void BMExadata_SBE_MyAccount_Subscription() throws InterruptedException {

		MyAccountDashboardSubscriptionCheck(bmExadataSBE);

	}

	public void MyAccountDashboardSubscriptionCheck(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);

		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).filterDomain();

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.checkSubstTypeMyAccount(service);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Status")
	public void BM_MyAccount_MBE_Status() throws InterruptedException {

		MyAccountServiceStatus(bmComputeMBE);
	}

	public void MyAccountServiceStatus(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);
		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).filterDomain();
		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).goToDetailsMyAccount();
		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.checkServiceStatusDetailsPage(service);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BM_Account_Balance_MyAccount")
	public void BM_MyAccount_Estimate_Balance() throws InterruptedException {

		MyAccountEstimateBalance(bmComputeMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMDB_Account_Balance_MyAccount")
	public void BMDB_Account_Balance_MyAccount() throws InterruptedException {

		MyAccountEstimateBalance(bmDBMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMExadata_MBE_Account_Balance_MyAccount")
	public void BMExadata_MBE_Account_Balance_MyAccount() throws InterruptedException {

		MyAccountEstimateBalance(bmExadataMBE);

	}

	public void MyAccountEstimateBalance(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);
		AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoAppliedService(service,
				myAccountUrl);
		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.checkEstimateBalanceMyAccount(service);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_ViewDetails")
	public void BM_MyAccount_ViewDetails() throws InterruptedException {

		MyAccountViewDetails(bmComputeMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyAccount_ViewDetails")
	public void BMDB_MyAccount_ViewDetails() throws InterruptedException {

		MyAccountViewDetails(bmDBMBE);
	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_MyAccount_ViewDetails")
	public void BMExadata_MBE_MyAccount_ViewDetails() throws InterruptedException {

		MyAccountViewDetails(bmExadataMBE);

	}

	public void MyAccountViewDetails(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);
		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).filterDomain();

		AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoAppliedService(service,
				myAccountUrl);
		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).goToDetailsMyAccount();

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.checkBMViewDetailsMyAccount(service);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_MyAccount_ViewDetails")
	public void BMExadata_SBE_MyAccount_ViewDetails() throws InterruptedException {

		MyAccountViewDetails(bmExadataSBE);
	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_MyAccount_ViewDetails")
	public void BMDB_SBE_MyAccount_ViewDetails() throws InterruptedException {

		MyAccountViewDetails(bmDBSBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_Account_Quota_MyAccount")
	public void BMDB_SBE_Account_Quota_MyAccount() throws InterruptedException {

		MyAccountResourceQuotaValidation(bmDBSBE);
	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_Account_Quota_MyAccount")
	public void BMExadata_SBE_Account_Quota_MyAccount() throws InterruptedException {

		MyAccountResourceQuotaValidation(bmExadataSBE);
	}

	public void MyAccountResourceQuotaValidation(CloudService service) throws InterruptedException {

		ActionUtils.gotoUrl(myAccountUrl);

		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).filterDomain();

		// AutomationPageFactory.getInstance().getPage(service,
		// DashboardPage.class).gotoServiceDetail(service,
		// myAccountUrl);

		AutomationPageFactory.getInstance().getPage(service, MyAccountPage.class).goToDetailsMyAccount();

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.AccountResourceQuotaValidation(service);

	}

	/*
	 * Portal Sanity Test -- MBE MyServices Part
	 */

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void MBE_MyService_Login() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, LoginServiceAction.class)
				.loginMyServiceWithFullInfo(bmDBMBE, myServiceURL);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

	}

	@Test(dependsOnMethods = { "MBE_MyService_Login" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MBE_MyService_Subscription")
	public void BM_MyService_MBE_Subscription() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.subscriptionValidation(bmComputeMBE, myServiceURL);
	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyService_Subscription")
	public void BMDB_MyService_Subscription() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).subscriptionValidation(bmDBMBE,
				myServiceURL);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMDB_MyService_Subscription")
	public void BMExadata_MyService_Subscription() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class)
				.subscriptionValidation(bmExadataMBE, myServiceURL);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Status")
	public void BM_MyService_MBE_Status() throws InterruptedException {

		// AutomationActionFactory.getInstance().getAction(bmComputeMBE,
		// BareMetalAction.class)
		// .checkServiceStatusDetailsPage(bmComputeMBE, myServiceURL);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_Estimate_Balance")
	public void BM_MBE_Account_Balance_MyService() throws InterruptedException {

		labelAccountManagement = "IAASMB";

		ActionUtils.gotoUrl(myServiceURL);

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, MyServicePage.class).goToMyServiceAccountManagement();

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, MyServicePage.class)
				.checkAccountManagementPage(labelAccountManagement);

	}

	@Test(dependsOnMethods = { "BM_MBE_Account_Balance_MyService" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_Account_Balance_MyService")
	public void BMExadata_MBE_Account_Balance_MyService() throws InterruptedException {

		labelAccountManagement = "DBMB";
		AutomationPageFactory.getInstance().getPage(bmExadataMBE, MyServicePage.class)
				.checkAccountManagementPage(labelAccountManagement);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_ViewDetails")
	public void BM_MyService_ViewDetails() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, DashboardPage.class).gotoServiceDetail(bmComputeMBE,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.checkBMViewDetailsMyAccount(bmComputeMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_MyService_ViewDetails")
	public void BMDB_MyService_ViewDetails() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmDBMBE, DashboardPage.class).gotoServiceDetail(bmDBMBE,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class)
				.checkBMViewDetailsMyAccount(bmDBMBE);

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_MBE_MyService_ViewDetails")
	public void BMExadata_MBE_MyService_ViewDetails() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmExadataMBE, DashboardPage.class).gotoServiceDetail(bmExadataMBE,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(bmExadataMBE, BareMetalAction.class)
				.checkBMViewDetailsMyAccount(bmExadataMBE);

	}

	@Test
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "MyService_ViewDetails_SFTP")
	public void BM_MyService_ViewDetails_SFTP() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmDBMBE, DashboardPage.class).gotoServiceDetail(bmDBMBE,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).checkSFTPViewDetails(bmDBMBE);

	}

	/*
	 * Portal Sanity Test -- BMDB SBE MyServices Part
	 */

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void BMDB_SBE_MyService_Login() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBSBE, LoginServiceAction.class)
				.loginMyServiceWithFullInfo(bmDBSBE, myServiceURL);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

	}

	@Test(dependsOnMethods = { "BMDB_SBE_MyService_Login" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_MyService_Subscription")
	public void BMDB_SBE_MyService_Subscription() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBSBE, BareMetalAction.class).subscriptionValidation(bmDBSBE,
				myServiceURL);

	}

	@Test(dependsOnMethods = { "BMDB_SBE_MyService_Login" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_Account_Quota_MyService")
	public void BMDB_SBE_Account_Quota_MyService() throws InterruptedException {

		MyServiceResourceQuotaValidation(bmDBSBE);

	}

	public void MyServiceResourceQuotaValidation(CloudService service) throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoServiceDetail(service,
				myServiceURL);

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.AccountResourceQuotaValidation(service);

	}

	@Test(dependsOnMethods = { "BMDB_SBE_MyService_Login" })
	@CBMetaData(component = "CloudPortal", product = "BareMetal Database", subComponent = "", testCaseNameInCB = "BMDB_SBE_MyService_ViewDetails")
	public void BMDB_SBE_MyService_ViewDetails() throws InterruptedException {

		MyServicesViewDetails(bmDBSBE);

	}

	public void MyServicesViewDetails(CloudService service) throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(service, DashboardPage.class).gotoServiceDetail(service,
				myServiceURL);
		// .gotoAppliedService(service, myServiceURL);
		//
		// AutomationPageFactory.getInstance().getPage(service,
		// MyAccountPage.class).goToDetailsMyAccount();

		AutomationActionFactory.getInstance().getAction(service, BareMetalAction.class)
				.checkBMViewDetailsMyAccount(service);

	}

	/*
	 * Portal Sanity Test -- BMExadata SBE MyServices Part
	 */

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void BMExadata_SBE_MyService_Login() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataSBE, LoginServiceAction.class)
				.loginMyServiceWithFullInfo(bmExadataSBE, myServiceURL);

		ActionUtils.waitFor(TimeUnit.SECONDS, Constants.DEFAULT_SHORT_TIMEOUT);

	}

	@Test(dependsOnMethods = { "BMExadata_SBE_MyService_Login" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_MyService_Subscription")
	public void BMExadata_SBE_MyService_Subscription() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmExadataSBE, BareMetalAction.class)
				.subscriptionValidation(bmExadataSBE, myServiceURL);
	}

	@Test(dependsOnMethods = { "BMExadata_SBE_MyService_Login" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_Account_Quota_MyService")
	public void BMExadata_SBE_Account_Quota_MyService() throws InterruptedException {

		MyServiceResourceQuotaValidation(bmExadataSBE);

	}

	//

	@Test(dependsOnMethods = { "BMExadata_SBE_MyService_Login" })
	@CBMetaData(component = "CloudPortal", product = "BareMetal Exadata", subComponent = "", testCaseNameInCB = "BMExadata_SBE_MyService_ViewDetails")
	public void BMExadata_SBE_MyService_ViewDetails() throws InterruptedException {

		MyServicesViewDetails(bmExadataSBE);

	}

	@Test
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering")
	public void BM_MyService_MBE_Metering() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmDBMBE, LoginServiceAction.class)
				.loginMyServiceWithFullInfo(bmDBMBE, myServiceURL);

		// AutomationActionFactory.getInstance()
		// .getAction(doc_service, DocCIMAction.class)
		// .myAccount_checkSubscription_SBE()
		// .checkAccount(expectedMyaccount_AccountName_SBE)
		// .checkCatagory(expectedMyaccount_Category_SBE);
		AutomationActionFactory.getInstance().getAction(bmDBMBE, BareMetalAction.class).validateBMMetering(bmDBMBE,
				myServiceURL);

	}

}
