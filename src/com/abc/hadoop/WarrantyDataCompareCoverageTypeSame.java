package com.abc.hadoop;

public class WarrantyDataCompareCoverageTypeSame implements WarrantyDataCountedI {

	public boolean isCounted (WarrantyAuditCompareI a, WarrantyAuditCompareI b) {
		boolean counted = false;

		if (a==null || b==null)
			return false;
		
		if ( a.getCoverageTypeCode() == null || b.getCoverageTypeCode() ==null) 
			return false;
		
		if ( a.getCoverageTypeCode().equals(b.getCoverageTypeCode()) ) 
			counted = true;

		return counted;
	}

}
