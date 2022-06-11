package de.d3rhase.interfaces;

import de.d3rhase.constants.LogColors;
import de.d3rhase.constants.LogTypes;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Logger {

    protected static final String DEFAULT_LOG_DIR = "logs/";
    protected static final String DEFAULT_LOG_NAME = "TOPIC";
    protected static final boolean DEFAULT_DELETE_EXISTING_LOGS = false;

    /**
     * Adding an ok entry to the log
     * @param module Entry module
     * @param text Entry text
     * @param printToConsole Print the entry to the console
     */
    public abstract void ok(String module, String text, boolean printToConsole);
    /**
     * Adding an ok entry to the log
     * @param module Entry module
     * @param text Entry text
     */
    public abstract  void ok(String module, String text);

    /**
     * Adding an info entry to the log
     * @param module Entry module
     * @param text Entry text
     * @param printToConsole Print the entry to the console
     */
    public abstract  void info(String module, String text, boolean printToConsole);
    /**
     * Adding an info entry to the log
     * @param module Entry module
     * @param text Entry text
     */
    public abstract  void info(String module, String text);

    /**
     * Adding a debug entry to the log
     * @param module Entry module
     * @param text Entry text
     * @param printToConsole Print the entry to the console
     */
    public abstract  void debug(String module, String text, boolean printToConsole);
    /**
     * Adding a debug entry to the log
     * @param module Entry module
     * @param text Entry text
     */
    public abstract  void debug(String module, String text);

    /**
     * Adding a warning entry to the log
     * @param module Entry module
     * @param text Entry text
     * @param printToConsole Print the entry to the console
     */
    public abstract  void warning(String module, String text, boolean printToConsole);
    /**
     * Adding a warning entry to the log
     * @param module Entry module
     * @param text Entry text
     */
    public abstract  void warning(String module, String text);

    /**
     * Adding an error entry to the log
     * @param module Entry module
     * @param text Entry text
     * @param printToConsole Print the entry to the console
     */
    public abstract  void error(String module, String text, boolean printToConsole);
    /**
     * Adding an error entry to the log
     * @param module Entry module
     * @param text Entry text
     */
    public abstract  void error(String module, String text);

    /**
     * Saving the current log to the log-file
     */
    public abstract  void saveLog();

    protected static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

    protected static String getDateTimeUsFormat(LocalDateTime dateTime) {
        String rawTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String[] splitTime = rawTime.split("T");
        rawTime = (splitTime[0] + "__" + splitTime[1]);
        splitTime = rawTime.split(":");
        rawTime = (splitTime[0] + "-" + splitTime[1] + "-" + splitTime[2]);
        return rawTime;
    }

    protected static String getDateTimePFormat (LocalDateTime dateTime) {
        String rawTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String[] splitTime = rawTime.split("-");
        rawTime = (splitTime[0] + "." + splitTime[1] + "." + splitTime[2]);
        splitTime = rawTime.split("T");
        rawTime = (splitTime[0] + " - " + splitTime[1]);
        return rawTime;
    }

    protected static String getDate(){
        LocalDateTime dateTime = getDateTime();
        return getDateTimeUsFormat(dateTime).substring(0, 23);
    }

    protected static void createLogDirectory(String dir){
        if (!Files.exists(Path.of(dir))) {
            try {
                new File(dir).mkdirs();
            } catch (SecurityException ignored) {
            }
        }
    }

    protected static void deleteExistingLogs(String logDir, String logTitle, String logCategory, boolean delete){
        if (delete) {
            File dir = new File(logDir);
            try {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.getName().contains(logTitle + logCategory)) {
                        file.delete();
                    }
                }
            } catch (NullPointerException | SecurityException ignored) {
            }
        }
    }

    protected String validateDir(String dir){
        if (dir.length() > 0) {
            if (dir.startsWith("/")) {
                dir = dir.replaceFirst("/","");
            }
            if (dir.contains("\\") || dir.contains(":") || dir.contains("*") || dir.contains("?") || dir.contains("\"") || dir.contains("<") || dir.contains(">") || dir.contains("|")){
                dir = dir.replace("\\", "");
                dir = dir.replace(":", "");
                dir = dir.replace("*", "");
                dir = dir.replace("?", "");
                dir = dir.replace("\"", "");
                dir = dir.replace("<", "");
                dir = dir.replace(">", "");
                dir = dir.replace("|", "");
            }
            if(dir.contains("//")){
                dir = dir.replace("//","/");
            }
            if(!dir.endsWith("/")){
                dir = (dir + "/");
            }
            return dir;
        }
        return DEFAULT_LOG_DIR;
    }

    protected static String createTxtEntry(String module, String text, String logType, LocalDateTime dateTime){
        return (LogTypes.getLogTypeString(logType) + " - " + getDateTimePFormat(dateTime) + " - " + module + " - " + text);
    }

    protected static JSONObject createJsonEntry(String module, String text, String type, LocalDateTime dateTime){
        JSONObject entry = new JSONObject();
        entry.put("type", LogTypes.getLogTypeString(type));
        entry.put("time", getDateTimeUsFormat(dateTime));
        entry.put("module",module);
        entry.put("text",text);
        return entry;
    }

    protected void clearTxtFile(String path) {
        if (Files.exists(Path.of(path))) {
            try {
                PrintWriter writer = new PrintWriter(path);
                writer.print("");
                writer.close();
            } catch (SecurityException | FileNotFoundException ignored) {
            }
        }
    }

    protected static String createTxtTitleEntry(String logTitle, String logCategory, String date){
        return ("---- " + logCategory + " - " + logTitle + " - Date: " + date + " ----\n");
    }

    protected static JSONObject createJsonTitleEntry(String logTitle, String logCategory, String date){
        JSONObject entry = new JSONObject();
        entry.put("category", logCategory);
        entry.put("date", date);
        entry.put("title", logTitle);
        return entry;
    }

    protected static String createTxtConsoleEntry(String module, String text, LocalDateTime dateTime){
        return (" - " + getDateTimePFormat(dateTime) + " - " + module + " - " + text);
    }

    protected static String createConsoleEntry(String module, String text, String logType, LocalDateTime dateTime) {
        switch (logType) {
            case LogTypes.OK:
                return (LogColors.OKGREEN + LogColors.BOLD + "OK" + LogColors.ENDC + createTxtConsoleEntry(module, text, dateTime));
            case LogTypes.INFO:
                return(LogColors.INFO + LogColors.BOLD + "INFO" + LogColors.ENDC + createTxtConsoleEntry(module, text, dateTime));
            case LogTypes.DEBUG:
                return (LogColors.OKCYAN + LogColors.BOLD + "DEBUG" + LogColors.ENDC + createTxtConsoleEntry(module, text, dateTime));
            case LogTypes.WARNING:
                return (LogColors.WARNING + LogColors.BOLD + "WARNING" + LogColors.ENDC + createTxtConsoleEntry(module, text, dateTime));
            case LogTypes.ERROR:
                return (LogColors.FAIL + LogColors.BOLD + "ERROR" + LogColors.ENDC + LogColors.FAIL + createTxtConsoleEntry(module, text, dateTime) + LogColors.ENDC);
        }
        return "";
    }

    protected static void printConsoleEntry(String entry, boolean print) {
        if (print) {
            System.out.println(entry);
        }

    }
}
