package application.concurrency;

import application.model.Ticket;

import application.logging.Log;
import application.logging.LogType;

public class Customer implements Runnable {
    private final int customerId;
    private final int retrievalInterval; // in milliseconds

    public Customer(int customerId, int retrievalInterval) {
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        Log.getInstance().record(LogType.INFO, "Customer " + customerId + " thread is started", true);

        while (!Thread.currentThread().isInterrupted()) {
            Ticket ticket = TicketPool.getInstance().removeTicket(customerId);

            if (ticket == null) break; // no more tickets to buy

            try {
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                break;
            }
        }

        Log.getInstance().record(LogType.INFO, "Customer " + customerId + " thread is ended", true);
    }
}
