package com.abc.hadoop;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.abc.hadoop.CountResponseSerialNumberV3.Map;



public class CountResponseSerialNumber10Sec extends Configured implements Tool {
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> { 
		static enum Counters { VALID_RECORDS_COUNT, HEADER_ONLY_CALLS, REPEATING_CALLS_IN_10_SEC }
		 	
		private long numRecords = 0;

		 	
		public void configure(JobConf job) {
			
			String propFile = job.get("main.config.file");
			System.out.println("lei - propFile " + propFile);
			
			WarrantyAuditCountParameter.getWarrantyAuditCountParameter().init(propFile);
			
			String val = WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getProperty("warranty.count.endtime", "");
			System.out.println("lei val - " + val);			
			System.out.println("startTime - " + WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getStartDateTime()  );
			System.out.println("endTime - " + WarrantyAuditCountParameter.getWarrantyAuditCountParameter().getEndDateTime()  );
		}

		private void countRepeatingCallWithinSeconds(OutputCollector<Text, IntWritable> output, List<WarrantyAuditCompareI> list, int numSec) throws IOException {

			WarrantyDataCountedI criteriaRepeatingCallsWithSameCoverage[] = new WarrantyDataCountedI[2];
			criteriaRepeatingCallsWithSameCoverage[0] = new WarrantyDataCompareTime(numSec);
			criteriaRepeatingCallsWithSameCoverage[1] = new WarrantyDataCompareCoverageTypeSame();

			
			List<WarrantyAuditCompareI> returnList10SecWithSameCoverage = ObjectFactory.getWarrantyAuditCompareListMeetingCondition(list, criteriaRepeatingCallsWithSameCoverage );
			if (returnList10SecWithSameCoverage.size()>0) {
				String strCountTitle = "{Total_Header_Only_Repeating_Calls_Same_CoverageTypeCode_Within_";
				if (numSec<10)
					strCountTitle = strCountTitle + "0" + numSec + "_Seconds}";
				else 
					strCountTitle = strCountTitle + numSec + "_Seconds}"; 
				output.collect( new Text (strCountTitle), new IntWritable( returnList10SecWithSameCoverage.size() ) );
			}
		}

		private void countRepeatingCallWithinSeconds(OutputCollector<Text, IntWritable> output, List<WarrantyAuditCompareI> list, String consumerProviderID, int numSec) throws IOException {

			WarrantyDataCountedI criteriaRepeatingCallsWithSameCoverage[] = new WarrantyDataCountedI[3];
			criteriaRepeatingCallsWithSameCoverage[0] = new WarrantyDataCompareTime(numSec);
			criteriaRepeatingCallsWithSameCoverage[1] = new WarrantyDataCompareCoverageTypeSame();
			criteriaRepeatingCallsWithSameCoverage[2] = new WarrantyDataCompareConsumerIDSame(consumerProviderID);
			
			List<WarrantyAuditCompareI> returnList10SecWithSameCoverage = ObjectFactory.getWarrantyAuditCompareListMeetingCondition(list, criteriaRepeatingCallsWithSameCoverage );
			if (returnList10SecWithSameCoverage.size()>0) {
				String strCountTitle = "{Total_Header_Only_Repeating_Calls_Same_CoverageTypeCode" +  "_For_ConsumerID_" + consumerProviderID + "_Within_";
				if (numSec<10)
					strCountTitle = strCountTitle + "0" + numSec + "_Seconds}" ;
				else 
					strCountTitle = strCountTitle + numSec + "_Seconds}" ;
				 
				output.collect( new Text (strCountTitle), new IntWritable( returnList10SecWithSameCoverage.size() ) );
			}
		}

		
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) 
			throws IOException {
			String line = value.toString() ;
			System.out.println("line - " + line);
			WarrantyAuditDataCount warrantyAuditDataCount = ObjectFactory.getWarrantyAuditDataCount(line);

			// duplicated calls 
			if (warrantyAuditDataCount!=null && warrantyAuditDataCount.getCount()>1 ) {

				// count header only repeating calls 
				List<WarrantyAuditCompareI> list = ObjectFactory.getWarrantyAuditCompareList(line);
				WarrantyDataCountedI criteriaRepeating10Sec[] = new WarrantyDataCountedI[1];
				criteriaRepeating10Sec[0] = new WarrantyDataCompareTime(10);

				List<WarrantyAuditCompareI> returnList10Sec = ObjectFactory.getWarrantyAuditCompareListMeetingCondition(list, criteriaRepeating10Sec );
				 
				if (returnList10Sec.size()>0) {
					output.collect( new Text ("{Total_Header_Only_In_10_Sec}"), new IntWritable( returnList10Sec.size() ) );
					//output.collect( new Text (warrantyAuditDataCount.getRes_SerialNumber() ), new IntWritable( returnList10Sec.size() ) );
				}

				// 60 to 20 seconds
				for (int i=60; i>10; i-=10) {
					countRepeatingCallWithinSeconds( output, list, i);
					countRepeatingCallWithinSeconds( output, list, "149", i);
					countRepeatingCallWithinSeconds( output, list, "157", i);
				}

				// 10 to 1 seconds
				for (int i=10; i>0; i--) {
					countRepeatingCallWithinSeconds( output, list, i);
					countRepeatingCallWithinSeconds( output, list, "149", i);
					countRepeatingCallWithinSeconds( output, list, "157", i);
				}
				
				/*
				WarrantyDataCountedI criteriaRepeating10SecWithSameCoverage[] = new WarrantyDataCountedI[2];
				criteriaRepeating10SecWithSameCoverage[0] = new WarrantyDataCompareTime(10);
				criteriaRepeating10SecWithSameCoverage[1] = new WarrantyDataCompareCoverageTypeSame();

				
				List<WarrantyAuditCompareI> returnList10SecWithSameCoverage = ObjectFactory.getWarrantyAuditCompareListMeetingCondition(list, criteriaRepeating10SecWithSameCoverage );
				if (returnList10SecWithSameCoverage.size()>0) {
					output.collect( new Text ("{Total_Repeating_Header_Only_Calls_Within_10_Sec_With_Same_CoverageType}"), new IntWritable( returnList10SecWithSameCoverage.size() ) );
					output.collect( new Text (warrantyAuditDataCount.getRes_SerialNumber() ), new IntWritable( returnList10SecWithSameCoverage.size() ) );
				}
				*/
				
				output.collect( new Text ("{Total_Header_Only}"), new IntWritable(warrantyAuditDataCount.getCount()) );
				output.collect( new Text ("{Total_Header_Unique_SerialNumber}"), new IntWritable( 1 ) );

				
				reporter.incrCounter( Map.Counters.HEADER_ONLY_CALLS, warrantyAuditDataCount.getCount());
				reporter.incrCounter( Map.Counters.REPEATING_CALLS_IN_10_SEC, returnList10Sec.size() );
			}
		}
	}


	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			 
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}
	
	
	public int run(String[] args) throws Exception {
		System.out.println("start-1");
		JobConf conf = new JobConf(getConf(), CountResponseSerialNumber10Sec.class);
		conf.setJobName("wordcount");


		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		 	
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		
		
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumber10Sec(), args);
		System.exit(res);
	}
}

	

