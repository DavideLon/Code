package it.unimore.dade.crosscourse.piprocess;

public interface SemaphoreStatusListener {
    public void onStatusChanged(String updatedStatus) throws InterruptedException;
}
