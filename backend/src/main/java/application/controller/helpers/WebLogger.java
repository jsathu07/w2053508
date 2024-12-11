package application.controller.helpers;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class WebLogger {
    private final SimpMessageSendingOperations messagingTemplate;

    public WebLogger(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void log(String message) {
        messagingTemplate.convertAndSend("/topic/logs", message);
    }
}
