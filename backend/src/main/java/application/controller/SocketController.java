package application.controller;

import application.concurrency.Observer;
import application.concurrency.UIType;
import application.logging.Log;
import application.controller.helpers.WebLogger;
import application.concurrency.Executor;
import application.logging.LogType;
import application.model.Command;

import application.model.Configuration;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class SocketController {
    private final Executor executor;

    public SocketController(SimpMessageSendingOperations messagingTemplate) throws Exception {
        Log.getInstance().setWebLogger(new WebLogger(messagingTemplate));

        executor = new Executor(UIType.GUI);
        executor.setMessagingTemplate(messagingTemplate);
    }

    @MessageMapping("/command")
    @SendTo("/topic/status")
    public int updateStatus(@Payload String message) {
        int command = new Command(message).getStatus();
        if (command == 1 && !executor.isRunning()) {
            executor.start();
            new Thread(new Observer(executor)).start();
            return 1;
        } else if (command == 0 && executor.isRunning()) {
            executor.stop();
        }
        return 0;
    }

    @MessageMapping("/configure")
    public void setConfiguration(@Payload Configuration configuration) {
        executor.setConfiguration(configuration);
    }

    @ExceptionHandler(MessageConversionException.class)
    public void handleHttpMessageNotReadable(MessageConversionException exception) {
        Log.getInstance().record(LogType.ERROR, "Invalid configuration", true);
    }
}
