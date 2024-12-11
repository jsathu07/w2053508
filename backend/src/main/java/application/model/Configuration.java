package application.model;

import org.json.JSONObject;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class Configuration {

    private UUID id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public Configuration() throws Exception {
        loadFromFile();
    }

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) throws Exception {
        this.setTotalTickets(totalTickets);
        this.setTicketReleaseRate(ticketReleaseRate);
        this.setCustomerRetrievalRate(customerRetrievalRate);
        this.setMaxTicketCapacity(maxTicketCapacity);
        id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) throws Exception {
        if (totalTickets <= 0) {
            throw new Exception("Total tickets cannot be negative");
        }
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

//    public void saveToFile() {
//        JSONObject obj = new JSONObject();
//
//        obj.put("id", id);
//        obj.put("totalTickets", totalTickets);
//        obj.put("ticketReleaseRate", ticketReleaseRate);
//        obj.put("customerRetrievalRate", customerRetrievalRate);
//        obj.put("maxTicketCapacity", maxTicketCapacity);
//
//        try {
//            File path = new File(String.format("config-[%s].json", id.toString()));
//            FileWriter writer = new FileWriter(path);
//
//            writer.write(obj.toString());
//            writer.flush();
//            writer.close();
//        } catch (Exception e) {
//            Log.getInstance().record(LogType.ERROR, "Error saving configuration to file", true);
//        }
//    }

    private void loadFromFile() throws Exception {
        File file = new File("default_configuration.json");

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            String content = scanner.useDelimiter("\\A").next();

            JSONObject obj = new JSONObject(content);

            this.setId(UUID.randomUUID());
            this.setTotalTickets(obj.getInt("totalTickets"));
            this.setTicketReleaseRate(obj.getInt("ticketReleaseRate"));
            this.setCustomerRetrievalRate(obj.getInt("customerRetrievalRate"));
            this.setMaxTicketCapacity(obj.getInt("maxTicketCapacity"));

            scanner.close();
        } else {
            throw new Exception("Default configuration file doesn't exist");
        }
    }

}
