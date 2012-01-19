package com.abc.hadoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFile {

	static private boolean isFile (File aFile) throws FileNotFoundException {
		  
		  if (aFile == null) {
			  throw new IllegalArgumentException("File can not not be null.");
		  }
		  if (!aFile.exists()) {
			  throw new FileNotFoundException("File does not exist: " + aFile);
		  }
		  return aFile.isFile();
	}
	
	static private void validateDirectory (File aDirectory) throws FileNotFoundException {
		  
		  if (aDirectory == null) {
			  throw new IllegalArgumentException("Directory should not be null.");
		  }
		  if (!aDirectory.exists()) {
			  throw new FileNotFoundException("Directory does not exist: " + aDirectory);
		  }
		  if (!aDirectory.isDirectory()) {
			  throw new IllegalArgumentException("Is not a directory: " + aDirectory);
		  }
		  if (!aDirectory.canRead()) {
			  throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
		  }
	}

	static private List<File> getFileListingNoSort( File aStartingDir) throws FileNotFoundException 
	{
		List<File> result = new ArrayList<File>();
	    File[] filesAndDirs = aStartingDir.listFiles();
	    List<File> filesDirs = Arrays.asList(filesAndDirs);
	    for(File file : filesDirs) {
	      result.add(file); //always add, even if directory
	      if ( ! file.isFile() ) {
	        //must be a directory
	        //recursive call!
	        List<File> deeperList = getFileListingNoSort(file);
	        result.addAll(deeperList);
	      }
	    }
	    return result;
	}

	static public List<File> getFileListing(File aStartingDir) {
		List<File> result = null;
		try {
			if ( isFile (aStartingDir)==true ) {
				result = new ArrayList<File>();
				result.add(aStartingDir);
			} else {
				validateDirectory(aStartingDir);
				result = getFileListingNoSort(aStartingDir);
				Collections.sort(result);
			}
		} catch (FileNotFoundException ex) {

		}
	    return result;
	}

	static public File[] getFileListing(String dirName) {
		List<File> result = getFileListing(new File(dirName));
		int size = ( result==null ) ? 0 : result.size();
		File[] files = new File[size];
		if (result!=null) {
			result.toArray(files);
		}
		return files;
		
	}	

	public static void main(String[] args) throws Exception {
		File[] fileArray = getFileListing("/Users/lei/AppleDoc/Project_Warranty/Data/2011_10");
		for (File f: fileArray) {
			String n = f.getName();
			String n2 = f.getName().replace('.', '_').replace(':', '_');
			System.out.println( "gzcat " + "/Users/lei/AppleDoc/Project_Warranty/Data/2011_10/"+ n  
					+ " > " + "/Users/lei/AppleDoc/Project_Warranty/Data/2011_10_unzip/" + n2);
		}
	}
}
