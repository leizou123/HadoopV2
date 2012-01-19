package com.abc.hadoop;

import java.io.*;
import java.util.*;
 	
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;

import org.apache.hadoop.util.ToolRunner;


public class CountResponseSerialNumberV2 extends Configured implements Tool {
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, WarrantyAuditDataCount> { 
		static enum Counters { VALID_RECORDS_COUNT, HEADER_ONLY_CALLS }
		 	
		private final static IntWritable one = new IntWritable(1);
		//private Text word = new Text();
		 	
		 	
		private long numRecords = 0;
		private String inputFile;
		 	
		public void configure(JobConf job) {
			
			inputFile = job.get("map.input.file");
			String SVC_NAME = job.get("SVC_NAME");
			System.out.println("lei - " + SVC_NAME);
			
			String propFile = job.get("main.config.file");
			System.out.println("lei - propFile " + propFile);
			
			WarrantyAuditCountParameter.getWarrantyAuditCountParameter().init(propFile);
			
			String val = WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getProperty("warranty.count.endtime", "");
			System.out.println("lei val - " + val);			
			System.out.println("startTime - " + WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getStartDateTime()  );
			System.out.println("endTime - " + WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getEndDateTime()  );
		}
		

		public void map(LongWritable key, Text value, OutputCollector<Text, WarrantyAuditDataCount> output, Reporter reporter) 
		throws IOException {
			String line = value.toString() ;
			//System.out.println("line - " + line);
			WarrantyAuditResponse warrantyAuditResponse =  WarrantyDataParser.parseLine (line);
			
			if (warrantyAuditResponse!=null && warrantyAuditResponse.getRes_SerialNumber()!=null ) {
				WarrantyAuditDataCount warrantyAuditDataCount = new WarrantyAuditDataCount( warrantyAuditResponse.getLongTIME(), 
						warrantyAuditResponse.getRes_SerialNumber(),
						warrantyAuditResponse.getRes_CoverageTypeCode(),
						warrantyAuditResponse.getRes_Assembly_ServicePartNumber(),
						warrantyAuditResponse.getStrGUID(),
						warrantyAuditResponse.getStrCONSUMER_APP_ID(),
						warrantyAuditResponse.getRes_CoverageTypeCode() );


				if ( WarrantyAuditCountParameter.getWarrantyAuditCountParameter().isTimeInRange(warrantyAuditDataCount.getLongTIME())  
						&& warrantyAuditDataCount.getRes_Assembly_ServicePartNumber()==null ) { 
					output.collect( new Text (warrantyAuditDataCount.getRes_SerialNumber()), warrantyAuditDataCount);
					//System.out.println( "here -"+ warrantyAuditDataCount.toString() );
					reporter.incrCounter( Map.Counters.HEADER_ONLY_CALLS, 1);
				}
				
				reporter.incrCounter( Map.Counters.VALID_RECORDS_COUNT, 1);
				
				if ((++numRecords % 100) == 0) {
					reporter.setStatus("Finished processing " + numRecords + " records " + "from the input file: " + inputFile);
				}
			}
		}
	}


	public static class Reduce extends MapReduceBase implements Reducer<Text, WarrantyAuditDataCount, Text, WarrantyAuditDataCount> {
		static enum RepeatCounters { COUNT_REPEATING_SERINAL_NUMBER }
		
		public void reduce(Text key, Iterator<WarrantyAuditDataCount> values, OutputCollector<Text, WarrantyAuditDataCount> output, Reporter reporter) throws IOException {
			int sum = 0;
			 
			WarrantyAuditDataCount obj = null;
			String TimeList=null;
			String GUIDList=null;
			String ConsumerIDList =null;
			String CoverageTypeCodeList =null;
			
			while (values.hasNext()) {
				int cnt=0;
				if (obj==null) { 
					obj = values.next();
					cnt = obj.getCount();
					TimeList=obj.getStrTimeList();
					GUIDList=obj.getStrGUIDList();
					ConsumerIDList = obj.getStrConsumerAppIDList();
					CoverageTypeCodeList = obj.getStrCoverageTypeCodeList();

					//System.out.println( "obj - " + obj.toString() );
				} else {
					WarrantyAuditDataCount localobj = values.next();
					cnt = localobj.getCount();
					
					TimeList = TimeList + "," + localobj.getStrTimeList();
					GUIDList = GUIDList + "," + localobj.getStrGUIDList();
					ConsumerIDList = ConsumerIDList + "," + localobj.getStrConsumerAppIDList();
					CoverageTypeCodeList = CoverageTypeCodeList + "," + localobj.getStrCoverageTypeCodeList();
				}
				
				sum += cnt;
			}

			
			if (obj!=null) {
				obj.setCount(sum);
				
				obj.setStrTimeList(TimeList);
				obj.setStrGUIDList(GUIDList);
				obj.setStrConsumerAppIDList(ConsumerIDList);
				obj.setStrCoverageTypeCodeList(CoverageTypeCodeList);
			}
			output.collect(key, obj);
		}
	}
	
	
	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), CountResponseSerialNumberV2.class);
		conf.setJobName("wordcount");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(WarrantyAuditDataCount.class);
		 	
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		
		conf.setOutputFormat(TextOutputFormat.class);
		//conf.setOutputFormat(SequenceFileOutputFormat.class);
		
		
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		
		JobClient.runJob(conf);
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumberV2(), args);
		System.exit(res);
	}
}

	
	
