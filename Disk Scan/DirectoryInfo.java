package project2;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class is the actual program that runs with command
 * line arguments only. The program takes two arguments:
 * 1. Path of a directory/file
 * 2. One of the keywords: oldest, newest and largest
 * If any arguments are invalid, error is printed and the
 * program is terminated. 
 * DirectoryInfo class has a getSize method that returns 
 * the size of a given directory/file. 
 * 
 * @author Han Zhang
 * @version 10/3/2016
 *
 */
public class DirectoryInfo {

	// Data Fields 
	// Creating an array to store all 
	// the file(s) in a given directory/file 
	private static ArrayList<FileOnDisk> fileList = 
			new ArrayList<FileOnDisk>(); 
	private static long totalFiles = 0;
	private static final int K = 1024;
	private static final int M = K * K;
	private static final int G = M * K;

	public static void main(String[] args) {
		
		// If no arguments entered no argument meassage is prompted 
		if(args.length == 0){
			System.out.println("No valid arguments entered.");
			System.exit(0);
		}

		// Instantiating the first argument into a File object 
		// and creating variable for storing information about the
		// file/directory 
		File file = new FileOnDisk (args[0]);
		double totalSize = 0;
		

		// If the create File doesn't exist on the machine
		// indicate the absence and terminate the program 
		if(!(file.exists())){
			System.out.println("The file/directory at '" 
					+ args[0] + "' doesn't exist.");
			System.exit(0);
		}
		
		// Else if the argument list is only 1 argument, prompt user to enter second argument 
		if(args.length < 2){
			System.out.println("Please enter a second argument e.g. newest, oldest, largest.");
			System.exit(0);
		}

		// Testing if the second argument is a valid argument 
		boolean newest = args[1].equalsIgnoreCase("newest");
		boolean oldest = args[1].equalsIgnoreCase("oldest");
		boolean largest = args[1].equalsIgnoreCase("largest");

		// If the seconds argument is invalid, 
		// print out error and end the program 
		if(!newest && !oldest && !largest){
			System.out.println("The entered argument '" 
					+ args[1] + "' is invalid.\nPlease enter "
					+ "one of the valid arguments:\n--newest, "
					+ "oldest, largest");
			System.exit(0);
		}

		// Use recursion to go through all the directories and files 
		// in the given directory or file 
		try {
			totalSize = DirectoryInfo.getSize(file);
		} 

		// Print out error in case the file cannot be open 
		catch (IOException e) {
			System.err.println("Error: A file cannot be open!");
		}

		// Converting the total size into appropriate units 
		String suffix = "";
		if(totalSize < 1000){
			suffix = "bytes";
		}
		if(totalSize < 1000 * 1000){
			totalSize = totalSize / K;
			suffix = "KB";
		}
		else if(totalSize < 1000 * 1000 * 1000){
			totalSize = totalSize / M;
			suffix = "MB";
		}
		else{
			totalSize = totalSize / G;
			suffix = "GB";
		}

		// Depending on the second argument, 
		// the list of files is sorted accordingly 
		if(newest){
			Comparator<FileOnDisk> dateComparatorNewest = 
					new CompareFilesByDateNewest();
			fileList.sort(dateComparatorNewest);
		}
		else if(oldest){
			Comparator<FileOnDisk> dateComparator = 
					new CompareFilesByDate();
			fileList.sort(dateComparator);
		}
		else if(largest){
			Comparator<FileOnDisk> sizeComparator = 
					new CompareFilesBySize();
			fileList.sort(sizeComparator);
		}

		// Printing out the information to the 
		// user according to the second argument
		System.out.printf("Total space used:%6.2f%s,"
				+ "\tTotal number of files: %d\n",
				totalSize,suffix,totalFiles);
		try{
			if(oldest){
				System.out.println("Least recent modified 20 files:");
				for (int start = 0; start < 20; start++) {
					System.out.println(fileList.get(start).toString());
				}
			}
			else if(newest){
				System.out.println("Most recent modified 20 files:");
				for (int start = 0; start < 20; start++) {
					System.out.println(fileList.get(start).toString());
				}
			}
			else if(largest){
				System.out.println("Largest 20 files:");
				for (int start = 0; start < 20; start++) {
					System.out.println(fileList.get(start).toString());
				}
			}
		}

		// If the file list's length is smaller than 20
		// this exception is caught to continue the program
		catch (IndexOutOfBoundsException e){
		}

	}

	/**
	 * This method returns the total size of a given directory/file
	 * by accumulating the sizes of each sub-directories and files in 
	 * the current directory. 
	 * 
	 * @param potentialDirName a given File object with a valid path 
	 * @return totalSize the accumulated size of the directory/file 
	 * @throws IOException throws exception when a certain file cannot be 
	 * one in a sub-directory. 
	 */
	public static long getSize(File potentialDirName) throws IOException{

		// Casting the File object into a 
		//FileOnDisk object for easier printing 
		FileOnDisk potentialDir = 
				new FileOnDisk(potentialDirName.getAbsolutePath());
		long totalSize = 0;

		// If the path points at a symbolic link 
		if (!(potentialDir.getAbsolutePath().equals
				(potentialDir.getCanonicalPath()))){

			// And the symbolic link leads to a directory
			// Add the size of the directory 
			if(potentialDir.isDirectory()){
				totalSize += potentialDir.length();
				return totalSize;
			}

			// If it leads to a file, just return the 0 
			if(potentialDir.isFile()){
				return totalSize;
			}
		}

		// If the path points to a directory 
		if(potentialDir.isDirectory()){

			// Add the size of the directory and 
			// all the sub-directory and files 
			totalSize += potentialDir.length();
			String[] subFileList = potentialDir.list();

			// Try if there are file(s) in this directory 
			try{

				// For every existing sub-directory/file add their sizes 
				for (int index = 0; index < subFileList.length; index++) {
					File temp = new FileOnDisk(potentialDir.getPath() + 
							"/" + subFileList[index]);

					// Recursive Case: accumulate all the sizes under this
					// directory 
					try{
						totalSize += DirectoryInfo.getSize(temp);
					}

					// Print error when a file/directory can't be opened 
					catch (IOException e){
						System.err.println("Error: Cannot open certain files.");
					}
				}
			}

			// Print error when the directory is empty 
			catch (NullPointerException e){
				System.err.println("Error: No files found in the directory.");
			}

		}

		// Base Case: if the path points to a file 
		if(potentialDir.isFile()){

			// Record the file and increase file count by 1 
			// and total size by file's size 
			fileList.add(potentialDir);
			totalFiles += 1;
			totalSize += (int)potentialDir.length();
		}
		return totalSize;
	}
}

/**
 * This class implements Comparator<E> to override 
 * compare method for sorting FileOnDisk files based
 * on ascending (oldest to newest) last modified date. 
 *
 */
class CompareFilesByDate implements Comparator<FileOnDisk>{
	/**
	 * This class overrides compare method to change 
	 * the natural ordering of FileOnDisk object.
	 * When sorted, there will be in an ascending order:
	 * from oldest to newest last modified date. 
	 * 
	 * @return 1 if first object is newer than the other object
	 * 		   0 if two object have the same date
	 * 		  -1 if first object is older than the other object 
	 */
	@Override
	public int compare(FileOnDisk o1, FileOnDisk o2) {
		long comp = o1.lastModified() - o2.lastModified();
		if(comp == 0){
			return o1.compareTo(o2);
		}
		if(comp > 0)
			return 1;
		else return -1;
	}
}

/**
 * This class implements Comparator<E> to override 
 * compare method for sorting FileOnDisk files based
 * on descending (newest to oldest) last modified date. 
 *
 */
class CompareFilesByDateNewest implements Comparator<FileOnDisk>{
	/**
	 * This class overrides compare method to change 
	 * the natural ordering of FileOnDisk object.
	 * When sorted, there will be in an descending order:
	 * from newest to oldest last modified date. 
	 * 
	 * @return -1 if first object is newer than the other object
	 * 		    0 if two object have the same date
	 * 		    1 if first object is older than the other object 
	 */
	@Override
	public int compare(FileOnDisk o1, FileOnDisk o2) {
		long comp = o1.lastModified() - o2.lastModified();
		if(comp == 0){
			return o1.compareTo(o2);
		}
		if(comp > 0)
			return -1;
		else return 1;
	}
}

/**
 * This class implements Comparator<E> to override 
 * compare method for sorting FileOnDisk files based
 * on descending (largest to smallest) file size. 
 *
 */
class CompareFilesBySize implements Comparator<FileOnDisk>{
	/**
	 * This class overrides compare method to change 
	 * the natural ordering of FileOnDisk object.
	 * When sorted, there will be an descending order:
	 * from largest to smallest file size. 
	 * 
	 * @return 1 if first object is smaller than the other object
	 * 		   0 if two object have the same size 
	 * 		  -1 if first object is larger than the other object 
	 */
	@Override
	public int compare(FileOnDisk o1,FileOnDisk o2){
		int comp = (int) (o2.length() - o1.length());
		if (comp == 0){
			return o2.compareTo(o1);
		}
		return comp;
	}
}
