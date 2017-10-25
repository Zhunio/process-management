package com.zhunio.process.schedule;

/**
 * Created on 10/17/17.
 *
 * Program that performs a minimal schedule algorithm calculation. Given an
 * input file as the first command line argument in the form of:
 *
 * 2		(# of processes)
 * 0 4		(0 if preemptive, 1 otherwise) (quantum time)
 * 1 2 3	(arrive time) (burst time) (priority)
 * 2 3 4	(arrive time) (burst time) (priority)
 * 	...
 * a b c	(arrive time) (burst time) (priority)
 *
 * and a schedule algorithm listed under the {@code ScheduleAlgorithm} interface,
 * the {@code Main} class performs the simulation. Please refer to the
 * {@code ScheduleAlgorithm} interface for additional information.
 *
 * @author Richard I. Zhunio
 */
public class Main {
	public static void main(String[] args) throws Exception {
		// Create a new scheduler
		Scheduler scheduler = new Scheduler();

		// Obtain the filepath to the jobpool
		String jobPool = args[0];

		// Generate the schedule algorithm
		ScheduleAlgorithm scheduleAlgorithm = ScheduleAlgorithm.generate(args[1]);

		// Execute the scheduler
		scheduler.execute(jobPool, scheduleAlgorithm);
	}
}
