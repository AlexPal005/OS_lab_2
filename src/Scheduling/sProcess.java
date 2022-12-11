package Scheduling;

public class sProcess implements Cloneable {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int arrivalTime;
  public int numberProcess;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int arrivalTime, int numberProcess) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.arrivalTime = arrivalTime;
    this.numberProcess = numberProcess;
  }

  public int getArrivalTime() {
    return arrivalTime;
  }
  public int getRemaindTime(){
    return cputime - cpudone;
  }  //remaining time until the end of the process

  public int getCputime() {
    return cputime;
  }
}
