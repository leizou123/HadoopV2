package com.abc.hadoop;

public class WarrantyAuditFile {

	private int FileID;
	
	private String	FileName = null;
	private int		FileRecordCount = 0;
	
	
	
	
	public WarrantyAuditFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public WarrantyAuditFile(String fileName) {
		super();
		FileName = fileName;
	}


	public int getFileID() {
		return FileID;
	}
	public String getFileName() {
		return FileName;
	}
	public int getFileRecordCount() {
		return FileRecordCount;
	}
	public void setFileID(int FileID) {
		this.FileID = FileID;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public void setFileRecordCount(int FileRecordCount) {
		this.FileRecordCount = FileRecordCount;
	}
	
	public int incrementFileRecordCount(int add) {
		this.FileRecordCount += add;
		return this.FileRecordCount;
	}
	

}
