package Scheduling;// Run() is called from Scheduling.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int runtime, Vector processVector, Results result) {
        int comptime = 0;
        sProcess process = null;
        sProcess prevProcess = null;
        int size = processVector.size();
        int completed = 0;
        int counter = 0;
        String resultsFile = "src\\Files\\Summary-Processes";

        result.schedulingType = "Batch (Preemptive)";
        result.schedulingName = "Shortest remaining time first";
        try {

            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            // sorted by arrivalTime
            processVector.sort(Comparator.comparing(sProcess::getArrivalTime));
            // queue of processes, sorted by ramaind time
            Queue<sProcess> queue = new PriorityQueue<>(Comparator.comparing(sProcess::getRemaindTime));

            while (comptime < runtime) {
                // write queue
                if (counter < size) {
                    while (comptime == ((sProcess) processVector.get(counter)).getArrivalTime()) {
                        queue.add((sProcess) processVector.get(counter));
                        counter++;
                        if (counter == size) {
                            break;
                        }
                    }
                }
                if (process == null) {
                    if(!queue.isEmpty()){
                        process = queue.poll();
                        out.println("Process: " + process.numberProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " + process.arrivalTime + ")");
                    }
                    else{
                        comptime++;
                        continue;
                    }
                }

                if (process.cpudone == process.cputime) {
                    completed++;
                    out.println("Process: " + process.numberProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " + process.arrivalTime + ")");
                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }
                    else{
                        process = null;
                        continue;
                    }
                }

                if (process.ioblocking == process.ionext) {
                    out.println("Process: " + process.numberProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " + process.arrivalTime + ")");
                    process.numblocked++;
                    process.ionext = 0;

                    if(!queue.isEmpty()) {
                        Queue<sProcess> queueLess = new PriorityQueue<>(Comparator.comparing(sProcess::getRemaindTime));
                        int k = 0;
                        prevProcess = process;
                        while ( k < size) {
                            if(queue.isEmpty()){
                                break;
                            }
                            process = queue.poll();
                            if (process.getRemaindTime() > prevProcess.getRemaindTime()) {
                                queueLess.add(process);
                            } else {
                                break;
                            }
                            k++;
                        }
                        queue.add(prevProcess);
                        for (int i = 0; i < queueLess.size(); i++) {
                            queue.add(queueLess.poll());
                        }
                    }

                    out.println("Process: " + process.numberProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + " " + process.arrivalTime + ")");

                }
                process.cpudone++;
                if (process.ioblocking > 0) {
                    process.ionext++;
                }
                comptime++;
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        result.compuTime = comptime;
        return result;
    }
}
