/**

 * automation-baremetal
 * dashboardMyAccountEntry.java
 * 2017年2月19日
 
 */
package com.oracle.opc.automation.test.entity.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueniu
 *
 */
public enum bmComputeBillingMetrics {

	PIC_COMPUTE_X5_STANDARD("Standard Compute (OCPU Hours)"), PIC_COMPUTE_X5_NVME_12_8(
			"High I/O Compute (OCPU Hours)"), PIC_COMPUTE_X5_NVME_28_8(
					"Dense I/O Compute (OCPU Hours)"), PIC_COMPUTE_VM_STANDARD(
							"Virtual Machine (OCPU Hours)"), PIC_COMPUTE_WINDOWS_OS(
									"Windows OS on Compute (OCPU Hours)"), PIC_BLOCK_STORAGE_STANDARD(
											"Block Volumes (GB Months)"), PIC_OBJECT_STORAGE(
													"Object Storage - Storage (GB Months)"), PIC_OBJECT_STORAGE_REQUEST(
															"Object Storage - Requests (10K Requests)"), PIC_LB_SMALL(
																	"Load Balancer 100 Mbps (LB Hours)"), PIC_LB_MEDIUM(
																			"Load Balancer 400 Mbps (LB Hours)"), PIC_LB_LARGE(
																					"Load Balancer 8000 Mbps (LB Hours)"), PIC_COMPUTE_OUTBOUND_DATA_TRANSFER(
																							"Outbound Data Transfer (GB Months)"), PIC_COMPUTE_FASTCONNECT_LARGE(
																									"FastConnect 10 Gbps (Port Hours)"), PIC_COMPUTE_FASTCONNECT_SMALL(
																											"FastConnect 1 Gbps (Port Hours)"), PIC_COMPUTE_VM_DENSEIO(
																													"Virtual Machine Dense I/O (OCPU Hours)");

	private String metricLabel;

	bmComputeBillingMetrics(String metricLabel) {
		this.metricLabel = metricLabel;

	}

	private static Map<String, bmComputeBillingMetrics> map = new HashMap<String, bmComputeBillingMetrics>();
	static {
		for (bmComputeBillingMetrics m : bmComputeBillingMetrics.values()) {
			map.put(m.name().toUpperCase(), m);
		}
	}

	public static String getMetricLabel(String labelName) {
		if (map.containsKey(labelName.toUpperCase())) {
			return map.get(labelName.toUpperCase()).getMetricLabel();
		}
		return null;
	}

	public String getMetricLabel() {
		return this.metricLabel;
	}
}
