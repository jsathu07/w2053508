package application.model;

// handles main queries to initiate and stop the system from the client
public class Command {
    private int status = 0;

    public Command(String command) {
        setStatus(command);
    }

    private void setStatus(String command) {
        if (command.equals("start")) {
            status = 1;
        } else {
            status = 0;
        }
    }

    public int getStatus() {
        return status;
    }
}
