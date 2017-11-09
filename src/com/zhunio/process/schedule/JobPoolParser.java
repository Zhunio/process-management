package com.zhunio.process.schedule;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created on 10/6/17.
 *
 * Loads and parses the following from a disk file:
 * 1) Total number of processes
 * 2) Options regarding preemption and quantum time
 * 3) Processes
 * <br>
 * Once the previous information is loaded, we load the processes
 * into a queue.
 *
 * @author Richard I. Zhunio
 */
class JobPoolParser {

	/* Represents the scanner that will read the contents of the disk file */
	private Scanner reader;

	/**
	 * Creates a new reader that will read the contents of the disk file
	 *
	 * @param file the file to read.
	 * @throws FileNotFoundException if file is not found.
	 */
	JobPoolParser(String file) throws FileNotFoundException {
		reader = new Scanner(new File(file));
	}

	/**
	 * Parses a job pool file. The job pool file must be in the following
	 * format:
	 *
	 * 1		- (number of total number of processes)
	 * 1 0		- (1 if preemptive, 0 otherwise) (quantum time)
	 * 1 1 1	- (arrive time) (burst time) (priority)
	 *
	 * @return A {@code ReadyQueue}.
	 * @throws Exception if something wrong happens.
	 */
	ReadyQueue<PCB> parse() throws Exception {

		// Represents the ready queue. List of processes ready to be allocated to
		// the CPU.
		ReadyQueue<PCB> readyQueue;

		// Read number of processes in the disk file
		// We expect a single int value
		int processNo = readProcessNo();

		// Read the following two options
		// 1) preemption
		// 2) quantum time
		int[] options = readOptions();
		boolean preemption = (options[0] == 1);
		int quantumTime = options[1];

		// Throw error if quantum time is 0
		if (quantumTime == 0)
			throw new Exception("Invalid quantum time: " + quantumTime);

		// Create new ready queue with the specific schedule algorithm
		readyQueue = new ReadyQueue<>(preemption, quantumTime);

		// Read processes into the ready queue
		readProcesses(readyQueue);

		// Error occurs if the number of processes does not equal
		// the actual number of processes read from the disk file
		if ( processNo != readyQueue.size() )
			throw new Exception("Number of processes number: " + processNo + ", "
				+ "does not equal actual number of processes in the ready queue: "
				+ readyQueue.size());

		return readyQueue;
	}

	/**
	 * Reads the number of processes in the disk file. The first line is
	 * read, and we expect one int value.
	 *
	 * @return the number of processes in the disk file.
	 * @throws Exception if there is an invalid number of processes.
	 */
	private int readProcessNo() throws Exception {

		// if invalid number of processes
		int processNo = -1;

		// Read the first line from the disk file
		if (reader.hasNextLine()) {

			// Read the very first line on the disk file
			processNo = Integer.parseInt(reader.nextLine());

			// Error occurs if an invalid number is entered
			if (processNo < 0)
				throw new Exception("No valid number of processes: "
					+ processNo);
		}

		return processNo;
	}

	/**
	 * Reads the options regarding the following about the processes:
	 * 1) preemption.
	 * 2) quantum time.
	 * The second line on the disk file represents this information.
	 *
	 * @return an array of options.
	 * @throws Exception if no valid number of options are specified.
	 */
	private int[] readOptions() throws Exception {

		// Options array size
		// We expect two option elements
		final int SIZE = 2;
		int[] options = new int[SIZE];

		// ERROR could be triggered HERE if there is not a next line to
		// parse
		int currentSize = parseLine(options);

		// Different ERROR could be triggered here if unexpected number of
		// options are returned
		if (currentSize != SIZE)
			throw new Exception("No valid number of options on disk file: "
				+ Arrays.toString(options));

		return options;
	}

	/**
	 * Read the total number of processes in the entire disk file.
	 * We expect three attributes in the following order:
	 * 1) arrival time
	 * 2) burst time
	 * 3) quantum time
	 *
	 * @param readyQueue the queue to load the processes into.
	 * @throws Exception if there are missing processes' attributes.
	 */
	private void readProcesses(ReadyQueue<PCB> readyQueue) throws Exception {

		// Process ID
		int procesdID = 1;

		// Content array size.
		// We expect three attributes
		final int SIZE = 3;
		int[] content = new int[SIZE];

		// Read the rest of the processes if there is not a next line to parse
		while (reader.hasNextLine()) {
			// Error could be triggered HERE
			int currentSize = parseLine(content);

			// Different ERROR could be triggered here if unexpected number of
			// processes attributes are returned
			if (currentSize != SIZE)
				throw new Exception("Missing process attributes on disk file: "
					+ Arrays.toString(content));

			// Format of line containing processes attributes
			int arrivalTime = content[0];
			int burstTime = content[1];
			int priority = content[2];

			// Create new Process Control Block and add it to the queue
			PCB process = new PCB(procesdID, arrivalTime, burstTime, priority);

			// Add new process to the ready queue
			readyQueue.add(process);

			// Increment process id
			procesdID++;
		}
	}

	/**
	 * Parse the contents of a line and add them to an array.
	 * Utility method with its main function to simplify
	 * functionality. Current size of the array could be different
	 * than actual size of the array.
	 *
	 * @param content the array to fill
	 * @return the current size of the array.
	 */
	private int parseLine(int[] content) throws Exception {

		// Size of the array to fill
		final int SIZE = content.length;

		// ERROR if there is not ScheduleAlgorithm next line to parse
		if (!reader.hasNextLine())
			throw new Exception("Expecting to parse next line. However, "
				+ "not next line available.");

		// Next line to parse
		String line = reader.nextLine();
		Scanner parser = new Scanner(line);

		// parse attributes on the line while not exceeding
		// the array limitations
		int currentSize = 0;
		while (parser.hasNext() && currentSize < SIZE)
			content[currentSize++] = parser.nextInt();

		// close the scanner
		parser.close();

		return currentSize;
	}
}