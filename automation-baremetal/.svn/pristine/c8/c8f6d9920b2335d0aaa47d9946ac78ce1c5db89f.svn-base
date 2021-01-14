/**

 * automation-baremetal
 * dashboardMyAccountEntry.java
 * 2017\u5e742\u670819\u65e5
 
 */
package com.oracle.opc.automation.test.entity.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ulises
 *
 */

public enum bmExadataBillingMetrics {

	PIC_EXADATA_QUARTER_RACK_X6("Exadata Quarter Rack X6 (Hosted Environment Months)"), PIC_EXADATA_HALF_RACK_X6(
			"Exadata Half Rack X6 (Hosted Environment Months)"), PIC_EXADATA_FULL_RACK_X6(
					"Exadata Full Rack X6 (Hosted Environment Months)"), PIC_EXADATA_ADDITIONAL_CAPACITY_X6_MONTHLY(
							"Exadata Added CPUs (Monthly) (OCPU Months)"), PIC_EXADATA_ADDITIONAL_CAPACITY_X6_HOURLY(
									"Exadata Added CPUs (Hourly) (OCPU Hours)");

	private String metricLabel;

	bmExadataBillingMetrics(String metricLabel) {
		this.metricLabel = metricLabel;

	}

	private static Map<String, bmExadataBillingMetrics> map = new HashMap<String, bmExadataBillingMetrics>();
	static {
		for (bmExadataBillingMetrics m : bmExadataBillingMetrics.values()) {
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
