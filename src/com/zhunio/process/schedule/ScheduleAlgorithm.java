package com.zhunio.process.schedule;

import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;
import com.zhunio.process.schedule.algorithm.FCFS;
import com.zhunio.process.schedule.algorithm.P_PL;

import java.util.List;

/**
 * Created on 10/9/17.
 * <p>
 * Performs the required steps to allocate {@code PCB}s into the CPU. The
 * required steps may vary accordingly to the specific implementation of the
 * {@code ScheduleAlgorithm}. However, each implementation should always
 * yield the same result for the same {@code ScheduleAlgorithm}. Different
 * {@code ScheduleAlgorithm}s will yield different results. A {@code List} is
 * returned with the performed steps in sequence.
 *
 * @author Richard I. Zhunio
 */
public interface ScheduleAlgorithm {

	/* First Come First Served Schedule Algorithm */
	String FCFS = "FCFS";

	/* Preemptive Lowest Priority Queue */
	String P_PL = "P_PL";

	/**
	 * Executes the required steps in this schedule algorithm. The
	 * implementation details are hidden. However, we can be certain
	 * of which processes were executed in the CPU by examining
	 * the {@code List}.
	 *
	 * @param readyQueue the {@code ReadyQueue} that contains the processes
	 *                   ready to be executed.
	 * @return a {@code List} containing the sequential steps in which {@code PCB}s
	 * visited the CPU.
	 */
	List<String> run(ReadyQueue<PCB> readyQueue);

	/**
	 * Generates a new {@code ScheduleAlgorithm} based on a string.
	 *
	 * @param scheduleAlgorithm the string represention of the
	 *                          {@code ScheduleAlgorithm}.
	 * @return a new {@code ScheduleAlgorithm}.
	 * @throws Exception if a non-supported {@code ScheduleAlgorithm} string is
	 *                   passed.
	 */
	static ScheduleAlgorithm generate(String scheduleAlgorithm) throws Exception {
		switch (scheduleAlgorithm) {
			case FCFS:
				return new FCFS();
			case P_PL:
				return new P_PL();
			default:
				throw new Exception("Not supported schedule algorithm: "
					+ scheduleAlgorithm);
		}
	}
}
