package com.abc.hadoop;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;


public class HDFSHelloWorld {
	public static final String theFilename = "/user/lei/hello.txt";
	public static final String message = "Hello, world!\n";

	// hadoop jar /Users/lei/Documents/workspace/HadoopV2/target/release/HadoopV2.jar com.abc.hadoop.HDFSHelloWorld 
	//
	public static void main (String [] args) throws IOException {

	     Configuration conf = new Configuration();
	     FileSystem fs = FileSystem.get(conf);

	     Path filenamePath = new Path(theFilename);

	     try {
	       if (fs.exists(filenamePath)) {
	         // remove the file first
	         fs.delete(filenamePath);
	       }

	       FSDataOutputStream out = fs.create(filenamePath);
	       out.writeUTF(message);
	       out.close();

	       FSDataInputStream in = fs.open(filenamePath);
	       String messageIn = in.readUTF();
	       System.out.print(messageIn);
	       in.close();
	     } catch (IOException ioe) {
	       System.err.println("IOException during operation: " + ioe.toString());
	       System.exit(1);
	     }
	   }
}
