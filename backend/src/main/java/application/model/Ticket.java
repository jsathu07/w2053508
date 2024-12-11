package application.model;

import java.util.UUID;

public class Ticket {
    private final int vendorId;
    private final UUID ticketId;

    public Ticket(int vendorId) {
        this.vendorId = vendorId;
        ticketId = UUID.randomUUID();
    }

    public int getVendorId() {
        return vendorId;
    }

    public String getTicketId() {
        return ticketId.toString();
    }
}
