package application.concurrency;

import application.logging.LogType;
import application.model.Configuration;
import application.logging.Log;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayList;

public class Executor {
    Configuration configuration;
    ArrayList<Thread> vendor = new ArrayList<>();
    ArrayList<Thread> customer = new ArrayList<>();

    private SimpMessageSendingOperations messagingTemplate = null;
    private boolean isRunning;
    private final UIType uiType;

    public Executor(UIType uiType) {
        this.uiType = uiType;
        try {
            configuration = new Configuration();
        } catch (Exception e) {
            Log.getInstance().record(LogType.ERROR, "Error setting configuration from file", true);
        }
    }

    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        vendor = new ArrayList<>(); // all vendor threads
        customer = new ArrayList<>(); // all customer threads
        isRunning = true;

        TicketPool.resetInstance();
        TicketPool.setAvailableTickets(configuration.getTotalTickets());
        TicketPool.setMaxTickets(configuration.getMaxTicketCapacity());

        Log.getInstance().record(LogType.INFO, "Executor service has been started", true);

        // better simulation with different intervals for each person
        int[] cusRates = {3, 1, 5, 2, 3};
        int[] venRates = {1, 5, 3, 10, 6};
        for (int i = 1; i <= 5; i++) {
            vendor.add(new Thread(new Vendor(i, venRates[i - 1] * 1000)));
        }
        for (int i = 1; i <= 5; i++) {
            customer.add(new Thread(new Customer(i, cusRates[i - 1] * 1000)));
        }

//        for (int i = 1; i <= 5; i++) {
//            vendor.add(new Thread(new Vendor(i, configuration.getTicketReleaseRate() * 1000)));
//        }
//
//        for (int i = 1; i <= 5; i++) {
//            customer.add(new Thread(new Customer(i, configuration.getCustomerRetrievalRate() * 1000)));
//        }

        for (Thread thread : vendor) {
            thread.start();
        }

        for (Thread thread : customer) {
            thread.start();
        }
    }

    public void stop() {
        if (uiType == UIType.GUI) {
            messagingTemplate.convertAndSend("/topic/status", 0);
        }
        isRunning = false;

        for (Thread thread : vendor) {
            thread.interrupt();
        }
        for (Thread thread : customer) {
            thread.interrupt();
        }

        Log.getInstance().record(LogType.INFO, "Executor service has been stopped", true);
    }

    public boolean isTicketsAvailable() {
        return TicketPool.availableTickets > 0;
    }

    public void updateCurrentTicketAvailability() {
        String availability = TicketPool.availableTickets + " / " + configuration.getTotalTickets();
        if (uiType == UIType.GUI) {
            messagingTemplate.convertAndSend("/topic/ticketsAvailable", availability);
        } else {
            Log.getInstance().record(LogType.INFO, "Ticket availability: " + availability);
        }
    }

    public void setConfiguration(Configuration configuration) {
        if (!isRunning) {
            this.configuration = configuration;
            Log.getInstance().record(LogType.INFO, "Successfully set configuration", true);
        }
    }
}
