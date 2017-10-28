package com.zhunio.process.schedule;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;

import java.util.List;

/**
 * Created on 10/12/17.
 *
 * The {@code CPUScheduler} maintains a record of the processes that
 * are ready to execute and allocates the CPU to one of them by obeying
 * a predetermined {@code ScheduleAlgorithm}. The processes ready to execute
 * are kept on a queue called the {@code ReadyQueue}. The {@code ReadyQueue}
 * also maintains additional information such as preemption and quantum time
 * that may be of importance to the {@code ScheduleAlgorithm}.
 *
 * @author Richard I. Zhunio
 */
public class CPUScheduler {
	/* List of processes ready to be executed by the CPU */
	private ReadyQueue<PCB> readyQueue;

	/* The schedule algorithm that the CPU must use */
	private ScheduleAlgorithm scheduleAlgorithm;

	/**
	 * Create a new {@code CPUScheduler} with the given {@code ReadyQueue},
	 * and the {@code ScheduleAlgorithm}.
	 *
	 * @param readyQueue        the list of processes ready to be executed.
	 * @param scheduleAlgorithm the {@code ScheduleAlgorithm} that will decide
	 *                          which processes visit the CPU first.
	 */
	public CPUScheduler(ReadyQueue<PCB> readyQueue,
				 ScheduleAlgorithm scheduleAlgorithm) {
		// Get a hold of the ready queue
		this.readyQueue = readyQueue;

		// Obtain the schedule algorithm
		this.scheduleAlgorithm = scheduleAlgorithm;
	}

	/**
	 * Dispatch all the processes form the ready queue to the CPU.
	 *
	 * @return a {@code List} containing the order of execution of
	 * processes.
	 */
	public List<String> dispatch() {

		// Run the following schedule algorithm and return its result
		// which consists of a list containing the order in which processes
		// visited the CPU
		return scheduleAlgorithm.run(readyQueue);
	}
}
