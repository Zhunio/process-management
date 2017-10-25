package com.zhunio.process.schedule.algorithm;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;
import com.zhunio.process.schedule.ScheduleAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 10/15/17.
 *
 * Implements the non-preemptive version of the Firs Come First Server {@code FCFS}
 * {@code ScheduleAlgorithm}.
 *
 * @author Richard I. Zhunio
 */
public class FCFS implements ScheduleAlgorithm {

	/**
	 * Runs the {@code ScheduleAlgorithm} using the following
	 * ready queue. Its implementation is obscured to the user.
	 * However, the {@code List} will show the order in which
	 * processes visited the {@code CPU}.
	 *
	 * @param readyQueue the ready queue containing the processes to be
	 *                   executed by the {@code CPU}.
	 * @return a {@code List} containing the order in which
	 * processes visited the {@code CPU}.
	 */
	@Override
	public List<String> run(ReadyQueue<PCB> readyQueue) {

		// Create new CPU
		CPU cpu = new CPU();

		// Set the timeline of cpu utilization
		// The timeline represents the Gant chart
		int timeline = 0;

		// Logger for tracking which processes go into the CPU first
		Logger logger = new Logger();

		while (!readyQueue.isEmpty()) {

			// Select the next process to execute in the CPU
			PCB process = select(readyQueue);

			// Execute the process in the CPU and obtain the amount of time
			// that process spent in the CPU
			int processedTime = cpu.compute(process);

			// Calculate start time of the processed
			int start = timeline;
			// Calculate end time of the processed
			int end = start + processedTime;

			// Add process to the log
			logger.add(start + " " + end + " P" + process.getProcessID());

			// Update the timeline
			timeline = end;
		}

		return logger;
	}

	/**
	 * Selects a process from the {@code ReadyQueue} obeying the policies
	 * for the First Come First Served {@code FCFS} algorithm.
	 * @param readyQueue the {@code ReadyQueue} containing the processes
	 *                   ready to be executed.
	 * @return a single {@code PCB} (process) that must be executed next
	 * according to the {@code FCFS} algorithm policy.
	 */
	private PCB select(ReadyQueue<? extends PCB> readyQueue) {

		// Select the next process whom arrived the earliest
		// The ready queue sorts PCB according to their arrive time
		PCB process = readyQueue.poll();

		// Set the cpu time utilization to the burst time
		process.setCpuTime( process.getBurstTime() );

		return process;
	}

	/**
	 * The {@code Logger} represents the amount of time each process spends
	 * on the gant chart.
	 */
	class Logger extends ArrayList<String> {
		/**
		 * Create a new {@code Logger}.
		 */
		Logger() {
			super();
		}
	}
	/**
	 * Created on 10/15/17.
	 *
	 * A very simple simulation of a {@code CPU}.
	 *
	 * @author Richard I. Zhunio
	 */
	class CPU {
		/**
		 * Compute a single {@code PCB} (process).
		 * @param process the {@code PCB} to compute.
		 * @return the {@code CPU} time utilization by the given process.
		 */
		int compute(PCB process) {
			return process.getCpuTime();
		}
	}
}
