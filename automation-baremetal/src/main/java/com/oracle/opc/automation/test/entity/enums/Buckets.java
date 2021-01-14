package com.oracle.opc.automation.test.entity.enums;

import com.oracle.opc.automation.entity.enums.CloudServiceType;

public enum Buckets {

	// Metering Service

	BMC("IaaS", CloudServiceType.BMCSAC), //

	BMDB("Database", CloudServiceType.BMDBAC);//
	/** ***************Bare Metal Part End*************** **/

	private String bucketName;
	private CloudServiceType type;

	Buckets(String bucketName, CloudServiceType type) {
		this.setBucketName(bucketName);
		this.setType(type);
	}

	public static Buckets getBuckets(String t) {
		for (Buckets type : Buckets.values()) {
			if (t.equalsIgnoreCase(type.name())) {
				return type;
			}
		}
		return null;
	}

	public CloudServiceType getType() {
		return type;
	}

	public void setType(CloudServiceType type) {
		this.type = type;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

}