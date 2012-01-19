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

import com.abc.hadoop.CountResponseSerialNumberV2.Map;
import com.abc.hadoop.CountResponseSerialNumberV2.Reduce;


public class CountResponseSerialNumberV3 extends Configured implements Tool {
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> { 
		static enum Counters { VALID_RECORDS_COUNT, HEADER_ONLY_CALLS }
		 	
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
		

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) 
			throws IOException {
			String line = value.toString() ;
			//System.out.println("line - " + line);
			WarrantyAuditDataCount warrantyAuditDataCount = ObjectFactory.getWarrantyAuditDataCount(line);
			
			if (warrantyAuditDataCount!=null && warrantyAuditDataCount.getCount()>1 ) {

				//output.collect( new Text (warrantyAuditDataCount.getRes_SerialNumber()), new IntWritable(warrantyAuditDataCount.getCount()) );
				output.collect( new Text ("Total_Header_Only"), new IntWritable(warrantyAuditDataCount.getCount()) );
				reporter.incrCounter( Map.Counters.HEADER_ONLY_CALLS, warrantyAuditDataCount.getCount());
				
			}
		}
	}


	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		static enum RepeatCounters { COUNT_REPEATING_SERINAL_NUMBER }
		
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			 
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}
	
	
	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), CountResponseSerialNumberV3.class);
		conf.setJobName("wordcount");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		 	
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
		int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumberV3(), args);
		System.exit(res);
	}
}

	

