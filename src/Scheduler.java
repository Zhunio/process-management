import com.zhunio.process.PCB;
import com.zhunio.process.queue.ReadyQueue;
import com.zhunio.process.schedule.CPUScheduler;
import com.zhunio.process.schedule.JobScheduler;
import com.zhunio.process.schedule.ScheduleAlgorithm;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

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
 * the {@code Scheduler} class performs the simulation. Please refer to the
 * {@code ScheduleAlgorithm} interface for additional information.
 *
 * @author Richard I. Zhunio
 */
public class Scheduler {

	/**
	 * Main program making use of this Scheduler class. Main program could be
	 * separated into another class, however project requirements dictate
	 * otherwise.
	 * @param args first argument should be the input file, and second argument
	 *             should be schedule algorithm specified as string (i.e P_PL).
	 * @throws Exception If an error occurs
	 */
	public static void main(String[] args) throws Exception {
		// Create a new scheduler
		Scheduler scheduler = new Scheduler();

		// Default filepath to the job pool
		// Default schedule algorithm
		String jobPool = "input.data";
		String schedAlgorithm = "FCFS";

		// Check for invalid number of cmd arguments
		if (args.length > 2) {
			System.err.println("Wrong number of cmd arguments.");
			System.exit(1);
		}

		// If input file and schedule algorithm provided
		if (args.length == 2) {
			jobPool = args[0];
			schedAlgorithm = args[1];
		}

		// Generate the schedule algorithm
		ScheduleAlgorithm scheduleAlgorithm = ScheduleAlgorithm.generate(schedAlgorithm);

		// Execute the scheduler
		scheduler.execute(jobPool, scheduleAlgorithm);
	}

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

		// Generate output file name
		File outputFile = getOutputFile(jobPool);

		// Save the log into a file
		PrintWriter writer = new PrintWriter(outputFile);
		log.forEach(writer::println);
		writer.close();
	}

	/**
	 * Generates the output file from the input file. It assures to place the
	 * output file in the same directory as the input file and also renames
	 * the generated file to outout.[ext]
	 * @param file the input file
	 * @return the generated ooutput file
	 */
	private File getOutputFile(String file) {
		// Get parent directory
		String parentDir = new File(file).getParent();

		// Test if parent dir is null
		parentDir = parentDir == null ? "" : parentDir + "/";

		// Get output name
		String output = "output.";

		// Get extension
		String[] split = file.split("\\.");
		String ext = split[split.length - 1];

		return new File(parentDir + output + ext);
	}
}
