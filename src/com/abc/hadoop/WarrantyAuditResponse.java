package com.abc.hadoop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;




public class WarrantyAuditResponse implements Serializable {

	private Integer mId;	// = new Integer(100);
	private WarrantyAuditFile AuditFile;
	private WarrantyAuditFileErrMsg AuditErrorMsg; 
	
	private List<WarrantyAuditAssemblyData> 	AssemblyDatas;
	
	private String iValue;
	private String tValue;
	private long longTIME;
	private String strDELTAT;
	private String strGUID;
	//private String strERR_MSG;

	private String strCONSUMER_APP_SEQ_NO;

	private String strSVC_NAME;
	private String strRESPONSE_STATUS;
	private String strREQUEST_PAYLOAD;
	
	private String strVERSION_NO;
	private String strCONSUMER_IP;
	private String strCONSUMER_APP_ID;
	private String strPROVIDER_APP_ID;
	private String strSERVER_IP;
	private String strRESPONSE_PAYLOAD;
	private String strOP_NAME;
	private String strESP_ENV;
	private String strREQUEST_TYPE;

	private String strCREQT;
	private String strESP_CLIENT_VERSION;

	private String body;
	private String WarrantyCheckRequest;
	private String CheckWarrantyRequest_Type;
	private String WarrantyData;
	private String WarrantyData_In;
	private String PartData;
	private String PartData_In;
	private String SerialNumber;
	private Date RequestSubmitDate;
	private String CompTIA;
	private String CountryOfPurchase;
	private String ServicePartNumber;
	private String AssemblyData_In;
	private String AssemblyData;

	
	private String ID;
	private String UUID;
	private String ReferenceID;
	private String ReferenceUUID;
	private String ApplicationSequenceNo;
	private String MessageType;
	private String ProviderAppID;
	private String ConsumerAppID;
	private String ConsumerPersonID;
	private String PrimaryKey;
	private String OperationCode;
	private Date TimeStamp;
	private String Environment;
	private String Attribute1;
	private String Attribute2;
	private String Attribute3;
	private String Attribut4;

	private String Res_SerialNumber;
	private String Res_ID;
	private String Res_UUID;
	private String Res_ReferenceID;
	private String Res_ReferenceUUID;
	private String Res_ApplicationSequenceNo;
	private String Res_MessageType;
	private String Res_ProviderAppID;
	private String Res_ConsumerAppID;
	private String Res_ConsumerPersonID;
	private String Res_PrimaryKey;
	private String Res_OperationCode;
	private Date Res_TimeStamp;
	private String Res_Environment;
	private String Res_Attribute1;
	private String Res_Attribute2;
	private String Res_Attribute3;
	private String Res_Attribute4;
	private String Res_MessageHeader_Type;

	private String Res_SoldToPartyName;
	private String Res_CoverageReferenceNumber;
	
	private String Res_MessageHeader;
	private Date Res_RequestSubmitDate;
	private String Res_CoverageTypeCode;
	
	 
	private String Res_Assembly_ServicePartNumber;
	private String Res_Assembly_CoverageReferenceNumber;
	private String Res_Assembly_LaborCoverage;
	private String Res_Assembly_PartCoverage;

	

	private Date Res_CoverageStartDate;
	private Date Res_CoverageEndDate;
	private Date Res_PurchaseDate;
	private Date Res_ContractStartDate;
	private Date Res_ContractEndDate;

	
	private String Res_CountryOfPurchase;
	private String Res_CountryOfRegistration;
	private String Res_CompTIA;
	private Date Res_RegistrationDate;
	private String Res_MaterialNumber;
	private String Res_SoldToPartyNumber;
	private String Res_ContractType;

	private String Res_SLAGroup;
	private String Res_PartReturnable;
	private String Res_PowerTrainWarrantyCoverage;
	private String Res_FormatInvalid;
	private String Res_WarrantyNotFound; 
	private String Res_SerialNumberNotFound;
	private String Res_IMCCountry;
	private String Res_OnsiteRepairAuthorized; 
	private String Res_TricareCoverage; 
	private String Res_ChinaHardBundle; 
	private String Res_ExtendedOnsiteCoverage; 
   
      

	
	private String Res_WarrantyCheck_LaborCoverage;
	private String Res_WarrantyCheck_PartCoverage;
	private String Res_WarrantyCheck_LimitedWarranty;
	private String Res_WarrantyCheck_AccidentalDamageCoverage;
	private String Res_WarrantyCheck_GlobalRepairAuthorized;
	private String Res_WarrantyCheck_WarrantyCheckIndicators_Out;

	private String Res_ExtData_PersonalizedProduct;
	private String Res_ExtData_PurchaseDirect;
	private String Res_ExtData_ExtendedPartData_Out;

	
	
	
	
	@Override
	public String toString() {
		return toStringShortForm ();
	}
	
	public String toStringLongForm() {
		StringBuilder builder = new StringBuilder();
		builder.append("WarrantyAuditResponse [longTIME=").append(longTIME)
				.append(", strDELTAT=").append(strDELTAT).append(", strGUID=")
				.append(strGUID).append(", strCONSUMER_APP_SEQ_NO=")
				.append(strCONSUMER_APP_SEQ_NO).append(", strSVC_NAME=")
				.append(strSVC_NAME).append(", strRESPONSE_STATUS=")
				.append(strRESPONSE_STATUS)
				.append(", strVERSION_NO=")
				.append(strVERSION_NO).append(", strCONSUMER_IP=")
				.append(strCONSUMER_IP).append(", strCONSUMER_APP_ID=")
				.append(strCONSUMER_APP_ID).append(", strPROVIDER_APP_ID=")
				.append(strPROVIDER_APP_ID).append(", strSERVER_IP=")
				.append(strSERVER_IP).append(", strOP_NAME=")
				.append(strOP_NAME).append(", strESP_ENV=").append(strESP_ENV)
				.append(", strREQUEST_TYPE=").append(strREQUEST_TYPE)
				.append("]");
		return builder.toString();
	}


	public String toStringShortForm () {
		StringBuilder builder = new StringBuilder();
		builder.append("WarrantyAuditResponse [strGUID=").append(strGUID)
				.append("]");
		return builder.toString();
	}
	

	public static String parseSort(String input, String delims) {
		if (input==null) return input;
		if (input.length()==0) return input;
		
		String[] tokens = input.split(delims);
		
		List <String> list = new ArrayList<String> ();
		for (int i = 0; i < tokens.length; i++) {
			list.add(tokens[i]);
		}
		String retString  = "";
		Collections.sort(list);
		for (String a : list) {
			if (retString.length()>0) {
				retString = retString + delims + a;
			} else {
				retString = a;
			}
			
		}
		return retString;
	}

	public String getRes_SerialNumber() {
		return Res_SerialNumber;
	}


	public void setRes_SerialNumber(String res_SerialNumber) {
		Res_SerialNumber = res_SerialNumber;
	}


	public WarrantyAuditResponse(WarrantyAuditFile AuditFile, 
			String iValue, String tValue, String strTIME,
			String strDELTAT, String strGUID, String strERR_MSG,
			String strCONSUMER_APP_SEQ_NO, String strSVC_NAME,
			String strRESPONSE_STATUS, String strREQUEST_PAYLOAD,
			String strVERSION_NO, String strCONSUMER_IP,
			String strCONSUMER_APP_ID, String strPROVIDER_APP_ID,
			String strSERVER_IP, String strRESPONSE_PAYLOAD, String strOP_NAME,
			String strESP_ENV, String strREQUEST_TYPE) {
		
		super();
		
		this.AuditFile = AuditFile;
		
		this.iValue = iValue;
		this.tValue = tValue;
		
		this.longTIME = Long.parseLong(strTIME);
		
		this.strDELTAT = strDELTAT;
		this.strGUID = strGUID;
		//this.strERR_MSG = strERR_MSG;
		this.strCONSUMER_APP_SEQ_NO = strCONSUMER_APP_SEQ_NO;
		this.strSVC_NAME = strSVC_NAME;
		this.strRESPONSE_STATUS = strRESPONSE_STATUS;
		this.strREQUEST_PAYLOAD = strREQUEST_PAYLOAD;
		this.strVERSION_NO = strVERSION_NO;
		this.strCONSUMER_IP = strCONSUMER_IP;
		this.strCONSUMER_APP_ID = strCONSUMER_APP_ID;
		this.strPROVIDER_APP_ID = strPROVIDER_APP_ID;
		this.strSERVER_IP = strSERVER_IP;
		this.strRESPONSE_PAYLOAD = strRESPONSE_PAYLOAD;
		this.strOP_NAME = strOP_NAME;
		this.strESP_ENV = strESP_ENV;
		this.strREQUEST_TYPE = strREQUEST_TYPE;
		
		
		AuditErrorMsg = new WarrantyAuditFileErrMsg();
		AuditErrorMsg.setStrERR_MSG(strERR_MSG) ;
		AuditErrorMsg.setAuditResponse(this);
	}
	
	
	public WarrantyAuditResponse() {
		super();
		
		AuditErrorMsg = new WarrantyAuditFileErrMsg();
		AuditErrorMsg.setStrERR_MSG("") ;
		AuditErrorMsg.setAuditResponse(this);

		// TODO Auto-generated constructor stub
	}

	


	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	
	public String getiValue() {
		return iValue;
	}
	public String gettValue() {
		return tValue;
	}
	public long getLongTIME() {
		return longTIME;
	}
	public String getStrDELTAT() {
		return strDELTAT;
	}
	public String getStrGUID() {
		return strGUID;
	}
	
	public String getStrCONSUMER_APP_SEQ_NO() {
		return strCONSUMER_APP_SEQ_NO;
	}
	public String getStrSVC_NAME() {
		return strSVC_NAME;
	}
	public String getStrRESPONSE_STATUS() {
		return strRESPONSE_STATUS;
	}
	public String getStrREQUEST_PAYLOAD() {
		return strREQUEST_PAYLOAD;
	}
	public String getStrVERSION_NO() {
		return strVERSION_NO;
	}
	public String getStrCONSUMER_IP() {
		return strCONSUMER_IP;
	}
	public String getStrCONSUMER_APP_ID() {
		return strCONSUMER_APP_ID;
	}
	public String getStrPROVIDER_APP_ID() {
		return strPROVIDER_APP_ID;
	}
	public String getStrSERVER_IP() {
		return strSERVER_IP;
	}
	public String getStrRESPONSE_PAYLOAD() {
		return strRESPONSE_PAYLOAD;
	}
	public String getStrOP_NAME() {
		return strOP_NAME;
	}
	public String getStrESP_ENV() {
		return strESP_ENV;
	}
	public String getStrREQUEST_TYPE() {
		return strREQUEST_TYPE;
	}
	public void setiValue(String iValue) {
		this.iValue = iValue;
	}
	public void settValue(String tValue) {
		this.tValue = tValue;
	}
	public void setLongTIME(long longTIME) {
		this.longTIME = longTIME;
	}
	public void setStrDELTAT(String strDELTAT) {
		this.strDELTAT = strDELTAT;
	}
	public void setStrGUID(String strGUID) {
		this.strGUID = strGUID;
	}

	public void setStrCONSUMER_APP_SEQ_NO(String strCONSUMER_APP_SEQ_NO) {
		this.strCONSUMER_APP_SEQ_NO = strCONSUMER_APP_SEQ_NO;
	}
	public void setStrSVC_NAME(String strSVC_NAME) {
		this.strSVC_NAME = strSVC_NAME;
	}
	public void setStrRESPONSE_STATUS(String strRESPONSE_STATUS) {
		this.strRESPONSE_STATUS = strRESPONSE_STATUS;
	}
	public void setStrREQUEST_PAYLOAD(String strREQUEST_PAYLOAD) {
		this.strREQUEST_PAYLOAD = strREQUEST_PAYLOAD;
	}
	public void setStrVERSION_NO(String strVERSION_NO) {
		this.strVERSION_NO = strVERSION_NO;
	}
	public void setStrCONSUMER_IP(String strCONSUMER_IP) {
		this.strCONSUMER_IP = strCONSUMER_IP;
	}
	public void setStrCONSUMER_APP_ID(String strCONSUMER_APP_ID) {
		this.strCONSUMER_APP_ID = strCONSUMER_APP_ID;
	}
	public void setStrPROVIDER_APP_ID(String strPROVIDER_APP_ID) {
		this.strPROVIDER_APP_ID = strPROVIDER_APP_ID;
	}
	public void setStrSERVER_IP(String strSERVER_IP) {
		this.strSERVER_IP = strSERVER_IP;
	}
	public void setStrRESPONSE_PAYLOAD(String strRESPONSE_PAYLOAD) {
		this.strRESPONSE_PAYLOAD = strRESPONSE_PAYLOAD;
	}
	public void setStrOP_NAME(String strOP_NAME) {
		this.strOP_NAME = strOP_NAME;
	}
	public void setStrESP_ENV(String strESP_ENV) {
		this.strESP_ENV = strESP_ENV;
	}
	public void setStrREQUEST_TYPE(String strREQUEST_TYPE) {
		this.strREQUEST_TYPE = strREQUEST_TYPE;
	}


	public String getWarrantyCheckRequest() {
		return WarrantyCheckRequest;
	}


	public String getCheckWarrantyRequest_Type() {
		return CheckWarrantyRequest_Type;
	}


	public String getWarrantyData() {
		return WarrantyData;
	}


	public String getWarrantyData_In() {
		return WarrantyData_In;
	}


	public String getPartData() {
		return PartData;
	}


	public String getPartData_In() {
		return PartData_In;
	}


	public String getSerialNumber() {
		return SerialNumber;
	}


	public Date getRequestSubmitDate() {
		return RequestSubmitDate;
	}


	public String getCompTIA() {
		return CompTIA;
	}


	public String getCountryOfPurchase() {
		return CountryOfPurchase;
	}


	public void setWarrantyCheckRequest(String warrantyCheckRequest) {
		WarrantyCheckRequest = warrantyCheckRequest;
	}


	public void setCheckWarrantyRequest_Type(String checkWarrantyRequest_Type) {
		CheckWarrantyRequest_Type = checkWarrantyRequest_Type;
	}


	public void setWarrantyData(String warrantyData) {
		WarrantyData = warrantyData;
	}


	public void setWarrantyData_In(String warrantyData_In) {
		WarrantyData_In = warrantyData_In;
	}


	public void setPartData(String partData) {
		PartData = partData;
	}


	public void setPartData_In(String partData_In) {
		PartData_In = partData_In;
	}


	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}


	public void setRequestSubmitDate(Date requestSubmitDate) {
		RequestSubmitDate = requestSubmitDate;
	}


	public void setCompTIA(String compTIA) {
		CompTIA = compTIA;
	}


	public void setCountryOfPurchase(String countryOfPurchase) {
		CountryOfPurchase = countryOfPurchase;
	}


	public String getServicePartNumber() {
		return ServicePartNumber;
	}

	

	public String getID() {
		return ID;
	}


	public String getUUID() {
		return UUID;
	}


	public String getReferenceID() {
		return ReferenceID;
	}


	public String getReferenceUUID() {
		return ReferenceUUID;
	}


	public String getApplicationSequenceNo() {
		return ApplicationSequenceNo;
	}


	public String getMessageType() {
		return MessageType;
	}


	public String getProviderAppID() {
		return ProviderAppID;
	}


	public String getConsumerAppID() {
		return ConsumerAppID;
	}


	public String getConsumerPersonID() {
		return ConsumerPersonID;
	}


	public String getPrimaryKey() {
		return PrimaryKey;
	}


	public String getOperationCode() {
		return OperationCode;
	}


	public Date getTimeStamp() {
		return TimeStamp;
	}


	public String getEnvironment() {
		return Environment;
	}


	public String getAttribute1() {
		return Attribute1;
	}


	public String getAttribute2() {
		return Attribute2;
	}


	public String getAttribute3() {
		return Attribute3;
	}


	public String getAttribut4() {
		return Attribut4;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public void setUUID(String uUID) {
		UUID = uUID;
	}


	public void setReferenceID(String referenceID) {
		ReferenceID = referenceID;
	}


	public void setReferenceUUID(String referenceUUID) {
		ReferenceUUID = referenceUUID;
	}


	public void setApplicationSequenceNo(String applicationSequenceNo) {
		ApplicationSequenceNo = applicationSequenceNo;
	}


	public void setMessageType(String messageType) {
		MessageType = messageType;
	}


	public void setProviderAppID(String providerAppID) {
		ProviderAppID = providerAppID;
	}


	public void setConsumerAppID(String consumerAppID) {
		ConsumerAppID = consumerAppID;
	}


	public void setConsumerPersonID(String consumerPersonID) {
		ConsumerPersonID = consumerPersonID;
	}


	public void setPrimaryKey(String primaryKey) {
		PrimaryKey = primaryKey;
	}


	public void setOperationCode(String operationCode) {
		OperationCode = operationCode;
	}


	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}


	public void setEnvironment(String environment) {
		Environment = environment;
	}


	public void setAttribute1(String attribute1) {
		Attribute1 = attribute1;
	}


	public void setAttribute2(String attribute2) {
		Attribute2 = attribute2;
	}


	public void setAttribute3(String attribute3) {
		Attribute3 = attribute3;
	}


	public void setAttribut4(String attribut4) {
		Attribut4 = attribut4;
	}


	public String getAssemblyData_In() {
		return AssemblyData_In;
	}


	public String getAssemblyData() {
		return AssemblyData;
	}


	public void setServicePartNumber(String servicePartNumber) {
		ServicePartNumber = servicePartNumber;
	}


	public void setAssemblyData_In(String assemblyData_In) {
		AssemblyData_In = assemblyData_In;
	}


	public void setAssemblyData(String assemblyData) {
		AssemblyData = assemblyData;
	}


	public String getRes_ID() {
		return Res_ID;
	}


	public String getRes_UUID() {
		return Res_UUID;
	}


	public String getRes_ReferenceID() {
		return Res_ReferenceID;
	}


	public String getRes_ReferenceUUID() {
		return Res_ReferenceUUID;
	}


	public String getRes_ApplicationSequenceNo() {
		return Res_ApplicationSequenceNo;
	}


	public String getRes_MessageType() {
		return Res_MessageType;
	}


	public String getRes_ProviderAppID() {
		return Res_ProviderAppID;
	}


	public String getRes_ConsumerAppID() {
		return Res_ConsumerAppID;
	}


	public String getRes_ConsumerPersonID() {
		return Res_ConsumerPersonID;
	}


	public String getRes_PrimaryKey() {
		return Res_PrimaryKey;
	}


	public String getRes_OperationCode() {
		return Res_OperationCode;
	}


	public Date getRes_TimeStamp() {
		return Res_TimeStamp;
	}


	public String getRes_Environment() {
		return Res_Environment;
	}


	public String getRes_Attribute1() {
		return Res_Attribute1;
	}


	public String getRes_Attribute2() {
		return Res_Attribute2;
	}


	public String getRes_Attribute3() {
		return Res_Attribute3;
	}


	public String getRes_Attribute4() {
		return Res_Attribute4;
	}


	public String getRes_MessageHeader_Type() {
		return Res_MessageHeader_Type;
	}


	public void setRes_ID(String res_ID) {
		Res_ID = res_ID;
	}


	public void setRes_UUID(String res_UUID) {
		Res_UUID = res_UUID;
	}


	public void setRes_ReferenceID(String res_ReferenceID) {
		Res_ReferenceID = res_ReferenceID;
	}


	public void setRes_ReferenceUUID(String res_ReferenceUUID) {
		Res_ReferenceUUID = res_ReferenceUUID;
	}


	public void setRes_ApplicationSequenceNo(String res_ApplicationSequenceNo) {
		Res_ApplicationSequenceNo = res_ApplicationSequenceNo;
	}


	public void setRes_MessageType(String res_MessageType) {
		Res_MessageType = res_MessageType;
	}


	public void setRes_ProviderAppID(String res_ProviderAppID) {
		Res_ProviderAppID = res_ProviderAppID;
	}


	public void setRes_ConsumerAppID(String res_ConsumerAppID) {
		Res_ConsumerAppID = res_ConsumerAppID;
	}


	public void setRes_ConsumerPersonID(String res_ConsumerPersonID) {
		Res_ConsumerPersonID = res_ConsumerPersonID;
	}


	public void setRes_PrimaryKey(String res_PrimaryKey) {
		Res_PrimaryKey = res_PrimaryKey;
	}


	public void setRes_OperationCode(String res_OperationCode) {
		Res_OperationCode = res_OperationCode;
	}


	public void setRes_TimeStamp(Date res_TimeStamp) {
		Res_TimeStamp = res_TimeStamp;
	}


	public void setRes_Environment(String res_Environment) {
		Res_Environment = res_Environment;
	}


	public void setRes_Attribute1(String res_Attribute1) {
		Res_Attribute1 = res_Attribute1;
	}


	public void setRes_Attribute2(String res_Attribute2) {
		Res_Attribute2 = res_Attribute2;
	}


	public void setRes_Attribute3(String res_Attribute3) {
		Res_Attribute3 = res_Attribute3;
	}


	public void setRes_Attribute4(String res_Attribute4) {
		Res_Attribute4 = res_Attribute4;
	}


	public void setRes_MessageHeader_Type(String res_MessageHeader_Type) {
		Res_MessageHeader_Type = res_MessageHeader_Type;
	}


	public String getRes_MessageHeader() {
		return Res_MessageHeader;
	}


	public Date getRes_RequestSubmitDate() {
		return Res_RequestSubmitDate;
	}


	public String getRes_CoverageTypeCode() {
		return Res_CoverageTypeCode;
	}



	public String getRes_CountryOfPurchase() {
		return Res_CountryOfPurchase;
	}


	public String getRes_CountryOfRegistration() {
		return Res_CountryOfRegistration;
	}


	public String getRes_CompTIA() {
		return Res_CompTIA;
	}


	public Date getRes_RegistrationDate() {
		return Res_RegistrationDate;
	}


	public String getRes_MaterialNumber() {
		return Res_MaterialNumber;
	}


	public String getRes_ContractType() {
		return Res_ContractType;
	}


	public void setRes_ContractType(String res_ContractType) {
		Res_ContractType = res_ContractType;
	}


	public String getRes_SoldToPartyNumber() {
		return Res_SoldToPartyNumber;
	}


	public void setRes_SoldToPartyNumber(String res_SoldToPartyNumber) {
		Res_SoldToPartyNumber = res_SoldToPartyNumber;
	}


	public void setRes_MessageHeader(String res_MessageHeader) {
		Res_MessageHeader = res_MessageHeader;
	}


	public void setRes_RequestSubmitDate(Date res_RequestSubmitDate) {
		Res_RequestSubmitDate = res_RequestSubmitDate;
	}


	public void setRes_CoverageTypeCode(String res_CoverageTypeCode) {
		Res_CoverageTypeCode = res_CoverageTypeCode;
	}








	public void setRes_CountryOfPurchase(String res_CountryOfPurchase) {
		Res_CountryOfPurchase = res_CountryOfPurchase;
	}


	public void setRes_CountryOfRegistration(String res_CountryOfRegistration) {
		Res_CountryOfRegistration = res_CountryOfRegistration;
	}


	public void setRes_CompTIA(String res_CompTIA) {
		Res_CompTIA = res_CompTIA;
	}


	public void setRes_RegistrationDate(Date res_RegistrationDate) {
		Res_RegistrationDate = res_RegistrationDate;
	}


	public void setRes_MaterialNumber(String res_MaterialNumber) {
		Res_MaterialNumber = res_MaterialNumber;
	}


	public String getRes_WarrantyCheck_LaborCoverage() {
		return Res_WarrantyCheck_LaborCoverage;
	}


	public String getRes_WarrantyCheck_PartCoverage() {
		return Res_WarrantyCheck_PartCoverage;
	}


	public String getRes_WarrantyCheck_LimitedWarranty() {
		return Res_WarrantyCheck_LimitedWarranty;
	}

	public String getRes_WarrantyCheck_AccidentalDamageCoverage() {
		return Res_WarrantyCheck_AccidentalDamageCoverage;
	}


	public String getRes_WarrantyCheck_GlobalRepairAuthorized() {
		return Res_WarrantyCheck_GlobalRepairAuthorized;
	}


	public String getRes_WarrantyCheck_WarrantyCheckIndicators_Out() {
		return Res_WarrantyCheck_WarrantyCheckIndicators_Out;
	}


	public void setRes_WarrantyCheck_LaborCoverage(
			String res_WarrantyCheck_LaborCoverage) {
		Res_WarrantyCheck_LaborCoverage = res_WarrantyCheck_LaborCoverage;
	}


	public void setRes_WarrantyCheck_PartCoverage(
			String res_WarrantyCheck_PartCoverage) {
		Res_WarrantyCheck_PartCoverage = res_WarrantyCheck_PartCoverage;
	}


	public void setRes_WarrantyCheck_LimitedWarranty(
			String res_WarrantyCheck_LimitedWarranty) {
		Res_WarrantyCheck_LimitedWarranty = res_WarrantyCheck_LimitedWarranty;
	}

	public void setRes_WarrantyCheck_AccidentalDamageCoverage( String res_WarrantyCheck_AccidentalDamageCoverage) {
		Res_WarrantyCheck_AccidentalDamageCoverage=res_WarrantyCheck_AccidentalDamageCoverage;
	}


	public void setRes_WarrantyCheck_GlobalRepairAuthorized(
			String res_WarrantyCheck_GlobalRepairAuthorized) {
		Res_WarrantyCheck_GlobalRepairAuthorized = res_WarrantyCheck_GlobalRepairAuthorized;
	}


	public void setRes_WarrantyCheck_WarrantyCheckIndicators_Out(
			String res_WarrantyCheck_WarrantyCheckIndicators_Out) {
		Res_WarrantyCheck_WarrantyCheckIndicators_Out = res_WarrantyCheck_WarrantyCheckIndicators_Out;
	}

	
	
	public String getRes_Assembly_CoverageReferenceNumber() {
		return Res_Assembly_CoverageReferenceNumber;
	}

	public void setRes_Assembly_CoverageReferenceNumber(
			String res_Assembly_CoverageReferenceNumber) {
		Res_Assembly_CoverageReferenceNumber = res_Assembly_CoverageReferenceNumber;
	}

	public String getRes_Assembly_ServicePartNumber() {
		return Res_Assembly_ServicePartNumber;
	}
	public String getRes_Assembly_LaborCoverage() {
		return Res_Assembly_LaborCoverage;
	}
	public String getRes_Assembly_PartCoverage() {
		return Res_Assembly_PartCoverage;
	}
	
	
	public void setRes_Assembly_ServicePartNumber(
			String res_Assembly_ServicePartNumber) {
		if (Res_Assembly_ServicePartNumber==null) {
			Res_Assembly_ServicePartNumber = res_Assembly_ServicePartNumber;
		} else {
			Res_Assembly_ServicePartNumber = Res_Assembly_ServicePartNumber + "," + res_Assembly_ServicePartNumber;
			Res_Assembly_ServicePartNumber = WarrantyAuditResponse.parseSort(Res_Assembly_ServicePartNumber, ",");
		}
	}
	
	public void setRes_Assembly_LaborCoverage(String res_Assembly_LaborCoverage) {
		Res_Assembly_LaborCoverage = res_Assembly_LaborCoverage;
	}
	public void setRes_Assembly_PartCoverage(String res_Assembly_PartCoverage) {
		Res_Assembly_PartCoverage = res_Assembly_PartCoverage;
	}
	public String getRes_ExtData_PersonalizedProduct() {
		return Res_ExtData_PersonalizedProduct;
	}
	public String getRes_ExtData_PurchaseDirect() {
		return Res_ExtData_PurchaseDirect;
	}
	public String getRes_ExtData_ExtendedPartData_Out() {
		return Res_ExtData_ExtendedPartData_Out;
	}
	public void setRes_ExtData_PersonalizedProduct(
			String res_ExtData_PersonalizedProduct) {
		Res_ExtData_PersonalizedProduct = res_ExtData_PersonalizedProduct;
	}
	public void setRes_ExtData_PurchaseDirect(String res_ExtData_PurchaseDirect) {
		Res_ExtData_PurchaseDirect = res_ExtData_PurchaseDirect;
	}


	public void setRes_ExtData_ExtendedPartData_Out(
			String res_ExtData_ExtendedPartData_Out) {
		Res_ExtData_ExtendedPartData_Out = res_ExtData_ExtendedPartData_Out;
	}


	public WarrantyAuditFile getAuditFile() {
		return AuditFile;
	}


	public void setAuditFile(WarrantyAuditFile auditFile) {
		AuditFile = auditFile;
	}


	public Date getRes_CoverageStartDate() {
		return Res_CoverageStartDate;
	}


	public Date getRes_CoverageEndDate() {
		return Res_CoverageEndDate;
	}


	public Date getRes_PurchaseDate() {
		return Res_PurchaseDate;
	}


	public Date getRes_ContractStartDate() {
		return Res_ContractStartDate;
	}


	public Date getRes_ContractEndDate() {
		return Res_ContractEndDate;
	}


	public void setRes_CoverageStartDate(Date res_CoverageStartDate) {
		Res_CoverageStartDate = res_CoverageStartDate;
	}


	public void setRes_CoverageEndDate(Date res_CoverageEndDate) {
		Res_CoverageEndDate = res_CoverageEndDate;
	}


	public void setRes_PurchaseDate(Date res_PurchaseDate) {
		Res_PurchaseDate = res_PurchaseDate;
	}


	public void setRes_ContractStartDate(Date res_ContractStartDate) {
		Res_ContractStartDate = res_ContractStartDate;
	}


	public void setRes_ContractEndDate(Date res_ContractEndDate) {
		Res_ContractEndDate = res_ContractEndDate;
	}


	public String getRes_SoldToPartyName() {
		return Res_SoldToPartyName;
	}


	public String getRes_CoverageReferenceNumber() {
		return Res_CoverageReferenceNumber;
	}


	public void setRes_SoldToPartyName(String res_SoldToPartyName) {
		Res_SoldToPartyName = res_SoldToPartyName;
	}


	public void setRes_CoverageReferenceNumber(String res_CoverageReferenceNumber) {
		Res_CoverageReferenceNumber = res_CoverageReferenceNumber;
	}


	public String getRes_SLAGroup() {
		return Res_SLAGroup;
	}


	public String getRes_PartReturnable() {
		return Res_PartReturnable;
	}


	public String getRes_PowerTrainWarrantyCoverage() {
		return Res_PowerTrainWarrantyCoverage;
	}


	public String getRes_FormatInvalid() {
		return Res_FormatInvalid;
	}


	public String getRes_WarrantyNotFound() {
		return Res_WarrantyNotFound;
	}


	public String getRes_SerialNumberNotFound() {
		return Res_SerialNumberNotFound;
	}


	public String getRes_IMCCountry() {
		return Res_IMCCountry;
	}


	public void setRes_SLAGroup(String res_SLAGroup) {
		Res_SLAGroup = res_SLAGroup;
	}


	public void setRes_PartReturnable(String res_PartReturnable) {
		Res_PartReturnable = res_PartReturnable;
	}


	public void setRes_PowerTrainWarrantyCoverage(
			String res_PowerTrainWarrantyCoverage) {
		Res_PowerTrainWarrantyCoverage = res_PowerTrainWarrantyCoverage;
	}


	public void setRes_FormatInvalid(String res_FormatInvalid) {
		Res_FormatInvalid = res_FormatInvalid;
	}


	public void setRes_WarrantyNotFound(String res_WarrantyNotFound) {
		Res_WarrantyNotFound = res_WarrantyNotFound;
	}


	public void setRes_SerialNumberNotFound(String res_SerialNumberNotFound) {
		Res_SerialNumberNotFound = res_SerialNumberNotFound;
	}


	public void setRes_IMCCountry(String res_IMCCountry) {
		Res_IMCCountry = res_IMCCountry;
	}


	public String getRes_OnsiteRepairAuthorized() {
		return Res_OnsiteRepairAuthorized;
	}


	public void setRes_OnsiteRepairAuthorized(String res_OnsiteRepairAuthorized) {
		Res_OnsiteRepairAuthorized = res_OnsiteRepairAuthorized;
	}


	public String getRes_TricareCoverage() {
		return Res_TricareCoverage;
	}


	public String getRes_ChinaHardBundle() {
		return Res_ChinaHardBundle;
	}


	public String getRes_ExtendedOnsiteCoverage() {
		return Res_ExtendedOnsiteCoverage;
	}


	public void setRes_TricareCoverage(String res_TricareCoverage) {
		Res_TricareCoverage = res_TricareCoverage;
	}


	public void setRes_ChinaHardBundle(String res_ChinaHardBundle) {
		Res_ChinaHardBundle = res_ChinaHardBundle;
	}


	public void setRes_ExtendedOnsiteCoverage(String res_ExtendedOnsiteCoverage) {
		Res_ExtendedOnsiteCoverage = res_ExtendedOnsiteCoverage;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	
	public String getStrCREQT() {
		return strCREQT;
	}

	public void setStrCREQT(String strCREQT) {
		this.strCREQT = strCREQT;
	}

	public String getStrESP_CLIENT_VERSION() {
		return strESP_CLIENT_VERSION;
	}

	public void setStrESP_CLIENT_VERSION(String strESP_CLIENT_VERSION) {
		this.strESP_CLIENT_VERSION = strESP_CLIENT_VERSION;
	}

	public WarrantyAuditFileErrMsg getAuditErrorMsg() {
		return AuditErrorMsg;
	}


	public void setAuditErrorMsg(WarrantyAuditFileErrMsg auditErrorMsg) {
		AuditErrorMsg = auditErrorMsg;
	}

	public List<WarrantyAuditAssemblyData> getAssemblyDatas() {
		return AssemblyDatas;
	}

	
	public void setAssemblyDatas(List<WarrantyAuditAssemblyData> assemblyDatas) {
		this.AssemblyDatas = assemblyDatas;
		Collections.sort(this.AssemblyDatas);
		this.Res_Assembly_ServicePartNumber = this.toListServicePartNumber();
		this.Res_Assembly_CoverageReferenceNumber = this.toListCoverageReferenceNumber();
		this.Res_Assembly_LaborCoverage = this.toListLaborCoverage();
		this.Res_Assembly_PartCoverage = this.toListPartCoverage();
	}

	private String toListPartCoverage () {
		StringBuffer buf = new StringBuffer();
		if (AssemblyDatas!=null) {
			for (int i=0; i<AssemblyDatas.size(); i++) {
				if (i>0) {
					buf.append(",");
				}
				buf.append(AssemblyDatas.get(i).getAssembly_PartCoverage());
			}
		}
		return buf.toString();
	}

	
	private String toListLaborCoverage () {
		StringBuffer buf = new StringBuffer();
		if (AssemblyDatas!=null) {
			for (int i=0; i<AssemblyDatas.size(); i++) {
				if (i>0) {
					buf.append(",");
				}
				buf.append(AssemblyDatas.get(i).getAssembly_LaborCoverage());
			}
		}
		return buf.toString();
	}

	
	private String toListCoverageReferenceNumber () {
		StringBuffer buf = new StringBuffer();
		if (AssemblyDatas!=null) {
			for (int i=0; i<AssemblyDatas.size(); i++) {
				if (i>0) {
					buf.append(",");
				}
				buf.append(AssemblyDatas.get(i).getAssembly_CoverageReferenceNumber());
			}
		}
		return buf.toString();
	}

	private String toListServicePartNumber () {
		StringBuffer buf = new StringBuffer();
		if (AssemblyDatas!=null) {
			for (int i=0; i<AssemblyDatas.size(); i++) {
				if (i>0) {
					buf.append(",");
				}
				buf.append(AssemblyDatas.get(i).getAssembly_ServicePartNumber());
			}
		}
		return buf.toString();
	}

	
}
