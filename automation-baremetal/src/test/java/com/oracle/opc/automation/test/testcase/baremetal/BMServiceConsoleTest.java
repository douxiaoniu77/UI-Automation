/**

 * automation-baremetal
 * BMCSTest.java   for Bare Metal Compute Service Sanity Check 
 * 2016年10月19日
 
 */

package com.oracle.opc.automation.test.testcase.baremetal;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oracle.opc.automation.common.Constants;
import com.oracle.opc.automation.common.annotation.CBMetaData;
import com.oracle.opc.automation.common.annotation.NeedCloseDriver;
import com.oracle.opc.automation.common.log.AutomationLogger;
import com.oracle.opc.automation.common.utils.ActionUtils;
import com.oracle.opc.automation.entity.CloudService;
import com.oracle.opc.automation.test.BaseTest;
import com.oracle.opc.automation.test.component.actions.BMConsoleAction;
import com.oracle.opc.automation.test.component.factory.AutomationActionFactory;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateComputeAction;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateDatabaseAction;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateNetworkingAction;
import com.oracle.opc.automation.test.testcase.baremetal.action.BMCreateTerminateStorageAction;

/**
 * @author xueniu
 *
 */

public class BMServiceConsoleTest extends BaseTest {

	private CloudService bm;
	String shape;
	boolean byol;
	CloudService bareMetal;

	AutomationLogger logger = new AutomationLogger(BMAccounLifecycle2.class);

	public BMServiceConsoleTest() {

		shape = Constants.P_FILE.getProperties().getProperty("shape");
		byol = Boolean.parseBoolean(
				Constants.P_FILE.getProperties().getProperty("byol"));

		System.setProperty("proxy.type", "MANUAL");
		System.setProperty("proxy.http", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.ssl", "www-proxy-hqdc.us.oracle.com:80");
		System.setProperty("proxy.noproxy", "*.oracleiaas.com*");
	}

	@Test
	public void BM_Create_Database_Instance()
			throws InterruptedException, IOException {
		this.BM_Create_Database_Instance(shape, byol);
	}

	public void BM_Create_Database_Instance(String shape, boolean byol)
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateDatabaseAction.class)
				.createDatabaseInstance(shape, byol);
	}

	@Test
	public void BM_ScaleUp_DB_Instance() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateDatabaseAction.class)
				.scaleUpDatabaseInstance();
	}

	@Test
	public void BM_ScaleDown_DB_Instance() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateDatabaseAction.class)
				.scaleDownDatabaseInstance();
	}

	@Test
	public void BM_Create_Compute_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateComputeAction.class)
				.createComputeInstance(shape);

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
	public void BM_Create_VCN_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.createVCNInstance();
	}

	@Test
	public void BM_Terminate_VCN_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.terminateVCNInstance();
	}

	@Test
	public void BM_Create_LoadBalancer_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.createLBInstance();
	}

	@Test
	public void BM_Terminate_LoadBalancer_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateNetworkingAction.class)
				.terminateLBInstance();
	}

	/**
	 * @author xueniu
	 * @Date: 2017/11/17
	 * @throws InterruptedException
	 */

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "Service Functional Sanity", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_ServiceConsole_Login")
	public void BM_ServiceConsole_Login() throws InterruptedException {

		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class).loginBMConsole();

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	@CBMetaData(component = "Service Functional Sanity", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_ServiceConsole_Login")
	public void BM_Federation_Login() throws InterruptedException {
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.loginBMFederation();
	}

	@Test(dependsOnMethods = { "BM_ServiceConsole_Login" })
	@CBMetaData(component = "Service Functional Sanity", product = "BareMetal Compute", subComponent = "", testCaseNameInCB = "BM_ServiceConsole_Logout")
	public void BM_ServiceConsole_Logout() throws InterruptedException {

		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class).logoutBMConsole();

	}

	@Test
	@NeedCloseDriver(isNeeded = false)
	public void BM_Email_Add_ApprovedSender() throws InterruptedException {

		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class).loginBMConsole();
		// To add Sender
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.addApprovedSenders();

		ActionUtils.refreshPage();

	}

	@Test(dependsOnMethods = { "BM_Email_Add_ApprovedSender" })
	public void BM_Email_Terminate_ApprovedSender()
			throws InterruptedException {

		// To add Sender
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.terminateApprovedSenders();

	}

	@Test
	public void BM_Email_SMTP_Configuration() throws InterruptedException {

		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class).loginBMConsole();

		// To add SMTP Credentials
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.configureEmailSMTP();

	}

	@Test
	@Parameters("ADname")
	@NeedCloseDriver(isNeeded = false)
	public void BM_Create_Block_Instance(String ADname)
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Create_BlockVolume(ADname);
	}

	@Test
	@NeedCloseDriver(isNeeded = false)

	public void BM_Create_ObjectStorage_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Create_ObjectStorage();
	}

	@Test(dependsOnMethods = { "BM_Create_ObjectStorage_Instance" })
	@NeedCloseDriver(isNeeded = false)
	public void BM_Upload_File() throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Upload_Object();
	}

	@Test(dependsOnMethods = { "BM_Upload_File" })
	@NeedCloseDriver(isNeeded = false)
	public void BM_Download_File() throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Download_Object();
	}

	@Test(dependsOnMethods = { "BM_Upload_File" })
	@NeedCloseDriver(isNeeded = false)
	public void BM_Delete_File() throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Delete_Object();
	}

	@Test(dependsOnMethods = { "BM_Create_Block_Instance" })
	public void BM_Terminate_Block_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.BM_Terminate_Block_Instance();
	}

	@Test(dependsOnMethods = { "BM_Upload_File" })
	public void BM_Terminate_ObjectStorage_Instance()
			throws InterruptedException, IOException {
		AutomationActionFactory.getInstance()
				.getAction(bm, BMCreateTerminateStorageAction.class)
				.terminateObjectStorageInstance();
	}

	@Test
	public void BM_Email_SEND_VIA_SWAKS()
			throws InterruptedException, IOException {

		// To add SMTP Credentials
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.sendEmailViaSwaks();

	}

	@Test
	public void BM_Upload_PublicKey() throws InterruptedException, IOException {

		// To add SMTP Credentials
		// AutomationActionFactory.getInstance().getAction(bareMetal,
		// BMConsoleAction.class).getKey();
		AutomationActionFactory.getInstance()
				.getAction(bareMetal, BMConsoleAction.class)
				.uploadPublicKeyUser();

	}
}
