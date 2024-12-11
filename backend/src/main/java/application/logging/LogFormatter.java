package application.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;

public class LogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record.getLevel() + ", " + record.getMessage() + ", " + new Date(record.getMillis()) + "\n";
    }
}
