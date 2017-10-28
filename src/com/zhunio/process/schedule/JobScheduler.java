package com.zhunio.process.schedule;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;

/**
 * Created on 10/6/17.
 *
 * Selects processes from the job pool, located on a disk file, and loads them into
 * a queue in memory.
 *
 * @author Richard I. Zhunio
 */
public class JobScheduler {

	/* Represents the job pool residing on disk and containing all newly created
	 * processes. Rather than being a File object, it is the path with which a File
	 * can be created.
	 * */
	private String jobPool;

	/**
	 * Creates a new Job Scheduler capable of selecting processes from the job
	 * pool and loading them into memory.
	 *
	 * @param filepath path to disk file containing the job pool.
	 */
	public JobScheduler(String filepath) {
		jobPool = filepath;
	}

	/**
	 * Load the processes from the job pool or disk file into memory. This means
	 * loading the processes into a ready queue.
	 *
	 * @return A ready queue.
	 */
	public ReadyQueue<PCB> loadJobPool() throws Exception {

		// Creates a new parser to read the contents of the job pool
		JobPoolParser parser = new JobPoolParser(jobPool);

		// Parse the job pool and return a a ready queue
		return parser.parse();

	}
}
