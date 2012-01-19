package com.abc.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class WarrantyAuditDataCount implements Writable {

	
	private long longTIME=0;
	private String Res_SerialNumber = "";
	private String Res_CoverageTypeCode = "";
	private String Res_Assembly_ServicePartNumber = "";
	private int count=0;
	private String strTimeList = "";
	private String strGUIDList = "";
	private String strConsumerAppIDList="";
	private String strCoverageTypeCodeList="";
	
	@Override
	public void write(DataOutput out) throws IOException {
		WritableUtils.writeVLong(out, longTIME);
		WritableUtils.writeString(out, Res_SerialNumber);
		WritableUtils.writeString(out, Res_CoverageTypeCode);
		WritableUtils.writeString(out, Res_Assembly_ServicePartNumber);
		
		WritableUtils.writeVInt(out, count);
		WritableUtils.writeString(out, strTimeList);
		WritableUtils.writeString(out, strGUIDList);
		WritableUtils.writeString(out, strConsumerAppIDList);
		WritableUtils.writeString(out, strCoverageTypeCodeList);
		
		
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		 longTIME = WritableUtils.readVLong(in);
		 Res_SerialNumber = WritableUtils.readString(in);
		 Res_CoverageTypeCode = WritableUtils.readString(in);
		 Res_Assembly_ServicePartNumber = WritableUtils.readString(in);
		 
		 count = WritableUtils.readVInt(in) ;
		 strTimeList = WritableUtils.readString(in);
		 strGUIDList = WritableUtils.readString(in);
		 strConsumerAppIDList = WritableUtils.readString(in);
		 strCoverageTypeCodeList = WritableUtils.readString(in);
	}
	
	
	public WarrantyAuditDataCount() {
	}

	public WarrantyAuditDataCount(long longTIME, String res_SerialNumber,
			String res_CoverageTypeCode, 
			String res_Assembly_ServicePartNumber, 
			String GUID,
			String ConsumerAppID,
			String CoverageTypeCode) {
		
		this.longTIME = ( longTIME) ;
		this.Res_SerialNumber =  ( res_SerialNumber ) ;
		this.Res_CoverageTypeCode = ( res_CoverageTypeCode ) ;
		this.Res_Assembly_ServicePartNumber = ( res_Assembly_ServicePartNumber ) ;
		this.count = 1;
		this.strTimeList=""+longTIME;
		this.strGUIDList=GUID;
		this.strConsumerAppIDList = ConsumerAppID;
		this.strCoverageTypeCodeList = CoverageTypeCode;
	}

//	 @Override
//	 public int hashCode() {
//		 return longTIME.hashCode() +  Res_SerialNumber.hashCode() + Res_CoverageTypeCode.hashCode() + Res_Assembly_ServicePartNumber.hashCode();
//	 }

	 
	 
//	 @Override
//	public int compareTo (WarrantyAuditDataCount obj) {
//		 int cmp = longTIME.compareTo(obj.longTIME); 
//		 if (cmp != 0) {
//			 return cmp;
//		 }
//		 cmp = Res_SerialNumber.compareTo(obj.Res_SerialNumber);
//		 if (cmp != 0) {
//			 return cmp;
//		 }
//		 cmp = Res_CoverageTypeCode.compareTo(obj.Res_CoverageTypeCode);
//		 if (cmp != 0) {
//			 return cmp;
//		 }
//		 return  Res_Assembly_ServicePartNumber.compareTo(obj.Res_Assembly_ServicePartNumber);
//	}
	 

	@Override
	public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("<WarrantyAuditDataCount>")
					.append("<TIME>").append(longTIME).append("</TIME>")
					.append("<SerialNumber>").append(Res_SerialNumber).append("</SerialNumber>")
					.append("<CoverageTypeCode>").append(Res_CoverageTypeCode).append("</CoverageTypeCode>")
					.append("<ServicePartNumber>").append(Res_Assembly_ServicePartNumber).append("</ServicePartNumber>")
					.append("<Count>").append(count).append("</Count>")
					.append("<TimeList>").append(strTimeList).append("</TimeList>")
					.append("<GUIDList>").append(strGUIDList).append("</GUIDList>")
					.append("<ConsumerAppIDList>").append(strConsumerAppIDList).append("</ConsumerAppIDList>")
					.append("<CoverageTypeCodeList>").append(strCoverageTypeCodeList).append("</CoverageTypeCodeList>")
					.append("</WarrantyAuditDataCount>");
			return builder.toString();
	}
	
	private String getValue(String inputStr, String beginTag, String endTag) {
		String value = null;
		int idxStart = inputStr.indexOf(beginTag) + beginTag.length();
		int idxEnd = inputStr.indexOf(endTag);
		if (idxStart>=0 && idxEnd>=idxStart) {
			value = inputStr.substring(idxStart, idxEnd);
		}
		if (value.compareTo("null")==0) value = null;
		
		return value;
	}
	
	
	public void parseString (String strLine) {
		int idx = strLine.indexOf("<WarrantyAuditDataCount>");
		if (idx>=0) {
			String strTime = getValue(strLine, "<TIME>", "</TIME>");
			String SerialNumber = getValue(strLine, "<SerialNumber>", "</SerialNumber>");
			String CoverageTypeCode = getValue(strLine, "<CoverageTypeCode>", "</CoverageTypeCode>");
			String ServicePartNumber = getValue(strLine, "<ServicePartNumber>", "</ServicePartNumber>");
			String Count = getValue(strLine, "<Count>", "</Count>");
			String TimeList = getValue(strLine, "<TimeList>", "</TimeList>");
			String GUIDList = getValue(strLine, "<GUIDList>", "</GUIDList>");
			String ConsumerAppIDList = getValue(strLine, "<ConsumerAppIDList>", "</ConsumerAppIDList>");
			String CoverageTypeCodeList = getValue(strLine, "<CoverageTypeCodeList>", "</CoverageTypeCodeList>");
			
			this.setLongTIME(Long.parseLong(strTime));
			this.setRes_SerialNumber(SerialNumber);
			this.setRes_CoverageTypeCode(CoverageTypeCode);
			this.setRes_Assembly_ServicePartNumber(ServicePartNumber);
			this.setCount(Integer.parseInt(Count));
			this.setStrTimeList(TimeList);
			this.setStrGUIDList(GUIDList);
			this.setStrConsumerAppIDList(ConsumerAppIDList);
			this.setStrCoverageTypeCodeList(CoverageTypeCodeList);
			
			
		}
	}
	

	
	
	public long getLongTIME() {
		return longTIME;
	}

	public void setLongTIME(long longTIME) {
		this.longTIME = longTIME;
	}

	public String getRes_SerialNumber() {
		return Res_SerialNumber;
	}

	public void setRes_SerialNumber(String res_SerialNumber) {
		Res_SerialNumber = res_SerialNumber;
	}

	public String getRes_CoverageTypeCode() {
		return Res_CoverageTypeCode;
	}

	public void setRes_CoverageTypeCode(String res_CoverageTypeCode) {
		Res_CoverageTypeCode = res_CoverageTypeCode;
	}

	public String getRes_Assembly_ServicePartNumber() {
		return Res_Assembly_ServicePartNumber;
	}

	public void setRes_Assembly_ServicePartNumber(
			String res_Assembly_ServicePartNumber) {
		Res_Assembly_ServicePartNumber = res_Assembly_ServicePartNumber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addTimeList(String strTime) {
		if (strTimeList==null || strTimeList.length()==0) {
			strTimeList = strTime;
		}
		else {
			strTimeList += "," + strTime;
		}
	}
	public void addUUIDList(String strGUID) {
		if (strGUIDList==null || strGUIDList.length()==0) {
			strGUIDList = strGUID;
		}
		else {
			strGUIDList += "," + strGUID;
		}
	}

	
	public String getStrTimeList() {
		return strTimeList;
	}

	public void setStrTimeList(String strTimeList) {
		this.strTimeList = strTimeList;
	}

	public String getStrGUIDList() {
		return strGUIDList;
	}

	public void setStrGUIDList(String strGUIDList) {
		this.strGUIDList = strGUIDList;
	}

	
	public String getStrConsumerAppIDList() {
		return strConsumerAppIDList;
	}

	public void setStrConsumerAppIDList(String strConsumerAppIDList) {
		this.strConsumerAppIDList = strConsumerAppIDList;
	}

	
	public String getStrCoverageTypeCodeList() {
		return strCoverageTypeCodeList;
	}

	public void setStrCoverageTypeCodeList(String strCoverageTypeCodeList) {
		this.strCoverageTypeCodeList = strCoverageTypeCodeList;
	}
	
	

	public static void main(String[] args) throws Exception {

		 WarrantyAuditDataCount obj = new WarrantyAuditDataCount(10100101, "Serial1111Number",
					"CT01", "PART111", "GUID", "149", "00");
		 
		 System.out.println(obj);
		 
		 WarrantyAuditDataCount p = new WarrantyAuditDataCount();
		 p.parseString(obj.toString() );
		 System.out.println(p);
		 
		 String line = "NA	<WarrantyAuditDataCount><TIME>1320094700619</TIME><SerialNumber>NA</SerialNumber><CoverageTypeCode>null</CoverageTypeCode><ServicePartNumber>null</ServicePartNumber><Count>5</Count><TimeList>1320094617302,1320094393628,1320094188305,1320094388389,1320094700619</TimeList><GUIDList>80e12b24-ff55-410a-adca-0546d30e67c6,7210088d-b62a-4748-a428-554607ff9a56,7246627d-4cc5-413c-aac6-36cb4b9bddb5,5a51d761-b0d8-4424-84e5-8643ab729a92,e6356693-7afb-4285-b8cc-96e828dc957e</GUIDList><ConsumerAppIDList>1085,1085,1085,1085,1085</ConsumerAppIDList><CoverageTypeCodeList>null,null,null,null,null</CoverageTypeCodeList></WarrantyAuditDataCount>";
		 List<WarrantyAuditCompareI> list = ObjectFactory.getWarrantyAuditCompareList(line);
		 for (WarrantyAuditCompareI objCompare : list) {
			 System.out.println(objCompare);
		 }
		 
		WarrantyDataCountedI criteriaRepeating10SecWithSameCoverage[] = new WarrantyDataCountedI[2];
		criteriaRepeating10SecWithSameCoverage[0] = new WarrantyDataCompareTime(10);
		criteriaRepeating10SecWithSameCoverage[1] = new WarrantyDataCompareCoverageTypeSame();

			
		List<WarrantyAuditCompareI> returnList10SecWithSameCoverage = ObjectFactory.getWarrantyAuditCompareListMeetingCondition(list, criteriaRepeating10SecWithSameCoverage );

		 
		 System.out.println("--------------- 10 sec plus --------------- "); 
		for (WarrantyAuditCompareI objReturn : returnList10SecWithSameCoverage) {
			 System.out.println(objReturn);
		}
		 
		 
	}
	
}


