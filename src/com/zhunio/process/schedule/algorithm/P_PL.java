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

		// While the ready queue is not empty
		while (!readyQueue.isEmpty()) {

			// Select process with the lowest priority under the given timeline
			PCB newProcess = selectLowest(readyQueue, remainingQueue, timeline);

			// Set the start and end time of this process
			int start = timeline;
			int end = -1;

			// If the gant chart is empty
			if (gantChart.isEmpty())
				gantChart.add(new GantEntry(newProcess, start, end));

				// If the gant chart is not empty
			else {

				// Get last gant entry
				GantEntry gantEntry = gantChart.getLast();

				// Get a hold of the process in the last gant entry
				PCB gantProcess = gantEntry.process;

				// If the new process has lowest priority
				if (newProcess.getPriority() < gantProcess.getPriority()) {

					// 1) Set the end time for the last gant entry
					gantEntry.end = timeline;

					// 2) Set the cpu utilization for the last gant entry
					gantProcess.addCpuTime(gantEntry.end - gantEntry.start);

					// 3) If current process is not finished, add it to the
					// remaining queue
					if (!isFinished(gantProcess))
						remainingQueue.add(gantEntry.process);

					// 4) Add new process to the gant chart
					gantChart.add(new GantEntry(newProcess, start, end));
				} else
					remainingQueue.add(newProcess);
			}

			// Increment the timeline of the gant chart
			timeline++;
		}

		// Finish executing process in the last gant chart entry
		GantEntry gantEntry = gantChart.getLast();

		// Temporarily set the end in the last gant chart entry
		gantEntry.end = timeline;

		// Add cpu time to check if process in the last gant entry has finished.
		gantEntry.process.addCpuTime(gantEntry.end - gantEntry.start);

		// If process in the last gant entry has not finished
		if (!isFinished(gantEntry.process)) {

			// Get the process
			PCB process = gantEntry.process;

			// Get remaining cpu utilization time
			int remainingTime = process.getBurstTime() - process.getCpuTime();

			// Calculate the end time of this process
			gantEntry.end = timeline + remainingTime;

			// Add cpu utilization
			process.addCpuTime(remainingTime);

			// Update timeline
			timeline = gantEntry.end;
		}

		// Finish executing remaining processes in the remaining queue
		while (!remainingQueue.isEmpty()) {

			// Select next process with the lowest priority to execute
			PCB nextProcess = remainingQueue.poll();

			// Get remaining cpu time utilization
			int remainingTime = nextProcess.getBurstTime() - nextProcess.getCpuTime();

			// Set the start and end time for process
			int start = timeline;
			int end = start + remainingTime;

			// Add cpu utilization
			nextProcess.addCpuTime(remainingTime);

			// Add process to the gant entry
			gantChart.add(new GantEntry(nextProcess, start, end));

			// update timeline
			timeline = end;
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