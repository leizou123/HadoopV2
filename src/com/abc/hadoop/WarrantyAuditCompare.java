package com.abc.hadoop;

public class WarrantyAuditCompare implements WarrantyAuditCompareI {

	private long time;
	private String servicePartNumber;
	private String coverageTypeCode;
	private String serialNumber;
	private String consumerAppID;
	private String guidID;

	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getServicePartNumber() {
		return servicePartNumber;
	}
	public void setServicePartNumber(String servicePartNumber) {
		this.servicePartNumber = servicePartNumber;
	}
	public String getCoverageTypeCode() {
		return coverageTypeCode;
	}
	public void setCoverageTypeCode(String coverageTypeCode) {
		this.coverageTypeCode = coverageTypeCode;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getConsumerAppID() {
		return consumerAppID;
	}
	public void setConsumerAppID(String consumerAppID) {
		this.consumerAppID = consumerAppID;
	}
	public String getGuidID() {
		return guidID;
	}
	public void setGuidID(String guidID) {
		this.guidID = guidID;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WarrantyAuditCompare [time=").append(time)
				.append(", servicePartNumber=").append(servicePartNumber)
				.append(", coverageTypeCode=").append(coverageTypeCode)
				.append(", serialNumber=").append(serialNumber)
				.append(", consumerAppID=").append(consumerAppID)
				.append(", guidID=").append(guidID).append("]");
		return builder.toString();
	}
	
	
	
}
