package it.unimore.dade.crosscourse.piprocess;

public interface SemaphoreStatusListener {
   // public static final Thread thread = new Thread;
    public void onStatusChanged(String updatedStatus) throws InterruptedException;

}
