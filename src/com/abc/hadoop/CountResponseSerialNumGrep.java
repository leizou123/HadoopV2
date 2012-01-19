package com.abc.hadoop;



import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;


import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

// hadoop fs -rmr  /output2
// hadoop jar /Users/lei/Documents/workspace/HadoopSample/target/release/HadoopSample.jar com.abc.hadoop.CountResponseSerialNumGrep -Dmain.config.file="/warranty/conf/WarrantyDataCount.properties"  /warranty/step1 /output2 > junk.out 2>&1  
//
public class CountResponseSerialNumGrep extends Configured implements Tool {
	
	public static class CountResponseSerialNumGrepMap<K> extends Mapper<K, Text, Text, LongWritable> {
		public void map(K key, Text value, Mapper<K, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException
		{
			String line = value.toString();
			//System.out.println("line - " + line );
			WarrantyAuditDataCount warrantyAuditDataCount = ObjectFactory.getWarrantyAuditDataCount(line);
			//context.write(new Text(line), new LongWritable(1L));
			if (warrantyAuditDataCount!=null && warrantyAuditDataCount.getCount()>1 ) {
				context.write( new Text (warrantyAuditDataCount.getRes_SerialNumber()), new LongWritable(warrantyAuditDataCount.getCount()) );
			}
		}
			
		public void setup(Mapper<K, Text, Text, LongWritable>.Context context)
		{
			Configuration conf = context.getConfiguration();
		}

	}
	
	private CountResponseSerialNumGrep () {}
	
	 public int run(String[] args) throws Exception {
		 if (args.length < 2) {
			 System.out.println("CountResponseSerialNumGrep <inDir> <outDir>");
			 ToolRunner.printGenericCommandUsage(System.out);
			 return 2;
		 }

		 Path tempDir =
		      new Path("grep-temp-"+
		          Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));

		    Configuration conf = getConf();

		    Job grepJob = new Job(conf);
		    
		    try {
		      
		      grepJob.setJobName("grep-search");

		      FileInputFormat.setInputPaths(grepJob, args[0]);

		      grepJob.setMapperClass(CountResponseSerialNumGrepMap.class);

		      
		      grepJob.setCombinerClass(LongSumReducer.class);
		      grepJob.setReducerClass(LongSumReducer.class);

		      FileOutputFormat.setOutputPath(grepJob, tempDir);
		      grepJob.setOutputFormatClass(SequenceFileOutputFormat.class);
		      grepJob.setOutputKeyClass(Text.class);
		      grepJob.setOutputValueClass(LongWritable.class);

		      grepJob.waitForCompletion(true);

		      
		      Job sortJob = new Job(conf);
		      sortJob.setJobName("grep-sort");

		      FileInputFormat.setInputPaths(sortJob, tempDir);
		      sortJob.setInputFormatClass(SequenceFileInputFormat.class);

		      sortJob.setMapperClass(InverseMapper.class);

		      sortJob.setNumReduceTasks(1);                 // write a single file
		      FileOutputFormat.setOutputPath(sortJob, new Path(args[1]));
		      sortJob.setSortComparatorClass(LongWritable.DecreasingComparator.class);	// sort by decreasing freq

		      sortJob.waitForCompletion(true);
		    }
		    finally {
		      FileSystem.get(conf).delete(tempDir, true);
		    }
		    return 0;
		  }

	  public static void main(String[] args) throws Exception {
		    int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumGrep(), args);
		    System.exit(res);
	  }

}
