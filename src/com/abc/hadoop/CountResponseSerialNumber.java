package com.abc.hadoop;

import java.io.*;
import java.util.*;
 	
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;




public class CountResponseSerialNumber extends Configured implements Tool {
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> { 
		static enum Counters { INPUT_WORDS }
		 	
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		 	
		private boolean caseSensitive = true;

		 	
		private long numRecords = 0;
		private String inputFile;
		 	
		public void configure(JobConf job) {
			caseSensitive = job.getBoolean("wordcount.case.sensitive", true);
			inputFile = job.get("map.input.file");
		 	
			if (job.getBoolean("wordcount.skip.patterns", false)) {
				Path[] patternsFiles = new Path[0];
				try {
					patternsFiles = DistributedCache.getLocalCacheFiles(job);
				} catch (IOException ioe) {
					System.err.println("Caught exception while getting cached files: " + StringUtils.stringifyException(ioe));
				}
			}
		}
		

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) 
		throws IOException {
			String line = value.toString() ;
			//System.out.println("line - " + line);
			WarrantyAuditResponse warrantyAuditResponse =  WarrantyDataParser.parseLine (line);
			if (warrantyAuditResponse!=null && warrantyAuditResponse.getRes_SerialNumber()!=null ) {
				System.out.println(warrantyAuditResponse);
				output.collect( new Text(warrantyAuditResponse.getRes_SerialNumber()), one);
				reporter.incrCounter(Counters.INPUT_WORDS, 1);
				
				if ((++numRecords % 100) == 0) {
					reporter.setStatus("Finished processing " + numRecords + " records " + "from the input file: " + inputFile);
				}
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
		JobConf conf = new JobConf(getConf(), CountResponseSerialNumber.class);
		conf.setJobName("wordcount");
		 	
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		 	
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		 	
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		 	
		List<String> other_args = new ArrayList<String>();
			for (int i=0; i < args.length; ++i) {
				if ("-skip".equals(args[i])) {
					DistributedCache.addCacheFile(new Path(args[++i]).toUri(), conf);
					conf.setBoolean("wordcount.skip.patterns", true);
				} else {
					other_args.add(args[i]);
				}
			}
		 	
		FileInputFormat.setInputPaths(conf, new Path(other_args.get(0)));
		FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));
		 	
		JobClient.runJob(conf);
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new CountResponseSerialNumber(), args);
		System.exit(res);
	}
}

	
	

