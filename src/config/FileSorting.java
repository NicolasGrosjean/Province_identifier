package config;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import text.Text;

/**
 * Management of the main arguments
 * @author Mouchi
 *
 */
public class FileSorting {
	/**
	 * Sort the files of the mainDirectories/subDirectory/ from the older to the younger
	 * @param mainDirectories
	 * @param subDirectory
	 * @param text
	 * @return
	 */
	public static LinkedList<String> sortFiles(LinkedList<String> mainDirectories, String subDirectory, Text text) {
		// Read the directories and sort the files
		LinkedList<String> sortedFileNames = new LinkedList<String>();
		Comparator<File> comparator = new FileComparator();
		PriorityQueue<File> files = new PriorityQueue<File>(20, comparator);
		while (!mainDirectories.isEmpty()) {
			readAndSortDirectoryFiles(mainDirectories.removeFirst(), text,
					files, subDirectory);
		}
		// Transform Files priority queues list into String list
		while (!files.isEmpty()) {
			sortedFileNames.addLast(files.remove().toString());
		}
		return sortedFileNames;
	}

	private static void readAndSortDirectoryFiles(String directoryName, text.Text text,
			PriorityQueue<File> files, String subDirectory) {
		if (directoryName != null) {
			File mainDirectory = new File(directoryName);
			if (!mainDirectory.isDirectory()) {
				throw new IllegalArgumentException(text.invalidDirectoryName(directoryName));
			}
		} else {
			throw new IllegalArgumentException(text.missingDirectoryName());
		}
		File subDirectoryFile = new File(directoryName + subDirectory);
		if (!subDirectoryFile.isDirectory()) {
			throw new IllegalArgumentException(text.invalidDirectoryName(directoryName + subDirectory));
		}
		for (File f : subDirectoryFile.listFiles()) {
			if (f.isFile()) {
				files.offer(f);
			}
		}
	}

	/**
	 * Order File from the older to the younger
	 *
	 */
	private static class FileComparator implements Comparator<File>{
		@Override
		public int compare(File f1, File f2) {
			return (int)(f2.lastModified() - f1.lastModified());
		}
	}
}
