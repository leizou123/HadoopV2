package com.abc.hadoop;

public class WarrantyAuditFileInfo implements Comparable<WarrantyAuditFileInfo> {
	private int FileInfoID;
	private String SVC_NAME;
	private String VALUE;
	private int Count;
	private WarrantyAuditFile	AuditFile;
	
	@Override
	public String toString() {
		return "WarrantyAuditFileInfo [SVC_NAME=" + SVC_NAME + ", Count="
				+ Count + ", getSVC_NAME()=" + getSVC_NAME() + ", getCount()="
				+ getCount() + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}
	
	


	public WarrantyAuditFileInfo(String sVC_NAME, String VALUE, int count, WarrantyAuditFile AuditFile) {
		super();
		this.SVC_NAME = sVC_NAME;
		this.VALUE = VALUE;
		this.Count = count;
		this.AuditFile = AuditFile;
	}

	
	public String getVALUE() {
		return VALUE;
	}


	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}


	public String getSVC_NAME() {
		return SVC_NAME;
	}
	public int getCount() {
		return Count;
	}
	public void setSVC_NAME(String sVC_NAME) {
		SVC_NAME = sVC_NAME;
	}
	
	public void setCount(int count) {
		Count = count;
	}

	public WarrantyAuditFile getAuditFile() {
		return AuditFile;
	}


	public void setAuditFile(WarrantyAuditFile auditFile) {
		AuditFile = auditFile;
	}


	public int getFileInfoID() {
		return FileInfoID;
	}


	public void setFileInfoID(int fileInfoID) {
		FileInfoID = fileInfoID;
	}


	public int incrementCount(int add) {
		this.Count += add;
		return this.Count;
	}

	
	 /**
	  * @param aThat is a non-null Account.
	  *
	  * @throws NullPointerException if aThat is null.
	  */
	public int compareTo( WarrantyAuditFileInfo aThat ) {
		if ( this == aThat ) return 0;
		return SVC_NAME.compareTo(aThat.getSVC_NAME());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((SVC_NAME == null) ? 0 : SVC_NAME.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WarrantyAuditFileInfo other = (WarrantyAuditFileInfo) obj;
		if (SVC_NAME == null) {
			if (other.SVC_NAME != null)
				return false;
		} else if (!SVC_NAME.equals(other.SVC_NAME))
			return false;
		return true;
	}
	
}
