package com.jbmo60927.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * formatter used for the log
 */
public class MyFormatter extends Formatter {
    
    /**
     * this method is called for every log records
     * [Date][ClassName <MethodName>] Level: Message
     */
    public String format(final LogRecord rec) {
        final String date = dateFormatter(rec.getMillis());
        final String className = rec.getSourceClassName();
        final String methodName = rec.getSourceMethodName();
        final String level = rec.getLevel().toString();
        final String message = rec.getMessage();
        return String.format("[%s][%s <%s>] %s: %s %n", date, className, methodName, level, message);
    }

    /**
     * date formatter for the log
     * @param millisecs current time used for the file name
     * @return a string with the date correctly displayed
     */
    private String dateFormatter(final long millisecs) {
        final Date resultDate = new Date(millisecs);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(resultDate);
    }
}
