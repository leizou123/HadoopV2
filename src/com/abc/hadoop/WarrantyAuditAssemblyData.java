package com.abc.hadoop;

import java.io.Serializable;
import java.util.Date;

public class WarrantyAuditAssemblyData implements Comparable<WarrantyAuditAssemblyData>, Serializable {

	private int AssemblyID;
	private String Assembly_ServicePartNumber;
	private String Assembly_CoverageReferenceNumber;
	private Date Assembly_CoverageStartDate;
	private Date Assembly_CoverageEndDate;
	private String Assembly_LaborCoverage;
	private String Assembly_PartCoverage;
	
	
	 /**
	  * @param aThat is a non-null Account.
	  *
	  * @throws NullPointerException if aThat is null.
	  */
	public int compareTo( WarrantyAuditAssemblyData aThat ) {
		if ( this == aThat ) return 0;
		return Assembly_ServicePartNumber.compareTo(aThat.getAssembly_ServicePartNumber());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Assembly_ServicePartNumber == null) ? 0 : Assembly_ServicePartNumber.hashCode());
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
		WarrantyAuditAssemblyData other = (WarrantyAuditAssemblyData) obj;
		if (Assembly_ServicePartNumber == null) {
			if (other.Assembly_ServicePartNumber != null)
				return false;
		} else if (!Assembly_ServicePartNumber.equals(other.Assembly_ServicePartNumber))
			return false;
		return true;
	}

	
	
	public WarrantyAuditAssemblyData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getAssembly_ServicePartNumber() {
		return Assembly_ServicePartNumber;
	}
	public String getAssembly_CoverageReferenceNumber() {
		return Assembly_CoverageReferenceNumber;
	}
	public Date getAssembly_CoverageStartDate() {
		return Assembly_CoverageStartDate;
	}
	public Date getAssembly_CoverageEndDate() {
		return Assembly_CoverageEndDate;
	}
	public String getAssembly_LaborCoverage() {
		return Assembly_LaborCoverage;
	}
	public String getAssembly_PartCoverage() {
		return Assembly_PartCoverage;
	}
	public void setAssembly_ServicePartNumber(String assembly_ServicePartNumber) {
		Assembly_ServicePartNumber = assembly_ServicePartNumber;
	}
	public void setAssembly_CoverageReferenceNumber(
			String assembly_CoverageReferenceNumber) {
		Assembly_CoverageReferenceNumber = assembly_CoverageReferenceNumber;
	}
	public void setAssembly_CoverageStartDate(Date assembly_CoverageStartDate) {
		Assembly_CoverageStartDate = assembly_CoverageStartDate;
	}
	public void setAssembly_CoverageEndDate(Date assembly_CoverageEndDate) {
		Assembly_CoverageEndDate = assembly_CoverageEndDate;
	}
	public void setAssembly_LaborCoverage(String assembly_LaborCoverage) {
		Assembly_LaborCoverage = assembly_LaborCoverage;
	}
	public void setAssembly_PartCoverage(String assembly_PartCoverage) {
		Assembly_PartCoverage = assembly_PartCoverage;
	}

	public int getAssemblyID() {
		return AssemblyID;
	}

	public void setAssemblyID(int assemblyID) {
		AssemblyID = assemblyID;
	}
	

}
