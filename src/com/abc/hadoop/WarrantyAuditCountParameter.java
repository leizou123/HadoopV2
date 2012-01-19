package com.abc.hadoop;

import java.io.Closeable;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class WarrantyAuditCountParameter {
	private static final WarrantyAuditCountParameter self = new WarrantyAuditCountParameter();
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Properties properties;
	
	private Date startDate;
	private long startDateTime;
	private Date endDate;
	private long endDateTime;
	
	private String servicesName;
	
	
	public static WarrantyAuditCountParameter getWarrantyAuditCountParameter() {
		return self;
	}

	public static WarrantyAuditCountParameter getInstance() {
		return self;
	}
	
	public boolean isTimeInRange(long longTime) {
		if (longTime>=startDateTime && longTime<endDateTime) 
			return true;
		else 
			return false;
	}
	
	public void init (String propFile) {
		if (propFile == null) {
			throw new IllegalArgumentException("main.config.file needs to be defined");
		}

		
		Configuration conf = new Configuration();
		FileSystem fs=null;
		
		FSDataInputStream input = null;
		Path inFile = new Path(propFile);
		try {
			fs = FileSystem.get(conf);
			
			input = fs.open(inFile);
			properties = new Properties();
			properties.load(input);
			
			startDate = parseDate ( this.getProperty("warranty.count.starttime", ""), dateFormat);
			startDateTime = startDate.getTime();
			endDate = parseDate ( this.getProperty("warranty.count.endtime", ""), dateFormat);
			endDateTime = endDate.getTime();
			servicesName = this.getProperty("warranty.count.svc.names", "");
			
		} catch (IOException e) {
			throw new IllegalArgumentException("error loading file[" + propFile + "]", e);
		} finally {
			closeQuietly(input);
		}
	}

	private WarrantyAuditCountParameter() {
	}
	
	public String getProperty(String prop, String defaultValue) {
		String value = properties.getProperty(prop);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
	
	public int getIntProperty(String prop, int defaultValue) {
		String value = properties.getProperty(prop);
		if (value == null) {
			return defaultValue;
		}
		int intValue = Integer.parseInt(value);
		return intValue;
	}

	public static void closeQuietly(Closeable ... closes) {
		if (closes != null) {
			for (Closeable close: closes) {
				if (close != null) {
					try {
						close.close();
					} catch (IOException e) {
						//ignore
					}
				}
			}
		}
	}

	
	public Date getStartDate() {
		return startDate;
	}

	
	public long getStartDateTime() {
		return startDateTime;
	}

	public Date getEndDate() {
		return endDate;
	}

	public long getEndDateTime() {
		return endDateTime;
	}

	public String getServicesName() {
		return servicesName;
	}


	static Date parseDate (String strDate, DateFormat dateFormat) {
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


	public static void main(String[] args) throws Exception {

		Date date1 = parseDate ("2011-11-01 00:00:00", dateFormat);
		long longTime = date1.getTime() ;
		System.out.println("date1 - " + date1.toString()  );
		System.out.println("longTime - " + longTime  );

		Date date2 = parseDate ("2011-11-01 00:00:01", dateFormat);
		long longTime2 = date2.getTime();
		System.out.println("date2 - " + date2.toString()  );
		System.out.println("longTime2 - " + longTime2  );

		System.out.println("time diff: - " + ( longTime2-longTime ) );
	}
	
}
