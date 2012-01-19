package com.abc.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectFactory {
	
	private static final Map<String, Object> objectMap = new HashMap<String, Object>();
	
//	public static ErrorCategoryDataLoader getErrorCategoryDataLoader() {
//		return (ErrorCategoryDataLoader)getObject("errorcat.loader", "com.apple.ist.est.esp.tool.alert.loader.dummy.ErrorCategoryStoreDataLoaderDummy");
//	}
//	
//	public static SupportInfoDataLoader getSupportInfoDataLoader() {
//		return (SupportInfoDataLoader)getObject("supportinfo.loader", "com.apple.ist.est.esp.tool.alert.loader.dummy.SupportInfoDataLoaderDummy");
//	}
//	
	public static WarrantyAuditDataCount getWarrantyAuditDataCount(String strLine) {
		WarrantyAuditDataCount obj = new WarrantyAuditDataCount();
		obj.parseString(strLine);
		return obj;
	}

	public static List<WarrantyAuditCompareI> getWarrantyAuditCompareListMeetingCondition(List<WarrantyAuditCompareI> listObj, WarrantyDataCountedI compare ) {
		 WarrantyDataCountedI criteria[] = new WarrantyDataCountedI[1];
		 criteria[0] = compare;
		 return getWarrantyAuditCompareListMeetingCondition(listObj, criteria );
		 
	}

	public static List<WarrantyAuditCompareI> getWarrantyAuditCompareListMeetingCondition(List<WarrantyAuditCompareI> listObj, WarrantyDataCountedI compare[] ) {
		List<WarrantyAuditCompareI> returnList = new ArrayList<WarrantyAuditCompareI>();
		if (listObj!=null) {
			for ( int i=0; i<listObj.size(); i++) {
				for ( int j=0; j<listObj.size(); j++) {
					if (j!=i) {
						boolean counted = compare[0].isCounted(listObj.get(i), listObj.get(j));
						for (int k=1; k<compare.length; k++ ) {
							counted = counted && compare[k].isCounted(listObj.get(i), listObj.get(j));
						}
						if ( counted ) {
							returnList.add(listObj.get(i));
							break;
						}
					}
				}
			}
		}
		return returnList;
	}
	
	public static List<WarrantyAuditCompareI> getWarrantyAuditCompareList(String strLine) {
		
		List<WarrantyAuditCompareI> list = new ArrayList<WarrantyAuditCompareI>();

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
			
			if (TimeList!=null && GUIDList!=null && ConsumerAppIDList!=null) {
				String TimeArray[] = TimeList.split(",");
				String GUIDArray[] = GUIDList.split(",");
				String ConsumerAppIDArray[] = ConsumerAppIDList.split(",");
				String CoverageTypeCodeArray[] = CoverageTypeCodeList.split(",");
				
				if (TimeArray!=null && GUIDArray!=null && ConsumerAppIDArray!=null &&  CoverageTypeCodeArray!=null 
						&& TimeArray.length==GUIDArray.length 
						&& GUIDArray.length == ConsumerAppIDArray.length
						&& ConsumerAppIDArray.length == CoverageTypeCodeArray.length  ) {
					
					for (int i=0; i<TimeArray.length; i++) {
						WarrantyAuditCompare obj = new WarrantyAuditCompare();
						
						obj.setSerialNumber( checkNull(SerialNumber) );
						obj.setTime( Long.parseLong(TimeArray[i]) );
						obj.setGuidID( checkNull(GUIDArray[i]) ) ; 
						obj.setConsumerAppID( checkNull(ConsumerAppIDArray[i]));
						obj.setCoverageTypeCode( checkNull(CoverageTypeCodeArray[i]));
						list.add(obj);
					}
					
				}
			}
		}
	
		
		return list;
	}

	public static String checkNull (String value) {
		if (value==null)	return null;
		if ( value.compareTo("null")==0) { 
			return null;
		}
		else { 
			return value;
		}
		
	}
	private static String getValue(String inputStr, String beginTag, String endTag) {
		String value = null;
		int idxStart = inputStr.indexOf(beginTag) + beginTag.length();
		int idxEnd = inputStr.indexOf(endTag);
		if (idxStart>=0 && idxEnd>=idxStart) {
			value = inputStr.substring(idxStart, idxEnd);
		}
		if (value.compareTo("null")==0) value = null;
		
		return value;
	}

	public static void parseString (String strLine) {}

	
	private static Object getObject(String key, String defaultClass) {
		Object ob = objectMap.get(key);
		if (ob == null) {
			synchronized(objectMap) {
				ob = objectMap.get(key);
				if (ob == null) {
					try {
						ob = createObjectByType(key, defaultClass);
						objectMap.put(key, ob);
					} catch (Exception e) {
						throw new IllegalArgumentException("failed to creating class[" 
								+ key + ", defaultClass[" + defaultClass + "]");
					}
				}
			}
		}
		return ob;
	}
	
	private static Object createObjectByType(String type, String defaultClass) throws Exception {
		String className = WarrantyAuditCountParameter.getInstance().getProperty(type, defaultClass);
		Class<?> clazz = Class.forName(className);
		return clazz.newInstance();	// need to have a default constructor
	}
	
	
}
