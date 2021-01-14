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
import com.oracle.opc.automation.test.entity.enums.bmComputeBillingMetrics;

/**
 * @author xuezhen.x.niu@oracle.com
 *
 */

public class BMComputeMeteringTest extends BaseTest {

	CloudService bmComputeMBE;
	CloudService bmIAASCM;
	String bucketName;
	String myServiceURL;
	String myAccountUrl;
	List<String> meteringListMyAccount;
	List<String> meteringListMyService;

	public BMComputeMeteringTest() {

		bmComputeMBE = new CloudService(new ServiceSummary(), CloudServiceType.BMCSAC, QuoteType.PREPAID,
				AccountInformation.getConstantInstance(), new CloudServiceType[] { CloudServiceType.BMCSAC }, null);

		bmComputeMBE.setQuotaPlan(ServiceQuotaPlan.BMMBE);

		bmComputeMBE.getSummary()
				.setSubscriptionId(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.subscription.id"));

		bmComputeMBE.getSummary()
				.setServiceAccount(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmComputeMBE.getAccountInfo().setUserName(Constants.P_FILE.getProperties().getProperty("opc.bm.user.name"));
		bmComputeMBE.getSummary()
				.setDomainName(Constants.P_FILE.getProperties().getProperty("opc.bm.mbe.account.name"));

		bmComputeMBE.setAlreadyFirstLogin(true);

		bucketName = CloudServiceType.IAASMB.getFullName();

		myServiceURL = Constants.P_FILE.getProperties().getProperty("myservice.domain") + Constants.MYSERVICE_DASHBOARD;
		myAccountUrl = Constants.P_FILE.getProperties().getProperty("sso.url");

	}

	// ----------------BM Compute Metering -- MyAccount Part-------------//

	// Basic: Get all Metering data from MyAccount Details Page

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMCompute_MBE_MyAccount_Metering")
	public void BMCompute_MBE_MyAccount_Metering() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, LoginSSOPage.class).loginSSO(SSOInfo.getInstance());
		// AutomationPageFactory.getInstance().getPage(bmComputeMBE,
		// DashboardPage.class).gotoServiceDetail(bmComputeMBE,
		// myAccountUrl);

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, MyAccountPage.class).filterDomain();
		AutomationPageFactory.getInstance().getPage(bmComputeMBE, MyAccountPage.class)
				.goToDetailsMyAccount(bmComputeMBE, bucketName);

		meteringListMyAccount = AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.getMeteringUsage(bmComputeMBE, myAccountUrl);
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_NVME_12_8")
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_NVME_12_8() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_NVME_12_8"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_NVME_28_8")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_NVME_28_8() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_NVME_28_8"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_VM_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_VM_STANDARD() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_VM_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_OBJECT_STORAGE")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_OBJECT_STORAGE() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_OBJECT_STORAGE"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_OBJECT_STORAGE_REQUEST")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_OBJECT_STORAGE_REQUEST() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_OBJECT_STORAGE_REQUEST"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_LB_SMALL")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_LB_SMALL() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_LB_SMALL"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_LB_MEDIUM")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_LB_MEDIUM() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_LB_MEDIUM"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_LB_LARGE")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_LB_LARGE() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_LB_LARGE"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_X5_STANDARD() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_BLOCK_STORAGE_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_BLOCK_STORAGE_STANDARD() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_BLOCK_STORAGE_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_OUTBOUND_DATA_TRANSFER")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_OUTBOUND_DATA_TRANSFER() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_OUTBOUND_DATA_TRANSFER"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_FASTCONNECT_SMALL")
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_FASTCONNECT_SMALL() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_FASTCONNECT_SMALL"));
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_FASTCONNECT_LARGE")
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_FASTCONNECT_LARGEL() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_FASTCONNECT_LARGE"));
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_VM_DENSEIO")
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_VM_DENSEIO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_VM_DENSEIO"));
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyAccount_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyAccount_MBE_Metering_PIC_COMPUTE_WINDOWS_OS")
	public void BM_MyAccount_MBE_Metering_PIC_COMPUTE_WINDOWS_OS() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyAccount, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_WINDOWS_OS"));
	}

	// ----------------BM DB Metering -- MyServices Part-------------//

	// Basic: Get all Metering data from MyServices Details Page
	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "CloudPortal", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BMCompute_MBE_MyService_Metering")
	public void BMCompute_MBE_MyService_Metering() throws InterruptedException {

		AutomationPageFactory.getInstance().getPage(bmComputeMBE, DashboardPage.class).gotoServiceDetail(bmComputeMBE,
				myServiceURL);

		meteringListMyService = AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.getMeteringUsage(bmComputeMBE, myServiceURL);
	}

	// public void MyServiceMeteringValidation(CloudService service, String key)
	// throws InterruptedException {
	//
	// AutomationActionFactory.getInstance().getAction(service,
	// BareMetalAction.class).loopCheckMeteringData(service,
	// 1, key, myServiceURL);
	//
	// }

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_X5_NVME_12_8")
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_X5_NVME_12_8() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_NVME_12_8"));
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_X5_NVME_28_8")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_X5_NVME_28_8() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_NVME_28_8"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_VM_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_VM_STANDARD() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_VM_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_OBJECT_STORAGE")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_OBJECT_STORAGE() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_OBJECT_STORAGE"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_OBJECT_STORAGE_REQUEST")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_OBJECT_STORAGE_REQUEST() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_OBJECT_STORAGE_REQUEST"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_LB_SMALL")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_LB_SMALL() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_LB_SMALL"));
	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_LB_MEDIUM")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_LB_MEDIUM() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_LB_MEDIUM"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_LB_LARGE")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_LB_LARGE() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class)
				.validateBMMetering(meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_LB_LARGE"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_X5_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_X5_STANDARD() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_X5_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_BLOCK_STORAGE_STANDARD")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_BLOCK_STORAGE_STANDARD() throws InterruptedException {
		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_BLOCK_STORAGE_STANDARD"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_OUTBOUND_DATA_TRANSFER")
	@NeedCloseDriver(isNeeded = false)
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_OUTBOUND_DATA_TRANSFER() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_OUTBOUND_DATA_TRANSFER"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_WINDOWS_OS")
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_WINDOWS_OS() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_WINDOWS_OS"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_FASTCONNECT_SMALL")
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_FASTCONNECT_SMALL() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_FASTCONNECT_SMALL"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_FASTCONNECT_LARGE")
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_FASTCONNECT_LARGE() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_FASTCONNECT_LARGE"));

	}

	@Test(dependsOnMethods = { "BMCompute_MBE_MyService_Metering" })
	@CBMetaData(component = "LCM-Quota Metering", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_MyService_MBE_Metering_PIC_COMPUTE_VM_DENSEIO")
	public void BM_MyService_MBE_Metering_PIC_COMPUTE_VM_DENSEIO() throws InterruptedException {

		AutomationActionFactory.getInstance().getAction(bmComputeMBE, BareMetalAction.class).validateBMMetering(
				meteringListMyService, bmComputeBillingMetrics.getMetricLabel("PIC_COMPUTE_VM_DENSEIO"));

	}

}
