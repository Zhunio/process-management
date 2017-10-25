package com.zhunio.process.queue;

import java.util.PriorityQueue;

/**
 * Created on 10/7/17.
 *
 * Represents a {@code ReadyQueue} that contains a list of processes ready to be
 * executed by the CPU.
 *
 * @author Richard I. Zhunio
 */
public class ReadyQueue<PCB> extends PriorityQueue<PCB> {
	/* Describes if this queue is supposed to be preemptive or not */
	private boolean preemption;

	/* If preemption is set, quantum time is used. */
	private int quantumTime;

	/**
	 * Construct a ready queue with no preemption and quantum
	 * time.
	 */
	public ReadyQueue(boolean preemption, int quantumTime) {

		// Initialize a priority queue
		super();

		this.preemption = preemption;
		this.quantumTime = quantumTime;

	}


	/**
	 * Checks if this {@code ReadyQueue} is suppose to be run by a preemptive
	 * {@code ScheduleAlgorithm}.
	 *
	 * @return if this algorithm is suppose to be run by a preemptive
	 * {@code ScheduleAlgorithm}.
	 */
	@SuppressWarnings("unused")
	public boolean isPreemptive() {
		return preemption;
	}

	/**
	 * Sets this {@code ReadyQueue} to be preemtive.
	 * @param preemption true for preemptive, false otherwise.
	 */
	@SuppressWarnings("unused")
	public void setPreemption(boolean preemption) {
		this.preemption = preemption;
	}

	/**
	 * Retrieves the quantum time of this {@code ReadyQueue} is preemptive.
	 * @return the quantum time.
	 */
	@SuppressWarnings("unused")
	public int getQuantumTime() {
		return quantumTime;
	}

	/**
	 * Sets the quantum time for this {@code ReadyQueue} if it is preemptive.
	 * @param quantumTime the quantum time for this {@code ReadyQueue} if it is
	 *                    preemptive.
	 */
	@SuppressWarnings("unused")
	public void setQuantumTime(int quantumTime) {
		this.quantumTime = quantumTime;
	}
}
