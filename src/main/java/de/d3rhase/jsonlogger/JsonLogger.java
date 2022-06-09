package de.d3rhase.jsonlogger;

import de.d3rhase.constants.LogColors;
import de.d3rhase.interfaces.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JsonLogger implements Logger {


    private static final String DEFAULT_LOG_DIR = "logs";
    private static final String DEFAULT_LOG_NAME = "TOPIC";
    private static final boolean DEFAULT_DELETE_EXISTING_LOGS = false;

    private final JSONArray logQueue;
    private boolean writingToLogFile;
    private String path;
    private String logDir;


    public JsonLogger(String logTitle, boolean deleteExistingLogs) {
        this(logTitle, deleteExistingLogs, DEFAULT_LOG_DIR);
    }

    public JsonLogger(String logTitle) {
        this(logTitle, DEFAULT_DELETE_EXISTING_LOGS, DEFAULT_LOG_DIR);
    }

    public JsonLogger(String logTitle, boolean deleteExistingLogs, String logDir) {
        this.logQueue = new JSONArray();
        this.writingToLogFile = false;
        this.logDir = logDir;

        String date = getDateTime();
        date = date.substring(0,23);

        path = (this.logDir + "/" + logTitle + "_log-" + date + ".json");

        if (!Files.exists(Path.of(this.logDir))) {
            try {
                new File(this.logDir).mkdir();
            } catch (SecurityException ignored) {
            }
        }

        if (deleteExistingLogs) {
            File dir = new File(this.logDir);
            try {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.getName().contains(logTitle + "_log")) {
                        file.delete();
                    }
                }
            } catch (NullPointerException | SecurityException ignored) {
            }
        }

        JSONObject titleEntry = new JSONObject();
        titleEntry.put("type", "JsonLOG");
        titleEntry.put("title", logTitle);
        titleEntry.put("date", date);
        this.logQueue.put(titleEntry);
        Thread writeThread = getWriteThread();
        writeThread.start();

        //this.testLogger(); // Test
    }

    private void append(JSONObject entry) {
        synchronized (this.logQueue) {
            this.logQueue.put(entry);
        }
        Thread writeThread = getWriteThread();
        if (!this.writingToLogFile) {
            writeThread.start();
        }
    }

    private Thread getWriteThread() {
        return new Thread(() -> {
            this.writingToLogFile = true;
            try {
                synchronized (this.logQueue) {
                    PrintWriter fileWriter = new PrintWriter(this.path);
                    fileWriter.write(this.logQueue.toString(4));
                    fileWriter.close();
                }
            } catch (IOException ignored) {
            }
            this.writingToLogFile = false;
        });
    }

    private static String getDateTime() {
        LocalDateTime time = LocalDateTime.now();
        String rawTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String[] splitTime = rawTime.split("T");
        rawTime = (splitTime[0] + "__" + splitTime[1]);
        splitTime = rawTime.split(":");
        rawTime = (splitTime[0] + "-" + splitTime[1] + "-" + splitTime[2]);
        return rawTime;
    }

    public void ok(String module, String text, boolean printToConsole) {
        JSONObject entry = new JSONObject();
        entry.put("type","OK");
        entry.put("time",getDateTime());
        entry.put("module",module);
        entry.put("text",text);
        this.append(entry);
        if (printToConsole) {
            System.out.println(LogColors.OKGREEN + LogColors.BOLD + "OK" + LogColors.ENDC + " - " + getTxtEntry(module, text));
        }
    }

    public void ok(String module, String text) {
        this.ok(module, text, true);
    }

    public void info(String module, String text, boolean printToConsole) {
        JSONObject entry = new JSONObject();
        entry.put("type","INFO");
        entry.put("time",getDateTime());
        entry.put("module",module);
        entry.put("text",text);
        this.append(entry);
        if (printToConsole) {
            System.out.println(LogColors.INFO + LogColors.BOLD + "INFO" + LogColors.ENDC + " - " + getTxtEntry(module, text));
        }
    }

    public void info(String module, String text) {
        this.info(module, text, true);
    }

    public void debug(String module, String text, boolean printToConsole) {
        JSONObject entry = new JSONObject();
        entry.put("type","DEBUG");
        entry.put("time",getDateTime());
        entry.put("module",module);
        entry.put("text",text);
        this.append(entry);
        if (printToConsole) {
            System.out.println(LogColors.OKCYAN + LogColors.BOLD + "DEBUG" + LogColors.ENDC + " - " + getTxtEntry(module, text));
        }
    }

    public void debug(String module, String text) {
        this.debug(module, text, true);
    }

    public void warning(String module, String text, boolean printToConsole) {
        JSONObject entry = new JSONObject();
        entry.put("type","WARNING");
        entry.put("time",getDateTime());
        entry.put("module",module);
        entry.put("text",text);
        this.append(entry);
        if (printToConsole) {
            System.out.println(LogColors.WARNING + LogColors.BOLD + "WARNING" + LogColors.ENDC + " - " + getTxtEntry(module, text));
        }
    }

    public void warning(String module, String text) {
        this.warning(module, text, true);
    }

    public void error(String module, String text, boolean printToConsole) {
        JSONObject entry = new JSONObject();
        entry.put("type","ERROR");
        entry.put("time",getDateTime());
        entry.put("module",module);
        entry.put("text",text);
        this.append(entry);
        if (printToConsole) {
            System.out.println(LogColors.FAIL + LogColors.BOLD + "ERROR" + LogColors.ENDC + LogColors.FAIL + " - " + getTxtEntry(module, text));
        }
    }

    public void error(String module, String text) {
        this.error(module, text, true);
    }

    @Override
    public void saveLog() {

    }

    private String getTxtEntry(String module, String text){
        return (getDateTime() + " - " + module + " - " + text);
    }

    private void testLogger() {
        this.ok("TEST", "Text");
        this.info("TEST", "Text");
        this.debug("TEST", "Text");
        this.warning("TEST", "Text");
        this.error("TEST", "Text");
    }
}
