package com.zhunio.process.schedule.algorithm;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;
import com.zhunio.process.schedule.ScheduleAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 10/19/17.
 * <p>
 * Performs the Preemptive Low Priority {@code ScheduleAlgorithm}. We use
 * the term lowest priority to mean that the process with the lowest priority
 * has a higher priority compared to a process with a higher priority.
 *
 * @author Richard I. Zhunio
 */
public class P_PL implements ScheduleAlgorithm {

	/**
	 * Creates a new remaining queue used to hold temporary processes that
	 * have been removed from the ready queue but have had a lowest priority
	 * than the current process executing
	 */
	private PriorityQueue<PCB> newRemainingQueue() {

		// Comparator for sorting low priorities first
		Comparator<PCB> byLowestPriority =
			(p1, p2) -> new Integer(p1.getPriority()).compareTo(p2.getPriority());

		return new PriorityQueue<>(byLowestPriority);
	}

	@Override
	public List<String> run(ReadyQueue<PCB> readyQueue) {

		// Create remaining queue
		PriorityQueue<PCB> remainingQueue = newRemainingQueue();

		// Create the gant chart
		LinkedList<GantEntry> gantChart = new LinkedList<>();

		// Initialize the timeline of the gant chart
		int timeline = 0;

		// Retrieve quantum time
		int quantum = readyQueue.getQuantumTime();

		// While the ready queue is not empty
		while (!readyQueue.isEmpty() || !remainingQueue.isEmpty()) {

			// Select process with the lowest priority under the given timeline
			PCB newProcess = selectLowest(readyQueue, remainingQueue, timeline);

			// If new process has arrived
			if (newProcess != null) {

				// Calculate the remaining burst time for this new process
				int burstTimeRemaining = newProcess.getBurstTime() - newProcess.getCpuTime();

				// Set the start timeline of this process
				int start = timeline;
				int end;

				// If the burst time is less than or equal to quantum time
				if (burstTimeRemaining <= quantum) {
					// Calculate end time for this process
					end = timeline + burstTimeRemaining;

					// Add cpu time utilization to this process
					newProcess.addCpuTime(quantum);
				}

				// If the burst time is greater than the quantum time
				else {
					// Calculate end time for this process
					end = timeline + quantum;

					// Add cpu time utilization to this process
					newProcess.addCpuTime(quantum);

					// Add this process to the remaining queue
					remainingQueue.add(newProcess);
				}

				// Add this process to the gant entry
				gantChart.add(new GantEntry(newProcess, start, end));
				timeline = end;

			}
			// If no process has arrived, increment by quantum
			else {

				// Increment the timeline of the gant chart
				timeline += quantum;
			}
		}

		return gantChart.stream().map(
			entry -> {
				String start = String.valueOf(entry.start);
				String end = String.valueOf(entry.end);
				String processID = String.valueOf(entry.process.getProcessID());

				return start + " " + end + " P" + processID;
			}
		).collect(Collectors.toList());
	}

	/**
	 * Selects the {@code PCB} with the lowest priority from the
	 * {@code ReadyQueue} under the given limit, namely the {@code timeline}. The
	 * {@code PCB} with the {@code arrivalTime} less than or equal to the timeline
	 * is returned.
	 *
	 * @param readyQueue the ready queue containing the processes.
	 * @param timeline   the limit to which check the {@code arriveTime} of each
	 *                   {@code PCB}
	 * @return The {@code PCB} with the {@code arrivalTime} less than or equal to
	 * the timeline is returned.
	 */
	private PCB selectLowest(ReadyQueue<PCB> readyQueue,
							 PriorityQueue<PCB> remainingQueue, int timeline) {

		PriorityQueue<PCB> tempQueue;
		// Comparator for sorting low priorities first
		Comparator<PCB> byLowestPriority =
			(p1, p2) -> new Integer(p1.getPriority()).compareTo(p2.getPriority());

		// Create remaining queue with low priority processes ordered first
		tempQueue = new PriorityQueue<>(byLowestPriority);

		// Retrieve processes that arrived before timeline from the ready queue
		while (!readyQueue.isEmpty() && readyQueue.peek().getArriveTime() <= timeline) {
			PCB process = readyQueue.poll();
			tempQueue.add(process);
		}

		// Retrieve processes that arrived before timeline form remaining queue
		while (!remainingQueue.isEmpty() &&
			remainingQueue.peek().getArriveTime() <= timeline) {

			PCB process = remainingQueue.poll();
			tempQueue.add(process);
		}

		// Retrieve the processes that has the lowest priority
		PCB process = tempQueue.poll();

		// Reinsert the processes took from ready queue to remaining queue
		while (!tempQueue.isEmpty()) {
			remainingQueue.add(tempQueue.poll());
		}

		return process;
	}

	/**
	 * Checks if the process has finished execution
	 *
	 * @param process the procces
	 * @return true if it has finish execution
	 */
	private boolean isFinished(PCB process) {
		return process.getBurstTime() == process.getCpuTime();
	}

	/**
	 * Represents a {@code GantEntry} in a {@code List}. A {@code GantEntry}
	 * contains a {@code process}, {@code start}, and {@code end}. The
	 * {@code process} represents a {@code PCB}. The {@code start} and {@code end}
	 * represents the start time and end time of the {@code GantEntry},
	 * respectively.
	 */
	class GantEntry {
		/* A process */
		PCB process;

		/* The start of this entry */

		int start;

		/* The end of this entry */
		int end;

		/**
		 * Creates a new {@code GantEntry} with the following attributes:
		 * {@code process}, {@code start}, and {@code end}.
		 *
		 * @param process the {@code PCB}.
		 * @param start   the start time.
		 * @param end     the end time.
		 */
		GantEntry(PCB process, int start, int end) {
			this.process = process;
			this.start = start;
			this.end = end;
		}

		/**
		 * The string representation of a {@code GantEntry}. The representation is
		 * as follows:
		 * {start, end, P#}
		 *
		 * @return a string representation of the {@code GantEntry}.
		 */
		public String toString() {
			return "{" + start + " " + end + " P" + process.getProcessID() + "}";
		}
	}
}