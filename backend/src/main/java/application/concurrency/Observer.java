package application.concurrency;

// in-case if all tickets have been sold need to stop the service
public class Observer implements Runnable {
    private final Executor executor;

    public Observer(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void run() {
        while (executor.isTicketsAvailable()) {
            try {
                executor.updateCurrentTicketAvailability();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
        executor.stop();
    }
}
