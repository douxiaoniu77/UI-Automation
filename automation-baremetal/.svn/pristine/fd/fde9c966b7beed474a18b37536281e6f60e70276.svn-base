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

public enum bmDBBillingMetrics {

	PIC_DATABASE_STANDARD_HIGH_IO("Database Standard High I/O Base (Database Hours)"), PIC_DATABASE_STANDARD_DENSE_IO(
			"Database Standard Dense I/O Base (Database Hours)"), PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY(
					"Dense I/O Compute (OCPU Hours)"), PIC_DATABASE_ENTERPRISE_HIGH_IO(
							"Database Enterprise High I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_DENSE_IO(
									"Database Enterprise Dense I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY(
											"Database Enterprise Added CPUs (OCPU Hours)"), PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO(
													"Database Enterprise High Performance High I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO(
															"Database Enterprise High Performance Dense I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY(
																	"Database Enterprise Extreme Performance Added CPUs (OCPU Hours)"), PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO(
																			"Database Enterprise Extreme Performance High I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO(
																					"Database Enterprise Extreme Performance Dense I/O Base (Database Hours)"), PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY(
																							"Database Enterprise Extreme Performance Added CPUs (OCPU Hours)");

	private String metricLabel;

	bmDBBillingMetrics(String metricLabel) {
		this.metricLabel = metricLabel;

	}

	private static Map<String, bmDBBillingMetrics> map = new HashMap<String, bmDBBillingMetrics>();
	static {
		for (bmDBBillingMetrics m : bmDBBillingMetrics.values()) {
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
