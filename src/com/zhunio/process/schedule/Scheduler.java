package com.zhunio.process.schedule;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created on 10/9/17.
 *
 * The {@code Scheduler} class executes the given {@code ScheduleAlgorithm} on
 * the file located on the{@code jobPool} path passed as a string.
 *
 * @author Richard I. Zhunio
 */
public class Scheduler {

	/**
	 * Performs the given {@code ScheduleAlgorithm} on the file located
	 * at the path represented by the {@code jobPool}.
	 * @param jobPool path to the file.
	 * @param scheduleAlgorithm Schedule algorithm to perform.
	 * @throws Exception if an error occurs.
	 */
	public void execute(String jobPool, ScheduleAlgorithm scheduleAlgorithm)
		throws Exception {
		// Create new job scheduler
		JobScheduler jobScheduler = new JobScheduler(jobPool);

		// Load the job pool into the ready queue
		ReadyQueue<PCB> readyQueue = jobScheduler.loadJobPool();

		// Create a cpu scheduler with the ready queue loaded in memory
		CPUScheduler cpuScheduler = new CPUScheduler(readyQueue, scheduleAlgorithm);

		// Begin dispatching the processes in the ready queue and obtain
		// a log
		List<String> log = cpuScheduler.dispatch();

		// Save the log into a file
		PrintWriter writer = new PrintWriter(new File(jobPool + ".processed"));
		log.forEach(writer::println);
		writer.close();
	}
}
