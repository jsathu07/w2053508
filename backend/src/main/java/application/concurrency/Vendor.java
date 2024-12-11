package application.concurrency;

import application.logging.Log;
import application.logging.LogType;

public class Vendor implements Runnable {
    private final int vendorId;
    private final int releaseInterval; // in milliseconds

    public Vendor(int vendorId, int releaseInterval) {
        this.vendorId = vendorId;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        Log.getInstance().record(LogType.INFO, "Vendor " + vendorId + " thread is started", true);

        while (!Thread.currentThread().isInterrupted()) {
            if (TicketPool.getInstance().addTickets(vendorId)) {
                // addTickets will return true if there are no more tickets to add
                break;
            }
            try {
                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                break;
            }
        }

        Log.getInstance().record(LogType.INFO, "Vendor " + vendorId + " thread is ended", true);
    }
}
