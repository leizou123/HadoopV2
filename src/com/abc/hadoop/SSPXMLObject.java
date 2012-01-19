package com.abc.hadoop;

import java.util.ArrayList;
import java.util.List;

public class SSPXMLObject {
	
	private String name;
	private List<SSPNameValue> nameValueList = new ArrayList<SSPNameValue>();

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public List<SSPNameValue> getNameValueList() {
		return nameValueList;
	}

	public void setNameValueList(List<SSPNameValue> nameValueList) {
		this.nameValueList = nameValueList;
	}
	public SSPXMLObject(String name) {
		super();
		this.name = name;
	}
	
	public void addNameValue(String name, String value) {
		SSPNameValue obj = new SSPNameValue(name, value);
		nameValueList.add(obj);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SSPXMLObject [name=").append(name)
				.append(", nameValueList=");
		
		for (SSPNameValue obj : nameValueList) {
			builder.append(obj.toString());
		}
		builder.append("]");

		return builder.toString();
	}
	
	

}


class SSPNameValue {
	private String name;
	private String value;
	
	public SSPNameValue() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SSPNameValue(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" ").append(name).append(":").append(value);
		return builder.toString();
	}
	
	
	
}