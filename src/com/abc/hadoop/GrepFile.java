package com.abc.hadoop;

import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/* Extracts matching regexs from input files and counts them. */

public class GrepFile extends Configured implements Tool {
	  private GrepFile() {}                               // singleton

	  public int run(String[] args) throws Exception {
	    if (args.length < 3) {
	      System.out.println("Grep <inDir> <outDir> <regex> [<group>]");
	      ToolRunner.printGenericCommandUsage(System.out);
	      return -1;
	    }

	    Path tempDir =
	      new Path("grep-temp-"+
	          Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));

	    JobConf grepJob = new JobConf(getConf(), GrepFile.class);
	    
	    try {
	      
	      grepJob.setJobName("grep-search");

	      FileInputFormat.setInputPaths(grepJob, args[0]);

	      grepJob.setMapperClass(RegexMapper.class);
	      grepJob.set("mapred.mapper.regex", args[2]);
	      if (args.length == 4)
	        grepJob.set("mapred.mapper.regex.group", args[3]);

	      grepJob.setCombinerClass(LongSumReducer.class);
	      grepJob.setReducerClass(LongSumReducer.class);

	      FileOutputFormat.setOutputPath(grepJob, tempDir);
	      grepJob.setOutputFormat(SequenceFileOutputFormat.class);
	      grepJob.setOutputKeyClass(Text.class);
	      grepJob.setOutputValueClass(LongWritable.class);

	      JobClient.runJob(grepJob);

	      JobConf sortJob = new JobConf(GrepFile.class);
	      sortJob.setJobName("grep-sort");

	      FileInputFormat.setInputPaths(sortJob, tempDir);
	      sortJob.setInputFormat(SequenceFileInputFormat.class);

	      sortJob.setMapperClass(InverseMapper.class);

	      sortJob.setNumReduceTasks(1);                 // write a single file
	      FileOutputFormat.setOutputPath(sortJob, new Path(args[1]));
	      sortJob.setOutputKeyComparatorClass           // sort by decreasing freq
	      (LongWritable.DecreasingComparator.class);

	      JobClient.runJob(sortJob);
	    }
	    finally {
	      FileSystem.get(grepJob).delete(tempDir, true);
	    }
	    return 0;
	  }

	  public static void main(String[] args) throws Exception {
	    int res = ToolRunner.run(new Configuration(), new GrepFile(), args);
	    System.exit(res);
	  }


}
