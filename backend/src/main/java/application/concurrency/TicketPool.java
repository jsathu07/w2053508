package application.concurrency;

import application.logging.Log;
import application.logging.LogType;

import application.model.Ticket;

import java.util.Vector;

public class TicketPool {

    private static TicketPool instance = new TicketPool();
    volatile static int availableTickets = 3; // total tickets available to purchase
    private static int maxTickets = 10; // maximum tickets the vector can hold at a given time
    private final Vector<Ticket> tickets;

    private TicketPool() {
        tickets = new Vector<Ticket>(maxTickets);
    }

    public static TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = new TicketPool();
    }

    public static void setAvailableTickets(int availableTickets) {
        TicketPool.availableTickets = availableTickets;
    }

    public static void setMaxTickets(int maxTickets) {
        TicketPool.maxTickets = maxTickets;
    }

    public boolean isTicketsAvailable() {
        return availableTickets > 0;
    }

    public synchronized boolean addTickets(int vendorId) {
        if (!isTicketsAvailable()) return true;

        Log.getInstance().record(LogType.INFO, "Vendor " + vendorId + " is going add a ticket", true);

        while (tickets.size() == maxTickets) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        Ticket ticket = new Ticket(vendorId);
        tickets.add(ticket);
        notifyAll();

        Log.getInstance().record(LogType.INFO, "Vendor " + vendorId + " has added " + ticket.getTicketId(), true);

        return false;
    }

    public synchronized Ticket removeTicket(int customerId) {
        if (!isTicketsAvailable()) return null;

        Log.getInstance().record(LogType.INFO, "Customer " + customerId + " is going to buy a ticket", true);

        while (tickets.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        Ticket purchase = tickets.remove(0);
        availableTickets--;
        notifyAll();

        String message = "Customer " + customerId + " has successfully bought the ticket " + purchase.getTicketId() + " from vendor " + purchase.getVendorId();
        Log.getInstance().record(LogType.INFO, message, true);
        return purchase;
    }
}
