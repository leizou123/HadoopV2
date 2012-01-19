package com.abc.hadoop;

public class WarrantyDataCompareTime implements WarrantyDataCountedI {
	
	private long diffTime = 0;
	
	
	public boolean isCounted (WarrantyAuditCompareI a, WarrantyAuditCompareI b) {
		boolean counted = false;
		//long diff = (long) ( (double) ((a.getTime()-b.getTime()) / (double) 1000 ) ) ;
		long diff = (long) ( (double)a.getTime()/(double)1000 + 0.5 ) - (long) ( (double)b.getTime()/(double)1000  + 0.5) ;
		
		if ( (diff>=(-diffTime))  && (diff<=diffTime) )
				counted = true;
		return counted;
	}

	
	

	public long getDiffTime() {
		return diffTime;
	}


	public void setDiffTime(long diffTime) {
		this.diffTime = diffTime;
	}


	public WarrantyDataCompareTime(long diffTime) {
		super();
		this.diffTime = diffTime;
	}
	
	
}
