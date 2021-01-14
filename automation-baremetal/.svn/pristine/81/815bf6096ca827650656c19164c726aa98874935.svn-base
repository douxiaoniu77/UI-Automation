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
public enum dashboardMyAccountEntry {
	
	SUBSCRIPTION("Subscription"), DATA_JURISDICTION("Data Jurisdiction"), IDENTITY_DOMAIN(
				"Identity Domain Name:"), CLOUD_SERVICE_ACCOUNT("Cloud Services Account"), CATEGORY(
				"Category");
		private static Map<String, dashboardMyAccountEntry> map = new HashMap<String, dashboardMyAccountEntry>();
		static {
			for (dashboardMyAccountEntry t : dashboardMyAccountEntry.values()) {
				map.put(t.name(), t);
			}
		}
		private String labelName;

		dashboardMyAccountEntry(String labelName) {
			this.labelName = labelName;
		}

		public String getLabelName() {
			return this.labelName;
		}
	}

