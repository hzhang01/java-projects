package project2;
import java.io.*;
import java.text.SimpleDateFormat;
/**
 * This class defined a FileOnDisk object that inherits from File class.
 * This class has a one argument constructor. 
 * FileOnDisk class overrides the toString method to provide 
 * meaningful information about file's size, date last modified and 
 * absolute path. 
 * 
 * @author Han Zhang
 * @version 10/3/2016
 */

@SuppressWarnings("serial")
public class FileOnDisk extends File {
	// Data Fields 
	// Creating variables to store the information about the file 
	private String pathName = "";
	private double fileSize = 0; 
	private String printDate = "";
	private SimpleDateFormat formatDate = 
			new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	private String suffix = "";
	private final int K = 1024;
	private final int M = K * K;
	private final int G = M * K;


	// Constructor 
	/**
	 * One argument constructor of FileOnDisk class that
	 * takes a string that contains a file's path. 
	 * 
	 * @param filePath the absolute path of a file 
	 */
	public FileOnDisk(String filePath) {

		// Using File one argument constructor to create an object 
		super(filePath);

		//Converting the file size into 5 significant figures number 
		// and correct unit 
		fileSize = this.length();
		if(fileSize < 1000){
			suffix = "bytes";
		}
		if(fileSize < 1000 * 1000){
			fileSize = fileSize / K;
			suffix = "KB";
		}
		else if(fileSize < (1000 * 1000 * 1000)){
			fileSize = fileSize / M;
			suffix = "MB";
		}
		else{
			fileSize = fileSize / G;
			suffix = "GB";
		}

		// Storing date last modified and the absolute path 
		printDate = formatDate.format(this.lastModified());
		pathName = this.getAbsolutePath();		
	}

	// Methods 	
	/**
	 *  A customized toString method for FileOnDisk objects that 
	 *  returns a string containing file's size, date last modified and 
	 *  absolute path. 
	 *  
	 *  @return string a string line containing the file's information 
	 */
	public String toString(){
		if(this.suffix.equals("bytes")){
			return String.format("%6.2f %s   %s   %s", 
					fileSize,suffix,printDate,pathName);
		}
		else{
			return String.format("%6.2f %s      %s   %s", 
					fileSize,suffix,printDate,pathName);
		}

	}
}

