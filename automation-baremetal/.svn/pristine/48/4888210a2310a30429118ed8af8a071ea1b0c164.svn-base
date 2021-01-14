package com.oracle.opc.automation.test.testcase.baremetal;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.WebDriverManager;
import com.oracle.opc.automation.common.annotation.CBMetaData;
import com.oracle.opc.automation.common.listener.GenerateCBResourceListener;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.entity.AccountInformation;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.entity.ServiceSummary;
import com.oracle.opc.automation.entity.enums.CloudServiceType;
import com.oracle.opc.automation.entity.enums.LoggerType;
import com.oracle.opc.automation.entity.enums.QuoteType;
import com.oracle.opc.automation.entity.enums.ServiceQuotaPlan;
import com.oracle.opc.automation.entity.xstream.payload.PLIORDER;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.ServiceAction;
import com.oracle.opc.automation.test.component.bizactions.ActivateAction;
import com.oracle.opc.automation.test.component.bizactions.CreateAction;
import com.oracle.opc.automation.test.component.bizactions.SoftTerminateAction;
import com.oracle.opc.automation.test.component.utils.payload.PayloadModifier;

public class BMAccounLifecycle2 extends BaseTest {

	AutomationLogger logger = new AutomationLogger(BMAccounLifecycle2.class);

	CloudService BMComputeMBE;
	CloudService BMDBMBE;
	CloudService BMExadataMBE;

	CloudService BMIAASCM;
	CloudService BMDBSBE;
	CloudService BMExadataSBE;

	// CloudService BMCLOUDCM;
	CloudService BMDBCLOUDCM;

	public BMAccounLifecycle2() {

		BMComputeMBE = initializeBMCompute();
		BMDBMBE = initializeBMDBMBE();
		BMIAASCM = initializeBMIAASCM();
		BMDBCLOUDCM = initializeBMDBCLOUDCM();

	}

	public CloudService initializeBMCompute() {
		BMComputeMBE = new CloudService(new ServiceSummary(),
				CloudServiceType.BMCSAC, QuoteType.PREPAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.BMCSAC }, null);
		BMComputeMBE.setQuotaPlan(ServiceQuotaPlan.BMMBE);
		BMComputeMBE.getSummary().setServiceName(CloudServiceType.BMCSAC
				.getShortName());

		return BMComputeMBE;

	}

	public CloudService initializeBMDBMBE() {
		BMDBMBE = new CloudService(new ServiceSummary(),
				CloudServiceType.BMDBAC, QuoteType.PREPAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.BMDBAC }, null);
		BMDBMBE.setQuotaPlan(ServiceQuotaPlan.BMDBMBE);
		BMDBMBE.getSummary().setServiceName(CloudServiceType.BMDBAC
				.getShortName());

		return BMDBMBE;

	}

	public CloudService initializeBMIAASCM() {
		BMIAASCM = new CloudService(new ServiceSummary(),
				CloudServiceType.IAASCM, QuoteType.PREPAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.IAASCM }, null);
		BMIAASCM.setQuotaPlan(ServiceQuotaPlan.IAASCM);

		logger.info(LoggerType.COMMENT, "Print ServiceName: " + BMIAASCM
				.getSummary().getServiceName());
		return BMIAASCM;

	}

	public CloudService initializeBMDBCLOUDCM() {

		BMDBCLOUDCM = new CloudService(new ServiceSummary(),
				CloudServiceType.BMDBAC, QuoteType.PAYASYOUGO,
				AccountInformation.getConstantInstance(),
				new CloudServiceType[] { CloudServiceType.BMDBAC }, null);
		BMDBCLOUDCM.setQuotaPlan(ServiceQuotaPlan.CLOUDCMBUK);

		return BMDBCLOUDCM;

	}

	/**
	 * For New Commit Model and NonMetered BM Services
	 */

	@Test
	public void Create_BM_IAASCM_Account() {

		createAccount(BMIAASCM);
		activateAccount(BMIAASCM);
	}

	@Test(
			dependsOnMethods = { "Create_BM_IAASCM_Account" })
	// <---- Set dependency to previous creation and activation test case for
	// IAASCM
	public void Create_BMDB_SBE_ExistingAccount() {

		// BMIAASCM.getSummary().setDomainName("a4357721");
		BMDBSBE = new CloudService(new ServiceSummary(),
				CloudServiceType.BMDBAC, QuoteType.PAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.BMDBAC }, null);

		BMDBSBE.setAccountInfo(BMIAASCM.getAccountInfo());
		BMDBSBE.setQuotaPlan(ServiceQuotaPlan.BMDBSBE);
		// <---- Set first login
		BMDBSBE.setAlreadyFirstLogin(true);
		// <---- get tenant name
		String existingDomain = BMIAASCM.getSummary().getDomainName();

		BMDBSBE.getSummary().setDomainName(existingDomain);

		CreateAction action = new CreateAction();
		Map<String, String> params = new HashMap<String, String>();
		// get a list of parameters to be changed in payload (tenant name this
		params.put("SYSTEM_NAME_FOR_AUTO_COMP", existingDomain);
		logger.info(LoggerType.COMMENT, "Print : " + params.get(
				"SYSTEM_NAME_FOR_AUTO_COMP"));
		action.execute(BMDBSBE, new PayloadModifier() {
			@Override
			public void modify(PLIORDER order,
					Map<String, String> changedItems) {
				order.getProperties().setItem("SYSTEM_NAME_FOR_AUTO_COMP",
						changedItems.get("SYSTEM_NAME_FOR_AUTO_COMP"));

				logger.info(LoggerType.COMMENT, "TEST COMMENT" + order
						.getProperties());
			}
		}, params);

		WebDriverManager.stopDriver();
		WebDriverManager.getDriverInstance();
		activateAccount(BMDBSBE);
	}

	@Test(
			dependsOnMethods = { "Create_BM_IAASCM_Account" })
	public void Create_BMExadata_SBE_ExistingAccount() {

		BMExadataSBE = new CloudService(new ServiceSummary(),
				CloudServiceType.BMEXAAC, QuoteType.PAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.BMEXAAC }, null);

		BMExadataSBE.setAccountInfo(BMIAASCM.getAccountInfo());
		BMExadataSBE.setQuotaPlan(ServiceQuotaPlan.BMEXASBE);
		BMExadataSBE.setAlreadyFirstLogin(true);

		String existingDomain = BMIAASCM.getSummary().getDomainName();
		BMExadataSBE.getSummary().setDomainName(existingDomain);

		CreateAction action = new CreateAction();
		Map<String, String> params = new HashMap<String, String>();

		params.put("SYSTEM_NAME_FOR_AUTO_COMP", existingDomain);
		logger.info(LoggerType.COMMENT, "Print : " + params.get(
				"SYSTEM_NAME_FOR_AUTO_COMP"));
		action.execute(BMExadataSBE, new PayloadModifier() {
			@Override
			public void modify(PLIORDER order,
					Map<String, String> changedItems) {
				order.getProperties().setItem("SYSTEM_NAME_FOR_AUTO_COMP",
						changedItems.get("SYSTEM_NAME_FOR_AUTO_COMP"));
			}
		}, params);
		logger.info(LoggerType.COMMENT, "TEST COMMENT");

		WebDriverManager.stopDriver();
		WebDriverManager.getDriverInstance();
		activateAccount(BMExadataSBE);
	}

	/**
	 * For CLOUDCM Services
	 */

	@Test
	@CBMetaData(
			component = "LCM-Provisioning", product = "BareMetal Database",
			subComponent = "",
			testCaseNameInCB = "BMDB_CLOUDCM_Montly_Account_Provision_Payload")
	public void Create_BMDB_CLOUDCM_Account() {

		boolean checkResult = false;
		try {
			createAccount(BMDBCLOUDCM);
			activateAccount(BMDBCLOUDCM);
			checkResult = true;

		} catch (Exception e) {

			logger.info(LoggerType.COMMENT, "Just here");
		} finally {

			logger.info(LoggerType.COMMENT, "Just here" + checkResult);
			GenerateCBResourceListener.addCBResultAsCheckpoint(
					"BM_CLOUDCM_Montly_Account_Provision_Payload",
					"BareMetal Compute", "LCM-Provisioning", "", checkResult);
		}

	}

	@Test(
			dependsOnMethods = { "Create_BMDB_CLOUDCM_Account" })
	public void Terminate_Create_BMDB_CLOUDCM_Account() {
		softTerminateAccount(BMDBCLOUDCM);
	}

	@Test
	public void Terminate_Existing_BMDB_CLOUDCM_Account() {

		String existingDomainName = Constants.P_FILE.getProperties()
				.getProperty("opc.terminate.existing.cloudcm");

		BMDBCLOUDCM.getSummary().setDomainName(existingDomainName);
		BMDBCLOUDCM.getSummary().setServiceAccount(existingDomainName);
		BMDBCLOUDCM.setAlreadyFirstLogin(true);

		softTerminateAccount(BMDBCLOUDCM);
	}

	// checkServiceStatus(MYACCOUNT, service, ServiceStatus.ACTIVE,
	// CloudportalDomain
	// .getDomainURL(CloudportalDomain.MYACCOUNT)
	// + Constants.MYACCOUNT_DASHBOARD);
	// if (!service.alreadyFirstLogin() && !withinDomain) {
	// logger.info(LoggerType.CHECKPOINT,
	// "Check the service welcome email from TAS exists and get temporary
	// password");
	// AutomationPageFactory.getInstance()
	// .getPage(service, CheckEmailPage.class)
	// .checkEmail(TimeUnit.SECONDS, 10l);

	/**
	 * For Metered BM Services
	 */

	@Test
	public void Create_BMCompute_MBE_Account() {

		createAccount(BMComputeMBE);
		activateAccount(BMComputeMBE);

	}

	@Test(
			dependsOnMethods = { "Create_BMCompute_MBE_Account" })
	public void Terminate_BareMetal_IAAS_MBE_Account() {

		softTerminateAccount(BMComputeMBE);
	}

	@Test
	public void Create_BMDB_MBE_Account() {
		createAccount(BMDBMBE);
		activateAccount(BMDBMBE);

	}

	@Test(
			dependsOnMethods = { "Create_BMDB_MBE_Account" })

	public void Create_BMExadata_MBE_Account() {
		BMExadataMBE = new CloudService(new ServiceSummary(),
				CloudServiceType.BMEXAAC, QuoteType.PREPAID, AccountInformation
						.getConstantInstance(), new CloudServiceType[] {
								CloudServiceType.BMEXAAC }, null);

		BMExadataMBE.setAccountInfo(BMDBMBE.getAccountInfo());
		BMExadataMBE.setQuotaPlan(ServiceQuotaPlan.BMEXAMBE);
		BMExadataMBE.setAlreadyFirstLogin(true);
		String existingDomain = BMDBMBE.getSummary().getDomainName();

		CreateAction action = new CreateAction();
		Map<String, String> params = new HashMap<String, String>();

		params.put("OPC_ACCOUNT_NAME", existingDomain);
		action.execute(BMExadataMBE, new PayloadModifier() {
			@Override
			public void modify(PLIORDER order,
					Map<String, String> changedItems) {
				order.getProperties().setItem("OPC_ACCOUNT_NAME", changedItems
						.get("OPC_ACCOUNT_NAME"));
			}
		}, params);
		logger.info(LoggerType.COMMENT, "TEST COMMENT");

	}

	@Test(
			dependsOnMethods = { "Create_BMDB_MBE_Account" })
	public void Terminate_BareMetal_DB_MBE_Account() {

		softTerminateAccount(BMDBMBE);
	}

	private void createAccount(CloudService service) {

		logger.info(LoggerType.STEP, "[Start] Place an order[" + service
				.getServiceType().name() + ":" + service.getQuotaPlan().name()
				+ "] by payload");
		ServiceAction action = new CreateAction();
		action.execute(service);
		logger.info(LoggerType.COMMENT, "TEST COMMENT" + service.getSummary()
				.getOrderId());
		WebDriverManager.stopDriver();
		WebDriverManager.getDriverInstance();
	}

	// @Test(dependsOnMethods = { "createAccount" })
	private void activateAccount(CloudService service) {
		ServiceAction action = new ActivateAction();
		action.execute(service);
	}

	// @Test(dependsOnMethods = { "Create_BareMetal_IAAS_MBE_Account" })
	private void softTerminateAccount(CloudService service) {
		ServiceAction action = new SoftTerminateAction();
		action.execute(service);
	}

}
