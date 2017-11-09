# Running the jar file

1. Specify the jar flag (-jar) followed by the jar filename (scheduler.jar)
2. Specify the input file name "input.data", without quotes, or any input file relative
to the running java program.
3. Specify the following scheduling algorithms without quotes
    - "FCFS", (For First Come First Served)
    - "P_PL", (For Preemptive Priority Low)

`java -jar scheduler.jar input.data P_PL`