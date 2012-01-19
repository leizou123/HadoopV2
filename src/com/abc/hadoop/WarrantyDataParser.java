package com.abc.hadoop;

import java.io.File;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;





public class WarrantyDataParser {
	//private static Log logger = LogFactory.getLog(WarrantyAuditParser.class);

	
	private static SAXParserFactory spf = SAXParserFactory.newInstance();
	private static SAXParserFactory request = SAXParserFactory.newInstance();

	private static final AtomicInteger counter = new AtomicInteger(0);
	private static final AtomicInteger fileCounter = new AtomicInteger(0);

	private static int flagProcessSSP = 1;
	static{
		String processAll = System.getProperty("PROCESS_SSP");
		if (processAll!=null && processAll.equals("0")) {
			flagProcessSSP = 0;
		}
		
		//System.out.println( "warranty.count.svc.names - " + mainParameter.getProperty("warranty.count.svc.names", "") );
	}

	private static int flagDisplay = 0;
	static{
		String strDisplay = System.getProperty("CONSOLE_DISPLAY");
		if (strDisplay!=null && strDisplay.equals("1")) {
			flagProcessSSP = 1;
		}
	}

	static int addUnhandledTag (String qName, String value, WarrantyAuditFile auditFile, ArrayList<WarrantyAuditFileInfo> listTag) {
		int ret = 1;
		WarrantyAuditFileInfo obj = new WarrantyAuditFileInfo(qName, value, 1, auditFile);
		int idx = listTag.indexOf(obj);
		if (idx<0 ) {
			listTag.add(obj);
		} else {
			ret = listTag.get(idx).incrementCount(1);
		}		
		return ret;
		
	}

	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat[] FORMATS = {
		new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS ': PDT'"), // "2011/05/26 06:23:33.100 : PDT"
		new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS ': GMT'"), // "2011/05/27 01:10:56.772 : GMT"
		new SimpleDateFormat("yyyy-MM-dd"), // "2011-05-27"
		new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS"), // "2011/05/27 01:10:56.772"
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"), //ISO8601 long RFC822 zone
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"), //ISO8601 long long form zone
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), //ignore timezone
        new SimpleDateFormat("yyyyMMddHHmmssZ"), //ISO8601 short
        new SimpleDateFormat("yyyyMMddHHmm"),
        new SimpleDateFormat("yyyyMMdd"), //birthdate from NIST IHE C32 sample
        new SimpleDateFormat("yyyyMM"),
        new SimpleDateFormat("yyyy") //just the year
    };

	public static Date parseDate (String qName, String strDate ) {
	    if (strDate == null) {
	        return null;
	    }
	    if (strDate.length()==0) {
	    	return null;
	    }
	    
	    Date retval = null;
	    for (SimpleDateFormat sdf : FORMATS) {
	        try {
	            sdf.setLenient(false);
	            retval = sdf.parse(strDate);
	            //System.out.println("Date:" + wtf + " hit on pattern:" + sdf.toPattern());
	            break;
	        } catch (ParseException ex) {
	            retval = null;
	            continue;
	        }
	    }
	    if (retval==null) {
			//logger.error("Exception " + "error parsing xml field [" + qName + "] " + " data [" + strDate + "] " + " date [" + retval + "] ");
	    	System.err.println("Exception " + "error parsing xml field [" + qName + "] " + " data [" + strDate + "] " + " date [" + retval + "] ");
	    }
	    
	    return retval;
	}

	static Date parseDate (String qName, String strDate, DateFormat dateFormat) {
		Date date = null;
	
		if (strDate == null) {
			return null;
		}
		
		if (strDate.trim().length()==0) { 
			return null;
		}
		
		try {
			date = dateFormat.parse(strDate.trim());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			e.printStackTrace();
		}

		return date;

	}

	
	
	private static final Map<String, String> warrantySVCNameMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 506781620440088835L;
		{
			String svcname = System.getProperty("SVC_NAME");
			if (svcname!=null) {
				String[] tokens = svcname.split(",");
			
				for (String obj : tokens) {
					put(obj, obj);
					//logger.info("SVC NAME for Warranty, [" + obj + "]");
					System.out.println("SVC NAME for Warranty, [" + obj + "]");
				}
			} else {
				put("Warranty", "Warranty");
			}
		}
	};
	

	
	static List <RequestResponsePair<String,String>> unhandledRequestResponseList = new ArrayList <RequestResponsePair<String,String>>(){
		private static final long serialVersionUID = 506781620440088835L;
		{
			add(new RequestResponsePair<String, String>("FetchDiagnosticEventNumbers", "FetchDiagnosticEventNumbersResponse") );
			add(new RequestResponsePair<String, String>("FetchIOSDiagnosticEventsByCaptureIds", "FetchIOSDiagnosticEventsByCaptureIdsResponse") );
			add(new RequestResponsePair<String, String>("CreateIOSDiagnostic", "CreateIOSDiagnosticResponse") );
			add(new RequestResponsePair<String, String>("FetchIOSDiagnosticCaptureIdsBySerialNumber", "FetchIOSDiagnosticCaptureIdsBySerialNumberResponse") );
		}
	};


	public static String escapeXMLCharacter(String aText) {
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      if (character == '&') {
	         result.append("&amp;");
	      }
	      else {
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	
	
	public static class AuditResponseSaxParserHandler extends DefaultHandler {
		private String tempValue;
		private WarrantyAuditResponse currentWarrantyAuditResponse=null;
		private WarrantyAuditFile responseAuditFile = null;
		
		boolean inWarrantyCheckIndicators = false;
		boolean inExtendedPartData = false;
		boolean inAssemblyData = false;
		boolean inAssemblyData_Out = false;
		
		boolean inOtherResponse = false;
		
		private List<WarrantyAuditAssemblyData> listWarrantyAuditAssemblyData;
		private WarrantyAuditAssemblyData currentWarrantyAuditAssemblyData;
		
		static final private ArrayList<WarrantyAuditFileInfo> unhandledTagList = new ArrayList<WarrantyAuditFileInfo>();
		
		static final private Map<String, String> unhandledResponseMap = new HashMap<String, String>() {
			private static final long serialVersionUID = 4198338568918352366L;
			{
				for (RequestResponsePair<String, String> obj : unhandledRequestResponseList) {
					put(obj.getResponse(), obj.getRequest());
				}
			}
		};
		static final private Map<String, String> unhandledResEndTagMap = new HashMap<String, String>(); 
		static
		{
			unhandledResEndTagMap.put("diagnosticEventNumber", "1");
			unhandledResEndTagMap.put("fetchDiagnosticEventNumbersResponseType", "1");
			unhandledResEndTagMap.put("diagnosticEventNumbers", "1");
			unhandledResEndTagMap.put("diagnosticEventNumbersType", "1");
			unhandledResEndTagMap.put("createIOSDiagnosticResponseType", "1");
			unhandledResEndTagMap.put("item", "1");		unhandledResEndTagMap.put("opCode", "1");
			
			unhandledResEndTagMap.put("resultType", "1");	unhandledResEndTagMap.put("diagnosticEventNumber", "1");
			unhandledResEndTagMap.put("information", "1");	unhandledResEndTagMap.put("accountID", "1");	
			unhandledResEndTagMap.put("eventSummary", "1");	unhandledResEndTagMap.put("shipTo", "1");
			unhandledResEndTagMap.put("diagnosticEventEndResult", "1");	unhandledResEndTagMap.put("serialNumber", "1");
			unhandledResEndTagMap.put("startTimeStamp", "1");	unhandledResEndTagMap.put("endTimeStamp", "1");
			unhandledResEndTagMap.put("modulePassCount", "1");	unhandledResEndTagMap.put("toolID", "1");
			unhandledResEndTagMap.put("diagnosticHeaderInfoType", "1");	unhandledResEndTagMap.put("toolVersion", "1");
			unhandledResEndTagMap.put("eventHeader", "1");	unhandledResEndTagMap.put("fetchDiagnosticEventResponseType", "1");
			
			unhandledResEndTagMap.put("diagnosticEventNumber", "1");
			unhandledResEndTagMap.put("shipTo", "1");
			unhandledResEndTagMap.put("serialNumber", "1");	unhandledResEndTagMap.put("diagnosticEventEndResult", "1");
			unhandledResEndTagMap.put("startTimeStamp", "1");	unhandledResEndTagMap.put("endTimeStamp", "1");
			unhandledResEndTagMap.put("modulePassCount", "1");	unhandledResEndTagMap.put("toolID", "1");
			unhandledResEndTagMap.put("toolVersion", "1");	unhandledResEndTagMap.put("diagnosticHeaderInfoType", "1");
			unhandledResEndTagMap.put("eventHeader", "1");	unhandledResEndTagMap.put("fetchDiagnosticEventResponseType", "1");
			unhandledResEndTagMap.put("name", "1");	unhandledResEndTagMap.put("value", "1");
			unhandledResEndTagMap.put("fetchIOSDiagnosticEventsByCaptureIdsResponseType", "1");	
			unhandledResEndTagMap.put("diagnosticTestType", "1");
			unhandledResEndTagMap.put("testResultType", "1");	unhandledResEndTagMap.put("testResult", "1");
			unhandledResEndTagMap.put("result", "1");	unhandledResEndTagMap.put("testContext", "1");
			unhandledResEndTagMap.put("testContextType", "1");	unhandledResEndTagMap.put("validationCode", "1");
			
		}
		
		private SSPXMLObject	currentResponseSSPXmlObj = null;
		
		
		public static ArrayList<WarrantyAuditFileInfo> getUnhandledtaglist() {
			return unhandledTagList;
		}

		public AuditResponseSaxParserHandler () throws Exception {
		}
	
		public AuditResponseSaxParserHandler (WarrantyAuditResponse obj, WarrantyAuditFile auditFile) throws Exception {
			this.currentWarrantyAuditResponse = obj;
			this.responseAuditFile = auditFile;
		}
		
		public void startDocument () throws SAXException {
		}
		
		public void endDocument() throws SAXException {
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			tempValue = (new String(ch, start, length).trim());
		}

		private void checkFalse(String field, boolean flag) {
			if (flag)
				throw new IllegalArgumentException("expecting [" + field + "] to be false");
		}
		
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (qName!=null && unhandledResponseMap.get(qName)!=null)
			{
				checkFalse("UnhandledResponse", inOtherResponse);
				currentResponseSSPXmlObj = new SSPXMLObject(qName);
				inOtherResponse = true;
			} else if ( qName.equals("WarrantyCheckIndicators")) {
				checkFalse("WarrantyCheckIndicators", inWarrantyCheckIndicators);
				inWarrantyCheckIndicators = true;

			} else if ( qName.equals("ExtendedPartData")) {
				checkFalse("ExtendedPartData", inExtendedPartData);
				inExtendedPartData = true;

			}
			else if ( qName.equals("AssemblyData_Out")) {
				checkFalse("AssemblyData_Out", inAssemblyData_Out);
				currentWarrantyAuditAssemblyData = new WarrantyAuditAssemblyData();
				inAssemblyData_Out = true;
			} 

			else if ( qName.equals("AssemblyData")) {
				checkFalse("AssemblyData", inAssemblyData);
				currentWarrantyAuditAssemblyData = new WarrantyAuditAssemblyData();
				inAssemblyData = true;
			} 

		}
		
		private void checkNull(Object ob) {
			if (ob == null)
				throw new NullPointerException("Object[" + ob + "] is null");
		}
		
		public boolean handleAssemblyData(String uri, String localName, String qName)  {
			boolean ret = false;
			if (qName.equals("ServicePartNumber")) {
				checkNull(currentWarrantyAuditAssemblyData);
				currentWarrantyAuditAssemblyData.setAssembly_ServicePartNumber(tempValue);
				
				if (listWarrantyAuditAssemblyData!=null && listWarrantyAuditAssemblyData.size()>0) {
					if (flagDisplay==1) {
						//logger.info("Res_Assembly_ServicePartNumber, File [" + responseAuditFile.getFileName() + "] tag [" + qName +  "] duplicated value [" + tempValue + "]");
						System.out.println("Res_Assembly_ServicePartNumber, File [" + responseAuditFile.getFileName() + "] tag [" + qName +  "] duplicated value [" + tempValue + "]");
					}
				}
				
				ret = true;
			}
			
			else if (qName.equals("CoverageReferenceNumber")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				currentWarrantyAuditAssemblyData.setAssembly_CoverageReferenceNumber(tempValue);
				ret = true;
			}

			else if (qName.equals("CoverageStartDate")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				currentWarrantyAuditAssemblyData.setAssembly_CoverageStartDate(parseDate ( qName, tempValue ));
				ret = true;
			}

			else if (qName.equals("CoverageEndDate")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				currentWarrantyAuditAssemblyData.setAssembly_CoverageEndDate(parseDate ( qName, tempValue ));
				ret = true;
			}

			else if (qName.equals("LaborCoverage")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				currentWarrantyAuditAssemblyData.setAssembly_LaborCoverage(tempValue);
				ret = true;
			}
			else if (qName.equals("PartCoverage")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				currentWarrantyAuditAssemblyData.setAssembly_PartCoverage(tempValue);
				ret = true;
			}
			else if (qName.equals("AssemblyData_Out")) {
				checkNull(currentWarrantyAuditAssemblyData); 
				if (listWarrantyAuditAssemblyData == null) {
					listWarrantyAuditAssemblyData = new ArrayList<WarrantyAuditAssemblyData>();
				}
				listWarrantyAuditAssemblyData.add(currentWarrantyAuditAssemblyData);
				
				currentWarrantyAuditAssemblyData = null;
				inAssemblyData_Out = false;
				ret = true;
			}
			else if (qName.equals("AssemblyData")) {
				//currentWarrantyAuditResponse.setRes_Assembly_AssemblyData(tempValue);
				if (listWarrantyAuditAssemblyData != null) {
					currentWarrantyAuditResponse.setAssemblyDatas(listWarrantyAuditAssemblyData);
				}
				
				currentWarrantyAuditAssemblyData = null;
				listWarrantyAuditAssemblyData = null;
				inAssemblyData = false;
				ret = true;
			} else {
				
			}

			return ret;
		}	

		public boolean handleExtendedPartData(String uri, String localName, String qName)  {
			boolean ret = false;
			if (qName.equals("PersonalizedProduct")) {
				currentWarrantyAuditResponse.setRes_ExtData_PersonalizedProduct(tempValue);
				ret = true;
			}
			else if (qName.equals("PurchaseDirect")) {
				currentWarrantyAuditResponse.setRes_ExtData_PurchaseDirect(tempValue);
				ret = true;
			}
			else if (qName.equals("ExtendedPartData_Out")) {
				currentWarrantyAuditResponse.setRes_ExtData_ExtendedPartData_Out(tempValue);
				ret = true;
			} else {

			}
			return ret;
		}	

		
		public boolean handleOtherResponse(String uri, String localName, String qName)  {
			boolean ret = false;
			if (qName !=null && unhandledResEndTagMap.get(qName)!=null ) {
				currentResponseSSPXmlObj.addNameValue(qName, tempValue);
				ret = true;
			}
			return ret;
		}	

		
		public boolean handleWarrantyCheckIndicators(String uri, String localName, String qName)  {
			boolean ret = false;
			if (qName.equals("LaborCoverage")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_LaborCoverage(tempValue);
				ret = true;
			}
			else if (qName.equals("PartCoverage")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_PartCoverage(tempValue);
				ret = true;
			}
			else if (qName.equals("LimitedWarranty")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_LimitedWarranty(tempValue);
				ret = true;
			}
			else if (qName.equals("AccidentalDamageCoverage")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_AccidentalDamageCoverage(tempValue);
				ret = true;
			}
			
			else if (qName.equals("GlobalRepairAuthorized")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_GlobalRepairAuthorized(tempValue);
				ret = true;
			}
			else if (qName.equals("WarrantyCheckIndicators_Out")) {
				currentWarrantyAuditResponse.setRes_WarrantyCheck_WarrantyCheckIndicators_Out(tempValue);
				ret = true;
			} else {

			}

			return ret;
		}	
		
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (inOtherResponse==true) {
				if (qName!=null && unhandledResponseMap.get(qName)!=null)
				{
//					logger.info("AuditResponseSaxParserHandler-handleOtherResponse-endElement, - qName [" + qName + "] currentWarrantyAuditResponse [" + currentWarrantyAuditResponse.getStrSVC_NAME() + ":" + currentWarrantyAuditResponse.getStrGUID()  + "]");
//					logger.info("AuditResponseSaxParserHandler-handleOtherResponse-endElement, - qName [" + qName + "] currentResponseSSPXmlObj [" + currentResponseSSPXmlObj.toString() + "]");
					System.out.println("AuditResponseSaxParserHandler-handleOtherResponse-endElement, - qName [" + qName + "] currentWarrantyAuditResponse [" + currentWarrantyAuditResponse.getStrSVC_NAME() + ":" + currentWarrantyAuditResponse.getStrGUID()  + "]");
					System.out.println("AuditResponseSaxParserHandler-handleOtherResponse-endElement, - qName [" + qName + "] currentResponseSSPXmlObj [" + currentResponseSSPXmlObj.toString() + "]");
					inOtherResponse=false;
					return;
				} else {
					if ( handleOtherResponse(uri, localName, qName))
						return;
				}
			}
			
			if (inWarrantyCheckIndicators==true) {
				if (handleWarrantyCheckIndicators(uri, localName, qName)) 
					return;
			}
			if (inExtendedPartData==true) {
				if ( handleExtendedPartData(uri, localName, qName) ) 
					return;
			}
			
			if (inAssemblyData==true) {
				if ( handleAssemblyData(uri, localName, qName) ) 
					return;
			}

			if (qName.equals("SerialNumber")) {
				currentWarrantyAuditResponse.setRes_SerialNumber(tempValue);
			} else if (qName.equals("ID")) {
				currentWarrantyAuditResponse.setRes_ID(tempValue);
			} else if (qName.equals("UUID")) {
				currentWarrantyAuditResponse.setRes_UUID(tempValue);
			} else if (qName.equals("ReferenceID")) {
				currentWarrantyAuditResponse.setRes_ReferenceID(tempValue);
			}  else if (qName.equals("ReferenceUUID")) {
				currentWarrantyAuditResponse.setRes_ReferenceUUID(tempValue);
			} else if (qName.equals("ApplicationSequenceNo")) {
				currentWarrantyAuditResponse.setRes_ApplicationSequenceNo(tempValue);
			} else if (qName.equals("MessageType")) {
				currentWarrantyAuditResponse.setRes_MessageType(tempValue);
			} else if (qName.equals("ProviderAppID")) {
				currentWarrantyAuditResponse.setRes_ProviderAppID(tempValue);
			} else if (qName.equals("ConsumerAppID")) {
				currentWarrantyAuditResponse.setRes_ConsumerAppID(tempValue);
			} else if (qName.equals("ConsumerPersonID")) {
				currentWarrantyAuditResponse.setRes_ConsumerPersonID(tempValue);
			} else if (qName.equals("PrimaryKey")) {
				currentWarrantyAuditResponse.setRes_PrimaryKey(tempValue);
			} else if (qName.equals("OperationCode")) {
				currentWarrantyAuditResponse.setRes_OperationCode(tempValue);
			} 
			else if (qName.equals("TimeStamp")) {
				currentWarrantyAuditResponse.setRes_TimeStamp( parseDate ( qName, tempValue) );
			} 
			else if (qName.equals("Environment")) {
				currentWarrantyAuditResponse.setRes_Environment(tempValue);
			} else if (qName.equals("Attribute1")) {
				currentWarrantyAuditResponse.setRes_Attribute1(tempValue);
			} else if (qName.equals("Attribute2")) {
				currentWarrantyAuditResponse.setRes_Attribute2(tempValue);
			} else if (qName.equals("Attribute3")) {
				currentWarrantyAuditResponse.setRes_Attribute3(tempValue);
			} else if (qName.equals("Attribut4")) {
				currentWarrantyAuditResponse.setRes_Attribute4(tempValue);
			} else if (qName.equals("MessageHeader_Type")) {
				currentWarrantyAuditResponse.setRes_MessageHeader_Type(tempValue);
			}  else if (qName.equals("MessageHeader")) {
				currentWarrantyAuditResponse.setRes_MessageHeader(tempValue);
			} 
			else if (qName.equals("RequestSubmitDate")) {
				currentWarrantyAuditResponse.setRes_RequestSubmitDate( parseDate ( qName, tempValue) );
			} 
			else if (qName.equals("ContractType")) {
				currentWarrantyAuditResponse.setRes_ContractType(tempValue);
			} 
			else if (qName.equals("CoverageTypeCode")) {
				currentWarrantyAuditResponse.setRes_CoverageTypeCode(tempValue);
			}
			
			else if (qName.equals("CoverageReferenceNumber")) {
				currentWarrantyAuditResponse.setRes_CoverageReferenceNumber(tempValue);
			}
			
			else if (qName.equals("CoverageStartDate")) {
				currentWarrantyAuditResponse.setRes_CoverageStartDate( parseDate ( qName, tempValue)   );
			}
			else if (qName.equals("CoverageEndDate")) {
				currentWarrantyAuditResponse.setRes_CoverageEndDate( parseDate ( qName, tempValue, dateFormat )   );
			}
			else if (qName.equals("PurchaseDate")) {
				currentWarrantyAuditResponse.setRes_PurchaseDate( parseDate ( qName, tempValue, dateFormat)  );
			}
			else if (qName.equals("ContractStartDate")) {
				currentWarrantyAuditResponse.setRes_ContractStartDate( parseDate ( qName, tempValue, dateFormat)  );
			}
			else if (qName.equals("ContractEndDate")) {
				currentWarrantyAuditResponse.setRes_ContractEndDate( parseDate ( qName, tempValue, dateFormat)  );
			}

			else if (qName.equals("CountryOfPurchase")) {
				currentWarrantyAuditResponse.setRes_CountryOfPurchase(tempValue);
			}
			else if (qName.equals("CountryOfRegistration")) {
				currentWarrantyAuditResponse.setRes_CountryOfRegistration(tempValue);
			}
			else if (qName.equals("CompTIA")) {
				currentWarrantyAuditResponse.setRes_CompTIA(tempValue);
			}
			else if (qName.equals("RegistrationDate")) {
				currentWarrantyAuditResponse.setRes_RegistrationDate( parseDate ( qName, tempValue, dateFormat) );
			}
			else if (qName.equals("MaterialNumber")) {
				currentWarrantyAuditResponse.setRes_MaterialNumber(tempValue);
			} 
			else if (qName.equals("SoldToPartyNumber")) {
				currentWarrantyAuditResponse.setRes_SoldToPartyNumber(tempValue);
			} 
			
			else if (qName.equals("SoldToPartyName")) {
				currentWarrantyAuditResponse.setRes_SoldToPartyName(tempValue);
			}
			else if (qName.equals("SLAGroup")) {
				currentWarrantyAuditResponse.setRes_SLAGroup(tempValue);
			} 			
			else if (qName.equals("PartReturnable")) {
				currentWarrantyAuditResponse.setRes_PartReturnable(tempValue);
			} 
			else if (qName.equals("PowerTrainWarrantyCoverage")) {
				currentWarrantyAuditResponse.setRes_PowerTrainWarrantyCoverage(tempValue);
			}
			else if (qName.equals("FormatInvalid")) {
				currentWarrantyAuditResponse.setRes_FormatInvalid(tempValue);
			}
			else if (qName.equals("WarrantyNotFound")) {
				currentWarrantyAuditResponse.setRes_WarrantyNotFound(tempValue);
			}
			else if (qName.equals("SerialNumberNotFound")) {
				currentWarrantyAuditResponse.setRes_SerialNumberNotFound(tempValue);
			}
			else if (qName.equals("IMCCountry")) {
				currentWarrantyAuditResponse.setRes_IMCCountry(tempValue);
			}
			else if (qName.equals("OnsiteRepairAuthorized")) {
				currentWarrantyAuditResponse.setRes_OnsiteRepairAuthorized(tempValue);
			}
			
			else if (qName.equals("TricareCoverage")) {
				currentWarrantyAuditResponse.setRes_TricareCoverage(tempValue);
			}
			else if (qName.equals("ChinaHardBundle")) {
				currentWarrantyAuditResponse.setRes_ChinaHardBundle(tempValue);
			}
			else if (qName.equals("ExtendedOnsiteCoverage")) {
				currentWarrantyAuditResponse.setRes_ExtendedOnsiteCoverage(tempValue);
			}
			else if ( qName.equals("WarrantyCheckIndicators")) {
				inWarrantyCheckIndicators = false;
				
			} else if ( qName.equals("ExtendedPartData")) {
				inExtendedPartData = false;
				// System.out.println("inExtendedPartData = false;");
			} else if ( qName.equals("PartData_Out")) {
				// ToDo check data 
			} else if ( qName.equals("PartData")) {
				// ToDo check data 
			} else if ( qName.equals("WarrantyData_Out")) {
				// ToDo check data 
			} else if ( qName.equals("WarrantyData")) {
				// ToDo check data 
			} else if ( qName.equals("CheckWarrantyResponse_Type")) {
				// ToDo check data 
			} else if ( qName.equals("WarrantyCheckResponse")) {
				// ToDo check data 
			}
			else if ( qName.equals("ValidationCheckIndicators_Out")) {
				// ToDo check data
			}
			else if ( qName.equals("ValidationCheckIndicators")) {
				// ToDo check data
			}
			else {
				//logger.error("	" + qName + " VARCHAR(50), " );
				//logger.error("	String Res_" + qName + ";" );
				
//				System.out.println("	private String Res_" + qName + ";" );
//				System.out.println("	Res_" + qName + " VARCHAR(50), " );

				//System.out.println("	private String Res_WarrantyCheck_" + qName + ";" );
				//System.out.println("	Res_WarrantyCheck_" + qName + " VARCHAR(50), " );

//				System.out.println("<property column=\"Res_WarrantyCheck_" + qName +  "\" generated=\"never\" lazy=\"false\"" );
//				System.out.println("name=\"Res_WarrantyCheck_" + qName + "\" type=\"string\"/>" );  

//				System.out.println("else if (qName.equals(\"" + qName + "\")) {" );
//				System.out.println("	currentWarrantyAuditResponse.setRes_" + qName + "(tempValue);" );
//				System.out.println("}" );

//				System.out.println("else if (qName.equals(\"" + qName + "\")) {" );
//				System.out.println("	currentWarrantyAuditResponse.setRes_WarrantyCheck_" + qName + "(tempValue);" );
//				System.out.println("	ret = true;" );
//				System.out.println("}" );

				//System.out.println("	private String Res_Assembly_" + qName + ";" );
				//System.out.println("	Res_Assembly_" + qName + " VARCHAR(50), " );
				
//				System.out.println("<property column=\"Res_Assembly_" + qName +  "\" generated=\"never\" lazy=\"false\"" );
//				System.out.println("name=\"Res_Assembly_" + qName + "\" type=\"string\"/>" );  

//				System.out.println("else if (qName.equals(\"" + qName + "\")) {" );
//				System.out.println("	currentWarrantyAuditResponse.setRes_Assembly_" + qName + "(tempValue);" );
//				System.out.println("	ret = true;" );
//				System.out.println("}" );
				//System.out.println(qName );
				
//				logger.error("ResponseSaxParserHandler-endElement, File [" + responseAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				System.out.println("ResponseSaxParserHandler-endElement, File [" + responseAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				addUnhandledTag(qName, tempValue, responseAuditFile, unhandledTagList);
				
			}
			
			tempValue = null;
		}
		
	}
	
	public static class AuditRequestSaxParserHandler extends DefaultHandler {

		static final private ArrayList<WarrantyAuditFileInfo> unhandledRequestTagList = new ArrayList<WarrantyAuditFileInfo>();
		
		
		static final private Map<String, String> unhandledRequestMap = new HashMap<String, String>() {
			private static final long serialVersionUID = 4421154998431499252L;
			{
				for (RequestResponsePair<String, String> obj : unhandledRequestResponseList) {
					put(obj.getRequest(), obj.getResponse());
				}
			}
		};
		
		static final private Map<String, String> unhandledRequestEndTagMap = new HashMap<String, String>() {
			private static final long serialVersionUID = 3160660601500645160L;
			{
				put("FetchDiagnosticEvent", "1");	put("fetchDiagnosticType", "1");
				put("fetchDiagnosticEventRequestWrapper", "1");
				put("item", "1");		put("serialNumber", "1");
				put("channelId", "1");	put("diagnosticFetchWindow", "1");
				put("fetchBySerialNumberRequestType", "1");
				put("FetchRequest", "1");
				put("fetchIOSDiagnosticCaptureIdsBySerialNumberRequestWrapper", "1");
				put("captureId", "1");	put("diagnosticTimeStamp", "1");
				put("diagnosticTimeStamp", "1");	put("diagnosticValidationResult", "1");
				put("validationCode", "1");

				put("createIOSDiagnosticResponseType", "1");
				put("name", "1");	put("reportDataType", "1");
				put("channelID", "1");	put("testContextType", "1");
				put("testContext", "1");	put("value", "1");
				put("resultType", "1");	put("simpleKeyType", "1");
				put("information", "1");	put("result", "1"); put("testResultType", "1");
				put("diagnosticTestType", "1");	put("key", "1"); 	put("data", "1");
				put("keyArrayType", "1");	put("unit", "1");	put("type", "1");
				put("testResults", "1");	put("diagnosticTestData", "1");
				put("module", "1");			put("captureIds", "1");
				put("profileType", "1");		put("profile", "1");
				put("extendedValueType", "1");	put("extendedKeyType", "1");
				put("extended-key", "1"); 	put("reportData", "1");
				put("report", "1"); 		put("diagnosticProfileType", "1");
				put("diagnosticProfileData", "1");
				put("createIOSDiagnosticRequestType", "1");
				put("createIOSDiagnosticRequestWrapper", "1");
			}
			
		};
		
		private String tempValue;
		private WarrantyAuditResponse currentWarrantyAuditRequest=null;
		private WarrantyAuditFile requestAuditFile;
		
		private SSPXMLObject	currentRequestSSPXmlObj = null; 
		private boolean inHandleOtherRequestEndTag = false;
		

		
		public static ArrayList<WarrantyAuditFileInfo> getUnhandledRequestTaglist() {
			return unhandledRequestTagList;
		}



		public AuditRequestSaxParserHandler () throws Exception {
		}
		
		
		private boolean handleWarrantyRequestEndTag(String uri, String localName, String qName)  {
			boolean ret = true;

			if (qName.equals("WarrantyCheckRequest")) {
				currentWarrantyAuditRequest.setWarrantyCheckRequest(tempValue);
			} 
			else if (qName.equals("CheckWarrantyRequest_Type")) {
				currentWarrantyAuditRequest.setCheckWarrantyRequest_Type(tempValue);
			} 
			else if (qName.equals("WarrantyData")) {
				currentWarrantyAuditRequest.setWarrantyData(tempValue);
			} 
			else if (qName.equals("WarrantyData_In")) {
				currentWarrantyAuditRequest.setWarrantyData_In(tempValue);
			}  
			else if (qName.equals("PartData")) {
				currentWarrantyAuditRequest.setPartData( tempValue );
			} else if (qName.equals("PartData_In")) {
				currentWarrantyAuditRequest.setPartData_In(tempValue);
			} else if (qName.equals("SerialNumber")) {
				currentWarrantyAuditRequest.setSerialNumber(tempValue);
			} else if (qName.equals("RequestSubmitDate")) {
				currentWarrantyAuditRequest.setRequestSubmitDate( parseDate ( qName, tempValue, dateFormat)   );
			} else if (qName.equals("CompTIA")) {
				currentWarrantyAuditRequest.setCompTIA(tempValue);
			} else if (qName.equals("CountryOfPurchase")) {
				currentWarrantyAuditRequest.setCountryOfPurchase(tempValue);
			} else if (qName.equals("ServicePartNumber")) {
				currentWarrantyAuditRequest.setServicePartNumber(tempValue);
			} 
			else if (qName.equals("ID")) {
				currentWarrantyAuditRequest.setID(tempValue);
			}
			else if (qName.equals("UUID")) {
				currentWarrantyAuditRequest.setUUID(tempValue);
			}
			else if (qName.equals("ReferenceID")) {
				currentWarrantyAuditRequest.setReferenceID(tempValue);
			}
			else if (qName.equals("ReferenceUUID")) {
				currentWarrantyAuditRequest.setReferenceUUID(tempValue);
			}
			else if (qName.equals("ApplicationSequenceNo")) {
				currentWarrantyAuditRequest.setApplicationSequenceNo(tempValue);
			}
			else if (qName.equals("MessageType")) {
				currentWarrantyAuditRequest.setMessageType(tempValue);
			}
			else if (qName.equals("ProviderAppID")) {
				currentWarrantyAuditRequest.setProviderAppID(tempValue);
			}
			else if (qName.equals("ConsumerAppID")) {
				currentWarrantyAuditRequest.setConsumerAppID(tempValue);
			}
			else if (qName.equals("ConsumerPersonID")) {
				currentWarrantyAuditRequest.setConsumerPersonID(tempValue);
			}
			else if (qName.equals("PrimaryKey")) {
				currentWarrantyAuditRequest.setPrimaryKey(tempValue);
			}
			else if (qName.equals("OperationCode")) {
				currentWarrantyAuditRequest.setOperationCode(tempValue);
			}
			else if (qName.equals("TimeStamp")) {
				currentWarrantyAuditRequest.setTimeStamp( parseDate ( qName, tempValue ) );
			}
			else if (qName.equals("Environment")) {
				currentWarrantyAuditRequest.setEnvironment(tempValue);
			}
			else if (qName.equals("Attribute1")) {
				currentWarrantyAuditRequest.setAttribute1(tempValue);
			}
			else if (qName.equals("Attribute2")) {
				currentWarrantyAuditRequest.setAttribute2(tempValue);
			}
			else if (qName.equals("Attribute3")) {
				currentWarrantyAuditRequest.setAttribute3(tempValue);
			}
			else if (qName.equals("Attribut4")) {
				currentWarrantyAuditRequest.setAttribut4(tempValue);
			}
			
			else if (qName.equals("AssemblyData_In")) {
				// nothing to do
				//currentWarrantyAuditRequest.setAssemblyData_In(tempValue);
			} 
			else if (qName.equals("AssemblyData")) {
				currentWarrantyAuditRequest.setAssemblyData(tempValue);
			}
			else if (qName.equals("body")) {
				currentWarrantyAuditRequest.setBody(tempValue);
			}
			
			else if (qName.equals("MessageHeader_Type")) {
				// nothing to do 
			} else if (qName.equals("MessageHeader")) {
				// nothing to do 
			} else {
				//logger.error("handleWarrantyRequestEndTag-endElement, File [" + requestAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				System.err.println("handleWarrantyRequestEndTag-endElement, File [" + requestAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				ret = false;
			} 

			return ret;
		}	

		
		public AuditRequestSaxParserHandler (WarrantyAuditResponse obj, WarrantyAuditFile auditFile) throws Exception {
			this.currentWarrantyAuditRequest = obj;
			this.requestAuditFile = auditFile;
		}
		
		public void startDocument () throws SAXException {
		}
		
		public void endDocument() throws SAXException {
		}
		
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if (qName !=null && unhandledRequestMap.get(qName)!=null) {
				//logger.info("AuditResponseSaxParserHandler-startElement, [" + qName + "" + "]");
				
				inHandleOtherRequestEndTag = true;
				currentRequestSSPXmlObj = new SSPXMLObject(qName);
			}
		}
		
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (tempValue==null) {
				tempValue = (new String(ch, start, length).trim());
			} else {
				tempValue = tempValue + (new String(ch, start, length).trim());
			}
		}

		
		private boolean handleOtherRequestEndTag(String uri, String localName, String qName)  {
			boolean ret = true;
			
			if (qName !=null && unhandledRequestMap.get(qName)!=null) {
				inHandleOtherRequestEndTag = false;
				//logger.info("AuditRequestSaxParserHandler-handleOtherRequestEndTag-" + qName + ", currentRequestSSPXmlObj [" + currentRequestSSPXmlObj.toString() + "]");
			}
			else if (qName !=null && unhandledRequestEndTagMap.get(qName)!=null) {
				currentRequestSSPXmlObj.addNameValue(qName, tempValue);
			} else {
				//logger.error("AuditRequestSaxParserHandler-handleOtherRequestEndTag-endElement, File [" + requestAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				ret = false;
			}
			
			return ret;
		}	


		public void endElement(String uri, String localName,
				String qName) throws SAXException {
			
			boolean processTag = false;
			
			if ( inHandleOtherRequestEndTag ) {
				processTag = handleOtherRequestEndTag(uri, localName, qName);
			} else {
				processTag = handleWarrantyRequestEndTag(uri, localName, qName);
			}
			
			if (!processTag) {
				//logger.error("RequestSaxParserHandler-endElement, File [" + requestAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				System.err.println("RequestSaxParserHandler-endElement, File [" + requestAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				addUnhandledTag(qName, tempValue, requestAuditFile, unhandledRequestTagList);
			}

			tempValue = null;
		}
		
	}

	
	public static class AuditSaxParserHandler extends DefaultHandler {
		private String tempValue;
		private String xmlParseString;

		
		private String iValue;
		private String tValue;
		private String strTIME;
		private boolean inTIME=false;
		
		private String strDELTAT;
		private boolean inDELTAT=false;
		
		private String strGUID;
		private boolean inGUID=false;
		
		private String strERR_MSG;
		private boolean inERR_MSG=false;
		
		private String strCONSUMER_APP_SEQ_NO;
		private boolean inCONSUMER_APP_SEQ_NO=false;
		
		private String strSVC_NAME="";
		private boolean inSVC_NAME=false;
		private int countSVC_NAME=0;

		private int countWarrantyAuditResponse=0;
		
		private String strRESPONSE_STATUS;
		private boolean inRESPONSE_STATUS=false;
		
		private String strREQUEST_PAYLOAD;
		private boolean inREQUEST_PAYLOAD=false;
		
		
		private String strVERSION_NO;
		private boolean inVERSION_NO=false;
		
		private String strCONSUMER_IP;
		private boolean inCONSUMER_IP=false;

		private String strCONSUMER_APP_ID;
		private boolean inCONSUMER_APP_ID=false;
		
		private String strPROVIDER_APP_ID;
		private boolean inPROVIDER_APP_ID=false;
		
		private String strSERVER_IP;
		private boolean inSERVER_IP=false;
		
		private String strRESPONSE_PAYLOAD;
		private boolean inRESPONSE_PAYLOAD=false;
		
		private String strOP_NAME;
		private boolean inOP_NAME=false;
		
		
		private String strESP_ENV;
		private boolean inESP_ENV=false;
		
		private String strREQUEST_TYPE;
		private boolean inREQUEST_TYPE=false;
		
		private String strCREQT;
		private boolean inCREQT=false;
		
		private String strESP_CLIENT_VERSION;
		private boolean inESP_CLIENT_VERSION=false;
		
		private List <WarrantyAuditResponse> currentWarrantyAuditResponseList; 
		private List <WarrantyAuditFileInfo> currentWarrantyAuditFileInfoList; 
		private WarrantyAuditFile currentAuditFile;

		public AuditSaxParserHandler (File file) throws Exception {
			currentAuditFile = new WarrantyAuditFile(file.getName());
		} 

		public AuditSaxParserHandler (WarrantyAuditFile auditFileObj, 
				List <WarrantyAuditFileInfo> listWarrantyAuditFileInfo,
				List <WarrantyAuditResponse> listWarrantyAuditResponse) throws Exception {
			currentAuditFile = auditFileObj;
			currentWarrantyAuditFileInfoList = listWarrantyAuditFileInfo;
			currentWarrantyAuditResponseList = listWarrantyAuditResponse;
		} 

		
		private boolean handleName (String name) {
			boolean ret = false; 
			if (name.equals("TIME")) {
				inTIME = true;
				ret = true;
			} else if (name.equals("DELTAT")) {
				inDELTAT = true;
				ret = true;
			} else if (name.equals("GUID")) {
				inGUID = true;
				ret = true;
			} else if (name.equals("ERR_MSG")) {
				inERR_MSG = true;
				ret = true;
			} else if (name.equals("CONSUMER_APP_SEQ_NO")) {
				inCONSUMER_APP_SEQ_NO = true;
				ret = true;
			} else if (name.equals("SVC_NAME")) {
				inSVC_NAME = true;
				ret = true;
				strSVC_NAME="";
				strREQUEST_PAYLOAD = "";
				strRESPONSE_PAYLOAD = "";
			} else if (name.equals("RESPONSE_STATUS")) {
				inRESPONSE_STATUS = true;
				ret = true;
			} else if (name.equals("REQUEST_PAYLOAD")) {
				inREQUEST_PAYLOAD = true;
				ret = true;
			} else if (name.equals("VERSION_NO")) {
				inVERSION_NO = true;
				ret = true;
			} else if (name.equals("CONSUMER_IP")) {
				inCONSUMER_IP = true;
				ret = true;
			} else if (name.equals("CONSUMER_APP_ID")) {
				inCONSUMER_APP_ID = true;
				ret = true;
			} else if (name.equals("PROVIDER_APP_ID")) {
				inPROVIDER_APP_ID = true;
				ret = true;
			} else if (name.equals("SERVER_IP")) {
				inSERVER_IP = true;
				ret = true;
			} else if (name.equals("RESPONSE_PAYLOAD")) {
				inRESPONSE_PAYLOAD = true;
				ret = true;
			} else if (name.equals("OP_NAME")) {
				inOP_NAME = true;
				ret = true;
			} else if (name.equals("ESP_ENV")) {
				inESP_ENV = true;
				ret = true;
			} else if (name.equals("REQUEST_TYPE")) {
				inREQUEST_TYPE = true;
				ret = true;
			} else if (name.equals("CREQT")) {
				inCREQT=true;
				ret = true;
			} else if (name.equals("ESP_CLIENT_VERSION")) {
				inESP_CLIENT_VERSION=true;
				ret = true;
			}
				
			

			return ret;
		}
		
		private boolean handleValue (String value) {
			boolean ret = false;
			if (inTIME == true) {
				strTIME = value;
				inTIME = false;
				ret = true;
			} else if (inDELTAT == true) {
				strDELTAT = value;
				inDELTAT = false;
				ret = true;
			} else if (inGUID == true) {
				strGUID = value;
				inGUID = false;
				ret = true;
			} else if (inERR_MSG == true) {
				strERR_MSG = value;
				inERR_MSG = false;
				ret = true;
			} else if (inCONSUMER_APP_SEQ_NO == true) {
				strCONSUMER_APP_SEQ_NO = value;
				inCONSUMER_APP_SEQ_NO = false;
				ret = true;
			} else if (inSVC_NAME == true) {
				strSVC_NAME = value;
				inSVC_NAME = false;
				countSVC_NAME+=1;
				if ( flagProcessSSP==1 && countSVC_NAME>1 ) {
					//logger.error("More than one more SVC_NAME entry in file [" + currentAuditFile.getFileName() +  "]" + " SVC_NAME [" + strSVC_NAME + "]" );
					System.err.println("More than one more SVC_NAME entry in file [" + currentAuditFile.getFileName() +  "]" + " SVC_NAME [" + strSVC_NAME + "]" );
				}
				ret = true;
			} else if (inRESPONSE_STATUS == true) {
				strRESPONSE_STATUS = value;
				inRESPONSE_STATUS = false;
				ret = true;
			} else if (inREQUEST_PAYLOAD == true) {
				strREQUEST_PAYLOAD = value;
				inREQUEST_PAYLOAD = false;
				ret = true;
			} else if (inVERSION_NO == true) {
				strVERSION_NO = value;
				inVERSION_NO = false;
				ret = true;
			} else if (inCONSUMER_IP == true) {
				strCONSUMER_IP = value;
				inCONSUMER_IP = false;
				ret = true;
			} else if (inCONSUMER_APP_ID == true) {
				strCONSUMER_APP_ID = value;
				inCONSUMER_APP_ID = false;
				ret = true;
			} else if (inPROVIDER_APP_ID == true) {
				strPROVIDER_APP_ID = value;
				inPROVIDER_APP_ID = false;
				ret = true;
			} else if (inSERVER_IP == true) {
				strSERVER_IP = value;
				inSERVER_IP = false;
				ret = true;
			} else if (inRESPONSE_PAYLOAD == true) {
				strRESPONSE_PAYLOAD = value;
				inRESPONSE_PAYLOAD = false;
				ret = true;
			} else if (inOP_NAME == true) {
				strOP_NAME = value;
				inOP_NAME = false;
				ret = true;
			} else if (inESP_ENV == true) {
				strESP_ENV = value;
				inESP_ENV = false;
				ret = true;
			} else if (inREQUEST_TYPE == true) {
				strREQUEST_TYPE = value;
				inREQUEST_TYPE = false; 
				ret = true;
			} else if (inCREQT == true) {
				strCREQT = value;
				inCREQT = false; 
				ret = true;
			} else if (inESP_CLIENT_VERSION == true) {
				strESP_CLIENT_VERSION = value;
				inESP_CLIENT_VERSION = false; 
				ret = true;
			} 
			

			return ret;
		}
		
		
		public WarrantyAuditFile getCurrentAuditFile() {
			return currentAuditFile;
		}


		public void setCurrentAuditFile(WarrantyAuditFile currentAuditFile) {
			this.currentAuditFile = currentAuditFile;
		}


		
		public AuditSaxParserHandler (String xmlString) throws Exception {
			this.xmlParseString = xmlString;
		}

		
		public void startDocument () throws SAXException {
		}
		
		public void endDocument() throws SAXException {
		}
		
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (qName.equals("a")) {
				iValue = attributes.getValue("i");
				tValue = attributes.getValue("t");
				//System.out.println("startElement [" + qName + "i-" + iValue + "t-" + tValue + "]");
			}
			//System.out.println("AuditSaxParserHandler-startElement [" + qName + "" + "]");
		}

		
		private String decodeString (String inputString) {
			if (inputString==null)	return "";
			if (inputString.length()==0)	return "";
			
			String retString="";
			try {
				retString = URLDecoder.decode(inputString, "UTF-8" );	// "ISO-8859-1"
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				//logger.error("Exception URLDecoder in file [" + currentAuditFile.getFileName() +  "]" + " inputString [" + inputString + "]" + "Exception details: "+ sw.toString());
				System.err.println("Exception URLDecoder in file [" + currentAuditFile.getFileName() +  "]" + " inputString [" + inputString + "]" + "Exception details: "+ sw.toString());

			}
			return retString;

		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			if ( tempValue !=null && ( inREQUEST_PAYLOAD == true || inRESPONSE_PAYLOAD == true ) ) { 
				tempValue = tempValue + (new String(ch, start, length).trim());
			}
			else {
				tempValue = (new String(ch, start, length).trim());
			}
			
		}

		private int addAuditFielInfo (String SVCNAME) {
			int ret = 1;
			WarrantyAuditFileInfo obj = new WarrantyAuditFileInfo(SVCNAME, "", 1, currentAuditFile);
			int idx = currentWarrantyAuditFileInfoList.indexOf(obj);
			if (idx<0 ) {
				currentWarrantyAuditFileInfoList.add(obj);
			} else {
				ret = currentWarrantyAuditFileInfoList.get(idx).incrementCount(1);
			}		
			return ret;
			
		}

		private void checkWarrantyAuditResponseData (WarrantyAuditResponse obj, String strRequestPayload, String strResponsePayload) {
			//logger.info(" xml obj - " + obj.toStringShortForm() + "");
			//System.out.println(" xml obj - " + obj.toStringShortForm() + "");

			
			if  ( obj.getStrRESPONSE_STATUS() !=null && obj.getStrRESPONSE_STATUS().equals("OK") 
					&& ( strRequestPayload==null || strRequestPayload.length()==0  ) ) {
				/*
				logger.warn("currentAuditFile [" 
						+ currentAuditFile.getFileName() 
						+ "], object -" + obj.toStringLongForm() 
						+ " Status OK but request payload is empty "
						);
				*/
				System.out.println("currentAuditFile [" 
						+ currentAuditFile.getFileName() 
						+ "], object -" + obj.toStringLongForm() 
						+ " Status OK but request payload is empty "
						);
			}

			if (obj.getSerialNumber() != null && obj.getRes_SerialNumber() != null
					&& !(obj.getSerialNumber().equals(obj.getRes_SerialNumber()))) {
				/*
				logger.warn("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], request serial [" + obj.getSerialNumber() 
						+ "] not matching response serial[" + obj.getRes_SerialNumber() + "]"
						+ "] response XML String [" + strResponsePayload + "]"
						+ " object -" + obj.toStringLongForm()
						);
				*/
				System.out.println("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], request serial [" + obj.getSerialNumber() 
						+ "] not matching response serial[" + obj.getRes_SerialNumber() + "]"
						+ "] response XML String [" + strResponsePayload + "]"
						+ " object -" + obj.toStringLongForm()
						);
			} else if (obj.getSerialNumber() == null && obj.getRes_SerialNumber() == null) {
				/*
				logger.warn("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], both request serial[" + obj.getSerialNumber() 
						+ "] and response serial [" + obj.getRes_SerialNumber() + "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						);
				*/
				System.out.println("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], both request serial[" + obj.getSerialNumber() 
						+ "] and response serial [" + obj.getRes_SerialNumber() + "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						); 

			} else if ( obj.getSerialNumber() == null ) {
				System.out.println("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], request serial[" + obj.getSerialNumber() 
						+ "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						);
				/*
				logger.warn("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], request serial[" + obj.getSerialNumber() 
						+ "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						);
				*/
			} else if ( obj.getRes_SerialNumber() == null ) {
				System.out.println("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], response serial[" + obj.getRes_SerialNumber() 
						+ "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						);
				/*
				logger.warn("currentAuditFile[" 
						+ currentAuditFile.getFileName() 
						+ "], response serial[" + obj.getRes_SerialNumber() 
						+ "] is NULL"
						+ "] request XML String [" + decodeString(strRequestPayload) + "]" 
						+ "] response XML String [" + decodeString(strResponsePayload) + "]"
						+ " object -" + obj.toStringLongForm()
						);
				*/
			} 

		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException {
			boolean handled = false;
			if (qName.equals("n")) {
				handled = handleName (tempValue);
			} else if (qName.equals("v")) {
				handled = handleValue (tempValue);
			} else if (qName.equals("e")) {
				handled = true;
			} else if (qName.equals("a")) {
				
				this.addAuditFielInfo (strSVC_NAME);
				
				// if it's the warranty, let's process it or process them all  
				if ( ( strSVC_NAME!=null &&  warrantySVCNameMap.get(strSVC_NAME) !=null ) ) {
					WarrantyAuditResponse obj = new WarrantyAuditResponse(currentAuditFile,
							iValue, tValue, strTIME, 
						strDELTAT, strGUID, strERR_MSG,
						strCONSUMER_APP_SEQ_NO, strSVC_NAME,
						strRESPONSE_STATUS, "",
						strVERSION_NO, strCONSUMER_IP,
						strCONSUMER_APP_ID, strPROVIDER_APP_ID,
						strSERVER_IP, "", 
						strOP_NAME,
						strESP_ENV, strREQUEST_TYPE);
					
//					if (flagAssignedKey==1) {
//						obj.setmId( keyCounter.addAndGet(1));
//					}
					
					
					obj.setStrESP_CLIENT_VERSION( decodeString(strESP_CLIENT_VERSION));
					obj.setStrCREQT( decodeString(strCREQT));
				
					
					//System.out.println(" REQUEST_PAYLOAD [" + strREQUEST_PAYLOAD + "]");
					if (strREQUEST_PAYLOAD!=null && strREQUEST_PAYLOAD.length()>0) {
						String strDecodeREQUEST_PAYLOAD = decodeString (strREQUEST_PAYLOAD);
						if (strDecodeREQUEST_PAYLOAD!=null && strDecodeREQUEST_PAYLOAD.length()>0) {
							int idx = strDecodeREQUEST_PAYLOAD.indexOf("<");
							if (idx>=0) {
								saxParseRequest(strDecodeREQUEST_PAYLOAD.substring(idx), obj, currentAuditFile);
							}
						}
					}

					// if the body is not empty, do parse one more time
					if (obj.getBody() !=null && obj.getBody().length()>0) {
						saxParseRequest(obj.getBody(), obj, currentAuditFile);
					}	
				
					if (strRESPONSE_PAYLOAD!=null && strRESPONSE_PAYLOAD.length()>0) {
						//System.out.println( "strRESPONSE_PAYLOAD [" + strRESPONSE_PAYLOAD + "]");
						String strDecodeRESPONSE_PAYLOAD = decodeString (strRESPONSE_PAYLOAD);
						if (strDecodeRESPONSE_PAYLOAD!=null && strDecodeRESPONSE_PAYLOAD.length()>0) {
							int idx = strDecodeRESPONSE_PAYLOAD.indexOf("<");
							if (idx>=0) {
;								saxParseResponse(strDecodeRESPONSE_PAYLOAD.substring(idx), obj, currentAuditFile);
							}
						}
					}
				
					handled = true;
					if (flagDisplay==1) {
						checkWarrantyAuditResponseData (obj, strREQUEST_PAYLOAD, strRESPONSE_PAYLOAD);
					}
					
									
					currentWarrantyAuditResponseList.add(obj);
					currentAuditFile.incrementFileRecordCount(1);
					countWarrantyAuditResponse+=1;
					if ( flagProcessSSP==1 && countWarrantyAuditResponse>1 ) {
						System.err.println("More than one more WarrantyAuditResponse object per line in file [" + currentAuditFile.getFileName() +  "]" 
								+ "  - " + obj.toString() + " " );
						/*
						logger.error("More than one more WarrantyAuditResponse object per line in file [" + currentAuditFile.getFileName() +  "]" 
								+ "  - " + obj.toString() + " " );
						*/
					}

				}
			} else if (qName.equals("p")) {
				handled = true;
				//logger.info("in AuditSaxParserHandler-endElement, unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
			} else {
				//logger.error("AuditSaxParserHandler-endElement, File [" + currentAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
				System.err.println("AuditSaxParserHandler-endElement, File [" + currentAuditFile.getFileName() + "] unrecognized handled qName [" + qName + "] localName  [" + localName + "] uri [" + uri + "] value [" + tempValue + "]");
			} 
			

			tempValue = null;
		}
		
	}
	
	
	
	public static void saxParseRequest(String xmlString, WarrantyAuditResponse obj, WarrantyAuditFile auditFile) {
		try {

			SAXParser parser = request.newSAXParser();	
			AuditRequestSaxParserHandler handler = new AuditRequestSaxParserHandler(obj, auditFile);
			parser.parse(new InputSource(new StringReader(xmlString)), handler);

		} catch (Exception e) {

			// try it one more time 
			try {
				String xmlString2 = escapeXMLCharacter(xmlString);
				SAXParser parser = request.newSAXParser();	
				AuditRequestSaxParserHandler handler = new AuditRequestSaxParserHandler(obj, auditFile);
				parser.parse(new InputSource(new StringReader(xmlString2)), handler);
			} catch (Exception ex) {
			
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				/*
				logger.error("Exception in processing file [" + auditFile.getFileName() +  "]" 
						+ " Request xmlString [" + xmlString + "]" + "Exception details: "+ sw.toString());
				*/
				System.err.println("Exception in processing file [" + auditFile.getFileName() +  "]" 
						+ " Request xmlString [" + xmlString + "]" + "Exception details: "+ sw.toString());
			}
		} 
	}

	private static void saxParseResponse(String xmlString, WarrantyAuditResponse obj, WarrantyAuditFile auditFile)  {
		try {
			
			SAXParser parser = request.newSAXParser();	
			AuditResponseSaxParserHandler responseHandler = new AuditResponseSaxParserHandler(obj, auditFile);
			parser.parse(new InputSource(new StringReader(xmlString)), responseHandler );

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.err.println("Exception in processing file [" + auditFile.getFileName() +  "]" 
					+ " Response xmlString [" + xmlString + "]" + "Exception details: "+ sw.toString());
			/*
			logger.error("Exception in processing file [" + auditFile.getFileName() +  "]" 
					+ " Response xmlString [" + xmlString + "]" + "Exception details: "+ sw.toString());
			*/

		} 
	}
	
	static public WarrantyAuditResponse parseLine (String strLine) {
		
		WarrantyAuditResponse retWarrantyAuditResponse = null;
		Date now = new Date();
		
		WarrantyAuditFile auditFile = new WarrantyAuditFile("" + now.getTime() );
		
		List <WarrantyAuditResponse> listWarrantyAuditResponse = new ArrayList<WarrantyAuditResponse>();
		List <WarrantyAuditFileInfo> listWarrantyAuditFileInfo = new ArrayList<WarrantyAuditFileInfo>();
		int idx = strLine.indexOf("<a");
		SAXParser parser;
		try {
			parser = spf.newSAXParser();
			if (idx>=0) {
				AuditSaxParserHandler handler;
				handler = new AuditSaxParserHandler(auditFile, listWarrantyAuditFileInfo, listWarrantyAuditResponse);
				parser.parse(new InputSource(new StringReader(strLine.substring(idx) )), handler);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (listWarrantyAuditResponse.size()==1) {
			retWarrantyAuditResponse = listWarrantyAuditResponse.get(0); 
		}
		
		return retWarrantyAuditResponse;

	}
	

	
	public static void main(String[] args) throws Exception {
		
		String line = "2011-11-01 06:59:59,811 INFO  [esp_audit] (Default[W/L]-13) <a i='1' t='11572165'><p><e><n>TIME</n><v>1320130799636</v></e><e><n>DELTAT</n><v>177</v></e><e><n>GUID</n><v>49401c38-089a-4519-9696-e646daa9542b</v></e><e><n>ERR_MSG</n><v></v></e><e><n>CONSUMER_APP_SEQ_NO</n><v>604076778</v></e><e><n>SVC_NAME</n><v>Warranty</v></e><e><n>RESPONSE_STATUS</n><v>OK</v></e><e><n>REQUEST_PAYLOAD</n><v>%3CWarrantyCheckRequest%3E%3CCheckWarrantyRequest_Type%3E%3CWarrantyData%3E%3CWarrantyData_In%3E%3CPartData%3E%3CPartData_In%3E%3CSerialNumber%3E8004964VA4S%3C%2FSerialNumber%3E%3CRequestSubmitDate%3E2011-11-01%3C%2FRequestSubmitDate%3E%3CCompTIA%3E%3C%2FCompTIA%3E%3CCountryOfPurchase%3ETW%3C%2FCountryOfPurchase%3E%3C%2FPartData_In%3E%3C%2FPartData%3E%3C%2FWarrantyData_In%3E%3C%2FWarrantyData%3E%3C%2FCheckWarrantyRequest_Type%3E%3C%2FWarrantyCheckRequest%3E</v></e><e><n>VERSION_NO</n><v>1.0</v></e><e><n>CONSUMER_IP</n><v>17.34.24.235</v></e><e><n>CONSUMER_APP_ID</n><v>149</v></e><e><n>PROVIDER_APP_ID</n><v>90</v></e><e><n>ESP_CLIENT_VERSION</n><v>1.3</v></e><e><n>SERVER_IP</n><v>17.34.144.250</v></e><e><n>RESPONSE_PAYLOAD</n><v>%3CWarrantyCheckResponse%3E%3CCheckWarrantyResponse_Type%3E%3CMessageHeader%3E%3CMessageHeader_Type%3E%3CID%3E%3C%2FID%3E%3CUUID%3E20111101-0659596377740-0659596825450%3C%2FUUID%3E%3CReferenceID%3E%3C%2FReferenceID%3E%3CReferenceUUID%3E20111101-0659596377740-0659596825450%3C%2FReferenceUUID%3E%3CApplicationSequenceNo%3E0000000000%3C%2FApplicationSequenceNo%3E%3CMessageType%3E%3C%2FMessageType%3E%3CProviderAppID%3E000%3C%2FProviderAppID%3E%3CConsumerAppID%3E%3C%2FConsumerAppID%3E%3CConsumerPersonID%3E%3C%2FConsumerPersonID%3E%3CPrimaryKey%3E%3C%2FPrimaryKey%3E%3COperationCode%3E%3C%2FOperationCode%3E%3CTimeStamp%3E%3C%2FTimeStamp%3E%3CEnvironment%3E%3C%2FEnvironment%3E%3CAttribute1%3E%3C%2FAttribute1%3E%3CAttribute2%3E%3C%2FAttribute2%3E%3CAttribute3%3E%3C%2FAttribute3%3E%3CAttribut4%3E%3C%2FAttribut4%3E%3C%2FMessageHeader_Type%3E%3C%2FMessageHeader%3E%3CWarrantyData%3E%3CWarrantyData_Out%3E%3CPartData%3E%3CPartData_Out%3E%3CSerialNumber%3E8004964VA4S%3C%2FSerialNumber%3E%3CRequestSubmitDate%3E2011-11-01%3C%2FRequestSubmitDate%3E%3CCoverageTypeCode%3ELP%3C%2FCoverageTypeCode%3E%3CCoverageStartDate%3E2010-12-14%3C%2FCoverageStartDate%3E%3CCoverageEndDate%3E2011-12-13%3C%2FCoverageEndDate%3E%3CPurchaseDate%3E2010-12-14%3C%2FPurchaseDate%3E%3CCountryOfPurchase%3ETW%3C%2FCountryOfPurchase%3E%3CCountryOfRegistration%3ETW%3C%2FCountryOfRegistration%3E%3CRegistrationDate%3E2010-12-13%3C%2FRegistrationDate%3E%3CMaterialNumber%3EMC603TA%2FA%3C%2FMaterialNumber%3E%3CSoldToPartyNumber%3E0000652282%3C%2FSoldToPartyNumber%3E%3CSoldToPartyName%3ECHUNGHWA+TELECOM+CO.%2C+LTD%3C%2FSoldToPartyName%3E%3CWarrantyCheckIndicators%3E%3CWarrantyCheckIndicators_Out%3E%3CLaborCoverage%3EY%3C%2FLaborCoverage%3E%3CPartCoverage%3EY%3C%2FPartCoverage%3E%3CLimitedWarranty%3EY%3C%2FLimitedWarranty%3E%3C%2FWarrantyCheckIndicators_Out%3E%3C%2FWarrantyCheckIndicators%3E%3C%2FPartData_Out%3E%3C%2FPartData%3E%3C%2FWarrantyData_Out%3E%3C%2FWarrantyData%3E%3C%2FCheckWarrantyResponse_Type%3E%3C%2FWarrantyCheckResponse%3E</v></e><e><n>OP_NAME</n><v>CheckWarranty</v></e><e><n>ESP_ENV</n><v>ESP-HT</v></e><e><n>REQUEST_TYPE</n><v>ESP_XML</v></e><e><n>CREQT</n><v>Tue+Nov+01+06%3A59%3A59+GMT%2B00%3A00+2011</v></e></p></a>";
		
		WarrantyAuditResponse warrantyAuditResponse =  WarrantyDataParser.parseLine (line);
		System.out.println(warrantyAuditResponse);
		
		String line2 = "2011-11-01 06:59:59,783 INFO  [esp_audit] (Default[W/L]-88) <a i='1' t='11572167'><p><e><n>TIME</n><v>1320130799731</v></e><e><n>DELTAT</n><v>53</v></e><e><n>GUID</n><v>d0c2dc0c-1830-4269-b70d-df1ce1af27c5</v></e><e><n>ERR_MSG</n><v>%3C%21%5BCDATA%5B%3Coperation-id%3EEHQNHvWXBzdqcVl_Gnm7QIbgP%3C%2Foperation-id%3E%5D%5D%3E</v></e><e><n>CONSUMER_APP_SEQ_NO</n><v>ADR_cb958cdb-32a5-47cd-b082-2c068809cf65</v></e><e><n>SVC_NAME</n><v>DiagnosticDataRetrieval</v></e><e><n>ERR_CD</n><v>ESP_INVOKER_LGCL_005_02</v></e><e><n>RESPONSE_STATUS</n><v>FAULT</v></e><e><n>REQUEST_PAYLOAD</n><v>%3CFetchIOSDiagnosticCaptureIdsBySerialNumber%3E%3CfetchIOSDiagnosticCaptureIdsBySerialNumberRequestWrapper%3E%3CFetchRequest%3E%3CfetchBySerialNumberRequestType%3E%3CserialNumber%3E%3Citem%3E85042QM9A4S%3C%2Fitem%3E%3C%2FserialNumber%3E%3CchannelId%3E%3Citem%3EGSX%3C%2Fitem%3E%3C%2FchannelId%3E%3CdiagnosticFetchWindow%3E%3Citem%3ET%3C%2Fitem%3E%3C%2FdiagnosticFetchWindow%3E%3C%2FfetchBySerialNumberRequestType%3E%3C%2FFetchRequest%3E%3C%2FfetchIOSDiagnosticCaptureIdsBySerialNumberRequestWrapper%3E%3C%2FFetchIOSDiagnosticCaptureIdsBySerialNumber%3E</v></e><e><n>VERSION_NO</n><v>1.0</v></e><e><n>CONSUMER_IP</n><v>17.34.147.250</v></e><e><n>CONSUMER_APP_ID</n><v>157</v></e><e><n>PROVIDER_APP_ID</n><v>1201</v></e><e><n>ESP_CLIENT_VERSION</n><v>1.4</v></e><e><n>SERVER_IP</n><v>17.34.144.250</v></e><e><n>RESPONSE_PAYLOAD</n><v></v></e><e><n>OP_NAME</n><v>FetchIOSDiagnosticCaptureIdsBySerialNumber</v></e><e><n>ESP_ENV</n><v>ESP-HT</v></e><e><n>REQUEST_TYPE</n><v>ESP_XML</v></e><e><n>CREQT</n><v>2011%2F11%2F01+06%3A59%3A59%3A674+%3A+%2B00%3A00</v></e></p></a>";
		warrantyAuditResponse =  WarrantyDataParser.parseLine (line2);
		System.out.println(warrantyAuditResponse );
		
		System.out.println("Done");
	}
}


final class RequestResponsePair<L,R> {

	  private final L request;
	  private final R response;

	  public RequestResponsePair(L request, R response) {
	    this.request = request;
	    this.response = response;
	  }

	  public L getRequest() { return request; }
	  public R getResponse() { return response; }

	  @Override
	  public int hashCode() { return request.hashCode() ^ response.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (o == null) return false;
	    if (!(o instanceof RequestResponsePair)) return false;
	    RequestResponsePair pairo = (RequestResponsePair) o;
	    return this.request.equals(pairo.getRequest()) &&
	           this.response.equals(pairo.getResponse());
	  }

}


