package de.d3rhase.constants;

public abstract class LogTypes {

    public static final String OK = "OK";
    public static final String INFO = "INFO";
    public static final String DEBUG = "DEBUG";
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";

    public static String getLogTypeString(String logType) {
        switch (logType) {
            case LogTypes.OK:
                return "OK";
            case LogTypes.INFO:
                return "INFO";
            case LogTypes.DEBUG:
                return "DEBUG";
            case LogTypes.WARNING:
                return "WARNING";
            case LogTypes.ERROR:
                return "ERROR";
        }
        return "";
    }
}
