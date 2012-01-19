package com.abc.hadoop;

public class WarrantyAuditFileErrMsg {

	private Integer FileErrMsgID;
	private String strERR_MSG;
	private Integer Audit_Warranty_ID;
	private WarrantyAuditResponse AuditResponse;

	public Integer getFileErrMsgID() {
		return FileErrMsgID;
	}
	public String getStrERR_MSG() {
		return strERR_MSG;
	}
	public Integer getAudit_Warranty_ID() {
		return Audit_Warranty_ID;
	}
	public WarrantyAuditResponse getAuditResponse() {
		return AuditResponse;
	}
	public void setFileErrMsgID(Integer fileErrMsgID) {
		FileErrMsgID = fileErrMsgID;
	}
	public void setStrERR_MSG(String strERR_MSG) {
		this.strERR_MSG = strERR_MSG;
	}
	public void setAudit_Warranty_ID(Integer audit_Warranty_ID) {
		Audit_Warranty_ID = audit_Warranty_ID;
	}
	public void setAuditResponse(WarrantyAuditResponse auditResponse) {
		AuditResponse = auditResponse;
	}
	public WarrantyAuditFileErrMsg(Integer fileErrMsgID, String strERR_MSG,
			Integer audit_Warranty_ID, WarrantyAuditResponse auditResponse) {
		super();
		FileErrMsgID = fileErrMsgID;
		this.strERR_MSG = strERR_MSG;
		Audit_Warranty_ID = audit_Warranty_ID;
		AuditResponse = auditResponse;
	}
	public WarrantyAuditFileErrMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
