package application.logging;

import application.controller.helpers.WebLogger;

import java.util.logging.*;

public final class Log {

    private static volatile Log instance;
    private Logger logger;
    private WebLogger webLogger;

    private Log() {
        configure();
    }

    public static Log getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (Log.class) {
            if (instance == null) {
                instance = new Log();
            }
            return instance;
        }
    }

    private void configure() {
        try {
            logger = Logger.getLogger(Log.class.getName());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("app.log", false);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);

            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }
        } catch (Exception e) {
            logger = null;
        }
    }

    public void record(LogType type, String message) {
        if (logger == null) return;

        if (!isMessageValid(message)) return;

        switch (type) {
            case WARNING:
                logger.warning(message);
                break;
            case ERROR:
                logger.severe(message);
                break;
            default:
                logger.info(message);
        }
    }

    private boolean isMessageValid(String message) {
        return message != null && !message.trim().isEmpty();
    }

    public void record(String message) {
        // if type is not specified assuming as INFO
        record(LogType.INFO, message);
    }

    public void record(LogType type, String message, boolean sendToWeb) {
        record(type, message);
        if (sendToWeb && webLogger != null) {
            webLogger.log(message);
        }
    }

    public void setWebLogger(WebLogger webLogger) {
        this.webLogger = webLogger;
    }
}
