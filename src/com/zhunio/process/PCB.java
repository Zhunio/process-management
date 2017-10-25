package com.zhunio.process;

/**
 * Created on 10/6/17.
 * <p>
 * Represents the Process Control Block namely {@code PCB} of a process. Each
 * {@code PCB} has a {@code processID} which is final, {@code arriveTime},
 * {@code burstTime}, {@code priority}, and {@code cpuTime}. A {@code PCB}
 * implements the comparable interface by enforcing its natural order to
 * be dictated by {@code arriveTime}. A first {@code PCB} is less than or equal to
 * a second {@code PCB} if it arrived before or at the same time as the second
 * {@code PCB}. If the second {@code PCB} arrives before the first {@code PCB},
 * then the second {@code PCB} is greater than the first. Additionally, two
 * {@code PCB}s are considered equal if they have the same following properties:
 * {@code processID} which is final, {@code arriveTime}, {@code burstTime},
 * {@code priority}, and {@code cpuTime}.
 *
 * @author Richard I. Zhunio
 * @version 1.0.0
 */
public class PCB implements Comparable<PCB> {
	/* Process ID of a process */
	private final int processID;

	/* Arrive time of a process */
	private int arriveTime;

	/* Burst time of a process */
	private int burstTime;

	/* priority of a process */
	private int priority;

	/* CPU time of a process */
	private int cpuTime;

	/**
	 * Creates a new {@code PCB} with the following properties.
	 *
	 * @param processID  the processID otherwise known as the process number of the process.
	 * @param arriveTime the arrive time of the process.
	 * @param burstTime  the burst time of the process.
	 * @param priority   the priority of the process.
	 */
	public PCB(int processID, int arriveTime, int burstTime, int priority) {
		this.processID = processID;
		this.arriveTime = arriveTime;
		this.burstTime = burstTime;
		this.priority = priority;
		this.cpuTime = 0;
	}

	/**
	 * Adds the given {@code cpuTime} to the current {@code cpuTime} of
	 * this process.
	 * @param cpuTime the {@code cpuTime} to add.
	 */
	public void addCpuTime(int cpuTime) {
		this.cpuTime += cpuTime;
	}

	/**
	 * Retrieves the arrive time of this process.
	 * @return the arrive time of this process.
	 */
	public int getArriveTime() {
		return arriveTime;
	}

	/**
	 * Retrieves the burst time of this process.
	 * @return the burst time of this process.
	 */
	public int getBurstTime() {
		return burstTime;
	}

	/**
	 * Retrieves the priority of this process.
	 * @return the priority of this process.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Retrieves the priority of this process.
	 * @return the priority of this process.
	 */
	public int getCpuTime() {
		return cpuTime;
	}

	/**
	 * Retrieves the process id of this process.
	 * @return the process id of this process.
	 */
	public int getProcessID() {

		return processID;
	}

	/**
	 * Sets the arrive time of this process.
	 * @param arriveTime the arrive time of this process.
	 */
	@SuppressWarnings("unused")
	public void setArriveTime(int arriveTime) {
		this.arriveTime = arriveTime;
	}

	/**
	 * Sets the burst time of this process.
	 * @param burstTime the burst time of this process.
	 */
	@SuppressWarnings("unused")
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	/**
	 * Sets the priority of this process.
	 * @param priority the priority of this process.
	 */
	@SuppressWarnings("unused")
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Sets the CPU time of this process.
	 * @param cpuTime the CPU time of this process.
	 */
	public void setCpuTime(int cpuTime) {
		this.cpuTime = cpuTime;
	}

	/**
	 * String representation of a {@code PCB}.
	 * It uses the following format:
	 * <br>
	 * Process ArriveTime BurstTime Priority
	 * 0        0         2         0
	 * <br>
	 * String representation:
	 * {P0, 0, 2, 0}
	 *
	 * @return The string representation of a process control block.
	 */
	public String toString() {
		return "{P" + processID +
			", " + arriveTime +
			", " + burstTime +
			", " + priority + "}";
	}

	/**
	 * Implements a natural order based on the arrival time of each process.
	 * A first process is less than a second process if it arrived before
	 * the second process. A first process is equal to a second if it arrived at
	 * exactly the same time as the second process. A first process is greater than
	 * a second process if it arrived after the second process.
	 * <br>
	 * For example, in the following case:
	 * Process  AT  BT  P
	 * P1       0  	2   0
	 * P2       1   5   0
	 * <br>
	 * P1 is less than P2 because it arrived before P2.
	 *
	 * @param other the other process to compare this process to.
	 * @return -1 if this {@code PCB} is less than the other {@code PCB},
	 * 			0 if this {@code PCB} is equal to the other {@code PCB},
	 * 			1 if this {@code PCB} is greater than the other {@code PCB}.
	 */
	@Override
	public int compareTo(PCB other) {
		return new Integer(arriveTime).compareTo(other.arriveTime);
	}

	/**
	 * Compares whether two {@code PCB}s are equal or not. They are defined equal
	 * if all of their properties are equal, namely the following:
	 * {@code processID} which is final, {@code arriveTime}, {@code burstTime},
	 * {@code priority}, and {@code cpuTime}.
	 * @param o the other object.
	 * @return true if this process equals the other process.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PCB pcb = (PCB) o;

		// Two PCBs should have the same properties
		return arriveTime == pcb.arriveTime &&
			burstTime == pcb.burstTime &&
			priority == pcb.priority &&
			cpuTime == pcb.cpuTime &&
			processID == pcb.processID;

	}

	/**
	 * Calculates the {@code hashCode} of this process. Don't rely on the
	 * implementation of the following {@code hashCode}. If {@code hashCode} is
	 * necessary, extend this class and implement your own {@code hashCode}.
	 * @return the hashcode of this process.
	 */
	@Override
	public int hashCode() {
		int result = processID;
		result = 31 * result + arriveTime;
		result = 31 * result + burstTime;
		result = 31 * result + priority;
		result = 31 * result + cpuTime;
		return result;
	}
}
