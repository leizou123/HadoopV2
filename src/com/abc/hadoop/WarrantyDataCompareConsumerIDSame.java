package com.abc.hadoop;

public class WarrantyDataCompareConsumerIDSame implements WarrantyDataCountedI {
	private String strConsumerAppID = null;
	
	public boolean isCounted (WarrantyAuditCompareI a, WarrantyAuditCompareI b) {
		boolean counted = false;

		if (a==null || b==null)
			return false;
		
		if ( a.getConsumerAppID () == null || b.getConsumerAppID()==null  ) 
			return false;
		
		if ( a.getConsumerAppID().equals(b.getConsumerAppID()) ) 
			counted = true;

		if (strConsumerAppID!=null) {
			counted = counted && strConsumerAppID.equals(a.getConsumerAppID()); 
		}
		return counted;
	}
	
	public WarrantyDataCompareConsumerIDSame() {
		
	}

	public WarrantyDataCompareConsumerIDSame(String strConsumerAppID) {
		super();
		this.strConsumerAppID = strConsumerAppID;
	}

}
