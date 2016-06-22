/*
 * This program expects two arguments
 * 1) Path to the Application
 * 2) Message name to search
 * Then this program gives you the list and no of SOR and DS test cases.
 *  First Compile: javac RTIRunner1.java 
 *  execute: java RTIRunner1 path msgname
 * 
 * 
 */
import java.io.File;
import java.lang.Object;
import java.util.*;
class RTIHelper
{
	final String ghpProjectDesc = ".ghp"; 
	final String testCaseDir = "Logical";
	final String TS_EXT = ".gts";
	final String TC_EXT = ".tsq";
	final String SOR_TAG = "SOR";
	final String DS_TAG = "DS";
	ArrayList<String> sorList = new ArrayList<String>();
	ArrayList<String> dsList = new ArrayList<String>();
	File path;
	String keyMsg;
	
	public RTIHelper(File path, String keyMsg)
	{
		this.path=path;
		this.keyMsg = keyMsg;
	}
	
	public boolean isValidRTIProject()
	{
		File flist[] = path.listFiles();
		for(File file: flist )
		{
			if(file.getName().endsWith(ghpProjectDesc))
			{
				return true;
			}
		}
		return false;		
	}
	
	public boolean isTestCaseDirExists()
	{
		File flist[] = path.listFiles();
		for (File file: flist )
		{
			//System.out.println(file.getName() + "," + testCaseDir);
			if (file.isDirectory() && file.getName().equals(testCaseDir))
			{
				return true;
			}
		}
		return false;		
	}
    
	public void scanTestCases(String msg)
	{
		scanTestDir(path, msg);
	}
	
	
	public void scanTestDir(File path, String msg)   // recursively searches the directories 
	{
		File flist[] = path.listFiles();
		for (File file:flist)
		{
			if (file.isFile())
			{
				String name = file.getName();
				if (name.endsWith(TS_EXT) || name.endsWith(TC_EXT))
				{
					if (name.contains(msg))
					{
						if (name.contains(SOR_TAG))
						{
							sorList.add(file.getAbsolutePath());
						}
						else if (name.contains(DS_TAG))
						{
							dsList.add(file.getAbsolutePath());
						}
					}
				}
			}
			else if (file.isDirectory())   
			{
				//String filePath = file.getAbsolutePath();
				//System.out.println(file.getName());
				scanTestDir(file, msg);
			}
		}
	}
	
	public void displayResults()
	{
		Iterator sorIterator = sorList.iterator();
		System.out.println();
		System.out.println("======================================================");
		System.out.println("SOR test cases count for " + keyMsg + " [" + sorList.size() + "]");
		System.out.println("======================================================");
		while(sorIterator.hasNext())
		{
			System.out.println(sorIterator.next());
		}
				
		
		Iterator dsIterator = dsList.iterator();
		System.out.println();
		System.out.println("======================================================");
		System.out.println("DS test cases count for " + keyMsg + " [" + dsList.size() + "]");
		System.out.println("======================================================");
		while(dsIterator.hasNext())
		{
			System.out.println(dsIterator.next());
		}
	}
	
}


public class RTIRunner1 
{
	public static void main(String args[])
	{
		
		File path = new File(args[0]);
		RTIHelper rti = new RTIHelper(path, args[1]);
		if(!path.exists())
		{
			System.out.println("The given path doesnt exists");
			System.exit(-1);
		}
		
		if (rti.isValidRTIProject())
		{
			if (rti.isTestCaseDirExists())
			{
				System.out.println();
				System.out.print("Scanning for the message "  + rti.keyMsg + " in " + path.getAbsolutePath());
				System.out.println(" ......");
				rti.scanTestCases(args[1]);
				rti.displayResults();
			}
			else
			{
				System.out.println("ERROR: Could not find the test case directory " + rti.testCaseDir);
			}
		}
		else
		{
			System.out.println("ERROR: Not a valid RIT project..!");
		}
			
	}
}